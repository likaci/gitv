package com.gala.video.lib.share.ifimpl.background;

import com.gala.video.lib.share.ifmanager.bussnessIF.background.IBackgroundManager;

public class BackgroundManagerCreator {
    public static IBackgroundManager create() {
        return new BackgroundManager();
    }
}
