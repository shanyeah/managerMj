package com.imovie.mogic.myRank.net;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.card.model.InternetBarModel;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.home.net.HomeWebHelper;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.myRank.model.MovieModel;
import com.imovie.mogic.myRank.model.OrderRecord;
import com.imovie.mogic.myRank.model.PraiseMode;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class RankWebHelper extends HttpWebHelper{
    /**
     * 点赞排行
     * @param listener
     */
    public static void getPraiseRank(IModelResultListener<PraiseMode> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("stgId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_likeRanking));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(PraiseMode.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }

    /**
     * 充值排行
     * @param listener
     */
    public static void getChargeRank(IModelResultListener<PraiseMode> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("stgId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_chargeRanking));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(PraiseMode.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }

    /**
     * 点餐排行
     * @param listener
     */
    public static void getGoodsRank(IModelResultListener<PraiseMode> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("stgId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_goodsRanking));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(PraiseMode.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }

    /**
     * 点赞记录
     * @param listener
     */
    public static void getPraisePage(int pageNum ,int pageSize,IModelResultListener<OrderRecord> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        baseReqParamNetMap.put("pageNum", pageNum);
        baseReqParamNetMap.put("pageSize", pageSize);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_likePage));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(OrderRecord.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 点餐记录
     * @param listener
     */
    public static void getGoodsPage(int pageNum ,int pageSize,IModelResultListener<OrderRecord> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        baseReqParamNetMap.put("pageNum", pageNum);
        baseReqParamNetMap.put("pageSize", pageSize);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_goodsPage));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(OrderRecord.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 充值记录
     * @param listener
     */
    public static void getChargeRecord(int pageNum ,int pageSize,IModelResultListener<OrderRecord> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        baseReqParamNetMap.put("pageNum", pageNum);
        baseReqParamNetMap.put("pageSize", pageSize);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_chargeRecord));
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(OrderRecord.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);

    }

    /**
     * 点赞次数
     * @param listener
     */
    public static void getPraiseNum(int type,IModelResultListener<PraiseMode> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("startTime", "");
        baseReqParamNetMap.put("endTime", "");
        StringBuffer data = new StringBuffer();
        switch (type){
            case 1:
                data.append(HTTPConfig.getUrlData(HTTPConfig.url_likeNum));
                break;
            case 2:
                data.append(HTTPConfig.getUrlData(HTTPConfig.url_goodsNum));
                break;
            case 3:
                data.append(HTTPConfig.getUrlData(HTTPConfig.url_chargeNum));
                break;
        }
//        data.append("distDate=" + distDate);
        new HomeWebHelper().sendPostWithTranslate(PraiseMode.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }
























    /**
     * 影片条件
     * @param listener
     */
    public static void getMovieCondition(String movieName, String movieCat,String movieTimes, String movieArea,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        String sign = MD5Helper.encode(distDate + "|" + hid + "|iwatchhome");
        baseReqParamNetMap.put("movieName", movieName);
        baseReqParamNetMap.put("movieCat", movieCat);
        baseReqParamNetMap.put("movieTimes", movieTimes);
        baseReqParamNetMap.put("movieArea", movieArea);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_Condition );
//        data.append("distDate=" + distDate);
        new RankWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, 0,baseReqParamNetMap, listener);
    }

    /**
     * 电影列表
     * @param listener
     */
    public static void getMovieList(int orderType, int pageNo, int pageSize, String movieName, String movieCat,String movieTimes, String movieArea,IModelResultListener<MovieModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
//        String sign = MD5Helper.encode(distDate + "|" + hid + "|iwatchhome");
        baseReqParamNetMap.put("orderType", orderType);
        baseReqParamNetMap.put("pageNo", pageNo);
        baseReqParamNetMap.put("pageSize", pageSize);
        baseReqParamNetMap.put("movieName", movieName);
        baseReqParamNetMap.put("movieCat", movieCat);
        baseReqParamNetMap.put("movieTimes", movieTimes);
        baseReqParamNetMap.put("movieArea", movieArea);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_movieList );
//        data.append("distDate=" + distDate);
        new RankWebHelper().sendPostWithTranslate(MovieModel.class, data.toString(), HttpHelper.TYPE_2, 2,baseReqParamNetMap, listener);
    }

    /**
     * 电影信息
     * @param listener
     */
    public static void getMovieDetail(int movieId,IModelResultListener<MovieModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("movieId", movieId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_movieDetail);
        new RankWebHelper().sendPostWithTranslate(MovieModel.class, data.toString(), HttpHelper.TYPE_2, 0,baseReqParamNetMap, listener);
    }

    /**
     * 馆列表
     * @param latitude
     * @param longitude
     * @param listener
     */
    public static void getHallList(int orderType, int pageNo, int pageSize, String latitude, String longitude, IModelResultListener<GameHall> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("orderType", orderType);
        baseReqParamNetMap.put("pageNo", pageNo);
        baseReqParamNetMap.put("pageSize", pageSize);
        baseReqParamNetMap.put("latitude", latitude);
        baseReqParamNetMap.put("longitude", longitude);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_hallList);
        new RankWebHelper().sendPostWithTranslate(GameHall.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }

    /**
     * 网吧列表
     * @param listener
     */
    public static void getMovieStgList(int movieId,IModelResultListener<InternetBarModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("movieId", ""+movieId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_movieStgList);
        new RankWebHelper().sendPostWithTranslate(InternetBarModel.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }

    /**
     * 房型列表
     * @param listener
     */
    public static void getRoomCategoryList(String stgId,IModelResultListener<InternetBarModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("stgId", stgId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_roomCategoryList);
        new RankWebHelper().sendPostWithTranslate(InternetBarModel.class, data.toString(), HttpHelper.TYPE_2,1, baseReqParamNetMap, listener);
    }

    /**
     * 预约观影
     * @param listener
     */
    public static void getBookMovie(String stgId,int movieId,String roomCategoryId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int memberId = MyApplication.getInstance().mPref.getInt("memberId",0);
        baseReqParamNetMap.put("memberId", memberId);
        baseReqParamNetMap.put("stgId", stgId);
        baseReqParamNetMap.put("roomCategoryId", roomCategoryId);
        baseReqParamNetMap.put("movieId", ""+movieId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_bookMovie);
        new RankWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2,0, baseReqParamNetMap, listener);
    }

}
