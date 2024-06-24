/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.jmh;

import org.lwjgl.system.*;
import org.lwjgl.system.ffm.*;
import org.lwjgl.system.jni.*;
import org.openjdk.jmh.annotations.*;

import java.lang.foreign.*;
import java.lang.invoke.*;

@State(Scope.Benchmark)
public class JNIOverheadTest {

    static {
        Library.initialize();
    }

    private static final SymbolLookup loader = SymbolLookup.libraryLookup("lwjgl", Arena.global());

    private static final MethodHandle NOOP = Linker.nativeLinker()
        .downcallHandle(FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))
        .bindTo(loader.find("Java_org_lwjgl_system_jni_JNINativeInterface_noop").orElseThrow());

    private interface Functions {
        @FFMName("Java_org_lwjgl_system_jni_JNINativeInterface_noop")
        void noop(MemorySegment JNIEnv, MemorySegment clazz);
    }

    private static final Functions FUNCTIONS = BindingGenerator.generate(MethodHandles.lookup(), Functions.class, loader);

    @Benchmark
    public void noop_jni() {
        JNINativeInterface.noop();
    }

    @Benchmark
    public void noop_ffm() {
        try {
            NOOP.invokeExact(MemorySegment.NULL, MemorySegment.NULL);
        } catch (Throwable e) {
            throw new AssertionError(e);
        }
    }

    @Benchmark
    public void noop_gen() {
        FUNCTIONS.noop(MemorySegment.NULL, MemorySegment.NULL);
    }

}

