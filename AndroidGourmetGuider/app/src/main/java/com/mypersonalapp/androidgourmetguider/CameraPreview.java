package com.mypersonalapp.androidgourmetguider;

/**
 * Created by jaspe_000 on 3/8/2015.
 */
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private float distance;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //get the pointer ID
        Camera.Parameters parameters = mCamera.getParameters();
        int action = event.getAction();


        if (event.getPointerCount() > 1) {
            //two finger case
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                distance = getFingerSpacing(event);
            }
            else if (action == MotionEvent.ACTION_MOVE && parameters.isZoomSupported()) {
                mCamera.cancelAutoFocus();
                handleZoom(event, parameters);
            }
        }
        else {
            //single finger case
            if (action == MotionEvent.ACTION_UP) {
                handleFocus(event, parameters);
            }
        }
        return true;
    }

    private void handleZoom(MotionEvent event, Camera.Parameters parameters) {
        int maxZoom = parameters.getMaxZoom();
        int tempZoom = parameters.getZoom();
        float tempDistance = getFingerSpacing(event);
        if (tempDistance > distance) {
            //zoom in
            if (tempZoom < maxZoom) {
                tempZoom++;
            }
        } else if (tempDistance < distance) {
            //zoom out
            if (tempZoom > 0)
                tempZoom--;
        }
        distance = tempDistance;
        parameters.setZoom(tempZoom);
        mCamera.setParameters(parameters);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters parameters) {
        int pointerID = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerID);
        float x_pointer = event.getX(pointerIndex);
        float y_pointer = event.getY(pointerIndex);

        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if ((supportedFocusModes != null) &&
                supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    //auto-focus on single touch
                }
            });
        }
    }

    public float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
            //set the pinch zoom
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();
    }
}