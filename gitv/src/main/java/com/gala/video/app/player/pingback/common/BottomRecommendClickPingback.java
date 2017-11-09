package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.ClickPingback;

public class BottomRecommendClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "c1", "e", "rfr", "now_qpid", "videolist", "rec", "series", "star"};

    public BottomRecommendClickPingback() {
        super(TYPES);
    }
}
