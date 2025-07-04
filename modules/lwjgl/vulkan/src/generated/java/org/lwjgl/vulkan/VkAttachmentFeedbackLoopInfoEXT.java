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
 * <pre>{@code
 * struct VkAttachmentFeedbackLoopInfoEXT {
 *     VkStructureType sType;
 *     void const * pNext;
 *     VkBool32 feedbackLoopEnable;
 * }}</pre>
 */
public class VkAttachmentFeedbackLoopInfoEXT extends Struct<VkAttachmentFeedbackLoopInfoEXT> implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        STYPE,
        PNEXT,
        FEEDBACKLOOPENABLE;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        STYPE = layout.offsetof(0);
        PNEXT = layout.offsetof(1);
        FEEDBACKLOOPENABLE = layout.offsetof(2);
    }

    protected VkAttachmentFeedbackLoopInfoEXT(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected VkAttachmentFeedbackLoopInfoEXT create(long address, @Nullable ByteBuffer container) {
        return new VkAttachmentFeedbackLoopInfoEXT(address, container);
    }

    /**
     * Creates a {@code VkAttachmentFeedbackLoopInfoEXT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public VkAttachmentFeedbackLoopInfoEXT(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return the value of the {@code sType} field. */
    @NativeType("VkStructureType")
    public int sType() { return nsType(address()); }
    /** @return the value of the {@code pNext} field. */
    @NativeType("void const *")
    public long pNext() { return npNext(address()); }
    /** @return the value of the {@code feedbackLoopEnable} field. */
    @NativeType("VkBool32")
    public boolean feedbackLoopEnable() { return nfeedbackLoopEnable(address()) != 0; }

    /** Sets the specified value to the {@code sType} field. */
    public VkAttachmentFeedbackLoopInfoEXT sType(@NativeType("VkStructureType") int value) { nsType(address(), value); return this; }
    /** Sets the {@link KHRUnifiedImageLayouts#VK_STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT} value to the {@code sType} field. */
    public VkAttachmentFeedbackLoopInfoEXT sType$Default() { return sType(KHRUnifiedImageLayouts.VK_STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT); }
    /** Sets the specified value to the {@code pNext} field. */
    public VkAttachmentFeedbackLoopInfoEXT pNext(@NativeType("void const *") long value) { npNext(address(), value); return this; }
    /** Sets the specified value to the {@code feedbackLoopEnable} field. */
    public VkAttachmentFeedbackLoopInfoEXT feedbackLoopEnable(@NativeType("VkBool32") boolean value) { nfeedbackLoopEnable(address(), value ? 1 : 0); return this; }

    /** Initializes this struct with the specified values. */
    public VkAttachmentFeedbackLoopInfoEXT set(
        int sType,
        long pNext,
        boolean feedbackLoopEnable
    ) {
        sType(sType);
        pNext(pNext);
        feedbackLoopEnable(feedbackLoopEnable);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public VkAttachmentFeedbackLoopInfoEXT set(VkAttachmentFeedbackLoopInfoEXT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static VkAttachmentFeedbackLoopInfoEXT malloc() {
        return new VkAttachmentFeedbackLoopInfoEXT(nmemAllocChecked(SIZEOF), null);
    }

    /** Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static VkAttachmentFeedbackLoopInfoEXT calloc() {
        return new VkAttachmentFeedbackLoopInfoEXT(nmemCallocChecked(1, SIZEOF), null);
    }

    /** Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance allocated with {@link BufferUtils}. */
    public static VkAttachmentFeedbackLoopInfoEXT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return new VkAttachmentFeedbackLoopInfoEXT(memAddress(container), container);
    }

    /** Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance for the specified memory address. */
    public static VkAttachmentFeedbackLoopInfoEXT create(long address) {
        return new VkAttachmentFeedbackLoopInfoEXT(address, null);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static @Nullable VkAttachmentFeedbackLoopInfoEXT createSafe(long address) {
        return address == NULL ? null : new VkAttachmentFeedbackLoopInfoEXT(address, null);
    }

    /**
     * Returns a new {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer malloc(int capacity) {
        return new Buffer(nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer calloc(int capacity) {
        return new Buffer(nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return new Buffer(memAddress(container), container, -1, 0, capacity, capacity);
    }

    /**
     * Create a {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer create(long address, int capacity) {
        return new Buffer(address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static VkAttachmentFeedbackLoopInfoEXT.@Nullable Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : new Buffer(address, capacity);
    }

    /**
     * Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static VkAttachmentFeedbackLoopInfoEXT malloc(MemoryStack stack) {
        return new VkAttachmentFeedbackLoopInfoEXT(stack.nmalloc(ALIGNOF, SIZEOF), null);
    }

    /**
     * Returns a new {@code VkAttachmentFeedbackLoopInfoEXT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static VkAttachmentFeedbackLoopInfoEXT calloc(MemoryStack stack) {
        return new VkAttachmentFeedbackLoopInfoEXT(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    /**
     * Returns a new {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer malloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkAttachmentFeedbackLoopInfoEXT.Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkAttachmentFeedbackLoopInfoEXT.Buffer calloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #sType}. */
    public static int nsType(long struct) { return memGetInt(struct + VkAttachmentFeedbackLoopInfoEXT.STYPE); }
    /** Unsafe version of {@link #pNext}. */
    public static long npNext(long struct) { return memGetAddress(struct + VkAttachmentFeedbackLoopInfoEXT.PNEXT); }
    /** Unsafe version of {@link #feedbackLoopEnable}. */
    public static int nfeedbackLoopEnable(long struct) { return memGetInt(struct + VkAttachmentFeedbackLoopInfoEXT.FEEDBACKLOOPENABLE); }

    /** Unsafe version of {@link #sType(int) sType}. */
    public static void nsType(long struct, int value) { memPutInt(struct + VkAttachmentFeedbackLoopInfoEXT.STYPE, value); }
    /** Unsafe version of {@link #pNext(long) pNext}. */
    public static void npNext(long struct, long value) { memPutAddress(struct + VkAttachmentFeedbackLoopInfoEXT.PNEXT, value); }
    /** Unsafe version of {@link #feedbackLoopEnable(boolean) feedbackLoopEnable}. */
    public static void nfeedbackLoopEnable(long struct, int value) { memPutInt(struct + VkAttachmentFeedbackLoopInfoEXT.FEEDBACKLOOPENABLE, value); }

    // -----------------------------------

    /** An array of {@link VkAttachmentFeedbackLoopInfoEXT} structs. */
    public static class Buffer extends StructBuffer<VkAttachmentFeedbackLoopInfoEXT, Buffer> implements NativeResource {

        private static final VkAttachmentFeedbackLoopInfoEXT ELEMENT_FACTORY = VkAttachmentFeedbackLoopInfoEXT.create(-1L);

        /**
         * Creates a new {@code VkAttachmentFeedbackLoopInfoEXT.Buffer} instance backed by the specified container.
         *
         * <p>Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link VkAttachmentFeedbackLoopInfoEXT#SIZEOF}, and its mark will be undefined.</p>
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
        protected VkAttachmentFeedbackLoopInfoEXT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code sType} field. */
        @NativeType("VkStructureType")
        public int sType() { return VkAttachmentFeedbackLoopInfoEXT.nsType(address()); }
        /** @return the value of the {@code pNext} field. */
        @NativeType("void const *")
        public long pNext() { return VkAttachmentFeedbackLoopInfoEXT.npNext(address()); }
        /** @return the value of the {@code feedbackLoopEnable} field. */
        @NativeType("VkBool32")
        public boolean feedbackLoopEnable() { return VkAttachmentFeedbackLoopInfoEXT.nfeedbackLoopEnable(address()) != 0; }

        /** Sets the specified value to the {@code sType} field. */
        public VkAttachmentFeedbackLoopInfoEXT.Buffer sType(@NativeType("VkStructureType") int value) { VkAttachmentFeedbackLoopInfoEXT.nsType(address(), value); return this; }
        /** Sets the {@link KHRUnifiedImageLayouts#VK_STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT} value to the {@code sType} field. */
        public VkAttachmentFeedbackLoopInfoEXT.Buffer sType$Default() { return sType(KHRUnifiedImageLayouts.VK_STRUCTURE_TYPE_ATTACHMENT_FEEDBACK_LOOP_INFO_EXT); }
        /** Sets the specified value to the {@code pNext} field. */
        public VkAttachmentFeedbackLoopInfoEXT.Buffer pNext(@NativeType("void const *") long value) { VkAttachmentFeedbackLoopInfoEXT.npNext(address(), value); return this; }
        /** Sets the specified value to the {@code feedbackLoopEnable} field. */
        public VkAttachmentFeedbackLoopInfoEXT.Buffer feedbackLoopEnable(@NativeType("VkBool32") boolean value) { VkAttachmentFeedbackLoopInfoEXT.nfeedbackLoopEnable(address(), value ? 1 : 0); return this; }

    }

}