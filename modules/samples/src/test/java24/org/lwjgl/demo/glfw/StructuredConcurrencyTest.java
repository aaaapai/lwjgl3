/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.*;

import java.util.concurrent.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class StructuredConcurrencyTest {

    private StructuredConcurrencyTest() { }

    private static final ScopedValue<MemoryStack> SCOPED_STACK = ScopedValue.newInstance();

    public static void main() {
        try (var threadStack = stackPush()) {
            ScopedValue.where(SCOPED_STACK, threadStack).run(() -> {
                try (var stack = SCOPED_STACK.get().push()) {
                    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                        scope.fork(() -> {
                            ScopedValue.where(SCOPED_STACK, MemoryStack.create(stack.malloc(1024)))
                                .run(() -> compilePipeline("shadow pass"));
                            return false;
                        });

                        scope.fork(() -> {
                            ScopedValue.where(SCOPED_STACK, MemoryStack.create(stack.malloc(1024)))
                                .run(() -> compilePipeline("light pass"));
                            return false;
                        });

                        scope.join().throwIfFailed();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private static boolean compilePipeline(String pipelineName) {
        System.out.println("COMPILING PIPELINE: " + pipelineName + " in " + Thread.currentThread());
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            try (var stack = SCOPED_STACK.get().push()) {
                for (var i = 0; i < 4; i++) {
                    var permutation = i;
                    scope.fork(() -> {
                        ScopedValue.where(SCOPED_STACK, MemoryStack.create(stack.malloc(256)))
                            .run(() -> compilePermutation(pipelineName, permutation));
                        return false;
                    });
                }
                scope.join().throwIfFailed();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private static boolean compilePermutation(String pipelineName, int permutation) {
        System.out.println("COMPILING PERMUTATION: " + pipelineName + " #" + permutation + " in " + Thread.currentThread());
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            try (var stack = SCOPED_STACK.get().push()) {
                scope.fork(() -> {
                    ScopedValue.where(SCOPED_STACK, MemoryStack.create(stack.malloc(128)))
                        .run(() -> compileShader(pipelineName, permutation, "vertex"));
                    return false;
                });

                scope.fork(() -> {
                    ScopedValue.where(SCOPED_STACK, MemoryStack.create(stack.malloc(128)))
                        .run(() -> compileShader(pipelineName, permutation, "fragment"));
                    return false;
                });
            }
            scope.join().throwIfFailed();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private static boolean compileShader(String pipelineName, int permutation, String type) {
        try (var stack = SCOPED_STACK.get().push()) {
            System.out.println("COMPILING SHADER: " + type + " of " + pipelineName + " #" + permutation + " in " + Thread.currentThread());
            for (int i = 0; i < 8; i++) {
                System.out.println(memAddress(stack.malloc(16)));
            }
        }
        return false;
    }
}
