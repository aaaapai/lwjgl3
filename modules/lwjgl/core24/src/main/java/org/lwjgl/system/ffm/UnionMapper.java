/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.foreign.*;

public non-sealed interface UnionMapper<T extends Record> extends GroupMapper<UnionLayout, T> {
    @Override
    public UnionMapper<T> set(MemorySegment segment, long offset, T value);
    @Override
    public UnionMapper<T> setAtIndex(MemorySegment segment, long index, T value);
}