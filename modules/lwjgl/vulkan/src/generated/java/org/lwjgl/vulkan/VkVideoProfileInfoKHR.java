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
 * struct VkVideoProfileInfoKHR {
 *     VkStructureType sType;
 *     void const * pNext;
 *     VkVideoCodecOperationFlagBitsKHR videoCodecOperation;
 *     VkVideoChromaSubsamplingFlagsKHR chromaSubsampling;
 *     VkVideoComponentBitDepthFlagsKHR lumaBitDepth;
 *     VkVideoComponentBitDepthFlagsKHR chromaBitDepth;
 * }}</pre>
 */
public class VkVideoProfileInfoKHR extends Struct<VkVideoProfileInfoKHR> implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        STYPE,
        PNEXT,
        VIDEOCODECOPERATION,
        CHROMASUBSAMPLING,
        LUMABITDEPTH,
        CHROMABITDEPTH;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(4),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        STYPE = layout.offsetof(0);
        PNEXT = layout.offsetof(1);
        VIDEOCODECOPERATION = layout.offsetof(2);
        CHROMASUBSAMPLING = layout.offsetof(3);
        LUMABITDEPTH = layout.offsetof(4);
        CHROMABITDEPTH = layout.offsetof(5);
    }

    protected VkVideoProfileInfoKHR(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected VkVideoProfileInfoKHR create(long address, @Nullable ByteBuffer container) {
        return new VkVideoProfileInfoKHR(address, container);
    }

    /**
     * Creates a {@code VkVideoProfileInfoKHR} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public VkVideoProfileInfoKHR(ByteBuffer container) {
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
    /** @return the value of the {@code videoCodecOperation} field. */
    @NativeType("VkVideoCodecOperationFlagBitsKHR")
    public int videoCodecOperation() { return nvideoCodecOperation(address()); }
    /** @return the value of the {@code chromaSubsampling} field. */
    @NativeType("VkVideoChromaSubsamplingFlagsKHR")
    public int chromaSubsampling() { return nchromaSubsampling(address()); }
    /** @return the value of the {@code lumaBitDepth} field. */
    @NativeType("VkVideoComponentBitDepthFlagsKHR")
    public int lumaBitDepth() { return nlumaBitDepth(address()); }
    /** @return the value of the {@code chromaBitDepth} field. */
    @NativeType("VkVideoComponentBitDepthFlagsKHR")
    public int chromaBitDepth() { return nchromaBitDepth(address()); }

    /** Sets the specified value to the {@code sType} field. */
    public VkVideoProfileInfoKHR sType(@NativeType("VkStructureType") int value) { nsType(address(), value); return this; }
    /** Sets the {@link KHRVideoQueue#VK_STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR} value to the {@code sType} field. */
    public VkVideoProfileInfoKHR sType$Default() { return sType(KHRVideoQueue.VK_STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR); }
    /** Sets the specified value to the {@code pNext} field. */
    public VkVideoProfileInfoKHR pNext(@NativeType("void const *") long value) { npNext(address(), value); return this; }
    /** Prepends the specified {@link VkVideoDecodeAV1ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoDecodeAV1ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoDecodeH264ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoDecodeH264ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoDecodeH265ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoDecodeH265ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoDecodeUsageInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoDecodeUsageInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoDecodeVP9ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoDecodeVP9ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoEncodeAV1ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoEncodeAV1ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoEncodeH264ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoEncodeH264ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoEncodeH265ProfileInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoEncodeH265ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Prepends the specified {@link VkVideoEncodeUsageInfoKHR} value to the {@code pNext} chain. */
    public VkVideoProfileInfoKHR pNext(VkVideoEncodeUsageInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
    /** Sets the specified value to the {@code videoCodecOperation} field. */
    public VkVideoProfileInfoKHR videoCodecOperation(@NativeType("VkVideoCodecOperationFlagBitsKHR") int value) { nvideoCodecOperation(address(), value); return this; }
    /** Sets the specified value to the {@code chromaSubsampling} field. */
    public VkVideoProfileInfoKHR chromaSubsampling(@NativeType("VkVideoChromaSubsamplingFlagsKHR") int value) { nchromaSubsampling(address(), value); return this; }
    /** Sets the specified value to the {@code lumaBitDepth} field. */
    public VkVideoProfileInfoKHR lumaBitDepth(@NativeType("VkVideoComponentBitDepthFlagsKHR") int value) { nlumaBitDepth(address(), value); return this; }
    /** Sets the specified value to the {@code chromaBitDepth} field. */
    public VkVideoProfileInfoKHR chromaBitDepth(@NativeType("VkVideoComponentBitDepthFlagsKHR") int value) { nchromaBitDepth(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public VkVideoProfileInfoKHR set(
        int sType,
        long pNext,
        int videoCodecOperation,
        int chromaSubsampling,
        int lumaBitDepth,
        int chromaBitDepth
    ) {
        sType(sType);
        pNext(pNext);
        videoCodecOperation(videoCodecOperation);
        chromaSubsampling(chromaSubsampling);
        lumaBitDepth(lumaBitDepth);
        chromaBitDepth(chromaBitDepth);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public VkVideoProfileInfoKHR set(VkVideoProfileInfoKHR src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code VkVideoProfileInfoKHR} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static VkVideoProfileInfoKHR malloc() {
        return new VkVideoProfileInfoKHR(nmemAllocChecked(SIZEOF), null);
    }

    /** Returns a new {@code VkVideoProfileInfoKHR} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static VkVideoProfileInfoKHR calloc() {
        return new VkVideoProfileInfoKHR(nmemCallocChecked(1, SIZEOF), null);
    }

    /** Returns a new {@code VkVideoProfileInfoKHR} instance allocated with {@link BufferUtils}. */
    public static VkVideoProfileInfoKHR create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return new VkVideoProfileInfoKHR(memAddress(container), container);
    }

    /** Returns a new {@code VkVideoProfileInfoKHR} instance for the specified memory address. */
    public static VkVideoProfileInfoKHR create(long address) {
        return new VkVideoProfileInfoKHR(address, null);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static @Nullable VkVideoProfileInfoKHR createSafe(long address) {
        return address == NULL ? null : new VkVideoProfileInfoKHR(address, null);
    }

    /**
     * Returns a new {@link VkVideoProfileInfoKHR.Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer malloc(int capacity) {
        return new Buffer(nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link VkVideoProfileInfoKHR.Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer calloc(int capacity) {
        return new Buffer(nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkVideoProfileInfoKHR.Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return new Buffer(memAddress(container), container, -1, 0, capacity, capacity);
    }

    /**
     * Create a {@link VkVideoProfileInfoKHR.Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer create(long address, int capacity) {
        return new Buffer(address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static VkVideoProfileInfoKHR.@Nullable Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : new Buffer(address, capacity);
    }

    /**
     * Returns a new {@code VkVideoProfileInfoKHR} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static VkVideoProfileInfoKHR malloc(MemoryStack stack) {
        return new VkVideoProfileInfoKHR(stack.nmalloc(ALIGNOF, SIZEOF), null);
    }

    /**
     * Returns a new {@code VkVideoProfileInfoKHR} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static VkVideoProfileInfoKHR calloc(MemoryStack stack) {
        return new VkVideoProfileInfoKHR(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    /**
     * Returns a new {@link VkVideoProfileInfoKHR.Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer malloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkVideoProfileInfoKHR.Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkVideoProfileInfoKHR.Buffer calloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #sType}. */
    public static int nsType(long struct) { return memGetInt(struct + VkVideoProfileInfoKHR.STYPE); }
    /** Unsafe version of {@link #pNext}. */
    public static long npNext(long struct) { return memGetAddress(struct + VkVideoProfileInfoKHR.PNEXT); }
    /** Unsafe version of {@link #videoCodecOperation}. */
    public static int nvideoCodecOperation(long struct) { return memGetInt(struct + VkVideoProfileInfoKHR.VIDEOCODECOPERATION); }
    /** Unsafe version of {@link #chromaSubsampling}. */
    public static int nchromaSubsampling(long struct) { return memGetInt(struct + VkVideoProfileInfoKHR.CHROMASUBSAMPLING); }
    /** Unsafe version of {@link #lumaBitDepth}. */
    public static int nlumaBitDepth(long struct) { return memGetInt(struct + VkVideoProfileInfoKHR.LUMABITDEPTH); }
    /** Unsafe version of {@link #chromaBitDepth}. */
    public static int nchromaBitDepth(long struct) { return memGetInt(struct + VkVideoProfileInfoKHR.CHROMABITDEPTH); }

    /** Unsafe version of {@link #sType(int) sType}. */
    public static void nsType(long struct, int value) { memPutInt(struct + VkVideoProfileInfoKHR.STYPE, value); }
    /** Unsafe version of {@link #pNext(long) pNext}. */
    public static void npNext(long struct, long value) { memPutAddress(struct + VkVideoProfileInfoKHR.PNEXT, value); }
    /** Unsafe version of {@link #videoCodecOperation(int) videoCodecOperation}. */
    public static void nvideoCodecOperation(long struct, int value) { memPutInt(struct + VkVideoProfileInfoKHR.VIDEOCODECOPERATION, value); }
    /** Unsafe version of {@link #chromaSubsampling(int) chromaSubsampling}. */
    public static void nchromaSubsampling(long struct, int value) { memPutInt(struct + VkVideoProfileInfoKHR.CHROMASUBSAMPLING, value); }
    /** Unsafe version of {@link #lumaBitDepth(int) lumaBitDepth}. */
    public static void nlumaBitDepth(long struct, int value) { memPutInt(struct + VkVideoProfileInfoKHR.LUMABITDEPTH, value); }
    /** Unsafe version of {@link #chromaBitDepth(int) chromaBitDepth}. */
    public static void nchromaBitDepth(long struct, int value) { memPutInt(struct + VkVideoProfileInfoKHR.CHROMABITDEPTH, value); }

    // -----------------------------------

    /** An array of {@link VkVideoProfileInfoKHR} structs. */
    public static class Buffer extends StructBuffer<VkVideoProfileInfoKHR, Buffer> implements NativeResource {

        private static final VkVideoProfileInfoKHR ELEMENT_FACTORY = VkVideoProfileInfoKHR.create(-1L);

        /**
         * Creates a new {@code VkVideoProfileInfoKHR.Buffer} instance backed by the specified container.
         *
         * <p>Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link VkVideoProfileInfoKHR#SIZEOF}, and its mark will be undefined.</p>
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
        protected VkVideoProfileInfoKHR getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code sType} field. */
        @NativeType("VkStructureType")
        public int sType() { return VkVideoProfileInfoKHR.nsType(address()); }
        /** @return the value of the {@code pNext} field. */
        @NativeType("void const *")
        public long pNext() { return VkVideoProfileInfoKHR.npNext(address()); }
        /** @return the value of the {@code videoCodecOperation} field. */
        @NativeType("VkVideoCodecOperationFlagBitsKHR")
        public int videoCodecOperation() { return VkVideoProfileInfoKHR.nvideoCodecOperation(address()); }
        /** @return the value of the {@code chromaSubsampling} field. */
        @NativeType("VkVideoChromaSubsamplingFlagsKHR")
        public int chromaSubsampling() { return VkVideoProfileInfoKHR.nchromaSubsampling(address()); }
        /** @return the value of the {@code lumaBitDepth} field. */
        @NativeType("VkVideoComponentBitDepthFlagsKHR")
        public int lumaBitDepth() { return VkVideoProfileInfoKHR.nlumaBitDepth(address()); }
        /** @return the value of the {@code chromaBitDepth} field. */
        @NativeType("VkVideoComponentBitDepthFlagsKHR")
        public int chromaBitDepth() { return VkVideoProfileInfoKHR.nchromaBitDepth(address()); }

        /** Sets the specified value to the {@code sType} field. */
        public VkVideoProfileInfoKHR.Buffer sType(@NativeType("VkStructureType") int value) { VkVideoProfileInfoKHR.nsType(address(), value); return this; }
        /** Sets the {@link KHRVideoQueue#VK_STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR} value to the {@code sType} field. */
        public VkVideoProfileInfoKHR.Buffer sType$Default() { return sType(KHRVideoQueue.VK_STRUCTURE_TYPE_VIDEO_PROFILE_INFO_KHR); }
        /** Sets the specified value to the {@code pNext} field. */
        public VkVideoProfileInfoKHR.Buffer pNext(@NativeType("void const *") long value) { VkVideoProfileInfoKHR.npNext(address(), value); return this; }
        /** Prepends the specified {@link VkVideoDecodeAV1ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoDecodeAV1ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoDecodeH264ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoDecodeH264ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoDecodeH265ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoDecodeH265ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoDecodeUsageInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoDecodeUsageInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoDecodeVP9ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoDecodeVP9ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoEncodeAV1ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoEncodeAV1ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoEncodeH264ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoEncodeH264ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoEncodeH265ProfileInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoEncodeH265ProfileInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Prepends the specified {@link VkVideoEncodeUsageInfoKHR} value to the {@code pNext} chain. */
        public VkVideoProfileInfoKHR.Buffer pNext(VkVideoEncodeUsageInfoKHR value) { return this.pNext(value.pNext(this.pNext()).address()); }
        /** Sets the specified value to the {@code videoCodecOperation} field. */
        public VkVideoProfileInfoKHR.Buffer videoCodecOperation(@NativeType("VkVideoCodecOperationFlagBitsKHR") int value) { VkVideoProfileInfoKHR.nvideoCodecOperation(address(), value); return this; }
        /** Sets the specified value to the {@code chromaSubsampling} field. */
        public VkVideoProfileInfoKHR.Buffer chromaSubsampling(@NativeType("VkVideoChromaSubsamplingFlagsKHR") int value) { VkVideoProfileInfoKHR.nchromaSubsampling(address(), value); return this; }
        /** Sets the specified value to the {@code lumaBitDepth} field. */
        public VkVideoProfileInfoKHR.Buffer lumaBitDepth(@NativeType("VkVideoComponentBitDepthFlagsKHR") int value) { VkVideoProfileInfoKHR.nlumaBitDepth(address(), value); return this; }
        /** Sets the specified value to the {@code chromaBitDepth} field. */
        public VkVideoProfileInfoKHR.Buffer chromaBitDepth(@NativeType("VkVideoComponentBitDepthFlagsKHR") int value) { VkVideoProfileInfoKHR.nchromaBitDepth(address(), value); return this; }

    }

}