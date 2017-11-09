package com.gala.video.app.epg;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class GalaPlayerLogProvider {
    private static final String TAG = "GalaPlayerLogProvider";

    private GalaPlayerLogProvider() {
    }

    public static synchronized String getPumaLog() {
        String log;
        synchronized (GalaPlayerLogProvider.class) {
            log = "";
            if (GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready()) {
                try {
                    log = GetInterfaceTools.getPlayerFeatureProxy().getLog(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "getPumaLog():\n" + log);
                }
            }
        }
        return log;
    }

    public static synchronized String getCupIdLog() {
        String log;
        synchronized (GalaPlayerLogProvider.class) {
            log = "";
            if (GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready()) {
                try {
                    log = GetInterfaceTools.getPlayerFeatureProxy().getLog(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "getPumaLog():\n" + log);
                }
            }
        }
        return log;
    }
}
