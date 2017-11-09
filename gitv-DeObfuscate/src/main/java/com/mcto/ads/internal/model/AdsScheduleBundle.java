package com.mcto.ads.internal.model;

import android.util.Log;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdsScheduleBundle {
    private Map<String, Object> cupidExtras = new HashMap();
    private String dspSessionId;
    private String finalUrl;
    private List<FutureSlotInfo> futureSlotList = new ArrayList();
    private String reqUrl;
    private int resultId;
    private long serverTime = 0;
    private List<SlotInfo> slotInfoList = new ArrayList();
    private int slot_id_seed = 0;
    private String videoEventId;

    class C19611 implements Comparator<SlotInfo> {
        C19611() {
        }

        public int compare(SlotInfo lhs, SlotInfo rhs) {
            return (-1 == lhs.getOffsetInTimeline() || -1 == rhs.getOffsetInTimeline()) ? rhs.getOffsetInTimeline() - lhs.getOffsetInTimeline() : lhs.getOffsetInTimeline() - rhs.getOffsetInTimeline();
        }
    }

    public AdsScheduleBundle(int resultId, String jsonBundleString, CupidContext cupidContext) throws JSONException {
        this.resultId = resultId;
        JSONObject jsonBundle = new JSONObject(jsonBundleString);
        if (jsonBundle.has(JsonBundleConstants.FINAL_URL)) {
            this.finalUrl = jsonBundle.getString(JsonBundleConstants.FINAL_URL);
        }
        if (jsonBundle.has(JsonBundleConstants.REQ_URL)) {
            this.reqUrl = jsonBundle.getString(JsonBundleConstants.REQ_URL);
        }
        if (jsonBundle.has(JsonBundleConstants.DSP_SESSION_ID)) {
            this.dspSessionId = jsonBundle.getString(JsonBundleConstants.DSP_SESSION_ID);
        }
        if (jsonBundle.has(JsonBundleConstants.VIDEO_EVENT_ID)) {
            this.videoEventId = jsonBundle.getString(JsonBundleConstants.VIDEO_EVENT_ID);
            cupidContext.setVideoEventId(this.videoEventId);
        }
        if (jsonBundle.has("network")) {
            cupidContext.setNetwork(jsonBundle.getString("network"));
        }
        if (jsonBundle.has(JsonBundleConstants.REQUEST_ID)) {
            cupidContext.setRequestId(jsonBundle.getString(JsonBundleConstants.REQUEST_ID));
        }
        parseReqTemplateTypes(jsonBundle, cupidContext);
        if (jsonBundle.has(JsonBundleConstants.CUPID_EXTRAS)) {
            JSONObject jsonCupidExtras = jsonBundle.getJSONObject(JsonBundleConstants.CUPID_EXTRAS);
            this.cupidExtras = CupidUtils.convertJson2Map(jsonCupidExtras);
            parseDebugTime(cupidContext);
            if (jsonCupidExtras.has(JsonBundleConstants.ENABLE_PINGBACK2) && jsonCupidExtras.optInt(JsonBundleConstants.ENABLE_PINGBACK2, 1) == 0) {
                cupidContext.setEnablePB2(false);
            }
        }
        if (jsonBundle.has(JsonBundleConstants.INVENTORY)) {
            cupidContext.setInventory(CupidUtils.convertJson2Map(jsonBundle.getJSONObject(JsonBundleConstants.INVENTORY)));
        }
        if (jsonBundle.has(JsonBundleConstants.PINGBACK_EXTRAS)) {
            cupidContext.setPingbackExtras(CupidUtils.convertJson2Map(jsonBundle.getJSONObject(JsonBundleConstants.PINGBACK_EXTRAS)));
        }
        parseUrlConfig(jsonBundle, cupidContext);
        parseSlots(jsonBundle, cupidContext);
        parseFutureSlots(jsonBundle);
        parseTrackingTimeouts(jsonBundle, cupidContext);
        parsePingbackConfig(jsonBundle, cupidContext);
    }

    public int getResultId() {
        return this.resultId;
    }

    public String getVideoEventId() {
        return this.videoEventId;
    }

    public String getFinalUrl() {
        return this.finalUrl;
    }

    public String getReqUrl() {
        return this.reqUrl;
    }

    public String getDspSessionId() {
        return this.dspSessionId;
    }

    public long getServerTime() {
        return this.serverTime;
    }

    public List<SlotInfo> getSlotInfoList() {
        return this.slotInfoList;
    }

    public List<FutureSlotInfo> getFutureSlotList() {
        return this.futureSlotList;
    }

    public Map<String, Object> getCupidExtras() {
        return this.cupidExtras;
    }

    public SlotInfo getSlotInfoBySlotId(int slotId) {
        int slotInfoSize = this.slotInfoList.size();
        for (int i = 0; i < slotInfoSize; i++) {
            SlotInfo slotInfo = (SlotInfo) this.slotInfoList.get(i);
            if (slotInfo.getSlotId() == slotId) {
                return slotInfo;
            }
        }
        return null;
    }

    private void parseDebugTime(CupidContext cupidContext) throws JSONException {
        Long tempTime = (Long) this.cupidExtras.get(JsonBundleConstants.SERVER_TIME);
        if (tempTime != null) {
            this.serverTime = tempTime.longValue();
            cupidContext.setDebugTime(tempTime.longValue() - new Date().getTime());
        }
        if (cupidContext.hasMobileInterstitial()) {
            int debugTime = CupidUtils.getDebugTime(cupidContext.getSystemContext(), cupidContext.getRequestId());
            if (Integer.MAX_VALUE != debugTime) {
                cupidContext.setDebugTime((long) debugTime);
            } else {
                CupidUtils.setDebugTime(cupidContext.getSystemContext(), cupidContext.getRequestId(), debugTime);
            }
        }
    }

    private void parseSlots(JSONObject jsonBundle, CupidContext cupidContext) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.AD_SLOTS)) {
            JSONArray adsSlotArr = jsonBundle.optJSONArray(JsonBundleConstants.AD_SLOTS);
            if (adsSlotArr != null) {
                boolean hasInterstitial = cupidContext.hasMobileInterstitial();
                long currentTime = (Calendar.getInstance().getTimeInMillis() / 1000) + (cupidContext.getDebugTime() / 1000);
                int length = adsSlotArr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject slotObj = adsSlotArr.getJSONObject(i);
                    int i2 = this.resultId;
                    int i3 = this.slot_id_seed + 1;
                    this.slot_id_seed = i3;
                    int slotId = CupidUtils.generateSlotId(i2, i3);
                    if (hasInterstitial) {
                        int orderStartTime = slotObj.optInt(JsonBundleConstants.ORDER_START_TIME);
                        int orderEndTime = slotObj.optInt(JsonBundleConstants.ORDER_END_TIME);
                        if (orderStartTime == 0 || orderEndTime == 0 || orderStartTime >= orderEndTime) {
                            Log.e("a71_ads_client", "parseSlots(): invalid order start or end time.");
                        } else {
                            if (((long) orderStartTime) < currentTime) {
                                if (currentTime >= ((long) orderEndTime)) {
                                }
                            }
                        }
                    }
                    this.slotInfoList.add(new SlotInfo(slotId, slotObj, cupidContext));
                }
                Collections.sort(this.slotInfoList, new C19611());
            }
        }
    }

    private void parseFutureSlots(JSONObject jsonBundle) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.FUTURE_SLOTS)) {
            JSONArray jsonFutureSlots = jsonBundle.optJSONArray(JsonBundleConstants.FUTURE_SLOTS);
            if (jsonFutureSlots != null) {
                int length = jsonFutureSlots.length();
                for (int i = 0; i < length; i++) {
                    this.futureSlotList.add(new FutureSlotInfo(jsonFutureSlots.getJSONObject(i)));
                }
            }
        }
    }

    private void parseReqTemplateTypes(JSONObject jsonBundle, CupidContext cupidContext) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.REQ_TEMPLATE_TYPES)) {
            JSONArray jsonReqTemplateTypes = jsonBundle.optJSONArray(JsonBundleConstants.REQ_TEMPLATE_TYPES);
            if (jsonReqTemplateTypes != null) {
                List<String> reqTemplateTypes = new ArrayList();
                int length = jsonReqTemplateTypes.length();
                for (int i = 0; i < length; i++) {
                    reqTemplateTypes.add(jsonReqTemplateTypes.getString(i));
                }
                cupidContext.setReqTemplateTypes(reqTemplateTypes);
            }
        }
    }

    private void parseTrackingTimeouts(JSONObject jsonBundle, CupidContext cupidContext) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.TRACKING_TIMEOUTS)) {
            JSONArray jsonTrackingTimeouts = jsonBundle.optJSONArray(JsonBundleConstants.TRACKING_TIMEOUTS);
            if (jsonTrackingTimeouts != null && jsonTrackingTimeouts.length() > 0) {
                int trackingTimeout = jsonTrackingTimeouts.optInt(0, 10000);
                if (trackingTimeout > 0 && trackingTimeout <= 60000) {
                    cupidContext.setTrackingTimeout(trackingTimeout);
                }
            }
        }
    }

    private void parsePingbackConfig(JSONObject jsonBundle, CupidContext context) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.PINGBACK_CONFIG)) {
            JSONObject pcObj = jsonBundle.getJSONObject(JsonBundleConstants.PINGBACK_CONFIG);
            if (pcObj != null) {
                Map<String, Map<String, Object>> pingbackConfig = new HashMap();
                JSONArray trackKeys = pcObj.names();
                int trackKeyCount = trackKeys.length();
                for (int i = 0; i < trackKeyCount; i++) {
                    String trackKey = trackKeys.getString(i);
                    JSONObject trackObj = pcObj.getJSONObject(trackKey);
                    if (trackObj != null) {
                        pingbackConfig.put(trackKey, CupidUtils.convertJson2Map(trackObj));
                    }
                }
                context.setPingbackConfig(pingbackConfig);
            }
        }
    }

    private void parseUrlConfig(JSONObject jsonBundle, CupidContext cupidContext) throws JSONException {
        if (jsonBundle.has(JsonBundleConstants.URL_CONFIG)) {
            JSONObject configObj = jsonBundle.getJSONObject(JsonBundleConstants.URL_CONFIG);
            if (configObj != null) {
                JSONArray pingbackUrls = configObj.optJSONArray(JsonBundleConstants.PINGBACK_URL);
                if (pingbackUrls != null && pingbackUrls.length() > 0) {
                    CupidGlobal.setPingbackUrl((String) pingbackUrls.get(0));
                }
                String cupidTrackingUrl = configObj.optString("cupidTracking");
                if (CupidUtils.isValidStr(cupidTrackingUrl)) {
                    cupidContext.setCupidTrackingUrl(cupidTrackingUrl);
                }
                String adxTrackingUrl = configObj.optString("adxTracking");
                if (CupidUtils.isValidStr(adxTrackingUrl)) {
                    cupidContext.setAdxTrackingUrl(adxTrackingUrl);
                }
            }
        }
    }

    public boolean isOldInterstitials() {
        int slotInfoSize = this.slotInfoList.size();
        for (int i = 0; i < slotInfoSize; i++) {
            List<AdInfo> adInfoList = ((SlotInfo) this.slotInfoList.get(i)).getPlayableAds();
            int adInfoSize = adInfoList.size();
            for (int j = 0; j < adInfoSize; j++) {
                if (((AdInfo) adInfoList.get(j)).isOldInterstitials()) {
                    return true;
                }
            }
        }
        return false;
    }
}
