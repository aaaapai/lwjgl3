/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.vulkan;

/**
 * This extension adds Vulkan support for the <a href="https://htmlpreview.github.io/?https://github.com/KhronosGroup/SPIRV-Registry/blob/master/extensions/KHR/SPV_KHR_terminate_invocation.html">{@code SPV_KHR_terminate_invocation}</a> SPIR-V extension. That SPIR-V extension provides a new instruction, {@code OpTerminateInvocation}, which causes a shader invocation to immediately terminate and sets the coverage of shaded samples to 0; only previously executed instructions will have observable effects. The {@code OpTerminateInvocation} instruction, along with the {@code OpDemoteToHelperInvocation} instruction from the {@link EXTShaderDemoteToHelperInvocation VK_EXT_shader_demote_to_helper_invocation} extension, together replace the {@code OpKill} instruction, which could behave like either of these instructions. {@code OpTerminateInvocation} provides the behavior required by the GLSL {@code discard} statement, and should be used when available by GLSL compilers and applications that need the GLSL {@code discard} behavior.
 * 
 * <h5>Promotion to Vulkan 1.3</h5>
 * 
 * <p>Functionality in this extension is included in core Vulkan 1.3, with the KHR suffix omitted. The original type, enum and command names are still available as aliases of the core functionality.</p>
 * 
 * <dl>
 * <dt><b>Name String</b></dt>
 * <dd>{@code VK_KHR_shader_terminate_invocation}</dd>
 * <dt><b>Extension Type</b></dt>
 * <dd>Device extension</dd>
 * <dt><b>Registered Extension Number</b></dt>
 * <dd>216</dd>
 * <dt><b>Revision</b></dt>
 * <dd>1</dd>
 * <dt><b>Extension and Version Dependencies</b></dt>
 * <dd>{@link KHRGetPhysicalDeviceProperties2 VK_KHR_get_physical_device_properties2} or <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#versions-1.1">Version 1.1</a></dd>
 * <dt><b>SPIR-V Dependencies</b></dt>
 * <dd><ul>
 * <li><a href="https://htmlpreview.github.io/?https://github.com/KhronosGroup/SPIRV-Registry/blob/master/extensions/KHR/SPV_KHR_terminate_invocation.html">SPV_KHR_terminate_invocation</a></li>
 * </ul></dd>
 * <dt><b>Deprecation State</b></dt>
 * <dd><ul>
 * <li><em>Promoted</em> to <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#versions-1.3-promotions">Vulkan 1.3</a></li>
 * </ul></dd>
 * <dt><b>Contact</b></dt>
 * <dd><ul>
 * <li>Jesse Hall <a href="https://github.com/KhronosGroup/Vulkan-Docs/issues/new?body=[VK_KHR_shader_terminate_invocation]%20@critsec%250A*Here%20describe%20the%20issue%20or%20question%20you%20have%20about%20the%20VK_KHR_shader_terminate_invocation%20extension*">critsec</a></li>
 * </ul></dd>
 * </dl>
 * 
 * <h5>Other Extension Metadata</h5>
 * 
 * <dl>
 * <dt><b>Last Modified Date</b></dt>
 * <dd>2020-08-11</dd>
 * <dt><b>IP Status</b></dt>
 * <dd>No known IP claims.</dd>
 * <dt><b>Contributors</b></dt>
 * <dd><ul>
 * <li>Alan Baker, Google</li>
 * <li>Jeff Bolz, NVIDIA</li>
 * <li>Jesse Hall, Google</li>
 * <li>Ralph Potter, Samsung</li>
 * <li>Tom Olson, Arm</li>
 * </ul></dd>
 * </dl>
 */
public final class KHRShaderTerminateInvocation {

    /** The extension specification version. */
    public static final int VK_KHR_SHADER_TERMINATE_INVOCATION_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String VK_KHR_SHADER_TERMINATE_INVOCATION_EXTENSION_NAME = "VK_KHR_shader_terminate_invocation";

    /** Extends {@code VkStructureType}. */
    public static final int VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SHADER_TERMINATE_INVOCATION_FEATURES_KHR = 1000215000;

    private KHRShaderTerminateInvocation() {}

}