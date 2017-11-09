package com.gala.video.app.player.pingback.player;

import com.gala.pingback.PingbackStore.IS1080P;
import com.gala.pingback.PingbackStore.IS4K;
import com.gala.video.app.player.pingback.ShowPingback;

public class PanelHDRTogglePageShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "e", "block", "qy_prv", IS4K.KEY, IS1080P.KEY, "now_c1", "c2"};

    public PanelHDRTogglePageShowPingback() {
        super(TYPES);
    }
}
