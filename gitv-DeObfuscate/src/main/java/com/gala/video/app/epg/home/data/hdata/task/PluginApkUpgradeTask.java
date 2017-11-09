package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.video.app.epg.BundleDownload;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PluginApkUpgradeTask extends BaseRequestTask {
    private static final String TAG = "PluginApkUpgradeTask";

    public void invoke() {
        LogUtils.m1568d(TAG, "invoke plugin apk request task");
        BundleDownload.getInstance().check();
    }

    public void onOneTaskFinished() {
    }
}
