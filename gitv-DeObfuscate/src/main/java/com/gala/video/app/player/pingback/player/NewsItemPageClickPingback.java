package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ClickPingback;

public class NewsItemPageClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "c1"};

    public NewsItemPageClickPingback() {
        super(TYPES);
    }
}
