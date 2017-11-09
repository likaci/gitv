package com.gala.video.app.epg.init.task;

import com.gala.video.app.epg.multiscreen.MultiScreenStartTool;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.project.Project;

public class MultiScreenInitTask implements Runnable {
    private static final String TAG = "init/MultiScreenInitTask";

    public void run() {
        LogUtils.d(TAG, "MultiScreen init");
        MultiScreen.get().setDlnaLogEnabled(false);
        MultiScreen.get().setTvVersion(Project.getInstance().getBuild().getVersionString());
        if (MultiScreen.get().isSupportMS()) {
            MultiScreenStartTool.start(AppRuntimeEnv.get().getApplicationContext());
        } else {
            MultiScreen.get().stop();
        }
    }
}
