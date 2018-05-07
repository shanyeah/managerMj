package com.imovie.mogic.card.net;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.card.model.PresentModel;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.model.ChargeListModel;
import com.imovie.mogic.home.model.ChargeSuccessModel;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class CardWebHelper extends HttpWebHelper{

    /**
     * 赠送
     * @param listener
     */
    public static void getPresentList(String chargeAmount,int userId,IModelResultListener<ChargeListModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("chargeAmount", chargeAmount);
        baseReqParamNetMap.put("userId", userId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_presentList));
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        data.append("&organId=" + organId);
        new CardWebHelper().sendPostWithTranslate(ChargeListModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_1, baseReqParamNetMap, listener);
    }

    /**
     * 充值
     * @param listener
     */
    public static void getPreqrCharge(String amount,int userId,int payType, long payCategoryId,int type,String remark,String tn,IModelResultListener<ChargeSuccessModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("amount", amount);
        baseReqParamNetMap.put("userId", userId);
        baseReqParamNetMap.put("type",type);
        baseReqParamNetMap.put("payType",payType);
//        baseReqParamNetMap.put("payCategoryId",payCategoryId);
        baseReqParamNetMap.put("remark",remark);
        baseReqParamNetMap.put("tn",tn);
        baseReqParamNetMap.put("outTradeNo",tn);

        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_preqrcharge));
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        data.append("&organId=" + organId);
        new CardWebHelper().sendPostWithTranslate(ChargeSuccessModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }




    /**
     * 卡明细
     * @param listener
     */
    public static void getCardDetail(IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        baseReqParamNetMap.put("memberId", memberId);
//        String sign = MD5Helper.encode(distDate + "|" + hid + "|iwatchhome");
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_cardDetail);
//        data.append("&sign=" + sign);
        new CardWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }





    /**
     * 充值成功
     * @param listener
     */
    public static void getChargeDetail(String orderNo,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        baseReqParamNetMap.put("memberId", memberId);
        baseReqParamNetMap.put("orderNo", orderNo);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_chargeDetail);
        new CardWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }

    /**
     * 支付码
     * @param listener
     */
    public static void getQRcode(IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        baseReqParamNetMap.put("memberId", memberId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_getQRcode);
        new CardWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }


}
