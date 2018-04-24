package com.imovie.mogic.login.net;

import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.LoginModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.utills.MD5Helper;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class LoginWebHelper extends HttpWebHelper{

    /**
     * 验证码
     * @param mobile
     * @param listener
     */
    public static void getRegAuth(String mobile,int type, IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("mobile", mobile);
//        String sign = MD5Helper.encode(distDate + "|" + hid + "|iwatchhome");
        StringBuffer data = new StringBuffer();
        if(type==0) {
            data.append(HTTPConfig.url_sendRegAuth);
        }else if(type==1){
            data.append(HTTPConfig.url_sendResetAuth);
        }
//        data.append("&sign=" + sign);
        new LoginWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }

    /**
     * 注册
     * @param listener
     */
    public static void getRegister(String userName,String password,int idType,String mobile,String phoneVerifyCode,String idNumber, IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("userName", userName);
        baseReqParamNetMap.put("password", password);
        baseReqParamNetMap.put("idType", idType);
        baseReqParamNetMap.put("mobile", mobile);
        baseReqParamNetMap.put("phoneVerifyCode", phoneVerifyCode);
        baseReqParamNetMap.put("idNumber", idNumber);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_reg);
        new LoginWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }

    /**
     * 登录
     * @param listener
     */
    public static void getLogin(String password,String userName,IModelResultListener<LoginModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("password", password);
        baseReqParamNetMap.put("userName", userName);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_login);
        new LoginWebHelper().sendPostWithTranslate(LoginModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }

    /**
     * 改登录密码
     * @param listener
     */
    public static void setLoginPassword(String mobile,String password,String vaildCode,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("password", password);
        baseReqParamNetMap.put("mobile", mobile);
        baseReqParamNetMap.put("vaildCode", vaildCode);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_resetLoginPassword);
        new LoginWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }
}
