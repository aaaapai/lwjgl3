#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <stdbool.h>
#include <string.h>

static long (*original_getVulkanDriverHandle)(void) = NULL;
static long (*original_getFpsAddress)(void) = NULL;

static void* try_dlopen(const char* name) {
    void* handle = dlopen(name, RTLD_NOLOAD | RTLD_GLOBAL);
    if (!handle) {
        handle = dlopen(name, RTLD_GLOBAL);
    }
    return handle;
}

// 加载 Vulkan 函数指针（只执行一次）
static void load_vulkan_symbols(void) {
    if (original_getVulkanDriverHandle && original_getFpsAddress)
        return;

    void *pojav_handle = try_dlopen("libpojavexec.so");
    if (pojav_handle) {
        if (!original_getVulkanDriverHandle)
            original_getVulkanDriverHandle = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle");
        if (!original_getFpsAddress)
            original_getFpsAddress = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getFpsAddress");
    } else {
        fprintf(stderr, "[LWJGL] libpojavexec.so cannot be opened, Vulkan functions unavailable.\n");
    }
}

EXTERN_C_ENTER

// ---- Vulkan 相关 JNI 方法 ----
JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass clazz) {
    load_vulkan_symbols();
    if (!original_getVulkanDriverHandle) return 0;
    return (jlong) original_getVulkanDriverHandle();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getFpsAddress(JNIEnv *env, jclass clazz) {
    load_vulkan_symbols();
    if (!original_getFpsAddress) return 0;
    return (jlong) original_getFpsAddress();
}

// ---- OpenGL 加载（直接使用 dlopen，无需命名空间） ----
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL_nLoadOpenGL(JNIEnv *env, jclass clazz, jstring libName) {
    const char *cLibName = (*env)->GetStringUTFChars(env, libName, NULL);
    if (!cLibName) return 0;

    // 直接 dlopen，不依赖命名空间
    void *handle = dlopen(cLibName, RTLD_LAZY | RTLD_GLOBAL);
    (*env)->ReleaseStringUTFChars(env, libName, cLibName);

    if (!handle) {
        fprintf(stderr, "[LWJGL] Failed to dlopen %s: %s\n", cLibName, dlerror());
        return 0;
    }

    return (jlong)(uintptr_t)handle;
}

EXTERN_C_EXIT
