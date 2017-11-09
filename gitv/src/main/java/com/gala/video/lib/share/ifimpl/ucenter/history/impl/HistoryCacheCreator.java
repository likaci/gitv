package com.gala.video.lib.share.ifimpl.ucenter.history.impl;

import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IHistoryCacheManager;

public class HistoryCacheCreator {
    public static IHistoryCacheManager create() {
        return new HistoryCacheManager();
    }
}
