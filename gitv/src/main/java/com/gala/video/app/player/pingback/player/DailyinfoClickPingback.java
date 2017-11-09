package com.gala.video.app.player.pingback.player;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.tvos.appdetailpage.client.Constants;
import java.util.Map;

public class DailyinfoClickPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"usract", "ppuid", "event_id", "cid", "bkt", "area", "rank", "taid", "tcid"};
    private static final String[] TYPES = new String[]{"usract", "ppuid", "event_id", "cid", "bkt", "area", "rank", "taid", "tcid"};

    public DailyinfoClickPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add("type", Constants.RECOMMEND_PINGBACK_TYPE_CLICK).add(Keys.PLATFORM, "5201");
        params.add("aid", "");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToAM71(keys);
    }
}
