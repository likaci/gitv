package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaAccountManager;

public class GalaAccountCreator {
    public static IGalaAccountManager create() {
        return new GalaAccountManager();
    }
}
