/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.imovie.mogic.ScanPay.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.manager.ScanPayManager;
import com.imovie.mogic.ScanPay.net.PayWebHelper;
import com.imovie.mogic.ScanPay.zxing.camera.CameraManager;
import com.imovie.mogic.ScanPay.zxing.decode.DecodeThread;
import com.imovie.mogic.ScanPay.zxing.utils.BeepManager;
import com.imovie.mogic.ScanPay.zxing.utils.CaptureActivityHandler;
import com.imovie.mogic.ScanPay.zxing.utils.DialogHelp;
import com.imovie.mogic.ScanPay.zxing.utils.InactivityTimer;
import com.imovie.mogic.home.DialogActivity;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements
        SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final int MSG_REFRESH = 90;
    public static final int MSG_CHAEGE = 91;
    public static final int MSG_OTHER = 92;
    public static final int MSG_BUYGOODS = 93;
    public static final int MSG_HOMESCAN = 95;
    public static final String MSG_FROM = "fromType";

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private ImageView mFlash;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;
    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
    public String data = "";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        if(getIntent().getIntExtra(MSG_FROM,0) == MSG_CHAEGE){
            data = getIntent().getStringExtra("data");
        }

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        mFlash = (ImageView) findViewById(R.id.capture_flash);
        mFlash.setOnClickListener(new View.OnClickListener() {
            private boolean flag;
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.capture_flash) {
                    if (flag == true) {
                        flag = false;
                        // 开闪光灯
                        cameraManager.openLight();
                        mFlash.setBackgroundResource(R.drawable.activity_capture_flash_open);
                    } else {
                        flag = true;
                        // 关闪光灯
                        cameraManager.offLight();
                        mFlash.setBackgroundResource(R.drawable.activity_capture_flash_default);
                    }
                }
            }
        });

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getIntExtra(MSG_FROM,0) == MSG_OTHER) {
            Intent mIntent = new Intent();
            mIntent.putExtra("code", "");
            setResult(MSG_OTHER, mIntent);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(final Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        // 通过这种方式可以获取到扫描的图片
//	bundle.putInt("width", mCropRect.width());
//	bundle.putInt("height", mCropRect.height());
//	bundle.putString("result", rawResult.getText());
//
//	startActivity(new Intent(CaptureActivity.this, ResultActivity.class)
//		.putExtras(bundle));

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                handleText(rawResult.getText());
            }
        }, 800);
    }

    private void handleText(String text) {
//        text = "4tBc1sghCFUj84YR6tXw4b";
//        text = "http://mobile.mjdj.cn/admin/view.jsp";
        if (isUrl(text) && getIntent().getIntExtra(MSG_FROM,0) != MSG_OTHER) {
            if(getIntent().getIntExtra(MSG_FROM,0) == MSG_HOMESCAN) {
                int organId = MyApplication.getInstance().mPref.getInt("organId",0);
                if(text.contains("?")){
                    text = text +"&organId=" + organId ;
                }else{
                    text = text +"?organId=" + organId ;
                }
            }
            showUrlOption(text);
        } else {
//            handleOtherText(text);
            Message message = new Message();
            if(getIntent().getIntExtra(MSG_FROM,0) == MSG_CHAEGE){
                message.what = MSG_CHAEGE;
            }else if(getIntent().getIntExtra(MSG_FROM,0) == MSG_BUYGOODS){
                message.what = MSG_BUYGOODS;
            }else if(getIntent().getIntExtra(MSG_FROM,0) == MSG_OTHER) {
                message.what = MSG_OTHER;
            }else if(getIntent().getIntExtra(MSG_FROM,0) == MSG_HOMESCAN) {
                message.what = MSG_HOMESCAN;
            }

            message.obj = text;
            uiHandler.sendMessage(message);
//            getScanQRcode(text);
        }
    }
    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }
    private void showUrlOption(final String url) {
        if (url.contains("scan_login")) {
            //showConfirmLogin(url);
            return;
        }else{
            try {
                // 浏览器
                Intent intent = new Intent(CaptureActivity.this, BrowserActivity.class);
                intent.putExtra(BrowserActivity.BUNDLE_KEY_URL, url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
    }
    private void handleOtherText(final String text) {
        // 判断是否符合基本的json格式
            showCopyTextOption(text);
    }

    private void showCopyTextOption(final String text) {
        DialogHelp.getConfirmDialog(this, text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setPrimaryClip(ClipData.newPlainText("data", text));
                //AppContext.showToast("复制成功");
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void getScanQRcode(String code) {
        PayWebHelper.getScanQRcode(data,code, new IModelResultListener<SearchUserModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, SearchUserModel resultModel, List<SearchUserModel> resultModelList, String resultMsg, String hint) {
//                Log.e("------scan",resultCode);

                if(resultModel.status == 1) {
//                    Utills.showShortToast("充值成功");
//                    ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,resultModel);
                }else{
//                    ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
                }
                finish();
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {
//                Utills.showShortToast("支付失败");
//                ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
                finish();
            }

            @Override
            public void onError(String errorMsg) {
//                Utills.showShortToast("支付失败");
//                ScanPayManager.enterChargeSuccessActivity(CaptureActivity.this,data,(SearchUserModel) getIntent().getSerializableExtra("userModel"));
                finish();
            }
        });
    }

    public void payGoodsOrder(long saleBillId, long userId,String seatNo,int payType, long payCategoryId,double incomeAmount,String remark,String tn,String payPassword){
        HomeWebHelper.payGoodsOrder(saleBillId, userId,seatNo,payType, payCategoryId,incomeAmount,remark,tn,payPassword,new IModelResultListener<TestModel>() {
            @Override
            public boolean onGetResultModel(HttpResultModel resultModel) {
                return false;
            }

            @Override
            public void onSuccess(String resultCode, TestModel resultModel, List<TestModel> resultModelList, String resultMsg, String hint) {
                if(resultCode.equals("0") && resultMsg.contains("订单成功")) {
                    Utills.showShortToast("支付成功");
                }else{
                    Utills.showShortToast(""+resultMsg);
                }
                finish();
            }

            @Override
            public void onFail(String resultCode, String resultMsg, String hint) {

            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    public void enterDialogActivity(String text){
        if(getIntent().getIntExtra("data",0)==1){
            Intent intent = new Intent(CaptureActivity.this,DialogActivity.class);
            intent.putExtra("data",text);
            startActivity(intent);
        }
        finish();
    }



    private final UIHandler uiHandler = new UIHandler(CaptureActivity.this);
    private static class UIHandler extends Handler {
        private final WeakReference<CaptureActivity> activity;
        public UIHandler(CaptureActivity act) {
            super();
            activity = new WeakReference<CaptureActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BUYGOODS:
//                    activity.get().queryWechatOrder(activity.get().wechatIDModel.orderNo);
                    PayResultModel model = (PayResultModel) activity.get().getIntent().getSerializableExtra("userModel");
                    Toast.makeText(activity.get(), "扫描完成，结果处理中...", Toast.LENGTH_SHORT).show();
                    String code1 = (String) msg.obj;
//                    activity.get().payGoodsOrder(model.saleBillId, model.saleBillId,code1);
                    break;

                case MSG_CHAEGE:
                    Toast.makeText(activity.get(), "扫描完成，结果处理中...", Toast.LENGTH_SHORT).show();
                    String code = (String) msg.obj;
                    activity.get().getScanQRcode(code);
                    break;

                case MSG_OTHER:
                    Toast.makeText(activity.get(), "扫描完成，结果处理中...", Toast.LENGTH_SHORT).show();
                    String code2 = (String) msg.obj;
                    Intent mIntent = new Intent();
                    mIntent.putExtra("code", code2);
                    activity.get().setResult(MSG_OTHER, mIntent);
                    activity.get().finish();
                break;
                case MSG_HOMESCAN:
                    Toast.makeText(activity.get(), "扫描完成，结果处理中...", Toast.LENGTH_SHORT).show();
                    String data = (String) msg.obj;
                    activity.get().enterDialogActivity(data);
                    break;
                default:
                    break;
            }
        }
    };

}