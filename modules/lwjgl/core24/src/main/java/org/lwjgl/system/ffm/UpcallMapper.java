/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.foreign.*;
import java.lang.invoke.*;

public interface UpcallMapper<T> {

    FunctionDescriptor descriptor();
    MethodHandle handle();

    default MemorySegment allocate(T upcall) {
        return allocate(Arena.global(), upcall);
    }

    default MemorySegment allocate(Arena arena, T upcall) {
        return Linker.nativeLinker()
            .upcallStub(
                handle().bindTo(upcall),
                descriptor(),
                arena
            );
    }

}
