/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.vulkan;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;

/**
 * This extension adds some line rasterization features that are commonly used in CAD applications and supported in other APIs like OpenGL. Bresenham-style line rasterization is supported, smooth rectangular lines (coverage to alpha) are supported, and stippled lines are supported for all three line rasterization modes.
 * 
 * <dl>
 * <dt><b>Name String</b></dt>
 * <dd>{@code VK_KHR_line_rasterization}</dd>
 * <dt><b>Extension Type</b></dt>
 * <dd>Device extension</dd>
 * <dt><b>Registered Extension Number</b></dt>
 * <dd>535</dd>
 * <dt><b>Revision</b></dt>
 * <dd>1</dd>
 * <dt><b>Extension and Version Dependencies</b></dt>
 * <dd>{@link KHRGetPhysicalDeviceProperties2 VK_KHR_get_physical_device_properties2} or <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#versions-1.1">Version 1.1</a></dd>
 * <dt><b>Contact</b></dt>
 * <dd><ul>
 * <li>Piers Daniell <a href="https://github.com/KhronosGroup/Vulkan-Docs/issues/new?body=[VK_KHR_line_rasterization]%20@pdaniell-nv%250A*Here%20describe%20the%20issue%20or%20question%20you%20have%20about%20the%20VK_KHR_line_rasterization%20extension*">pdaniell-nv</a></li>
 * </ul></dd>
 * </dl>
 * 
 * <h5>Other Extension Metadata</h5>
 * 
 * <dl>
 * <dt><b>Last Modified Date</b></dt>
 * <dd>2023-06-08</dd>
 * <dt><b>IP Status</b></dt>
 * <dd>No known IP claims.</dd>
 * <dt><b>Contributors</b></dt>
 * <dd><ul>
 * <li>Jeff Bolz, NVIDIA</li>
 * <li>Allen Jensen, NVIDIA</li>
 * <li>Faith Ekstrand, Intel</li>
 * </ul></dd>
 * </dl>
 */
public class KHRLineRasterization {

    /** The extension specification version. */
    public static final int VK_KHR_LINE_RASTERIZATION_SPEC_VERSION = 1;

    /** The extension name. */
    public static final String VK_KHR_LINE_RASTERIZATION_EXTENSION_NAME = "VK_KHR_line_rasterization";

    /**
     * Extends {@code VkStructureType}.
     * 
     * <h5>Enum values:</h5>
     * 
     * <ul>
     * <li>{@link #VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_FEATURES_KHR STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_FEATURES_KHR}</li>
     * <li>{@link #VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_LINE_STATE_CREATE_INFO_KHR STRUCTURE_TYPE_PIPELINE_RASTERIZATION_LINE_STATE_CREATE_INFO_KHR}</li>
     * <li>{@link #VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_PROPERTIES_KHR STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_PROPERTIES_KHR}</li>
     * </ul>
     */
    public static final int
        VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_FEATURES_KHR   = 1000259000,
        VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_LINE_STATE_CREATE_INFO_KHR = 1000259001,
        VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_LINE_RASTERIZATION_PROPERTIES_KHR = 1000259002;

    /** Extends {@code VkDynamicState}. */
    public static final int VK_DYNAMIC_STATE_LINE_STIPPLE_KHR = 1000259000;

    protected KHRLineRasterization() {
        throw new UnsupportedOperationException();
    }

    // --- [ vkCmdSetLineStippleKHR ] ---

    /**
     * Set line stipple dynamically for a command buffer.
     * 
     * <h5>C Specification</h5>
     * 
     * <p>To <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#pipelines-dynamic-state">dynamically set</a> the line stipple state, call:</p>
     * 
     * <pre><code>
     * void vkCmdSetLineStippleKHR(
     *     VkCommandBuffer                             commandBuffer,
     *     uint32_t                                    lineStippleFactor,
     *     uint16_t                                    lineStipplePattern);</code></pre>
     * 
     * <p>or the equivalent command</p>
     * 
     * <pre><code>
     * void vkCmdSetLineStippleEXT(
     *     VkCommandBuffer                             commandBuffer,
     *     uint32_t                                    lineStippleFactor,
     *     uint16_t                                    lineStipplePattern);</code></pre>
     * 
     * <h5>Description</h5>
     * 
     * <p>This command sets the line stipple state for subsequent drawing commands when drawing using <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#shaders-objects">shader objects</a>, or when the graphics pipeline is created with {@link EXTLineRasterization#VK_DYNAMIC_STATE_LINE_STIPPLE_EXT DYNAMIC_STATE_LINE_STIPPLE_EXT} set in {@link VkPipelineDynamicStateCreateInfo}{@code ::pDynamicStates}. Otherwise, this state is specified by the {@link VkPipelineRasterizationLineStateCreateInfoKHR}{@code ::lineStippleFactor} and {@link VkPipelineRasterizationLineStateCreateInfoKHR}{@code ::lineStipplePattern} values used to create the currently active pipeline.</p>
     * 
     * <h5>Valid Usage</h5>
     * 
     * <ul>
     * <li>{@code lineStippleFactor} <b>must</b> be in the range <code>[1,256]</code></li>
     * </ul>
     * 
     * <h5>Valid Usage (Implicit)</h5>
     * 
     * <ul>
     * <li>{@code commandBuffer} <b>must</b> be a valid {@code VkCommandBuffer} handle</li>
     * <li>{@code commandBuffer} <b>must</b> be in the <a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#commandbuffers-lifecycle">recording state</a></li>
     * <li>The {@code VkCommandPool} that {@code commandBuffer} was allocated from <b>must</b> support graphics operations</li>
     * <li>This command <b>must</b> only be called outside of a video coding scope</li>
     * </ul>
     * 
     * <h5>Host Synchronization</h5>
     * 
     * <ul>
     * <li>Host access to {@code commandBuffer} <b>must</b> be externally synchronized</li>
     * <li>Host access to the {@code VkCommandPool} that {@code commandBuffer} was allocated from <b>must</b> be externally synchronized</li>
     * </ul>
     * 
     * <h5>Command Properties</h5>
     * 
     * <table class="lwjgl">
     * <thead><tr><th><a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#VkCommandBufferLevel">Command Buffer Levels</a></th><th><a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#vkCmdBeginRenderPass">Render Pass Scope</a></th><th><a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#vkCmdBeginVideoCodingKHR">Video Coding Scope</a></th><th><a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#VkQueueFlagBits">Supported Queue Types</a></th><th><a href="https://registry.khronos.org/vulkan/specs/1.3-extensions/html/vkspec.html#fundamentals-queueoperation-command-types">Command Type</a></th></tr></thead>
     * <tbody><tr><td>Primary Secondary</td><td>Both</td><td>Outside</td><td>Graphics</td><td>State</td></tr></tbody>
     * </table>
     *
     * @param commandBuffer      the command buffer into which the command will be recorded.
     * @param lineStippleFactor  the repeat factor used in stippled line rasterization.
     * @param lineStipplePattern the bit pattern used in stippled line rasterization.
     */
    public static void vkCmdSetLineStippleKHR(VkCommandBuffer commandBuffer, @NativeType("uint32_t") int lineStippleFactor, @NativeType("uint16_t") short lineStipplePattern) {
        long __functionAddress = commandBuffer.getCapabilities().vkCmdSetLineStippleKHR;
        if (CHECKS) {
            check(__functionAddress);
        }
        callPCV(commandBuffer.address(), lineStippleFactor, lineStipplePattern, __functionAddress);
    }

}