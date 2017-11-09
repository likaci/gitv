package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.tvos.appdetailpage.client.Constants;
import java.util.Map;

public class GuessYoulikeClickPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"ppuid", "rank", "aid", "event_id", "cid", "bkt", "area", "taid", "tcid", "source"};
    private static final String[] TYPES = new String[]{"ppuid", "rank", "aid", "event_id", "cid", "bkt", "area", "taid", "tcid", "source"};

    public GuessYoulikeClickPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add("type", Constants.RECOMMEND_PINGBACK_TYPE_CLICK).add("usract", Constants.RECOMMEND_PINGBACK_USERACT_CLICK).add(Keys.PLATFORM, "5201").add("url", "").add("tag", "").add(Keys.T, "");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToAM71(keys);
    }
}
