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
        handle = dlopen(name, RTLD_GLOBAL | RTLD_LAZY);
    }
    return handle;
}

static void init_pojav(void) {
    static int initialized = 0;
    if (initialized) return;
    initialized = 1;

    void* handle = try_dlopen("libpojavexec.so");
    if (!handle) {
        fprintf(stderr, "[LWJGL] Failed to dlopen libpojavexec.so for init: %s\n", dlerror());
        return;
    }

    int (*pojavInit)(void) = (int (*)(void)) dlsym(handle, "pojavInit");
    if (!pojavInit) {
        fprintf(stderr, "[LWJGL] Cannot find pojavInit: %s\n", dlerror());
        return;
    }

    int ret = pojavInit();
    fprintf(stderr, "[LWJGL] pojavInit returned %d\n", ret);
}

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

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL_nLoadOpenGL(JNIEnv *env, jclass clazz, jstring libName) {
    init_pojav();

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

EXTERN_C_EXIT
