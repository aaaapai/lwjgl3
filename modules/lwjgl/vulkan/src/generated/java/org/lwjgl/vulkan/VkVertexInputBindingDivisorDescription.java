/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.vulkan;

import org.jspecify.annotations.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.system.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Structure specifying a divisor used in instanced rendering.
 * 
 * <h5>Description</h5>
 * 
 * <p>If this structure is not used to define a divisor value for an attribute, then the divisor has a logical default value of 1.</p>
 * 
 * <h5>Valid Usage</h5>
 * 
 * <ul>
 * <li>{@code binding} <b>must</b> be less than {@link VkPhysicalDeviceLimits}{@code ::maxVertexInputBindings}</li>
 * <li>If the <a href="https://registry.khronos.org/vulkan/specs/latest/html/vkspec.html#features-vertexAttributeInstanceRateZeroDivisor">{@code vertexAttributeInstanceRateZeroDivisor}</a> feature is not enabled, {@code divisor} <b>must</b> not be 0</li>
 * <li>If the <a href="https://registry.khronos.org/vulkan/specs/latest/html/vkspec.html#features-vertexAttributeInstanceRateDivisor">{@code vertexAttributeInstanceRateDivisor}</a> feature is not enabled, {@code divisor} <b>must</b> be 1</li>
 * <li>{@code divisor} <b>must</b> be a value between 0 and {@link VkPhysicalDeviceVertexAttributeDivisorProperties}{@code ::maxVertexAttribDivisor}, inclusive</li>
 * <li>{@link VkVertexInputBindingDescription}{@code ::inputRate} <b>must</b> be of type {@link VK10#VK_VERTEX_INPUT_RATE_INSTANCE VERTEX_INPUT_RATE_INSTANCE} for this {@code binding}</li>
 * </ul>
 * 
 * <h5>See Also</h5>
 * 
 * <p>{@link VkPipelineVertexInputDivisorStateCreateInfo}</p>
 * 
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct VkVertexInputBindingDivisorDescription {
 *     uint32_t {@link #binding};
 *     uint32_t {@link #divisor};
 * }</code></pre>
 */
public class VkVertexInputBindingDivisorDescription extends Struct<VkVertexInputBindingDivisorDescription> implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        BINDING,
        DIVISOR;

    static {
        Layout layout = __struct(
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        BINDING = layout.offsetof(0);
        DIVISOR = layout.offsetof(1);
    }

    protected VkVertexInputBindingDivisorDescription(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected VkVertexInputBindingDivisorDescription create(long address, @Nullable ByteBuffer container) {
        return new VkVertexInputBindingDivisorDescription(address, container);
    }

    /**
     * Creates a {@code VkVertexInputBindingDivisorDescription} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public VkVertexInputBindingDivisorDescription(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** the binding number for which the divisor is specified. */
    @NativeType("uint32_t")
    public int binding() { return nbinding(address()); }
    /** the number of successive instances that will use the same value of the vertex attribute when instanced rendering is enabled. For example, if the divisor is N, the same vertex attribute will be applied to N successive instances before moving on to the next vertex attribute. The maximum value of {@code divisor} is implementation-dependent and can be queried using {@link VkPhysicalDeviceVertexAttributeDivisorProperties}{@code ::maxVertexAttribDivisor}. A value of 0 <b>can</b> be used for the divisor if the <a href="https://registry.khronos.org/vulkan/specs/latest/html/vkspec.html#features-vertexAttributeInstanceRateZeroDivisor">{@code vertexAttributeInstanceRateZeroDivisor}</a> feature is enabled. In this case, the same vertex attribute will be applied to all instances. */
    @NativeType("uint32_t")
    public int divisor() { return ndivisor(address()); }

    /** Sets the specified value to the {@link #binding} field. */
    public VkVertexInputBindingDivisorDescription binding(@NativeType("uint32_t") int value) { nbinding(address(), value); return this; }
    /** Sets the specified value to the {@link #divisor} field. */
    public VkVertexInputBindingDivisorDescription divisor(@NativeType("uint32_t") int value) { ndivisor(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public VkVertexInputBindingDivisorDescription set(
        int binding,
        int divisor
    ) {
        binding(binding);
        divisor(divisor);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public VkVertexInputBindingDivisorDescription set(VkVertexInputBindingDivisorDescription src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code VkVertexInputBindingDivisorDescription} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static VkVertexInputBindingDivisorDescription malloc() {
        return new VkVertexInputBindingDivisorDescription(nmemAllocChecked(SIZEOF), null);
    }

    /** Returns a new {@code VkVertexInputBindingDivisorDescription} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static VkVertexInputBindingDivisorDescription calloc() {
        return new VkVertexInputBindingDivisorDescription(nmemCallocChecked(1, SIZEOF), null);
    }

    /** Returns a new {@code VkVertexInputBindingDivisorDescription} instance allocated with {@link BufferUtils}. */
    public static VkVertexInputBindingDivisorDescription create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return new VkVertexInputBindingDivisorDescription(memAddress(container), container);
    }

    /** Returns a new {@code VkVertexInputBindingDivisorDescription} instance for the specified memory address. */
    public static VkVertexInputBindingDivisorDescription create(long address) {
        return new VkVertexInputBindingDivisorDescription(address, null);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static @Nullable VkVertexInputBindingDivisorDescription createSafe(long address) {
        return address == NULL ? null : new VkVertexInputBindingDivisorDescription(address, null);
    }

    /**
     * Returns a new {@link VkVertexInputBindingDivisorDescription.Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer malloc(int capacity) {
        return new Buffer(nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link VkVertexInputBindingDivisorDescription.Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer calloc(int capacity) {
        return new Buffer(nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkVertexInputBindingDivisorDescription.Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return new Buffer(memAddress(container), container, -1, 0, capacity, capacity);
    }

    /**
     * Create a {@link VkVertexInputBindingDivisorDescription.Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer create(long address, int capacity) {
        return new Buffer(address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static VkVertexInputBindingDivisorDescription.@Nullable Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : new Buffer(address, capacity);
    }

    /**
     * Returns a new {@code VkVertexInputBindingDivisorDescription} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static VkVertexInputBindingDivisorDescription malloc(MemoryStack stack) {
        return new VkVertexInputBindingDivisorDescription(stack.nmalloc(ALIGNOF, SIZEOF), null);
    }

    /**
     * Returns a new {@code VkVertexInputBindingDivisorDescription} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static VkVertexInputBindingDivisorDescription calloc(MemoryStack stack) {
        return new VkVertexInputBindingDivisorDescription(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    /**
     * Returns a new {@link VkVertexInputBindingDivisorDescription.Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer malloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkVertexInputBindingDivisorDescription.Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkVertexInputBindingDivisorDescription.Buffer calloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #binding}. */
    public static int nbinding(long struct) { return memGetInt(struct + VkVertexInputBindingDivisorDescription.BINDING); }
    /** Unsafe version of {@link #divisor}. */
    public static int ndivisor(long struct) { return memGetInt(struct + VkVertexInputBindingDivisorDescription.DIVISOR); }

    /** Unsafe version of {@link #binding(int) binding}. */
    public static void nbinding(long struct, int value) { memPutInt(struct + VkVertexInputBindingDivisorDescription.BINDING, value); }
    /** Unsafe version of {@link #divisor(int) divisor}. */
    public static void ndivisor(long struct, int value) { memPutInt(struct + VkVertexInputBindingDivisorDescription.DIVISOR, value); }

    // -----------------------------------

    /** An array of {@link VkVertexInputBindingDivisorDescription} structs. */
    public static class Buffer extends StructBuffer<VkVertexInputBindingDivisorDescription, Buffer> implements NativeResource {

        private static final VkVertexInputBindingDivisorDescription ELEMENT_FACTORY = VkVertexInputBindingDivisorDescription.create(-1L);

        /**
         * Creates a new {@code VkVertexInputBindingDivisorDescription.Buffer} instance backed by the specified container.
         *
         * <p>Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link VkVertexInputBindingDivisorDescription#SIZEOF}, and its mark will be undefined.</p>
         *
         * <p>The created buffer instance holds a strong reference to the container object.</p>
         */
        public Buffer(ByteBuffer container) {
            super(container, container.remaining() / SIZEOF);
        }

        public Buffer(long address, int cap) {
            super(address, null, -1, 0, cap, cap);
        }

        Buffer(long address, @Nullable ByteBuffer container, int mark, int pos, int lim, int cap) {
            super(address, container, mark, pos, lim, cap);
        }

        @Override
        protected Buffer self() {
            return this;
        }

        @Override
        protected Buffer create(long address, @Nullable ByteBuffer container, int mark, int position, int limit, int capacity) {
            return new Buffer(address, container, mark, position, limit, capacity);
        }

        @Override
        protected VkVertexInputBindingDivisorDescription getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@link VkVertexInputBindingDivisorDescription#binding} field. */
        @NativeType("uint32_t")
        public int binding() { return VkVertexInputBindingDivisorDescription.nbinding(address()); }
        /** @return the value of the {@link VkVertexInputBindingDivisorDescription#divisor} field. */
        @NativeType("uint32_t")
        public int divisor() { return VkVertexInputBindingDivisorDescription.ndivisor(address()); }

        /** Sets the specified value to the {@link VkVertexInputBindingDivisorDescription#binding} field. */
        public VkVertexInputBindingDivisorDescription.Buffer binding(@NativeType("uint32_t") int value) { VkVertexInputBindingDivisorDescription.nbinding(address(), value); return this; }
        /** Sets the specified value to the {@link VkVertexInputBindingDivisorDescription#divisor} field. */
        public VkVertexInputBindingDivisorDescription.Buffer divisor(@NativeType("uint32_t") int value) { VkVertexInputBindingDivisorDescription.ndivisor(address(), value); return this; }

    }

}