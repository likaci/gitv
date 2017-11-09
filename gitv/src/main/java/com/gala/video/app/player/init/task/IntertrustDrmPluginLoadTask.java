package com.gala.video.app.player.init.task;

import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.app.player.feature.drm.IntertrustDrmPluginProvider;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class IntertrustDrmPluginLoadTask implements Runnable {
    private static final String TAG = "IntertrustDrmPluginLoadTask";
    private IPlayerFeature mPlayerFeature;

    public void run() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "run()");
        }
        onLoadIntertrustDrmPlugin();
    }

    private synchronized void onLoadIntertrustDrmPlugin() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onLoadIntertrustDrmPlugin()");
        }
        loadPlayerFeatureSync();
        CharSequence drmModulePath = IntertrustDrmPluginProvider.getIntertrustDrmModulePath();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeature: drmModulePath=" + drmModulePath);
        }
        if (!(this.mPlayerFeature == null || StringUtils.isEmpty(drmModulePath))) {
            this.mPlayerFeature.setIntertrustDrmModulePath(drmModulePath);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< onLoadIntertrustDrmPlugin()");
        }
    }

    private void loadPlayerFeatureSync() {
        if (this.mPlayerFeature == null) {
            this.mPlayerFeature = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature();
        }
    }
}
