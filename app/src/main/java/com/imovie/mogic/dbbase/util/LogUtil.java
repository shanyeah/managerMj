package com.imovie.mogic.dbbase.util;

import android.util.Log;


import com.imovie.mogic.dbbase.exception.BaseException;

import java.util.Date;

/**
 * Created by zhouxinshan on 2016/04/06.
 */
public class LogUtil {
    public static void LogMsg(Class cls, String msg) {

        //没有开启记录功能则直接返回
        /*
        if (!AppConfig.isLogOn())
            return;
        */
        try {
            String className = "className";
            if (cls != null) {
                className = cls.getSimpleName();
            }

            if (msg != null) {
                System.out.print("####################################################\n");
                Log.i(className, "－－－－－－－－－－－－－－－－－－－－" + className + "  MSG  －－－－－－－－－－－－－－－－－－－－");
                Log.i(className, "| \n");
                String[] msgLines = msg.split("\n");
                for (String oneLine : msgLines) {
                    Log.i(className, "| " + oneLine);
                }
                Log.i(className, "| \n");
                Log.i(className, "－－－－－－－－－－－－－－－－－－－－" + className + "  MSG END  －－－－－－－－－－－－－－－－－－－－");
                System.out.print("####################################################\n");
            } else {
                msg = className + "is log a message!";
                Log.i(className, msg);
            }

            //为消息添加日期标签
            String currentTime = null;
            Date date;
            date = new Date();
            String dateString = DateUtil.Date2String(date, "yyyy-MM-dd HH:mm:ss");
            msg = dateString + "\n" + msg;

            /*
            if (!AppConfig.isSaveLog())
                return;
            FileUtil.createTXTFileAtPath(AppConfig.logPath+dateString.substring(0,10)+ File.separator, msg, className);
            */
        } catch (Exception ex) {
            Log.e(LogUtil.class.getSimpleName(), ex.getMessage());

        }

    }

    public static void LogErr(Class cls, Exception ex) {
        LogErr(cls, "", ex);
    }

    public static void LogErr(Class cls, String msg, Exception ex) {
        /*
        if (!AppConfig.isLogOn()) {
            return;
        }
        */
        try {
            String className = (cls != null) ? cls.getSimpleName() : "className";
            String errorMsg = (null != msg) ? msg : "an error message!";
            Exception exp = (null != ex) ? ex : new BaseException();
            Log.e(className, msg, exp);

            //为消息添加日期标签
            Date date;
            date = new Date();
            StringBuilder errorStringBuilder = new StringBuilder();
            errorStringBuilder.append("\n");
            errorStringBuilder.append("*********[Error]*********\n");
            String dateString = DateUtil.Date2String(date, "yyyy-MM-dd HH:mm:ss");
            errorStringBuilder.append(dateString);
            errorStringBuilder.append("\n");
            errorStringBuilder.append(errorMsg);
            errorStringBuilder.append("\n");
            errorStringBuilder.append("*********[Error]*********\n");
            errorStringBuilder.append("\n");

            /*
            if (!AppConfig.isSaveLog())
                return;
            FileUtil.createTXTFileAtPath(AppConfig.logPath+dateString.substring(0,10)+File.separator, errorStringBuilder.toString(), className);
            */
        } catch (Exception e) {
            Log.e(LogUtil.class.getSimpleName(), "", e);
        }

    }
}
