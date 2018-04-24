package com.imovie.mogic.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.home.MainActivity;
import com.imovie.mogic.login.LoginActivity;
import com.imovie.mogic.webview.widget.YSBWebView;


/**
 * Created by $zhou on 2017/4/13 0013.
 */

public class JsInterface {

    private YSBWebView mWebView;
    public Context context;

    public JsInterface(Context context,YSBWebView webView) {
        this.mWebView = webView;
        this.context=context;
    }

    @JavascriptInterface
    public void J2C(final String type) {
        Log.e("----cc",type);
        if(type.equals("J2C_Login")){
            J2C_Login();
        }else if(type.equals("J2C_ShowNavBar")){
            J2C_ShowNavBar();
        }else if(type.equals("J2C_HideNavBar")){
            J2C_HideNavBar();
        }
    }

    public void J2C_Login() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //update UI in main looper, or it will crash
                try {
//                    Toast.makeText(mWebView.getContext(), "javaFunction had been called", Toast.LENGTH_SHORT).show();
                    boolean isLogin = MyApplication.getInstance().mPref.getBoolean("isLogin",false);
                    if(!isLogin){
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void J2C_ShowNavBar(){
        Intent intent = new Intent(MainActivity.SEND_BROADCAST);
        intent.putExtra(MainActivity.SEND_DATA,MainActivity.SHOW_NAV_BAR);
        context.sendBroadcast(intent);
    }

    public void J2C_HideNavBar(){
        Intent intent = new Intent(MainActivity.SEND_BROADCAST);
        intent.putExtra(MainActivity.SEND_DATA,MainActivity.HIDE_NAV_BAR);
        context.sendBroadcast(intent);
    }




    public void javaCallJsFunction(int code){
        mWebView.loadUrl(String.format("javascript:payError("+code+")"));
    }
}
