/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import java.util.*;
import java.util.concurrent.*;

import static org.lwjgl.system.MemoryStack.*;

public class VirtualPromiseTest {

    private VirtualPromiseTest() { }

    public static void main() throws InterruptedException, ExecutionException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var list = new ArrayList<Callable<Long>>();
            for (int i = 0; i < 1024; i++) {
                var vti = i;
                list.add(() -> {
                    System.out.println("Yo from virtual thread: " + Thread.currentThread());
                    var promise = ForkJoinPool.commonPool().submit(() -> {
                        try (var stack = stackPush()) {
                            System.out.println("Yo from carrier thread: " + Thread.currentThread());
                            return Thread.currentThread().threadId();
                        }
                    });
                    try {
                        System.out.println("BEFORE GET: " + vti);
                        return promise.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        System.out.println("AFTER GET: " + vti);
                    }
                });
            }

            var results = executor.invokeAll(list);
            for (int i = 0; i < 1024; i++) {
                System.out.println(i + " - " + results.get(i).get());
            }
        }
    }
}
