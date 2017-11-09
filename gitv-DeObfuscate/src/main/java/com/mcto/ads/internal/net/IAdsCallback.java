package com.mcto.ads.internal.net;

import java.util.Map;

public interface IAdsCallback {
    void addTrackingEventCallback(int i, TrackingParty trackingParty, String str, Map<String, String> map);
}
