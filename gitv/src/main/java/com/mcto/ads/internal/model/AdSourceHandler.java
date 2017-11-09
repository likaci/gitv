package com.mcto.ads.internal.model;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;
import android.util.Log;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.tvapi.tv2.constants.ChannelId;
import com.gala.video.app.epg.ui.search.ad.Keys;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.mcto.ads.internal.common.AdsClientConstants;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.mcto.ads.internal.net.PingbackConstants;
import com.mcto.ads.internal.net.TrackingConstants;
import com.tvos.apps.utils.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdSourceHandler {
    private final int MAX_PLAY_COUNT = ChannelId.CHANNEL_ID_VIP_NEW;
    private final String PARSE_ADSOURCE_ERROR = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
    private Context context;
    private String cupidUserId;
    private String mobileKey;
    private String playerId;
    private String uaaUserId;
    private String userAgent;

    private class ItemSetter implements Runnable {
        private Context ctx;
        private String key;
        private Integer val;

        public ItemSetter(String key, Integer val, Context ctx) {
            this.key = key;
            this.val = val;
            this.ctx = ctx;
        }

        public void run() {
            Editor editor = this.ctx.getSharedPreferences("a71_ads_client", 0).edit();
            if (this.val.intValue() > ChannelId.CHANNEL_ID_VIP_NEW) {
                this.val = Integer.valueOf(0);
            }
            editor.putInt(this.key, this.val.intValue());
            editor.commit();
        }
    }

    public AdSourceHandler(CupidGlobal cupidGlobal, String playerId, Context context) {
        this.uaaUserId = cupidGlobal.getUaaUserId();
        this.cupidUserId = cupidGlobal.getCupidUserId();
        this.playerId = playerId;
        this.context = context;
    }

    public static List<Map<String, String>> getAdCreativesByAdSource(String adSource) {
        List<Map<String, String>> creatives = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(adSource).optJSONObject("data");
            if (jsonObject != null) {
                Iterator<?> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    JSONArray ads = jsonObject.optJSONObject(iterator.next().toString()).optJSONArray("ads");
                    if (ads != null) {
                        for (int i = 0; i < ads.length(); i++) {
                            JSONObject ad = ads.getJSONObject(i);
                            Map<String, String> creative = new HashMap();
                            String dynamicUrl = ad.optString("dynamicUrl");
                            if (CupidUtils.isValidStr(dynamicUrl)) {
                                creative.put("landScapeUrl", dynamicUrl);
                                creative.put(JsonBundleConstants.PORTRAIT_URL, dynamicUrl);
                            } else {
                                creative.put("landScapeUrl", ad.optString("landScapeUrl"));
                                creative.put(JsonBundleConstants.PORTRAIT_URL, ad.optString(JsonBundleConstants.PORTRAIT_URL));
                            }
                            String renderType = ad.optJSONObject(JsonBundleConstants.CREATIVE_OBJECT).optString(AdsConstants.AD_SCREEN_RENDER_TYPE);
                            if (CupidUtils.isValidStr(renderType)) {
                                creative.put(AdsConstants.AD_SCREEN_RENDER_TYPE, renderType);
                            }
                            creatives.add(creative);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("a71_ads_client", "getAdCreativesByAdSource(): json error, adSource:" + adSource, e);
        }
        return creatives;
    }

    public String getAdDataWithAdSource(String adSource, long offsetInSecond, String mobileKey, String userAgent) {
        this.mobileKey = mobileKey;
        this.userAgent = userAgent;
        String targetAdInfo = "";
        try {
            JSONObject adSourceJSON = new JSONObject(adSource);
            Integer code = Integer.valueOf(adSourceJSON.optInt(PingbackConstants.CODE));
            if (code.intValue() != 0) {
                Log.e("a71_ads_client", "adSource error,code = " + code);
                return MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
            }
            Integer adZoneRotation = Integer.valueOf(adSourceJSON.optInt("rotation"));
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(now.getTimeInMillis() + (1000 * offsetInSecond));
            String targetDate = DateFormat.format(DateUtil.PATTERN_STANDARD10H, now).toString();
            JSONObject allData = adSourceJSON.optJSONObject("data");
            if (!(allData == null || adZoneRotation.intValue() == 0)) {
                JSONObject targetData = allData.optJSONObject(targetDate);
                if (targetData != null) {
                    JSONArray ads = targetData.optJSONArray("ads");
                    if (ads != null) {
                        JSONObject ad = getAvailableAd(ads, adZoneRotation.intValue());
                        if (ad != null) {
                            targetAdInfo = generateAdInfo(ad, adSourceJSON, (now.getTimeInMillis() / 1000) + "");
                        }
                    }
                }
            }
            Log.i("a71_ads_client", "generateAdInfo finish,targetAdInfo = " + targetAdInfo);
            return targetAdInfo;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "getAdDataWithAdSource json error,adSource = " + adSource, e);
            targetAdInfo = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        } catch (NullPointerException ex) {
            Log.e("a71_ads_client", "getAdDataWithAdSource json error,adSource = " + adSource, ex);
            targetAdInfo = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        }
    }

    private String generateAdInfo(JSONObject targetAd, JSONObject adSource, String timestamp) throws JSONException {
        String template = adSource.optString("template");
        if (!CupidUtils.isValidStr(template)) {
            return "";
        }
        int i;
        String impressionId = generateImpressionId(timestamp + "");
        template = template.replace("#STR_AD_ZONE_ID#", adSource.optString("adZoneId")).replace("#STR_ORDER_ITEM_ID#", targetAd.optString("orderItemId")).replace("#STR_AD_ID#", targetAd.optString("adId")).replace("#STR_CREATIVE_ID#", targetAd.optString(JsonBundleConstants.CREATIVE_ID)).replace("#STR_CLICK_TITLE#", targetAd.optString("clickTitle")).replace("#STR_CLICK_DESCRIPTION#", targetAd.optString("clickDescription")).replace("#STR_IS_SKIPPABLE#", targetAd.optString(AdsConstants.AD_VIDEO_SKIPPABLE)).replace("#STR_LAND_SCAPE_URL#", targetAd.optString("landScapeUrl")).replace("#STR_PORTRAIT_URL#", targetAd.optString(JsonBundleConstants.PORTRAIT_URL));
        String clickThroughUrl = targetAd.optString("clickThroughUrl");
        if (!(clickThroughUrl == null || this.uaaUserId == null)) {
            clickThroughUrl = clickThroughUrl.replace("[UDID]", this.uaaUserId);
        }
        template = template.replace("#STR_CLICK_THROUGH_URL#", clickThroughUrl).replace("#STR_CLICK_THROUGH_TYPE#", targetAd.optString("clickThroughType")).replace("#STR_IMPRESSION_ID#", impressionId).replace("#STR_CUPID_IMP_TRACKING#", "").replace("#STR_CUPID_CLICK_TRACKING#", "");
        JSONArray thirdPartyClickTrackings = targetAd.optJSONArray("thirdPartyClickTracking");
        List<String> thirdPartyClickTrackingList = new ArrayList();
        if (thirdPartyClickTrackings != null) {
            for (i = 0; i < thirdPartyClickTrackings.length(); i++) {
                thirdPartyClickTrackingList.add(thirdPartyClickTrackings.get(i).toString());
            }
        }
        JSONArray thirdPartyImpressionTrackings = targetAd.optJSONArray("thirdPartyImpressionTracking");
        List<String> thirdPartyImpressionTrackingList = new ArrayList();
        if (thirdPartyImpressionTrackings != null) {
            for (i = 0; i < thirdPartyImpressionTrackings.length(); i++) {
                thirdPartyImpressionTrackingList.add(thirdPartyImpressionTrackings.get(i).toString());
            }
        }
        return assembleAdInfo(template.replace("#ARRAY_TRD_IMP_TRACKING#", generateThirdPartyTrackings(thirdPartyImpressionTrackingList, timestamp)).replace("#ARRAY_TRD_CLICK_TRACKING#", generateThirdPartyTrackings(thirdPartyClickTrackingList, timestamp)), targetAd, adSource, timestamp, impressionId);
    }

    private String assembleAdInfo(String template, JSONObject adSrcNode, JSONObject adSource, String timestamp, String impressionId) throws JSONException {
        JSONObject jSONObject = new JSONObject(template);
        JSONObject a71TrackingObject = generateA71Trackings(adSrcNode, adSource, timestamp, impressionId);
        JSONArray adSlotsObject = jSONObject.optJSONArray(JsonBundleConstants.AD_SLOTS);
        if (adSlotsObject != null && adSlotsObject.length() > 0) {
            JSONArray adsObject = adSlotsObject.getJSONObject(0).optJSONArray("ads");
            if (adsObject != null && adsObject.length() > 0) {
                JSONObject adObject = adsObject.getJSONObject(0);
                adObject.put(CupidUtils.strReverse("gnikcarTiyiqi"), a71TrackingObject);
                JSONObject dstCreativeObj = adObject.getJSONObject(JsonBundleConstants.CREATIVE_OBJECT);
                if (adSrcNode.has(JsonBundleConstants.CREATIVE_OBJECT)) {
                    JSONObject srcCreativeObj = adSrcNode.getJSONObject(JsonBundleConstants.CREATIVE_OBJECT);
                    String needAdBadge = srcCreativeObj.optString("needAdBadge");
                    if (CupidUtils.isValidStr(needAdBadge)) {
                        dstCreativeObj.put("needAdBadge", needAdBadge);
                    } else {
                        dstCreativeObj.put("needAdBadge", "true");
                    }
                    String duration = srcCreativeObj.optString("duration");
                    if (CupidUtils.isValidStr(duration)) {
                        dstCreativeObj.put("duration", duration);
                    }
                    String renderType = srcCreativeObj.optString(AdsConstants.AD_SCREEN_RENDER_TYPE);
                    if (CupidUtils.isValidStr(renderType)) {
                        dstCreativeObj.put(AdsConstants.AD_SCREEN_RENDER_TYPE, renderType);
                    }
                }
                String dynamicUrl = adSrcNode.optString("dynamicUrl");
                if (CupidUtils.isValidStr(dynamicUrl)) {
                    dstCreativeObj.put("landScapeUrl", dynamicUrl);
                    dstCreativeObj.put(JsonBundleConstants.PORTRAIT_URL, dynamicUrl);
                }
            }
        }
        jSONObject.getJSONObject(JsonBundleConstants.CUPID_EXTRAS).put(JsonBundleConstants.SERVER_TIME, Long.parseLong(timestamp) * 1000);
        return jSONObject.toString();
    }

    private String generateThirdPartyTrackings(List<String> thirdPartyTrackings, String timestamp) {
        List<String> thirdPartyTrackingUrls = new ArrayList();
        for (String thirdPartyTrackingUrl : thirdPartyTrackings) {
            String thirdPartyTrackingUrl2;
            if (this.uaaUserId != null) {
                thirdPartyTrackingUrl2 = thirdPartyTrackingUrl2.replaceAll("\\[M?_?IDFA\\]", this.uaaUserId);
            }
            thirdPartyTrackingUrls.add(thirdPartyTrackingUrl2.replace("\"", "\\\""));
        }
        return "\"" + CupidUtils.join(thirdPartyTrackingUrls, "\",\"") + "\"";
    }

    private JSONObject generateTrackingParams(JSONObject adSrcNode, JSONObject adSource, String timestamp, String impressionId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(TrackingConstants.TRACKING_KEY_TIMESTAMP, timestamp);
        obj.put("c", impressionId);
        obj.put("d", adSrcNode.optString("orderItemId"));
        obj.put("f", this.uaaUserId);
        obj.put(Keys.G, this.uaaUserId);
        obj.put(CuteConstants.H, this.cupidUserId);
        obj.put("i", this.playerId);
        obj.put("l", this.mobileKey);
        obj.put("m", this.userAgent);
        obj.put("o", 0);
        obj.put("p", adSource.optString("adZoneId"));
        obj.put("q", adSrcNode.optString(JsonBundleConstants.CREATIVE_ID));
        obj.put("s", generateSecurityWithImpressionId(impressionId, adSrcNode.optString("orderItemId"), adSrcNode.optString(JsonBundleConstants.CREATIVE_ID), this.mobileKey));
        obj.put(PingBackParams.Keys.T, adSource.optString("clientType"));
        obj.put("u", adSrcNode.optString("roleInAdtemplate"));
        obj.put("v", adSrcNode.optString("chargingPrice"));
        return obj;
    }

    private JSONObject generateA71Trackings(JSONObject adSrcNode, JSONObject adSource, String timestamp, String impressionId) throws JSONException {
        JSONObject a71Obj = new JSONObject();
        JSONArray events = new JSONArray();
        events.put("0");
        events.put("1");
        JSONObject params = generateTrackingParams(adSrcNode, adSource, timestamp, impressionId);
        JSONObject cupidObj = new JSONObject();
        cupidObj.put(JsonBundleConstants.A71_TRACKING_EVENTS, events);
        cupidObj.put(JsonBundleConstants.A71_TRACKING_PARAMS, params);
        a71Obj.put("cupidTracking", cupidObj);
        return a71Obj;
    }

    private String generateImpressionId(String timestamp) {
        return CupidUtils.md5(timestamp + this.cupidUserId);
    }

    private String generateSecurityWithImpressionId(String impressionId, String orderItemId, String creativeId, String mobileKey) {
        return CupidUtils.md5(impressionId + orderItemId + creativeId + mobileKey + "cupid");
    }

    private JSONObject getAvailableAd(JSONArray ads, int adZoneRotation) throws JSONException {
        if (ads == null) {
            return null;
        }
        Integer playCount = getPlayCount(adZoneRotation);
        Integer currentRotation = Integer.valueOf(playCount.intValue() % adZoneRotation);
        Integer rotationCount = Integer.valueOf(0);
        for (int i = 0; i < ads.length(); i++) {
            JSONObject ad = ads.getJSONObject(i);
            Integer adRotation = Integer.valueOf(ad.optInt("rotation"));
            if (adRotation != null) {
                rotationCount = Integer.valueOf(rotationCount.intValue() + adRotation.intValue());
                if (rotationCount.intValue() > currentRotation.intValue()) {
                    setItem(AdsClientConstants.INITIALIZATION_COUNT_KEY, Integer.valueOf(playCount.intValue() + 1));
                    return ad;
                }
            }
        }
        setItem(AdsClientConstants.INITIALIZATION_COUNT_KEY, Integer.valueOf(playCount.intValue() + 1));
        return null;
    }

    private Integer getPlayCount(int adZoneRotation) {
        return getItem(AdsClientConstants.INITIALIZATION_COUNT_KEY, generateDefaultPlayCount(adZoneRotation).intValue());
    }

    private Integer generateDefaultPlayCount(int adZoneRotation) {
        return Integer.valueOf(Double.valueOf(Math.random() * ((double) adZoneRotation)).intValue());
    }

    private void setItem(String key, Integer value) {
        new Thread(new ItemSetter(key, value, this.context)).start();
    }

    private Integer getItem(String key, int defValue) {
        return Integer.valueOf(this.context.getSharedPreferences("a71_ads_client", 0).getInt(key, defValue));
    }

    public static List<Map<String, String>> getAdCreativesByServerResponse(Context context, String response) {
        List<Map<String, String>> creatives = new ArrayList();
        try {
            JSONObject mixerObj = new JSONObject(response);
            if (mixerObj != null) {
                parseDebugTime(context, mixerObj);
                JSONArray adSlotsObj = mixerObj.getJSONArray(JsonBundleConstants.AD_SLOTS);
                if (adSlotsObj != null) {
                    int slotSize = adSlotsObj.length();
                    for (int i = 0; i < slotSize; i++) {
                        JSONObject slotObj = adSlotsObj.getJSONObject(i);
                        if (slotObj != null) {
                            JSONArray adsObj = slotObj.getJSONArray("ads");
                            if (adSlotsObj != null) {
                                int adSize = adsObj.length();
                                for (int j = 0; j < adSize; j++) {
                                    JSONObject adObj = adsObj.getJSONObject(j);
                                    if (adObj != null) {
                                        JSONObject creativeObj = adObj.getJSONObject(JsonBundleConstants.CREATIVE_OBJECT);
                                        if (creativeObj != null) {
                                            Map<String, String> creative = new HashMap();
                                            creative.put(AdsConstants.AD_SCREEN_RENDER_TYPE, creativeObj.optString(AdsConstants.AD_SCREEN_RENDER_TYPE));
                                            String dynamicUrl = creativeObj.optString("dynamicUrl");
                                            if (CupidUtils.isValidStr(dynamicUrl)) {
                                                creative.put("landScapeUrl", dynamicUrl);
                                                creative.put(JsonBundleConstants.PORTRAIT_URL, dynamicUrl);
                                            } else {
                                                creative.put(JsonBundleConstants.PORTRAIT_URL, creativeObj.optString(JsonBundleConstants.PORTRAIT_URL));
                                                creative.put("landScapeUrl", creativeObj.optString("landScapeUrl"));
                                            }
                                            creatives.add(creative);
                                        } else {
                                            continue;
                                        }
                                    }
                                }
                                continue;
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("a71_ads_client", "getAdCreativesByServerResponse(): error: " + e.getMessage());
        }
        return creatives;
    }

    private static void parseDebugTime(Context context, JSONObject mixerObj) throws JSONException {
        if (context != null) {
            String requestId = "";
            if (mixerObj.has(JsonBundleConstants.REQUEST_ID)) {
                requestId = mixerObj.optString(JsonBundleConstants.REQUEST_ID);
            }
            if (CupidUtils.isValidStr(requestId) && Integer.MAX_VALUE == CupidUtils.getDebugTime(context, requestId) && mixerObj.has(JsonBundleConstants.CUPID_EXTRAS)) {
                Long serverTime = (Long) CupidUtils.convertJson2Map(mixerObj.getJSONObject(JsonBundleConstants.CUPID_EXTRAS)).get(JsonBundleConstants.SERVER_TIME);
                if (serverTime != null) {
                    CupidUtils.setDebugTime(context, requestId, (int) (serverTime.longValue() - new Date().getTime()));
                }
            }
        }
    }
}
