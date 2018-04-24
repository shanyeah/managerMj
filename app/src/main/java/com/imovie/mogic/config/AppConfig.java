package com.imovie.mogic.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.io.File;

/**
 * Created by zhouxinshan on 2016/04/06
 * App Config 配置文件
 */
public class AppConfig {
    public final static String dbPath = MyApplication.getInstance().getDir("database", Context.MODE_PRIVATE).getPath() + File.separator + "DB.sqlite";
    public final static String AppName = "mogic";
    public final static String appPath = Environment.getExternalStorageDirectory().getPath() +
            File.separator + AppName + File.separator;

////    public final static String dbPath = "/storage/external_storage/sda1/DB.sqlite";
////    public final static String dbPath = "/storage/udisk0/movieHD/DB.sqlite";
//    public final static String ADCFTT_PATH = "/storage/udisk0/movieHD/";
//    public final static String ADCFTT_IMAGE = "/storage/udisk0/imageMovieHD/";
//    public final static String PATH_PHOTO = MyApplication.LOCAL_HDDDISK_PATH + "/NAS/gallery/";
//    public final static String PATH_MUSIC = MyApplication.LOCAL_HDDDISK_PATH + "/NAS/music/";



    public static void init(Context context) {

    }

    public static int getScreenWidth(){
        int screenWidth = MyApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }


    public static boolean isExitsSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static String getVersionName() {

        try {
            String versionName = MyApplication.getInstance().getPackageManager()
                    .getPackageInfo(MyApplication.getInstance().getPackageName(), 0).versionName;

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.LogErr(AppConfig.class, e);
        }
        return null;

    }

}
