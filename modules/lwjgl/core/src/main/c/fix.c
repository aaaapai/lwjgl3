#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <stdbool.h>
#include <string.h>

// ---- 缓存 pojavexec 中的函数指针 ----
static long (*original_getVulkanDriverHandle)(void) = NULL;
static long (*original_getFpsAddress)(void) = NULL;
static int   (*original_linker_ns_load)(const char* lib_search_path) = NULL;
static void* (*original_linker_ns_dlopen)(const char* name, int flag) = NULL;
static void* (*original_linker_ns_dlopen_unique)(const char* tmpdir, const char* name, int flags) = NULL;

// 加载所有符号（只执行一次，使用 RTLD_NOLOAD）
static void load_pojavexec_symbols(void) {
    if (original_getVulkanDriverHandle && original_getFpsAddress &&
        original_linker_ns_load && original_linker_ns_dlopen &&
        original_linker_ns_dlopen_unique) {
        return;
    }

    void *handle = dlopen("libpojavexec.so", RTLD_NOLOAD | RTLD_GLOBAL);
    if (!handle) {
        fprintf(stderr, "[LWJGL] libpojavexec.so not loaded, cannot provide required symbols.\n");
        return;
    }

    original_getVulkanDriverHandle = (long (*)(void)) dlsym(handle, "Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle");
    original_getFpsAddress         = (long (*)(void)) dlsym(handle, "Java_org_lwjgl_vulkan_VK_getFpsAddress");
    original_linker_ns_load        = (int (*)(const char*)) dlsym(handle, "linker_ns_load");
    original_linker_ns_dlopen      = (void* (*)(const char*, int)) dlsym(handle, "linker_ns_dlopen");
    original_linker_ns_dlopen_unique = (void* (*)(const char*, const char*, int)) dlsym(handle, "linker_ns_dlopen_unique");

    // 不要 dlclose(handle)
}

EXTERN_C_ENTER

// ---- Vulkan 相关 ----
JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass clazz) {
    load_pojavexec_symbols();
    if (!original_getVulkanDriverHandle) return 0;
    return (jlong) original_getVulkanDriverHandle();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getFpsAddress(JNIEnv *env, jclass clazz) {
    load_pojavexec_symbols();
    if (!original_getFpsAddress) return 0;
    return (jlong) original_getFpsAddress();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_GL_nLoadOpenGL(JNIEnv *env, jclass clazz, jstring libName) {
    load_pojavexec_symbols();
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
