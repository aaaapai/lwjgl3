#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <stdbool.h>
#include <string.h>

// ---- 缓存函数指针 ----
static long (*original_getVulkanDriverHandle)(void) = NULL;
static long (*original_getFpsAddress)(void) = NULL;
static int   (*original_linker_ns_load)(const char* lib_search_path) = NULL;
static void* (*original_linker_ns_dlopen)(const char* name, int flag) = NULL;
static void* (*original_linker_ns_dlopen_unique)(const char* tmpdir, const char* name, int flags) = NULL;

// 辅助：尝试获取已加载的库，若不存在则主动加载
static void* try_dlopen(const char* name) {
    void* handle = dlopen(name, RTLD_NOLOAD | RTLD_GLOBAL);
    if (!handle) {
        // 未加载，则主动加载
        handle = dlopen(name, RTLD_GLOBAL);
    }
    return handle;
}

// 加载所有符号（只执行一次）
static void load_all_symbols(void) {
    // 如果所有符号都已加载，直接返回
    if (original_getVulkanDriverHandle && original_getFpsAddress &&
        original_linker_ns_load && original_linker_ns_dlopen &&
        original_linker_ns_dlopen_unique) {
        return;
    }

    // 1. 从 libpojavexec.so 加载 Vulkan 相关函数
    void *pojav_handle = try_dlopen("libpojavexec.so");
    if (pojav_handle) {
        if (!original_getVulkanDriverHandle)
            original_getVulkanDriverHandle = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle");
        if (!original_getFpsAddress)
            original_getFpsAddress = (long (*)(void)) dlsym(pojav_handle, "Java_org_lwjgl_vulkan_VK_getFpsAddress");
        // 注意：不要 dlclose(pojav_handle)，保留引用
    } else {
        fprintf(stderr, "[LWJGL] libpojavexec.so cannot be opened, Vulkan functions unavailable.\n");
    }

    // 2. 从 libdriver_helper.so 加载 linker 命名空间函数
    void *driver_handle = try_dlopen("libdriver_helper.so");
    if (driver_handle) {
        if (!original_linker_ns_load)
            original_linker_ns_load = (int (*)(const char*)) dlsym(driver_handle, "linker_ns_load");
        if (!original_linker_ns_dlopen)
            original_linker_ns_dlopen = (void* (*)(const char*, int)) dlsym(driver_handle, "linker_ns_dlopen");
        if (!original_linker_ns_dlopen_unique)
            original_linker_ns_dlopen_unique = (void* (*)(const char*, const char*, int)) dlsym(driver_handle, "linker_ns_dlopen_unique");
        // 同样不要 dlclose(driver_handle)
    } else {
        fprintf(stderr, "[LWJGL] libdriver_helper.so cannot be opened, linker namespace functions unavailable.\n");
    }
}

EXTERN_C_ENTER

// ---- Vulkan 相关 ----
JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass clazz) {
    load_all_symbols();
    if (!original_getVulkanDriverHandle) return 0;
    return (jlong) original_getVulkanDriverHandle();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getFpsAddress(JNIEnv *env, jclass clazz) {
    load_all_symbols();
    if (!original_getFpsAddress) return 0;
    return (jlong) original_getFpsAddress();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL_nLoadOpenGL(JNIEnv *env, jclass clazz, jstring libName) {
    load_all_symbols();
    if (!original_linker_ns_load || !original_linker_ns_dlopen) {
        fprintf(stderr, "[LWJGL] linker_ns functions not available.\n");
        return 0;
    }

    const char *cLibName = (*env)->GetStringUTFChars(env, libName, NULL);
    if (!cLibName) return 0;

    const char *searchPath = getenv("LD_LIBRARY_PATH");
    if (!searchPath || strlen(searchPath) == 0) {
        searchPath = "/vendor/lib64:/system/lib64";
    }

    static int ns_loaded = 0;
    if (!ns_loaded) {
        if (original_linker_ns_load(searchPath)) {
            ns_loaded = 1;
        } else {
            fprintf(stderr, "[LWJGL] Failed to load namespace with path: %s\n", searchPath);
            (*env)->ReleaseStringUTFChars(env, libName, cLibName);
            return 0;
        }
    }

    void *handle = original_linker_ns_dlopen(cLibName, RTLD_LAZY | RTLD_GLOBAL);
    (*env)->ReleaseStringUTFChars(env, libName, cLibName);

    if (!handle) {
        fprintf(stderr, "[LWJGL] Failed to dlopen %s via namespace.\n", cLibName);
        return 0;
    }

    return (jlong)(uintptr_t)handle;
}

EXTERN_C_EXIT
