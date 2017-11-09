package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class DailyInfoShowPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"ppuid", "event_id", "cid", "bkt", "area", "albumlist", "type", "usract"};
    private static final String[] TYPES = new String[]{"ppuid", "event_id", "cid", "bkt", "area", "albumlist", "type", "usract"};

    public DailyInfoShowPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.PLATFORM, "5201");
        params.add("aid", "");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToAM71(keys);
    }
}
