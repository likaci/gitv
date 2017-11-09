package com.gala.video.app.epg.init.task;

import android.os.SystemClock;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;

public class AppendPingbackParamsTask implements Runnable {
    private static final String TAG = "AppendPingbackParamsTask";

    public void run() {
        PingBackInitParams params = PingBack.getInstance().getPingbackInitParams();
        if (params != null) {
            long st = SystemClock.currentThreadTimeMillis();
            params.sIsSmallWindowDisable = !Project.getInstance().getBuild().isSupportSmallWindowPlay();
            PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), params);
            LogUtils.m1568d(TAG, ",calc small window cost : " + (SystemClock.currentThreadTimeMillis() - st) + " ms");
        }
    }
}
