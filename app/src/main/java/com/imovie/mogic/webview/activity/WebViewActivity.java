package com.imovie.mogic.webview.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imovie.mogic.R;
import com.imovie.mogic.base.universal_loading.YSBLoadingDialog;
import com.imovie.mogic.dbbase.util.LogUtil;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.utills.YSBException;
import com.imovie.mogic.web.common.CommonUtil;
import com.imovie.mogic.webview.factory.WebViewHelper;
import com.imovie.mogic.webview.factory.YSBWebViewChromeClient;
import com.imovie.mogic.webview.factory.YSBWebViewClient;
import com.imovie.mogic.webview.widget.YSBWebView;
import com.imovie.mogic.widget.TitleBar;

import java.lang.ref.WeakReference;


/**
 * 显示网页页面。
 * 已统一处理添加usertoken参数到url
 * 入参1：(EXTRA_URL, <url>) // 页面url(不需要添加usertoken参数)
 * 入参2：(EXTRA_RIGHT_TEXT, <right_text>) // 是否显示关闭按钮,true显示
 * Created by yeah on 2015/11/30.
 */
public class WebViewActivity extends BaseActivity {
    public final static String EXTRA_URL = "url";
    public final static String EXTRA_RIGHT_TEXT = "right_text";

    public static final String SEND_BROADCAST = "send_broadcast";
    public static final String SEND_DATA = "data";
    public static final int SHOW_NAV_BAR = 81;
    public static final int HIDE_NAV_BAR= 82;
    public static final int PAY_SUCCESS = 83;

    public TitleBar navigationbar;
    YSBWebView id_webview;
    RelativeLayout rl_Layout;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMsgLollipop ;
    // data
    private String url = "";
    public Boolean right_text = false;

    protected PayBroadcast payReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentParam();
        initView();
//        showLoadingView("正在加载数据", 10000);
        setView();

        payReceiver = new PayBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SEND_BROADCAST);
        this.registerReceiver(payReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(payReceiver);
    }

    @Override
    public void onBackPressed() {
        if(id_webview.canGoBack()){
            id_webview.goBack();
        }else{
            super.onBackPressed();
        }
    }

    private void getIntentParam() {
        try {
            url = getIntent().getStringExtra(EXTRA_URL);
//            url = "http://web.ysbang.cn/index.php/Achievement/Index/index.html";
//            url = "http://192.168.0.233/test/webAppInterActive.html";
            right_text = Boolean.parseBoolean(getIntent().getStringExtra(EXTRA_RIGHT_TEXT));
            if(CommonUtil.isStringEmpty(url)) {
                LogUtil.LogMsg(this.getClass(), "url参数为空");
                throw new YSBException();
            }
            // 添加usertoken
            url = WebViewHelper.urlAddUserToken(url);
        }catch (Exception ex) {
            LogUtil.LogErr(this.getClass(), ex);
           // Toast.makeText(this, "参数出错", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        setContentView(R.layout.home_webview);
        //get view
        navigationbar = (TitleBar)findViewById(R.id.navigationbar);
        id_webview = (YSBWebView)findViewById(R.id.id_webview);
        rl_Layout = (RelativeLayout)findViewById(R.id.rl_web_view_layout);
    }

    private void setView() {
        setListener();

        showLoadingView("加载中...", 3000, new YSBLoadingDialog.OnCancelListener() {
            @Override
            public void onTimeout() {
                hideLoadingView();
            }

            @Override
            public void onCancel() {
                hideLoadingView();
            }
        });

        //设置打开的网页
        id_webview.setData(id_webview, url);

        YSBWebViewClient webViewClient = new YSBWebViewClient();
        webViewClient.setOnGetTitleListener(new YSBWebViewClient.getTitleListener() {
            @Override
            public void onGetTitle(String title) {
                navigationbar.setTitle(title);
            }
        });

        id_webview.setWebViewClient(webViewClient);

        YSBWebViewChromeClient webViewChromeClient = new YSBWebViewChromeClient(this);
        webViewChromeClient.setOnGetTitleListener(new YSBWebViewChromeClient.getTitleListener() {
            @Override
            public void onGetTitle(String title) {
                navigationbar.setTitle(title);
            }

            @Override
            public void onGetProgress(int newProgress) {
                if (100 == newProgress) {
//                    hideLoadingView();
                }
            }

            @Override
            public void onGetUploadMessage(ValueCallback<Uri> mUploadMessage) {
                WebViewActivity.this.mUploadMessage = mUploadMessage;
            }

            @Override
            public void onGetUploadMsgLollipop(ValueCallback<Uri[]> mUploadMsgLollipop) {
                WebViewActivity.this.mUploadMsgLollipop = mUploadMsgLollipop;
            }
        });
        id_webview.setWebChromeClient(webViewChromeClient);

        if (right_text){
            addNavigationRightText();
        }
    }

    // 添加 右"关闭"按钮
    public void addNavigationRightText() {
        TextView rightText = new TextView(WebViewActivity.this);
        rightText.setText("关闭");
        rightText.setTextSize(16);
        rightText.setGravity(Gravity.CENTER_VERTICAL);
        rightText.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
        rightText.setLayoutParams(layoutParams);
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });
        navigationbar.addViewToRightLayout(rightText);
    }

    private void setListener()
    {
        //navigationBar
        navigationbar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_webview.canGoBack()) {
                    id_webview.goBack();
                } else {
                    WebViewActivity.this.finish();
                }
            }
        });

    }

    /* 返回文件选择 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == YSBWebViewChromeClient.FILECHOOSER_RESULTCODE) {
            if(Build.VERSION.SDK_INT < 21){
                // android 5.0 以下适配
                if (null == mUploadMessage)
                    return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }else{
                // android 5.0 以上适配
                if (null == mUploadMsgLollipop)
                    return;
                Uri result = intent == null || resultCode != RESULT_OK ? Uri.parse("") : intent.getData();
                Uri resultList[] = new Uri[1];
                resultList[0] = result;
                mUploadMsgLollipop.onReceiveValue(resultList);
                mUploadMsgLollipop = null;
            }
        }
    }


    private final MainHandler uiHandler = new MainHandler(this);
    private static class MainHandler extends Handler {
        private final WeakReference<WebViewActivity> activity;
        public MainHandler(WebViewActivity act) {
            super();
            activity = new WeakReference<WebViewActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SHOW_NAV_BAR:
                    activity.get().navigationbar.setVisibility(View.VISIBLE);
                    break;
                case HIDE_NAV_BAR:
                    activity.get().navigationbar.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    };

    public class PayBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(SEND_BROADCAST)) {
                    int data = intent.getIntExtra(SEND_DATA,0);
                    switch (data) {
                        case SHOW_NAV_BAR:
                            Message msg1 = new Message();
                            msg1.what = SHOW_NAV_BAR;
                            uiHandler.sendMessage(msg1);
                            break;
                        case HIDE_NAV_BAR:
                            Message msg2 = new Message();
                            msg2.what = HIDE_NAV_BAR;
                            uiHandler.sendMessage(msg2);
                            break;
                    }
//                    Toast.makeText(MainActivity.this,PAY_SUCCESS,Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}