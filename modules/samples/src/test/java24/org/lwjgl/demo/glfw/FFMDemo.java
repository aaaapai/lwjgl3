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
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.ffm.StackArena.*;

public final class FFMDemo {

    static final SymbolLookup GLFW_LOADER = SymbolLookup.libraryLookup(Objects.requireNonNull(org.lwjgl.glfw.GLFW.getLibrary().getPath()), Arena.global());

    private static final TraceConsumer TRACER = (method, returnValue, args) -> {
        var prefix = method.getDeclaringClass().getAnnotation(FFMPrefix.class);
        if (prefix != null) {
            System.err.print(prefix.value());
        }
        System.err.println(method.getName() + '(' + Stream.of(args)
            .skip(1)
            .map(FFMDemo::render)
            .collect(Collectors.joining(", ")) + ")" + (returnValue == null ? "" : " : " + render(returnValue)));
    };

    static final MyGLFWTypes GLFW = BindingGenerator.generate(MethodHandles.lookup(), MyGLFWTypes.class);
    static final MyGLFW      glfw = BindingGenerator.generate(
        MethodHandles.lookup(),
        MyGLFW.class,
        BindingConfig.builder()
            .withLookup(GLFW_LOADER)
            .withMappers(GLFW)
            //.withTracing(TRACER/*, method -> method.getParameterCount() != 0 && method.getParameters()[0].getType() == MemorySegment.class*/)
            .build()
    );

    private FFMDemo() {
    }

    public static void main(String[] ignored) {
        var callbackArena = Arena.ofConfined();

        //callbackArena.allocate(234, 34);

        glfw.SetErrorCallback(callbackArena, (error_code, description) -> {
            System.err.format("GLFW Error %d: %s%n", error_code, description);

            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (int i = 2; i < stack.length; i++) {
                System.err.format("\t%s%n", stack[i]);
            }
        });

        if (!glfw.Init()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }

        // trigger error
        //glfw.WindowHint(0xDEAD_C0D3, GLFW_TRUE);

        glfw.DefaultWindowHints();
        glfw.WindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfw.WindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfw.WindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        glfw.WindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        if (Platform.get() == Platform.MACOSX) {
            glfw.WindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
        }

        try (var stack = stackPush()) {
            var count_p     = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
            var monitors_pp = glfw.GetMonitors(count_p);
            if (!MemorySegment.NULL.equals(monitors_pp)) {
                var count    = count_p.getAtIndex(ValueLayout.JAVA_INT, 0);
                var monitors = monitors_pp.reinterpret(ValueLayout.ADDRESS.scale(0, count));

                var xpos = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
                var ypos = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
                for (int i = 0; i < count; i++) {
                    var monitor = monitors.getAtIndex(ValueLayout.ADDRESS, i).address();
                    glfw.GetMonitorPos(monitor, xpos, ypos);

                    var name = glfw.GetMonitorName(monitor);
                    System.err.printf(
                        "Monitor #%d %s at %d,%d%n", i, name,
                        xpos.getAtIndex(ValueLayout.JAVA_INT, 0),
                        ypos.getAtIndex(ValueLayout.JAVA_INT, 0)
                    );
                }
            }
        }

        int WIDTH  = 300;
        int HEIGHT = 300;

        var window = glfw.CreateWindow(WIDTH, HEIGHT, "GLFW Demo", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //var gr = GLFW.gammaramp().get(MemorySegment.NULL, 0);
        //var foo = new MyGLFWTypes.gammaramp().red()


        /*
        var mapper = glfw.vidmode();

        var random = glfw.vidmode().ofAddress(10L);

        var s = MemorySegment.ofAddress(0L)
            .reinterpret(glfw.vidmode().sizeof() * 100L);

        var s2 = glfw.vidmode()
            .reinterpret(MemorySegment.ofAddress(0L), 100L);

        glfw.vidmode().setAtIndex(s, 10L, new GLFWvidmode(1024, 768, 8, 8, 8, 60));
        var b = glfw.vidmode().getAtIndex(s, 10L);

        System.out.println(glfw.vidmode().sizeof());

        glfw.vidmode()
            .setAtIndex(MemorySegment.NULL, 0L, new GLFWvidmode(1024, 768, 8, 8, 8, 60))
            .setAtIndex(MemorySegment.NULL, 1L, new GLFWvidmode(1024, 768, 8, 8, 8, 60));*/

        /*try (var arena = Arena.ofConfined()) {
            var s = GLFWvector4f().allocate(arena, 100);

            var v = GLFWvector4f().getAtIndex(s, 50);

            System.out.println(v.y());
            System.out.println(v.z());
        }*/

        glfw.SetWindowSizeLimits(window, WIDTH, HEIGHT, GLFW_DONT_CARE, GLFW_DONT_CARE);
        //glfw.SetWindowAspectRatio(window, 1, 1);

        var monitor = glfw.GetPrimaryMonitor();

        //var gammaRamp = glfw.GetGammaRamp(monitor);
        //System.out.println(gammaRamp);

        /*var ramp = glfw.gammaramp().get(glfw.GetGammaRamp(monitor));

        try (var stack = stackPush()) {
            var s = glfw.gammaramp().allocate(stack);
            glfw.SetGammaRamp(monitor, glfw.gammaramp().set(s, new GLFWgammaramp(
                ramp.red(),
                stack.allocate(ValueLayout.JAVA_SHORT, ramp.size()).fill((byte)0),
                ramp.blue(),
                ramp.size()
            )));
        }*/

        var vidmode = Objects.requireNonNull(glfw.GetVideoMode(monitor)).reinterpret(org.lwjgl.glfw.GLFWVidMode.SIZEOF);
        // Center window
        glfw.SetWindowPos(
            window,
            /*
            (vidmode.width() - WIDTH) / 2,
            (vidmode.height() - HEIGHT) / 2
             */
            (vidmode.get(ValueLayout.JAVA_INT, 0) - WIDTH) / 2,
            (vidmode.get(ValueLayout.JAVA_INT, 4) - HEIGHT) / 2
        );

        glfw.MakeContextCurrent(window);

        var OPENGL_PROVIDER = Objects.requireNonNull(GL.getFunctionProvider());

        // This is a like a lazy version of GL.createCapabilities(). Function pointers for the currently bound context will be lazily loaded on first use. No
        // GLCapabilities required! The owner of the generated bindings is responsible for using it only with the current context/thread.
        var gl = BindingGenerator.generate(
            MethodHandles.lookup(),
            MyOpenGL.class,
            BindingConfig.builder()
                .withLookup(OPENGL_PROVIDER)
                //.withTracing(TRACER/*, method -> method.getName().contains("ClearColor")*/)
                .withCriticalOverride(method -> method.getName().contains("Clear") ? Boolean.TRUE : null)
                .build()
        );

        // Do some random OpenGL calls to test the bindings
        try (var stack = stackPush()) {
            System.err.println("GL_VERSION = " + gl.GetString(GL_VERSION));
            System.err.println("GL_MAX_TEXTURE_SIZE = " + gl.GetInteger(stack, GL_MAX_TEXTURE_SIZE));

            var vsh = gl.CreateShader(GL_VERTEX_SHADER);

            var header = stack.allocateFrom("#version 120\n#line 0\n", StandardCharsets.ISO_8859_1);
            var code = stack.allocateFrom("""
                    uniform mat4 u_MVP;
                    uniform mat3 u_NORMAL;
                    
                    uniform vec3 u_LIGHT;
                    
                    attribute vec3 in_Position;
                    attribute vec3 in_Normal;
                    
                    varying float v_Shade;
                    
                    void main() {
                        vec3 normal = normalize(u_NORMAL * in_Normal);
                        v_Shade = max(dot(normal, u_LIGHT), 0.0);
                        gl_Position = u_MVP * vec4(in_Position, 1.0);
                    }""",
                StandardCharsets.UTF_8
            );

            var strings = stack.allocate(ValueLayout.ADDRESS, 2L);
            strings.setAtIndex(ValueLayout.ADDRESS, 0L, header);
            strings.setAtIndex(ValueLayout.ADDRESS, 1L, code);

            var lengths = stack.allocate(ValueLayout.JAVA_INT, 2L);
            lengths.setAtIndex(ValueLayout.JAVA_INT, 0L, (int)header.byteSize() - 1);
            lengths.setAtIndex(ValueLayout.JAVA_INT, 1L, (int)code.byteSize() - 1);

            gl.ShaderSource(vsh, 2, strings, lengths);

            gl.CompileShader(vsh);

            var infoLogLength = gl.GetShaderi(vsh, GL_INFO_LOG_LENGTH);
            if (infoLogLength != 0) {
                System.err.println("Shader Info Log:");
                System.err.println("----------------");
                System.err.println(gl.GetShaderInfoLog(stack, vsh, infoLogLength));
                System.err.println("----------------");
            }

            var sourceLength = gl.GetShaderi(vsh, GL_SHADER_SOURCE_LENGTH);
            System.err.println("Shader Source:");
            System.err.println("--------------");
            System.err.println(gl.GetShaderSource(stack, vsh, sourceLength));
            System.err.println("--------------");

            if (gl.GetShaderi(vsh, GL_COMPILE_STATUS) != GL_TRUE) {
                throw new IllegalStateException("Failed to compile shader.");
            }

            gl.DeleteShader(vsh);
        }

        gl.ClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        glfw.SwapInterval(1);
        glfw.ShowWindow(window);

        var SwapBuffers = GLFW_LOADER.find("glfwSwapBuffers").orElseThrow();
        while (!glfw.WindowShouldClose(window)) {
            glfw.PollEvents();

            /*int ARGB = Color.HSBtoRGB(System.currentTimeMillis() % 10000 / 10000.0f, 1.0f, 1.0f);
            gl.ClearColor(
                ((ARGB >> 16) & 0xFF) / 255.0f,
                ((ARGB >> 8) & 0xFF) / 255.0f,
                ((ARGB >> 0) & 0xFF) / 255.0f,
                0.0f
            );*/
            gl.Clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfw.SwapBuffers(SwapBuffers, window);
        }

        glfw.DestroyWindow(window);

        glfw.SetErrorCallback(NULL);

        callbackArena.close(); // frees all upcalls associated with the arena

        glfw.Terminate();
    }

    private static String render(Object value) {
        if (value instanceof MemorySegment segment) {
            return "0x" + Long.toHexString(segment.address()) + (segment.byteSize() == 0 ? "" : (" [" + (segment.byteSize() == Long.MAX_VALUE ? "?" : segment.byteSize()) + "]"));
        } else {
            return value.toString();
        }
    }

}