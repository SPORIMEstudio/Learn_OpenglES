
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

import android.opengl.Matrix;

public class Camera2D {
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] combinedMatrix = new float[16];

    private float x, y; // Camera position
    private float zoom = 1.0f;

    public Camera2D(float screenWidth, float screenHeight) {
        // Set up the orthographic projection matrix
        Matrix.orthoM(projectionMatrix, 0,
                0, screenWidth,
                0, screenHeight,
                -1, 1);
        Matrix.setIdentityM(viewMatrix, 0);
        update();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        update();
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        update();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZoom() {
        return zoom;
    }

    public float[] getCombinedMatrix() {
        return combinedMatrix;
    }

    private void update() {
        // Create the view matrix
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, -x, -y, 0);
        Matrix.scaleM(viewMatrix, 0, zoom, zoom, 1);

        // Combine view and projection matrices
        Matrix.multiplyMM(combinedMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }
}
