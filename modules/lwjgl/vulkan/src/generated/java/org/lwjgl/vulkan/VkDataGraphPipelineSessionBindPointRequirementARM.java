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
 * struct VkDataGraphPipelineSessionBindPointRequirementARM {
 *     VkStructureType sType;
 *     void const * pNext;
 *     VkDataGraphPipelineSessionBindPointARM bindPoint;
 *     VkDataGraphPipelineSessionBindPointTypeARM bindPointType;
 *     uint32_t numObjects;
 * }}</pre>
 */
public class VkDataGraphPipelineSessionBindPointRequirementARM extends Struct<VkDataGraphPipelineSessionBindPointRequirementARM> implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        STYPE,
        PNEXT,
        BINDPOINT,
        BINDPOINTTYPE,
        NUMOBJECTS;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        STYPE = layout.offsetof(0);
        PNEXT = layout.offsetof(1);
        BINDPOINT = layout.offsetof(2);
        BINDPOINTTYPE = layout.offsetof(3);
        NUMOBJECTS = layout.offsetof(4);
    }

    protected VkDataGraphPipelineSessionBindPointRequirementARM(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected VkDataGraphPipelineSessionBindPointRequirementARM create(long address, @Nullable ByteBuffer container) {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(address, container);
    }

    /**
     * Creates a {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public VkDataGraphPipelineSessionBindPointRequirementARM(ByteBuffer container) {
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
    /** @return the value of the {@code bindPoint} field. */
    @NativeType("VkDataGraphPipelineSessionBindPointARM")
    public int bindPoint() { return nbindPoint(address()); }
    /** @return the value of the {@code bindPointType} field. */
    @NativeType("VkDataGraphPipelineSessionBindPointTypeARM")
    public int bindPointType() { return nbindPointType(address()); }
    /** @return the value of the {@code numObjects} field. */
    @NativeType("uint32_t")
    public int numObjects() { return nnumObjects(address()); }

    /** Sets the specified value to the {@code sType} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM sType(@NativeType("VkStructureType") int value) { nsType(address(), value); return this; }
    /** Sets the {@link ARMDataGraph#VK_STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM} value to the {@code sType} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM sType$Default() { return sType(ARMDataGraph.VK_STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM); }
    /** Sets the specified value to the {@code pNext} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM pNext(@NativeType("void const *") long value) { npNext(address(), value); return this; }
    /** Sets the specified value to the {@code bindPoint} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM bindPoint(@NativeType("VkDataGraphPipelineSessionBindPointARM") int value) { nbindPoint(address(), value); return this; }
    /** Sets the specified value to the {@code bindPointType} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM bindPointType(@NativeType("VkDataGraphPipelineSessionBindPointTypeARM") int value) { nbindPointType(address(), value); return this; }
    /** Sets the specified value to the {@code numObjects} field. */
    public VkDataGraphPipelineSessionBindPointRequirementARM numObjects(@NativeType("uint32_t") int value) { nnumObjects(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public VkDataGraphPipelineSessionBindPointRequirementARM set(
        int sType,
        long pNext,
        int bindPoint,
        int bindPointType,
        int numObjects
    ) {
        sType(sType);
        pNext(pNext);
        bindPoint(bindPoint);
        bindPointType(bindPointType);
        numObjects(numObjects);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public VkDataGraphPipelineSessionBindPointRequirementARM set(VkDataGraphPipelineSessionBindPointRequirementARM src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static VkDataGraphPipelineSessionBindPointRequirementARM malloc() {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(nmemAllocChecked(SIZEOF), null);
    }

    /** Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static VkDataGraphPipelineSessionBindPointRequirementARM calloc() {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(nmemCallocChecked(1, SIZEOF), null);
    }

    /** Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance allocated with {@link BufferUtils}. */
    public static VkDataGraphPipelineSessionBindPointRequirementARM create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return new VkDataGraphPipelineSessionBindPointRequirementARM(memAddress(container), container);
    }

    /** Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance for the specified memory address. */
    public static VkDataGraphPipelineSessionBindPointRequirementARM create(long address) {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(address, null);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static @Nullable VkDataGraphPipelineSessionBindPointRequirementARM createSafe(long address) {
        return address == NULL ? null : new VkDataGraphPipelineSessionBindPointRequirementARM(address, null);
    }

    /**
     * Returns a new {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer malloc(int capacity) {
        return new Buffer(nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer calloc(int capacity) {
        return new Buffer(nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return new Buffer(memAddress(container), container, -1, 0, capacity, capacity);
    }

    /**
     * Create a {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer create(long address, int capacity) {
        return new Buffer(address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.@Nullable Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : new Buffer(address, capacity);
    }

    /**
     * Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM malloc(MemoryStack stack) {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(stack.nmalloc(ALIGNOF, SIZEOF), null);
    }

    /**
     * Returns a new {@code VkDataGraphPipelineSessionBindPointRequirementARM} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM calloc(MemoryStack stack) {
        return new VkDataGraphPipelineSessionBindPointRequirementARM(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    /**
     * Returns a new {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer malloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static VkDataGraphPipelineSessionBindPointRequirementARM.Buffer calloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #sType}. */
    public static int nsType(long struct) { return memGetInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.STYPE); }
    /** Unsafe version of {@link #pNext}. */
    public static long npNext(long struct) { return memGetAddress(struct + VkDataGraphPipelineSessionBindPointRequirementARM.PNEXT); }
    /** Unsafe version of {@link #bindPoint}. */
    public static int nbindPoint(long struct) { return memGetInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.BINDPOINT); }
    /** Unsafe version of {@link #bindPointType}. */
    public static int nbindPointType(long struct) { return memGetInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.BINDPOINTTYPE); }
    /** Unsafe version of {@link #numObjects}. */
    public static int nnumObjects(long struct) { return memGetInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.NUMOBJECTS); }

    /** Unsafe version of {@link #sType(int) sType}. */
    public static void nsType(long struct, int value) { memPutInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.STYPE, value); }
    /** Unsafe version of {@link #pNext(long) pNext}. */
    public static void npNext(long struct, long value) { memPutAddress(struct + VkDataGraphPipelineSessionBindPointRequirementARM.PNEXT, value); }
    /** Unsafe version of {@link #bindPoint(int) bindPoint}. */
    public static void nbindPoint(long struct, int value) { memPutInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.BINDPOINT, value); }
    /** Unsafe version of {@link #bindPointType(int) bindPointType}. */
    public static void nbindPointType(long struct, int value) { memPutInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.BINDPOINTTYPE, value); }
    /** Unsafe version of {@link #numObjects(int) numObjects}. */
    public static void nnumObjects(long struct, int value) { memPutInt(struct + VkDataGraphPipelineSessionBindPointRequirementARM.NUMOBJECTS, value); }

    // -----------------------------------

    /** An array of {@link VkDataGraphPipelineSessionBindPointRequirementARM} structs. */
    public static class Buffer extends StructBuffer<VkDataGraphPipelineSessionBindPointRequirementARM, Buffer> implements NativeResource {

        private static final VkDataGraphPipelineSessionBindPointRequirementARM ELEMENT_FACTORY = VkDataGraphPipelineSessionBindPointRequirementARM.create(-1L);

        /**
         * Creates a new {@code VkDataGraphPipelineSessionBindPointRequirementARM.Buffer} instance backed by the specified container.
         *
         * <p>Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link VkDataGraphPipelineSessionBindPointRequirementARM#SIZEOF}, and its mark will be undefined.</p>
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
        protected VkDataGraphPipelineSessionBindPointRequirementARM getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code sType} field. */
        @NativeType("VkStructureType")
        public int sType() { return VkDataGraphPipelineSessionBindPointRequirementARM.nsType(address()); }
        /** @return the value of the {@code pNext} field. */
        @NativeType("void const *")
        public long pNext() { return VkDataGraphPipelineSessionBindPointRequirementARM.npNext(address()); }
        /** @return the value of the {@code bindPoint} field. */
        @NativeType("VkDataGraphPipelineSessionBindPointARM")
        public int bindPoint() { return VkDataGraphPipelineSessionBindPointRequirementARM.nbindPoint(address()); }
        /** @return the value of the {@code bindPointType} field. */
        @NativeType("VkDataGraphPipelineSessionBindPointTypeARM")
        public int bindPointType() { return VkDataGraphPipelineSessionBindPointRequirementARM.nbindPointType(address()); }
        /** @return the value of the {@code numObjects} field. */
        @NativeType("uint32_t")
        public int numObjects() { return VkDataGraphPipelineSessionBindPointRequirementARM.nnumObjects(address()); }

        /** Sets the specified value to the {@code sType} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer sType(@NativeType("VkStructureType") int value) { VkDataGraphPipelineSessionBindPointRequirementARM.nsType(address(), value); return this; }
        /** Sets the {@link ARMDataGraph#VK_STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM} value to the {@code sType} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer sType$Default() { return sType(ARMDataGraph.VK_STRUCTURE_TYPE_DATA_GRAPH_PIPELINE_SESSION_BIND_POINT_REQUIREMENT_ARM); }
        /** Sets the specified value to the {@code pNext} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer pNext(@NativeType("void const *") long value) { VkDataGraphPipelineSessionBindPointRequirementARM.npNext(address(), value); return this; }
        /** Sets the specified value to the {@code bindPoint} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer bindPoint(@NativeType("VkDataGraphPipelineSessionBindPointARM") int value) { VkDataGraphPipelineSessionBindPointRequirementARM.nbindPoint(address(), value); return this; }
        /** Sets the specified value to the {@code bindPointType} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer bindPointType(@NativeType("VkDataGraphPipelineSessionBindPointTypeARM") int value) { VkDataGraphPipelineSessionBindPointRequirementARM.nbindPointType(address(), value); return this; }
        /** Sets the specified value to the {@code numObjects} field. */
        public VkDataGraphPipelineSessionBindPointRequirementARM.Buffer numObjects(@NativeType("uint32_t") int value) { VkDataGraphPipelineSessionBindPointRequirementARM.nnumObjects(address(), value); return this; }

    }

}