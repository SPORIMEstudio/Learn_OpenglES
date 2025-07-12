
/*
MIT License

Copyright (c) 2025 SPORIMEstudio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.test.vectorbuilder.engine;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GridRenderer {
    private FloatBuffer vertexBuffer;
    private int program;

    private static final String VERTEX_SHADER_CODE =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private static final float[] COLOR = {0.6f, 0.2f, 0.8f, 1.0f}; // Light gray
    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;

    public GridRenderer() {
        // Generate grid lines
        float[] vertices = generateGridVertices(-10, 10, -10, 10, 1);
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Compile shaders and link program
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    private float[] generateGridVertices(float left, float right, float bottom, float top, float spacing) {
        int horizontalLines = (int) ((top - bottom) / spacing) + 1;
        int verticalLines = (int) ((right - left) / spacing) + 1;

        float[] vertices = new float[(horizontalLines + verticalLines) * 4];
        int index = 0;

        // Horizontal lines
        for (float y = bottom; y <= top; y += spacing) {
            vertices[index++] = left;  // Start x
            vertices[index++] = y;    // Start y
            vertices[index++] = right; // End x
            vertices[index++] = y;    // End y
        }

        // Vertical lines
        for (float x = left; x <= right; x += spacing) {
            vertices[index++] = x;    // Start x
            vertices[index++] = bottom; // Start y
            vertices[index++] = x;    // End x
            vertices[index++] = top;  // End y
        }

        return vertices;
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glUniform4fv(colorHandle, 1, COLOR, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexBuffer.limit() / 2);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            System.err.println("Shader Compilation Error: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }
}
