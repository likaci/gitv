package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class CarouselProgrammeShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "e", "block", "c2", "now_c1"};

    public CarouselProgrammeShowPingback() {
        super(TYPES);
    }
}
