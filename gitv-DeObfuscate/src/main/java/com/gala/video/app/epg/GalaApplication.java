package com.gala.video.app.epg;

import android.app.Application;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import com.gala.video.app.epg.startup.StartUpCostInfoProvider;
import com.gala.video.app.epg.utils.HookAppLoad;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class GalaApplication extends Application {
    private static final String TAG = "GalaApplication";
    private WakeLock mWakeLock;

    public void onCreate() {
        super.onCreate();
        long startTime = SystemClock.elapsedRealtime();
        acquireWakeLock();
        HookAppLoad.expandNativeLibrary(GalaApplication.class.getClassLoader(), getApplicationInfo());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">>gala application create start");
        }
        long start = SystemClock.elapsedRealtime();
        GalaVideoClient.get().setupWithContext(getApplicationContext());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<<gala application create end");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "[start performance] app init cost " + (SystemClock.elapsedRealtime() - start) + " ms");
        }
        StartUpCostInfoProvider.mGalaApplicationCostTime = SystemClock.elapsedRealtime() - startTime;
    }

    private void acquireWakeLock() {
        try {
            if (this.mWakeLock == null) {
                this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, TAG);
                if (this.mWakeLock != null) {
                    this.mWakeLock.acquire();
                }
            }
        } catch (Exception e) {
            LogUtils.m1575i(TAG, "acquireWakeLock", e);
        }
    }
}
