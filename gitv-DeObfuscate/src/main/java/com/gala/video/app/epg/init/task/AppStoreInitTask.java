package com.gala.video.app.epg.init.task;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gitv.tvappstore.AppStoreManager;

public class AppStoreInitTask implements Runnable {
    private static final String TAG = "startup/AppStoreInitTask";

    public void run() {
        LogUtils.m1568d(TAG, "initAppStoreManager");
        try {
            AppStoreManager.getInstance().init(AppRuntimeEnv.get().getApplicationContext(), Project.getInstance().getBuild().getAppStorePkgName(), AppRuntimeEnv.get().getDefaultUserId());
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "initAppStoreManager() ---> mAppStoreManager e:", e);
        }
    }
}
