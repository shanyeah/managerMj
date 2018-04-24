package com.imovie.mogic.home.checkUpdate.net;


import com.imovie.mogic.MyApplication;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.checkUpdate.model.UpdateModel;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;


public class CheckUpdateWebHelper extends HttpWebHelper {

    /** 检查更新
     *  appId= 10001,魔杰运营，10000 订餐
     * */

    public static void checkUpdate(IModelResultListener<UpdateModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        String sign = MD5Helper.encode(distDate + "|" + hid + "|iwatchhome");
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_update);
        data.append("?version=" + AppConfig.getVersionName());
        data.append("&stgId=" + organId);
        data.append("&appId=" + 10001);
        new HomeWebHelper().sendPostWithTranslate(UpdateModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }
}
