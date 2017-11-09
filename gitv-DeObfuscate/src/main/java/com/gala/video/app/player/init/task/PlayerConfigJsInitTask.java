package com.gala.video.app.player.init.task;

import com.gala.video.app.player.config.PlayerConfigManager;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PlayerConfigJsInitTask implements Runnable {
    private static final String TAG = "PlayerConfigJsInitTask";

    public void run() {
        LogUtils.m1568d(TAG, "PlayerConfigJsInitTask execute ");
        PlayerConfigManager.getPlayerConfig();
    }
}
