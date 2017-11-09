package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class InitPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"rpage", "e", "td", "localtime", "hcdn"};
    private static final String[] TYPES = new String[]{"rpage", "e", "td", "localtime", "hcdn"};

    public InitPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11").add("ct", "150710_pageinit");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
