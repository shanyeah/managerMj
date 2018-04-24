package com.imovie.mogic.webview.factory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Wayne on 2015/12/4.
 */
public class YSBWebViewChromeClient extends WebChromeClient {

    public final static int FILECHOOSER_RESULTCODE = 1;
    private Activity act;

    public YSBWebViewChromeClient(Activity act) {
        this.act = act;
    }

    public getTitleListener listener;

    public interface getTitleListener {
        /** 获取标题 */
        public void onGetTitle(String title);

        /** 获取网页加载的进度条 */
        public void onGetProgress(int newProgress);

        /** 获取上传的单个相片 */
        public void onGetUploadMessage(ValueCallback<Uri> mUploadMessage);

        /** 获取上传的多个相片 */
        public void onGetUploadMsgLollipop(ValueCallback<Uri[]> mUploadMsgLollipop);
    }

    public void setOnGetTitleListener(getTitleListener listener){
        this.listener = listener;
    }

    // 设置网页加载的进度条
    public void onProgressChanged(WebView view, int newProgress) {
            listener.onGetProgress(newProgress);
    }

    // 获取网页的标题
    public void onReceivedTitle(WebView view, String title) {
        if(title==null){
            title = "";
        }
        listener.onGetTitle(title);
    }

    // JavaScript弹出框
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    // JavaScript输入框
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    // JavaScript确认框
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }


    @TargetApi(11)
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        getOpenFile(uploadMsg);
    }

    @TargetApi(15)
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        getOpenFile(uploadMsg);
//        Toast.makeText(act,"api 15",Toast.LENGTH_SHORT).show();
    }

    @TargetApi(21)
    public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
        listener.onGetUploadMsgLollipop(uploadMsg);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        act.startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), YSBWebViewChromeClient.FILECHOOSER_RESULTCODE);
        return true;
    }

    public void getOpenFile(ValueCallback<Uri> uploadMsg){
        listener.onGetUploadMessage(uploadMsg);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        act.startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), YSBWebViewChromeClient.FILECHOOSER_RESULTCODE);
    }

}
