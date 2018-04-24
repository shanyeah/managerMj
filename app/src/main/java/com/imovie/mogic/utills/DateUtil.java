package com.imovie.mogic.utills;

import com.imovie.mogic.dbbase.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String Date2String(Date date, String format){

        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format);

            String dateStr = format1.format(date);

            return dateStr;

        } catch (Exception ex) {

//            LogUtil.LogErr((new DateUtil()).getClass(), ex);
            return null;
        }


    }

    public static Date String2Date(String dateString, String format){

        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.ENGLISH);

            Date date = format1.parse(dateString);

            return date;

        } catch (Exception ex) {
//            LogUtil.LogErr((new DateUtil()).getClass(), ex);
            return null;
        }
    }

    public static long StringToLongDate(String dateString, String format){

        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.ENGLISH);
            Date date = format1.parse(dateString);
            return date.getTime();
        } catch (Exception ex) {
//            LogUtil.LogErr((new DateUtil()).getClass(), ex);
            return 0;
        }
    }



    /**
     *  long time to string
     * @param time milliseconds
     * @param format like ""yyyy/MM/dd HH:mm:ss""
     * @return
     */
    public static String TimeFormat(long time, String format) {
        String formatString = "yyyy/MM/dd HH:mm:ss";
        if ((null != format) && !format.isEmpty()) {
            formatString = format;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.getDefault());
            return sdf.format(new Date(time));
        } catch (Exception ex) {
            LogUtil.LogErr(DateUtil.class, ex);
            return "";
        }
    }


    public static boolean isAfterToday(Date date) {
        long datestamp = date.getTime();
        long datestampDay = datestamp / (1000 * 60 * 60 * 24);
        long todaystamp = (new Date()).getTime();
        long todaystampDay = todaystamp / (1000 * 60 * 60 * 24);
        return datestampDay > todaystampDay;
    }

    /** 获取年份 */
    public static int getYear(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        return calender.get(Calendar.YEAR);
    }
    /** 获取月份 */
    public static int getMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        return calender.get(Calendar.MONTH);
    }
    /** 添加月份 */
    public static Date addMonth(Date date, int monthStep) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, monthStep);
        return calender.getTime();
    }


}
