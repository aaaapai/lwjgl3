/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;
import java.lang.invoke.*;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.ffm.StackArena.*;

public class BindingTest {

    //@FFMCritical
    @FFMPrefix("glfw")
    @FFMCharset("UTF_8")
    public interface MyGLFWBindings {
        int Init();
        void DefaultWindowHints();
        void WindowHint(int hint, int value);
        MemorySegment CreateWindow(int width, int height, MemorySegment title, MemorySegment monitor, MemorySegment share);
        MemorySegment CreateWindow(int width, int height, String title, MemorySegment monitor, MemorySegment share);
        void DestroyWindow(MemorySegment window);
        void SetWindowSizeLimits(MemorySegment window, int minwidth, int minheight, int maxwidth, int maxheight);
        MemorySegment GetMonitors(MemorySegment count);
        MemorySegment GetPrimaryMonitor();
        void GetMonitorPos(MemorySegment monitor, MemorySegment xpos, MemorySegment ypos);
        String GetMonitorName(MemorySegment monitor);
        MemorySegment GetVideoMode(MemorySegment monitor);
        void SetWindowPos(MemorySegment window, int xpos, int ypos);
        void MakeContextCurrent(MemorySegment window);
        void SwapInterval(int interval);
        void ShowWindow(MemorySegment window);
        int WindowShouldClose(MemorySegment window);
        void PollEvents();
        void SwapBuffers(MemorySegment window);
    }

    private static final SymbolLookup   loader = SymbolLookup.libraryLookup(Objects.requireNonNull(org.lwjgl.glfw.GLFW.getLibrary().getPath()), Arena.global());
    private static final MyGLFWBindings glfw   = BindingGenerator.generate(MethodHandles.lookup(), MyGLFWBindings.class, loader);

    public static void main(String[] args) {
        System.err.println("BEFORE Init");
        if (glfw.Init() == 0) {
            throw new IllegalStateException("Unable to initialize glfw");
        }

        glfw.DefaultWindowHints();
        glfw.WindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfw.WindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfw.WindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        glfw.WindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        if (Platform.get() == Platform.MACOSX) {
            glfw.WindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
        }

        System.err.println("BEFORE Monitors");

        try (var arena = stackPush()) {
            var count_p     = arena.allocateFrom(ValueLayout.JAVA_INT, 1);
            var xpos_p      = arena.allocateFrom(ValueLayout.JAVA_INT, 1);
            var ypos_p      = arena.allocateFrom(ValueLayout.JAVA_INT, 1);
            var monitors_pp = glfw.GetMonitors(count_p);
            if (!MemorySegment.NULL.equals(monitors_pp)) {
                var count    = count_p.getAtIndex(ValueLayout.JAVA_INT, 0);
                var monitors = monitors_pp.reinterpret(ValueLayout.ADDRESS.scale(0, count));
                for (int i = 0; i < count; i++) {
                    var monitor = monitors.getAtIndex(ValueLayout.ADDRESS, i);
                    glfw.GetMonitorPos(monitor, xpos_p, ypos_p);

                    var name = glfw.GetMonitorName(monitor);
                    System.out.println("Monitor #" + i + " " + name + " at " + xpos_p.getAtIndex(ValueLayout.JAVA_INT, 0) + "," + ypos_p.getAtIndex(ValueLayout.JAVA_INT, 0));
                }
            }

        }

        System.err.println("BEFORE CreateWindow");

        int WIDTH  = 300;
        int HEIGHT = 300;

        /*MemorySegment window;
        try (var arena = Arena.ofConfined()) {
            window = glfw.CreateWindow(WIDTH, HEIGHT, arena.allocateFrom("GLFW Gears Demo"), MemorySegment.NULL, MemorySegment.NULL);
        }*/
        var window = glfw.CreateWindow(WIDTH, HEIGHT, "GLFW Gears Demo", MemorySegment.NULL, MemorySegment.NULL);
        if (MemorySegment.NULL.equals(window)) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfw.SetWindowSizeLimits(window, WIDTH, HEIGHT, GLFW_DONT_CARE, GLFW_DONT_CARE);
        //glfw.SetWindowAspectRatio(window, 1, 1);

        var monitor = glfw.GetPrimaryMonitor();

        var vidmode = Objects.requireNonNull(glfw.GetVideoMode(monitor)).reinterpret(org.lwjgl.glfw.GLFWVidMode.SIZEOF);
        // Center window
        glfw.SetWindowPos(
            window,
            (vidmode.get(ValueLayout.JAVA_INT, 0) - WIDTH) / 2,
            (vidmode.get(ValueLayout.JAVA_INT, 4) - HEIGHT) / 2
        );

        glfw.MakeContextCurrent(window);

        GL.createCapabilities(MemoryUtil::memCallocPointer);
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        glfw.SwapInterval(1);
        glfw.ShowWindow(window);

        System.err.println("BEFORE render loop");

        while (glfw.WindowShouldClose(window) == 0) {
            glfw.PollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfw.SwapBuffers(window);
        }

        glfw.DestroyWindow(window);
    }

}