#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>

// 函数指针，缓存原始实现
static long (*original_getVulkanDriverHandle)(void) = NULL;
static long (*original_getFpsAddress)(void) = NULL;

// 加载 pojavexec 中的符号（仅当库已加载时）
static void load_pojavexec_symbols(void) {
    if (original_getVulkanDriverHandle != NULL && original_getFpsAddress != NULL) {
        return; // 已缓存
    }

    // 使用 RTLD_NOLOAD 避免重复加载，只获取已加载的句柄
    void *handle = dlopen("libpojavexec.so", RTLD_NOLOAD | RTLD_GLOBAL);
    if (!handle) {
        // 库未加载，无法获取符号
        fprintf(stderr, "[LWJGL] libpojavexec.so not loaded, cannot provide Vulkan driver handle.\n");
        return;
    }

    // 获取原始函数指针（符号名与 Java native 方法相同）
    original_getVulkanDriverHandle = (long (*)(void)) dlsym(handle, "Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle");
    original_getFpsAddress = (long (*)(void)) dlsym(handle, "Java_org_lwjgl_vulkan_VK_getFpsAddress");

    // 注意：不要 dlclose(handle)，因为我们需要保持库存活，且后续调用符号仍有效
}

EXTERN_C_ENTER

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass clazz) {
    load_pojavexec_symbols();
    if (original_getVulkanDriverHandle == NULL) {
        return 0;
    }
    return (jlong) original_getVulkanDriverHandle();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_vulkan_VK_getFpsAddress(JNIEnv *env, jclass clazz) {
    load_pojavexec_symbols();
    if (original_getFpsAddress == NULL) {
        return 0;
    }
    return (jlong) original_getFpsAddress();
}

EXTERN_C_EXIT
