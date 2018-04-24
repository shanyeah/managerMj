package com.imovie.mogic.ScanPay.net;


import com.imovie.mogic.ScanPay.model.QrCodeModel;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class PayWebHelper extends HttpWebHelper {

    /** 扫码支付
     * @param listener
     */
    public static void getScanQRcode(String billId,String code,IModelResultListener<SearchUserModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("billId", billId);
        baseReqParamNetMap.put("code", code);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_adminQrCharge));
        new PayWebHelper().sendPostWithTranslate(SearchUserModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }

    /** 扫码支付
     * @param listener
     */
    public static void getScanReset(String billId,IModelResultListener<SearchUserModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("billId", billId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_qrChargeReset));
        new PayWebHelper().sendPostWithTranslate(SearchUserModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }







    /**
     * 消费确认支付
     * @param listener
     */
    public static void getPayOrder(int placeId,int payType,int balance,int payAmount,int realAmount,String remark,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("payType", payType);
        baseReqParamNetMap.put("balance", balance);
        baseReqParamNetMap.put("payAmount", payAmount);
        baseReqParamNetMap.put("realAmount", realAmount);
        baseReqParamNetMap.put("remark", remark);
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_checkPayOrder));
        data.append("&placeId=" + placeId);
        new PayWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }


    /**
     * 取二维码
     * @param listener
     */
    public static void getQRcode(int objectType,int objectId,IModelResultListener<QrCodeModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_getQRcode));
        data.append("&objectType=" + objectType);
        data.append("&objectId=" + objectId);

        new PayWebHelper().sendPostWithTranslate(QrCodeModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }

}
