package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.ClickPingback;

public class BottomEpisodeClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "isprevue", "c1", "e", "rfr", "now_qpid", "now_ep", "videolist", "rec", "series", "star"};

    public BottomEpisodeClickPingback() {
        super(TYPES);
    }
}
