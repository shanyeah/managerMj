
package com.imovie.mogic.ScanPay.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public final class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();
    private static DisplayMetrics thisDm;

    private static int MIN_FRAME_WIDTH = 480;
    private static int MIN_FRAME_HEIGHT = 480;
    private static int MAX_FRAME_WIDTH = 480;
    private static int MAX_FRAME_HEIGHT = 480;

    private static CameraManager cameraManager;

    static final int SDK_INT; // Later we can use Build.VERSION.SDK_INT

    static {
        int sdkInt;
        try {
            sdkInt = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException nfe) {
            // Just to be safe
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private boolean previewing;
    private final boolean useOneShotPreviewCallback;
    /**
     * Preview frames are delivered here, which we pass on to the registered handler. Make sure to
     * clear the handler so it will only receive one message.
     */
    private final PreviewCallback previewCallback;
    /**
     * Autofocus callbacks arrive here, and are dispatched to the Handler which requested them.
     */
    private final AutoFocusCallback autoFocusCallback;

    /**
     * Initializes this static object with the Context of the calling Activity.
     *
     * @param context The Activity which wants to use the camera.
     */
    public static void init(Context context, final DisplayMetrics dm) {
        thisDm = dm;
        if (cameraManager == null) {
            cameraManager = new CameraManager(context);
        }
    }

    /**
     * Gets the CameraManager singleton instance.
     *
     * @return A reference to the CameraManager singleton.
     */
    public static CameraManager get() {
        return cameraManager;
    }

    private CameraManager(Context context) {

        this.context = context;
        this.configManager = new CameraConfigurationManager(context);

        // Camera.setOneShotPreviewCallback() has a race condition in Cupcake, so we use the older
        // Camera.setPreviewCallback() on 1.5 and earlier. For Donut and later, we need to use
        // the more efficient one shot callback, as the older one can swamp the system and cause it
        // to run out of memory. We can't use SDK_INT because it was introduced in the Donut SDK.
        //useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.CUPCAKE;
        useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3 = Cupcake

        previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
        autoFocusCallback = new AutoFocusCallback();
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder The surface object which the camera will draw preview frames into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);

            //FIXME
            //     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //�Ƿ�ʹ��ǰ��
//      if (prefs.getBoolean(PreferencesActivity.KEY_FRONT_LIGHT, false)) {
//        FlashlightManager.enableFlashlight();
//      }
            FlashlightManager.enableFlashlight();
        }
    }

    /**
     * Closes the camera driver if still in use.
     */
    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public void stopPreview() {
        if (camera != null && previewing) {
            if (!useOneShotPreviewCallback) {
                camera.setPreviewCallback(null);
            }
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data will arrive as byte[]
     * in the message.obj field, with width and height encoded as message.arg1 and message.arg2,
     * respectively.
     *
     * @param handler The handler to send the message to.
     * @param message The what field of the message to be sent.
     */
    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            if (useOneShotPreviewCallback) {
                camera.setOneShotPreviewCallback(previewCallback);
            } else {
                camera.setPreviewCallback(previewCallback);
            }
        }
    }

    /**
     * Asks the camera hardware to perform an autofocus.
     *
     * @param handler The Handler to notify when the autofocus completes.
     * @param message The message to deliver.
     */
    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            //Log.d(TAG, "Requesting auto-focus callback");
            camera.autoFocus(autoFocusCallback);
        }
    }

    /**
     * Calculates the framing rect which the UI should draw to show the user where to place the
     * barcode. This target helps with alignment as well as forces the user to hold the device
     * far enough away to ensure the image will be in focus.
     *
     * @return The rectangle to draw on screen in window coordinates.
     */
    public Rect getFramingRect() {

        int screenW = thisDm.widthPixels;

        MIN_FRAME_WIDTH = screenW * 620 / 680;
        MIN_FRAME_HEIGHT = screenW * 620 / 680;
        MAX_FRAME_WIDTH = screenW * 620 / 680;
        MAX_FRAME_HEIGHT = screenW * 620 / 680;

        Point pointTemp = configManager.getScreenResolution();

        //@patch
        if (camera != null) {
            if (pointTemp == null) {
                configManager.initFromCameraParameters(camera);
            }
        } else {
            return null;
        }

        Point screenResolution = configManager.getScreenResolution();

        framingRect = null;//frameingRect  reset
        if (framingRect == null) {
            if (camera == null) {
                return null;
            }
//            int width = screenResolution.x * 3 / 4;
//            if (width < MIN_FRAME_WIDTH) {
//                width = MIN_FRAME_WIDTH;
//            } else if (width > MAX_FRAME_WIDTH) {
//                width = MAX_FRAME_WIDTH;
//            }
//            int height = screenResolution.y * 3 / 4;
//            if (height < MIN_FRAME_HEIGHT) {
//                height = MIN_FRAME_HEIGHT;
//            } else if (height > MAX_FRAME_HEIGHT) {
//                height = MAX_FRAME_HEIGHT;
//            }

            int width = MAX_FRAME_WIDTH;
            int height = MAX_FRAME_WIDTH;
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = screenW * 68 / 1129;//(screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    /**
     * 设置宽高比
     *
     * @param withVsheight       高宽比  height/withd 目前是  9/16 = 0.5625
     * @param screenradioofwidth 宽度的屏占比,即取景框最大占有屏幕宽度的比例  <=1
     * @return rect 返回相机取景框的矩形Rect
     */
    public Rect getFramingRect(float withVsheight, float screenradioofwidth) {
        int screenW = thisDm.widthPixels;

        Point screenResolution = configManager.getScreenResolution();

        //@patch
        if (camera != null) {
            if (screenResolution == null) {
                configManager.initFromCameraParameters(camera);
            }
        } else {
            return null;
        }

        int width = (int) (screenResolution.x * screenradioofwidth);
        int height = (int) (width * withVsheight);

        int leftOffset = (screenResolution.x - width) / 2;//marginleft
        int topOffset = screenW * 356 / 1129;//margintop

        framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);

        Log.d(TAG, "Calculated framing rect: " + framingRect);

        return framingRect;
    }

    /**
     * Like {@link #getFramingRect} but coordinates are in terms of the preview frame,
     * not UI / screen.
     */
    public Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            //modify here
//      rect.left = rect.left * cameraResolution.x / screenResolution.x;
//      rect.right = rect.right * cameraResolution.x / screenResolution.x;
//      rect.top = rect.top * cameraResolution.y / screenResolution.y;
//      rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }


    /**
     * A factory method to build the appropriate LuminanceSource object based on the format
     * of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = configManager.getPreviewFormat();
        String previewFormatString = configManager.getPreviewFormatString();
        switch (previewFormat) {
            // This is the standard Android format which all devices are REQUIRED to support.
            // In theory, it's the only one we should ever care about.
            case PixelFormat.YCbCr_420_SP:
                // This format has never been seen in the wild, but is compatible as we only care
                // about the Y channel, so allow it.
            case PixelFormat.YCbCr_422_SP:
                return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                        rect.width(), rect.height());
            default:
                // The Samsung Moment incorrectly uses this variant instead of the 'sp' version.
                // Fortunately, it too has all the Y data up front, so we can read it.
                if ("yuv420p".equals(previewFormatString)) {
                    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                            rect.width(), rect.height());
                }
        }
        throw new IllegalArgumentException("Unsupported picture format: " +
                previewFormat + '/' + previewFormatString);
    }

    public Context getContext() {
        return context;
    }

    public void openFlashLight() {
        if (camera == null)
            return;
        Camera.Parameters parameter = camera.getParameters();

        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        camera.setParameters(parameter);
    }

    public void closeFlashLight() {
        if (camera == null)
            return;
        Camera.Parameters parameter = camera.getParameters();

        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        camera.setParameters(parameter);
    }

}
