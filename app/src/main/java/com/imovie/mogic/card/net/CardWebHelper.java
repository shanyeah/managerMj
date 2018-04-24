package com.imovie.mogic.card.net;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.card.model.PresentModel;
import com.imovie.mogic.config.HTTPConfig;
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
    public static void getPresentList(String chargeFee,int userId,IModelResultListener<PresentModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("stgId", organId);
        baseReqParamNetMap.put("chargeFee", chargeFee);
        baseReqParamNetMap.put("userId", userId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_presentList));
        new CardWebHelper().sendPostWithTranslate(PresentModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_1, baseReqParamNetMap, listener);
    }

    /**
     * 下单
     * @param listener
     */
    public static void getPreqrCharge(String chargeFee,int userId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        int adminId = MyApplication.getInstance().mPref.getInt("adminId",0);
        baseReqParamNetMap.put("stgId", organId);
        baseReqParamNetMap.put("fee", chargeFee);
        baseReqParamNetMap.put("userId", userId);
        baseReqParamNetMap.put("adminId", adminId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_preqrcharge));
        new CardWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_0, baseReqParamNetMap, listener);
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
