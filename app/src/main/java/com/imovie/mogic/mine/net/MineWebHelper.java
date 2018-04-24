package com.imovie.mogic.mine.net;

import com.baidu.mapapi.model.LatLng;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.mine.attend.model.AttendClock;
import com.imovie.mogic.mine.attend.model.AttendDatesModel;
import com.imovie.mogic.mine.model.AttendAreaModel;
import com.imovie.mogic.mine.model.AttendInfo;
import com.imovie.mogic.mine.model.AttendModel;
import com.imovie.mogic.mine.model.AutoModel;
import com.imovie.mogic.mine.model.IncomeModel;
import com.imovie.mogic.mine.model.MyAttendModel;
import com.imovie.mogic.mine.model.ChargeInfoModel;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class MineWebHelper extends HttpWebHelper{


    /**
     * 定餐列表
     * @param listener
     */
    public static void getOrderList(IModelResultListener<AutoModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        data.append(HTTPConfig.url_orderList);
        data.append("?stgId=" + organId);
        new MineWebHelper().sendPostWithTranslate(AutoModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_1,baseReqParamNetMap, listener);
    }

    /**
     * 考勤范围
     * @param listener
     */
    public static void getAttendArea(IModelResultListener<AttendAreaModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        LatLng latLng = MyApplication.getInstance().getCoordinate();
        baseReqParamNetMap.put("latitude", latLng.latitude);
        baseReqParamNetMap.put("longitude", latLng.longitude);
        baseReqParamNetMap.put("sid", MyApplication.getInstance().getMacAddress());
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_attendArea));
        new MineWebHelper().sendPostWithTranslate(AttendAreaModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 考勤信息
     * @param listener
     */
    public static void getAttendInfo(String date,IModelResultListener<AttendInfo> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("attendDate", date);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_attendInfo));
        new MineWebHelper().sendPostWithTranslate(AttendInfo.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 考勤列表
     * @param listener
     */
    public static void getAttendRecords(String date,IModelResultListener<AttendModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("attendDate", date);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_attendRecords));
        new MineWebHelper().sendPostWithTranslate(AttendModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 打卡
     * @param listener
     */
    public static void getClock(String date,int type,int scheduleId,int attendWay,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("attendDate", date);
        baseReqParamNetMap.put("type", type);
        baseReqParamNetMap.put("scheduleId", scheduleId);
        baseReqParamNetMap.put("attendWay", attendWay);
        LatLng latLng = MyApplication.getInstance().getCoordinate();
        baseReqParamNetMap.put("latitude", latLng.latitude);
        baseReqParamNetMap.put("longitude", latLng.longitude);
        baseReqParamNetMap.put("sid", MyApplication.getInstance().getMacAddress());
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_clock));
        new MineWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 统计
     * @param listener
     */
    public static void getMyAttend(String month,IModelResultListener<MyAttendModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("month", month);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_myAttend));
        new MineWebHelper().sendPostWithTranslate(MyAttendModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 打卡月历
     * @param listener
     */
    public static void getMyCalendar(String date,IModelResultListener<AttendDatesModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("attendDate", date);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_myCalendar));
        new MineWebHelper().sendPostWithTranslate(AttendDatesModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }


    /**
     * 补卡
     * @param listener
     */
    public static void getRecheckClock(String date,String applyReason,int attendRecordId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("attendRecordId", attendRecordId);
        baseReqParamNetMap.put("checkTime", date);
        baseReqParamNetMap.put("applyReason", applyReason);
        baseReqParamNetMap.put("address", MyApplication.getInstance().getAddress());
        baseReqParamNetMap.put("wifiName", MyApplication.getInstance().getWifiName());
        LatLng latLng = MyApplication.getInstance().getCoordinate();
        baseReqParamNetMap.put("latitude", latLng.latitude);
        baseReqParamNetMap.put("longitude", latLng.longitude);
        baseReqParamNetMap.put("sid", MyApplication.getInstance().getMacAddress());
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_recheckClock));
        new MineWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 补卡
     * @param listener
     */
    public static void getRecheckInfo(int attendRecordId,IModelResultListener<AttendClock> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("attendRecordId", attendRecordId);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_recheckInfo));
        new MineWebHelper().sendPostWithTranslate(AttendClock.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 改密码
     * @param listener
     */
    public static void setPassword(String newPassword,String oldPassword,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("newPassword", newPassword);
        baseReqParamNetMap.put("oldPassword", oldPassword);
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_setPassword));
        new MineWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 收入汇总
     * @param listener
     */
    public static void getIncomeSummary(IModelResultListener<IncomeModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
        data.append(HTTPConfig.getUrlData(HTTPConfig.url_incomeSummary));
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        int adminId = MyApplication.getInstance().mPref.getInt("adminId",0);
        data.append("&organId="+organId);
        data.append("&adminId="+adminId);
        new MineWebHelper().sendPostWithTranslate(IncomeModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }


    /**
     * 充值明细
     * @param listener
     */
    public static void getChargeInfo(int pageNum ,int pageSize,String startTime,String endTime,int fromType,IModelResultListener<ChargeInfoModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        int organId = MyApplication.getInstance().mPref.getInt("organId",0);
        int adminId = MyApplication.getInstance().mPref.getInt("adminId",0);
        baseReqParamNetMap.put("organId", organId);
        baseReqParamNetMap.put("adminId", adminId);
        baseReqParamNetMap.put("startTimeStr", startTime);
        baseReqParamNetMap.put("endTimeStr", endTime);
        baseReqParamNetMap.put("pageNum", pageNum);
        baseReqParamNetMap.put("pageSize", pageSize);
        StringBuffer data = new StringBuffer();
        if(fromType == 21){
            data.append(HTTPConfig.getUrlData(HTTPConfig.url_incomeCharge));
        }else if(fromType == 22){
            data.append(HTTPConfig.getUrlData(HTTPConfig.url_incomePraise));
        }else if(fromType == 23){
            data.append(HTTPConfig.getUrlData(HTTPConfig.url_incomeOrder));
        }
        new MineWebHelper().sendPostWithTranslate(ChargeInfoModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

























}
