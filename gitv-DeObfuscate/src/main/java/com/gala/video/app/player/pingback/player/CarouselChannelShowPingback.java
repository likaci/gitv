package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class CarouselChannelShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "e", "block", "c2", "now_c1"};

    public CarouselChannelShowPingback() {
        super(TYPES);
    }
}
