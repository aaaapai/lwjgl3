#include "common_tools.h"
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <stdbool.h>
#include <string.h>
#include <signal.h>
#include <setjmp.h>

static sigjmp_buf jmp_buffer;

static void sigsegv_handler(int sig, siginfo_t *info, void *context) {
    fprintf(stderr, "[LWJGL] Caught SIGSEGV at address %p, jumping back...\n", info->si_addr);
    siglongjmp(jmp_buffer, 1);
}

static void install_signal_handler(void) {
    static int installed = 0;
    if (installed) return;
    installed = 1;

    struct sigaction sa;
    sa.sa_sigaction = sigsegv_handler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_SIGINFO;
    if (sigaction(SIGSEGV, &sa, NULL) == -1) {
        fprintf(stderr, "[LWJGL] Failed to install signal handler\n");
    } else {
        fprintf(stderr, "[LWJGL] Signal handler installed for SIGSEGV\n");
    }
}

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

    install_signal_handler();

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

    if (sigsetjmp(jmp_buffer, 1) == 0) {
        int ret = pojavInit();
        fprintf(stderr, "[LWJGL] pojavInit returned %d\n", ret);
    } else {
        // 如果 pojavInit 内部发生 SIGSEGV，跳转到这里
        fprintf(stderr, "[LWJGL] SIGSEGV occurred inside pojavInit, ignoring and continuing...\n");
        // 尝试重新初始化或跳过
    }
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
