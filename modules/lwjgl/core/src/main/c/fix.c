#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <stdbool.h>
#include <string.h>

// ---- 静态函数指针变量 ----
static long (*original_getVulkanDriverHandle)(void) = NULL;
static long (*original_getFpsAddress)(void) = NULL;

static jboolean (*original_nativeNotifyLauncher)(JNIEnv*, jclass, jint, jintArray) = NULL;
static jboolean (*original_nativeSetInputReady)(jboolean) = NULL; // Critical 函数，无 JNIEnv 和 jclass
static jstring (*original_nativeClipboard)(JNIEnv*, jclass, jint, jbyteArray) = NULL;
static void (*original_nativeSetGrabbing)(JNIEnv*, jclass, jboolean) = NULL;
static void (*original_nativeSetCursorShape)(JNIEnv*, jclass, jint) = NULL;

// ---- 尝试 dlopen ----
static void* try_dlopen(const char* name) {
    void* handle = dlopen(name, RTLD_NOLOAD | RTLD_GLOBAL);
    if (!handle) {
        handle = dlopen(name, RTLD_GLOBAL | RTLD_LAZY);
    }
    return handle;
}

// ---- 加载所有需要的符号 ----
static void load_symbols(void) {
    static int loaded = 0;
    if (loaded) return;
    loaded = 1;

    void *pojav_handle = try_dlopen("libpojavexec.so");
    if (!pojav_handle) {
        fprintf(stderr, "[LWJGL] libpojavexec.so cannot be opened, symbols unavailable.\n");
        return;
    }

    // Vulkan 符号
    if (!original_getVulkanDriverHandle)
        original_getVulkanDriverHandle = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle");
    if (!original_getFpsAddress)
        original_getFpsAddress = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getFpsAddress");

    // GLFW 回调符号
    if (!original_nativeNotifyLauncher)
        original_nativeNotifyLauncher = (jboolean (*)(JNIEnv*, jclass, jint, jintArray)) dlsym(pojav_handle, "Java_org_lwjgl_glfw_CallbackBridge_nativeNotifyLauncher");
    if (!original_nativeSetInputReady)
        original_nativeSetInputReady = (jboolean (*)(jboolean)) dlsym(pojav_handle, "JavaCritical_org_lwjgl_glfw_CallbackBridge_nativeSetInputReady");
    if (!original_nativeClipboard)
        original_nativeClipboard = (jstring (*)(JNIEnv*, jclass, jint, jbyteArray)) dlsym(pojav_handle, "Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard");
    if (!original_nativeSetGrabbing)
        original_nativeSetGrabbing = (void (*)(JNIEnv*, jclass, jboolean)) dlsym(pojav_handle, "Java_org_lwjgl_glfw_CallbackBridge_nativeSetGrabbing");
    if (!original_nativeSetCursorShape)
        original_nativeSetCursorShape = (void (*)(JNIEnv*, jclass, jint)) dlsym(pojav_handle, "Java_org_lwjgl_glfw_CallbackBridge_nativeSetCursorShape");
}

EXTERN_C_ENTER

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass clazz) {
    load_symbols();
    if (!original_getVulkanDriverHandle) return 0;
    return (jlong) original_getVulkanDriverHandle();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getFpsAddress(JNIEnv *env, jclass clazz) {
    load_symbols();
    if (!original_getFpsAddress) return 0;
    return (jlong) original_getFpsAddress();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL_nLoadOpenGL(JNIEnv *env, jclass clazz, jstring libName) {

    const char *cLibName = (*env)->GetStringUTFChars(env, libName, NULL);
    if (!cLibName) return 0;

    void *handle = try_dlopen(cLibName);
    (*env)->ReleaseStringUTFChars(env, libName, cLibName);

    if (!handle) {
        fprintf(stderr, "[LWJGL] Failed to dlopen %s: %s\n", cLibName, dlerror());
        return 0;
    }

    return (jlong)(uintptr_t)handle;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeNotifyLauncher(JNIEnv* env, __attribute__((unused)) jclass clazz, jint type, jintArray action) {
    load_symbols();
    if (!original_nativeNotifyLauncher) return JNI_FALSE;
    return original_nativeNotifyLauncher(env, clazz, type, action);
}

JNIEXPORT jboolean JNICALL JavaCritical_org_lwjgl_glfw_CallbackBridge_nativeSetInputReady(jboolean inputReady) {
    load_symbols();
    if (!original_nativeSetInputReady) return JNI_FALSE;
    return original_nativeSetInputReady(inputReady);
}

JNIEXPORT jstring JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard(JNIEnv* env, __attribute__((unused)) jclass clazz, jint action, jbyteArray copySrc) {
    load_symbols();
    if (!original_nativeClipboard) return NULL;
    return original_nativeClipboard(env, clazz, action, copySrc);
}

JNIEXPORT void JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeSetGrabbing(__attribute__((unused)) JNIEnv* env, __attribute__((unused)) jclass clazz, jboolean grabbing) {
    load_symbols();
    if (!original_nativeSetGrabbing) return;
    original_nativeSetGrabbing(env, clazz, grabbing);
}

JNIEXPORT void JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeSetCursorShape(__attribute__((unused)) JNIEnv* env, __attribute__((unused)) jclass clazz, jint shape) {
    load_symbols();
    if (!original_nativeSetCursorShape) return;
    original_nativeSetCursorShape(env, clazz, shape);
}

EXTERN_C_EXIT
