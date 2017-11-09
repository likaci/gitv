package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class PageLoadPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"rpage", "e", "td", "localtime"};
    private static final String[] TYPES = new String[]{"rpage", "e", "td", "localtime"};

    public PageLoadPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "11").add("ct", "150710_pageshow");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToLongYuan(keys);
    }
}
