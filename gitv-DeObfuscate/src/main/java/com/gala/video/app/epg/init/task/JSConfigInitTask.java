package com.gala.video.app.epg.init.task;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.web.provider.WebPluginProvider;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;

public class JSConfigInitTask implements Runnable {
    private static final String TAG = "JSConfigInitTask";

    public void run() {
        LogUtils.m1568d(TAG, "JSConfigInitTask begin run");
        GetInterfaceTools.getIJSConfigDataProvider().loadData();
        initWebPlugin();
    }

    public void initWebPlugin() {
        if (!Project.getInstance().getControl().isOpenCrossWalk()) {
            LogUtils.m1568d(TAG, "initWebPlugin() -> is not OpenCrossWalk");
        } else if (GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsDisableCrosswalk()) {
            LogUtils.m1568d(TAG, "initWebPlugin() -> dynamic disable crosswalk");
        } else if (GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().isCrosswalkEnable()) {
            LogUtils.m1568d(TAG, "initWebPlugin() -> id isEnableCrosswalk");
            WebPluginProvider.getInstance().load();
        } else {
            LogUtils.m1571e(TAG, "initWebPlugin() -> DisableCrosswalk");
        }
    }
}
