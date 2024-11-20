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
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.classfile.ClassFile.*;
import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.ffm.BindingGenerator.FeatureFlag.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

/**
 * Generates LWJGL bindings from an interface, optionally annotated with {@code FFM*} annotations.
 *
 * <p>The objective is to allow users to create their own LWJGL-style bindings, dynamically at run-time, without sacrificing performance.</p>
 *
 * <p>Several modern Java technologies are being utilized to achieve this:</p>
 * <ul>
 *     <li>Dynamic Class-File Constants (JEP 309, JDK 11, a.k.a. Condy): Used to lazily initialize bindings, without precompiling a private nested class per function.</li>
 *     <li>Hidden Classes (JEP 371, JDK 15): Used to generate classes with minimal metadata and no interaction with ClassLoaders.</li>
 *     <li>Class data support for hidden classes (JDK-8256214, JDK 16): Used to boostrap Condy values.</li>
 *     <li>Class-File API (JEP 457, JDK 22, preview): Used to generate bytecode at runtime, without 3rd-party dependencies.</li>
 *     <li>Foreign Function & Memory API (JEP 442, JDK 22): Used for native interoperation without JNI.</li>
 * </ul>
 *
 * <p>The {@link #generate} method is designed to be relatively cheap. The instance returned is lightweight and its methods are never materialized, unless
 * actually used by the application. More specifically:</p>
 * <ul>
 *    <li>A minimal class is generated that implements the input interface.</li>
 *    <li>There is no state in the generated class, only methods.</li>
 *    <li>All methods are simple, with minimal bytecode. A method handle is retrieved using Condy and arguments are passed directly to {@link MethodHandle#invokeExact}.</li>
 *    <li>There is no overhead from the Condy {@code ldc} instruction. It is executed only once, if and when the method is called.</li>
 *    <li>
 *        Execution of relatively expensive binding code is deferred to the Condy bootstrap. This includes:
 *        <ul>
 *            <li>lookup of {@code FFM*} annotations and associated logic</li>
 *            <li>function address lookup and creation of FFM method handles</li>
 *            <li>further bytecode generation and wrapping of FFM method handles if necessary</li>
 *        </ul>
 *    </li>
 *    <li>
 *        Hidden class data is used:
 *        <ul>
 *            <li>to pass reflected {@link Method} instances to the Condy bootstrap</li>
 *            <li>to pass FFM {@link MethodHandle}/{@link MemorySegment} instances to generated wrapper methods</li>
 *        </ul>
 *    </li>
 * </ul>
 *
 * <p>When stored in {@code static final} fields, performance of the generated bindings is equivalent to direct FFM calls. The JVM can inline through
 * everything, for the following reasons:</p>
 * <ul>
 *     <li>The binding instance is constant.</li>
 *     <li>There is a single implementation of the binding interface.</li>
 *     <li>The {@link MethodHandle} instances created via Condy are also treated as constants by the JVM.</li>
 * </ul>
 *
 * <p>The generator DSL supports the specification of 3 virtual parameters, which must be present in a strict order before other parameters:</p>
 * <ul>
 *     <li>
 *         A {@link MemorySegment} parameter in methods annotated with {@link FFMFunctionAddress}.
 *
 *         <p>It must be specified before any other virtual or normal parameter.</p>
 *     </li>
 *     <li>
 *         A {@link SegmentAllocator} or {@link StackAllocator} parameter. This parameter will be used to allocate storage for struct values passed or returned
 *         by-value and also for temporary storage needed internally by the method call (e.g. for {@link FFMReturn} buffers).
 *
 *         <p>If the parameter is of type {@link StackAllocator}, a stack frame will be pushed & popped inside the method call when temporary storage is
 *         needed.</p>
 *
 *         <p>It must be specified after the {@link FFMFunctionAddress} parameter, if one exists, and before any other virtual or normal parameter.</p>
 *     </li>
 *     <li>
 *         A {@link MemorySegment} parameter annotated with {@link FFMCaptureCallState}.
 *
 *         <p>It must be specified after other virtual parameters and before any normal parameter.</p>
 *     </li>
 * </ul>
 */
public final class BindingGenerator {

    public static final AddressLayout C_POINTER = ValueLayout.ADDRESS
        .withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));

    private static final DirectMethodHandleDesc BSM_BOOTSTRAP = ofConstantBootstrap(
        BindingGenerator.class.describeConstable().orElseThrow(),
        "bootstrap",
        CD_MethodHandle,
        CD_int
    );

    private static final Set<String> STANDARD_CHARSETS = Arrays.stream(StandardCharsets.class.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toUnmodifiableSet());

    private BindingGenerator() {
    }

    public static <T> T generate(Lookup context, Class<T> bindingInterface)                            { return generate(context, bindingInterface, BindingConfig.builder().build()); }
    public static <T> T generate(Lookup context, Class<T> bindingInterface, FunctionProvider provider) { return generate(context, bindingInterface, BindingConfig.builder().withLookup(provider).build()); }
    public static <T> T generate(Lookup context, Class<T> bindingInterface, SymbolLookup lookup)       { return generate(context, bindingInterface, BindingConfig.builder().withLookup(lookup).build()); }
    @SuppressWarnings("unchecked")
    public static <T> T generate(Lookup context, Class<T> bindingInterface, BindingConfig config) {
        try {
            return ((Class<T>)generateImplementation(context, bindingInterface, config).lookupClass())
                .getDeclaredConstructor()
                .newInstance();
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static Lookup generateImplementation(Lookup context, Class<?> functionsClass, BindingConfig config) throws IllegalAccessException {
        var methods = functionsClass.getMethods();

        // Slot 0: loader
        // Slot 1+: reflected Method instances
        var classData = new ArrayList<>(1 + methods.length);
        classData.add(config);
        classData.addAll(Arrays.asList(methods));

        var bytecode = of().build(getClassDesc(functionsClass), classBuilder -> {
            startHiddenClass(classBuilder)
                .withInterfaceSymbols(functionsClass.describeConstable().orElseThrow());

            for (int m = 0; m < methods.length; m++) {
                var method = methods[m];

                var condy = DynamicConstantDesc.<MethodHandle>ofNamed(
                    BSM_BOOTSTRAP,
                    method.getName(),
                    CD_MethodHandle,
                    m + 1);

                var methodTypeDesc = getMethodTypeDesc(method);

                classBuilder
                    .withMethod(method.getName(), methodTypeDesc, ACC_PUBLIC, mb -> mb
                        .withCode(cb -> {
                            // Do the Condy lookup
                            cb.ldc(condy);
                            // Pass arguments unmodified
                            for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                                cb.loadLocal(TypeKind.from(methodTypeDesc.parameterType(p)), cb.parameterSlot(p));
                            }
                            // Invoke the generated method handle
                            cb
                                .invokevirtual(CD_MethodHandle, "invokeExact", methodTypeDesc)
                                // Return result if non-void, also unmodified
                                .return_(TypeKind.from(methodTypeDesc.returnType()));
                        })
                    );
            }
        });

        return context.defineHiddenClassWithClassData(bytecode, classData, false);
    }

    // The Condy boostrap method, called the first time the above LDC bytecode is executed.
    @SuppressWarnings("unused")
    public static MethodHandle bootstrap(Lookup lookup, String name, Class<?> bootstrapClass, int methodIndex) {
        var debugGenerator = Configuration.DEBUG_GENERATOR.get(false);
        if (debugGenerator) {
            apiLog("BOOTSTRAPPING #" + methodIndex + ": " + name);
        }
        try {
            var config = classDataAt(lookup, DEFAULT_NAME, BindingConfig.class, 0);
            var method = classDataAt(lookup, DEFAULT_NAME, Method.class, methodIndex);

            var parameters = method.getParameters();

            // The target handle
            var nativeHandle = new NativeHandle(config, method, parameters);
            if (debugGenerator) {
                nativeHandle.printDebug(method, parameters);
            }

            var virtualParameterCount = nativeHandle.getVirtualParameterCount();

            var hasTracing = config.traceConsumer() != null && (config.tracingFilter() == null || config.tracingFilter().test(method));
            var featureFlags = getFeatureFlags(method, parameters, virtualParameterCount) | (hasTracing ? FF_TRACING.mask : 0);
            if (featureFlags != 0) {
                // Wrap the FFM method handle call in a generated method with the same signature as the bootstrapped method.
                // This is required when the native function is not compatible with the bootstrapped method and transformations must be applied.
                if (debugGenerator) {
                    apiLog("\t-> generating wrapper method");
                }

                var thisClass = getClassDescWrapper(method);
                var bytecode = of().build(thisClass, classBuilder -> {
                    startHiddenClass(classBuilder);

                    var nativeMethodTypeDesc = nativeHandle.ffm.type().describeConstable().orElseThrow();
                    if (hasTracing) {
                        classBuilder
                            .withMethod("trace", nativeMethodTypeDesc, ACC_PRIVATE | ACC_STATIC, methodBuilder -> methodBuilder
                                .withCode(cb -> NativeHandle.trace(cb, nativeMethodTypeDesc))
                            );
                    }

                    var methodTypeDesc = getMethodTypeDesc(method);

                    classBuilder
                        .withMethod(method.getName(), methodTypeDesc, ACC_PUBLIC | ACC_STATIC, methodBuilder -> methodBuilder
                            .withCode(cb -> {
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

                                var allocatorSlot = FF_STACK.isSet(featureFlags) ? nativeHandle.getStackSlot(cb) : -1;
                                buildMethodBody(cb, methodTypeDesc, nativeHandle, allocatorSlot, bcb -> {
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
                                    nativeHandle.build(config, hasTracing, bcb);
                                    // Pass arguments, transform if necessary
                                    var firstNativeParameterIndex = nativeHandle.getFirstNativeParameterIndex();
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
                                        } else if (type == boolean.class && parameter.getAnnotation(FFMBooleanInt.class) != null) {
                                            bcb.iload(slot); // TODO: test
                                        } else if (Platform.getArchitecture().is32Bit() && type == long.class && parameter.getAnnotation(FFMPointer.class) != null) {
                                            bcb.lload(slot);
                                            bcb.ldc(0xFFFF_FFFFL);
                                            bcb.land();
                                            bcb.l2i();
                                        } else {
                                            bcb.loadLocal(TypeKind.from(methodTypeDesc.parameterType(p)), slot);
                                        }
                                    }
                                    if (returnTransform != null) {
                                        returnTransform.loadParametersTail(bcb, virtualParameterCount, methodTypeDesc.parameterCount());
                                    }

                                    // Invoke the FFM method handle
                                    var booleanInt = method.getAnnotation(FFMBooleanInt.class);
                                    if (hasTracing) {
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
                                            bcb.lconst_0();
                                            buildCharsetInstance(bcb, getCharset(method));
                                            bcb.invokeinterface(CD_MemorySegment, "getString", MTD_MemorySegment_getString);
                                        } else if (booleanInt != null) {
                                            if (!booleanInt.binary()) {
                                                bcb.ifThenElse(Opcode.IFEQ, CodeBuilder::iconst_0, CodeBuilder::iconst_1);
                                            }
                                        } else if (Platform.getArchitecture().is32Bit() && type == long.class && method.getAnnotation(FFMPointer.class) != null) {
                                            bcb.i2l();
                                        }
                                    }
                                });
                            })
                        );
                });

                if (debugGenerator) {
                    printModel(of().parse(bytecode));
                }

                // Store the target handle as class data in the hidden class.
                try {
                    var wrapperLookup = lookup.defineHiddenClassWithClassData(bytecode, nativeHandle.getClassData(config, method, hasTracing), true);
                    return wrapperLookup.findStatic(
                        wrapperLookup.lookupClass(),
                        method.getName(),
                        methodType(method.getReturnType(), method.getParameterTypes())
                    );
                } catch (Throwable t) {
                    printModel(of().parse(bytecode));
                    throw t;
                }
            }

            // No wrapper needed
            return nativeHandle.getSimpleHandle(config.lookup());
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ClassBuilder startHiddenClass(ClassBuilder classBuilder) {
        return classBuilder
            .withVersion(latestMajorVersion(), latestMinorVersion())
            .withFlags(AccessFlag.PUBLIC, AccessFlag.FINAL)
            .withSuperclass(CD_Object)
            // private <init>() { super(); }
            .withMethod(INIT_NAME, MTD_void, ACC_PUBLIC, mb -> mb
                .withCode(cb -> cb
                    .aload(cb.receiverSlot())
                    .invokespecial(CD_Object, INIT_NAME, MTD_void, false)
                    .return_()));
    }

    static enum FeatureFlag {
        /** Needs a new stack frame */
        FF_STACK,
        /** Needs integer to boolean conversion */
        FF_BOOLEAN_INT,
        /** Needs a check */
        FF_CHECK,
        /** Needs tracing */
        FF_TRACING,
        /** Needs long to int conversion */
        FF_POINTER32;

        final int mask;

        FeatureFlag() {
            this.mask = 1 << this.ordinal();
        }

        public boolean isSet(int flags) {
            return (flags & mask) != 0;
        }
    }

    private static int getFeatureFlags(Method method, Parameter[] parameters, int virtualParameterCount) {
        var wrapperFlags = 0;
        if (method.getReturnType() == String.class || method.getAnnotation(FFMReturn.class) != null) {
            wrapperFlags |= FF_STACK.mask;
        } else if (method.getReturnType() == boolean.class) {
            if (method.getAnnotation(FFMBooleanInt.class) != null) {
                wrapperFlags |= FF_BOOLEAN_INT.mask;
            }
        } else if (Platform.getArchitecture().is32Bit() && method.getReturnType() == long.class && method.getAnnotation(FFMPointer.class) != null) {
            wrapperFlags |= FF_POINTER32.mask;
        }

        for (int i = virtualParameterCount; i < parameters.length; i++) {
            var parameter = parameters[i];
            var type      = parameter.getType();
            if (isPointerType(parameter, type)) {
                if (CHECKS && parameter.getAnnotation(FFMNullable.class) == null) {
                    wrapperFlags |= FF_CHECK.mask; // requires null check
                }
                if (Platform.getArchitecture().is32Bit() && type == long.class) {
                    wrapperFlags |= FF_POINTER32.mask;
                }
            } else if (type == String.class) {
                wrapperFlags |= FF_STACK.mask;
            } else if (type == boolean.class) {
                if (parameter.getAnnotation(FFMBooleanInt.class) != null) {
                    wrapperFlags |= FF_BOOLEAN_INT.mask;
                }
            }
        }
        return wrapperFlags;
    }

    private static void buildMethodBody(CodeBuilder cb, MethodTypeDesc methodTypeDesc, NativeHandle nativeHandle, int allocatorSlot, Consumer<CodeBuilder> bodyHandler) {
        var returnTK = TypeKind.from(methodTypeDesc.returnType());
        if (allocatorSlot != -1 && (nativeHandle.allocatorClass == null || StackAllocator.class.isAssignableFrom(nativeHandle.allocatorClass))) {
            buildStackBody(cb, returnTK, nativeHandle, allocatorSlot, bodyHandler);
        } else {
            bodyHandler.accept(cb);
        }
        cb.return_(returnTK);
    }

    private static void buildStackBody(CodeBuilder cb, TypeKind returnTK, NativeHandle nativeHandle, int stackSlot, Consumer<CodeBuilder> arenaTryHandler) {
        var returnSlot = returnTK == TypeKind.VOID ? -1 : cb.allocateLocal(returnTK);

        if (nativeHandle.allocatorClass == null || !StackAllocator.class.isAssignableFrom(nativeHandle.allocatorClass)) {
            // try (var stack = stackPush()) {
            cb
                .invokestatic(CD_StackArena, "stackPush", MTD_StackArena_stackPush)
                .astore(stackSlot);
        } else {
            // stack.push();
            cb
                .aload(cb.parameterSlot(nativeHandle.hasFunctionAddress ? 1 : 0))
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

    static void buildCharsetInstance(CodeBuilder cb, FFMCharset.Type type) {
        cb.getstatic(CD_StandardCharsets, type.charset, CD_Charset);
        /*if (STANDARD_CHARSETS.contains(charsetName)) {
            cb.getstatic(CD_StandardCharsets, charsetName, CD_Charset);
        } else {
            cb
                .ldc(cb.constantPool().stringEntry(charsetName).constantValue())
                .invokestatic(CD_Charset, "forName", MTD_Charset_forName);
        }*/
    }

    static void buildCharsetShift(CodeBuilder bcb, FFMCharset.Type type, TypeKind kind) {
        if (type.byteSize == 1) {
            return;
        }

        switch (type.byteSize) {
            case 2:
                bcb.iconst_1();
                break;
            case 4:
                bcb.iconst_2();
                break;
            default:
                throw new AssertionError();
        }

        if (kind != TypeKind.LONG) {
            bcb.ishl();
        } else {
            bcb.lshl();
        }
    }

    private static void buildAllocateFrom(CodeBuilder cb, int allocatorSlot, int slot, Parameter parameter) {
        cb
            .aload(allocatorSlot)
            .aload(slot);
        buildCharsetInstance(cb, getCharset(parameter));
        cb.invokeinterface(CD_SegmentAllocator, "allocateFrom", MTD_SegmentAllocator_allocateFrom);
    }

    private static ClassDesc getClassDesc(Class<?> functionsClass) {
        return ClassDesc.of(functionsClass.getPackageName(), functionsClass.getSimpleName() + "Impl");
    }

    private static ClassDesc getClassDescWrapper(Method method) {
        var declaringClass = method.getDeclaringClass();
        return ClassDesc.of(
            declaringClass.getPackageName(),
            declaringClass.getSimpleName() + "$" + method.getName() // TODO: do we need to handle multiple overloads?
        );
    }

    private static ClassDesc getClassDescNative(Class<?> type) {
        Class<?> nativeType;
        if (type == String.class) {
            nativeType = MemorySegment.class;
        } else {
            nativeType = type;
        }
        return nativeType.describeConstable().orElseThrow();
    }

    private static MethodTypeDesc getMethodTypeDesc(Method method) {
        return MethodTypeDesc.of(
            method.getReturnType().describeConstable().orElseThrow(),
            getParameterDescs(method)
        );
    }

    private static ClassDesc[] getParameterDescs(Method method) {
        var parameterTypes = method.getParameterTypes();
        var parameterDescs = new ClassDesc[parameterTypes.length];
        for (int p = 0; p < parameterTypes.length; p++) {
            parameterDescs[p] = parameterTypes[p].describeConstable().orElseThrow();
        }
        return parameterDescs;
    }

    private static ClassDesc[] getParameterDescsNative(Method method, ReturnTransform returnTransform) {
        var parameterTypes = method.getParameterTypes();
        var parameterDescs = new ClassDesc[parameterTypes.length];
        for (int p = 0; p < parameterTypes.length; p++) {
            parameterDescs[p] = getClassDescNative(parameterTypes[p]);
        }
        return parameterDescs;
    }

    private static boolean isPointerType(Parameter parameter, Class<?> type) {
        return type == MemorySegment.class || (type == long.class && parameter.getAnnotation(FFMPointer.class) != null);
    }

    private static String getParameterName(Parameter parameter, int index) {
        return parameter.isNamePresent()
            ? "<" + parameter.getName() + ">"
            : "#" + index;
    }
    private static String getExceptionTextNULL(Parameter parameter, int index) {
        return parameter.getType().getSimpleName() + " argument " + getParameterName(parameter, index) + " cannot be NULL";
    }

    static FFMCharset.Type getCharset(Method method) {
        var annotation = method.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : FFMCharset.DEFAULT;
    }

    private static FFMCharset.Type getCharset(Parameter parameter) {
        var annotation = parameter.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = parameter.getDeclaringExecutable().getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : FFMCharset.DEFAULT;
    }

    private static void printModel(CompoundElement<?> model) {
        DEBUG_STREAM.println(model);
        printModel(model, 0);
    }
    private static void printModel(CompoundElement<?> model, int depth) {
        var indent = "\t".repeat(depth);
        var bci    = 0;
        for (var el : model) {
            if (el instanceof Instruction i) {
                DEBUG_STREAM.println(indent + bci + ": " + i);
                bci += i.sizeInBytes();
            } else {
                DEBUG_STREAM.println(indent + " ".repeat(Integer.toString(bci).length()) + "* " + el);
            }
            if (el instanceof CompoundElement<?> ce) {
                printModel(ce, depth + 1);
            }
        }
    }

    // Experimental wrapper generation using the MethodHandle API.
    // It works, but is awkward (reverse order, broadcasting the StackArena to each parameter that needs it) and verbose.
    // It results in stack frames 5+ levels deep.
    // It is probably not as efficient as the bytecode generation approach above.
    /*private static MethodHandle generateMethodHandleAPI(Method method, MethodHandle nativeHandle) throws Exception {
        // T nativeCall(...);
        // T innerCall2(..., Arena stack, argN, ...) {
        //     return nativeCall(..., stack.allocateFrom(argN), ...);
        // }
        var handle = collectArguments(
            nativeHandle,
            2,
            lookup().findStatic(BindingGenerator.class, "allocateFrom", methodType(MemorySegment.class, Arena.class, String.class))
        );
        // T innerCall1(Arena stack, ...) {
        //     return innerCell2(..., stack, argN, ...);
        // }
        handle = permuteArguments(
            handle,
            methodType(MemorySegment.class, Arena.class, int.class, int.class, String.class, MemorySegment.class, MemorySegment.class),
            1, 2, 0, 3, 4, 5
        );
        // T innerCall0(Arena stack, ...) {
        //     try {
        //         return innerCall1(stack, ...);
        //     } finally {
        //         cleanup(throwable, result, stack); // stack.close() is called here
        //     }
        // }
        handle = tryFinally(
            handle,
            foldArguments(
                permuteArguments(
                    identity(method.getReturnType()),
                    methodType(method.getReturnType(), Throwable.class, method.getReturnType(), Arena.class),
                    1
                ),
                2,
                publicLookup().findVirtual(Arena.class, "close", methodType(void.class))
            )

        );
        // T wrappedCall(...) {
        //     var stack = stackPush();
        //     return innerCall0(stack, ...);
        // }
        handle = foldArguments(
            handle,
            0,
            publicLookup().findStatic(Arena.class, "ofConfined", methodType(Arena.class))
        );

        return handle;
    }*/
}

/*
public interface MyBindings {
    public void simple1();
    public void simple2(float x, float y, float z);
    public String complex(String text, long value);
    public int simple3(int foo);
}

// ON INIT: Generate exactly 1 hidden class, regardless of how many methods there are in the interface.
//
// Each method has a single LDC instruction that executes the Condy bootstrap and returns a method handle.
// The method handle is invoked with the exact same parameters as the original interface method.
//
// Each LDC instruction is basically equivalent to a "lazy static final" field, except that the value is stored
// inline (in the class constant pool) and not in a field. Benefits:
//
// 1. Achieves the laziness we require, without a holder class per method.
// 2. The generated class is extremely simple and no special logic needs to be executed.
public class MyBindings$HIDDEN_CLASS implements MyBindings {
    public void simple1() {
        MethodHandle mh = <CONDY BOOTSTRAP>
        mh.invokeExact();
    }

    public void simple2(float x, float y, float z) {
        MethodHandle mh = <CONDY BOOTSTRAP>
        mh.invokeExact(x, y, z);
    }

    public String complex(String text, long value) {
        MethodHandle mh = <CONDY BOOTSTRAP>
        return mh.invokeExact(text, value);
    }

    public int simple3(int foo) {
        MethodHandle mh = <CONDY BOOTSTRAP>
        return mh.invokeExact(foo);
    }
}

// AFTER INIT: We start calling each method. The first-time, we execute the LDC bootstrap code.
//
// For simple methods that don't require any wrapping, the method handle returned by the bootstrap code, is
// exactly the method handle created by the FFM API. There's never a need for an extra class for such methods!
//
// For complex methods (or for every method if tracing is enabled), we create a new hidden class with a single
// method that implements the wrapping logic, which calls the FFM method handle internally. The LDC instruction
// then returns a method handle to the generated wrapper method (which has the exact same parameters as the
// original method). Benefits:
//
// 1. The original hidden class does not know or care if the method handle is wrapped or not. It just calls it.
// 2. The function address lookup from the native library and the complicated logic related to wrapper methods
//    is executed lazily, on first call. Or never if the function is never used by the client.
MyBindings bindings = BindingGenerator.generate(MethodHandles.lookup(), MyBindings.class);
bindings.simple1();
bindings.simple2(0.5f, 0.5f, 0.5f);
String str = bindings.complex("foo", 42L);
int i = bindings.simple3(42);

// After bootstrapping the above calls, the class effectively becomes:
public class MyBindings$HIDDEN_CLASS implements MyBindings {
    public void simple1() {
        MethodHandle mh = <CONSTANT POOL ENTRY THAT HOLDS simple1 FFM METHOD HANDLE>;
        mh.invokeExact();
    }

    public void simple2(float x, float y, float z) {
        MethodHandle mh = <CONSTANT POOL ENTRY THAT HOLDS simple2 FFM METHOD HANDLE>;
        mh.invokeExact(x, y, z);
    }

    public String complex(String text, long value) {
        MethodHandle mh = <CONSTANT POOL ENTRY THAT HOLDS MyBindings$Complex$HIDDEN_CLASS.complex(String, long) METHOD HANDLE>;
        return mh.invokeExact(text, value);
    }

    public int simple3(int foo) {
        MethodHandle mh = <CONSTANT POOL ENTRY THAT HOLDS simple3 FFM METHOD HANDLE>;
        return mh.invokeExact(foo);
    }

    // Generated hidden class for complex method
    private static class MyBindings$Complex$HIDDEN_CLASS {
        public String complex(String text, long value) {
            MethodHandle mh = <CONSTANT POOL ENTRY THAT HOLDS complex FFM METHOD HANDLE>;

            MemorySegment textPointer = <serialize text parameter>
            MemorySegment resultPointer = mh.invokeExact(textPointer, value);

            String result = <deserialize resultPointer>
            return result;
        }
    }
}
*/
