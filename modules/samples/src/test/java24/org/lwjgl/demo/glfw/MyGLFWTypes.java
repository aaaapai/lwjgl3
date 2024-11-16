/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;

public interface MyGLFWTypes {
    @FunctionalInterface
    interface errorfun {
        @FFMSignature("void (* GLFWerrorfun)(int error_code, const char* description);")
        void invoke(int error_code, String description);
    }

    @FunctionalInterface
    interface windowposfun {
        @FFMSignature("void (* GLFWwindowposfun)(GLFWwindow * window, int xpos, int ypos)")
        void invoke(@FFMPointer long window, int xpos, int ypos);
    }

    @FunctionalInterface
    interface windowsizefun {
        @FFMSignature("void (* GLFWwindowsizefun)(GLFWwindow* window, int width, int height)")
        void invoke(@FFMPointer long window, int width, int height);
    }

    @FunctionalInterface
    interface windowclosefun {
        @FFMSignature("void (* GLFWwindowclosefun)(GLFWwindow* window)")
        void invoke(@FFMPointer long window);
    }

    @FunctionalInterface
    interface windowrefreshfun {
        @FFMSignature("void (* GLFWwindowrefreshfun)(GLFWwindow* window)")
        void invoke(@FFMPointer long window);
    }

    @FunctionalInterface
    interface windowfocusfun {
        @FFMSignature("void (* GLFWwindowfocusfun)(GLFWwindow* window, int focused)")
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean focused);
    }

    @FunctionalInterface
    interface windowiconifyfun {
        @FFMSignature("void (* GLFWwindowiconifyfun)(GLFWwindow* window, int iconified)")
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean iconified);
    }

    @FunctionalInterface
    interface windowmaximizefun {
        @FFMSignature("void (* GLFWwindowmaximizefun)(GLFWwindow* window, int maximized)")
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean maximized);
    }

    @FunctionalInterface
    interface framebuffersizefun {
        @FFMSignature("void (* GLFWframebuffersizefun)(GLFWwindow* window, int width, int height)")
        void invoke(@FFMPointer long window, int width, int height);
    }

    @FunctionalInterface
    interface windowcontentscalefun {
        @FFMSignature("void (* GLFWwindowcontentscalefun)(GLFWwindow* window, float xscale, float yscale)")
        void invoke(@FFMPointer long window, float xscale, float yscale);
    }

    @FunctionalInterface
    interface mousebuttonfun {
        @FFMSignature("void (* GLFWmousebuttonfun)(GLFWwindow* window, int button, int action, int mods)")
        void invoke(@FFMPointer long window, int button, int action, int mods);
    }

    @FunctionalInterface
    interface cursorposfun {
        @FFMSignature("void (* GLFWcursorposfun)(GLFWwindow* window, double xpos, double ypos)")
        void invoke(@FFMPointer long window, double xpos, double ypos);
    }

    @FunctionalInterface
    interface cursorenterfun {
        @FFMSignature("void (* GLFWcursorenterfun)(GLFWwindow* window, int entered)")
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean entered);
    }

    @FunctionalInterface
    interface scrollfun {
        @FFMSignature("void (* GLFWscrollfun)(GLFWwindow* window, double xoffset, double yoffset)")
        void invoke(@FFMPointer long window, double xoffset, double yoffset);
    }

    @FunctionalInterface
    interface keyfun {
        @FFMSignature("void (* GLFWkeyfun)(GLFWwindow* window, int key, int scancode, int action, int mods)")
        void invoke(@FFMPointer long window, int key, int scancode, int action, int mods);
    }

    @FunctionalInterface
    interface charfun {
        @FFMSignature("void (* GLFWcharfun)(GLFWwindow* window, unsigned int codepoint)")
        void invoke(@FFMPointer long window, int codepoint);
    }

    @FunctionalInterface
    interface charmodsfun {
        @FFMSignature("void (* GLFWcharmodsfun)(GLFWwindow* window, unsigned int codepoint, int mods)")
        void invoke(@FFMPointer long window, int codepoint, int mods);
    }

    @FunctionalInterface
    interface dropfun {
        @FFMSignature("void (* GLFWdropfun)(GLFWwindow* window, int path_count, const char* paths[])")
        void invoke(@FFMPointer long window, int path_count, MemorySegment paths);
    }

    @FunctionalInterface
    public interface monitorfun {
        @FFMSignature("void (* GLFWmonitorfun)(GLFWmonitor* monitor, int event)")
        void invoke(@FFMPointer long monitor, int event);
    }

    @FunctionalInterface
    public interface joystickfun {
        @FFMSignature("void (* GLFWjoystickfun)(int jid, int event)")
        void invoke(int jid, int event);
    }

    @FFMDefinition("""
        struct GLFWvidmode {
            int width;
            int height;
            int redBits;
            int greenBits;
            int blueBits;
            int refreshRate;
        }""")
    public record vidmode(
        int width,
        int height,
        int redBits,
        int greenBits,
        int blueBits,
        int refreshRate
    ) { }

    @FFMDefinition("""
        struct GLFWgammaramp {
            unsigned short * red;
            unsigned short * green;
            unsigned short * blue;
            unsigned int size;
        }""")
    public record gammaramp(
        MemorySegment red,
        MemorySegment green,
        MemorySegment blue,
        int size
    ) { }

    public record vector4f(float x, float y, float z, float w) { }

    StructMapper<vidmode> vidmode();
    StructMapper<gammaramp> gammaramp();
    StructMapper<vector4f> vector4f();

    UpcallMapper<errorfun> errorfun();

    UpcallMapper<windowposfun> windowposfun();
    UpcallMapper<windowsizefun> windowsizefun();
    UpcallMapper<windowclosefun> windowclosefun();
    UpcallMapper<windowrefreshfun> windowrefreshfun();
    UpcallMapper<windowfocusfun> windowfocusfun();
    UpcallMapper<windowiconifyfun> windowiconifyfun();
    UpcallMapper<windowmaximizefun> windowmaximizefun();
    UpcallMapper<framebuffersizefun> framebuffersizefun();
    UpcallMapper<windowcontentscalefun> windowcontentscalefun();

    UpcallMapper<mousebuttonfun> mousebuttonfun();
    UpcallMapper<cursorposfun> cursorposfun();
    UpcallMapper<cursorenterfun> cursorenterfun();
    UpcallMapper<scrollfun> scrollfun();

    UpcallMapper<keyfun> keyfun();
    UpcallMapper<charfun> charfun();
    UpcallMapper<charmodsfun> charmodsfun();

    UpcallMapper<dropfun> dropfun();

    UpcallMapper<monitorfun> monitorfun();

    UpcallMapper<joystickfun> joystickfun();
}
