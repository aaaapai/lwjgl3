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
public class FFMOverheadTest {

    static {
        Library.initialize();
    }

    private static final SymbolLookup loader = SymbolLookup.libraryLookup("lwjgl", Arena.global());

    private static final MethodHandle NOOP = Linker.nativeLinker()
        .downcallHandle(FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS))
        .bindTo(loader.find("Java_org_lwjgl_system_jni_JNINativeInterface_noop").orElseThrow());

    private interface FFMBindings {
        @FFMName("Java_org_lwjgl_system_jni_JNINativeInterface_noop")
        void noop(MemorySegment JNIEnv, MemorySegment clazz);

        @FFMCritical
        @FFMName("Java_org_lwjgl_system_jni_JNINativeInterface_noop")
        void noop_crit(MemorySegment JNIEnv, MemorySegment clazz);
    }

    private static final FFMBindings BINDINGS = BindingGenerator.generate(MethodHandles.lookup(), FFMBindings.class, loader);

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
        BINDINGS.noop(MemorySegment.NULL, MemorySegment.NULL);
    }

    @Benchmark
    public void noop_crit() {
        BINDINGS.noop_crit(MemorySegment.NULL, MemorySegment.NULL);
    }

}