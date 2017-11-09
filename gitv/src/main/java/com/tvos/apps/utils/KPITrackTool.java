package com.tvos.apps.utils;

import org.cybergarage.soap.SOAP;

public class KPITrackTool {
    public static final String TAG = "KPITrack";

    public enum KPIType {
        TIME_DOWNLOAD_OTA,
        TIME_DOWNLOAD_GAME,
        TIME_INSTALL_GAME,
        TIME_UNINSTALL_GAME,
        TIME_DOWNLOAD_APP,
        TIME_INSTALL_APP,
        TIME_USE_COUPON
    }

    public enum TimeType {
        START,
        FINISH,
        COUNT
    }

    public static void trackOTADownloadTime(String pkgName, long downloadTime) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_DOWNLOAD_OTA, pkgName, downloadTime, TimeType.COUNT));
    }

    public static void trackGameDownloadTime(String gameName, long downloadTime) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_DOWNLOAD_GAME, gameName, downloadTime, TimeType.COUNT));
    }

    public static void trackGameInstallTime(String gameName, long installTime) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_INSTALL_GAME, gameName, installTime, TimeType.COUNT));
    }

    public static void trackGameUninstallTime(String gameName, long uninstallTime, TimeType timeType) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_UNINSTALL_GAME, gameName, uninstallTime, timeType));
    }

    public static void trackAppDownloadTime(String appName, long downloadTime) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_DOWNLOAD_APP, appName, downloadTime, TimeType.COUNT));
    }

    public static void trackAppInstallTime(String appName, long installTime, TimeType timeType) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_INSTALL_APP, appName, installTime, timeType));
    }

    public static void trackUseCouponTime(String couponName, long time, TimeType timeType) {
        LogUtil.d(TAG, formatTimeLogMsg(KPIType.TIME_USE_COUPON, couponName, time, timeType));
    }

    private static String formatTimeLogMsg(KPIType kpiType, String appKey, long time, TimeType timeType) {
        StringBuilder builder = new StringBuilder();
        builder.append(kpiType.name());
        builder.append(SOAP.DELIM);
        builder.append(appKey);
        builder.append(SOAP.DELIM);
        builder.append(time);
        builder.append(SOAP.DELIM);
        builder.append(timeType.name());
        return builder.toString();
    }
}
