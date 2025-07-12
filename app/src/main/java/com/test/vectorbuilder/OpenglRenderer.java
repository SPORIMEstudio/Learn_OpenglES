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

package com.test.vectorbuilder;

//import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.test.vectorbuilder.engine.Triangle;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;


public class OpenglRenderer implements GLSurfaceView.Renderer {
    
   Triangle triangle=new Triangle();
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    //    System.out.println("On sirface crated");
        GLES20.glClearColor(1.0f,0f, 0f, 1.0f);
       Triangle triangle=new Triangle();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
      //  System.out.println("onDraw Frames");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        triangle.draw();
    
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
