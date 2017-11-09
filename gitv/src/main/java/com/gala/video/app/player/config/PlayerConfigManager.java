package com.gala.video.app.player.config;

import com.gala.sdk.utils.MyLogUtils;
import com.gala.video.app.player.config.playerconfig.CachedPlayerConfig;
import com.gala.video.app.player.config.playerconfig.DefaultPlayerConfig;
import com.gala.video.app.player.config.playerconfig.IPlayerConfig;
import com.gala.video.app.player.config.playerconfig.LocalPlayerConfig;
import com.gala.video.app.player.config.playerconfig.RemotePlayerConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;

public class PlayerConfigManager {
    private static final String TAG = "PlayerConfigManager";
    private static boolean mInitialized = false;
    private static IPlayerConfig sCachedPlayerConfig;
    private static IPlayerConfig sDefaultPlayerConfig;
    private static IPlayerConfig sLocalPlayerConfig;
    private static IPlayerConfig sRemotePlayerConfig;

    private static void init() {
        sLocalPlayerConfig = LocalPlayerConfig.instance();
        sRemotePlayerConfig = RemotePlayerConfig.instance();
        sDefaultPlayerConfig = DefaultPlayerConfig.instance();
        sCachedPlayerConfig = CachedPlayerConfig.instance();
        mInitialized = true;
    }

    public static IPlayerConfig getPlayerConfig() {
        if (!mInitialized) {
            init();
        }
        int testCloudJsConfig = PlayerDebugUtils.testCloudJsConfig();
        MyLogUtils.d(TAG, "getPlayerConfig() " + testCloudJsConfig);
        if (checkConfigAvailable(sRemotePlayerConfig) && testCloudJsConfig == 0) {
            MyLogUtils.d(TAG, "getPlayerConfig(), return " + sRemotePlayerConfig);
            return sRemotePlayerConfig;
        } else if (checkConfigAvailable(sCachedPlayerConfig)) {
            MyLogUtils.d(TAG, "getPlayerConfig(), return " + sCachedPlayerConfig);
            return sCachedPlayerConfig;
        } else if (checkConfigAvailable(sLocalPlayerConfig)) {
            MyLogUtils.d(TAG, "getPlayerConfig(), return " + sLocalPlayerConfig);
            return sLocalPlayerConfig;
        } else {
            MyLogUtils.d(TAG, "getPlayerConfig(), return " + sDefaultPlayerConfig);
            return sDefaultPlayerConfig;
        }
    }

    private static boolean checkConfigAvailable(IPlayerConfig config) {
        if (config == null) {
            return false;
        }
        if (config.ready()) {
            return true;
        }
        config.load();
        return false;
    }
}
