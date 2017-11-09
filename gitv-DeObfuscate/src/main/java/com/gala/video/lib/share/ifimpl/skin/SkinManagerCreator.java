package com.gala.video.lib.share.ifimpl.skin;

import com.gala.video.lib.share.ifmanager.bussnessIF.skin.ISkinManager;

public class SkinManagerCreator {
    public static ISkinManager create() {
        return new SkinManager();
    }
}
