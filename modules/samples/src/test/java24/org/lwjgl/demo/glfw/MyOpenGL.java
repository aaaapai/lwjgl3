/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.demo.glfw;

import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;

@FFMPrefix("gl")
@FFMCharset(FFMCharset.Type.UTF8)
public interface MyOpenGL {
    @FFMSignature("void glClear(GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha)")
    void ClearColor(float red, float green, float blue, float alpha);

    @FFMSignature("void glClear(GLbitfield mask)")
    void Clear(int mask);

    @FFMName("glGetIntegerv") @FFMReturn(1)
    @FFMSignature("void glGetIntegerv(GLenum name, GLint * params)")
    int GetInteger(StackAllocator<?> stack, int name);

    @FFMSignature("GLubyte const * glGetIntegerv(GLenum name)")
    String GetString(int name);

    @FFMSignature("GLuint glCreateShader(GLenum type)")
    int CreateShader(int type);

    @FFMSignature("void glCompileShader(GLuint shader)")
    void CompileShader(int shader);

    @FFMSignature("void glShaderSource(GLuint shader, GLsizei count, GLchar const * const * string, GLint const * length)")
    void ShaderSource(int shader, int count, MemorySegment string, MemorySegment length);

    @FFMReturn(3) @FFMReturn.SizeOut(2)
    @FFMSignature("void glGetShaderInfoLog(GLuint shader, GLsizei bufSize, GLsizei * length, GLchar * infoLog)")
    String GetShaderInfoLog(StackAllocator<?> allocator, int shader, @FFMReturn.Size int bufSize);

    @FFMName("glGetShaderiv") @FFMReturn(2)
    @FFMSignature("void glGetShaderiv(GLuint shader, GLenum pname, GLint * params)")
    int GetShaderi(int shader, int pname);

    @FFMReturn(3) @FFMReturn.SizeOut(2)
    @FFMSignature("void glGetShaderSource(GLuint shader, GLsizei bufSize, GLsizei * length, GLchar * source)")
    String GetShaderSource(StackAllocator<?> allocator, int shader, @FFMReturn.Size int bufSize);

    @FFMSignature("void glDeleteShader(GLuint shader)")
    void DeleteShader(int shader);
}
