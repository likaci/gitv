package com.mcto.ads.test;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class AndroidSDKTest extends AndroidTestCase {
    private final String CONSTANT_UDID = "TEST_UDID";
    AdsClient adsclient;
    private AssetManager mAssets;
    private Context mContext;

    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.mContext = (Context) AndroidTestCase.class.getMethod("getTestContext", new Class[0]).invoke(this, (Object[]) null);
            this.mAssets = this.mContext.getAssets();
        } catch (Exception x) {
            throw x;
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void resetJsonBundle(String fileName) throws JSONException {
        this.adsclient = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        this.adsclient.onRequestMobileServer();
        this.adsclient.onRequestMobileServerSucceededWithAdData(ReadAssetsFile.readAssetsFile(this.mAssets, fileName), "100003", "100004");
    }

    private String getPingbackWithReflection(AdsClient client) {
        try {
            Method MTD_getPingBack = AdsClient.class.getDeclaredMethod("getPingBack", (Class[]) null);
            MTD_getPingBack.setAccessible(true);
            return (String) MTD_getPingBack.invoke(client, (Object[]) null);
        } catch (Exception e) {
            return "";
        }
    }

    public void testGetFinalUrl() throws JSONException {
        resetJsonBundle("json_bundle_normal.json");
        assertTrue(this.adsclient.getFinalUrl().equals("testFinalUrl"));
    }

    public void testGetSlotSchedules() throws JSONException {
        boolean z;
        boolean z2 = true;
        resetJsonBundle("json_bundle_normal.json");
        List<CupidAdSlot> cupidAdSlotList = this.adsclient.getSlotSchedules();
        if (cupidAdSlotList.size() == 1) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        CupidAdSlot cupidAdSlot = (CupidAdSlot) cupidAdSlotList.get(0);
        if (cupidAdSlot.getSlotId() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (cupidAdSlot.getSlotType() == 1) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (cupidAdSlot.getDuration() == 30) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (cupidAdSlot.getOffsetInTimeline() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        Map<String, Object> slotExtras = cupidAdSlot.getSlotExtras();
        assertTrue(slotExtras.containsKey("string_param"));
        assertTrue(slotExtras.get("string_param") instanceof String);
        assertTrue("abc".equals(slotExtras.get("string_param")));
        assertTrue(slotExtras.containsKey("int_param"));
        assertTrue(slotExtras.get("int_param") instanceof Integer);
        if (((Integer) slotExtras.get("int_param")).intValue() != 1) {
            z2 = false;
        }
        assertTrue(z2);
    }

    public void testGetAdSchedules() throws JSONException {
        boolean z;
        boolean z2 = true;
        resetJsonBundle("json_bundle_normal.json");
        List<CupidAd> cupidAdList = this.adsclient.getAdSchedules(0);
        if (cupidAdList.size() == 1) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        CupidAd cupidAd = (CupidAd) cupidAdList.get(0);
        if (cupidAd.getAdId() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (cupidAd.getDuration() == 25) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (cupidAd.getOffsetInSlot() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        assertTrue(cupidAd.getClickThroughUrl().equals("url"));
        Map<String, Object> adExtras = cupidAd.getAdExtras();
        assertTrue(adExtras.containsKey("string_param"));
        assertTrue(adExtras.get("string_param") instanceof String);
        assertTrue("abc".equals(adExtras.get("string_param")));
        assertTrue(adExtras.containsKey("int_param"));
        assertTrue(adExtras.get("int_param") instanceof Integer);
        if (((Integer) adExtras.get("int_param")).intValue() != 1) {
            z2 = false;
        }
        assertTrue(z2);
    }

    public void testGetCupidExtras() throws JSONException {
        resetJsonBundle("json_bundle_normal.json");
        Map<String, Object> cupidExtras = this.adsclient.getCupidExtras();
        assertTrue(cupidExtras.containsKey("string_param"));
        assertTrue(cupidExtras.get("string_param") instanceof String);
        assertTrue("abc".equals(cupidExtras.get("string_param")));
        assertTrue(cupidExtras.containsKey("int_param"));
        assertTrue(cupidExtras.get("int_param") instanceof Integer);
        assertTrue(((Integer) cupidExtras.get("int_param")).intValue() == 1);
    }

    public void testGetAdPingBacks() throws JSONException {
        resetJsonBundle("json_bundle_normal.json");
        this.adsclient.onAdStarted(0);
        this.adsclient.onAdFirstQuartile(0);
        this.adsclient.onAdSecondQuartile(0);
        this.adsclient.onAdThirdQuartile(0);
        this.adsclient.onAdCompleted(0);
        this.adsclient.onAdClicked(0);
        pauseWaitingAsyncJobs(10);
        String targetImpressionId = "7c76beb46d5f177a14ac32f15521cff2";
        JSONObject jSONObject = new JSONObject(getPingbackWithReflection(this.adsclient));
        assertTrue(jSONObject.getString(PingbackConstants.USER_ID).equals("100001"));
        assertTrue(jSONObject.getString("playerId").equals("qc_100001_100015"));
        assertTrue(jSONObject.getString("albumId").equals("100002"));
        assertTrue(jSONObject.getString("appVersion").equals("3.6"));
        assertTrue(jSONObject.getString(PingbackConstants.SDK_VERSION).equals(AdsClient.SDK_VERSION));
        assertTrue(jSONObject.getString("udid").equals("TEST_UDID"));
        assertTrue(jSONObject.getString("tvId").equals("100003"));
        assertTrue(jSONObject.getString(PingbackConstants.V_ID).equals("100004"));
        assertTrue(jSONObject.getString(PingbackConstants.CHANNEL_ID).equals("100005"));
        JSONArray adInventoryArray = jSONObject.getJSONArray(PingbackConstants.AD_INVENTORY);
        assertTrue(adInventoryArray.length() == 1);
        JSONObject slotJson = adInventoryArray.getJSONObject(0);
        assertTrue(slotJson.getInt(PingbackConstants.SLOT_TYPE) == 1);
        assertTrue(slotJson.getInt(PingbackConstants.SLOT_START_TIME) == 0);
        assertTrue(slotJson.getInt("duration") == 30);
        JSONArray adArray = slotJson.getJSONArray("ads");
        assertTrue(adArray.length() == 1);
        JSONObject adJson = adArray.getJSONObject(0);
        assertTrue(adJson.getInt(PingbackConstants.AD_ORDER) == 0);
        assertTrue(adJson.getLong("orderItemId") == 1000000001111L);
        assertTrue(adJson.getInt("duration") == 25);
        JSONArray mixerEventArray = jSONObject.getJSONArray(PingbackConstants.MIXER_EVENTS);
        assertTrue(hasMixerEvent(mixerEventArray, "success", ""));
        assertTrue(hasMixerEvent(mixerEventArray, "success", ""));
        JSONArray adEventArray = jSONObject.getJSONArray(PingbackConstants.AD_EVENTS);
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "start", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "firstQuartile", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "midpoint", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "thirdQuartile", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "complete", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        assertTrue(hasAdEvent(adEventArray, 1, 0, 0, "click", "", 1000000001111L, targetImpressionId, "1/1;25/30;", "1:n1;"));
        JSONArray trackingEventArray = jSONObject.getJSONArray("tracking");
        assertTrue(hasTrackingEvent(trackingEventArray, PingbackConstants.ACT_TRACKING_SUCCESS, "impression", Boolean.valueOf(true), ErrorEvent.HTTP_CODE_SUCCESS));
        assertTrue(hasTrackingEvent(trackingEventArray, PingbackConstants.ACT_TRACKING_SUCCESS, "impression", Boolean.valueOf(false), ErrorEvent.HTTP_CODE_SUCCESS));
        assertTrue(hasTrackingEvent(trackingEventArray, PingbackConstants.ACT_TRACKING_SUCCESS, "click", Boolean.valueOf(true), ErrorEvent.HTTP_CODE_SUCCESS));
        assertTrue(hasTrackingEvent(trackingEventArray, PingbackConstants.ACT_TRACKING_SUCCESS, "click", Boolean.valueOf(false), ErrorEvent.HTTP_CODE_SUCCESS));
        this.adsclient.sendAdPingBacks();
    }

    public void testGetAdPingBacks_After_onRequestMobileServer() throws JSONException {
        boolean z;
        boolean z2 = true;
        AdsClient client = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        client.onRequestMobileServer();
        if (client.getAdSchedules(0).size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getSlotSchedules().size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getFinalUrl() != "") {
            z2 = false;
        }
        assertTrue(z2);
        String ping = getRidOfSendTimePart(getRidOfUUIDPart(getPingbackWithReflection(client)));
        JSONStringer jsonstr = new JSONStringer();
        jsonstr.object();
        jsonstr.key(PingbackConstants.USER_ID).value("100001");
        jsonstr.key("playerId").value("qc_100001_100015");
        jsonstr.key("albumId").value("100002");
        jsonstr.key("appVersion").value("3.6");
        jsonstr.key("udid").value("TEST_UDID");
        jsonstr.key(PingbackConstants.SDK_VERSION).value(AdsClient.SDK_VERSION);
        ArrayList<String> acts = new ArrayList();
        acts.add("success");
        addMixerEvents(acts, jsonstr);
        jsonstr.endObject();
        assertTrue(ping.equals(jsonstr.toString()));
    }

    public void testNoClickTracking() throws JSONException {
        resetJsonBundle("json_bundle_norma_no_click_tracking.json");
        JSONObject adJson = new JSONObject(getPingbackWithReflection(this.adsclient)).getJSONArray(PingbackConstants.AD_INVENTORY).getJSONObject(0).getJSONArray("ads").getJSONObject(0);
    }

    private boolean hasAdEvent(JSONArray adEventArray, int slotType, int slotStartTime, int adOrder, String act, String code, long orderItemId, String impressionId, String adServiceData, String adStrategy) {
        for (int i = 0; i < adEventArray.length(); i++) {
            try {
                JSONObject jsonAdEvent = adEventArray.getJSONObject(i);
                if (jsonAdEvent.getInt(PingbackConstants.SLOT_TYPE) == slotType && jsonAdEvent.getInt(PingbackConstants.SLOT_START_TIME) == slotStartTime && jsonAdEvent.getInt(PingbackConstants.AD_ORDER) == adOrder && jsonAdEvent.getString(PingbackConstants.ACT).equals(act) && jsonAdEvent.getString(PingbackConstants.CODE).equals(code) && jsonAdEvent.getLong("orderItemId") == orderItemId && jsonAdEvent.getString(PingbackConstants.IMPRESSION_ID).equals(impressionId) && jsonAdEvent.getString("data").equals(adServiceData) && jsonAdEvent.getString(PingbackConstants.AD_STRATEGY).equals(adStrategy)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean hasMixerEvent(JSONArray mixerEventArray, String act, String code) {
        for (int i = 0; i < mixerEventArray.length(); i++) {
            try {
                JSONObject jsonMixerEvent = mixerEventArray.getJSONObject(i);
                if (jsonMixerEvent.getString(PingbackConstants.ACT).equals(act) && jsonMixerEvent.getString(PingbackConstants.CODE).equals(code)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean hasTrackingEvent(JSONArray trackingEventArray, String act, String type, Boolean isCupid, String code) {
        int i = 0;
        while (i < trackingEventArray.length()) {
            try {
                JSONObject tracking = trackingEventArray.getJSONObject(i);
                return (tracking.getString(PingbackConstants.ACT).equals(act) && tracking.getString("type").equals(type) && tracking.getBoolean(PingbackConstants.IS_CUPID) == isCupid.booleanValue() && !tracking.getString(PingbackConstants.CODE).equals(code)) ? true : true;
            } catch (JSONException e) {
                e.printStackTrace();
                i++;
            }
        }
        return false;
    }

    public void testOnRequestMobileSucceeded_NoAd() throws JSONException {
        boolean z;
        boolean z2 = true;
        AdsClient client = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        client.onRequestMobileServer();
        client.onRequestMobileServerSucceededWithAdData(ReadAssetsFile.readAssetsFile(this.mAssets, "json_bundle_no_ad.json"), "100003", "100004");
        if (client.getSlotSchedules().size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getAdSchedules(0).size() != 0) {
            z2 = false;
        }
        assertTrue(z2);
        assertTrue(client.getFinalUrl().equals(""));
        String ping = getRidOfSendTimePart(getRidOfUUIDPart(getPingbackWithReflection(client)));
        JSONStringer jsonstr = new JSONStringer();
        jsonstr.object();
        jsonstr.key(PingbackConstants.USER_ID).value("100001");
        jsonstr.key("playerId").value("qc_100001_100015");
        jsonstr.key("albumId").value("100002");
        jsonstr.key("appVersion").value("3.6");
        jsonstr.key("udid").value("TEST_UDID");
        jsonstr.key(PingbackConstants.SDK_VERSION).value(AdsClient.SDK_VERSION);
        jsonstr.key("tvId").value("100003");
        jsonstr.key(PingbackConstants.V_ID).value("100004");
        jsonstr.key(PingbackConstants.CHANNEL_ID).value("100005");
        ArrayList<String> acts = new ArrayList();
        acts.add("success");
        acts.add(PingbackConstants.ACT_MIXER_NO_ADS);
        addMixerEvents(acts, jsonstr);
        jsonstr.key(PingbackConstants.AD_INVENTORY);
        jsonstr.array();
        jsonstr.endArray();
        jsonstr.key(PingbackConstants.AD_EVENTS);
        jsonstr.array();
        jsonstr.endArray();
        jsonstr.key("tracking");
        jsonstr.array();
        jsonstr.endArray();
        jsonstr.endObject();
        assertTrue(ping.equals(jsonstr.toString()));
    }

    public void testOnRequestMobileSucceeded_JsonError() throws JSONException {
        boolean z;
        boolean z2 = true;
        AdsClient client = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        client.onRequestMobileServer();
        client.onRequestMobileServerSucceededWithAdData(ReadAssetsFile.readAssetsFile(this.mAssets, "json_bundle_error.json"), "100003", "100004");
        if (client.getSlotSchedules().size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getAdSchedules(0).size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getFinalUrl() != "") {
            z2 = false;
        }
        assertTrue(z2);
        String ping = getRidOfSendTimePart(getRidOfUUIDPart(getPingbackWithReflection(client)));
        JSONStringer jsonstr = new JSONStringer();
        jsonstr.object();
        jsonstr.key(PingbackConstants.USER_ID).value("100001");
        jsonstr.key("playerId").value("qc_100001_100015");
        jsonstr.key("albumId").value("100002");
        jsonstr.key("appVersion").value("3.6");
        jsonstr.key("udid").value("TEST_UDID");
        jsonstr.key(PingbackConstants.SDK_VERSION).value(AdsClient.SDK_VERSION);
        jsonstr.key("tvId").value("100003");
        jsonstr.key(PingbackConstants.V_ID).value("100004");
        jsonstr.key(PingbackConstants.CHANNEL_ID).value("100005");
        ArrayList<String> acts = new ArrayList();
        acts.add("success");
        acts.add(PingbackConstants.ACT_MIXER_PARSE_ERROR);
        addMixerEvents(acts, jsonstr);
        jsonstr.endObject();
        assertTrue(ping.equals(jsonstr.toString()));
    }

    public void testOnRequestMobileServerFailed() throws JSONException {
        boolean z;
        boolean z2 = true;
        AdsClient client = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        client.onRequestMobileServer();
        client.onRequestMobileServerFailed();
        if (client.getAdSchedules(0).size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getSlotSchedules().size() == 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (client.getFinalUrl() != "") {
            z2 = false;
        }
        assertTrue(z2);
        String ping = getRidOfSendTimePart(getRidOfUUIDPart(getPingbackWithReflection(client)));
        JSONStringer jsonstr = new JSONStringer();
        jsonstr.object();
        jsonstr.key(PingbackConstants.USER_ID).value("100001");
        jsonstr.key("playerId").value("qc_100001_100015");
        jsonstr.key("albumId").value("100002");
        jsonstr.key("appVersion").value("3.6");
        jsonstr.key("udid").value("TEST_UDID");
        jsonstr.key(PingbackConstants.SDK_VERSION).value(AdsClient.SDK_VERSION);
        ArrayList<String> acts = new ArrayList();
        acts.add("success");
        acts.add(PingbackConstants.ACT_MIXER_HTTP_ERROR);
        addMixerEvents(acts, jsonstr);
        jsonstr.endObject();
        assertTrue(ping.equals(jsonstr.toString()));
    }

    private String getRidOfUUIDPart(String jsonStr) {
        return jsonStr.replaceAll("\\\"uuid\\\":\\\"\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}\\\",?", "");
    }

    private String getRidOfSendTimePart(String jsonStr) {
        return jsonStr.replaceAll("\\\"sendTime\\\":\\\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\\",?", "");
    }

    private void addMixerEvents(ArrayList<String> actList, JSONStringer jsonstr) throws JSONException {
        jsonstr.key(PingbackConstants.MIXER_EVENTS).array();
        for (int i = 0; i < actList.size(); i++) {
            jsonstr.object();
            jsonstr.key(PingbackConstants.ACT).value(actList.get(i));
            jsonstr.key(PingbackConstants.CODE).value("");
            jsonstr.endObject();
        }
        jsonstr.endArray();
    }

    private void pauseWaitingAsyncJobs(int interval) {
        try {
            Thread.sleep((long) (interval * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
