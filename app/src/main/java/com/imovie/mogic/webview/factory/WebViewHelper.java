package com.imovie.mogic.webview.factory;

import com.imovie.mogic.dbbase.util.LogUtil;
import com.imovie.mogic.web.common.CommonUtil;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;


/**
 * webview helper类
 * Created by yeah on 2015/11/30.
 */
public class WebViewHelper {

    /**
     * url 加上UserToken
     * @param url 跳转URL
     */
    public static String urlAddUserToken(String url){
        String _url = url;
//        if (AppConfig.getUserDefault(AppConfig.flag_userLoginState, boolean.class)) {
//            String usertoken = "token=" + AppConfig.getUserDefault(AppConfig.flag_userToken, String.class);
//            if(!url.contains("?")) {
//                _url = url + "?" + usertoken;
//            } else {
//                _url = url + "&" + usertoken;
//            }
//        }
        return _url;
    }

    /**
     * 拼接http参数
     * @param paramModel 参数model
     * @param excludeField 不拼接字段集
     * @return 例：token=123456&user=234455
     */
    public static String joinHttpParam(Object paramModel, Set<String> excludeField) {
        StringBuffer httpParam = new StringBuffer();
        if(null == excludeField) {
            excludeField = new HashSet<String>();
        }
        try {
            Field[] fields = paramModel.getClass().getFields();
            for(Field field : fields) {
                if (excludeField.contains(fields)) {
                    continue;
                }
                httpParam.append("&"+field.getName()).append("=").append(URLEncoder.encode(field.get(paramModel).toString(), "utf-8"));
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        String param = httpParam.toString();
        if(CommonUtil.isStringNotEmpty(param)) {
            param = param.substring(1);
        }
        LogUtil.LogMsg(WebViewHelper.class, "httpParam=" + param);

       return param;
    }

}
