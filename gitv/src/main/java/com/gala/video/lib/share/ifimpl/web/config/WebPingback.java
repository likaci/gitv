package com.gala.video.lib.share.ifimpl.web.config;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class WebPingback {
    private static final String LOG_TAG = "EPG/WebPingbackUtils";

    public void error(Exception exception, String apiname) {
        String pfec = "";
        String errurl = "";
        if (exception == null || !(exception instanceof ApiException)) {
            LogUtils.e(LOG_TAG, "error --- exception is null or is not ApiException");
        } else {
            pfec = ((ApiException) exception).getCode();
            errurl = ((ApiException) exception).getUrl();
        }
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "0").add("ec", "315008").add("pfec", pfec).add(Keys.ERRURL, errurl).add("e", PingBackUtils.createEventId());
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
