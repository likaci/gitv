package com.gala.video.app.player.pingback.detail;

import com.gala.pingback.PingbackStore.CONTENT_TYPE;
import com.gala.video.app.player.pingback.Pingback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.Map;

public class CustomerDetailLoadedPingback extends Pingback {
    private static final String[] ALLTYPES = new String[]{"ct", "td", CONTENT_TYPE.KEY, "hcdn", "e"};
    private static final String[] TYPES = new String[]{"ct", "td", CONTENT_TYPE.KEY, "hcdn", "e"};

    public CustomerDetailLoadedPingback() {
        super(TYPES, ALLTYPES);
    }

    public void doSend(String[] arrays) {
    }

    public void doSend(Map<String, String> map) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11");
        Map<String, String> paramsMap = params.build();
        paramsMap.putAll(map);
        PingBack.getInstance().postPingBackToLongYuan(paramsMap);
    }
}
