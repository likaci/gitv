package com.gala.video.app.epg;

import com.gala.video.app.epg.startup.StartUpCostInfoProvider;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class AppStartCostTime {
    public static long getStartCostTime() {
        return (StartUpCostInfoProvider.mGalaApplicationCostTime + StartUpCostInfoProvider.mStartUpCompletedTime) - AppRuntimeEnv.get().getStartTime();
    }

    public static long getHomeBuildCostTime() {
        return (StartUpCostInfoProvider.mGalaApplicationCostTime + StartUpCostInfoProvider.mHomeBuildCompletedTime) - AppRuntimeEnv.get().getStartTime();
    }
}
