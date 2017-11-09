package com.gala.video.app.epg.startup;

import android.os.SystemClock;
import com.gala.video.app.epg.home.data.hdata.DataRequestRouter;
import com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IStartupDataLoader.Wrapper;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;

public final class StartupDataLoader extends Wrapper {
    private static final int MIN_REQUEST_INTERVAL = 20000;
    private static final String TAG = "startup/StartUpDataReqeust";
    private static long sLastReqeustTime = 0;

    private void start(boolean force, boolean isHome) {
        if (shouldRequestData() || force) {
            sLastReqeustTime = SystemClock.elapsedRealtime();
            LogUtils.m1568d(TAG, "start data request current time =" + sLastReqeustTime);
            DataRequestRouter.sInstance.startBootUpDataRequest(isHome);
            return;
        }
        LogUtils.m1571e(TAG, "data request has not finished,current time = " + System.currentTimeMillis());
    }

    public void load(boolean isHome) {
        start(false, isHome);
    }

    public void forceLoad(boolean isHome) {
        start(true, isHome);
    }

    private boolean shouldRequestData() {
        return DataRequestTaskStrategy.getInstance().isAllTaskFinished() && SystemClock.elapsedRealtime() - sLastReqeustTime > 20000;
    }

    public void stop() {
        sLastReqeustTime = 0;
        DeviceCheckModel.getInstance().clear();
    }

    public boolean isDataLoading() {
        return !shouldRequestData();
    }
}
