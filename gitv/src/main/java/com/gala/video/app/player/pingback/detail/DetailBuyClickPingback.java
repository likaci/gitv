package com.gala.video.app.player.pingback.detail;

import com.gala.video.app.player.pingback.ClickPingback;

public class DetailBuyClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "c1", "e", "rfr", "now_c1", "now_qpid", "s2"};

    public DetailBuyClickPingback() {
        super(TYPES);
    }
}
