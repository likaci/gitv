package com.gala.video.lib.framework.coreservice.netdiagnose.traceroute;

import java.util.List;

public interface TracerouteStateListener {
    void onTraceFailed(String str, String str2);

    void onTraceSuccess(String str, String str2, List<TracerouteContainer> list);
}
