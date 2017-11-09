package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class SelectionsShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "e", "block", "c2"};

    public SelectionsShowPingback() {
        super(TYPES);
    }
}
