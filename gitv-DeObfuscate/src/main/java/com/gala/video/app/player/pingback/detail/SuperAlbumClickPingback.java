package com.gala.video.app.player.pingback.detail;

import com.gala.video.app.player.pingback.ClickPingback;

public class SuperAlbumClickPingback extends ClickPingback {
    private static final String[] TYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "c1", "e", "rfr", "now_qpid", "videolist", "rec", "series", "star"};

    public SuperAlbumClickPingback() {
        super(TYPES);
    }
}
