package com.imovie.mogic.gameHall.net;

import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.gameHall.model.ReviewListModel;
import com.imovie.mogic.home.model.GameHall;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class HallWebHelper extends HttpWebHelper{

    /**
     * 评论列表
     * @param listener
     */
    public static void getReviewList(int stgId, IModelResultListener<ReviewListModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("stgId", stgId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_reviewList);
        new HallWebHelper().sendPostWithTranslate(ReviewListModel.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }

    /**
     * 馆详情
     * @param listener
     */
    public static void getHallDetail(int stgId, IModelResultListener<GameHall> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("stgId", stgId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.url_stgDetail);
        new HallWebHelper().sendPostWithTranslate(GameHall.class, data.toString(), HttpHelper.TYPE_2,HttpWebHelper.TYPE_3, baseReqParamNetMap, listener);
    }

}
