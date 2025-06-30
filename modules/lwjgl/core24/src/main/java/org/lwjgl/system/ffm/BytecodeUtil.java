/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.classfile.*;
import java.lang.constant.*;
import java.lang.foreign.*;
import java.lang.reflect.*;

import static java.lang.classfile.ClassFile.*;
import static java.lang.constant.ConstantDescs.*;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.ffm.ConstantDescs.*;

enum BytecodeUtil {
    ;

    static ClassBuilder startHiddenClass(ClassBuilder classBuilder) {
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

    static void buildPointer64to32(CodeBuilder cb) {
        cb.l2i();
    }

    static void buildPointer32to64(CodeBuilder cb) {
        cb
            .i2l()
            .ldc(0xFFFF_FFFFL)
            .land();
    }

    static void buildGetString(CodeBuilder cb, Method method) {
        cb.lconst_0();
        buildCharsetInstance(cb, getCharset(method));
        cb.invokeinterface(CD_MemorySegment, "getString", MTD_MemorySegment_getString);
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

    static <T> DynamicConstantDesc<T> condyCData(ClassDesc constantType) {
        return DynamicConstantDesc.ofNamed(BSM_CLASS_DATA, DEFAULT_NAME, constantType);
    }

    static <T> DynamicConstantDesc<T> condyCDataAt(ClassDesc constantType, int index) {
        return DynamicConstantDesc.ofNamed(BSM_CLASS_DATA_AT, DEFAULT_NAME, constantType, index);
    }

    static FFMCharset.Type getCharset(Method method) {
        var annotation = method.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : FFMCharset.DEFAULT;
    }

    static FFMCharset.Type getCharset(Parameter parameter) {
        var annotation = parameter.getAnnotation(FFMCharset.class);
        if (annotation == null) {
            annotation = parameter.getDeclaringExecutable().getDeclaringClass().getAnnotation(FFMCharset.class);
        }
        return annotation != null ? annotation.value() : FFMCharset.DEFAULT;
    }

    static boolean isPointerType(Parameter parameter, Class<?> type) {
        return type == MemorySegment.class || (type == long.class && parameter.getAnnotation(FFMPointer.class) != null);
    }

    private static String getParameterName(Parameter parameter, int index) {
        return parameter.isNamePresent()
            ? "<" + parameter.getName() + ">"
            : "#" + index;
    }
    static String getExceptionTextNULL(Parameter parameter, int index) {
        return parameter.getType().getSimpleName() + " argument " + getParameterName(parameter, index) + " cannot be NULL";
    }

    static void printModel(CompoundElement<?> model) {
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

}