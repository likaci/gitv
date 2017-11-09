package com.gala.video.app.player.pingback.detail;

import com.gala.video.app.player.pingback.ShowPingback;

public class DetailPageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "rfr", "e", "block", "now_c1"};

    public DetailPageShowPingback() {
        super(TYPES);
    }
}
