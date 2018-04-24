package com.imovie.mogic.webview.factory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.utills.Utills;

/**
 * Created by yeah on 2015/12/4.
 */
public class YSBWebViewClient extends WebViewClient {

    public getTitleListener listener;

    public interface getTitleListener {
        /** 获取标题 */
        public void onGetTitle(String title);
    }

    public void setOnGetTitleListener(getTitleListener listener){
        this.listener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Log.e("---cc","ttt1:--"+url);
        Utills.setUrlCookie(view.getContext(),url);
        if (url.startsWith("tel:")) {// 页面打电话功能
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            view.getContext().startActivity(intent);
        }else if(url.startsWith("http:") || url.startsWith("https:")) {
            view.loadUrl(url); // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
        }

        if (url != null && url.startsWith("websupport.ysbang://")) { // webview 对scheme 支持
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }


        return true;
    }

    @Override  // 获取标题
    public void onPageFinished(WebView view, String url) {
        String title = view.getTitle();
        if(title==null){
            title = "";
        }
        listener.onGetTitle(title);
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        Log.e("---cc","ttt2:--"+url);
        Utills.setUrlCookie(view.getContext(),url);
        super.onPageStarted(view, url, favicon);
    }

}
