package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ClickPingback;

public class CarouselChannelClickPingback extends ClickPingback {
    private static final String[] VALIDTYPES = new String[]{"block", "rt", "rseat", "rpage", "c1", "c2", "now_c1", "now_c2"};

    public CarouselChannelClickPingback() {
        super(VALIDTYPES);
    }
}
