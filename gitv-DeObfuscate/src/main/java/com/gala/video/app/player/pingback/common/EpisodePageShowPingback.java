package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.ShowPingback;

public class EpisodePageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "rfr", "e", "block", "videolist", "rec", "series", "star"};

    public EpisodePageShowPingback() {
        super(TYPES);
    }
}
