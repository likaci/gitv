package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.ShowPingback;

public class CardPageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "rfr", "e", "block", "line", "now_c1"};

    public CardPageShowPingback() {
        super(TYPES);
    }
}
