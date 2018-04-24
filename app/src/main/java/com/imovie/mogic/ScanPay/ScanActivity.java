package com.imovie.mogic.ScanPay;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.camera.CameraManager;
import com.imovie.mogic.ScanPay.decoding.CaptureActivityHandler;
import com.imovie.mogic.ScanPay.view.ViewfinderStyle1;
import com.imovie.mogic.ScanPay.view.ViewfinderView;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.widget.TitleBar;

import java.io.IOException;
import java.util.Vector;

//import ysbang.cn.config.application.YaoShiBangApplication;
//import ysbang.cn.yaocaigou.YaoCaiGouClassifyAndSearchActivity;
//import ysbang.cn.yaocaigou.search.model.YCGSearchParamModel;

/**
 * 扫描取搜索词页面。
 * 入参：(INTENT_KEY_param_model, YCGSearchParamModel)
 * Created by lyl on 2016/1/4.
 */
public class ScanActivity extends BaseScannerActivity implements SurfaceHolder.Callback {
    public final static String INTENT_KEY_param_model = "param_model";

//    // data
//    private YCGSearchParamModel searchParamModel;

    /**
     * dispathcer the scanresult string to the decode center.
     */
    private CaptureActivityHandler handler;

    /**
     * 相机图层通道：即显示相机的实时取景
     */
    private SurfaceView surfaceView;
    /**
     * 取景框，中心透明，带边框的视图，
     */
    private ViewfinderStyle1 viewfinderView;
    private SurfaceHolder surfaceHolder;
    private TextView flashlight_btn;

    private boolean hasSurface;

    /**
     * 支持的条码格式集合
     */
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;

    /**
     * 播放音乐（解码匹配成功后）
     */
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean playBeep;

    /**
     * 设置启动振子的时长（解码匹配成功后）
     */
    private static final long VIBRATE_DURATION = 200L;
    private boolean vibrate;

    private ImageView viewfinder_bg, viewfinder_laser;

    protected TextView scanLabel;

    /* 扫码激光宽度*/
    private int laserWidth;


    /**
     * 扫码结果串
     */
    private String resultString = "";


    /**
     * rl_navigationbarcontainer  自定义的actionbar 区域，只需要将自己的actionbar 添加到其中即可拥有头部
     */
    public TitleBar titleBar;

    /**
     * rl_promptpanel  扫码底部提示框跟布局，CaptureActivity的子类只需要将要显示的view 加入其中即可。
     */
    public RelativeLayout rl_promptpanel;


//    private YSBNavigationBar navigationBar;//导航条


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getIntentData();
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
        reInit the  cameraManager agin to avoid nullpoint excepion  for the none-application variable
		 */
        if (CameraManager.get() == null) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            CameraManager.init(getApplication(), dm);
        }

        surfaceHolder = surfaceView.getHolder();

        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
//        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        /** 启动上下扫描动画 */
        laserDownAnimation(laserWidth);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
    }

    private void getScanQRcode(String code) {
//        PayWebHelper.getScanQRcode(code, new IModelResultListener<ScanModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, ScanModel resultModel, List<ScanModel> resultModelList, String resultMsg, String hint) {
//                Utills.showShortToast(resultModel.retMessage);
//                if(resultModel.objectType==1){
////                    Intent intent = new Intent(ScanActivity.this, UserCenterActivity.class);
////                    intent.putExtra(UserCenterActivity.USER_ID,""+resultModel.objectId);
////                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFail(String resultCode, String resultMsg, String hint) {
//
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//
//            }
//        });
    }

    /***
     * Init View
     */
    void initView() {
        setContentView(R.layout.scan_scanner_activity);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;

		/* 初始化矩形框 */
        viewfinderView = (ViewfinderStyle1) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        flashlight_btn = (TextView) findViewById(R.id.flashlight_btn);
        hasSurface = false;

        viewfinder_bg = (ImageView) findViewById(R.id.viewfinder_bg);
        viewfinder_laser = (ImageView) findViewById(R.id.viewfinder_laser);

        scanLabel = (TextView) findViewById(R.id.scanLabel);

        titleBar = (TitleBar) findViewById(R.id.titleBar);//顶部actionbar
        rl_promptpanel = (RelativeLayout) findViewById(R.id.rl_promptpanel);//底部提示框

        /* viewfinder_bg Params Fix  （can be customed）*/
        RelativeLayout.LayoutParams viewfinder_bgParam = (RelativeLayout.LayoutParams) viewfinder_bg.getLayoutParams();

        viewfinder_bgParam.width = (int) (screenW * 0.8);
        viewfinder_bgParam.height = viewfinder_bgParam.width;
        viewfinder_bgParam.setMargins(0, screenW * 356 / 1129, 0, 0);
        viewfinder_bg.setLayoutParams(viewfinder_bgParam);

        RelativeLayout.LayoutParams viewfinder_laserParam = (RelativeLayout.LayoutParams) viewfinder_laser.getLayoutParams();
        viewfinder_laserParam.width = screenW * 709 / 1129;
        viewfinder_laser.setLayoutParams(viewfinder_laserParam);

        laserWidth = viewfinder_bgParam.height;//扫码激光动画高度

        setNavigationBar();
    }

    /**
     * 设置导航条
     */
    private void setNavigationBar() {
        titleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle("扫一扫");


        flashlight_btn.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;//默认没开启

            @Override
            public void onClick(View v) {
                if (!flag) {//打开状态
                    CameraManager.get().openFlashLight();
                    flag = true;
                    flashlight_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flashlight_on, 0, 0);
                    flashlight_btn.setText("点击关闭闪光灯");

                } else {//关闭状态
                    CameraManager.get().closeFlashLight();
                    flag = false;
                    flashlight_btn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.flashlight_off, 0, 0);
                    flashlight_btn.setText("点击开启闪光灯");
                }
            }
        });


    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(final Result rawResult, Bundle bundle) {
//        inactivityTimer.onActivity();
//        beepManager.playBeepSoundAndVibrate();

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

//        if (isUrl(text)) {
//            showUrlOption(text);
//        } else {
//            handleOtherText(text);
//        }
        Utills.showShortToast(text);
    }

    /**
     * command the camera to refocus and redecoding
     */
    protected void resetScanning() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                continuePreview();
            }
        }, 2000);
    }

    private void continuePreview() {
        //initCamera();
        if (handler != null)
            handler.restartPreviewAndDecode();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
//        }

    }

    /**
     * surface callbacks
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
//        surfaceView.getHolder().removeCallback(this);
        //when the surfaceView destroyed we release the camera resources
        CameraManager.get().closeDriver();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    public Handler getDispatchHandler() {
        return handler;
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {

    }

    @Override
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * init beepsound
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * start to play the beep sund
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * scanning down animation
     *
     * @param height the moving length
     */
    void laserDownAnimation(final int height) {
        AnimationSet AnimationSet = new AnimationSet(true);
        TranslateAnimation TAnimation = new TranslateAnimation(0, 0, 0, height);
        TAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                laserUpAnimation(height);
            }
        });
        AnimationSet.setDuration(2000);
        AnimationSet.setStartOffset(0);
        AnimationSet.setFillAfter(true);
        AnimationSet.addAnimation(TAnimation);
        viewfinder_laser.startAnimation(AnimationSet);
    }

    /**
     * scanning up animation
     *
     * @param height the moving length
     */
    void laserUpAnimation(final int height) {
        AnimationSet AnimationSet = new AnimationSet(true);
        TranslateAnimation TAnimation = new TranslateAnimation(0, 0, height, 0);
        TAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                laserDownAnimation(height);
            }
        });
        AnimationSet.setDuration(2000);
        AnimationSet.setStartOffset(0);
        AnimationSet.setFillAfter(true);
        AnimationSet.addAnimation(TAnimation);
        viewfinder_laser.startAnimation(AnimationSet);
    }


}
