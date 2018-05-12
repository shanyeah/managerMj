package com.imovie.mogic;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.content.FileProvider;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.imovie.mogic.mine.UpdateMyInfoActivity;
import com.imovie.mogic.utills.Utills;
import com.imovie.mogic.utills.baidu.GetLocation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;

/**
 * Created by zhou on 2017/3/7 0007.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    public SharedPreferences mPref;
    //用户当前的经纬度
    private double latitude = -1;
    private double longitude = -1;
    private String address = "";

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        GetLocation getLocation = new GetLocation(getApplicationContext());
        getLocation.start();
        instance = this;
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        initImageLoader(getApplicationContext());

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }


    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 6;

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
                .discCacheSize(64 * 1024 * 1024)
                .threadPoolSize(3);
//        // 如果是调试模式，输出日志，否则不输出
//        if (false) {
//            builder.writeDebugLogs();
//        }
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(builder.build());

        L.writeLogs(false);
    }

    public boolean installNewVersion(Context context, String apkFilePath) {
        boolean bl = true;
        try {
            //Runtime.getRuntime().exec("pm install -r "+apkFilePath);//+";am start -n cn.com.imovie.player/cn.com.imovie.player.activity.SplashActivity;");
            File apkfile = new File(apkFilePath);
            if (apkfile.exists()){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                    i.setDataAndType(Uri.parse("file://" + apkFilePath),"application/vnd.android.package-archive");
                }else{
                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */

                    Uri uriForFile = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", apkfile);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    i.setDataAndType(uriForFile, getApplicationContext().getContentResolver().getType(uriForFile));
                    i.setDataAndType(uriForFile,"application/vnd.android.package-archive");
                }

                context.startActivity(i);
                System.exit(0);
            }
        } catch (Exception e) {
            bl = false;
            e.printStackTrace();
        }
        return bl;
    }

    public void setLocation(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    /**
     * 获取坐标（经纬度）
     */
    public LatLng getCoordinate() {
        double lat = (latitude == -1) ? 0.0 : latitude;
        double lon = (longitude == -1) ? 0.0 : longitude;
        return new LatLng(lat, lon);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public String getMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getWifiName() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getSSID();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
