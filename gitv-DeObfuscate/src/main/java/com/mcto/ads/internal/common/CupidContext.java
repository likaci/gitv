package com.mcto.ads.internal.common;

import android.content.Context;
import android.util.Log;
import com.mcto.ads.CupidAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CupidContext {
    private String adZoneId;
    private String adxTrackingUrl;
    private String cupidTrackingUrl;
    private long debugTime = 0;
    private boolean enablePB2 = true;
    private boolean fromCache = false;
    private Map<String, String> inventory = new HashMap();
    private boolean isTargeted = false;
    private String network = null;
    private Map<String, Map<String, Object>> pingbackConfig = new HashMap();
    private Map<String, String> pingbackExtras = new HashMap();
    private String playerId = null;
    private List<String> reqTemplateTypes = new ArrayList();
    private String requestId = null;
    private Context systemContext = null;
    private String timeSlice;
    private int trackingTimeout = 10000;
    private String tvId = null;
    private String videoEventId = null;

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getTvId() {
        return this.tvId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setVideoEventId(String videoEventId) {
        this.videoEventId = videoEventId;
    }

    public String getVideoEventId() {
        return this.videoEventId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getNetwork() {
        return this.network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public long getDebugTime() {
        return this.debugTime;
    }

    public void setDebugTime(long debugTime) {
        this.debugTime = debugTime;
    }

    public void setPingbackExtras(Map<String, Object> pingbackExtras) {
        for (String key : pingbackExtras.keySet()) {
            this.pingbackExtras.put(key, String.valueOf(pingbackExtras.get(key)));
        }
    }

    public Map<String, String> getPingbackExtras() {
        return this.pingbackExtras;
    }

    public void setInventory(Map<String, Object> inventory) {
        for (String key : inventory.keySet()) {
            this.inventory.put(key, String.valueOf(inventory.get(key)));
        }
    }

    public Map<String, String> getInventory() {
        return this.inventory;
    }

    public boolean enablePB2() {
        return this.enablePB2;
    }

    public void setEnablePB2(boolean enablePB2) {
        this.enablePB2 = enablePB2;
    }

    public boolean isFromCache() {
        return this.fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    public boolean isTargeted() {
        return this.isTargeted;
    }

    public void setIsTargeted(boolean isTargeted) {
        this.isTargeted = isTargeted;
    }

    public List<String> getReqTemplateTypes() {
        return this.reqTemplateTypes;
    }

    public void setReqTemplateTypes(List<String> reqTemplateTypes) {
        this.reqTemplateTypes.addAll(reqTemplateTypes);
    }

    public int getTrackingTimeout() {
        return this.trackingTimeout;
    }

    public void setTrackingTimeout(int trackingTimeout) {
        this.trackingTimeout = trackingTimeout;
    }

    public Map<String, Map<String, Object>> getPingbackConfig() {
        return this.pingbackConfig;
    }

    public void setPingbackConfig(Map<String, Map<String, Object>> pingbackConfig) {
        this.pingbackConfig.putAll(pingbackConfig);
    }

    public Map<String, Object> getThirdPingbackParams(String trackingUrl) {
        Map<String, Object> params = new HashMap();
        for (String trackingKey : this.pingbackConfig.keySet()) {
            if (trackingUrl.startsWith(trackingKey)) {
                return (Map) this.pingbackConfig.get(trackingKey);
            }
        }
        return params;
    }

    public String getAdZoneId() {
        return this.adZoneId;
    }

    public void setAdZoneId(String adZoneId) {
        this.adZoneId = adZoneId;
    }

    public String getTimeSlice() {
        return this.timeSlice;
    }

    public void setTimeSlice(String timeSlice) {
        this.timeSlice = timeSlice;
    }

    public String getCupidTrackingUrl() {
        return this.cupidTrackingUrl;
    }

    public void setCupidTrackingUrl(String cupidTrackingUrl) {
        this.cupidTrackingUrl = cupidTrackingUrl;
    }

    public String getAdxTrackingUrl() {
        return this.adxTrackingUrl;
    }

    public void setAdxTrackingUrl(String adxTrackingUrl) {
        this.adxTrackingUrl = adxTrackingUrl;
    }

    public Context getSystemContext() {
        return this.systemContext;
    }

    public void setSystemContext(Context systemContext) {
        this.systemContext = systemContext;
    }

    public boolean relyOnCardShow() {
        if (this.reqTemplateTypes == null) {
            return false;
        }
        if (this.reqTemplateTypes.contains("mobile_flow") || this.reqTemplateTypes.contains("mobile_flow_pair")) {
            return true;
        }
        if (this.reqTemplateTypes.contains("native_video") || this.reqTemplateTypes.contains(CupidAd.TEMPLATE_TYPE_NATIVE_MULTI_IMAGE) || this.reqTemplateTypes.contains(CupidAd.TEMPLATE_TYPIE_NATIVE_IMAGE)) {
            return true;
        }
        if (this.reqTemplateTypes.contains(CupidAd.TEMPLATE_TYPE_HEADLINE_NATIVE_IMAGE)) {
            return true;
        }
        Log.e("a71_ads_client", "relyOnCardShow(): some templates has not been processed.");
        return false;
    }

    public boolean hasMobileInterstitial() {
        if (this.reqTemplateTypes != null) {
            return this.reqTemplateTypes.contains("mobile_interstitials");
        }
        return false;
    }
}
