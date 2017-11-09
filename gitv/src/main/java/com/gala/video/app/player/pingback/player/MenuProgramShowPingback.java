package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.ShowPingback;

public class MenuProgramShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "e", "block", "c2", "qy_prv", "now_c1"};

    public MenuProgramShowPingback() {
        super(TYPES);
    }
}
