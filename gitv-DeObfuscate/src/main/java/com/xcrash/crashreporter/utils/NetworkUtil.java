package com.xcrash.crashreporter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkUtil {
    public static final int NETWORK_TYPE_HSPAP = 15;

    public static NetworkInfo getAvailableNetWorkInfo(Context context) {
        try {
            NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return (activeNetInfo == null || !activeNetInfo.isAvailable()) ? null : activeNetInfo;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNetAvailable(Context context) {
        return getAvailableNetWorkInfo(context) != null;
    }

    public static boolean isWifiOrEthernetOn(Context context) {
        NetworkInfo networkInfo = getAvailableNetWorkInfo(context);
        if (networkInfo == null) {
            return false;
        }
        int type = networkInfo.getType();
        if (1 == type || 9 == type) {
            return true;
        }
        return false;
    }

    public static String getNetWorkType(Context context) {
        if (context == null) {
            return "";
        }
        if (Utility.isTvGuo()) {
            return "1";
        }
        String netWorkType = "";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo(context);
        if (netWorkInfo == null) {
            return netWorkType;
        }
        if (netWorkInfo.getType() == 1) {
            return "1";
        }
        if (netWorkInfo.getType() != 0) {
            return netWorkType;
        }
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "4";
            case 4:
                return "8";
            case 5:
                return "9";
            case 6:
                return "10";
            case 7:
                return "11";
            case 8:
                return "5";
            case 9:
                return "6";
            case 10:
                return "7";
            case 13:
                return "14";
            case 15:
                return "12";
            default:
                return "-1";
        }
    }

    public static NetworkStatus getNetworkStatus(Context context) {
        NetworkStatus status = getNetworkStatusFor4G(context);
        if (status == NetworkStatus.MOBILE_4G) {
            return NetworkStatus.MOBILE_3G;
        }
        return status;
    }

    public static boolean isNetworkOff(Context context) {
        if (getAvailableNetWorkInfo(context) == null) {
            return true;
        }
        return false;
    }

    public static NetworkStatus getNetworkStatusFor4G(Context context) {
        NetworkInfo networkInfo = getAvailableNetWorkInfo(context);
        if (networkInfo == null) {
            return NetworkStatus.OFF;
        }
        if (1 == networkInfo.getType()) {
            return NetworkStatus.WIFI;
        }
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 1:
            case 2:
            case 4:
                return NetworkStatus.MOBILE_2G;
            case 13:
                return NetworkStatus.MOBILE_4G;
            default:
                return NetworkStatus.MOBILE_3G;
        }
    }
}
