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

package com.test.vectorbuilder.input;

import android.view.MotionEvent;
import com.test.vectorbuilder.engine.Camera2D;

public class TouchInputHandler {

    private float previousX, previousY;
    private float initialDistance = 0;
    private boolean isZooming = false; // Track if zooming is happening

    private final Camera2D camera;

    public TouchInputHandler(Camera2D camera) {
        this.camera = camera;
    }

    public boolean handleTouchEvent(MotionEvent event) {
       if (camera == null) return false; // Avoid crash if camera is not initialized

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // Save the touch position for single-finger drag
                previousX = event.getX();
                previousY = event.getY();
                isZooming = false; // Reset zooming flag
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1 && !isZooming) {
                    // Single finger drag (move camera)
                    float dx = event.getX() - previousX;
                    float dy = event.getY() - previousY;

                    camera.setPosition(
                            camera.getX() - dx / camera.getZoom(),
                            camera.getY() + dy / camera.getZoom()
                    );

                    // Update previous touch position
                    previousX = event.getX();
                    previousY = event.getY();
                } else if (event.getPointerCount() == 2) {
                    // Two-finger pinch (zoom)
                    float distance = getDistance(event);
                    if (initialDistance > 0) {
                        float scale = distance / initialDistance;
                        camera.setZoom(camera.getZoom() * scale);
                    }
                    initialDistance = distance;
                    isZooming = true; // Mark zooming as active
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                // Reset initial distance and touch tracking
                initialDistance = 0;
                isZooming = false;
                previousX = 0;
                previousY = 0;
                break;
        }
        return true;
    }

    private float getDistance(MotionEvent event) {
        float dx = event.getX(0) - event.getX(1);
        float dy = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
