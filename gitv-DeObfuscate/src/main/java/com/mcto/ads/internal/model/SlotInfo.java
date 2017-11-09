package com.mcto.ads.internal.model;

import android.util.Log;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.mcto.ads.internal.net.TrackingParty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SlotInfo {
    private String adZoneId;
    private int ad_id_seed = 0;
    private int duration;
    private List<AdInfo> emptyTrackings;
    private int offsetInTimeline;
    private List<AdInfo> playableAds;
    private int sequenceId = 0;
    private Map<String, Object> slotExtras;
    private int slotId;
    private int startTime;
    private int type;

    class C19621 implements Comparator<AdInfo> {
        C19621() {
        }

        public int compare(AdInfo lhs, AdInfo rhs) {
            return lhs.getOrder() - rhs.getOrder();
        }
    }

    SlotInfo(int slotId, JSONObject jsonSlot, CupidContext cupidContext) throws JSONException {
        this.slotId = slotId;
        this.playableAds = new ArrayList();
        this.emptyTrackings = new ArrayList();
        this.slotExtras = new HashMap();
        if (jsonSlot.has("type")) {
            this.type = jsonSlot.getInt("type");
        }
        if (jsonSlot.has("startTime")) {
            this.offsetInTimeline = jsonSlot.getInt("startTime") * 1000;
        }
        if (jsonSlot.has("duration")) {
            this.duration = jsonSlot.getInt("duration") * 1000;
        }
        if (jsonSlot.has("startTime")) {
            this.startTime = jsonSlot.getInt("startTime") * 1000;
        }
        if (jsonSlot.has("adZoneId")) {
            this.adZoneId = jsonSlot.getString("adZoneId");
        }
        if (jsonSlot.has(JsonBundleConstants.SLOT_EXTRAS)) {
            this.slotExtras = CupidUtils.convertJson2Map(jsonSlot.getJSONObject(JsonBundleConstants.SLOT_EXTRAS));
        }
        parsePlayableAds(jsonSlot, cupidContext);
        parseEmptyTrackings(jsonSlot);
    }

    private void parseEmptyTrackings(JSONObject jsonSlot) throws JSONException {
        try {
            if (jsonSlot.has(JsonBundleConstants.EMPTY_TRACKING)) {
                JSONObject jsonEmptyTrackings = jsonSlot.optJSONObject(JsonBundleConstants.EMPTY_TRACKING);
                if (jsonEmptyTrackings != null) {
                    JSONArray jsonTimeSlices = jsonEmptyTrackings.optJSONArray(JsonBundleConstants.TIME_SLICES);
                    if (jsonTimeSlices != null) {
                        int length = jsonTimeSlices.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonTimeSlice = jsonTimeSlices.getJSONObject(i);
                            int i2 = this.slotId;
                            int i3 = this.ad_id_seed + 1;
                            this.ad_id_seed = i3;
                            AdInfo adInfo = new AdInfo(CupidUtils.generateAdId(i2, i3), this, jsonTimeSlice);
                            this.emptyTrackings.add(adInfo);
                            if (jsonEmptyTrackings.has(JsonBundleConstants.A71_TRACKING_PARAMS)) {
                                adInfo.setTrackingParams(TrackingParty.CUPID, jsonEmptyTrackings.getJSONObject(JsonBundleConstants.A71_TRACKING_PARAMS));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("a71_ads_client", "parseEmptyTrackings(): exception: ", ex);
        }
    }

    private void parsePlayableAds(JSONObject jsonSlot, CupidContext cupidContext) {
        try {
            if (jsonSlot.has("ads")) {
                JSONArray jsonAds = jsonSlot.optJSONArray("ads");
                if (jsonAds != null) {
                    int length = jsonAds.length();
                    int offsetInSlot = 0;
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonAd = jsonAds.getJSONObject(i);
                        int i2 = this.slotId;
                        int i3 = this.ad_id_seed + 1;
                        this.ad_id_seed = i3;
                        AdInfo adInfo = new AdInfo(CupidUtils.generateAdId(i2, i3), this, offsetInSlot, jsonAd);
                        StringBuilder sb = new StringBuilder();
                        sb.append(cupidContext.getRequestId());
                        sb.append(this.adZoneId);
                        sb.append(adInfo.getOrder());
                        Log.d("a71_ads_client", "set ad identifier: " + sb.toString());
                        adInfo.setIdentifier(sb.toString());
                        this.playableAds.add(adInfo);
                        offsetInSlot += adInfo.getDuration();
                    }
                    Collections.sort(this.playableAds, new C19621());
                }
            }
        } catch (Exception ex) {
            Log.d("a71_ads_client", "parsePlayableAds(): exception: ", ex);
        }
    }

    public int getType() {
        return this.type;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getOffsetInTimeline() {
        return this.offsetInTimeline;
    }

    public int getDuration() {
        return this.duration;
    }

    public List<AdInfo> getPlayableAds() {
        return this.playableAds;
    }

    public List<AdInfo> getEmptyTrackings() {
        return this.emptyTrackings;
    }

    public String getAdZoneId() {
        return this.adZoneId;
    }

    public Map<String, Object> getSlotExtras() {
        return this.slotExtras;
    }

    public int getSequenceId() {
        return this.sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public boolean isRollType() {
        if (1 == this.type || 2 == this.type || 3 == this.type) {
            return true;
        }
        return false;
    }

    public boolean isRuntimeSlot() {
        if (2 == this.type || 4 == this.type || 10 == this.type) {
            return true;
        }
        return false;
    }

    public boolean isFirstAd(AdInfo adInfo) {
        if (adInfo == null || this.playableAds.isEmpty()) {
            return false;
        }
        if (adInfo.getAdId() == ((AdInfo) this.playableAds.get(0)).getAdId()) {
            return true;
        }
        return false;
    }

    public boolean isLastAd(AdInfo adInfo) {
        if (adInfo == null || this.playableAds.isEmpty()) {
            return false;
        }
        if (adInfo.getAdId() == ((AdInfo) this.playableAds.get(this.playableAds.size() - 1)).getAdId()) {
            return true;
        }
        return false;
    }

    public boolean isPlayableAdsEmpty() {
        return this.playableAds.isEmpty();
    }

    public int getAdIndex(int adId) {
        for (int i = 0; i < getPlayableAds().size(); i++) {
            if (adId == ((AdInfo) getPlayableAds().get(i)).getAdId()) {
                return i;
            }
        }
        return -1;
    }
}
