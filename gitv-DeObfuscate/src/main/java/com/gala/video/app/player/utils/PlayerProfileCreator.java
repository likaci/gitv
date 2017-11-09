package com.gala.video.app.player.utils;

import com.gala.sdk.player.IPlayerProfile;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerProfileCreator.Wrapper;

public class PlayerProfileCreator extends Wrapper {
    private static final String sTag = "PlayerProfileCreator";

    public IPlayerProfile createProfile() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(sTag, "createProfile()");
        }
        return new MyPlayerProfile();
    }
}
