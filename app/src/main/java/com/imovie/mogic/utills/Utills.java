package com.imovie.mogic.utills;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.login.model.LoginModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";
    public static String ListToString(List<LoginModel.OrganList> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null) {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<LoginModel.OrganList>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return "L" + sb.toString();
    }

    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<LoginModel.OrganList>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }


    public static Map<String, Object> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);

        mapText = mapText;

        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2); // 转换为数组
        for (String str : text) {
            String[] keyText = str.split(SEP3); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * String转换List
     *
     * @param listText
     *            :需要转换的文本
     * @return List<?>
     */
    public static List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("")) {
            return null;
        }
        listText = listText.substring(1);

        listText = listText;

        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP1);
        for (String str : text) {
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);
                list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }
}
