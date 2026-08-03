/*
 * Copyright LWJGL All rights reserved.
 * Modified to provide backward-compatible ALCcontext / ALCdevice wrappers.
 */
package org.lwjgl.openal;

import org.jspecify.annotations.*;
import java.nio.*;
import org.lwjgl.system.*;
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ALC10 {

    // ---------- 常量（与原类一致，此处省略，但必须保留所有常量） ----------
    public static final int
        ALC_INVALID = 0xFFFFFFFF,
        ALC_FALSE   = 0x0,
        ALC_TRUE    = 0x1,
        ALC_FREQUENCY = 0x1007,
        ALC_REFRESH   = 0x1008,
        ALC_SYNC      = 0x1009,
        ALC_NO_ERROR        = 0x0,
        ALC_INVALID_DEVICE  = 0xA001,
        ALC_INVALID_CONTEXT = 0xA002,
        ALC_INVALID_ENUM    = 0xA003,
        ALC_INVALID_VALUE   = 0xA004,
        ALC_OUT_OF_MEMORY   = 0xA005,
        ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004,
        ALC_DEVICE_SPECIFIER         = 0x1005,
        ALC_EXTENSIONS               = 0x1006,
        ALC_MAJOR_VERSION   = 0x1000,
        ALC_MINOR_VERSION   = 0x1001,
        ALC_ATTRIBUTES_SIZE = 0x1002,
        ALC_ALL_ATTRIBUTES  = 0x1003;

    protected ALC10() { throw new UnsupportedOperationException(); }

    // ---------- 内部包装类（兼容 LWJGL 2 风格） ----------
    public static class ALCcontext {
        public final long handle;
        public ALCcontext(long handle) { this.handle = handle; }
    }

    public static class ALCdevice {
        public final long handle;
        public ALCdevice(long handle) { this.handle = handle; }
    }

    // ---------- 原有 LWJGL 3 原生方法（不变） ----------
    // （这里仅列出关键原生方法，实际使用时请保持与 LWJGL 官方一致，
    //  但为了编译通过，以下方法必须存在。下面的实现是 LWJGL 3.3.3 的版本，
    //  你可以直接从 LWJGL 源码中复制完整内容，或直接依赖官方 jar，
    //  再通过以下包装方法调用。）

    // 原生方法示例（实际应从 LWJGL 复制完整）：
    public static long nalcOpenDevice(long deviceSpecifier) {
        long func = ALC.getICD().alcOpenDevice;
        return invokePP(deviceSpecifier, func);
    }
    public static long alcOpenDevice(ByteBuffer deviceSpecifier) {
        if (CHECKS) checkNT1Safe(deviceSpecifier);
        return nalcOpenDevice(memAddressSafe(deviceSpecifier));
    }
    public static long alcOpenDevice(CharSequence deviceSpecifier) {
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            stack.nUTF8Safe(deviceSpecifier, true);
            return nalcOpenDevice(deviceSpecifier == null ? NULL : stack.getPointerAddress());
        } finally { stack.setPointer(ptr); }
    }

    public static boolean alcCloseDevice(long deviceHandle) {
        long func = ALC.getICD().alcCloseDevice;
        if (CHECKS) check(deviceHandle);
        return invokePZ(deviceHandle, func);
    }

    public static long nalcCreateContext(long deviceHandle, long attrList) {
        long func = ALC.getICD().alcCreateContext;
        if (CHECKS) check(deviceHandle);
        return invokePPP(deviceHandle, attrList, func);
    }
    public static long alcCreateContext(long deviceHandle, IntBuffer attrList) {
        if (CHECKS) checkNTSafe(attrList);
        return nalcCreateContext(deviceHandle, memAddressSafe(attrList));
    }

    public static boolean alcMakeContextCurrent(long context) {
        long func = ALC.getICD().alcMakeContextCurrent;
        return invokePZ(context, func);
    }
    public static void alcProcessContext(long context) {
        long func = ALC.getICD().alcProcessContext;
        if (CHECKS) check(context);
        invokePV(context, func);
    }
    public static void alcSuspendContext(long context) {
        long func = ALC.getICD().alcSuspendContext;
        if (CHECKS) check(context);
        invokePV(context, func);
    }
    public static void alcDestroyContext(long context) {
        long func = ALC.getICD().alcDestroyContext;
        if (CHECKS) check(context);
        invokePV(context, func);
    }

    public static long alcGetCurrentContext() { // 原生返回 long
        long func = ALC.getICD().alcGetCurrentContext;
        return invokeP(func);
    }

    public static long alcGetContextsDevice(long context) {
        long func = ALC.getICD().alcGetContextsDevice;
        if (CHECKS) check(context);
        return invokePP(context, func);
    }

    public static boolean nalcIsExtensionPresent(long deviceHandle, long extName) {
        long func = ALC.getICD().alcIsExtensionPresent;
        return invokePPZ(deviceHandle, extName, func);
    }
    public static boolean alcIsExtensionPresent(long deviceHandle, ByteBuffer extName) {
        if (CHECKS) checkNT1(extName);
        return nalcIsExtensionPresent(deviceHandle, memAddress(extName));
    }
    public static boolean alcIsExtensionPresent(long deviceHandle, CharSequence extName) {
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            stack.nASCII(extName, true);
            return nalcIsExtensionPresent(deviceHandle, stack.getPointerAddress());
        } finally { stack.setPointer(ptr); }
    }

    public static long nalcGetProcAddress(long deviceHandle, long funcName) {
        long func = ALC.getICD().alcGetProcAddress;
        return invokePPP(deviceHandle, funcName, func);
    }
    public static long alcGetProcAddress(long deviceHandle, ByteBuffer funcName) {
        if (CHECKS) checkNT1(funcName);
        return nalcGetProcAddress(deviceHandle, memAddress(funcName));
    }
    public static long alcGetProcAddress(long deviceHandle, CharSequence funcName) {
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            stack.nASCII(funcName, true);
            return nalcGetProcAddress(deviceHandle, stack.getPointerAddress());
        } finally { stack.setPointer(ptr); }
    }

    public static int nalcGetEnumValue(long deviceHandle, long enumName) {
        long func = ALC.getICD().alcGetEnumValue;
        return invokePPI(deviceHandle, enumName, func);
    }
    public static int alcGetEnumValue(long deviceHandle, ByteBuffer enumName) {
        if (CHECKS) checkNT1(enumName);
        return nalcGetEnumValue(deviceHandle, memAddress(enumName));
    }
    public static int alcGetEnumValue(long deviceHandle, CharSequence enumName) {
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            stack.nASCII(enumName, true);
            return nalcGetEnumValue(deviceHandle, stack.getPointerAddress());
        } finally { stack.setPointer(ptr); }
    }

    public static int alcGetError(long deviceHandle) {
        long func = ALC.getICD().alcGetError;
        return invokePI(deviceHandle, func);
    }

    public static long nalcGetString(long deviceHandle, int token) {
        long func = ALC.getICD().alcGetString;
        return invokePP(deviceHandle, token, func);
    }
    public static String alcGetString(long deviceHandle, int token) {
        long result = nalcGetString(deviceHandle, token);
        return memUTF8Safe(result);
    }

    public static void nalcGetIntegerv(long deviceHandle, int token, int size, long dest) {
        long func = ALC.getICD().alcGetIntegerv;
        invokePPV(deviceHandle, token, size, dest, func);
    }
    public static void alcGetIntegerv(long deviceHandle, int token, IntBuffer dest) {
        nalcGetIntegerv(deviceHandle, token, dest.remaining(), memAddress(dest));
    }
    public static int alcGetInteger(long deviceHandle, int token) {
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            IntBuffer dest = stack.callocInt(1);
            nalcGetIntegerv(deviceHandle, token, 1, memAddress(dest));
            return dest.get(0);
        } finally { stack.setPointer(ptr); }
    }

    // ========== 新增兼容包装方法（返回 LWJGL 2 风格对象） ==========

    /** 包装：创建上下文，返回 ALCcontext 对象 */
    public static ALCcontext alcCreateContext(ALCdevice device, IntBuffer attrList) {
        long ctxHandle = alcCreateContext(device.handle, attrList);
        return ctxHandle == 0 ? null : new ALCcontext(ctxHandle);
    }

    /** 包装：获取当前上下文，返回 ALCcontext 对象 */
    public static ALCcontext alcGetCurrentContext() {
        long ctx = alcGetCurrentContext(); // 调用原生 long 方法
        return ctx == 0 ? null : new ALCcontext(ctx);
    }

    /** 包装：获取上下文对应的设备，返回 ALCdevice 对象 */
    public static ALCdevice alcGetContextsDevice(ALCcontext context) {
        if (context == null) return null;
        long dev = alcGetContextsDevice(context.handle);
        return dev == 0 ? null : new ALCdevice(dev);
    }

    /** 包装：获取整型属性，写入 IntBuffer */
    public static void alcGetInteger(ALCdevice device, int pname, IntBuffer integerdata) {
        if (device == null) return;
        alcGetIntegerv(device.handle, pname, integerdata);
    }

    /** 包装：获取字符串属性 */
    public static String alcGetString(ALCdevice device, int pname) {
        if (device == null) return null;
        return alcGetString(device.handle, pname);
    }

    /** 包装：检测扩展是否存在 */
    public static boolean alcIsExtensionPresent(ALCdevice device, String extName) {
        if (device == null || extName == null) return false;
        // 直接使用原生 long 版本（避免反射）
        MemoryStack stack = stackGet(); int ptr = stack.getPointer();
        try {
            stack.nASCII(extName, true);
            return nalcIsExtensionPresent(device.handle, stack.getPointerAddress());
        } finally { stack.setPointer(ptr); }
    }

    /** 包装：打开设备，返回 ALCdevice 对象（可选，按需添加） */
    public static ALCdevice alcOpenDevice(String deviceSpecifier) {
        long dev = alcOpenDevice((CharSequence) deviceSpecifier);
        return dev == 0 ? null : new ALCdevice(dev);
    }
}
