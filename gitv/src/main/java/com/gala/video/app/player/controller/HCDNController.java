package com.gala.video.app.player.controller;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy.IActiveStateChangeListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IHCDNController.Wrapper;

public class HCDNController extends Wrapper implements IActiveStateChangeListener {
    private static HCDNController sController = null;
    private static final String sTag = "HCDNController";

    public synchronized void initialize() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(sTag, "initialize()");
        }
        if (sController == null) {
            sController = new HCDNController();
            GetInterfaceTools.getActiveStateDispatcher().resister(sController);
        }
    }

    public void turnToActive() {
        LogUtils.d(sTag, "turn to active");
    }

    public void turnToInActive() {
        LogUtils.d(sTag, "turn to inactive");
        GetInterfaceTools.getPlayerFeatureProxy().setHCDNCleanAvailable(true);
    }
}
