package com.imovie.mogic.base.universal_loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.imovie.mogic.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhou on 2015/8/24.
 * Loading Dialog
 *
 */
public class YSBLoadingDialog extends Dialog {

    private static final int TIMEOUT_ACTION = 0x00;

    private Context mContext;
    private static YSBLoadingDialog mInstance;
    private static OnCancelListener cancelListener;
    private boolean isTimeoutStop = true;
    protected View dialogView;

    protected ImageView loadingCircle;

    private static Handler mHandler;
    protected static ExecutorService cachedThreadPool;

    public YSBLoadingDialog(Context context) {
        super(context);
        mContext = context;
        initView();
        mHandler = new MyHandler();
    }



    private void initView(){
        dialogView = View.inflate(mContext, R.layout.ysb_loading_dialog, null);
//        dialogView = LayoutInflater.from(mContext).inflate(R.layout.ysb_loading_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);

        loadingCircle = (ImageView) findViewById(R.id.ysb_loading_dialog_circle);
        RotateAnimation anim = new RotateAnimation(0f, 350f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        loadingCircle.startAnimation(anim);

    }

    /**
     *
     * @param context context 实例
     * @param timeout 超时时间配置（单位：毫秒）
     * @param listener 返回按钮监听/超时监听
     * @return dialog实例
     */

     public static YSBLoadingDialog showLoadingDialog(Context context, final long timeout, final OnCancelListener listener) {

        try {
            dismissDialog();
            mInstance = new YSBLoadingDialog(context);
            YSBLoadingDialog.cancelListener = listener;
            mInstance.show();
            mInstance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mInstance.setCancelable(false);
            cachedThreadPool = Executors.newCachedThreadPool();
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                            Thread.sleep(timeout);
                        if (mInstance.isTimeoutStop) {
                                try {
                                    sendMSG(TIMEOUT_ACTION, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    } catch (InterruptedException e) {
                        e.getStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstance;
    }

    /**
     * @deprecated 此方法无取消方法，需手工调用dismissDialog()方法 不建议使用
     * @param context context 实例
     * @return dialog实例
     */
    public static YSBLoadingDialog showLoadingDialog(Context context){

        try {
            dismissDialog();
            mInstance = new YSBLoadingDialog(context);
            mInstance.show();
            mInstance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mInstance.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstance;
    }

    /**
     * 此方法无超时机制，可使用onCancel关闭dialog
     * @param context context 实例
     * @param listener 回调监听
     * @return dialog实例
     */

    public static YSBLoadingDialog showLoadingDialog(Context context, OnCancelListener listener){

        try {
            dismissDialog();
            YSBLoadingDialog.cancelListener = listener;
            mInstance = new YSBLoadingDialog(context);
            mInstance.show();
            mInstance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mInstance.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstance;
    }


    public static void dismissDialog() {
        try {
            if (mInstance != null && mInstance.isShowing()) {
                mInstance.isTimeoutStop = false;
                if(cachedThreadPool != null){
                    cachedThreadPool.shutdownNow();
                }
                cachedThreadPool = null;
                YSBLoadingDialog.cancelListener = null;
                mInstance.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCancelListener {
        void onTimeout();
        void onCancel();
    }

    private static void sendMSG(int m,Object object) {
        Message msg = new Message();
        msg.what = m;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    private static class MyHandler extends Handler {
//        private final WeakReference<YSBLoadingDialog> mTarget;

//        private MyHandler(YSBLoadingDialog context) {
////            mTarget = new WeakReference<>(context);
//        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            YSBLoadingDialog target = mTarget.get();
            switch (msg.what){
                case TIMEOUT_ACTION:
                    if(YSBLoadingDialog.cancelListener != null){
                        try{
                            YSBLoadingDialog.cancelListener.onTimeout();
                        }catch (Exception e){e.printStackTrace();}
                    }
                    YSBLoadingDialog.dismissDialog();
                    break;
                default:
                    break;

            }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(cancelListener != null){
                try{
                    cancelListener.onCancel();
                }catch (Exception e){e.printStackTrace();}
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
