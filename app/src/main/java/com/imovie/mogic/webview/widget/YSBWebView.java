package com.imovie.mogic.webview.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.webview.JsInterface;

/**
 * Created by Wayne on 2015/12/4.
 */
public class YSBWebView extends WebView {
    private Context context;

    public YSBWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * WebView 的参数和扩展设置
     * @param webView YSBWebView本身
     * @param url 跳转的url
     */

    public void setData(YSBWebView webView,String url){
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true); // 设置支持javascript脚本
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setBuiltInZoomControls(true); // 设置显示缩放按钮
        ws.setSupportZoom(true); //支持缩放
        ws.setDomStorageEnabled(true); // 浏览器缓存设置
        webView.clearCache(true);  // 强制清楚webview缓存
        ws.setUseWideViewPort(true);//将图片调整到适合webview的大小
        ws.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
//        ws.setGeolocationEnabled(true);//设置滚动条隐藏
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH); //启用地理定位
        /**
         * 用WebView显示图片，可使用这个参数
         * 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式

        //添加Javascript调用java对象
        JsInterface JSInterface2 = new JsInterface(context,webView);

        webView.addJavascriptInterface(JSInterface2, "JSInterface");
        webView.loadUrl(url);
    }
}
