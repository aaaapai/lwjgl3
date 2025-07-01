/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.*;

import java.lang.foreign.*;
import java.util.*;
import java.util.concurrent.*;

public class TLUTest {

    private TLUTest() { }

    public static void main() throws InterruptedException, ExecutionException {
        var uniqueStacks = new ConcurrentHashMap<>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var i = 0; i < 5000; i++) {
                //var id = i;
                executor.submit(() -> {
                    var stack = ThreadLocalUtil.getMemoryStack();
                    uniqueStacks.put(stack, Boolean.FALSE);

                    /*long t = System.nanoTime();
                    for (int j = 0; j < 100; j++) {
                        stack = ThreadLocalUtil.getMemoryStack();
                    }
                    t = (System.nanoTime() - t);
                    System.err.println(id + " - " + stack + " - " + t);*/
                });
            }
        }
        //System.err.println(uniqueStacks);
        System.err.println(uniqueStacks.size());
    }
}