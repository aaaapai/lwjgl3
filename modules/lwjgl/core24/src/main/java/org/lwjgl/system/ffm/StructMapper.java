/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.foreign.*;

public non-sealed interface StructMapper<T extends Record> extends GroupMapper<StructLayout, T> {
    @Override
    public StructMapper<T> set(MemorySegment segment, long offset, T value);
    @Override
    public StructMapper<T> setAtIndex(MemorySegment segment, long index, T value);
}