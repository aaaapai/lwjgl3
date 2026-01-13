/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.glfw;

import org.jspecify.annotations.*;

import java.nio.*;

import org.lwjgl.*;

import org.lwjgl.system.*;

import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTMetalSurface.*;
import static org.lwjgl.vulkan.KHRAndroidSurface.*;

import org.lwjgl.vulkan.*;

public class GLFWVulkan {

     /** PojavLauncher: stub or wrap all functions to equivalent Vulkan functions */

    static {
        if (Platform.get() == Platform.MACOSX) {
            setPath(VK.getFunctionProvider());
        }
    }

    protected GLFWVulkan() {
        throw new UnsupportedOperationException();
    }

    // --- [ glfwInitVulkanLoader ] ---

    /** {@code void glfwInitVulkanLoader(PFN_vkGetInstanceProcAddr loader)} */
    public static void glfwInitVulkanLoader(@NativeType("PFN_vkGetInstanceProcAddr") long loader) {
        //long __functionAddress = Functions.InitVulkanLoader;
        //invokePV(loader, __functionAddress);
    }

    // --- [ glfwVulkanSupported ] ---

    /** {@code int glfwVulkanSupported(void)} */
    @NativeType("int")
    public static boolean glfwVulkanSupported() {
        return true;
    }

    // --- [ glfwGetRequiredInstanceExtensions ] ---


    // --- [ glfwGetInstanceProcAddress ] ---
    /**
     * Returns an array of names of Vulkan instance extensions required by GLFW for creating Vulkan surfaces for GLFW windows. If successful, the list will
     * always contain {@code VK_KHR_surface}, so if you don't require any additional extensions you can pass this list directly to the {@link VkInstanceCreateInfo}
     * struct.
     * 
     * <p>If Vulkan is not available on the machine, this function returns {@code NULL} and generates a {@link GLFW#GLFW_API_UNAVAILABLE API_UNAVAILABLE} error. Call {@link #glfwVulkanSupported VulkanSupported} to check whether
     * Vulkan is available.</p>
     * 
     * <p>If Vulkan is available but no set of extensions allowing window surface creation was found, this function returns {@code NULL}. You may still use Vulkan for
     * off-screen rendering and compute work.</p>
     * 
     * <p>Additional extensions may be required by future versions of GLFW. You should check if any extensions you wish to enable are already in the returned
     * array, as it is an error to specify an extension more than once in the {@code VkInstanceCreateInfo} struct.</p>
     * 
     * <p>The returned array is allocated and freed by GLFW. You should not free it yourself. It is guaranteed to be valid only until the library is terminated.</p>
     * 
     * <p>This function may be called from any thread.</p>
     * 
     * <p>Possible errors include {@link GLFW#GLFW_NOT_INITIALIZED NOT_INITIALIZED} and {@link GLFW#GLFW_API_UNAVAILABLE API_UNAVAILABLE}.</p>
     *
     * @return an array of ASCII encoded extension names, or {@code NULL} if an error occurred
     *
     * @since version 3.2
     */
    @NativeType("char const **")
    public static @Nullable PointerBuffer glfwGetRequiredInstanceExtensions() {
        Platform platform = Platform.get();
        
        MemoryStack stack = stackGet();
        int stackPointer = stack.getPointer();
    
        try {
            int baseExtensions = 1;
        
            String platformExtension = null;
            if (platform == Platform.MACOSX) {
                platformExtension = "VK_EXT_metal_surface";
            } else if (platform == Platform.LINUX) {
                platformExtension = "VK_KHR_android_surface";
            } else {
                return null;
            }
        
            PointerBuffer extensions;
            if (platformExtension != null) {
                extensions = stack.mallocPointer(baseExtensions + 1);
                extensions.put(0, stack.UTF8(KHRSurface.VK_KHR_SURFACE_EXTENSION_NAME));
                extensions.put(1, stack.UTF8(platformExtension));
            } else {
                extensions = stack.mallocPointer(baseExtensions);
                extensions.put(0, stack.UTF8(KHRSurface.VK_KHR_SURFACE_EXTENSION_NAME));
            }
        
            return extensions;
        
        } finally {
        stack.setPointer(stackPointer);
        }
    }

     
    /** {@code GLFWvkproc glfwGetInstanceProcAddress(VkInstance instance, char const * procname)} */
    @NativeType("GLFWvkproc")
    public static long glfwGetInstanceProcAddress(@NativeType("VkInstance") @Nullable VkInstance instance, @NativeType("char const *") ByteBuffer procname) {
        if (CHECKS) {
            checkNT1(procname);
        }
        return VK10.vkGetInstanceProcAddr(instance, procname);
    }

    /** {@code GLFWvkproc glfwGetInstanceProcAddress(VkInstance instance, char const * procname)} */
    @NativeType("GLFWvkproc")
    public static long glfwGetInstanceProcAddress(@Nullable VkInstance instance, @NativeType("char const *") CharSequence procname) {
        return VK10.vkGetInstanceProcAddr(instance, procname);
    }

    // --- [ glfwGetPhysicalDevicePresentationSupport ] ---

    /** {@code int glfwGetPhysicalDevicePresentationSupport(VkInstance instance, VkPhysicalDevice device, uint32_t queuefamily)} */
    @NativeType("int")
    public static boolean glfwGetPhysicalDevicePresentationSupport(VkInstance instance, VkPhysicalDevice device, @NativeType("uint32_t") int queuefamily) {
        return true;
    }

    // --- [ glfwCreateWindowSurface ] ---

    /** {@code VkResult glfwCreateWindowSurface(VkInstance instance, GLFWwindow * window, VkAllocationCallbacks const * allocator, VkSurfaceKHR * surface)} */
    /**
     * Creates a Vulkan surface for the specified window.
     * 
     * <p>If the Vulkan loader was not found at initialization, this function returns {@link VK10#VK_ERROR_INITIALIZATION_FAILED} and generates a {@link GLFW#GLFW_API_UNAVAILABLE API_UNAVAILABLE} error.
     * Call {@link #glfwVulkanSupported VulkanSupported} to check whether the Vulkan loader was found.</p>
     * 
     * <p>If the required window surface creation instance extensions are not available or if the specified instance was not created with these extensions
     * enabled, this function returns {@link VK10#VK_ERROR_EXTENSION_NOT_PRESENT} and generates a {@link GLFW#GLFW_API_UNAVAILABLE API_UNAVAILABLE} error. Call {@link #glfwGetRequiredInstanceExtensions GetRequiredInstanceExtensions} to
     * check what instance extensions are required.</p>
     * 
     * <p>The window surface cannot be shared with another API so the window must have been created with the client api hint set to {@link GLFW#GLFW_NO_API NO_API} otherwise it
     * generates a {@link GLFW#GLFW_INVALID_VALUE INVALID_VALUE} error and returns {@link KHRSurface#VK_ERROR_NATIVE_WINDOW_IN_USE_KHR}.</p>
     * 
     * <p>The window surface must be destroyed before the specified Vulkan instance. It is the responsibility of the caller to destroy the window surface. GLFW
     * does not destroy it for you. Call {@link KHRSurface#vkDestroySurfaceKHR} to destroy the surface.</p>
     * 
     * <p>Possible errors include {@link GLFW#GLFW_NOT_INITIALIZED NOT_INITIALIZED}, {@link GLFW#GLFW_API_UNAVAILABLE API_UNAVAILABLE}, {@link GLFW#GLFW_PLATFORM_ERROR PLATFORM_ERROR} and {@link GLFW#GLFW_INVALID_VALUE INVALID_VALUE}.</p>
     * 
     * <p>If an error occurs before the creation call is made, GLFW returns the Vulkan error code most appropriate for the error. Appropriate use of
     * {@link #glfwVulkanSupported VulkanSupported} and {@code glfwGetRequiredInstanceExtensions} should eliminate almost all occurrences of these errors.</p>
     * 
     * <p>Notes:</p>
     * 
     * <ul>
     * <li>This function may be called from any thread. For synchronization details of Vulkan objects, see the Vulkan specification.</li>
     * <li><b>macOS</b>: GLFW prefers the {@code VK_EXT_metal_surface} extension, with the {@code VK_MVK_macos_surface} extension as a fallback. The name of
     * the selected extension, if any, is included in the array returned by {@code glfwGetRequiredInstanceExtensions}.</li>
     * <li><b>macOS</b>: This function creates and sets a {@code CAMetalLayer} instance for the window content view, which is required for MoltenVK to
     * function.</li>
     * <li><b>x11</b>: By default GLFW prefers the {@code VK_KHR_xcb_surface} extension, with the {@code VK_KHR_xlib_surface} extension as a fallback. You can
     * make {@code VK_KHR_xlib_surface} the preferred extension by setting the {@link GLFW#GLFW_X11_XCB_VULKAN_SURFACE X11_XCB_VULKAN_SURFACE} init hint. The name of the selected extension, if
     * any, is included in the array returned by {@code glfwGetRequiredInstanceExtensions}.</li>
     * </ul>
     *
     * @param instance  the Vulkan instance to create the surface in
     * @param window    the window to create the surface for
     * @param allocator the allocator to use, or {@code NULL} to use the default allocator.
     * @param surface   where to store the handle of the surface. This is set to {@link VK10#VK_NULL_HANDLE} if an error occurred.
     *
     * @return {@link VK10#VK_SUCCESS} if successful, or a Vulkan error code if an error occurred
     *
     * @since version 3.2
     */
    // --- [ glfwCreateWindowSurface ] ---

    /** {@code VkResult glfwCreateWindowSurface(VkInstance instance, GLFWwindow * window, VkAllocationCallbacks const * allocator, VkSurfaceKHR * surface)} */
    @NativeType("VkResult")
    public static int glfwCreateWindowSurface(VkInstance instance, @NativeType("GLFWwindow *") long window, @NativeType("VkAllocationCallbacks const *") @Nullable VkAllocationCallbacks allocator, @NativeType("VkSurfaceKHR *") LongBuffer surface) {
        if (CHECKS) {
            check(surface, 1);
        }
    
        Platform platform = Platform.get();
    
        try {
            if (platform == Platform.MACOSX) {
                VkMetalSurfaceCreateInfoEXT pCreateInfo = VkMetalSurfaceCreateInfoEXT
                    .calloc()
                    .sType(EXTMetalSurface.VK_STRUCTURE_TYPE_METAL_SURFACE_CREATE_INFO_EXT)
                    .pNext(NULL)
                    .flags(0)
                    .pLayer(window);
            
                return EXTMetalSurface.vkCreateMetalSurfaceEXT(
                    instance, 
                    pCreateInfo, 
                    memAddressSafe(allocator), 
                    memAddress(surface)
                );
            
            } else if (platform == Platform.LINUX) {
                VkAndroidSurfaceCreateInfoKHR pCreateInfo = VkAndroidSurfaceCreateInfoKHR
                    .calloc()
                    .sType(KHRAndroidSurface.VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR)
                    .pNext(NULL)
                    .flags(0)
                    .window(window);
            
                return KHRAndroidSurface.vkCreateAndroidSurfaceKHR(
                    instance, 
                    pCreateInfo, 
                    memAddressSafe(allocator), 
                    memAddress(surface)
                );
            }
        } catch (UnsatisfiedLinkError e) {
            return VK10.VK_ERROR_EXTENSION_NOT_PRESENT;
        }
    
        return VK10.VK_ERROR_EXTENSION_NOT_PRESENT;
    }

    /** Array version of: {@link #glfwCreateWindowSurface CreateWindowSurface} */
    @NativeType("VkResult")
    public static int glfwCreateWindowSurface(VkInstance instance, @NativeType("GLFWwindow *") long window, @NativeType("VkAllocationCallbacks const *") @Nullable VkAllocationCallbacks allocator, @NativeType("VkSurfaceKHR *") long[] surface) {
        if (CHECKS) {
            check(surface, 1);
        }
    
        MemoryStack stack = stackGet();
        int stackPointer = stack.getPointer();
    
        try {
            LongBuffer surfaceBuffer = stack.mallocLong(1);
            int result = glfwCreateWindowSurface(instance, window, allocator, surfaceBuffer);
        
            if (result == VK10.VK_SUCCESS) {
                surface[0] = surfaceBuffer.get(0);
            }
        
            return result;
        } finally {
            stack.setPointer(stackPointer);
        }
    }

    public static int nglfwCreateWindowSurface(long instance, long window, long allocator, long surface) {
        Platform platform = Platform.get();
    
        MemoryStack stack = stackGet();
        int stackPointer = stack.getPointer();
    
        try {
            if (platform == Platform.MACOSX) {
                long pCreateInfo = stack.ncalloc(VkMetalSurfaceCreateInfoEXT.SIZEOF, 1, VkMetalSurfaceCreateInfoEXT.ALIGNOF);
                memPutInt(pCreateInfo, EXTMetalSurface.VK_STRUCTURE_TYPE_METAL_SURFACE_CREATE_INFO_EXT);
                memPutAddress(pCreateInfo + VkMetalSurfaceCreateInfoEXT.PLAYER, window);
            
                return EXTMetalSurface.nvkCreateMetalSurfaceEXT(instance, pCreateInfo, allocator, surface);
            
            } else if (platform == Platform.LINUX) {
                long pCreateInfo = stack.ncalloc(VkAndroidSurfaceCreateInfoKHR.SIZEOF, 1, VkAndroidSurfaceCreateInfoKHR.ALIGNOF);
                memPutInt(pCreateInfo, KHRAndroidSurface.VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR);
                memPutAddress(pCreateInfo + VkAndroidSurfaceCreateInfoKHR.WINDOW, window);
            
                return KHRAndroidSurface.nvkCreateAndroidSurfaceKHR(instance, pCreateInfo, allocator, surface);
            }
        } finally {
            stack.setPointer(stackPointer);
        }
    
        return VK10.VK_ERROR_EXTENSION_NOT_PRESENT;
    }

    /**
     * Calls {@link #setPath(String)} with the path of the specified {@link SharedLibrary}.
     * 
     * <p>Example usage: {@code GLFWVulkan.setPath(VK.getFunctionProvider());}</p> 
     *
     * @param sharedLibrary a {@code FunctionProvider} instance that will be cast to {@code SharedLibrary}
     */
    public static void setPath(FunctionProvider sharedLibrary) {
        if (!(sharedLibrary instanceof SharedLibrary)) {
            apiLog("GLFW Vulkan path override not set: function provider is not a shared library.");
            return;
        }

        String path = ((SharedLibrary)sharedLibrary).getPath();
        if (path == null) {
            apiLog("GLFW Vulkan path override not set: Could not resolve the shared library path.");
            return;
        }

        setPath(path);
    }

    /**
     * Overrides the Vulkan shared library that GLFW loads internally.
     *
     * <p>This is useful when there's a mismatch between the shared libraries loaded by LWJGL and GLFW.</p>
     *
     * <p>This method must be called before GLFW initializes Vulkan. The override is available only in the default GLFW build bundled with LWJGL. Using the
     * override with a custom GLFW build will produce a warning in {@code DEBUG} mode (but not an error).</p>
     *
     * @param path the Vulkan shared library path, or {@code null} to remove the override.
     */
    public static void setPath(@Nullable String path) {
        long override = GLFW.getLibrary().getFunctionAddress("_glfw_vulkan_library");
        if (override == NULL) {
            apiLog("GLFW Vulkan path override not set: Could not resolve override symbol.");
            return;
        }

        long a = memGetAddress(override);
        if (a != NULL) {
            nmemFree(a);
        }
        memPutAddress(override, path == null ? NULL : memAddress(memUTF8(path)));
    }

}
