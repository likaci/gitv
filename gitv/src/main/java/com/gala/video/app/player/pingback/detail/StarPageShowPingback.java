package com.gala.video.app.player.pingback.detail;

import com.gala.video.app.player.pingback.ShowPingback;

public class StarPageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "rfr", "e", "block", "videolist", "rec", "series", "star"};

    public StarPageShowPingback() {
        super(TYPES);
    }
}
