package com.gala.video.app.player.utils.debug;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;

public class DetailDebugOptions {
    public static final String PROP_COMMON_TEST_API_ALL_E000001 = "gala.test.detail.e000001";
    public static final String PROP_COMMON_TEST_DETAILLOADING_TIME = "gala.test.detail.loadingtime";
    public static final String PROP_COMMON_TEST_EPISODES_LOST = "gala.test.detail.episodelost";
    public static final String PROP_COMMON_TEST_HTTP_ERROR_CODE = "gala.test.detail.httpcode";
    public static final String PROP_COMMON_TEST_HTTP_JSON_FAIL = "gala.test.detail.jsonfail";
    public static final String PROP_PLAYER_TEST_API_ALL_E000012 = "gala.test.detail.e000012";
    public static final String PROP_PLAYER_TEST_API_ALL_E000054 = "gala.test.detail.e000054";
    private static final String TAG = "AlbumDetail/Debug/DetailDebugOptions";

    static {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "allowDebug() returns false");
        }
    }

    private static boolean allowDebug() {
        return false;
    }

    public static boolean testApiCommonForE000001() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_COMMON_TEST_API_ALL_E000001, false) : false;
    }

    public static boolean testApiAllForE000012() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_ALL_E000012, false) : false;
    }

    public static boolean testApiAllForE000054() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_ALL_E000054, false) : false;
    }

    public static int testHttpCommonErrorCode() {
        return allowDebug() ? SysPropUtils.getInt(PROP_COMMON_TEST_HTTP_ERROR_CODE, 0) : 0;
    }

    public static boolean testHttpJsonFail() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_COMMON_TEST_HTTP_JSON_FAIL, false) : false;
    }

    public static String testEpisodeLost() {
        return allowDebug() ? SysPropUtils.get(PROP_COMMON_TEST_EPISODES_LOST, "") : "";
    }

    public static int getDetailLoadingDelayTime() {
        return allowDebug() ? SysPropUtils.getInt(PROP_COMMON_TEST_DETAILLOADING_TIME, -1) : -1;
    }
}
