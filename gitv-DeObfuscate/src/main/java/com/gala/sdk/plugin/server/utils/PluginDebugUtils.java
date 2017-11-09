package com.gala.sdk.plugin.server.utils;

import com.gala.sdk.plugin.Log;

public class PluginDebugUtils {
    private static final String TAG = "PluginDebugUtils";
    private static boolean mHostAllowDebug = true;

    public static class DEBUG_PROPERTY {
        public static final String CHECK_UPGRADE_FAILED = "checkUpgradeFailed";
        public static final String CONSTRUCTOR_INVISIBLE = "constructorInvisible";
        public static final String COPY_FROM_ASSETS_FAILED = "copyAssetsFailed";
        public static final String COPY_SO_FAILED = "copySoFailed";
        public static final String CREATE_SAVEPATH_FAILED = "createSavepathFailed";
        public static final String DOWNLOAD_IO_EXCAPTION = "downloadIOException";
        public static final String INSTANCE_FAILED = "instanceFailed";
        public static final String LAST_FAILED = "lastFailed";
        public static final String LOAD_CLASS_FAILED = "loadClassFailed";
        public static final String LOAD_LOCAL_FAILED = "loadLocalInfosFailed";
        public static final String MD5_NOT_EQUAL = "md5NotEqual";
        public static final String NEED_DEBUG_UPGRADE_INTERAVL = "upgradeIntervalDebug";
        public static final String OVER_ERROR_COUNT = "overErrorCount";
        public static final String PLAYER_PLUGIN_COPY_FROM_ASSETS_FAILED = "CopyAssetsFailedpluginplayer";
        public static final String PLAYER_PLUGIN_PERIOD_FOUR_LOAD_DOWNLOAD = "periodfourdownload";
        public static final String PLAYER_PLUGIN_PERIOD_ONE_LOCAL_FAIL = "periodonelocal";
        public static final String PLAYER_PLUGIN_PERIOD_THREE_ASSET_SD_FAIL = "periodthreeassetsd";
        public static final String PLAYER_PLUGIN_PERIOD_TWO_ASSET_FAIL = "periodtwoasset";
        public static final String RENAME_FAILED = "renameFailed";
        public static final String UPGRADE_INTERVAL = "upgradeInterval";
        public static final String URL_INVAILD = "urlInvaild";
    }

    public static void setDebug(boolean b) {
        mHostAllowDebug = b;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "setDebug returns " + mHostAllowDebug);
        }
    }

    private static boolean allowDebug() {
        return mHostAllowDebug;
    }

    public static boolean needThrowable(String key) {
        boolean needThrowable = false;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "needThrowable<<(key=" + key + ")");
        }
        if (allowDebug()) {
            needThrowable = SysPropUtils.getBoolean(key, false);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "needThrowable() returns " + needThrowable);
        }
        return needThrowable;
    }

    public static boolean needDebug(String key) {
        boolean needDebug = false;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "needDebug<<(key=" + key + ")");
        }
        if (allowDebug()) {
            needDebug = SysPropUtils.getBoolean(key, false);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "needDebug() returns " + needDebug);
        }
        return needDebug;
    }

    public static int getInt(String key) {
        int value = -1;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getInt<<(key=" + key + ")");
        }
        if (allowDebug()) {
            value = SysPropUtils.getInt(key, -1);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getInt() returns " + value);
        }
        return value;
    }
}
