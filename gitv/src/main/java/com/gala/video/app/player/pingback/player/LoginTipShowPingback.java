package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class LoginTipShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "block", "now_c1"};

    public LoginTipShowPingback() {
        super(TYPES);
    }
}
