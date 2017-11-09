package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ClickPingback;

public class LoginTipClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"block", "rt", "rseat", "rpage", "c1", "now_c1", "now_qpid"};

    public LoginTipClickPingback() {
        super(TYPES);
    }
}
