package com.imovie.mogic.login.model;

import java.util.HashMap;

/**
 * 接口请求参数基础map.
 * 已加入 plateform，version, token
 * Created zhou on 2015/4/13.
 */
public class BaseReqParamNetMap extends HashMap<String, Object> {

    public BaseReqParamNetMap() {
        super();
        // 必须
//        put("plateform", "android");
//        put("version", AppConfig.getVersionName());
//        //
//        put("token", LoginHelp.getUserToken());
        // put("format", "json");
        // put("authcode", "123456");
    }


}