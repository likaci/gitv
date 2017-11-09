package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class HDRGuidePageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "block"};

    public HDRGuidePageShowPingback() {
        super(TYPES);
    }
}
