package com.imovie.mogic.ScanPay.zxing.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.imovie.mogic.R;


public class BrowserActivity extends AppCompatActivity {

    public final static String BUNDLE_KEY_URL = "BUNDLE_KEY_URL";
    private WebView mWebView;
    private ImageView mImgBack;
    private ImageView mImgForward;
    private ImageView mImgRefresh;
    private LinearLayout mLayoutBottom;
    private ProgressBar mProgress;
    private String mCurrentUrl;

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private CookieManager cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mWebView = (WebView) findViewById(R.id.webview);
        mCurrentUrl=getIntent().getStringExtra(BUNDLE_KEY_URL);
        mImgBack = (ImageView) findViewById(R.id.browser_back);
        mImgForward = (ImageView) findViewById(R.id.browser_forward);
        mImgRefresh = (ImageView) findViewById(R.id.browser_refresh);

        mLayoutBottom = (LinearLayout) findViewById(R.id.browser_bottom);
        initBarAnim();
        initWebView();
       //双击隐藏底部mLayoutBottom
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {// webview的双击事件
                if (mLayoutBottom.getVisibility()== View.GONE) {
                    mLayoutBottom.startAnimation(animBottomIn);
                } else {
                    mLayoutBottom.startAnimation(animBottomOut);
                }
                return super.onDoubleTap(e);
            }
        });


        mWebView.loadUrl(mCurrentUrl);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
            }
        });

        mImgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
            }
        });

        mImgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
                //mWebView.loadUrl(mWebView.getUrl());
            }
        });
    }


    /**
     * 初始化上下栏的动画并设置结束监听事件
     */
    private void initBarAnim() {
        animBottomIn = AnimationUtils.loadAnimation(this, R.anim.activity_browser_bottom_in);
        animBottomOut = AnimationUtils.loadAnimation(this,
                R.anim.activity_browser_bottom_out);
        animBottomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.VISIBLE);
            }
        });
        animBottomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化浏览器设置信息
     */
    private void initWebView() {
        cookie = CookieManager.getInstance();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean flag = super.shouldOverrideUrlLoading(view, url);
            mCurrentUrl = url;
            return flag;
        }

        //网页加载开始时调用，显示加载提示旋转进度条
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgress.setVisibility(View.VISIBLE);
        }

        //网页加载失败时调用，隐藏加载提示旋转进度条
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mProgress.setVisibility(View.GONE);
        }
        //网页加载完成时调用，隐藏加载提示旋转进度条
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mCurrentUrl = url;
            mProgress.setVisibility(View.GONE);
        }
    }
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            //onWebIcon(view, farmIcon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 当前WebView显示页面的标题
     *
     * @param view
     *            WebView
     * @param title
     *            web页面标题
     */
    protected void onWebTitle(WebView view, String title) {
        if ( mWebView != null && this.getSupportActionBar()!=null) { // 必须做判断，由于webview加载属于耗时操作，可能会本Activity已经关闭了才被调用
            this.getSupportActionBar().setTitle(mWebView.getTitle());
       }
    }
}
