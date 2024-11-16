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

    @FFMBooleanInt(binary = true)
    @FFMSignature("int glfwInit()")
    boolean Init();

    @FFMSignature("GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback)")
    @FFMPointer long SetErrorCallback(@FFMNullable @FFMPointer long callback);

    @FFMSignature("GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback)")
    @FFMPointer long SetErrorCallback(Arena arena, @FFMNullable MyGLFWTypes.errorfun callback);

    @FFMSignature("GLFWmonitor** glfwGetMonitors(int * count)")
    MemorySegment GetMonitors(MemorySegment count); // TODO: auto-size result via stack-allocated count

    @FFMSignature("GLFWmonitor * glfwGetPrimaryMonitor()")
    @FFMPointer long GetPrimaryMonitor();

    @FFMSignature("void glfwGetMonitorPos(GLFWmonitor * monitor, int * xpos, int * ypos)")
    void GetMonitorPos(@FFMPointer long monitor, MemorySegment xpos, MemorySegment ypos);

    @FFMSignature("void glfwGetMonitorPhysicalSize(GLFWmonitor* monitor, int* widthMM, int* heightMM)")
    void GetMonitorPhysicalSize(@FFMPointer long monitor, @FFMNullable MemorySegment widthMM, @FFMNullable MemorySegment heightMM);

    @FFMSignature("void glfwGetMonitorContentScale(GLFWmonitor* monitor, float* xscale, float* yscale)")
    void GetMonitorContentScale(@FFMPointer long monitor, @FFMNullable MemorySegment xscale, @FFMNullable MemorySegment yscale);

    @FFMSignature("char const * glfwGetMonitorName(GLFWmonitor * monitor)")
    String GetMonitorName(@FFMPointer long monitor);

    @FFMSignature("GLFWmonitorfun glfwSetMonitorCallback(GLFWmonitorfun callback)")
    @FFMPointer long SetMonitorCallback(Arena arena, @FFMNullable MyGLFWTypes.monitorfun cbfun);

    @FFMSignature("GLFWvidmode const * glfwGetVideoMode(GLFWmonitor * monitor)")
    MemorySegment GetVideoMode(@FFMPointer long monitor);

    @FFMSignature("void glfwSetGamma(GLFWmonitor * monitor, float gamma)")
    void SetGamma(@FFMPointer long monitor, float gamma);

    @FFMSignature("GLFWgammaramp const * glfwGetGammaRamp(GLFWmonitor * monitor)")
    MemorySegment GetGammaRamp(@FFMPointer long monitor);

    @FFMSignature("void glfwSetGammaRamp(GLFWmonitor * monitor, GLFWgammaramp const * ramp)")
    void SetGammaRamp(@FFMPointer long monitor, MemorySegment ramp);

    @FFMSignature("void glfwDefaultWindowHints()")
    void DefaultWindowHints();

    @FFMSignature("void glfwWindowHint(int hint, int value)")
    void WindowHint(int hint, int value);

    @FFMSignature("GLFWwindow * glfwCreateWindow(int width, int height, char const * title, GLFWmonitor * monitor, GLFWwindow * share)")
    @FFMPointer long CreateWindow(int width, int height, String title, @FFMNullable @FFMPointer long monitor, @FFMNullable @FFMPointer long share);

    @FFMSignature("void glfwDestroyWindow(GLFWwindow * window)")
    void DestroyWindow(@FFMPointer long window);

    @FFMSignature("void glfwGetWindowPos(GLFWwindow* window, int* xpos, int* ypos)")
    void GetWindowPos(@FFMPointer long window, MemorySegment xpos, MemorySegment ypos);

    @FFMSignature("void glfwSetWindowPos(GLFWwindow * window, int xpos, int ypos)")
    void SetWindowPos(@FFMPointer long window, int xpos, int ypos);

    @FFMSignature("void glfwGetWindowSize(GLFWwindow* window, int* width, int* height)")
    void GetWindowSize(@FFMPointer long window, MemorySegment width, MemorySegment height);

    @FFMSignature("void glfwSetWindowSizeLimits(GLFWwindow * window, int minwidth, int minheight, int maxwidth, int maxheight)")
    void SetWindowSizeLimits(@FFMPointer long window, int minwidth, int minheight, int maxwidth, int maxheight);

    @FFMSignature("void glfwGetWindowFrameSize(GLFWwindow* window, int* left, int* top, int* right, int* bottom)")
    void GetWindowFrameSize(@FFMPointer long window, MemorySegment left, MemorySegment top, MemorySegment right, MemorySegment bottom);

    @FFMSignature("void glfwGetWindowContentScale(GLFWwindow* window, float* xscale, float* yscale)")
    void GetWindowContentScale(@FFMPointer long window, MemorySegment xscale, MemorySegment yscale);

    @FFMSignature("void glfwGetFramebufferSize(GLFWwindow* window, int* width, int* height)")
    void GetFramebufferSize(@FFMPointer long window, MemorySegment width, MemorySegment height);


    @FFMSignature("void glfwSwapInterval(int interval)")
    void SwapInterval(int interval);

    @FFMSignature("void glfwShowWindow(GLFWwindow * window)")
    void ShowWindow(@FFMPointer long window);

    @FFMBooleanInt(binary = true)
    @FFMSignature("int glfwWindowShouldClose(GLFWwindow * window)")
    boolean WindowShouldClose(@FFMPointer long window);

    @FFMSignature("GLFWwindowposfun glfwSetWindowPosCallback(GLFWwindow * window, GLFWwindowposfun callback)")
    @FFMPointer long SetWindowPosCallback(@FFMPointer long window, @FFMNullable @FFMPointer long callback);

    @FFMSignature("GLFWwindowposfun glfwSetWindowPosCallback(GLFWwindow * window, GLFWwindowposfun callback)")
    @FFMPointer long SetWindowPosCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowposfun callback);

    @FFMSignature("GLFWwindowsizefun glfwSetWindowSizeCallback(GLFWwindow* window, GLFWwindowsizefun callback)")
    @FFMPointer long SetWindowSizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowsizefun callback);

    @FFMSignature("GLFWwindowclosefun glfwSetWindowCloseCallback(GLFWwindow* window, GLFWwindowclosefun callback)")
    @FFMPointer long SetWindowCloseCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowclosefun callback);

    @FFMSignature("GLFWwindowrefreshfun glfwSetWindowRefreshCallback(GLFWwindow* window, GLFWwindowrefreshfun callback)")
    @FFMPointer long SetWindowRefreshCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowrefreshfun callback);

    @FFMSignature("GLFWwindowfocusfun glfwSetWindowFocusCallback(GLFWwindow* window, GLFWwindowfocusfun callback)")
    @FFMPointer long SetWindowFocusCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowfocusfun callback);

    @FFMSignature("GLFWwindowiconifyfun glfwSetWindowIconifyCallback(GLFWwindow* window, GLFWwindowiconifyfun callback)")
    @FFMPointer long SetWindowIconifyCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowiconifyfun callback);

    @FFMSignature("GLFWwindowmaximizefun glfwSetWindowMaximizeCallback(GLFWwindow* window, GLFWwindowmaximizefun callback)")
    @FFMPointer long SetWindowMaximizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowmaximizefun callback);

    @FFMSignature("GLFWframebuffersizefun glfwSetFramebufferSizeCallback(GLFWwindow* window, GLFWframebuffersizefun callback)")
    @FFMPointer long SetFramebufferSizeCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.framebuffersizefun callback);

    @FFMSignature("GLFWwindowcontentscalefun glfwSetWindowContentScaleCallback(GLFWwindow* window, GLFWwindowcontentscalefun callback)")
    @FFMPointer long SetWindowContentScaleCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.windowcontentscalefun callback);

    @FFMSignature("GLFWkeyfun glfwSetKeyCallback(GLFWwindow* window, GLFWkeyfun callback)")
    @FFMPointer long SetKeyCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.keyfun callback);

    @FFMSignature("GLFWcharfun glfwSetCharCallback(GLFWwindow* window, GLFWcharfun callback)")
    @FFMPointer long SetCharCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.charfun callback);

    @FFMSignature("GLFWcharmodsfun glfwSetCharModsCallback(GLFWwindow* window, GLFWcharmodsfun callback)")
    @FFMPointer long SetCharModsCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.charmodsfun callback);

    @FFMSignature("GLFWmousebuttonfun glfwSetMouseButtonCallback(GLFWwindow* window, GLFWmousebuttonfun callback)")
    @FFMPointer long SetMouseButtonCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.mousebuttonfun callback);

    @FFMSignature("GLFWcursorposfun glfwSetCursorPosCallback(GLFWwindow* window, GLFWcursorposfun callback)")
    @FFMPointer long SetCursorPosCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.cursorposfun callback);

    @FFMSignature("GLFWcursorenterfun glfwSetCursorEnterCallback(GLFWwindow* window, GLFWcursorenterfun callback)")
    @FFMPointer long SetCursorEnterCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.cursorenterfun callback);

    @FFMSignature("GLFWscrollfun glfwSetScrollCallback(GLFWwindow* window, GLFWscrollfun callback)")
    @FFMPointer long SetScrollCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.scrollfun callback);

    @FFMSignature("GLFWdropfun glfwSetDropCallback(GLFWwindow* window, GLFWdropfun callback)")
    @FFMPointer long SetDropCallback(Arena arena, @FFMPointer long window, @FFMNullable MyGLFWTypes.dropfun callback);

    @FFMSignature("void glfwPollEvents()")
    void PollEvents();

    @FFMSignature("void glfwWaitEvents()")
    void WaitEvents();

    @FFMSignature("void glfwDestroyCursor(GLFWcursor* cursor)")
    void DestroyCursor(@FFMPointer long cursor);

    @FFMSignature("void glfwSetCursor(GLFWwindow* window, GLFWcursor* cursor)")
    void SetCursor(@FFMPointer long window, @FFMPointer long cursor);

    @FFMSignature("GLFWjoystickfun glfwSetJoystickCallback(GLFWjoystickfun callback)")
    @FFMPointer long SetJoystickCallback(Arena rena, @FFMNullable MyGLFWTypes.joystickfun cbfun);

    @FFMSignature("void glfwMakeContextCurrent(GLFWwindow * window)")
    void MakeContextCurrent(@FFMPointer long window);

    @FFMSignature("void glfwSwapBuffers(GLFWwindow * window)")
    void SwapBuffers(@FFMPointer long window);

    @FFMSignature("void glfwSwapBuffers(GLFWwindow * window)")
    @FFMFunctionAddress
    void SwapBuffers(MemorySegment address, @FFMPointer long window); // tests @FFMFunctionAddress

    @FFMSignature("void glfwTerminate(void)")
    void Terminate();

}