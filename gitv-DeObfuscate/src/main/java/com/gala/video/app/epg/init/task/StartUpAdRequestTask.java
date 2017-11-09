package com.gala.video.app.epg.init.task;

import com.gala.video.app.epg.AppStartMode;
import com.gala.video.app.epg.home.ads.controller.StartScreenAdHandler;
import com.gala.video.app.stub.StartUpInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class StartUpAdRequestTask implements Runnable {
    private static final String TAG = "AppendPingbackParamsTask";

    public void run() {
        boolean isLaunchStartMode = StartUpInfo.get().isStartFromLaunch();
        LogUtils.m1568d(TAG, "app start is plugin mode : " + AppStartMode.IS_PLUGIN_MODE + ", is launch start = " + isLaunchStartMode);
        if (AppStartMode.IS_PLUGIN_MODE && isLaunchStartMode) {
            StartScreenAdHandler.instance().init(AppRuntimeEnv.get().getApplicationContext());
            StartScreenAdHandler.instance().start();
        }
    }
}
