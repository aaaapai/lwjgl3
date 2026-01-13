/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.jmh;

import org.lwjgl.system.*;
import org.lwjgl.system.ffm.*;
import org.openjdk.jmh.annotations.*;

import java.lang.foreign.*;
import java.lang.invoke.*;
import java.util.function.*;

@State(Scope.Benchmark)
public class StableValueTest {

    static {
        Library.initialize();
    }

    private interface FFMBindings {
        @FFMName("org_lwjgl_system_noop")
        void noop();
    }

    private static final SymbolLookup LOADER = SymbolLookup.libraryLookup("lwjgl", Arena.global());

    private static final FFMBindings BINDINGS = BindingGenerator.generate(MethodHandles.lookup(), FFMBindings.class, LOADER);

    private static final MemorySegment FUNCTION_NOOP = LOADER.find("org_lwjgl_system_noop").orElseThrow();

    private static final MethodHandle NOOP = Linker.nativeLinker()
        .downcallHandle(FunctionDescriptor.ofVoid())
        .bindTo(FUNCTION_NOOP);

    private static final Supplier<MethodHandle> NOOP_STABLE = StableValue.supplier(() -> {
        MemorySegment FUNCTION_NOOP = LOADER.find("org_lwjgl_system_noop").orElseThrow();
        return Linker.nativeLinker()
            .downcallHandle(FunctionDescriptor.ofVoid())
            .bindTo(FUNCTION_NOOP);
    });

    @Benchmark
    public void b01_noop_static_eager() {
        try {
            NOOP.invokeExact();
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

    @Benchmark
    public void b02_noop_stable_lazy() {
        try {
            NOOP_STABLE.get().invokeExact();
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

    @Benchmark
    public void b03_noop_gen_lazy() {
        BINDINGS.noop();
    }


}
