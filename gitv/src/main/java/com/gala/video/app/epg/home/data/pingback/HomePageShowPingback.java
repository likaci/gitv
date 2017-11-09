package com.gala.video.app.epg.home.data.pingback;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.HashMap;

public final class HomePageShowPingback extends HomePingback {
    private ShowPingback mHomePingbackType;

    public HomePageShowPingback(ShowPingback homePingbackType) {
        this.mHomePingbackType = homePingbackType;
    }

    protected final void addDefaultField(HashMap<String, String> map) {
        map.put(Keys.T, "21");
        map.put("bstp", "1");
    }

    public String getType() {
        return this.mHomePingbackType.getValue();
    }
}
