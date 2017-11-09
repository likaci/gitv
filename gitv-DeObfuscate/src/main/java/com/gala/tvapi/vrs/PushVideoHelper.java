package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.IPushVideoServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.p031a.C0337a;
import com.gala.tvapi.vrs.p032b.C0357b;
import com.gala.tvapi.vrs.result.ApiResultAuthVipVideo;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;

public class PushVideoHelper extends BaseHelper {
    public static final IPushVideoServer<ApiResultAuthVipVideo> authVipPushVideo = C0214a.m583a(new C0357b(C0365a.f1257x), new C0336k(), ApiResultAuthVipVideo.class, "authVipPushVideo");
    public static final IPushVideoServer<ApiResultKeepaliveInterval> checkVipAccountPushVideo = C0214a.m583a(C0214a.m580a(C0365a.aQ), new C0337a(), ApiResultKeepaliveInterval.class, "checkVipAccountPushVideo");
    public static final IPushVideoServer<ApiResultKeepaliveInterval> keepAlivePushVideo = C0214a.m583a(C0214a.m580a(C0365a.aP), new C0336k(), ApiResultKeepaliveInterval.class, "keepAlivePushVideo");
}
