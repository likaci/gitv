package com.gala.tvapi.vrs;

import com.gala.tvapi.b.a;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.b.b;
import com.gala.tvapi.vrs.core.IPushVideoServer;
import com.gala.tvapi.vrs.result.ApiResultAuthVipVideo;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;

public class PushVideoHelper extends BaseHelper {
    public static final IPushVideoServer<ApiResultAuthVipVideo> authVipPushVideo = a.a(new b(com.gala.tvapi.vrs.core.a.x), new k(), ApiResultAuthVipVideo.class, "authVipPushVideo");
    public static final IPushVideoServer<ApiResultKeepaliveInterval> checkVipAccountPushVideo = a.a(a.a(com.gala.tvapi.vrs.core.a.aQ), new com.gala.tvapi.vrs.a.a(), ApiResultKeepaliveInterval.class, "checkVipAccountPushVideo");
    public static final IPushVideoServer<ApiResultKeepaliveInterval> keepAlivePushVideo = a.a(a.a(com.gala.tvapi.vrs.core.a.aP), new k(), ApiResultKeepaliveInterval.class, "keepAlivePushVideo");
}
