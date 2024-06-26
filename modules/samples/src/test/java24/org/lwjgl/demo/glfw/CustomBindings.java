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
import static org.lwjgl.system.ffm.StackArena.*;

public class CustomBindings {

    //@FFMCritical
    @FFMPrefix("glfw")
    @FFMCharset(FFMCharset.Type.UTF8)
    public interface MyGLFWBindings {
        @FFMBooleanInt(binary = true)
        @FFMSignature("int glfwInit()")
        boolean Init();

        @FFMSignature("void glfwDefaultWindowHints()")
        void DefaultWindowHints();

        @FFMSignature("void glfwWindowHint(int hint, int value)")
        void WindowHint(int hint, int value);

        @FFMSignature("GLFWwindow * glfwCreateWindow(int width, int height, char const *title, GLFWmonitor *monitor, GLFWwindow *share)")
        MemorySegment CreateWindow(int width, int height, String title, @FFMNullable MemorySegment monitor, @FFMNullable MemorySegment share);

        @FFMSignature("void glfwDestroyWindow(GLFWwindow *window)")
        void DestroyWindow(MemorySegment window);

        @FFMSignature("void glfwSetWindowSizeLimits(GLFWwindow *window, int minwidth, int minheight, int maxwidth, int maxheight)")
        void SetWindowSizeLimits(MemorySegment window, int minwidth, int minheight, int maxwidth, int maxheight);

        @FFMSignature("GLFWmonitor** glfwGetMonitors(int *count)")
        MemorySegment GetMonitors(MemorySegment count);

        @FFMSignature("GLFWmonitor * glfwGetPrimaryMonitor()")
        MemorySegment GetPrimaryMonitor();

        @FFMSignature("void glfwGetMonitorPos(GLFWmonitor *monitor, int *xpos, int *ypos)")
        void GetMonitorPos(MemorySegment monitor, MemorySegment xpos, MemorySegment ypos);

        @FFMSignature("char const * glfwGetMonitorName(GLFWmonitor *monitor)")
        String GetMonitorName(MemorySegment monitor);

        @FFMSignature("GLFWvidmode const * glfwGetVideoMode(GLFWmonitor *monitor)")
        MemorySegment GetVideoMode(MemorySegment monitor);

        @FFMSignature("void glfwSetWindowPos(GLFWwindow *window, int xpos, int ypos)")
        void SetWindowPos(MemorySegment window, int xpos, int ypos);

        @FFMSignature("void glfwMakeContextCurrent(GLFWwindow *window)")
        void MakeContextCurrent(MemorySegment window);

        @FFMSignature("void glfwSwapInterval(int interval)")
        void SwapInterval(int interval);

        @FFMSignature("void glfwShowWindow(GLFWwindow *window)")
        void ShowWindow(MemorySegment window);

        @FFMBooleanInt(binary = true)
        @FFMSignature("int glfwWindowShouldClose(GLFWwindow *window)")
        boolean WindowShouldClose(MemorySegment window);

        @FFMSignature("void glfwPollEvents()")
        void PollEvents();

        @FFMSignature("void glfwSwapBuffers(GLFWwindow *window)")
        @FFMFunctionAddress
        void SwapBuffers(MemorySegment address, MemorySegment window); // tests @FFMFunctionAddress

        @FFMSignature("void glfwTerminate(void)")
        void Terminate();
    }

    @FFMPrefix("gl")
    @FFMCharset(FFMCharset.Type.UTF8)
    public interface MyOpenGLBindings {
        @FFMSignature("void glClear(GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha)")
        void ClearColor(float red, float green, float blue, float alpha);

        @FFMSignature("void glClear(GLbitfield mask)")
        void Clear(int mask);

        @FFMName("glGetIntegerv") @FFMReturn(1)
        @FFMSignature("void glGetIntegerv(GLenum name, GLint *params)")
        int GetInteger(StackAllocator<?> stack, int name);

        @FFMSignature("GLubyte const * glGetIntegerv(GLenum name)")
        String GetString(int name);

        @FFMSignature("GLuint glCreateShader(GLenum type)")
        int CreateShader(int type);

        @FFMSignature("void glCompileShader(GLuint shader)")
        void CompileShader(int shader);

        @FFMSignature("void glShaderSource(GLuint shader, GLsizei count, GLchar const * const *string, GLint const *length)")
        void ShaderSource(int shader, int count, MemorySegment string, MemorySegment length);

        @FFMReturn(3) @FFMReturn.SizeOut(2)
        @FFMSignature("void glGetShaderInfoLog(GLuint shader, GLsizei bufSize, GLsizei *length, GLchar *infoLog)")
        String GetShaderInfoLog(StackAllocator<?> allocator, int shader, @FFMReturn.Size int bufSize);

        @FFMName("glGetShaderiv") @FFMReturn(2)
        @FFMSignature("void glGetShaderiv(GLuint shader, GLenum pname, GLint *params)")
        int GetShaderi(int shader, int pname);

        @FFMReturn(3) @FFMReturn.SizeOut(2)
        @FFMSignature("void glGetShaderSource(GLuint shader, GLsizei bufSize, GLsizei *length, GLchar *source)")
        String GetShaderSource(StackAllocator<?> allocator, int shader, @FFMReturn.Size int bufSize);

        @FFMSignature("void glDeleteShader(GLuint shader)")
        void DeleteShader(int shader);
    }

    static {
        //Configuration.DISABLE_CHECKS.set(true);
        Configuration.DEBUG_GENERATOR.set(false);
    }

    private static final SymbolLookup GLFW_LOADER = SymbolLookup.libraryLookup(Objects.requireNonNull(org.lwjgl.glfw.GLFW.getLibrary().getPath()), Arena.global());

    private static final TraceConsumer TRACER = (method, returnValue, args) -> {
        var prefix = method.getDeclaringClass().getAnnotation(FFMPrefix.class);
        if (prefix != null) {
            System.err.print(prefix.value());
        }
        System.err.println(method.getName() + '(' + Stream.of(args)
            .skip(1)
            .map(CustomBindings::render)
            .collect(Collectors.joining(", ")) + ")" + (returnValue == null ? "" : " : " + render(returnValue)));
    };

    private static final MyGLFWBindings glfw = BindingGenerator.generate(
        MethodHandles.lookup(),
        MyGLFWBindings.class,
        BindingConfig.builder()
            .withLookup(GLFW_LOADER)
            //.withTracing(TRACER/*, method -> method.getParameterCount() != 0 && method.getParameters()[0].getType() == MemorySegment.class*/)
            .build()
    );

    public static void main(String[] ignored) {
        if (!glfw.Init()) {
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

        try (var stack = stackPush()) {
            var count_p     = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
            var monitors_pp = glfw.GetMonitors(count_p);
            if (!MemorySegment.NULL.equals(monitors_pp)) {
                var count    = count_p.getAtIndex(ValueLayout.JAVA_INT, 0);
                var monitors = monitors_pp.reinterpret(ValueLayout.ADDRESS.scale(0, count));

                var xpos = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
                var ypos = stack.allocateFrom(ValueLayout.JAVA_INT, 1);
                for (int i = 0; i < count; i++) {
                    var monitor = monitors.getAtIndex(ValueLayout.ADDRESS, i);
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

        var window = glfw.CreateWindow(WIDTH, HEIGHT, "GLFW Demo", MemorySegment.NULL, MemorySegment.NULL);
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

        var OPENGL_PROVIDER = Objects.requireNonNull(GL.getFunctionProvider());

        // This is a like a lazy version of GL.createCapabilities(). Function pointers for the currently bound context will be lazily loaded on first use. No
        // GLCapabilities required! The owner of the generated bindings is responsible for using it only with the current context/thread.
        var gl = BindingGenerator.generate(
            MethodHandles.lookup(),
            MyOpenGLBindings.class,
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