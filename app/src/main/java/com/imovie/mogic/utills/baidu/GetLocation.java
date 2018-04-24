package com.imovie.mogic.utills.baidu;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.utills.Utills;


public  class GetLocation {

    public LocationClient mLocClient;
    private int  mScanSpan;
    double latitude=0.0;
    double longitude =0.0;


    public GetLocation(Context context) {

        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(new BDLocationListener() {
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                latitude = Math.abs(bdLocation.getLatitude()) < 1 ? 0 : bdLocation.getLatitude();
                longitude = Math.abs(bdLocation.getLongitude()) < 1 ? 0 : bdLocation.getLongitude();

                MyApplication.getInstance().setLocation(latitude, longitude);
                MyApplication.getInstance().setAddress(bdLocation.getAddrStr());
//                MyApplication.getInstance().setProvince(bdLocation.getProvince());
//                MyApplication.getInstance().setCity(bdLocation.getCity());
//                Utills.showShortToast(""+latitude);
            }

            public void onConnectHotSpotMessage(String s, int i) {

            }
/*
            @Override
            public void onReceivePoi(BDLocation bdLocation) {

            }
*/

        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);

    }

    public void start(){
        if(mLocClient != null && !mLocClient.isStarted())
        mLocClient.start();
    }
    public void stop(){
        if(mLocClient != null)
            mLocClient.stop();
    }
}