package com.gala.tvapi.type;

import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.push.pushservice.constants.PushConstants;
import com.tvos.appdetailpage.client.Constants;

public enum DailyInfoType {
    RECOMMEND("-2"),
    FINANCE(PushConstants.PLATFORM_TYPE),
    ENTERTAINMENT("7"),
    NEW(Values.value25),
    ORIGINAL("27"),
    FUNNY(Constants.PINGBACK_4_0_P_PHONE_APP);
    
    private String f1126a;

    private DailyInfoType(String v) {
        this.f1126a = v;
    }

    public final String toString() {
        return this.f1126a;
    }
}
