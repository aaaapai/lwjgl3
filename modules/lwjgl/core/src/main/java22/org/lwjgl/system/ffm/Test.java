/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.system.ffm;

import java.lang.foreign.*;
import java.nio.charset.*;

public class Test {

    public static void main(String[] args) {
        var KERNEL32 = SymbolLookup
            .libraryLookup("kernel32", Arena.global());

        var LoadLibrary = Linker.nativeLinker()
            .downcallHandle(
                FunctionDescriptor.of(
                    ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS
                ),
                Linker.Option.captureCallState("GetLastError")
            )
            .bindTo(KERNEL32.find("LoadLibraryW").orElseThrow());

        var capturedStateLayout = Linker.Option.captureStateLayout();
        var GetLastErrorHandle = capturedStateLayout
            .varHandle(MemoryLayout.PathElement.groupElement("GetLastError"))
            .withInvokeBehavior();

        try (var arena = Arena.ofConfined()) {
            var LastError = arena.allocate(capturedStateLayout);

            var kernel32 = (MemorySegment)LoadLibrary.invokeExact(LastError, arena.allocateFrom("kernel32", StandardCharsets.UTF_16LE));

            System.out.println("kernel32 = " + kernel32);
            var error = (int)GetLastErrorHandle.get(LastError, 0L);
            System.out.println("error = " + error);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        //Linker.nativeLinker().defaultLookup().


        /*try (var arena = Arena.ofConfined()) {
            arena.allocate(1024);
            var s0 = arena.allocateFrom("test");

            var b = arena.allocate(1024);
            var a = SegmentAllocator.slicingAllocator(b);

            var s1 = a.allocateFrom("test");
        }*/

        //var lookup = Linker.nativeLinker().defaultLookup();
        //System.out.println("lookup.find(\"dlopen\") = " + lookup.find("malloc"));
    }

}
