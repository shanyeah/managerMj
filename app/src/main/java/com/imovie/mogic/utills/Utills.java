package com.imovie.mogic.utills;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.config.HTTPConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by $zhou on 2017/3/22 0022.
 */

public class Utills {
    public static void showShortToast(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int resid) {
        Toast.makeText(MyApplication.getInstance(), resid, Toast.LENGTH_SHORT).show();
    }

    public static void setUrlCookie(Context context,String url) {

        String phone = MyApplication.getInstance().mPref.getString("phone","");
        String password = MyApplication.getInstance().mPref.getString("password","");
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        int cardNo = MyApplication.getInstance().mPref.getInt("cardNo",0);

        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        boolean isLogin = MyApplication.getInstance().mPref.getBoolean("isLogin",false);
        if(isLogin){
            cookieManager.setCookie(url, "MJClient=android");
            cookieManager.setCookie(url, "phone="+phone);
            cookieManager.setCookie(url, "p="+MD5Helper.encode(password));
            cookieManager.setCookie(url, "cardId="+cardNo);
            cookieManager.setCookie(url, "memberId="+memberId);
        }else{
            cookieManager.setCookie(url, "MJClient=android");
        }
        cookieSyncManager.sync();
        String str = cookieManager.getCookie(url);
        Log.e("---cc",str);
    }


    public static String apkFileDownload(String url,String path, String apkFileName) {
        String str = "";
//        InputStream inputstream = null;
//        FileOutputStream fileoutputstream = null;
//        try {
//            DefaultHttpClient httpclient = new DefaultHttpClient();
//            HttpGet httpget = new HttpGet(url);
//            HttpEntity httpentity = httpclient.execute(httpget).getEntity();
//            inputstream = httpentity.getContent();
//            byte abyte[] = new byte[1024];
//            if (inputstream != null) {
//                long contentLength = httpentity.getContentLength();
//
//                int j = 0;
//
//                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                    return null;
//                }
//
//                File storageDirectory= Environment.getExternalStorageDirectory();
//
//                String fileName = storageDirectory + "/" + apkFileName;
//
//                File file = new File(fileName);
//
//                if(file.exists()) {
//                    file.delete();
//                }
//                fileoutputstream = new FileOutputStream(file);
//                while ((j = inputstream.read(abyte)) != -1) {
//                    fileoutputstream.write(abyte, 0, j);
//                }
//                fileoutputstream.flush();
//                str = fileName;
//            }
//        } catch (Exception e) {
//            str = null;
//            e.printStackTrace();
//        } finally {
//            try {
//                if (inputstream != null) {
//                    inputstream.close();
//                }
//                if (fileoutputstream != null) {
//                    fileoutputstream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return str;
    }

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
