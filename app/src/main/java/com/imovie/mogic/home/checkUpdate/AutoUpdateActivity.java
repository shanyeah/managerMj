package com.imovie.mogic.home.checkUpdate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.card.CardActivity;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.dbbase.util.LogUtil;
import com.imovie.mogic.home.checkUpdate.model.UpdateModel;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.StringHelper;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.common.CommonUtil;
import com.imovie.mogic.web.http.HttpHelper;

import java.lang.ref.WeakReference;


public class AutoUpdateActivity extends FragmentActivity {
    public final static String INTENT_KEY_UPDATEMODEL = "update_model";
    public static final int DOWN_REFRESH = 60;
    public static final int DOWN_OVER = 61;

    private TextView tv_content;
    private TextView tv_refusedUpdate;
    private ImageView tv_update;
    private UpdateModel updateModel;
    private ProgressBar progressPencent;
    private TextView tvPencent;
    private RelativeLayout rlDownPencent;
    private RelativeLayout rlRefuse;
    private RelativeLayout rlUpdate;
    public float percentDown=0l;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        getIntentData();
        setContentView(R.layout.dialog_version_update);
        initView();
        fixViews();
        setListeners();
        setDatas();
        rlUpdate.requestFocus();
        rlUpdate.setFocusable(true);
    }
    private void getIntentData() {
        try {
            updateModel = (UpdateModel) getIntent().getSerializableExtra(INTENT_KEY_UPDATEMODEL);
            if (null == updateModel) {
                finish();
            }
        } catch (Exception ex) {
            LogUtil.LogErr(getClass(), ex);
            finish();
        }
    }

    private void initView() {
        tv_content = (TextView) findViewById(R.id.tv_versiondetails);
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());

        tv_update = (ImageView) findViewById(R.id.tv_update);

        tv_refusedUpdate = (TextView) findViewById(R.id.tv_refuse);
        tv_refusedUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_refusedUpdate.getPaint().setAntiAlias(true);
        progressPencent = (ProgressBar) findViewById(R.id.progressPencent);
        tvPencent = (TextView) findViewById(R.id.tvPencent);
        rlDownPencent = (RelativeLayout) findViewById(R.id.rlDownPencent);
        rlRefuse = (RelativeLayout) findViewById(R.id.rlRefuse);
        rlUpdate = (RelativeLayout) findViewById(R.id.rlUpdate);
    }

    private void fixViews() {
        if (updateModel.upgradeType == 2) {
            tv_refusedUpdate.setVisibility(View.GONE);
            tv_update.setImageResource(R.drawable.background_force_update);
        }else{
            tv_refusedUpdate.setVisibility(View.VISIBLE);
            tv_update.setImageResource(R.drawable.background_update_button);
        }

    }

    private void setListeners() {
        tv_refusedUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                AutoUpdateActivity.this.finish();
            }
        });

        rlRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                AutoUpdateActivity.this.finish();
            }
        });

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateModel != null) {
                    try {
//                        Message msg = new Message();
//                        msg.what = DOWN_REFRESH;
//                        msg.obj = "0";
//                        uiHandler.sendMessage(msg);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateModel.url));
//                        startActivity(intent);
//                        apkFileDownload(updateModel.url,"Mogic.apk");
                        apkFileDownload(updateModel.downloadUrl,"Mogic.apk");

                    }catch (Exception ex) {
                        LogUtil.LogErr(getClass(), ex);
//                        showToast("跳转网页异常");
                    }
                }
//                AutoUpdateActivity.this.finish();
            }
        });

        rlUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateModel != null) {
                    try {
//                        Message msg = new Message();
//                        msg.what = DOWN_REFRESH;
//                        msg.obj = "0";
//                        uiHandler.sendMessage(msg);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateModel.url));
//                        startActivity(intent);
//                        apkFileDownload(updateModel.url,"Mogic.apk");
                        apkFileDownload(updateModel.downloadUrl,"Mogic.apk");

                    }catch (Exception ex) {
                        LogUtil.LogErr(getClass(), ex);
//                        showToast("跳转网页异常");
                    }
                }
//                AutoUpdateActivity.this.finish();
            }
        });
    }

    private void setDatas() {
        if (updateModel != null) {
            if(updateModel.remark.equals("")){
                updateModel.remark="\n\n1.暂无有关更新的详细信息....请直接升级";
            }

            tv_content.setText(CommonUtil.toDBC(updateModel.remark));
        }
    }

    private void apkFileDownload(String url, String apkFileName) {

            HttpHelper helper = new HttpHelper();
            helper.simpleDownload(url, AppConfig.appPath, "Mogic.apk",true, new HttpHelper.onDownloadStatueListener() {
                @Override
                public void onDownloading(int total, int current) {

//                    Utills.showShortToast(""+total);
//                    Log.e("-----d",""+total);
//                    Log.e("-----c",""+current);
                    if(current>0){
                    Message msg = new Message();
                    msg.what = DOWN_REFRESH;
                    msg.arg1 = total;
                    msg.arg2 = current;
                    uiHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onDownloadFinished(String path) {
//                    Log.e("-----d",path);
//                    Utills.showShortToast(path);
                    Message msg = new Message();
                    msg.what = DOWN_OVER;
                    msg.obj = path;
                    uiHandler.sendMessage(msg);
                }

                @Override
                public void onDownloadError(String error) {
//                    Utills.showShortToast("err");

                }
            });
    }

    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<AutoUpdateActivity> activity;
        public UIHandler(AutoUpdateActivity act) {
            super();
            activity = new WeakReference<AutoUpdateActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_REFRESH:
                    activity.get().rlDownPencent.setVisibility(View.VISIBLE);
                    float f1 = msg.arg1;
                    float f2 = msg.arg2;
                    if(f2>activity.get().percentDown) {
                        activity.get().progressPencent.setMax(msg.arg1);
                        activity.get().progressPencent.setProgress(msg.arg2);
                        float percent = (f2 / f1) * 100;
                        activity.get().tvPencent.setText("" + DecimalUtil.FormatMoney(percent, "#0.0") + "%");
                        activity.get().percentDown = f2;
                    }
                    break;
                case DOWN_OVER:
                    activity.get().rlDownPencent.setVisibility(View.VISIBLE);
                    String path = (String) msg.obj;
                    if(!StringHelper.isEmpty(path)){
                        MyApplication.getInstance().installNewVersion(activity.get(),path);
                    }else{
                        Utills.showShortToast("升级不成功");
                        activity.get().finish();
                    }

                    break;
                default:
                    break;
            }
        }
    };




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

}