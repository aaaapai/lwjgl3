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
import java.util.stream.*;

import static java.lang.constant.ConstantDescs.*;
import static java.lang.invoke.MethodHandles.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

abstract class Call {

    protected final MethodHandles.Lookup lookup;

    protected final BindingConfig config;

    protected final Map<Type, Method> typeMap;

    Call(MethodHandles.Lookup lookup) {
        this.lookup = lookup;
        try {
            config = classDataAt(lookup, DEFAULT_NAME, BindingConfig.class, 0);

            Object mappers = config.mappers();

            typeMap = mappers == null
                ? null
                : ((MapperContainer)mappers).$mappers();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected boolean needsMapper(Class<?> type) {
        return !type.isPrimitive() && typeMap != null && typeMap.containsKey(type);
    }

    protected static void boxPrimitiveValue(CodeBuilder cb, TypeKind tk) {
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

    protected static MemoryLayout valueLayout(Parameter parameter) { return valueLayout(parameter, parameter.getType()); }
    protected static MemoryLayout valueLayout(AnnotatedElement element, Class<?> type) {
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

    protected static void printDebug(Method method, Parameter[] parameters, FunctionDescriptor descriptor) {
        apiLog("\t-> J: " + method.getReturnType() + ' ' + method.getName() + '(' + Stream.of(parameters).map(it -> it.getType().getSimpleName()).collect(Collectors.joining(", ")) + ')');
        var signature = method.getAnnotation(FFMSignature.class);
        if (signature != null) {
            apiLog("\t-> S: " + signature.value());
        }
        apiLog("\t-> N: " + descriptor.toString());
    }

    static enum FeatureFlag {
        /** Needs tracing */
        FF_TRACING,
        /** Needs a check */
        FF_CHECK,
        /** Needs a new stack frame */
        FF_STACK,
        /** Has struct/union/upcall type */
        FF_MAPPER,
        /** Needs MemorySegment.getString */
        FF_STRING,
        /** Needs integer to boolean conversion */
        FF_BOOLEAN_INT,
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

}
