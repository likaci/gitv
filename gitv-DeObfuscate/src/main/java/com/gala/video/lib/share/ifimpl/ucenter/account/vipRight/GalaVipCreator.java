package com.gala.video.lib.share.ifimpl.ucenter.account.vipRight;

import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaVipManager;

public class GalaVipCreator {
    public static IGalaVipManager create() {
        return new GalaVipRightsManager();
    }
}
