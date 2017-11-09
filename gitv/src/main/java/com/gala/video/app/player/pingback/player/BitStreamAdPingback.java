package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class BitStreamAdPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "qtcurl", "e", "block"};

    public BitStreamAdPingback() {
        super(TYPES);
    }
}
