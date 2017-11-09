package com.gala.video.lib.share.ifimpl.startup;

import com.gala.video.lib.share.ifmanager.bussnessIF.startup.IInit;

public class InitCreator {
    public static IInit create() {
        return new Init();
    }
}
