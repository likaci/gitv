package com.gala.video.app.player.utils.debug;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class DebugOptionsCache {
    private static final String DEBUG_OPTION_PREF_NAME = "debug_options";
    private static final String PREFTAG_PERF_FLOATINGWINDOW = "dbg_perf_floatingwindow";
    private static final String PREFTAG_PERF_LEFTFLOATINGWINDOW = "dbg_perf_left_floating_window";
    private static final String TAG = "Debug/DebugOptionsCache";
    private static boolean sADSeekAfterStart = false;
    private static boolean sCloseADCache = false;
    private static boolean sMovieSeekAfterStart = false;
    private static boolean sOpenADLocalServer = false;
    private static boolean sOpenH211 = false;

    public static boolean isEnableH211() {
        return sOpenH211;
    }

    public static void setEnableH211(boolean enable) {
        sOpenH211 = enable;
    }

    public static boolean isMovieSeekAfterStart() {
        return sMovieSeekAfterStart;
    }

    public static void setMovieSeekAfterStart(boolean enable) {
        sMovieSeekAfterStart = enable;
    }

    public static boolean isCloseADCache() {
        return sCloseADCache;
    }

    public static void setCloseAdCache(boolean enable) {
        sCloseADCache = enable;
    }

    public static boolean isEnableADLocalServer() {
        return sOpenADLocalServer;
    }

    public static void setEnableADLocalServer(boolean enable) {
        sOpenADLocalServer = enable;
    }

    public static boolean isADSeekAfterStart() {
        return sADSeekAfterStart;
    }

    public static void setADSeekAfterStart(boolean enable) {
        sADSeekAfterStart = enable;
    }

    public static boolean isPerfFloatingWindowEnabled(Context context) {
        boolean ret = false;
        if (context != null) {
            ret = context.getSharedPreferences(DEBUG_OPTION_PREF_NAME, 0).getBoolean(PREFTAG_PERF_FLOATINGWINDOW, false);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "isPerfFloatingWindowEnabled=" + ret);
            }
        }
        return ret;
    }

    public static void setPerfFloatingWindowEnabled(Context context, boolean enable) {
        Editor ed = context.getSharedPreferences(DEBUG_OPTION_PREF_NAME, 0).edit();
        ed.putBoolean(PREFTAG_PERF_FLOATINGWINDOW, enable);
        ed.commit();
    }

    public static boolean isPerfLeftFloatingWindowEnabled(Context context) {
        boolean ret = false;
        if (context != null) {
            ret = context.getSharedPreferences(DEBUG_OPTION_PREF_NAME, 0).getBoolean(PREFTAG_PERF_LEFTFLOATINGWINDOW, false);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "isPerfLeftFloatingWindowEnabled=" + ret);
            }
        }
        return ret;
    }

    public static void setPerfLeftFloatingWindowEnabled(Context context, boolean enable) {
        Editor ed = context.getSharedPreferences(DEBUG_OPTION_PREF_NAME, 0).edit();
        ed.putBoolean(PREFTAG_PERF_LEFTFLOATINGWINDOW, enable);
        ed.commit();
    }
}
