package com.imovie.mogic.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.mine.AutoScrollActivity;
import com.imovie.mogic.mine.SetingHallActivity;
import com.imovie.mogic.web.model.HttpResultModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class WelcomeActivity extends BaseActivity {

    public static final int MSG_TOLOGIN = 60;
    public static final int MSG_TOMAIN= 61;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_welcome_activity);

//        try {
//            getDeviceRegister();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        boolean isLogin = MyApplication.getInstance().mPref.getBoolean("isLogin",false);
//        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
            if (isLogin){
                uiHandler.sendEmptyMessageDelayed(MSG_TOMAIN,300);

            }else{
                uiHandler.sendEmptyMessageDelayed(MSG_TOLOGIN,300);
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private final UIHandler uiHandler = new UIHandler(this);
    private static class UIHandler extends Handler {
        private final WeakReference<WelcomeActivity> activity;
        public UIHandler(WelcomeActivity act) {
            super();
            activity = new WeakReference<WelcomeActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOMAIN:
//                    Intent intent = new Intent(activity.get(), AutoScrollActivity.class);
                    Intent intent = new Intent(activity.get(), MainActivity.class);
                    activity.get().startActivity(intent);
                    activity.get().finish();
                    break;
                case MSG_TOLOGIN:
//                    Intent intent1 = new Intent(activity.get(), SetingHallActivity.class);
                    Intent intent1 = new Intent(activity.get(), LoginActivity.class);
//                    intent1.putExtra("selectFrom",SetingHallActivity.SELECT_WEL);
                    activity.get().startActivity(intent1);
                    activity.get().finish();
                    break;
                default:
                    break;
            }
        }
    };

//    public void getDeviceRegister(){
//        HomeWebHelper.getDeviceRegister(new IModelResultListener<DeviceModel>() {
//            @Override
//            public boolean onGetResultModel(HttpResultModel resultModel) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(String resultCode, DeviceModel resultModel, List<DeviceModel> resultModelList, String resultMsg, String hint) {
//
////                String s = resultCode;
//                Log.e("----",resultCode+"::"+resultModel.deviceToken);
//                if(resultCode.equals("0")){
//                    SharedPreferences.Editor editor = MyApplication.getInstance().mPref.edit();
//                    editor.putString("deviceToken",resultModel.deviceToken);
//                    editor.commit();
//                }else{
//                    uiHandler.sendEmptyMessageDelayed(MSG_TOLOGIN,3000);
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
//    }

}

