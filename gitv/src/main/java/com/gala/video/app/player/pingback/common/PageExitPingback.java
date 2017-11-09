package com.gala.video.app.player.pingback.common;

import com.gala.pingback.PingbackStore.ISPLAYERSTART;
import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class PageExitPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"rpage", "e", "td", "st", "localtime", "ec", "pfec", ISPLAYERSTART.KEY, "hcdn"};
    private static final String[] TYPES = new String[]{"rpage", "e", "td", "st", "localtime", "ec", "pfec", ISPLAYERSTART.KEY, "hcdn"};

    public PageExitPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "11").add("ct", "150710_pagequit");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToLongYuan(keys);
    }
}
