package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class EpisodeAdPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "qtcurl", "e", "block"};

    public EpisodeAdPingback() {
        super(TYPES);
    }
}
