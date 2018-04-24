package com.imovie.mogic.utills;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.math.RoundingMode;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

public class DecimalUtil {
    // format the money to "#.00"
    public static String FormatMoney(double dMoney) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(dMoney);
        } catch (Exception ex) {
            return "";
        }
    }

    // format the money to "#.00"
    public static String FormatMoney(String strMoney) {
        try {
            return FormatMoney(Double.parseDouble(strMoney));
        } catch (Exception ex) {
            return "";
        }
    }
    /**
     * format the money as pattern,
     * @param dMoney
     * @param pattern like "#.00"
     * @return
     */
    public static String FormatMoney(Double dMoney, String pattern) {
        try {
            DecimalFormat f = new DecimalFormat(pattern);
            f.setRoundingMode(RoundingMode.HALF_UP);
            return f.format(dMoney);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String FormatMoney(float dMoney, String pattern) {
        try {
            DecimalFormat f = new DecimalFormat(pattern);
            f.setRoundingMode(RoundingMode.HALF_UP);
            return f.format(dMoney);
        } catch (Exception ex) {
            return "";
        }
    }


    /**
     * 是否a>b
     */
    public static boolean isAGtB(double a, double b) {
        return !DecimalUtil.isEltZero(a - b);
    }
    /**
     * 是否a<b
     */
    public static boolean isALtB(double a, double b) {
        return !DecimalUtil.isEltZero(b - a);
    }

    /**
     * 是否小于等于零。取小数点后7位的精度比较
     * @param d
     * @return
     */
    public static boolean isEltZero(double d) {
        return d < 0.0000001;
    }

    /**
     * 是否小于等于零。取小数点后7位的精度比较
     * @param str
     * @return
     */
    public static boolean isEltZero(String str) {
        try {
            if (null == str || str.isEmpty()) {
                return true;
            }
            double dPrice = Double.valueOf(str);
            return isEltZero(dPrice);
        } catch (Exception ex) {
            LogUtil.LogErr(null, ex);
        }
        return true;
    }

    /**
     * 获取中文金额符号
     * @param context
     * @return
     */
    public static String getRmbSymbol(Context context) {
        return context.getResources().getString(R.string.symbol_of_RMB);
    }

    public static String getLocalIpAddress()
    {
        try
        {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        catch (Exception ex)
        {
            LogUtil.LogErr(null, ex);
            return null;
        }

    }

    public static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }


    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }
}
