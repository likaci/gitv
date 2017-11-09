package com.gala.video.app.player.pingback.common;

import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.tvos.appdetailpage.client.Constants;
import java.util.Map;

public class GuessYouLikeShowPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"ppuid", "aid", "event_id", "cid", "bkt", "area", "albumlist", "source"};
    private static final String[] TYPES = new String[]{"ppuid", "aid", "event_id", "cid", "bkt", "area", "albumlist", "source"};

    public GuessYouLikeShowPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add("type", Constants.RECOMMEND_PINGBACK_TYPE_CLICK).add("usract", DBColumns.IS_NEED_SHOW).add(Keys.PLATFORM, "5201").add("rank", "").add("url", "").add("tag", "").add(Keys.f2035T, "").add("taid", "").add("tcid", "");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToAM71(keys);
    }
}
