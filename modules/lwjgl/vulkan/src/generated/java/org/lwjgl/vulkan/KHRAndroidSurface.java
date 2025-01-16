/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.vulkan;

import org.jspecify.annotations.*;

import java.nio.*;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The {@code VK_KHR_android_surface} extension is an instance extension. It provides a mechanism to create a {@code VkSurfaceKHR} object (defined by the {@link KHRSurface VK_KHR_surface} extension) that refers to an {@code ANativeWindow}, Android’s native surface type. The {@code ANativeWindow} represents the producer endpoint of any buffer queue, regardless of consumer endpoint. Common consumer endpoints for {@code ANativeWindows} are the system window compositor, video encoders, and application-specific compositors importing the images through a {@code SurfaceTexture}.
 * 
 * <dl>
 * <dt><b>Name String</b></dt>
 * <dd>{@code VK_KHR_android_surface}</dd>
 * <dt><b>Extension Type</b></dt>
 * <dd>Instance extension</dd>
 * <dt><b>Registered Extension Number</b></dt>
 * <dd>9</dd>
 * <dt><b>Revision</b></dt>
 * <dd>6</dd>
 * <dt><b>Extension and Version Dependencies</b></dt>
 * <dd><ul>
 * <li>Requires support for Vulkan 1.0</li>
 * <li>Requires {@link KHRSurface VK_KHR_surface} to be enabled</li>
 * </ul></dd>
 * <dt><b>Contact</b></dt>
 * <dd><ul>
 * <li>Jesse Hall <a target="_blank" href="https://github.com/KhronosGroup/Vulkan-Docs/issues/new?body=[VK_KHR_android_surface]%20@critsec%250A*Here%20describe%20the%20issue%20or%20question%20you%20have%20about%20the%20VK_KHR_android_surface%20extension*">critsec</a></li>
 * </ul></dd>
 * </dl>
 * 
 * <h5>Other Extension Metadata</h5>
 * 
 * <dl>
 * <dt><b>Last Modified Date</b></dt>
 * <dd>2016-01-14</dd>
 * <dt><b>IP Status</b></dt>
 * <dd>No known IP claims.</dd>
 * <dt><b>Contributors</b></dt>
 * <dd><ul>
 * <li>Patrick Doane, Blizzard</li>
 * <li>Jason Ekstrand, Intel</li>
 * <li>Ian Elliott, LunarG</li>
 * <li>Courtney Goeltzenleuchter, LunarG</li>
 * <li>Jesse Hall, Google</li>
 * <li>James Jones, NVIDIA</li>
 * <li>Antoine Labour, Google</li>
 * <li>Jon Leech, Khronos</li>
 * <li>David Mao, AMD</li>
 * <li>Norbert Nopper, Freescale</li>
 * <li>Alon Or-bach, Samsung</li>
 * <li>Daniel Rakos, AMD</li>
 * <li>Graham Sellers, AMD</li>
 * <li>Ray Smith, ARM</li>
 * <li>Jeff Vigil, Qualcomm</li>
 * <li>Chia-I Wu, LunarG</li>
 * </ul></dd>
 * </dl>
 */
public class KHRAndroidSurface {

    public static final int VK_KHR_ANDROID_SURFACE_SPEC_VERSION = 6;

    public static final String VK_KHR_ANDROID_SURFACE_EXTENSION_NAME = "VK_KHR_android_surface";

    public static final int VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR = 1000008000;

    protected KHRAndroidSurface() {
        throw new UnsupportedOperationException();
    }

    // --- [ vkCreateAndroidSurfaceKHR ] ---

    /** {@code VkResult vkCreateAndroidSurfaceKHR(VkInstance instance, VkAndroidSurfaceCreateInfoKHR const * pCreateInfo, VkAllocationCallbacks const * pAllocator, VkSurfaceKHR * pSurface)} */
    public static int nvkCreateAndroidSurfaceKHR(VkInstance instance, long pCreateInfo, long pAllocator, long pSurface) {
        long __functionAddress = instance.getCapabilities().vkCreateAndroidSurfaceKHR;
        if (CHECKS) {
            check(__functionAddress);
        }
        return callPPPPI(instance.address(), pCreateInfo, pAllocator, pSurface, __functionAddress);
    }

    /** {@code VkResult vkCreateAndroidSurfaceKHR(VkInstance instance, VkAndroidSurfaceCreateInfoKHR const * pCreateInfo, VkAllocationCallbacks const * pAllocator, VkSurfaceKHR * pSurface)} */
    @NativeType("VkResult")
    public static int vkCreateAndroidSurfaceKHR(VkInstance instance, @NativeType("VkAndroidSurfaceCreateInfoKHR const *") VkAndroidSurfaceCreateInfoKHR pCreateInfo, @NativeType("VkAllocationCallbacks const *") @Nullable VkAllocationCallbacks pAllocator, @NativeType("VkSurfaceKHR *") LongBuffer pSurface) {
        if (CHECKS) {
            check(pSurface, 1);
        }
        return nvkCreateAndroidSurfaceKHR(instance, pCreateInfo.address(), memAddressSafe(pAllocator), memAddress(pSurface));
    }

    /** {@code VkResult vkCreateAndroidSurfaceKHR(VkInstance instance, VkAndroidSurfaceCreateInfoKHR const * pCreateInfo, VkAllocationCallbacks const * pAllocator, VkSurfaceKHR * pSurface)} */
    @NativeType("VkResult")
    public static int vkCreateAndroidSurfaceKHR(VkInstance instance, @NativeType("VkAndroidSurfaceCreateInfoKHR const *") VkAndroidSurfaceCreateInfoKHR pCreateInfo, @NativeType("VkAllocationCallbacks const *") @Nullable VkAllocationCallbacks pAllocator, @NativeType("VkSurfaceKHR *") long[] pSurface) {
        long __functionAddress = instance.getCapabilities().vkCreateAndroidSurfaceKHR;
        if (CHECKS) {
            check(__functionAddress);
            check(pSurface, 1);
        }
        return callPPPPI(instance.address(), pCreateInfo.address(), memAddressSafe(pAllocator), pSurface, __functionAddress);
    }

}
