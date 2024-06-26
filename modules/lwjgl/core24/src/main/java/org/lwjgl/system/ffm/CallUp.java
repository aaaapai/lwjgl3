/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import org.lwjgl.system.*;

import java.lang.classfile.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.classfile.ClassFile.*;
import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static org.lwjgl.system.ffm.BindingGenerator.*;
import static org.lwjgl.system.ffm.BytecodeUtil.*;
import static org.lwjgl.system.ffm.Call.FeatureFlag.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

final class CallUp extends Call {

    private final Class<?> upcallInterface;

    private final Method      method;
    private final Parameter[] parameters;

    private final int featureFlags;

    final FunctionDescriptor descriptor;

    CallUp(MethodHandles.Lookup lookup, int methodIndex) {
        super(lookup);

        Method mapperMethod;
        try {
            mapperMethod = classDataAt(lookup, DEFAULT_NAME, Method[].class, 1)[methodIndex];
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        upcallInterface = switch (((ParameterizedType)mapperMethod.getGenericReturnType()).getActualTypeArguments()[0]) {
            case ParameterizedType pt -> (Class<?>)pt.getRawType();
            case Class<?> c -> c;
            default -> throw new UnsupportedOperationException();
        };

        if (upcallInterface.getDeclaredAnnotation(FunctionalInterface.class) == null) {
            throw new UnsupportedOperationException("The upcall interface must be annotated with @FunctionalInterface");
        }

        // This is the callback method that the user implements.
        // It may contain high-level types that need to be mapped to FFM types. If true, 2 cases:
        //   - The FFM-compatible method does not exist in the interface.
        //       * Generate the FFM-compatible method and forward to this method
        //   - The FFM-compatible method exists in the interface, implemented as default.
        //       * Just use the existing method
        method = Arrays.stream(upcallInterface.getDeclaredMethods())
            .filter(m -> !m.isDefault())
            .findFirst()
            .orElseThrow(); // guaranteed to find exactly 1 with @FunctionalInterface

        parameters = method.getParameters();

        var hasTracing   = config.traceConsumer() != null && (config.tracingFilter() == null || config.tracingFilter().test(method));
        var featureFlags = hasTracing ? FF_TRACING.mask : 0;

        var argLayouts = new ArrayList<MemoryLayout>(parameters.length);
        for (Parameter parameter : parameters) {
            var type = parameter.getType();
            if (isPointerType(parameter, type)) {
                if (Platform.getArchitecture().is32Bit() && type == long.class) {
                    featureFlags |= FF_POINTER32.mask;
                }
            } else if (type == String.class) {
                featureFlags |= FF_STRING.mask;
            } else if (type == boolean.class) {
                if (parameter.getAnnotation(FFMBooleanInt.class) != null) {
                    featureFlags |= FF_BOOLEAN_INT.mask;
                }
            } else if (needsMapper(type)) {
                featureFlags |= FF_MAPPER.mask;
                argLayouts.add(ValueLayout.ADDRESS);
                continue;
            }

            argLayouts.add(valueLayout(parameter));
        }

        MemoryLayout resLayout = null;

        var type = method.getReturnType();
        if (type != void.class) {
            if (type == String.class) {
                featureFlags |= FF_STACK.mask;
            } else if (type == boolean.class) {
                if (method.getAnnotation(FFMBooleanInt.class) != null) {
                    featureFlags |= FF_BOOLEAN_INT.mask;
                }
            } else if (Platform.getArchitecture().is32Bit() && type == long.class && method.getAnnotation(FFMPointer.class) != null) {
                featureFlags |= FF_POINTER32.mask;
            }

            resLayout = valueLayout(method, method.getReturnType());
        }

        this.featureFlags = featureFlags;

        var argLayoutsArray = argLayouts.toArray(MemoryLayout[]::new);
        this.descriptor = resLayout == null
            ? FunctionDescriptor.ofVoid(argLayoutsArray)
            : FunctionDescriptor.of(resLayout, argLayoutsArray);
    }

    UpcallMapper<?> bootstrap(boolean debugGenerator) {
        if (debugGenerator) {
            printDebug(method, parameters, descriptor);
        }

        var bridgeDescriptor = featureFlags == 0 ? null : descriptor
            .toMethodType()
            .insertParameterTypes(0, upcallInterface);

        /*
        new UpcallMapper<T>() {
            private static final FunctionDescriptor DESCRIPTOR = <DESCRIPTOR LDC>;
            private static final MethodHandle HANDLE = <either HANDLE LDC or MethodHandle to bridge method>;

            @Override
            public FunctionDescriptor descriptor() {
                return DESCRIPTOR;
            }

            @Override
            public MemorySegment allocate(Arena arena, T upcall) {
                return Linker.nativeLinker()
                    .upcallStub(HANDLE.bindTo(upcall), DESCRIPTOR, arena, EMPTY_OPTIONS);
            }

            // optional (created when featureFlags != 0)
            public static void bridge(T upcall, ...FFM-compatible params...) {
                upcall.invoke(...upcall params...);
            }
        };
        */
        var thisClass = getClassDescWrapper(method);
        var bytecode = ClassFile.of().build(thisClass, classBuilder -> {
            startHiddenClass(classBuilder)
                .withInterfaceSymbols(CD_UpcallMapper);

            classBuilder
                .withField("DESCRIPTOR", CD_FunctionDescriptor, ACC_PRIVATE | ACC_STATIC | ACC_FINAL)
                .withField("HANDLE", CD_MethodHandle, ACC_PRIVATE | ACC_STATIC | ACC_FINAL)
                .withMethod("<clinit>", MTD_void, ACC_STATIC, mb -> mb.withCode(cb -> {
                        cb
                            .ldc(condyCDataAt(CD_FunctionDescriptor, 0))
                            .putstatic(thisClass, "DESCRIPTOR", CD_FunctionDescriptor);
                        if (featureFlags == 0) {
                            cb
                                .ldc(condyCDataAt(CD_MethodHandle, 1));
                        } else {
                            cb
                                .invokestatic(CD_MethodHandles, "lookup", MTD_MethodHandles_lookup)
                                .ldc(cb.constantPool().classEntry(thisClass))
                                .ldc("bridge")
                                .ldc(condyCDataAt(CD_MethodType, 1))
                                .invokevirtual(CD_MethodHandles_Lookup, "findStatic", MTD_MethodHandles$Lookup_findStatic);
                        }
                        cb
                            .putstatic(thisClass, "HANDLE", CD_MethodHandle)
                            .return_();
                    })
                )
                .withMethod("descriptor", MTD_UpcallMapper_descriptor, ACC_PUBLIC | ACC_FINAL, mb -> mb.withCode(cb -> cb
                    .getstatic(thisClass, "DESCRIPTOR", CD_FunctionDescriptor)
                    .areturn())
                )
                .withMethod("handle", MTD_UpcallMapper_handle, ACC_PUBLIC | ACC_FINAL, mb -> mb.withCode(cb -> cb
                    .getstatic(thisClass, "HANDLE", CD_MethodHandle)
                    .areturn())
                );

            if (featureFlags != 0) {
                var methodTypeDesc = getMethodTypeDesc(method);

                classBuilder.withMethod("bridge", bridgeDescriptor.describeConstable().orElseThrow(), ACC_PUBLIC | ACC_STATIC, mb -> mb.withCode(cb -> {
                    cb.aload(cb.parameterSlot(0));
                    for (int p = 0; p < methodTypeDesc.parameterCount(); p++) {
                        var parameter = parameters[p];

                        var type = parameter.getType();

                        var slot = cb.parameterSlot(p + 1);
                        if (type == String.class) {
                            cb.aload(slot);
                            if (parameter.getAnnotation(FFMNullable.class) != null) {
                                cb
                                    .invokeinterface(CD_MemorySegment, "address", MTD_MemorySegment_address)
                                    .lconst_0()
                                    .lcmp()
                                    .ifThenElse(Opcode.IFEQ,
                                        CodeBuilder::aconst_null,
                                        bcb -> buildGetString(bcb.aload(slot), method));
                            } else {
                                buildGetString(cb, method);
                            }
                        } else if (Platform.getArchitecture().is32Bit() && type == long.class && parameter.getAnnotation(FFMPointer.class) != null) {
                            // TODO: test
                            cb.iload(slot);
                            buildPointer32to64(cb);
                        } else {
                            // FFMBooleanInt is handled implicitly, boolean parameters use iload anyway
                            cb.loadLocal(TypeKind.from(methodTypeDesc.parameterType(p)), slot);
                        }
                    }
                    cb.invokeinterface(upcallInterface.describeConstable().orElseThrow(), method.getName(), methodTypeDesc);
                    // TODO: transform return value
                    cb.return_(TypeKind.from(bridgeDescriptor.returnType()));
                }));
            }
        });

        if (debugGenerator) {
            printModel(ClassFile.of().parse(bytecode));
        }

        // Store the target handle as class data in the hidden class.
        try {
            var wrapperLookup = lookup.defineHiddenClassWithClassData(bytecode,
                List.of(
                    descriptor,
                    featureFlags == 0
                        ? lookup.unreflect(method)
                        : bridgeDescriptor
                ), true);

            //noinspection unchecked
            return ((Class<UpcallMapper<?>>)wrapperLookup.lookupClass())
                .getDeclaredConstructor()
                .newInstance();
        } catch (Error | RuntimeException e) {
            printModel(ClassFile.of().parse(bytecode));
            throw e;
        } catch (Exception e) {
            printModel(ClassFile.of().parse(bytecode));
            throw new RuntimeException(e);
        }
    }

}
