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
import java.util.stream.*;

import static java.lang.constant.ConstantDescs.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.ffm.BindingGenerator.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

final class NativeHandle {

    final boolean hasFunctionAddress;

    final Class<?> allocatorClass;

    final Linker.Option captureCallState;

    final String nativeName;

    final FunctionDescriptor descriptor;

    final MethodHandle ffm;

    NativeHandle(BindingConfig config, Method method, Parameter[] parameters) {
        hasFunctionAddress = getFunctionAddress(method) != null;

        var argLayouts = new ArrayList<MemoryLayout>(parameters.length);

        Class<?>      allocatorClass   = null;
        Linker.Option captureCallState = null;

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
                    throw new IllegalStateException("Invalid position of SegmentAllocator/StackArena parameter.");
                }
                allocatorClass = parameter.getType();
                // TODO: if a struct is returned or passed by-value?
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
                resLayout = valueLayout(method, method.getReturnType());
            }
        }

        this.nativeName = hasFunctionAddress ? null : getNativeName(method);

        var argLayoutsArray = argLayouts.toArray(MemoryLayout[]::new);
        this.descriptor = resLayout == null
            ? FunctionDescriptor.ofVoid(argLayoutsArray)
            : FunctionDescriptor.of(resLayout, argLayoutsArray);

        this.ffm = Linker.nativeLinker()
            .downcallHandle(descriptor, createOptions(config, method));
    }

    int getVirtualParameterCount() {
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

    int getFirstNativeParameterIndex() {
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

    int getStackSlot(CodeBuilder cb) {
        return allocatorClass != null
            ? cb.parameterSlot(hasFunctionAddress ? 1 : 0)
            : cb.allocateLocal(TypeKind.REFERENCE);
    }

    Object getClassData(BindingConfig config, Method method, boolean hasTracing) {
        var functionAddress = hasFunctionAddress
            ? MemorySegment.NULL
            : config.lookup().find(nativeName).orElseThrow();

        if (!hasTracing) {
            return hasFunctionAddress
                ? ffm
                : List.of(ffm, functionAddress);
        }

        return List.of(ffm, functionAddress, config.traceConsumer(), method);
    }

    void build(BindingConfig config, boolean hasTracing, CodeBuilder cb) {
        if (hasFunctionAddress && !hasTracing) {
            // Load target handle via Condy, using MethodHandles::classData as the bootstrap method.
            cb.ldc(DynamicConstantDesc.<MethodHandle>ofNamed(BSM_CLASS_DATA, DEFAULT_NAME, CD_MethodHandle));
        } else {
            if (!hasTracing) {
                // Load target handle & function address via Condy, using MethodHandles::classDataAt as the bootstrap method.
                cb.ldc(DynamicConstantDesc.<MethodHandle>ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, CD_MethodHandle, 0));
            }
            if (!hasFunctionAddress) {
                // Pass the function address manually, to avoid bindTo's additional lambda form.
                cb.ldc(DynamicConstantDesc.<MemorySegment>ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, CD_MemorySegment, 1));
            }
        }
    }

    static void trace(CodeBuilder cb, MethodTypeDesc nativeMethodTypeDesc) {
        // Load target handle via Condy, using MethodHandles::classDataAt as the bootstrap method.
        cb.ldc(DynamicConstantDesc.<MethodHandle>ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, CD_MethodHandle, 0));
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

        cb
            // Load TraceConsumer via Condy, using MethodHandles::classDataAt as the bootstrap method.
            .ldc(DynamicConstantDesc.<TraceConsumer>ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, CD_TraceConsumer, 2))
            // Load Method via Condy, using MethodHandles::classDataAt as the bootstrap method.
            .ldc(DynamicConstantDesc.<Method>ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, CD_Method, 3));
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
    }

    private static void boxPrimitiveValue(CodeBuilder cb, TypeKind tk) {
        switch (tk) {
            case BOOLEAN -> cb.invokestatic(CD_Boolean, "valueOf", MTD_Boolean_valueOf);
            case BYTE -> cb.invokestatic(CD_Byte, "valueOf", MTD_Byte_valueOf);
            case SHORT -> cb.invokestatic(CD_Short, "valueOf", MTD_Short_valueOf);
            case INT -> cb.invokestatic(CD_Integer, "valueOf", MTD_Integer_valueOf);
            case LONG -> cb.invokestatic(CD_Long, "valueOf", MTD_Long_valueOf);
            case FLOAT -> cb.invokestatic(CD_Float, "valueOf", MTD_Float_valueOf);
            case DOUBLE -> cb.invokestatic(CD_Double, "valueOf", MTD_Double_valueOf);
            default -> throw new UnsupportedOperationException("Unsupported primitive type: " + tk);
        }
    }


    MethodHandle getSimpleHandle(SymbolLookup lookup) {
        return hasFunctionAddress
            ? ffm
            : ffm.bindTo(lookup.find(nativeName).orElseThrow());
    }

    private static MemoryLayout valueLayout(Parameter parameter) { return valueLayout(parameter, parameter.getType()); }
    private static MemoryLayout valueLayout(AnnotatedElement element, Class<?> type) {
        if (type == String.class) {
            return BindingGenerator.C_POINTER;
        } else if (type == MemorySegment.class) {
            return ValueLayout.ADDRESS;
        } else if (type == boolean.class) {
            var booleanInt = element.getAnnotation(FFMBooleanInt.class);
            if (booleanInt != null) {
                return booleanInt.value().layout;
            }
            return ValueLayout.JAVA_BOOLEAN;
        } else if (type == byte.class) {
            return ValueLayout.JAVA_BYTE;
        } else if (type == short.class) {
            return ValueLayout.JAVA_SHORT;
        } else if (type == int.class) {
            return ValueLayout.JAVA_INT;
        } else if (type == long.class) {
            return Platform.getArchitecture().is32Bit() && element.getAnnotation(FFMPointer.class) != null
                ? ValueLayout.JAVA_INT
                : ValueLayout.JAVA_LONG;
        } else if (type == float.class) {
            return ValueLayout.JAVA_FLOAT;
        } else if (type == double.class) {
            return ValueLayout.JAVA_DOUBLE;
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private Linker.Option[] createOptions(BindingConfig config, Method method) {
        var options = new ArrayList<Linker.Option>(2);

        addCritical(config, method, options);

        if (captureCallState != null) {
            options.add(captureCallState);
        }

        return options.toArray(Linker.Option[]::new);
    }

    private static void addCritical(BindingConfig config, Method method, ArrayList<Linker.Option> options) {
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

    private static FFMFunctionAddress getFunctionAddress(Method method) {
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

    void printDebug(Method method, Parameter[] parameters) {
        apiLog("\t-> J: " + method.getReturnType() + ' ' + method.getName() + '(' + Stream.of(parameters).map(it -> it.getType().getSimpleName()).collect(Collectors.joining(", ")) + ')');
        var signature = method.getAnnotation(FFMSignature.class);
        if (signature != null) {
            apiLog("\t-> S: " + signature.value());
        }
        apiLog("\t-> N: " + descriptor.toString());
    }

}
