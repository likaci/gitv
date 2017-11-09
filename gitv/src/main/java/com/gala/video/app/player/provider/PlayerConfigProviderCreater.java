package com.gala.video.app.player.provider;

import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerConfigProvider;

public class PlayerConfigProviderCreater {
    public static IPlayerConfigProvider create() {
        return new PlayerConfigProvider();
    }
}
