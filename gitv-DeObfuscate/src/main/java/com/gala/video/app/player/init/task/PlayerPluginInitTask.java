package com.gala.video.app.player.init.task;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class PlayerPluginInitTask implements Runnable {
    private static final String TAG = "PlayerPluginInitTask";

    public void run() {
        LogUtils.m1568d(TAG, "PlayerPluginInitTask execute ");
        GetInterfaceTools.getPlayerFeatureProxy().initailize(AppRuntimeEnv.get().getApplicationContext());
    }
}
