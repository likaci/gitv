package com.gala.video.app.player.pingback;

import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class ClickPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"r", "block", "rt", "rseat", "rpage", "isprevue", "c1", "plid", "letter_exist", "c2", "count", "s1", "e", "rfr", "tabid", "tvsrchsource", "keyword", "now_c1", "now_qpid", "now_c2", "state", "adcount", "viprate", "now_ep", "copy", "flow", "allitem", "dftitem", "line", "hissrch", "s2", "videolist", "rec", "series", "star", "sawitem", "isad", Keys.S3, "qtcurl", "rlink", Keys.JUMP_TYPE, Keys.ISREAD, "isdftcard", "isdftitem"};

    public ClickPingback(String[] types) {
        this(types, ALLTYPES);
    }

    public ClickPingback(String[] types, String[] allTypes) {
        super(types, allTypes);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "20");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToLongYuan(keys);
    }
}
