package com.imovie.mogic.webview;

import android.content.Context;
import android.content.Intent;

import com.imovie.mogic.webview.activity.WebViewActivity;


/**
 * Created by yeah on 2015/11/30.
 */
public class WebViewManager {

    /**
     * 跳转 WebViewActivity页
     * @param context
     * @param directurl 跳转的url
     * @param bl 显示右"关闭"按钮,true为显示
     */
    public static void enterWebView(Context context, String directurl , Boolean bl){

        if(directurl == null || directurl.equals("") || directurl.length() < 5){
            return;
        }

        String str = directurl.substring(0, 5);

        if (str.equals("http:") || str.equals("https")) {

            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(WebViewActivity.EXTRA_URL, directurl);
            intent.putExtra(WebViewActivity.EXTRA_RIGHT_TEXT, bl + "");
            context.startActivity(intent);
        }
    }

}
