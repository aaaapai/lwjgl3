/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import org.lwjgl.system.*;

import java.lang.classfile.*;
import java.lang.constant.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import static java.lang.classfile.ClassFile.*;
import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.ffm.BytecodeUtil.*;
import static org.lwjgl.system.ffm.BindingGenerator.*;
import static org.lwjgl.system.ffm.Call.FeatureFlag.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

final class CallDown extends Call {

    private final Method      method;
    private final Parameter[] parameters;

    private final boolean hasFunctionAddress;

    private final String nativeName;

    private final int featureFlags;

    private final Class<?> allocatorClass;

    private final Linker.Option captureCallState;

    private final FunctionDescriptor descriptor;

    private final MethodHandle ffm;

    CallDown(MethodHandles.Lookup lookup, int methodIndex) {
        super(lookup);

        try {
            method = classDataAt(lookup, DEFAULT_NAME, Method[].class, 1)[methodIndex];
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        parameters = method.getParameters();

        hasFunctionAddress = getFunctionAddress() != null;
        nativeName = hasFunctionAddress ? null : getNativeName(method);

        Class<?>      allocatorClass   = null;
        Linker.Option captureCallState = null;

        var hasTracing   = config.traceConsumer() != null && (config.tracingFilter() == null || config.tracingFilter().test(method));
        var featureFlags = hasTracing ? FF_TRACING.mask : 0;

        var argLayouts = new ArrayList<MemoryLayout>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];

            if (parameter.getType() == MemorySegment.class) {
                if (i == 0 && hasFunctionAddress) {
                    if (DEBUG && Arrays.stream(parameter.getAnnotations()).anyMatch(it -> "org.lwjgl.system.ffm".equals(it.annotationType().getPackage().getName()))) {
                        throw new IllegalStateException("FFMFunctionAddress parameters cannot have FFM annotations.");
                    }
                    continue;
                }

                var ccs = parameter.getAnnotation(FFMCaptureCallState.class);
                if (ccs != null) {
                    if (i != (hasFunctionAddress ? 1 : 0) + (allocatorClass != null ? 1 : 0)) {
                        throw new IllegalStateException("Invalid position of FFMCaptureCallState parameter.");
                    }

                    captureCallState = Linker.Option.captureCallState(ccs.value());
                    continue;
                }
            } else if (i == 0 && hasFunctionAddress) {
                throw new IllegalStateException("Missing FFMFunctionAddress parameter.");
            }

            if (SegmentAllocator.class.isAssignableFrom(parameter.getType())) {
                if (i != (hasFunctionAddress ? 1 : 0)) {
                    throw new IllegalStateException("Invalid position of SegmentAllocator/Arena parameter.");
                }
                allocatorClass = parameter.getType();
                // TODO: if a struct is returned or passed by-value?
                continue;
            }

            var type = parameter.getType();
            if (isPointerType(parameter, type)) {
                if (CHECKS && parameter.getAnnotation(FFMNullable.class) == null) {
                    featureFlags |= FF_CHECK.mask; // requires null check
                }
                if (Platform.getArchitecture().is32Bit() && type == long.class) {
                    featureFlags |= FF_POINTER32.mask;
                }
            } else if (type == String.class) {
                featureFlags |= FF_STACK.mask;
            } else if (type == boolean.class) {
                if (parameter.getAnnotation(FFMBooleanInt.class) != null) {
                    featureFlags |= FF_BOOLEAN_INT.mask;
                }
            } else if (needsMapper(type)) {
                if (allocatorClass == null) {
                    throw new IllegalStateException("A SegmentAllocator/Arena parameter is required for parameter #" + i + ": " + type);
                }

                featureFlags |= FF_MAPPER.mask;
                argLayouts.add(ValueLayout.ADDRESS);
                continue;
            }

            argLayouts.add(valueLayout(parameter));
        }

        this.allocatorClass = allocatorClass;
        this.captureCallState = captureCallState;

        MemoryLayout resLayout = null;

        var type = method.getReturnType();
        if (type != void.class) {
            var returnAnnotation = method.getAnnotation(FFMReturn.class);

            if (type == String.class || returnAnnotation != null) {
                featureFlags |= FF_STACK.mask;
            } else if (type == boolean.class) {
                if (method.getAnnotation(FFMBooleanInt.class) != null) {
                    featureFlags |= FF_BOOLEAN_INT.mask;
                }
            } else if (Platform.getArchitecture().is32Bit() && type == long.class && method.getAnnotation(FFMPointer.class) != null) {
                featureFlags |= FF_POINTER32.mask;
            }

            if (returnAnnotation != null) {
                var returnOutputAnnotation = method.getAnnotation(FFMReturn.SizeOut.class);

                // TODO: protect against multiple Size annotations
                // TODO: validate Size parameter type
                if (returnOutputAnnotation == null) {
                    for (var parameter : parameters) {
                        if (parameter.getAnnotation(FFMReturn.Size.class) != null) {
                            resLayout = valueLayout(parameter);
                        }
                    }
                }
                injectReturnParameters(argLayouts, returnAnnotation, returnOutputAnnotation);
            } else {
                resLayout = valueLayout(method, type);
            }
        }

        this.featureFlags = featureFlags;

        var argLayoutsArray = argLayouts.toArray(MemoryLayout[]::new);
        this.descriptor = resLayout == null
            ? FunctionDescriptor.ofVoid(argLayoutsArray)
            : FunctionDescriptor.of(resLayout, argLayoutsArray);

        this.ffm = Linker.nativeLinker()
            .downcallHandle(descriptor, createOptions());
    }

    private FFMFunctionAddress getFunctionAddress() {
        var annotation = method.getAnnotation(FFMFunctionAddress.class);
        if (annotation != null) {
            return annotation;
        }
        return method.getDeclaringClass().getAnnotation(FFMFunctionAddress.class);
    }

    private static void injectReturnParameters(ArrayList<MemoryLayout> argLayouts, FFMReturn returnAnnotation, FFMReturn.SizeOut returnSizeOutAnnotation) {
        if (returnSizeOutAnnotation != null && returnSizeOutAnnotation.value() < returnAnnotation.value()) {
            injectReturnParameter(argLayouts, returnSizeOutAnnotation.value(), "Invalid @FFMReturn.SizeOut parameter index: ");
        }
        injectReturnParameter(argLayouts, returnAnnotation.value(), "Invalid return parameter index: ");
        if (returnSizeOutAnnotation != null && returnAnnotation.value() < returnSizeOutAnnotation.value()) {
            injectReturnParameter(argLayouts, returnSizeOutAnnotation.value(), "Invalid @FFMReturn.SizeOut parameter index: ");
        }
    }
    private static void injectReturnParameter(ArrayList<MemoryLayout> argLayouts, int injectIndex, String errorMessage) {
        if (injectIndex < 0 || argLayouts.size() < injectIndex) {
            throw new IllegalArgumentException(errorMessage + injectIndex);
        }
        argLayouts.add(injectIndex, C_POINTER);
    }

    private static String getNativeName(Method method) {
        var nativeName = method.getAnnotation(FFMName.class);

        String name;
        if (nativeName != null) {
            name = nativeName.value();
        } else {
            name = method.getName();

            var nativePrefix = method.getDeclaringClass().getAnnotation(FFMPrefix.class);
            if (nativePrefix != null && method.getAnnotation(FFMNoPrefix.class) == null) {
                name = nativePrefix.value() + name;
            }
        }
        return name;
    }

    private Linker.Option[] createOptions() {
        var options = new ArrayList<Linker.Option>(2);

        addCritical(options);

        if (captureCallState != null) {
            options.add(captureCallState);
        }

        return options.toArray(Linker.Option[]::new);
    }

    private void addCritical(ArrayList<Linker.Option> options) {
        var override = config.criticalOverride() == null
            ? null
            : config.criticalOverride().apply(method);

        // check if forcibly disabled
        if (override != null && !override) {
            return;
        }

        // get annotation
        FFMCritical annotation = method.getAnnotation(FFMCritical.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(FFMCritical.class);
        }

        // check if forcibly enabled or annotation present
        if (override != null || annotation != null) {
            // even when forcibly enabled, get allowHeapAccess from annotation
            options.add(Linker.Option.critical(annotation != null && annotation.value()));
        }
    }

    MethodHandle bootstrap(boolean debugGenerator) {
        if (debugGenerator) {
            printDebug(method, parameters, descriptor);
        }

        if (featureFlags != 0) {
            return bootstrapWrapper(debugGenerator);
        }

        // No wrapper needed
        return hasFunctionAddress
            ? ffm
            : ffm.bindTo(config.lookup().find(nativeName).orElseThrow());
    }

    private MethodHandle bootstrapWrapper(boolean debugGenerator) {
        // Wrap the FFM method handle call in a generated method with the same signature as the bootstrapped method.
        // This is required when the native function is not compatible with the bootstrapped method and transformations must be applied.
        if (debugGenerator) {
            apiLog("\t-> generating wrapper method");
        }

        var classData = getClassData();

        var thisClass = getClassDescWrapper(method);
        var bytecode = ClassFile.of().build(thisClass, classBuilder -> {
            startHiddenClass(classBuilder);

            var nativeMethodTypeDesc = ffm.type().describeConstable().orElseThrow();
            if (hasFeature(FF_TRACING)) {
                trace(classBuilder, nativeMethodTypeDesc);
            }

            var methodTypeDesc = getMethodTypeDesc(method);

            classBuilder
                .withMethod(method.getName(), methodTypeDesc, ACC_PUBLIC | ACC_STATIC, mb -> mb.withCode(cb -> {
                    var virtualParameterCount = getVirtualParameterCount();

                    if (CHECKS) {
                        // Generate checks
                        for (var p = virtualParameterCount; p < methodTypeDesc.parameterCount(); p++) {
                            var parameter = parameters[p];
                            var type      = parameter.getType();
                            if (isPointerType(parameter, type) && parameter.getAnnotation(FFMNullable.class) == null) {
                                var slot = cb.parameterSlot(p);

                                Opcode ifThenOpcode;
                                if (type == MemorySegment.class) {
                                    cb
                                        .getstatic(CD_MemorySegment, "NULL", CD_MemorySegment)
                                        .aload(slot)
                                        .invokeinterface(CD_MemorySegment, "equals", MTD_MemorySegment_equals);
                                    ifThenOpcode = Opcode.IFNE;
                                } else if (type == long.class) {
                                    cb
                                        .lconst_0()
                                        .loadLocal(TypeKind.LONG, slot)
                                        .lcmp();
                                    ifThenOpcode = Opcode.IFEQ;
                                } else {
                                    // String will throw in allocateFrom
                                    // TODO: add more types
                                    throw new UnsupportedOperationException();
                                }

                                var exceptionText = getExceptionTextNULL(parameter, p - virtualParameterCount);
                                cb.ifThen(ifThenOpcode, thenHandler -> thenHandler
                                    .new_(CD_IllegalArgumentException)
                                    .dup()
                                    .ldc(exceptionText)
                                    .invokespecial(CD_IllegalArgumentException, INIT_NAME, MTD_IllegalArgumentException_new)
                                    .athrow());
                            }
                        }
                    }

                    var allocatorSlot = hasFeature(FF_STACK) ? getStackSlot(cb) : -1;
                    buildMethodBody(cb, methodTypeDesc, allocatorSlot, bcb -> {
                        ReturnTransform returnTransform = null;

                        var returnAnnotation = method.getAnnotation(FFMReturn.class);
                        if (returnAnnotation != null) {
                            returnTransform = ReturnTransform.create(
                                bcb,
                                methodTypeDesc, method, parameters,
                                returnAnnotation,
                                allocatorSlot
                            );
                        }

                        // ldc the target handle
                        if (!hasFeature(FF_TRACING)) {
                            // Load target handle via Condy, using MethodHandles::classData as the bootstrap method.
                            cb.ldc(condyCDataAt(CD_MethodHandle, 0));
                        }
                        if (!hasFunctionAddress) {
                            // Pass the function address manually, to avoid bindTo's additional lambda form.
                            cb.ldc(condyCDataAt(CD_MemorySegment, 1));
                        }
                        // Pass arguments, transform if necessary
                        var firstNativeParameterIndex = getFirstNativeParameterIndex();
                        for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                            var parameter = parameters[p];
                            var type      = parameter.getType();

                            if (SegmentAllocator.class.isAssignableFrom(type)) {
                                // TODO: unless there is a struct value returned or passed by-value
                                continue;
                            }

                            if (returnTransform != null) {
                                returnTransform.loadParameters(bcb, virtualParameterCount, p);
                            }

                            var slot = bcb.parameterSlot(p);
                            if (type == String.class) {
                                if (parameter.getAnnotation(FFMNullable.class) != null) {
                                    bcb
                                        .aload(slot)
                                        .ifThenElse(
                                            Opcode.IFNULL,
                                            thenHandler -> thenHandler.getstatic(CD_MemorySegment, "NULL", CD_MemorySegment),
                                            elseHandler -> buildAllocateFrom(elseHandler, allocatorSlot, slot, parameter)
                                        );
                                } else {
                                    buildAllocateFrom(bcb, allocatorSlot, slot, parameter);
                                }
                            } else if (Platform.getArchitecture().is32Bit() && type == long.class && parameter.getAnnotation(FFMPointer.class) != null) {
                                // TODO: test
                                bcb.lload(slot);
                                buildPointer64to32(bcb);
                            } else if (needsMapper(type)) {
                                var mapper = typeMap.get(type);
                                if (mapper.getReturnType() == UpcallMapper.class) {
                                    if (parameter.getAnnotation(FFMNullable.class) != null) {
                                        bcb
                                            .aload(slot)
                                            .ifThenElse(Opcode.IFNULL,
                                                b0 -> b0.getstatic(CD_MemorySegment, "NULL", CD_MemorySegment),
                                                b1 -> buildUpcallMapperAllocation(b1, classData, mapper, slot)
                                            );
                                    } else {
                                        buildUpcallMapperAllocation(bcb, classData, mapper, slot);
                                    }
                                } else {
                                    // TODO: structs & unions
                                    throw new UnsupportedOperationException();
                                }
                            } else {
                                // FFMBooleanInt is handled implicitly, boolean parameters use iload anyway
                                bcb.loadLocal(TypeKind.from(methodTypeDesc.parameterType(p)), slot);
                            }
                        }
                        if (returnTransform != null) {
                            returnTransform.loadParametersTail(bcb, virtualParameterCount, methodTypeDesc.parameterCount());
                        }

                        // Invoke the FFM method handle
                        var booleanInt = method.getAnnotation(FFMBooleanInt.class);
                        if (hasFeature(FF_TRACING)) {
                            bcb.invokestatic(thisClass, "trace", nativeMethodTypeDesc);
                        } else {
                            bcb.invokevirtual(CD_MethodHandle, "invokeExact", nativeMethodTypeDesc);
                        }
                        var type = method.getReturnType();
                        if (type != void.class) {
                            // Return result if non-void, transform if necessary
                            if (returnTransform != null) {
                                returnTransform.buildResult(bcb, methodTypeDesc, method);
                            } else if (type == String.class) {
                                // Native function returns null-terminated string
                                buildGetString(bcb, method);
                            } else if (booleanInt != null) {
                                if (!booleanInt.binary()) {
                                    bcb.ifThenElse(Opcode.IFEQ, CodeBuilder::iconst_0, CodeBuilder::iconst_1);
                                }
                            } else if (Platform.getArchitecture().is32Bit() && type == long.class && method.getAnnotation(FFMPointer.class) != null) {
                                // TODO: test
                                buildPointer32to64(bcb);
                            }
                        }
                    });
                }));
        });

        if (debugGenerator) {
            printModel(ClassFile.of().parse(bytecode));
        }

        // Store the target handle as class data in the hidden class.
        try {
            var wrapperLookup = lookup.defineHiddenClassWithClassData(bytecode, classData, true);
            return wrapperLookup.findStatic(
                wrapperLookup.lookupClass(),
                method.getName(),
                methodType(method.getReturnType(), method.getParameterTypes())
            );
        } catch (Error | RuntimeException e) {
            printModel(ClassFile.of().parse(bytecode));
            throw e;
        } catch (Exception e) {
            printModel(ClassFile.of().parse(bytecode));
            throw new RuntimeException(e);
        }
    }

    private void buildMethodBody(CodeBuilder cb, MethodTypeDesc methodTypeDesc, int allocatorSlot, Consumer<CodeBuilder> bodyHandler) {
        var returnTK = TypeKind.from(methodTypeDesc.returnType());
        if (allocatorSlot != -1 && (allocatorClass == null || StackAllocator.class.isAssignableFrom(allocatorClass))) {
            buildStackBody(cb, returnTK, allocatorSlot, bodyHandler);
        } else {
            bodyHandler.accept(cb);
        }
        cb.return_(returnTK);
    }

    private void buildStackBody(CodeBuilder cb, TypeKind returnTK, int stackSlot, Consumer<CodeBuilder> arenaTryHandler) {
        var returnSlot = returnTK == TypeKind.VOID ? -1 : cb.allocateLocal(returnTK);

        if (allocatorClass == null || !StackAllocator.class.isAssignableFrom(allocatorClass)) {
            // try (var stack = stackPush()) {
            cb
                .invokestatic(CD_StackArena, "stackPush", MTD_StackArena_stackPush)
                .astore(stackSlot);
        } else {
            // stack.push();
            cb
                .aload(cb.parameterSlot(hasFunctionAddress ? 1 : 0))
                .invokeinterface(CD_StackAllocator, "push", MTD_StackAllocator_push)
                .pop();
        }
        cb.trying(
                tryingHandler -> {
                    arenaTryHandler.accept(tryingHandler);
                    if (returnTK != TypeKind.VOID) {
                        tryingHandler.storeLocal(returnTK, returnSlot);
                    }
                },
                catchesHandler -> catchesHandler.catchingAll(bcb0 -> bcb0
                    .astore(stackSlot + 1)
                    .trying(
                        finallyTryHandler -> finallyTryHandler
                            // finally, pop the stack
                            .aload(stackSlot)
                            .invokeinterface(CD_StackAllocator, "pop", MTD_StackAllocator_pop)
                            .pop(),
                        suppressedCatchesHandler -> suppressedCatchesHandler.catchingAll(bcb1 -> bcb1
                            // exception in pop, add to suppressed list
                            .astore(stackSlot + 2)
                            .aload(stackSlot + 1)
                            .aload(stackSlot + 2)
                            .invokevirtual(CD_Throwable, "addSuppressed", MTD_Throwable_addSuppressed))
                    )
                    // rethrow original exception
                    .aload(stackSlot + 1)
                    .athrow()
                )
            )
            // finally, pop the stack
            .aload(stackSlot)
            .invokeinterface(CD_StackAllocator, "pop", MTD_StackAllocator_pop)
            .pop();
        if (returnTK != TypeKind.VOID) {
            cb.loadLocal(returnTK, returnSlot);
        }
    }

    private void trace(ClassBuilder classBuilder, MethodTypeDesc nativeMethodTypeDesc) {
        classBuilder.withMethod("trace", nativeMethodTypeDesc, ACC_PRIVATE | ACC_STATIC, mb -> mb.withCode(cb -> {
            // Load target handle via Condy, using MethodHandles::classDataAt as the bootstrap method.
            cb.ldc(condyCDataAt(CD_MethodHandle, 0));
            for (var p = 0; p < nativeMethodTypeDesc.parameterCount(); p++) {
                cb.loadLocal(
                    TypeKind.from(nativeMethodTypeDesc.parameterType(p)),
                    cb.parameterSlot(p)
                );
            }
            cb.invokevirtual(CD_MethodHandle, "invokeExact", nativeMethodTypeDesc);

            int returnSlot;
            var returnTK = TypeKind.from(nativeMethodTypeDesc.returnType());
            if (returnTK != TypeKind.VOID) {
                returnSlot = cb.allocateLocal(returnTK);
                cb.storeLocal(returnTK, returnSlot);
            } else {
                returnSlot = -1;
            }

            var consumerIndex = hasFunctionAddress ? 1 : 2;
            cb
                // Load TraceConsumer via Condy
                .ldc(condyCDataAt(CD_TraceConsumer, consumerIndex))
                // Load Method via Condy
                .ldc(condyCDataAt(CD_Method, consumerIndex + 1));
            if (returnTK == TypeKind.VOID) {
                cb.aconst_null();
            } else {
                cb.loadLocal(returnTK, returnSlot);
                if (returnTK != TypeKind.REFERENCE) {
                    boxPrimitiveValue(cb, returnTK);
                }
            }

            cb
                .loadConstant(nativeMethodTypeDesc.parameterCount())
                .anewarray(CD_Object);
            for (var p = 0; p < nativeMethodTypeDesc.parameterCount(); p++) {
                var tk = TypeKind.from(nativeMethodTypeDesc.parameterType(p));

                cb
                    .dup()
                    .loadConstant(p)
                    .loadLocal(tk, cb.parameterSlot(p));
                if (tk != TypeKind.REFERENCE) {
                    boxPrimitiveValue(cb, tk);
                }
                cb.aastore();
            }
            cb.invokeinterface(CD_TraceConsumer, "accept", MTD_TraceConsumer_accept);

            // Return
            if (returnTK != TypeKind.VOID) {
                cb.loadLocal(returnTK, returnSlot);
            }
            cb.return_(returnTK);
        }));
    }

    private boolean hasFeature(FeatureFlag flag) {
        return flag.isSet(featureFlags);
    }

    private int getVirtualParameterCount() {
        var index = 0;
        if (hasFunctionAddress) {
            index++;
        }
        if (allocatorClass != null) {
            index++;
        }
        if (captureCallState != null) {
            index++;
        }
        return index;
    }

    private int getFirstNativeParameterIndex() {
        var index = 0;
        if (hasFunctionAddress) {
            index++;
        }
        if (allocatorClass != null) {
            // TODO: only if there is a struct value returned or passed by-value
        }
        if (captureCallState != null) {
            index++;
        }
        return index;
    }

    private int getStackSlot(CodeBuilder cb) {
        return allocatorClass != null
            ? cb.parameterSlot(hasFunctionAddress ? 1 : 0)
            : cb.allocateLocal(TypeKind.REFERENCE);
    }

    private int getArenaSlot(CodeBuilder cb) {
        // TODO: add support for StackArena + non-StackArena for upcalls
        if (allocatorClass == null) {
            throw new IllegalStateException();
        }
        return cb.parameterSlot(hasFunctionAddress ? 1 : 0);
    }

    private List<Object> getClassData() {
        var list = new ArrayList<>();

        list.add(ffm);

        if (!hasFunctionAddress) {
            list.add(config.lookup().find(nativeName).orElseThrow());
        }

        if (hasFeature(FF_TRACING)) {
            list.add(config.traceConsumer());
            list.add(method);
        }

        if (hasFeature(FF_MAPPER)) {
            list.add(config.mappers());
        }

        return list;
    }

    private static void buildAllocateFrom(CodeBuilder cb, int allocatorSlot, int slot, Parameter parameter) {
        cb
            .aload(allocatorSlot)
            .aload(slot);
        buildCharsetInstance(cb, getCharset(parameter));
        cb.invokeinterface(CD_SegmentAllocator, "allocateFrom", MTD_SegmentAllocator_allocateFrom);
    }

    private void buildUpcallMapperAllocation(CodeBuilder bcb, List<Object> classData, Method mapper, int parameterSlot) {
        var mapperInterface = mapper.getDeclaringClass().describeConstable().orElseThrow();
        bcb
            .ldc(condyCDataAt(mapperInterface, classData.size() - 1))
            .invokeinterface(mapperInterface, mapper.getName(), MethodTypeDesc.of(CD_UpcallMapper))
            .aload(getArenaSlot(bcb))
            .aload(parameterSlot)
            .invokeinterface(CD_UpcallMapper, "allocate", MTD_UpcallMapper_allocate);
    }

}