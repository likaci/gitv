package com.gala.video.app.player.pingback.detail;

import com.gala.video.app.player.pingback.ClickPingback;

public class CarPageClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "isprevue", "c1", "e", "rfr", "now_c1", "now_qpid", "now_ep", "line"};

    public CarPageClickPingback() {
        super(TYPES);
    }
}
