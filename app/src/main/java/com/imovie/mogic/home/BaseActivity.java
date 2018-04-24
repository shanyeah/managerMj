package com.imovie.mogic.home;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    private ViewGroup contentView;
    protected HashMap<String, SoftReference<Bitmap>> caches = new HashMap<>();
    protected ActionBar ab;
    protected RelativeLayout view_container;
    public static ArrayList<BackPressHandler> mListeners = new ArrayList<>();
    private InputMethodManager imm;
    private View view;
    private boolean isAutoControl = true;
    private boolean isNeedSetRootViewProperty = false;

//    private YSBLoadingFailed loadingFailed;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isTranslucentStatus() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
            isNeedSetRootViewProperty = true;
        }

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//        AutoStopMonitor.getInstance().onActivityCreate(this);

        getPermissions();
    }


    private void setTranslucentStatus() {
        /**透明导航栏*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {/**5.0及以上*/
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (alterThemeAttr()) {
                window.setStatusBarColor(getResources().getColor(R.color.BG18));
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        }
    }

    /**
     * 设置主题属性
     *
     * @param rootView
     */
    public void setThemeAttr(ViewGroup rootView) {
        if (isNeedSetRootViewProperty && alterThemeAttr()) {/**如果设置了状态栏透明则使View从状态栏下部开始*/
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
            isNeedSetRootViewProperty = false;
        }

    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(R.layout.activity_base_layout);
        getLayoutInflater().inflate(layoutResId, (ViewGroup) findViewById(R.id.linearlayout_base));
        setHeight4_4();
        view = this.findViewById(R.id.base_view);
//        layout_tips_view = (RelativeLayout) this.findViewById(R.id.layout_tips_view);
//        tv_tips_view = (TextView) this.findViewById(R.id.tv_tips_view);
        contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base_layout, null);
        setThemeAttr(contentView);
    }

    public void chooseContentView() {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = getStatusHeight() / 2;
        view.setLayoutParams(layoutParams);
    }

    public void requestNoTitle() {
        view.setVisibility(View.GONE);
    }

    public void requestTitle() {
        view.setVisibility(View.VISIBLE);
    }

    public void setHeight4_4() {
        View view = this.findViewById(R.id.base_view);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        // params.height = dip2px(getApplicationContext(), getActionBarHeight() + getStatusHeight());
//        params.height = getActionBarHeight() + getStatusHeight();
    }

    public void setHeight() {
        View view = this.findViewById(R.id.base_view);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        //params.height = dip2px(getApplicationContext(), getActionBarHeight());
        params.height = getActionBarHeight() - getStatusHeight();

    }

    /**
     * 是否将状态栏透明
     */
    public boolean isTranslucentStatus() {
        return true;
    }

    public boolean alterThemeAttr() {

        return true;
    }

    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getApplicationContext().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusHeight() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) Math.ceil(25 * metrics.density);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected void hideKeyboard() {
        if (imm.isActive()) {
            //如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

    }

    public void hideSoftKeyboard(EditText view) {
        this.imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    protected void showTipsLayout(String content) {
//        if (layout_tips_view == null) return;
//        tv_tips_view.setText(content);
//        layout_tips_view.setVisibility(View.VISIBLE);
//    }
//
//    protected void hideTipsLayout() {
//        if (layout_tips_view == null) return;
//        layout_tips_view.setVisibility(View.GONE);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListeners.size() > 0)
            for (BackPressHandler handler : mListeners) {
                handler.activityOnResume();
            }
    }

    public ActionBar getAb() {
        return ab;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListeners.size() > 0)
            for (BackPressHandler handler : mListeners) {
                handler.activityOnPause();
            }
    }


    public interface BackPressHandler {
        void activityOnResume();

        void activityOnPause();
    }

//    public void initActionBar(String title) {
//        this.ab = this.getSupportActionBar();
//        this.ab.setSplitBackgroundDrawable(null);
//        this.ab.setDisplayHomeAsUpEnabled(true);
//        this.ab.setBackgroundDrawable(getResources().getDrawable(R.color.title_color));
//        this.ab.setDisplayUseLogoEnabled(false);
//        this.ab.setDisplayShowHomeEnabled(false);
//        this.ab.setTitle(title);
//    }

//    public void initToolBar(String title) {
//        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setBackgroundColor(this.getResources().getColor(R.color.title_color));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mToolbar.setPadding(0, this.getStatusBarHeight(), 0, 0);
//            mToolbar.setClipToPadding(true);
//        }
//        setSupportActionBar(mToolbar);
//        this.ab = this.getSupportActionBar();
//        this.ab.setSplitBackgroundDrawable(null);
//        this.ab.setDisplayHomeAsUpEnabled(true);
//        this.ab.setDisplayUseLogoEnabled(false);
//        this.ab.setDisplayShowHomeEnabled(false);
//        this.ab.setTitle(title);
//    }

//    public void initToolBar(String title, boolean trans) {
//        if (trans) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        initToolBar(title);
//    }

//    protected void overridePendingTransition() {
//        this.overridePendingTransition(R.anim.home_fade, R.anim.home_hold);
//    }
//
//    public void hideLoadingTips() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (viewLoading != null) {
//                    viewLoading.showState(LoadingView.FINISH, null);
//                    view_container.setVisibility(View.GONE);
//                }
//            }
//        });
//    }

//    public void setLoadingTips() {
//        if (this.view_container == null) {
//            synchronized (this) {
//                if (view_container == null) {
//                    view_container = ((RelativeLayout) findViewById(R.id.view_container));
//                    if (view_container != null) {
//                        this.viewLoading = new LoadingView(this);
//                        this.view_container.addView(this.viewLoading);
//                    }
//                }
//            }
//        }
//        if (this.view_container == null) return;
//        this.view_container.setVisibility(View.VISIBLE);
//        this.viewLoading.showState(LoadingView.LOADING, null);
//    }

//    public void setLoadingTips(String tx) {
//        if (this.view_container == null) {
//            synchronized (this) {
//                if (view_container == null) {
//                    view_container = ((RelativeLayout) findViewById(R.id.view_container));
//                    if (view_container != null) {
//                        this.viewLoading = new LoadingView(this);
//                        viewLoading.setText(tx);
//                        this.view_container.addView(this.viewLoading);
//                    }
//                }
//            }
//        }
//        if (this.view_container == null) return;
//        this.view_container.setVisibility(View.VISIBLE);
//        this.viewLoading.showState(LoadingView.LOADING, null);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

//    public static String createJsonString(Object value) {
//        return JSON.toJSONString(value);
//    }
//
//    public boolean isArmeabi() {
//        if (Build.CPU_ABI.equalsIgnoreCase("x86")) {
//            return false;
//        }
//        return true;
//    }
//
//    public int getStatusBarHeight() {
//        int result = 0;
//        try {
//            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//            if (resourceId > 0) {
//                result = getResources().getDimensionPixelSize(resourceId);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 设置App字体不随系统字体的改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AutoStopMonitor.getInstance().onActivityStop(this);
    }

//    public void onEventMainThread(EventExit event) {
////        AutoStopMonitor.Log("activity %s 收到退出通知，开始退出", this.getClass().getSimpleName());
////        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AutoStopMonitor.getInstance().onActivityDestroy(this);
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 13)
        {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission Denied
                    //                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    //若没有通过授权就直接退出程序，要不然后面需要权限的时候会崩
//                    GlobalConstantHolder.finishAll();
//                    finish();
                    break;
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getPermissions() {
        int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
        if (SDK_VERSION >= 23) {
            //检测权限授权（针对6.0以上先安装后检查权限的情况）
            List<String> permissionsList = new ArrayList<>();
            String[] permissions = null;
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(android.Manifest.permission.CAMERA);
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissionsList.size() != 0) {
                permissions = new String[permissionsList.size()];
                for (int i = 0; i < permissionsList.size(); i++) {
                    permissions[i] = permissionsList.get(i);
                }
                //此句调起权限授权框
                ActivityCompat.requestPermissions(this, permissions, 13);
            }
        }
    }

    /**
     * 显示加载弹窗，建议使用此方法
     * @param msg
     * @param delayTimeOut 超时时间
     * @param listener 返回按钮点击事件
     */
    public void showLoadingView(String msg,long delayTimeOut, final YSBLoadingDialog.OnCancelListener listener ){
        if(delayTimeOut == 0){
            delayTimeOut = 30000;
        }
        YSBLoadingDialog.showLoadingDialog(this, delayTimeOut, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                listener.onTimeout();
            }

            @Override
            public void onCancel() {
                listener.onCancel();
            }
        });
    }

    public void hideLoadingView() {
        YSBLoadingDialog.dismissDialog();
//        hideFailedLoading();
    }

}
