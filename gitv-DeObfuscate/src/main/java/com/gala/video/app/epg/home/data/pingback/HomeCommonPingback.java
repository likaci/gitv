package com.gala.video.app.epg.home.data.pingback;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;

public final class HomeCommonPingback extends HomePingback {
    private CommonPingback mHomePingbackType;

    public HomeCommonPingback(CommonPingback homePingbackType) {
        this.mHomePingbackType = homePingbackType;
    }

    public String getType() {
        return this.mHomePingbackType.getValue();
    }
}
