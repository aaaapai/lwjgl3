/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.vulkan;

import org.jspecify.annotations.*;
import org.lwjgl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static java.lang.Math.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK11.*;

/**
 * This class loads the Vulkan library into the JVM process.
 *
 * <p><b>macOS</b>: Vulkan on macOS is emulated over the Metal API using the <a href="https://github.com/KhronosGroup/MoltenVK">MoltenVK</a> library.
 * The {@code lwjgl-vulkan-natives-macos*} artifact includes a MoltenVK build and LWJGL will try to load it first, if available. Using MoltenVK directly is
 * simple and efficient, but circumvents the Vulkan SDK and the validation layers it provides. During development, it is often very useful to make use of these
 * validation layers, to verify correct API usage and avoid common pitfalls. In order to do that on macOS, LWJGL must be forced to load the Vulkan Loader
 * library ({@code libvulkan.1.dylib}) instead of MoltenVK. There are two options:</p>
 * <ol>
 *     <li>
 *     Set {@link Configuration#VULKAN_LIBRARY_NAME} to {@code libvulkan.1.dylib}. Depending on the Vulkan SDK installation, this file may not be in the
 *     library path and an absolute path must be used instead.
 *     </li>
 *     <li>
 *     Remove the {@code lwjgl-vulkan-natives} artifact from the class/module-path. LWJGL will fall back to loading {@code libvulkan.1.dylib} (if it is in the
 *     library path).
 *     </li>
 * </ol>
 *
 * <p>Note that when using the Vulkan Loader the Vulkan implementation will be provided by the MoltenVK build shipped with the Vulkan SDK. That build may be
 * different from the one bundled with LWJGL.</p>
 *
 * @see Configuration#VULKAN_LIBRARY_NAME
 * @see Configuration#VULKAN_EXPLICIT_INIT
 */
public final class VK {

    private static @Nullable FunctionProvider functionProvider;

    private static @Nullable GlobalCommands globalCommands;

    static {
        if (!Configuration.VULKAN_EXPLICIT_INIT.get(false)) {
            create();
        }
    }

    private VK() {}

    /**
     * Loads the Vulkan shared library, using the default library name.
     *
     * @see #create(String)
     */
    public static void create() {
        if(tryCreateFromEnv()) return;
        SharedLibrary VK;
        switch (Platform.get()) {
            case FREEBSD:
            case LINUX:
                VK = Library.loadNative(VK.class, "org.lwjgl.vulkan", Configuration.VULKAN_LIBRARY_NAME, "libvulkan.so.1");
                break;
            case WINDOWS:
                VK = Library.loadNative(VK.class, "org.lwjgl.vulkan", Configuration.VULKAN_LIBRARY_NAME, "vulkan-1");
                break;
            case MACOSX:
                String override = Configuration.VULKAN_LIBRARY_NAME.get();
                if (override != null) {
                    // use the override without a fallback
                    VK = Library.loadNative(VK.class, "org.lwjgl.vulkan", override);
                } else {
                    try {
                        // no override, try to use the bundled implementation (if available)
                        VK = Library.loadNative(VK.class, "org.lwjgl.vulkan", "MoltenVK", true);
                    } catch (Throwable ignored) {
                        // TODO: print if found but loading failed (print in verbose debugloader mode?)
                        // TODO: must print all suppressed exceptions
                        // fallback to the Vulkan loader
                        VK = Library.loadNative(VK.class, "org.lwjgl.vulkan", "libvulkan.1.dylib");
                    }
                }
                break;
            default:
                throw new IllegalStateException();
        }
        create(VK);
    }

    /**
     * Attempt to get a pointer to the Vulkan shared library
     * from the enviroinment.
     * This is used by Pojav to provide the correct Vulkan driver
     * on Adreno devices.
     * @returns true when the library handle was found, parsed and 
     *          create(FunctionProvider) was called, false otherwise.
     */
    private static boolean tryCreateFromEnv() {
       if(Platform.get() != Platform.LINUX) return false;
       long vulkanHandle = 0;
       try {
           vulkanHandle = getVulkanDriverHandle();
       } catch(UnsatisfiedLinkError e) { 
           e.printStackTrace();
           return false;
       }
       SharedLibrary VK = Library.createFromHandle("libvulkan.so", vulkanHandle);
       create(VK);
       return true;
    }

    /**
     * Loads the Vulkan shared library, using the specified library name.
     *
     * <p>The {@link FunctionProvider} instance created by this method can only be used to retrieve global commands and commands exposed statically by the
     * Vulkan shared library.</p>
     *
     * @param libName the shared library name
     *
     * @see #create(FunctionProvider)
     */
    public static void create(String libName) {
        create(Library.loadNative(VK.class, "org.lwjgl.vulkan", libName));
    }

    /**
     * Initializes Vulkan with the specified {@link FunctionProvider}. This method can be used to implement custom Vulkan library loading.
     *
     * @param functionProvider the provider of Vulkan function addresses
     */
    public static void create(FunctionProvider functionProvider) {
        if (VK.functionProvider != null) {
            throw new IllegalStateException("Vulkan has already been created.");
        }

        VK.functionProvider = functionProvider;
        globalCommands = new GlobalCommands(functionProvider);
    }

    /** Unloads the Vulkan shared library. */
    public static void destroy() {
        if (functionProvider == null) {
            return;
        }

        if (functionProvider instanceof NativeResource) {
            ((NativeResource)functionProvider).free();
        }
        functionProvider = null;
        globalCommands = null;
    }

    private static <T> T check(@Nullable T t) {
        if (t == null) {
            throw new IllegalStateException("Vulkan library has not been loaded.");
        }
        return t;
    }

    /** Returns the {@link FunctionProvider} for the Vulkan shared library. */
    public static FunctionProvider getFunctionProvider() {
        return check(functionProvider);
    }

    /**
     * Returns a {@code uint32_t}, which is the version of Vulkan supported by instance-level functionality, encoded as described in the
     * <a target="_blank" href="https://www.khronos.org/registry/vulkan/specs/1.3-extensions/html/vkspec.html#extendingvulkan-coreversions-versionnumbers">
     * Version Numbers</a> section.
     *
     * <p>This method can be called before creating a Vulkan instance. The returned value can be used to set the {@link VkApplicationInfo}{@code ::apiVersion}
     * member.</p>
     */
    public static int getInstanceVersionSupported() {
        long EnumerateInstanceVersion = getGlobalCommands().vkEnumerateInstanceVersion;
        if (EnumerateInstanceVersion != NULL) {
            try (MemoryStack stack = stackPush()) {
                IntBuffer pi = stack.callocInt(1);
                if (vkEnumerateInstanceVersion(pi) == VK_SUCCESS) {
                    return pi.get(0);
                }
            }
        }
        return VK_API_VERSION_1_0;
    }

    static class GlobalCommands {

        final long vkGetInstanceProcAddr;

        final long vkCreateInstance;
        final long vkEnumerateInstanceExtensionProperties;
        final long vkEnumerateInstanceLayerProperties;
        final long vkEnumerateInstanceVersion;

        GlobalCommands(FunctionProvider library) {
            vkGetInstanceProcAddr = library.getFunctionAddress("vkGetInstanceProcAddr");
            if (vkGetInstanceProcAddr == NULL) {
                throw new IllegalArgumentException("A critical function is missing. Make sure that Vulkan is available.");
            }

            vkCreateInstance = getFunctionAddress("vkCreateInstance");
            vkEnumerateInstanceExtensionProperties = getFunctionAddress("vkEnumerateInstanceExtensionProperties");
            vkEnumerateInstanceLayerProperties = getFunctionAddress("vkEnumerateInstanceLayerProperties");
            vkEnumerateInstanceVersion = getFunctionAddress("vkEnumerateInstanceVersion", false);
        }

        private long getFunctionAddress(String name) { return getFunctionAddress(name, true); }
        private long getFunctionAddress(String name, boolean required) {
            try (MemoryStack stack = stackPush()) {
                long address = callPPP(NULL, memAddress(stack.ASCII(name)), vkGetInstanceProcAddr);
                if (address == NULL && required) {
                    throw new IllegalArgumentException("A critical function is missing. Make sure that Vulkan is available.");
                }
                return address;
            }
        }
    }

    /** Returns the Vulkan global commands. */
    static GlobalCommands getGlobalCommands() { return check(globalCommands); }

    static Set<String> getEnabledExtensionSet(int apiVersion, @Nullable PointerBuffer extensionNames) {
        Set<String> enabledExtensions = new HashSet<>(16);

        int majorVersion = VK_VERSION_MAJOR(apiVersion);
        int minorVersion = VK_VERSION_MINOR(apiVersion);

        int[] VK_VERSIONS = {
            3 // Vulkan 1.0 to 1.3
        };

        int maxMajor = min(majorVersion, VK_VERSIONS.length);
        for (int M = 1; M <= maxMajor; M++) {
            int maxMinor = VK_VERSIONS[M - 1];
            if (M == majorVersion) {
                maxMinor = min(minorVersion, maxMinor);
            }
            for (int m = 0; m <= maxMinor; m++) {
                enabledExtensions.add("Vulkan" + M + m);
            }
        }

        if (extensionNames != null) {
            for (int i = extensionNames.position(); i < extensionNames.limit(); i++) {
                enabledExtensions.add(extensionNames.getStringUTF8(i));
            }
        }

        return enabledExtensions;
    }
    public static native long getVulkanDriverHandle();
}
