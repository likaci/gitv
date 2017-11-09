package com.gala.video.app.epg.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class AdbConfigUtils {
    public static final boolean ADB_CONFIG_DEBUG = false;
    private static final String HOME_DATA_REFRESH_INTERVAL = "home_data_refresh_interval";
    private static final String IS_OPEN_EXIT_RECOMMEND_SHOW = "isOpenExitRecommendShow";
    private static final String IS_OPEN_MONKEY_TEST = "isOpenMonkeyTest";
    private static final String IS_OPEN_OPERATE_IMAGE_SEQUENCE_SHOW_TEST = "isOpenOperateShowTest";
    private static final String SCREEN_SAVER_INTERVAL = "screen_saver_interval";
    private static final String TAB_INFO_REFRESH_INTERVAL = "tab_info_refresh_interval";
    private static final String TAG = "AdbConfigUtils";
    private static volatile Method get = null;
    private static volatile Method set = null;

    public static String getValueOld(String key) {
        if (StringUtils.isEmpty((CharSequence) key)) {
            return "";
        }
        String cmd = "getprop " + key;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
            String temp = "";
            String result = "";
            while (true) {
                temp = br.readLine();
                if (temp != null) {
                    result = result + temp;
                } else {
                    LogUtils.d(TAG, "getValue key = " + key + " value = " + result);
                    return result;
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "getValue cmd = " + cmd + " exception ", e);
            return "";
        }
    }

    public static String getValue(String prop) {
        String value = "";
        try {
            if (get == null) {
                synchronized (AdbConfigUtils.class) {
                    if (get == null) {
                        get = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class});
                    }
                }
            }
            return (String) get.invoke(null, new Object[]{prop, ""});
        } catch (Throwable e) {
            e.printStackTrace();
            return value;
        }
    }

    public static long getHomeDataRefreshInterval() {
        long j = -1;
        CharSequence interval = getValue(HOME_DATA_REFRESH_INTERVAL);
        if (StringUtils.isEmpty(interval)) {
            return j;
        }
        try {
            return (Long.valueOf(interval).longValue() * 60) * 1000;
        } catch (Exception e) {
            LogUtils.e(TAG, "getHomeDataRefreshInterval, exception = ", e);
            return j;
        }
    }

    public static long getTabInfoRefreshInterval() {
        long j = -1;
        CharSequence interval = getValue(TAB_INFO_REFRESH_INTERVAL);
        if (StringUtils.isEmpty(interval)) {
            return j;
        }
        try {
            return (Long.valueOf(interval).longValue() * 60) * 1000;
        } catch (Exception e) {
            LogUtils.e(TAG, "getTabInfoRefreshInterval, exception = ", e);
            return j;
        }
    }

    public static long getScreenSaverInterval() {
        long j = -1;
        CharSequence interval = getValue(SCREEN_SAVER_INTERVAL);
        if (StringUtils.isEmpty(interval)) {
            return j;
        }
        try {
            return (Long.valueOf(interval).longValue() * 60) * 1000;
        } catch (Exception e) {
            LogUtils.e(TAG, "getScreenSaverInterval, exception = ", e);
            return j;
        }
    }

    public static boolean isMonkeyTest() {
        boolean z = false;
        CharSequence interval = getValue(IS_OPEN_MONKEY_TEST);
        LogUtils.d(TAG, "isOpenMonkeyTest:" + interval);
        if (!StringUtils.isEmpty(interval)) {
            try {
                z = Boolean.valueOf(interval).booleanValue();
            } catch (Exception e) {
                LogUtils.e(TAG, "getScreenSaverInterval, exception = ", e);
            }
        }
        return z;
    }

    public static boolean isOpenOperateImageSequenceShowTest() {
        boolean z = false;
        CharSequence result = getValue(IS_OPEN_OPERATE_IMAGE_SEQUENCE_SHOW_TEST);
        LogUtils.d(TAG, "isOpenOperateImageSequenceShowTest:" + result);
        if (!StringUtils.isEmpty(result)) {
            try {
                z = Boolean.valueOf(result).booleanValue();
            } catch (Exception e) {
                LogUtils.e(TAG, "isOpenOperateImageSequenceShowTest, exception = ", e);
            }
        }
        return z;
    }

    public static boolean isOpenExitRecommendShow() {
        boolean z = false;
        CharSequence result = getValue(IS_OPEN_EXIT_RECOMMEND_SHOW);
        LogUtils.d(TAG, "isOpenExitRecommendShowTest:" + result);
        if (!StringUtils.isEmpty(result)) {
            try {
                z = Boolean.valueOf(result).booleanValue();
            } catch (Exception e) {
                LogUtils.e(TAG, "isOpenExitRecommendShowTest, exception = ", e);
            }
        }
        return z;
    }
}
