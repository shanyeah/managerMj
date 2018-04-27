package com.imovie.mogic.home.net;

import android.graphics.Bitmap;

import com.baidu.mapapi.model.LatLng;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.car.bean.FoodBean;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.card.net.CardWebHelper;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.gameHall.model.ReviewModel;
import com.imovie.mogic.home.model.AuthCodeModel;
import com.imovie.mogic.home.model.CardModel;
import com.imovie.mogic.home.model.ClassifyModel;
import com.imovie.mogic.home.model.DBModel_SlideBanner;
import com.imovie.mogic.home.model.GoodsModel;
import com.imovie.mogic.home.model.HallModel;
import com.imovie.mogic.home.model.OrderModel;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchModel;
import com.imovie.mogic.home.model.SearchUserModel;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.MyDataModel;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.myRank.model.ExercisesMode;
import com.imovie.mogic.utills.DecimalUtil;
import com.imovie.mogic.utills.ImageUtil;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class HomeWebHelper extends HttpWebHelper{
    /**
     * 查询用户
     * @param listener
     */
    public static void getCheckUserInfo(String input,IModelResultListener<SearchModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("certFlag", 0);
        baseReqParamNetMap.put("input", input);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_checkUserInfo));
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        data.append("&organId=" + organId);
        new HomeWebHelper().sendPostWithTranslate(SearchModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 商品列表
     * @param listener
     */
    public static void getAllGoodList(IModelResultListener<CardModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
//        baseReqParamNetMap.put("stgId", organId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_allGoodList);
        data.append("?organId=" + organId);
        new HomeWebHelper().sendPostWithTranslate(CardModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }


    /**
     * 保存定单
     * @param listener
     */
    public static void saveGoodsOrder(int userId, String qrCode, String remark, String seatNo, List<FoodBean> goodsList, IModelResultListener<PayResultModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("userId", userId);
        baseReqParamNetMap.put("remark", remark);
        baseReqParamNetMap.put("seatNo", seatNo);
        baseReqParamNetMap.put("qrCode", qrCode);
        baseReqParamNetMap.put("goodsList", changeGoodsModel(goodsList));
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_saveGoodsOrder));
        data.append("&organId=" + organId);
        new HomeWebHelper().sendPostWithTranslate(PayResultModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    public static List<OrderModel> changeGoodsModel(List<FoodBean> goodsList){
        List<OrderModel> list = new ArrayList<>();
        for(int i=0;i<goodsList.size();i++){
            OrderModel orderModel = new OrderModel();
            orderModel.goodsId = goodsList.get(i).getGoodsId();
            orderModel.price = goodsList.get(i).getPrice().toString();
            orderModel.quantity = goodsList.get(i).getSelectCount();
            orderModel.goodsPackList = goodsList.get(i).goodsPackList;
            list.add(orderModel);
        }
        return list;
    }









    /**
     * 我的信息
     * @param listener
     */
    public static void getMy(IModelResultListener<MyDataModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_my));
        new HomeWebHelper().sendPostWithTranslate(MyDataModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 修改我的信息
     * @param listener
     */
    public static void updateMyInfo(String nickName,String shorDesc,String fileStr,IModelResultListener<MyDataModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        baseReqParamNetMap.put("faceImage", new File(fileStr));
        baseReqParamNetMap.put("nickName", nickName);
        baseReqParamNetMap.put("shortDesc", shorDesc);
        baseReqParamNetMap.put("faceImage", fileStr);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_uploadImage));
        new HomeWebHelper().sendPostWithTranslate(MyDataModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }



    /**
     * 点赞URL
     * @param listener
     */
    public static void getLikeUrl(IModelResultListener<MyDataModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_likeUrl));
        new HomeWebHelper().sendPostWithTranslate(MyDataModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 点赞二维码
     * @param listener
     */
    public static void getMyQrCode(IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_myQrCode));
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_0,baseReqParamNetMap, listener);
    }




    /**
     * 支付定单
     * @param listener
     */
    public static void payGoodsOrder(int goodsOrderId, int clerkOrderId,String code, IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("goodsOrderId", goodsOrderId);
        baseReqParamNetMap.put("clerkOrderId", clerkOrderId);
        baseReqParamNetMap.put("code", code);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_payGoodsOrder));
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 回复评论
     * @param listener
     */
    public static void getFriendComments(String content,int reviewId,IModelResultListener<ReviewModel.Replies> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("content", content);
        baseReqParamNetMap.put("reviewId", reviewId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_reply));
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        data.append("&stgId=" + organId);
        new HomeWebHelper().sendPostWithTranslate(ReviewModel.Replies.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 扫码授权
     * @param listener
     */
    public static void getScanAuthCode(String code,IModelResultListener<AuthCodeModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("code", code);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_scanAuthCode));
        new HomeWebHelper().sendPostWithTranslate(AuthCodeModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 授权确认
     * @param listener
     */
    public static void getConfirmAuth(int authId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("authId", authId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_confirmAuth));
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 授权列表
     * @param listener
     */
    public static void getAuthCodeList(IModelResultListener<ClassifyModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_authCodeList));
        new HomeWebHelper().sendPostWithTranslate(ClassifyModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }

    /**
     * 馆列表
     * @param listener
     */
    public static void getHallList(int orderType, String cityId,int pageNo, int pageSize,IModelResultListener<HallModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        baseReqParamNetMap.put("orderType", orderType);
//        baseReqParamNetMap.put("cityId", cityId);
//        baseReqParamNetMap.put("pageNo", pageNo);
//        baseReqParamNetMap.put("pageSize", pageSize);
//        LatLng latLng = MyApplication.getInstance().getCoordinate();
////        Log.e("----lat",""+latLng.latitude+"---"+latLng.longitude);
//        baseReqParamNetMap.put("latitude", latLng.latitude);
//        baseReqParamNetMap.put("longitude", latLng.longitude);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_hallList));
        new HomeWebHelper().sendPostWithTranslate(HallModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }













    /**
     * 地区列表
     * @param listener
     */
    public static void getCityList(IModelResultListener<InternetBarModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_cityList);
        new HomeWebHelper().sendPostWithTranslate(InternetBarModel.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }





    /**
     * 下单
     * @param listener
     */
    public static void getWechatId(String body,String detail,String totalFee,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        String ip= DecimalUtil.getHostIP();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        baseReqParamNetMap.put("ip",ip);
        baseReqParamNetMap.put("body",body);
        baseReqParamNetMap.put("detail",detail);
        baseReqParamNetMap.put("totalFee",totalFee);
        baseReqParamNetMap.put("memberId",memberId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_payWechatId);
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, 0,baseReqParamNetMap, listener);
    }

    /**
     * 订单查询
     * @param listener
     */
    public static void queryWechatOrder(String orderNo,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("orderNo",orderNo);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_queryWechatOrder);
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, 0,baseReqParamNetMap, listener);
    }

    /**
     * 上传图片
     * @param listener
     */
    public static void uploadImage(IModelResultListener<TestModel> listener){
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
//        String fileStr = ImageUtil.compressAndBase64Bitmap(ImageUtil.getBitmap(AppConfig.appPath + memberId + "_headerImage.jpg"));
        String fileStr = AppConfig.appPath + memberId + "_headerImage.jpg";
        Bitmap bitmap = null;
        try {
            bitmap = ImageUtil.getBitmapFromLocal(fileStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(bitmap!=null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            baseReqParamNetMap.put("file",new ByteArrayInputStream(outputStream.toByteArray()));
        }
        baseReqParamNetMap.put("memberId",memberId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_uploadImage);
        new HomeWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, 0,baseReqParamNetMap, listener);
    }

    public static String FileInputStreamDemo(String path){
        File file=new File(path);
        StringBuffer sb= null;
        try {
            if(!file.exists()||file.isDirectory())
                throw new FileNotFoundException();
            FileInputStream fis=new FileInputStream(file);
            byte[] buf = new byte[1024];
            sb = new StringBuffer();
            while((fis.read(buf))!=-1){
                sb.append(new String(buf));
                buf=new byte[1024];//重新生成，避免和上次读取的数据重复
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 活动列表
     * @param listener
     */
    public static void getSchemeList(int type, int pageNo, int pageSize, String stgId,String memberId, IModelResultListener<ExercisesMode> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("type", type);
        baseReqParamNetMap.put("pageNo", pageNo);
        baseReqParamNetMap.put("pageSize", pageSize);
        baseReqParamNetMap.put("stgId", stgId);
        baseReqParamNetMap.put("memberId", memberId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_schemeList);
        new HomeWebHelper().sendPostWithTranslate(ExercisesMode.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }

    /**
     * 头条广告
     * @param listener
     */
    public static void getHeadLineList(IModelResultListener<DBModel_SlideBanner> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("showNum", 6);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_getHeadLineList);
        new CardWebHelper().sendPostWithTranslate(DBModel_SlideBanner.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }
}
