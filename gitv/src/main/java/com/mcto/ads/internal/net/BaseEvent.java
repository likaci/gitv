package com.mcto.ads.internal.net;

import java.util.Map;

class BaseEvent {
    public String adInfo;
    public String adStrategy;
    public String appVersion;
    public long creativeId = -1;
    public String cupidUserId;
    public String customInfo;
    public long dspId = -1;
    public String errCode;
    public String errMsg;
    public Map<String, String> inventory;
    public int isOffline = 0;
    public String mobileKey;
    public String network;
    public long orderItemId = -1;
    public Map<String, Object> params;
    public Map<String, String> pingbackExtras;
    public String playerId;
    public int reqCount = -1;
    public int reqDuration = 0;
    public String requestId;
    public String sdkVersion;
    public int sequenceId = 0;
    public String subType;
    public String templateType;
    public String tvId;
    public String type;
    public String uaaUserId;
    public String videoEventId;
}
