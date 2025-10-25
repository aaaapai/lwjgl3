/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;

public interface MyGLFWTypes {
    @FFMDefinition("void (* GLFWerrorfun)(int error_code, const char* description);")
    @FunctionalInterface
    interface errorfun {
        void invoke(int error_code, String description);
    }

    @FFMDefinition("void (* GLFWwindowposfun)(GLFWwindow * window, int xpos, int ypos)")
    @FunctionalInterface
    interface windowposfun {
        void invoke(@FFMPointer long window, int xpos, int ypos);
    }

    @FFMDefinition("void (* GLFWwindowsizefun)(GLFWwindow* window, int width, int height)")
    @FunctionalInterface
    interface windowsizefun {
        void invoke(@FFMPointer long window, int width, int height);
    }

    @FFMDefinition("void (* GLFWwindowclosefun)(GLFWwindow* window)")
    @FunctionalInterface
    interface windowclosefun {
        void invoke(@FFMPointer long window);
    }

    @FFMDefinition("void (* GLFWwindowrefreshfun)(GLFWwindow* window)")
    @FunctionalInterface
    interface windowrefreshfun {
        void invoke(@FFMPointer long window);
    }

    @FFMDefinition("void (* GLFWwindowfocusfun)(GLFWwindow* window, int focused)")
    @FunctionalInterface
    interface windowfocusfun {
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean focused);
    }

    @FFMDefinition("void (* GLFWwindowiconifyfun)(GLFWwindow* window, int iconified)")
    @FunctionalInterface
    interface windowiconifyfun {
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean iconified);
    }

    @FFMDefinition("void (* GLFWwindowmaximizefun)(GLFWwindow* window, int maximized)")
    @FunctionalInterface
    interface windowmaximizefun {
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean maximized);
    }

    @FFMDefinition("void (* GLFWframebuffersizefun)(GLFWwindow* window, int width, int height)")
    @FunctionalInterface
    interface framebuffersizefun {
        void invoke(@FFMPointer long window, int width, int height);
    }

    @FFMDefinition("void (* GLFWwindowcontentscalefun)(GLFWwindow* window, float xscale, float yscale)")
    @FunctionalInterface
    interface windowcontentscalefun {
        void invoke(@FFMPointer long window, float xscale, float yscale);
    }

    @FFMDefinition("void (* GLFWmousebuttonfun)(GLFWwindow* window, int button, int action, int mods)")
    @FunctionalInterface
    interface mousebuttonfun {
        void invoke(@FFMPointer long window, int button, int action, int mods);
    }

    @FFMDefinition("void (* GLFWcursorposfun)(GLFWwindow* window, double xpos, double ypos)")
    @FunctionalInterface
    interface cursorposfun {
        void invoke(@FFMPointer long window, double xpos, double ypos);
    }

    @FFMDefinition("void (* GLFWcursorenterfun)(GLFWwindow* window, int entered)")
    @FunctionalInterface
    interface cursorenterfun {
        void invoke(@FFMPointer long window, @FFMBooleanInt boolean entered);
    }

    @FFMDefinition("void (* GLFWscrollfun)(GLFWwindow* window, double xoffset, double yoffset)")
    @FunctionalInterface
    interface scrollfun {
        void invoke(@FFMPointer long window, double xoffset, double yoffset);
    }

    @FFMDefinition("void (* GLFWkeyfun)(GLFWwindow* window, int key, int scancode, int action, int mods)")
    @FunctionalInterface
    interface keyfun {
        void invoke(@FFMPointer long window, int key, int scancode, int action, int mods);
    }

    @FFMDefinition("void (* GLFWcharfun)(GLFWwindow* window, unsigned int codepoint)")
    @FunctionalInterface
    interface charfun {
        void invoke(@FFMPointer long window, int codepoint);
    }

    @FFMDefinition("void (* GLFWcharmodsfun)(GLFWwindow* window, unsigned int codepoint, int mods)")
    @FunctionalInterface
    interface charmodsfun {
        void invoke(@FFMPointer long window, int codepoint, int mods);
    }

    @FFMDefinition("void (* GLFWdropfun)(GLFWwindow* window, int path_count, const char* paths[])")
    @FunctionalInterface
    interface dropfun {
        void invoke(@FFMPointer long window, int path_count, MemorySegment paths);
    }

    @FFMDefinition("void (* GLFWmonitorfun)(GLFWmonitor* monitor, int event)")
    @FunctionalInterface
    public interface monitorfun {
        void invoke(@FFMPointer long monitor, int event);
    }

    @FFMDefinition("void (* GLFWjoystickfun)(int jid, int event)")
    @FunctionalInterface
    public interface joystickfun {
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

    @FFMDefinition("struct GLFWgammaramp")
    public record gammaramp(
        @FFMDefinition("unsigned short *") MemorySegment red,
        @FFMDefinition("unsigned short *") MemorySegment green,
        @FFMDefinition("unsigned short *") MemorySegment blue,
        @FFMDefinition("unsigned int") int size
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
