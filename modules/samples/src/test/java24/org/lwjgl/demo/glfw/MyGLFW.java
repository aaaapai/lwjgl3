/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;

//@FFMCritical
@FFMPrefix("glfw")
@FFMCharset(FFMCharset.Type.UTF8)
public interface MyGLFW {

    @FFMDefinition("int glfwInit()")
    @FFMBooleanInt(binary = true)
    boolean Init();

    @FFMDefinition("GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback)")
    @FFMPointer long SetErrorCallback(@FFMNullable @FFMPointer long callback);

    @FFMDefinition("GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback)")
    @FFMPointer long SetErrorCallback(Arena arena, @FFMNullable MyGLFWTypes.errorfun callback);

    @FFMDefinition("GLFWmonitor** glfwGetMonitors(int * count)")
    MemorySegment GetMonitors(MemorySegment count); // TODO: auto-size result via stack-allocated count

    @FFMDefinition("GLFWmonitor * glfwGetPrimaryMonitor()")
    @FFMPointer long GetPrimaryMonitor();

    @FFMDefinition("void glfwGetMonitorPos(GLFWmonitor * monitor, int * xpos, int * ypos)")
    void GetMonitorPos(@FFMPointer long monitor, MemorySegment xpos, MemorySegment ypos);

    @FFMDefinition("void glfwGetMonitorPhysicalSize(GLFWmonitor* monitor, int* widthMM, int* heightMM)")
    void GetMonitorPhysicalSize(@FFMPointer long monitor, @FFMNullable MemorySegment widthMM, @FFMNullable MemorySegment heightMM);

    @FFMDefinition("void glfwGetMonitorContentScale(GLFWmonitor* monitor, float* xscale, float* yscale)")
    void GetMonitorContentScale(@FFMPointer long monitor, @FFMNullable MemorySegment xscale, @FFMNullable MemorySegment yscale);

    @FFMDefinition("char const * glfwGetMonitorName(GLFWmonitor * monitor)")
    String GetMonitorName(@FFMPointer long monitor);

    @FFMDefinition("GLFWmonitorfun glfwSetMonitorCallback(GLFWmonitorfun callback)")
    @FFMPointer long SetMonitorCallback(Arena arena, @FFMNullable MyGLFWTypes.monitorfun cbfun);

    @FFMDefinition("GLFWvidmode const * glfwGetVideoMode(GLFWmonitor * monitor)")
    MemorySegment GetVideoMode(@FFMPointer long monitor);

    @FFMDefinition("void glfwSetGamma(GLFWmonitor * monitor, float gamma)")
    void SetGamma(@FFMPointer long monitor, float gamma);

    @FFMDefinition("GLFWgammaramp const * glfwGetGammaRamp(GLFWmonitor * monitor)")
    MemorySegment GetGammaRamp(@FFMPointer long monitor);

    @FFMDefinition("void glfwSetGammaRamp(GLFWmonitor * monitor, GLFWgammaramp const * ramp)")
    void SetGammaRamp(@FFMPointer long monitor, MemorySegment ramp);

    @FFMDefinition("void glfwDefaultWindowHints()")
    void DefaultWindowHints();

    @FFMDefinition("void glfwWindowHint(int hint, int value)")
    void WindowHint(int hint, int value);

    @FFMDefinition("GLFWwindow * glfwCreateWindow(int width, int height, char const * title, GLFWmonitor * monitor, GLFWwindow * share)")
    @FFMPointer long CreateWindow(int width, int height, String title, @FFMNullable @FFMPointer long monitor, @FFMNullable @FFMPointer long share);

    @FFMDefinition("void glfwDestroyWindow(GLFWwindow * window)")
    void DestroyWindow(@FFMPointer long window);

    @FFMDefinition("void glfwGetWindowPos(GLFWwindow* window, int* xpos, int* ypos)")
    void GetWindowPos(@FFMPointer long window, MemorySegment xpos, MemorySegment ypos);

    @FFMDefinition("void glfwSetWindowPos(GLFWwindow * window, int xpos, int ypos)")
    void SetWindowPos(@FFMPointer long window, int xpos, int ypos);

    @FFMDefinition("void glfwGetWindowSize(GLFWwindow* window, int* width, int* height)")
    void GetWindowSize(@FFMPointer long window, MemorySegment width, MemorySegment height);

    @FFMDefinition("void glfwSetWindowSizeLimits(GLFWwindow * window, int minwidth, int minheight, int maxwidth, int maxheight)")
    void SetWindowSizeLimits(@FFMPointer long window, int minwidth, int minheight, int maxwidth, int maxheight);

    @FFMDefinition("void glfwGetWindowFrameSize(GLFWwindow* window, int* left, int* top, int* right, int* bottom)")
    void GetWindowFrameSize(@FFMPointer long window, MemorySegment left, MemorySegment top, MemorySegment right, MemorySegment bottom);

    @FFMDefinition("void glfwGetWindowContentScale(GLFWwindow* window, float* xscale, float* yscale)")
    void GetWindowContentScale(@FFMPointer long window, MemorySegment xscale, MemorySegment yscale);

    @FFMDefinition("void glfwGetFramebufferSize(GLFWwindow* window, int* width, int* height)")
    void GetFramebufferSize(@FFMPointer long window, MemorySegment width, MemorySegment height);

    @FFMDefinition("void glfwSwapInterval(int interval)")
    void SwapInterval(int interval);

    @FFMDefinition("void glfwShowWindow(GLFWwindow * window)")
    void ShowWindow(@FFMPointer long window);

    @FFMDefinition("int glfwWindowShouldClose(GLFWwindow * window)")
    @FFMBooleanInt(binary = true)
    boolean WindowShouldClose(@FFMPointer long window);

    @FFMDefinition("GLFWwindowposfun glfwSetWindowPosCallback(GLFWwindow * window, GLFWwindowposfun callback)")
    @FFMPointer long SetWindowPosCallback(@FFMPointer long window, @FFMNullable @FFMPointer long callback);

    @FFMDefinition("GLFWwindowposfun glfwSetWindowPosCallback(GLFWwindow * window, GLFWwindowposfun callback)")
    @FFMPointer long SetWindowPosCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowposfun callback);

    @FFMDefinition("GLFWwindowsizefun glfwSetWindowSizeCallback(GLFWwindow* window, GLFWwindowsizefun callback)")
    @FFMPointer long SetWindowSizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowsizefun callback);

    @FFMDefinition("GLFWwindowclosefun glfwSetWindowCloseCallback(GLFWwindow* window, GLFWwindowclosefun callback)")
    @FFMPointer long SetWindowCloseCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowclosefun callback);

    @FFMDefinition("GLFWwindowrefreshfun glfwSetWindowRefreshCallback(GLFWwindow* window, GLFWwindowrefreshfun callback)")
    @FFMPointer long SetWindowRefreshCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowrefreshfun callback);

    @FFMDefinition("GLFWwindowfocusfun glfwSetWindowFocusCallback(GLFWwindow* window, GLFWwindowfocusfun callback)")
    @FFMPointer long SetWindowFocusCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowfocusfun callback);

    @FFMDefinition("GLFWwindowiconifyfun glfwSetWindowIconifyCallback(GLFWwindow* window, GLFWwindowiconifyfun callback)")
    @FFMPointer long SetWindowIconifyCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowiconifyfun callback);

    @FFMDefinition("GLFWwindowmaximizefun glfwSetWindowMaximizeCallback(GLFWwindow* window, GLFWwindowmaximizefun callback)")
    @FFMPointer long SetWindowMaximizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowmaximizefun callback);

    @FFMDefinition("GLFWframebuffersizefun glfwSetFramebufferSizeCallback(GLFWwindow* window, GLFWframebuffersizefun callback)")
    @FFMPointer long SetFramebufferSizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.framebuffersizefun callback);

    @FFMDefinition("GLFWwindowcontentscalefun glfwSetWindowContentScaleCallback(GLFWwindow* window, GLFWwindowcontentscalefun callback)")
    @FFMPointer long SetWindowContentScaleCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowcontentscalefun callback);

    @FFMDefinition("GLFWkeyfun glfwSetKeyCallback(GLFWwindow* window, GLFWkeyfun callback)")
    @FFMPointer long SetKeyCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.keyfun callback);

    @FFMDefinition("GLFWcharfun glfwSetCharCallback(GLFWwindow* window, GLFWcharfun callback)")
    @FFMPointer long SetCharCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.charfun callback);

    @FFMDefinition("GLFWcharmodsfun glfwSetCharModsCallback(GLFWwindow* window, GLFWcharmodsfun callback)")
    @FFMPointer long SetCharModsCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.charmodsfun callback);

    @FFMDefinition("GLFWmousebuttonfun glfwSetMouseButtonCallback(GLFWwindow* window, GLFWmousebuttonfun callback)")
    @FFMPointer long SetMouseButtonCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.mousebuttonfun callback);

    @FFMDefinition("GLFWcursorposfun glfwSetCursorPosCallback(GLFWwindow* window, GLFWcursorposfun callback)")
    @FFMPointer long SetCursorPosCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.cursorposfun callback);

    @FFMDefinition("GLFWcursorenterfun glfwSetCursorEnterCallback(GLFWwindow* window, GLFWcursorenterfun callback)")
    @FFMPointer long SetCursorEnterCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.cursorenterfun callback);

    @FFMDefinition("GLFWscrollfun glfwSetScrollCallback(GLFWwindow* window, GLFWscrollfun callback)")
    @FFMPointer long SetScrollCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.scrollfun callback);

    @FFMDefinition("GLFWdropfun glfwSetDropCallback(GLFWwindow* window, GLFWdropfun callback)")
    @FFMPointer long SetDropCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.dropfun callback);

    @FFMDefinition("void glfwPollEvents()")
    void PollEvents();

    @FFMDefinition("void glfwWaitEvents()")
    void WaitEvents();

    @FFMDefinition("void glfwDestroyCursor(GLFWcursor* cursor)")
    void DestroyCursor(@FFMPointer long cursor);

    @FFMDefinition("void glfwSetCursor(GLFWwindow* window, GLFWcursor* cursor)")
    void SetCursor(@FFMPointer long window, @FFMPointer long cursor);

    @FFMDefinition("GLFWjoystickfun glfwSetJoystickCallback(GLFWjoystickfun callback)")
    @FFMPointer long SetJoystickCallback(Arena rena, @FFMNullable MyGLFWTypes.joystickfun cbfun);

    @FFMDefinition("void glfwMakeContextCurrent(GLFWwindow * window)")
    void MakeContextCurrent(@FFMPointer long window);

    @FFMDefinition("void glfwSwapBuffers(GLFWwindow * window)")
    void SwapBuffers(@FFMPointer long window);

    @FFMDefinition("void glfwSwapBuffers(GLFWwindow * window)")
    @FFMFunctionAddress
    void SwapBuffers(MemorySegment address, @FFMPointer long window); // tests @FFMFunctionAddress

    @FFMDefinition("void glfwTerminate(void)")
    void Terminate();

}