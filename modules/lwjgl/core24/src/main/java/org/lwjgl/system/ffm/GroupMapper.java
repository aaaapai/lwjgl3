/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.foreign.*;
import java.util.function.*;

public sealed interface GroupMapper<L extends GroupLayout, T extends Record>
    permits StructMapper, UnionMapper {

    public L layout();

    public T ofAddress(long address);

    public default T get(MemorySegment segment) {
        return get(reinterpret(segment), 0L);
    }
    public T get(MemorySegment segment, long offset);
    public T getAtIndex(MemorySegment segment, long index);

    public default MemorySegment set(MemorySegment segment, T value) {
        set(segment, 0L, value);
        return segment;
    }

    public GroupMapper<L, T> set(MemorySegment segment, long offset, T value);
    public GroupMapper<L, T> setAtIndex(MemorySegment segment, long index, T value);

    public default long sizeof()  { return layout().byteSize(); }
    public default long alignof() { return layout().byteAlignment(); }

    public default MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    public default MemorySegment asSlice(MemorySegment array, long index, long elementCount) {
        var sizeof = layout().byteSize();
        return array.asSlice(sizeof * index, sizeof * elementCount, layout().byteAlignment());
    }

    public default MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }
    public default MemorySegment allocate(SegmentAllocator allocator, long elementCount) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    public default MemorySegment reinterpret(MemorySegment addr) {
        return addr.reinterpret(layout().byteSize());
    }
    public default MemorySegment reinterpret(MemorySegment addr, long elementCount) {
        return addr.reinterpret(layout().byteSize() * elementCount);
    }
    public default MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize(), arena, cleanup);
    }
    public default MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }

}