package com.gala.video.app.player.init.task;

import com.gala.video.app.player.pingback.PingbackManager;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PlayerPingbackManagerInitTask implements Runnable {
    private static final String TAG = "PlayerPingbackManagerInitTask";

    public void run() {
        LogUtils.m1568d(TAG, "PlayerPingbackManagerInitTask execute ");
        new PingbackManager().start();
    }
}
