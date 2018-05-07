package com.imovie.mogic.ScanPay.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.imovie.mogic.ScanPay.ChargeSuccessActivity;
import com.imovie.mogic.ScanPay.zxing.activity.CaptureActivity;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.home.model.ChargeSuccessModel;
import com.imovie.mogic.home.model.PayResultModel;
import com.imovie.mogic.home.model.SearchUserModel;

/**
 * Created by yeah on 2015/11/30.
 */
public class ScanPayManager {

    /**
     * 跳转 CaptureActivity页
     * @param context
     */
    public static void enterCaptureActivity(Context context, BaseActivity baseActivity){
        //判断Android版本是否为6.0以上，如果是则进行权限允许
        if (Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},222);
                return;
            }
        }
        Intent intent = new Intent(context,CaptureActivity.class);
        intent.putExtra("data","");
        intent.putExtra(CaptureActivity.MSG_FROM,CaptureActivity.MSG_OTHER);
//        context.startActivity(intent);
        baseActivity.startActivityForResult(intent,CaptureActivity.MSG_OTHER);

    }

    /**
     * 跳转 CaptureActivity页
     * @param context
     * @param type type=0 App业务, type=1 操作授权 ,type=2 网页跳转
     */
    public static void enterCaptureActivity(Context context,int type){
        //判断Android版本是否为6.0以上，如果是则进行权限允许
        if (Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},222);
                return;
            }
        }
        Intent intent = new Intent(context,CaptureActivity.class);
        intent.putExtra("data",type);
        intent.putExtra(CaptureActivity.MSG_FROM,CaptureActivity.MSG_HOMESCAN);
        context.startActivity(intent);

    }

    /**
     * 跳转 CaptureActivity页
     * @param context
     */
    public static void enterCaptureActivity(Context context, String data, SearchUserModel userModel){
        //判断Android版本是否为6.0以上，如果是则进行权限允许
        if (Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},222);
                return;
            }
        }
        Intent intent = new Intent(context,CaptureActivity.class);
        intent.putExtra("data",data);
        intent.putExtra(CaptureActivity.MSG_FROM,CaptureActivity.MSG_CHAEGE);
        intent.putExtra("userModel",userModel);
        context.startActivity(intent);
//        context.startActivityForResult(intent,CaptureActivity.MSG_CHAEGE);
    }


    /**
     * 跳转 CaptureActivity页
     * @param context
     */
    public static void enterCaptureActivity(Context context,PayResultModel model){
        //判断Android版本是否为6.0以上，如果是则进行权限允许
        if (Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},222);
                return;
            }
        }
        Intent intent = new Intent(context,CaptureActivity.class);
        intent.putExtra(CaptureActivity.MSG_FROM,CaptureActivity.MSG_BUYGOODS);
        intent.putExtra("userModel",model);
        context.startActivity(intent);
    }

    /**
     * 跳转 ChargeSuccessActivity页
     * @param context
     */
    public static void enterChargeSuccessActivity(Context context, String data, ChargeSuccessModel userModel){
        Intent intent = new Intent(context, ChargeSuccessActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("userModel",userModel);
        context.startActivity(intent);
    }

}
