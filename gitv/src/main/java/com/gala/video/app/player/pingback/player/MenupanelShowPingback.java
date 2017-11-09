package com.gala.video.app.player.pingback.player;

import com.gala.pingback.PingbackStore.IS1080P;
import com.gala.pingback.PingbackStore.IS4K;
import com.gala.video.app.player.pingback.ShowPingback;

public class MenupanelShowPingback extends ShowPingback {
    private static final String[] TYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "e", "block", "c2", "qy_prv", "viprate", IS4K.KEY, IS1080P.KEY};

    public MenupanelShowPingback() {
        super(TYPES);
    }
}
