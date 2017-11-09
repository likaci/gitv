package com.gala.video.app.player.provider;

import com.gala.video.lib.share.ifmanager.bussnessIF.player.IGalaPlayerPageProvider;

public class GalaPlayerPageProviderCreater {
    public static IGalaPlayerPageProvider create() {
        return new GalaPlayerPageProvider();
    }
}
