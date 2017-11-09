package com.mcto.ads.internal.model;

import android.content.ContentValues;
import android.util.Log;
import com.mcto.ads.CupidAd;
import com.mcto.ads.constants.ClickArea;
import com.mcto.ads.constants.DeliverType;
import com.mcto.ads.constants.EventProperty;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.mcto.ads.internal.net.PingbackConstants;
import com.mcto.ads.internal.net.TrackingConstants;
import com.mcto.ads.internal.net.TrackingController;
import com.mcto.ads.internal.net.TrackingParty;
import com.mcto.ads.internal.persist.DBConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.Message;

public class AdInfo {
    private final int DEFAULT_AUTO_PLAY_TIME = 0;
    private final int DEFAULT_CLICK_PLAY_TIME = 5;
    private final int DEFAULT_SKIPPABLE_TIME = 5;
    private Map<String, Object> adExtras;
    private int adId;
    private List<String> adxAvailableEvents = new ArrayList();
    private Map<String, Object> adxTrackingParams = new HashMap();
    private String adxTrackingUrl;
    private int autoPlayTime;
    private int clickPlayTime;
    private String clickThroughType;
    private String clickThroughUrl;
    private long creativeId;
    private Map<String, Object> creativeObject;
    private String creativeType;
    private String creativeUrl;
    private List<String> cupidAvailableEvents = new ArrayList();
    private Map<String, Object> cupidTrackingParams = new HashMap();
    private String cupidTrackingUrl;
    private DeliverType deliverType;
    private long dspId;
    private int dspType;
    private int duration;
    private Map<String, Object> eventProperties = new HashMap();
    private String identifier;
    private int offsetInSlot;
    private int order;
    private long orderItemId;
    private int playCount;
    private int playType;
    private int progress;
    private int sendRecord;
    private int skippableTime;
    private SlotInfo slotInfo;
    private String templateType;
    private Map<String, List<String>> thirdPartyEventTrackings = new HashMap();
    private String timePosition;
    private int trueviewTime;

    public AdInfo(int adId, SlotInfo slotInfo, int offsetInSlot, JSONObject jsonAd) throws JSONException {
        this.adId = adId;
        this.slotInfo = slotInfo;
        this.offsetInSlot = offsetInSlot;
        this.duration = 0;
        this.progress = 0;
        this.trueviewTime = -1;
        this.skippableTime = 0;
        this.autoPlayTime = -1;
        this.clickPlayTime = -1;
        this.playCount = 0;
        this.sendRecord = 0;
        this.templateType = "";
        this.creativeType = "";
        this.identifier = "";
        this.playType = -1;
        this.deliverType = DeliverType.DELIVER_UNSUPPORTED;
        if (jsonAd.has("order")) {
            this.order = jsonAd.getInt("order");
        }
        if (jsonAd.has("orderItemId")) {
            this.orderItemId = jsonAd.getLong("orderItemId");
        }
        if (jsonAd.has("duration")) {
            this.duration = jsonAd.getInt("duration") * 1000;
        }
        if (jsonAd.has("clickThroughUrl")) {
            this.clickThroughUrl = jsonAd.getString("clickThroughUrl");
        }
        this.clickThroughType = jsonAd.optString("clickThroughType", "0");
        this.deliverType = DeliverType.build(jsonAd.optInt("deliverType"));
        if (jsonAd.has(JsonBundleConstants.CREATIVE_TYPE)) {
            this.creativeType = jsonAd.getString(JsonBundleConstants.CREATIVE_TYPE);
        }
        parseCreativeObject(jsonAd);
        if (jsonAd.has(JsonBundleConstants.CREATIVE_URL)) {
            this.creativeUrl = jsonAd.getString(JsonBundleConstants.CREATIVE_URL);
        }
        if (jsonAd.has(JsonBundleConstants.CREATIVE_ID)) {
            this.creativeId = jsonAd.getLong(JsonBundleConstants.CREATIVE_ID);
        }
        if (jsonAd.has(JsonBundleConstants.AD_EXTRAS)) {
            this.adExtras = CupidUtils.convertJson2Map(jsonAd.getJSONObject(JsonBundleConstants.AD_EXTRAS));
        }
        if (jsonAd.has(JsonBundleConstants.DSP_ID)) {
            this.dspId = jsonAd.getLong(JsonBundleConstants.DSP_ID);
        }
        if (jsonAd.has(JsonBundleConstants.DSP_TYPE)) {
            this.dspType = jsonAd.getInt(JsonBundleConstants.DSP_TYPE);
            Log.d("a71_ads_client", "AdInfo(): dspType:" + this.dspType);
        }
        if (jsonAd.has(JsonBundleConstants.TIME_POSITION)) {
            this.timePosition = jsonAd.getString(JsonBundleConstants.TIME_POSITION);
        }
        if (jsonAd.has(JsonBundleConstants.TEMPLATE_TYPE)) {
            this.templateType = jsonAd.getString(JsonBundleConstants.TEMPLATE_TYPE);
        }
        setTrueViewTime(jsonAd);
        setSkippableTime(jsonAd);
        parseBillingPointTime(jsonAd);
        parseTrackings(jsonAd);
    }

    public AdInfo(int adId, SlotInfo slotInfo, JSONObject emptyInfo) throws JSONException {
        this.adId = adId;
        this.slotInfo = slotInfo;
        this.templateType = "";
        this.creativeType = "";
        this.identifier = "";
        if (emptyInfo.has("w")) {
            this.timePosition = emptyInfo.getString("w");
        }
        if (emptyInfo.has("url")) {
            this.cupidTrackingUrl = emptyInfo.optString("url");
        }
        this.cupidAvailableEvents.add(TrackingConstants.mapToNumEvent("impression"));
        setTrackingParams(TrackingParty.CUPID, emptyInfo);
    }

    public int getAdId() {
        return this.adId;
    }

    private void parseCreativeObject(JSONObject jsonAd) throws JSONException {
        if (jsonAd.has(JsonBundleConstants.CREATIVE_OBJECT)) {
            this.creativeObject = CupidUtils.convertJson2Map(jsonAd.getJSONObject(JsonBundleConstants.CREATIVE_OBJECT));
            int tmpDuration = CupidUtils.stringToInt((String) this.creativeObject.get("duration"));
            if (tmpDuration > 0) {
                this.duration = tmpDuration * 1000;
            }
            if (this.creativeObject.containsKey("dynamicUrl")) {
                String dynamicUrl = (String) this.creativeObject.get("dynamicUrl");
                if (CupidUtils.isValidStr(dynamicUrl)) {
                    this.creativeObject.put(JsonBundleConstants.PORTRAIT_URL, dynamicUrl);
                    this.creativeObject.put("landScapeUrl", dynamicUrl);
                }
            }
        }
    }

    private void parseTrackings(JSONObject jsonAd) throws JSONException {
        parseThirdPartyTrackings(jsonAd);
        if (jsonAd.has(CupidUtils.strReverse("gnikcarTiyiqi"))) {
            parseA71Trackings(jsonAd.getJSONObject(CupidUtils.strReverse("gnikcarTiyiqi")));
        }
    }

    private void parseThirdPartyTrackings(JSONObject jsonAd) {
        try {
            if (jsonAd.has(JsonBundleConstants.IMPRESSION_TRACKING)) {
                this.thirdPartyEventTrackings.put("impression", parseThirdPartyTrackingsUtil(jsonAd, JsonBundleConstants.IMPRESSION_TRACKING));
            }
            if (jsonAd.has(JsonBundleConstants.CLICK_TRACKING)) {
                this.thirdPartyEventTrackings.put("click", parseThirdPartyTrackingsUtil(jsonAd, JsonBundleConstants.CLICK_TRACKING));
            }
            if (jsonAd.has(JsonBundleConstants.EVENT_TRACKINGS)) {
                JSONArray jsonArray = jsonAd.optJSONArray(JsonBundleConstants.EVENT_TRACKINGS);
                if (jsonArray != null) {
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject eventObj = jsonArray.getJSONObject(i);
                        JSONArray eventTrackings = eventObj.optJSONArray("tracking");
                        if (eventTrackings != null) {
                            List<String> newTrackings = new ArrayList();
                            for (int j = 0; j < eventTrackings.length(); j++) {
                                newTrackings.add(eventTrackings.getString(j));
                            }
                            String event = eventObj.optString("event");
                            List<String> existTrackings = (List) this.thirdPartyEventTrackings.get(event);
                            if (existTrackings == null) {
                                this.thirdPartyEventTrackings.put(event, newTrackings);
                            } else {
                                existTrackings.addAll(newTrackings);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("a71_ads_client", "parseThirdPartyTrackings(): exception: ", ex);
        }
    }

    private List<String> parseThirdPartyTrackingsUtil(JSONObject jsonAd, String event) throws JSONException {
        List<String> urls = new ArrayList();
        JSONObject obj = jsonAd.optJSONObject(event);
        if (obj != null) {
            JSONArray thirdPartyUrls = obj.optJSONArray(JsonBundleConstants.THIRD_TRACKING_URLS);
            if (thirdPartyUrls != null) {
                int length = thirdPartyUrls.length();
                for (int i = 0; i < length; i++) {
                    urls.add(thirdPartyUrls.getString(i));
                }
            }
        }
        return urls;
    }

    private void parseA71Trackings(JSONObject object) throws JSONException {
        JSONObject cupidObject = object.optJSONObject("cupidTracking");
        if (cupidObject != null) {
            if (cupidObject.has("url")) {
                this.cupidTrackingUrl = cupidObject.optString("url");
            }
            addTrackingEvents(TrackingParty.CUPID, cupidObject.optJSONArray(JsonBundleConstants.A71_TRACKING_EVENTS));
            setTrackingParams(TrackingParty.CUPID, cupidObject.optJSONObject(JsonBundleConstants.A71_TRACKING_PARAMS));
        }
        JSONObject adxObject = object.optJSONObject("adxTracking");
        if (adxObject != null) {
            if (adxObject.has("url")) {
                this.adxTrackingUrl = cupidObject.optString("url");
            }
            addTrackingEvents(TrackingParty.ADX, adxObject.optJSONArray(JsonBundleConstants.A71_TRACKING_EVENTS));
            setTrackingParams(TrackingParty.ADX, adxObject.optJSONObject(JsonBundleConstants.A71_TRACKING_PARAMS));
        }
    }

    public void addTrackingEvents(TrackingParty party, JSONArray trackingEvents) throws JSONException {
        if (trackingEvents != null) {
            int eventSize = trackingEvents.length();
            if (TrackingParty.CUPID == party) {
                for (int i = 0; i < eventSize; i++) {
                    this.cupidAvailableEvents.add(trackingEvents.getString(i));
                }
            } else if (TrackingParty.ADX == party) {
                for (int j = 0; j < eventSize; j++) {
                    this.adxAvailableEvents.add(trackingEvents.getString(j));
                }
            }
        }
    }

    public void setTrackingParams(TrackingParty party, JSONObject object) throws JSONException {
        Map<String, Object> params = CupidUtils.convertJson2Map(object);
        if (TrackingParty.CUPID == party) {
            this.cupidTrackingParams.putAll(params);
        } else if (TrackingParty.ADX == party) {
            this.adxTrackingParams.putAll(params);
        }
    }

    public boolean isEventCupidAvailable(String event) {
        return this.cupidAvailableEvents.contains(TrackingConstants.mapToNumEvent(event));
    }

    public boolean isEventAdxAvailable(String event) {
        return this.adxAvailableEvents.contains(TrackingConstants.mapToNumEvent(event));
    }

    private String getCupidConfigUrl(CupidContext cupidContext) {
        String rtn = TrackingConstants.CUPID_TRACKING_URL;
        if (this.cupidTrackingUrl != null) {
            rtn = this.cupidTrackingUrl;
        } else {
            String cupidConfigUrl = cupidContext.getCupidTrackingUrl();
            if (cupidConfigUrl != null) {
                rtn = cupidConfigUrl;
            }
        }
        if (rtn.contains("?")) {
            return rtn;
        }
        return rtn + "?";
    }

    private String getAdxConfigUrl(CupidContext cupidContext) {
        String rtn = TrackingConstants.ADX_TRACKING_URL;
        if (this.adxTrackingUrl != null) {
            rtn = this.adxTrackingUrl;
        } else {
            String adxConfigUrl = cupidContext.getAdxTrackingUrl();
            if (adxConfigUrl != null) {
                rtn = adxConfigUrl;
            }
        }
        if (rtn.contains("?")) {
            return rtn;
        }
        return rtn + "?";
    }

    public List<String> getThirdPartyTrackings(String event, CupidContext cupidContext, boolean needMacroSubstitution) {
        List<String> newUrls = new ArrayList();
        if (!this.thirdPartyEventTrackings.containsKey(event)) {
            return newUrls;
        }
        List<String> urls = (List) this.thirdPartyEventTrackings.get(event);
        if (!needMacroSubstitution) {
            return urls;
        }
        for (String url : urls) {
            String url2 = TrackingController.updateThirdPartyTrackingUrl(url2, cupidContext.getDebugTime()).replace(CupidUtils.strReverse("]TESFFO_IYIQI["), String.valueOf(this.progress / 1000));
            if (this.playCount >= 0) {
                url2 = url2.replace("CUPID_VPC", String.valueOf(this.playCount));
            }
            int playType = getPlayType();
            if (playType != -1) {
                url2 = url2.replace("CUPID_VPT", String.valueOf(playType));
            }
            String clickArea = getClickArea();
            if (CupidUtils.isValidStr(clickArea)) {
                url2 = url2.replace("CUPID_CLA", clickArea);
            }
            newUrls.add(url2);
        }
        return newUrls;
    }

    public List<String> getCupidTrackingUrls(String event, CupidGlobal cupidGlobal, CupidContext cupidContext) {
        List<String> newUrls = new ArrayList();
        if (isEventCupidAvailable(event)) {
            String url = "" + getCupidConfigUrl(cupidContext);
            this.cupidTrackingParams.put("a", TrackingConstants.mapToNumEvent(event));
            this.cupidTrackingParams.put(TrackingConstants.TRACKING_KEY_APP_VERSION, cupidGlobal.getAppVersion());
            this.cupidTrackingParams.put(TrackingConstants.TRACKING_KEY_SDK_VERSION, cupidGlobal.getSdkVersion());
            this.cupidTrackingParams.put("r", cupidContext.getVideoEventId());
            this.cupidTrackingParams.put("st", Integer.valueOf(getStartTime(cupidGlobal)));
            for (String key : this.cupidTrackingParams.keySet()) {
                url = url + key + SearchCriteria.EQ + this.cupidTrackingParams.get(key) + "&";
            }
            String newUrl = TrackingController.updateCupidTrackingUrl(url.substring(0, url.length() - 1), cupidContext.getDebugTime());
            newUrls.add(newUrl);
            Log.d("a71_ads_client", "getCupidTrackingUrls(): " + newUrl);
        }
        return newUrls;
    }

    public List<String> getAdxTrackingUrls(String event, CupidGlobal cupidGlobal, CupidContext cupidContext) {
        List<String> newUrls = new ArrayList();
        if (isEventAdxAvailable(event)) {
            String url = "" + getAdxConfigUrl(cupidContext);
            this.adxTrackingParams.put("a", TrackingConstants.mapToNumEvent(event));
            this.adxTrackingParams.put(TrackingConstants.TRACKING_KEY_APP_VERSION, cupidGlobal.getAppVersion());
            this.adxTrackingParams.put(TrackingConstants.TRACKING_KEY_SDK_VERSION, cupidGlobal.getSdkVersion());
            this.adxTrackingParams.put("r", cupidContext.getVideoEventId());
            for (String key : this.adxTrackingParams.keySet()) {
                url = url + key + SearchCriteria.EQ + this.adxTrackingParams.get(key) + "&";
            }
            String newUrl = TrackingController.updateAdxTrackingUrl(url.substring(0, url.length() - 1), cupidContext.getDebugTime());
            newUrls.add(newUrl);
            Log.d("a71_ads_client", "getAdxTrackingUrls(): " + newUrl);
        }
        return newUrls;
    }

    public int getOrder() {
        return this.order;
    }

    public long getOrderItemId() {
        return this.orderItemId;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getClickThroughUrl() {
        return this.clickThroughUrl;
    }

    public String getClickThroughType() {
        return this.clickThroughType;
    }

    public String getCreativeType() {
        return this.creativeType;
    }

    public Map<String, Object> getCreativeObject() {
        return this.creativeObject;
    }

    public String getCreativeUrl() {
        return this.creativeUrl;
    }

    public long getCreativeId() {
        return this.creativeId;
    }

    public long getDspId() {
        return this.dspId;
    }

    public int getDspType() {
        return this.dspType;
    }

    public Map<String, Object> getAdExtras() {
        return this.adExtras;
    }

    public void setProgress(int progress) {
        if (progress > 0 && progress <= this.duration) {
            this.progress = progress;
        }
    }

    public int getProgress() {
        return this.progress;
    }

    public String getTimePosition() {
        return this.timePosition;
    }

    public String getTemplateType() {
        return this.templateType;
    }

    public int getSlotType() {
        return this.slotInfo.getType();
    }

    public String getAdZoneId() {
        return this.slotInfo.getAdZoneId();
    }

    public int getPlayType() {
        int rtn = -1;
        if (this.playType >= 0) {
            return this.playType;
        }
        Object playType = this.eventProperties.get(EventProperty.EVENT_PROP_KEY_PLAY_TYPE.value());
        if (playType != null) {
            rtn = ((Integer) playType).intValue();
        }
        return rtn;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getPlayCount() {
        return this.playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void addPlayCount(int count) {
        this.playCount += count;
    }

    private String getClickArea() {
        String rtn = "";
        if (this.eventProperties == null) {
            return rtn;
        }
        Object clickArea = this.eventProperties.get(EventProperty.EVENT_PROP_KEY_CLICK_AREA.value());
        if (clickArea != null) {
            return ((ClickArea) clickArea).value();
        }
        return rtn;
    }

    public Map<String, Object> getEventProperties() {
        return this.eventProperties;
    }

    public void addEventProperties(Map<String, Object> properties) {
        if (properties != null) {
            this.eventProperties.putAll(properties);
        }
    }

    public String getIdentifier() {
        Log.d("a71_ads_client", "getIdentifier(): ad id: " + this.adId + ", identifier: " + this.identifier);
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getBillingPoint() {
        int playType = getPlayType();
        if (playType == 0) {
            return this.autoPlayTime;
        }
        if (playType == 1) {
            return this.clickPlayTime;
        }
        return this.trueviewTime;
    }

    public void setTrueViewTime(JSONObject jsonAd) throws JSONException {
        if (DeliverType.DELIVER_TRUEVIEW == this.deliverType) {
            this.trueviewTime = this.duration / 2;
            String trueviewTime = jsonAd.optString(JsonBundleConstants.AD_TRUEVIEW_TIME);
            if (!trueviewTime.equals("")) {
                try {
                    int time;
                    if (trueviewTime.contains("%")) {
                        time = Integer.parseInt(trueviewTime.replace("%", ""));
                        if (time >= 0 && time <= 100) {
                            this.trueviewTime = (this.duration * time) / 100;
                            return;
                        }
                        return;
                    }
                    time = Integer.parseInt(trueviewTime);
                    if (time >= 0 && time <= this.duration / 1000) {
                        this.trueviewTime = time * 1000;
                    }
                } catch (NumberFormatException e) {
                    this.trueviewTime = this.duration / 2;
                }
            }
        }
    }

    public DeliverType getDeliverType() {
        return this.deliverType;
    }

    public int getSkippableTime() {
        return this.skippableTime;
    }

    public void setSkippableTime(JSONObject jsonAd) throws JSONException {
        if (DeliverType.DELIVER_TRUEVIEW == this.deliverType) {
            int skipTime = jsonAd.optInt(JsonBundleConstants.AD_SKIPPABLE_TIME, 5);
            if (skipTime < 0 || skipTime > this.duration / 1000) {
                this.skippableTime = 5000;
            } else {
                this.skippableTime = skipTime * 1000;
            }
        }
    }

    public void parseBillingPointTime(JSONObject jsonAd) throws JSONException {
        if (jsonAd.has(JsonBundleConstants.NATIVE_AD_BILLING_POINT)) {
            JSONObject nativeAdBillingPoint = jsonAd.getJSONObject(JsonBundleConstants.NATIVE_AD_BILLING_POINT);
            if (nativeAdBillingPoint.has(JsonBundleConstants.AUTO_PLAY_TIME)) {
                this.autoPlayTime = nativeAdBillingPoint.optInt(JsonBundleConstants.AUTO_PLAY_TIME, 0) * 1000;
                if (this.autoPlayTime > this.duration || this.autoPlayTime < 0) {
                    this.autoPlayTime = 0;
                }
            }
            if (nativeAdBillingPoint.has(JsonBundleConstants.CLICK_PLAY_TIME)) {
                this.clickPlayTime = nativeAdBillingPoint.optInt(JsonBundleConstants.CLICK_PLAY_TIME, 5) * 1000;
                if (this.clickPlayTime > this.duration || this.clickPlayTime < 0) {
                    this.clickPlayTime = 5000;
                }
            }
            Log.d("a71_ads_client", "parseBillingPointTime: auto time:" + this.autoPlayTime + ", click time:" + this.clickPlayTime);
        }
    }

    public String getAdInfo() {
        return (this.slotInfo.getType() + "||" + this.dspId + "||" + this.orderItemId + "||" + this.creativeId + "||") + (!this.templateType.equals("") ? this.templateType : this.creativeType);
    }

    public String getAdStrategy() {
        String rtn = String.valueOf(this.slotInfo.getType()) + SOAP.DELIM + this.timePosition.replaceAll(",", "") + SOAP.DELIM;
        List<String> timePositions = new ArrayList();
        List<AdInfo> adInfoList = this.slotInfo.getPlayableAds();
        if (!(adInfoList == null || adInfoList.isEmpty())) {
            for (AdInfo ad : adInfoList) {
                timePositions.add(ad.getTimePosition().replaceAll(",", ""));
            }
        }
        return rtn + CupidUtils.join(timePositions, "|");
    }

    public int getSequenceId() {
        if (this.slotInfo != null) {
            return this.slotInfo.getSequenceId();
        }
        return 0;
    }

    public int getSendRecord() {
        return this.sendRecord;
    }

    public void setSendRecord(int sendRecord) {
        this.sendRecord = sendRecord;
    }

    public boolean isTrackingPingbackSent(int flag) {
        if ((this.sendRecord & flag) != 0) {
            return true;
        }
        return false;
    }

    public void setTrackingPingbackFlag(int flag) {
        this.sendRecord |= flag;
    }

    public void resetPingbackFlags() {
        this.sendRecord &= Message.MAXLENGTH;
    }

    public int getOffsetInSlot() {
        return this.offsetInSlot;
    }

    private int getStartTime(CupidGlobal cupidGlobal) {
        int slotType = this.slotInfo.getType();
        switch (slotType) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 4:
            case 10:
                return this.slotInfo.getStartTime() / 1000;
            case 6:
                return cupidGlobal.getVVProgress() / 1000;
            default:
                Log.e("a71_ads_client", "getStartTime(): unknown slot type: " + slotType);
                return 0;
        }
    }

    public boolean isOldInterstitials() {
        if (this.creativeType == null || !this.creativeType.equals(CupidAd.CREATIVE_TYPE_IMAGE_START_SCREEN) || this.templateType.equals(CupidAd.TEMPLATE_TYPE_GPHONE_INTERSTITIALS) || this.templateType.equals(CupidAd.TEMPLATE_TYPE_GPAD_INTERSTITIALS) || this.templateType.equals(CupidAd.TEMPLATE_TYPE_GTV_INTERSTITIALS)) {
            return false;
        }
        return true;
    }

    public String getCustomInfo(String actType) {
        String customInfo = "";
        if (actType.equals("skip")) {
            return "ofs:" + (this.progress / 1000);
        }
        if (actType.equals(PingbackConstants.ACT_AD_AREA_CLICK)) {
            if (this.eventProperties != null) {
                ClickArea clickAreaObj = this.eventProperties.get(EventProperty.EVENT_PROP_KEY_CLICK_AREA.value());
                if (clickAreaObj != null) {
                    customInfo = clickAreaObj.value();
                }
            }
            return customInfo;
        } else if (!this.templateType.equals("native_video")) {
            return customInfo;
        } else {
            customInfo = "pt:" + getPlayType() + ";pc:" + getPlayCount();
            if (!actType.equals(PingbackConstants.ACT_AD_PLAY_DURATION)) {
                return customInfo;
            }
            if (this.eventProperties != null) {
                Object playDurationObj = this.eventProperties.get(EventProperty.EVENT_PROP_KEY_PLAY_DURATION.value());
                if (playDurationObj != null) {
                    int playDuration = CupidUtils.objectToInt(playDurationObj);
                    if (-1 != playDuration) {
                        customInfo = customInfo + ";pd:" + (playDuration / 1000);
                    }
                }
            }
            return customInfo + ";pg:" + (this.progress / 1000);
        }
    }

    public ContentValues getNativeVideoItem() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.DB_KEY_IDENTIFIER, this.identifier);
        contentValues.put(DBConstants.DB_KEY_PLAY_TYPE, Integer.valueOf(getPlayType()));
        contentValues.put(DBConstants.DB_KEY_PLAY_COUNT, Integer.valueOf(this.playCount));
        contentValues.put(DBConstants.DB_KEY_SEND_RECORD, Integer.valueOf(this.sendRecord));
        contentValues.put(DBConstants.DB_KEY_LAST_UPDATE_TIME, Integer.valueOf((int) (new Date().getTime() / 1000)));
        return contentValues;
    }

    public void updateNativeVideoItem(Map<String, Object> item) {
        if (item != null && item.size() > 0) {
            Integer playType = (Integer) item.get(DBConstants.DB_KEY_PLAY_TYPE);
            if (playType != null) {
                setPlayType(playType.intValue());
            }
            Integer playCount = (Integer) item.get(DBConstants.DB_KEY_PLAY_COUNT);
            if (playCount != null) {
                Log.d("a71_ads_client", "updateNativeVideoItem: play count:" + playCount);
                setPlayCount(playCount.intValue());
            }
            Integer sendRecord = (Integer) item.get(DBConstants.DB_KEY_SEND_RECORD);
            if (sendRecord != null) {
                setSendRecord(sendRecord.intValue());
            }
        }
    }
}
