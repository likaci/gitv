package com.gala.video.app.player.pingback;

import com.gala.pingback.PingbackStore.IS1080P;
import com.gala.pingback.PingbackStore.IS4K;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class ShowPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"bstp", "c1", "qtcurl", "qpid", "rfr", "showbuyvip", "e", "td", "block", "plid", "c2", "qy_prv", "r", "s2", "tabid", "rlink", "tvsrchsource", "card", "tvlogin", "s1", "viprate", "allitem", "dftitem", "line", "rseat", "flow", "isQR", "count", "videolist", "rec", "series", "star", "tabsrc", "iscontent", "sawitem", "adcount", IS4K.KEY, IS1080P.KEY, Keys.JUMP_TYPE, Keys.ISREAD, "now_c1", Keys.ISACT};

    public ShowPingback(String[] types) {
        this(types, ALLTYPES);
    }

    public ShowPingback(String[] types, String[] allTypes) {
        super(types, allTypes);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "21");
        Map<String, String> keys = params.build();
        keys.putAll(map);
        PingBack.getInstance().postPingBackToLongYuan(keys);
    }
}
