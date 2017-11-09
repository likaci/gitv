package com.gala.video.app.epg.ui.search.utils;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class SearchPingbackUtils {
    public static void error(ApiException exception, String apiname) {
        String s1 = "";
        String r = "";
        String ec = "315008";
        String pfec = exception != null ? exception.getCode() : "";
        String errurl = exception != null ? exception.getUrl() : "";
        String e = PingBackUtils.createEventId();
        String playmode = "";
        String isWindow = "";
        String vvfrom = "";
        String ichannel_name = "";
        String news_type = "";
        String plid = "";
        String s2 = "";
        String qy_prv = "";
        String player = "";
        String c1 = "";
        String ra = "";
        String errdetail = "";
        String sdkv = "";
        String erreason = "";
        String activity = "";
        String excptnnm = "";
        String crashtype = "";
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "0").add("ec", ec).add("pfec", pfec).add(Keys.ERRURL, errurl).add("e", e);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
