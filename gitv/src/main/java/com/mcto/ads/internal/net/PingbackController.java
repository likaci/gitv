package com.mcto.ads.internal.net;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.model.AdInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONStringer;

public class PingbackController {
    private static final String ADX_TRACKING_EVENT_KEY = "adxtracking";
    private static final String AD_EVENT_KEY = "ad";
    private static final String INVENTORY_EVENT_KEY = "inventory";
    public static final int MAX_PINGBACK_SIZE = 1;
    private static final String MIXER_EVENT_KEY = "mixer";
    public static final int SIZE_FOR_TV_EXIT = 1;
    private static final String STATISTICS_EVENT_KEY = "statistics";
    private static final String TRACKING_EVENT_KEY = "tracking";
    private static final String VISIT_EVENT_KEY = "visit";
    private static ArrayList<String> visitSentList = new ArrayList();
    private Map<Integer, Map<Integer, List<BaseEvent>>> adEvents = new HashMap();
    private Map<Integer, BaseEvent> baseEvents = new HashMap();
    private long debugTime = 0;
    private ArrayList<Integer> inventorySentList = new ArrayList();
    private Map<String, BasicInfo> kBasicEvents = new HashMap();
    private Map<Integer, Map<String, List<BaseEvent>>> monitorEvents = new HashMap();
    private Map<Integer, List<BaseEvent>> statisticsEvents = new HashMap();
    private int triggerSendingSize = 1;

    public PingbackController() {
        initBasicEvents();
    }

    public void assembleBaseEvent(int resultId, CupidGlobal global, CupidContext context) {
        BaseEvent event = new BaseEvent();
        if (global != null) {
            event.uaaUserId = global.getUaaUserId();
            event.cupidUserId = global.getCupidUserId();
            event.sdkVersion = global.getSdkVersion();
            event.appVersion = global.getAppVersion();
            event.mobileKey = global.getMobileKey();
        }
        if (context != null) {
            event.tvId = context.getTvId();
            event.videoEventId = context.getVideoEventId();
            event.requestId = context.getRequestId();
            event.pingbackExtras = context.getPingbackExtras();
            event.playerId = context.getPlayerId();
            event.network = context.getNetwork();
            this.debugTime = context.getDebugTime();
        }
        this.baseEvents.put(Integer.valueOf(resultId), event);
    }

    public void sendVisitPingback(int resultId, String actType, CupidContext context) {
        if (context != null && context.enablePB2()) {
            String tvId = context.getTvId();
            if (CupidUtils.isValidStr(tvId)) {
                if (!visitSentList.contains(tvId)) {
                    visitSentList.add(tvId);
                } else {
                    return;
                }
            }
            BaseEvent event = new BaseEvent();
            BasicInfo basicInfo = getBasicInfo(actType);
            if (basicInfo != null) {
                event.subType = basicInfo.subType;
            }
            sendPingbackData(buildVisitEventsValue(resultId, event));
        }
    }

    public void sendInventoryPingback(int resultId, String actType, CupidContext context) {
        if (context != null && context.enablePB2() && !context.getInventory().isEmpty()) {
            if (context.hasMobileInterstitial()) {
                Log.d("a71_ads_client", "sendInventoryPingback(): mobile interstitial not send inventory.");
            } else if (!this.inventorySentList.contains(Integer.valueOf(resultId))) {
                this.inventorySentList.add(Integer.valueOf(resultId));
                BaseEvent event = new BaseEvent();
                BasicInfo basicInfo = getBasicInfo(actType);
                if (basicInfo != null) {
                    event.subType = basicInfo.subType;
                }
                event.inventory = context.getInventory();
                event.requestId = context.getRequestId();
                sendPingbackData(buildInventoryEventsValue(resultId, event));
            }
        }
    }

    public void addAdEvent(String actType, AdInfo adInfo, CupidContext context) {
        if (adInfo != null) {
            BaseEvent event = new BaseEvent();
            BasicInfo basicInfo = getBasicInfo(actType);
            if (basicInfo != null) {
                event.subType = basicInfo.subType;
            }
            if (context != null) {
                event.requestId = context.getRequestId();
            }
            event.orderItemId = adInfo.getOrderItemId();
            event.creativeId = adInfo.getCreativeId();
            event.dspId = adInfo.getDspId();
            event.adStrategy = adInfo.getAdStrategy();
            event.sequenceId = adInfo.getSequenceId();
            event.customInfo = adInfo.getCustomInfo(actType);
            event.templateType = adInfo.getTemplateType();
            assembleAdEvents(adInfo.getAdId(), event);
        }
    }

    public void addMixerEvent(int resultId, String actType, String errorMsg, CupidContext context) {
        if (context != null && context.enablePB2()) {
            BaseEvent event = new BaseEvent();
            BasicInfo basicInfo = getBasicInfo(actType);
            if (basicInfo != null) {
                event.subType = basicInfo.subType;
                event.errCode = basicInfo.code;
            }
            event.requestId = context.getRequestId();
            event.errMsg = errorMsg;
            assembleMonitorEvents(resultId, "mixer", event);
        }
    }

    public void addTrackEvent(TrackingParty party, String actType, AdInfo adInfo, Map<String, String> notification, CupidContext context) {
        if (context != null && context.enablePB2() && adInfo != null) {
            String pingbackType;
            Map<String, Object> params = null;
            if (party == TrackingParty.CUPID) {
                pingbackType = "tracking";
            } else if (party == TrackingParty.ADX) {
                pingbackType = ADX_TRACKING_EVENT_KEY;
            } else if (party == TrackingParty.THIRD) {
                String trackingStr = null;
                if (notification != null) {
                    trackingStr = (String) notification.get(PingbackConstants.TRACKING_URL);
                }
                if (trackingStr != null) {
                    params = context.getThirdPingbackParams(trackingStr);
                    if (!params.isEmpty()) {
                        pingbackType = (String) params.get("p");
                        if (pingbackType == null) {
                            return;
                        }
                    }
                    return;
                }
                return;
            } else {
                return;
            }
            assembleMonitorEvents(CupidUtils.getResultIdByAdId(adInfo.getAdId()), pingbackType, assembleTrackEvent(actType, notification, adInfo, context, params));
        }
    }

    private BaseEvent assembleTrackEvent(String actType, Map<String, String> notification, AdInfo adInfo, CupidContext context, Map<String, Object> params) {
        BaseEvent event = new BaseEvent();
        BasicInfo basicInfo = getBasicInfo(actType);
        if (basicInfo != null) {
            event.subType = basicInfo.subType;
            event.errCode = basicInfo.code;
        }
        if (!(params == null || params.isEmpty())) {
            event.params = new HashMap();
            event.params.putAll(params);
        }
        if (notification != null) {
            try {
                String reqDuration = (String) notification.get(PingbackConstants.REQUEST_DURATION);
                if (CupidUtils.isValidStr(reqDuration)) {
                    event.reqDuration = Integer.parseInt(reqDuration);
                }
                String reqCount = (String) notification.get(PingbackConstants.REQUEST_COUNT);
                if (CupidUtils.isValidStr(reqCount)) {
                    event.reqCount = Integer.parseInt(reqCount);
                }
            } catch (NumberFormatException ex) {
                Log.d("a71_ads_client", "assembleTrackEvent():", ex);
            }
        }
        event.adInfo = adInfo.getAdInfo();
        event.requestId = context.getRequestId();
        return event;
    }

    public void addStatisticsPingback(String actType, AdInfo adInfo, CupidContext context) {
        if (context != null && context.enablePB2()) {
            BaseEvent event = new BaseEvent();
            BasicInfo basicInfo = getBasicInfo(actType);
            if (basicInfo != null) {
                event.subType = basicInfo.subType;
            }
            event.adInfo = adInfo.getAdInfo();
            event.requestId = context.getRequestId();
            event.customInfo = adInfo.getCustomInfo(actType);
            assembleStatisticsEvents(CupidUtils.getResultIdByAdId(adInfo.getAdId()), event);
        }
    }

    private synchronized void assembleStatisticsEvents(int resultId, BaseEvent event) {
        List<BaseEvent> events = (List) this.statisticsEvents.get(Integer.valueOf(resultId));
        if (events == null) {
            events = new ArrayList();
            this.statisticsEvents.put(Integer.valueOf(resultId), events);
        }
        events.add(event);
        tryToFlushPingback();
    }

    public void flushCupidPingback() {
        flushAdPingback();
        flushMonitorPingback();
        flushStatisticsPingback();
    }

    private synchronized void flushAdPingback() {
        if (!this.adEvents.isEmpty()) {
            for (Integer intValue : this.adEvents.keySet()) {
                sendPingbackData(buildAdEventsValue(intValue.intValue()));
            }
            this.adEvents.clear();
        }
    }

    private synchronized void flushMonitorPingback() {
        if (!this.monitorEvents.isEmpty()) {
            for (Integer intValue : this.monitorEvents.keySet()) {
                sendPingbackData(buildMonitorEventsValue(intValue.intValue()));
            }
            this.monitorEvents.clear();
        }
    }

    private synchronized void flushStatisticsPingback() {
        if (!this.statisticsEvents.isEmpty()) {
            for (Integer intValue : this.statisticsEvents.keySet()) {
                sendPingbackData(buildStatisticsEventsValue(intValue.intValue()));
            }
            this.statisticsEvents.clear();
        }
    }

    private synchronized void assembleAdEvents(int adId, BaseEvent event) {
        int resultId = CupidUtils.getResultIdByAdId(adId);
        Map<Integer, List<BaseEvent>> events = (Map) this.adEvents.get(Integer.valueOf(resultId));
        if (events == null) {
            events = new HashMap();
            this.adEvents.put(Integer.valueOf(resultId), events);
        }
        List<BaseEvent> list = (List) events.get(Integer.valueOf(adId));
        if (list == null) {
            list = new ArrayList();
            events.put(Integer.valueOf(adId), list);
        }
        list.add(event);
        tryToFlushPingback();
    }

    private synchronized void assembleMonitorEvents(int resultId, String type, BaseEvent event) {
        Map<String, List<BaseEvent>> events = (Map) this.monitorEvents.get(Integer.valueOf(resultId));
        if (events == null) {
            events = new HashMap();
            this.monitorEvents.put(Integer.valueOf(resultId), events);
        }
        List<BaseEvent> list = (List) events.get(type);
        if (list == null) {
            list = new ArrayList();
            events.put(type, list);
        }
        list.add(event);
        tryToFlushPingback();
    }

    private void tryToFlushPingback() {
        int count = 0;
        for (Map<Integer, List<BaseEvent>> events : this.adEvents.values()) {
            if (events != null) {
                count += events.size();
            }
        }
        for (Map<String, List<BaseEvent>> events2 : this.monitorEvents.values()) {
            if (events2 != null) {
                for (List<BaseEvent> eventList : events2.values()) {
                    if (eventList != null) {
                        count += eventList.size();
                    }
                }
            }
        }
        for (List<BaseEvent> events3 : this.statisticsEvents.values()) {
            if (events3 != null) {
                count += events3.size();
            }
        }
        if (count >= this.triggerSendingSize) {
            flushCupidPingback();
        }
    }

    private void sendPingbackData(String pingbackData) {
        try {
            if (VERSION.SDK_INT < 11) {
                new HttpPostAsyncClient().execute(new String[]{URLEncoder.encode(pingbackData, "UTF-8")});
            } else {
                new HttpPostAsyncClient().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{URLEncoder.encode(pingbackData, "UTF-8")});
            }
            Log.d("a71_ads_client", "sendPingbackData(): post data:" + pingbackData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String buildVisitEventsValue(int resultId, BaseEvent event) {
        String rtn = "";
        try {
            JSONStringer jsonstr = new JSONStringer();
            jsonstr.object();
            BaseEvent baseEvent = (BaseEvent) this.baseEvents.get(Integer.valueOf(resultId));
            if (baseEvent == null) {
                return rtn;
            }
            buildBaseEventsValue(baseEvent, jsonstr);
            jsonstr.key("visit").array();
            jsonstr.object();
            jsonstr.key(Keys.T).array();
            jsonstr.value(event.subType);
            jsonstr.endArray();
            CupidUtils.setJsonKeyValue(jsonstr, "pp", CupidUtils.getPassportId());
            jsonstr.endObject();
            jsonstr.endArray();
            jsonstr.endObject();
            rtn = jsonstr.toString();
            return rtn;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildVisitEventsValue(): json error:", e);
        }
    }

    private String buildInventoryEventsValue(int resultId, BaseEvent event) {
        String rtn = "";
        try {
            JSONStringer jsonstr = new JSONStringer();
            jsonstr.object();
            BaseEvent baseEvent = (BaseEvent) this.baseEvents.get(Integer.valueOf(resultId));
            if (baseEvent == null) {
                return rtn;
            }
            buildBaseEventsValue(baseEvent, jsonstr);
            jsonstr.key("inventory").array();
            jsonstr.object();
            jsonstr.key(Keys.T).array();
            jsonstr.value(event.subType);
            jsonstr.endArray();
            Map<String, String> inventory = event.inventory;
            if (!(inventory == null || inventory.isEmpty())) {
                for (String key : inventory.keySet()) {
                    CupidUtils.setJsonKeyValue(jsonstr, key, (String) inventory.get(key));
                }
            }
            CupidUtils.setJsonKeyValue(jsonstr, "rid", event.requestId);
            jsonstr.endObject();
            jsonstr.endArray();
            jsonstr.endObject();
            rtn = jsonstr.toString();
            return rtn;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildInventoryEventsValue(): json error:", e);
        }
    }

    private String buildAdEventsValue(int resultId) {
        String rtn = "";
        try {
            JSONStringer jsonstr = new JSONStringer();
            jsonstr.object();
            BaseEvent baseEvent = (BaseEvent) this.baseEvents.get(Integer.valueOf(resultId));
            if (baseEvent == null) {
                return rtn;
            }
            buildBaseEventsValue(baseEvent, jsonstr);
            jsonstr.key("ad").array();
            Map<Integer, List<BaseEvent>> events = (Map) this.adEvents.get(Integer.valueOf(resultId));
            if (!(events == null || events.isEmpty())) {
                for (Integer intValue : events.keySet()) {
                    List<BaseEvent> eventList = (List) events.get(Integer.valueOf(intValue.intValue()));
                    jsonstr.object();
                    jsonstr.key(Keys.T).array();
                    if (!(eventList == null || eventList.isEmpty())) {
                        for (BaseEvent event : eventList) {
                            jsonstr.value(event.subType);
                        }
                        jsonstr.endArray();
                        BaseEvent event2 = (BaseEvent) eventList.get(0);
                        jsonstr.key("sq").value((long) event2.sequenceId);
                        jsonstr.key("od").value(event2.orderItemId);
                        jsonstr.key("ct").value(event2.creativeId);
                        jsonstr.key("dp").value(event2.dspId);
                        CupidUtils.setJsonKeyValue(jsonstr, WebConstants.PARAM_KEY_X, event2.customInfo);
                        CupidUtils.setJsonKeyValue(jsonstr, "as", event2.adStrategy);
                        CupidUtils.setJsonKeyValue(jsonstr, "rid", event2.requestId);
                        CupidUtils.setJsonKeyValue(jsonstr, "tt", event2.templateType);
                    }
                    jsonstr.endObject();
                }
            }
            jsonstr.endArray();
            jsonstr.endObject();
            rtn = jsonstr.toString();
            return rtn;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildAdEventsValue(): json error:", e);
        }
    }

    private String buildMonitorEventsValue(int resultId) {
        String rtn = "";
        try {
            JSONStringer jsonstr = new JSONStringer();
            jsonstr.object();
            BaseEvent baseEvent = (BaseEvent) this.baseEvents.get(Integer.valueOf(resultId));
            if (baseEvent == null) {
                return rtn;
            }
            buildBaseEventsValue(baseEvent, jsonstr);
            Map<String, List<BaseEvent>> events = (Map) this.monitorEvents.get(Integer.valueOf(resultId));
            if (!(events == null || events.isEmpty())) {
                for (String type : events.keySet()) {
                    jsonstr.key(type).array();
                    List<BaseEvent> eventList = (List) events.get(type);
                    if (!(eventList == null || eventList.isEmpty())) {
                        for (BaseEvent event : eventList) {
                            jsonstr.object();
                            jsonstr.key(Keys.T).array();
                            jsonstr.value(event.subType);
                            jsonstr.endArray();
                            jsonstr.key("rd").value((long) event.reqDuration);
                            jsonstr.key("rc").value((long) event.reqCount);
                            CupidUtils.setJsonKeyValue(jsonstr, "ec", event.errCode);
                            CupidUtils.setJsonKeyValue(jsonstr, "em", event.errMsg);
                            CupidUtils.setJsonKeyValue(jsonstr, "ai", event.adInfo);
                            CupidUtils.setJsonKeyValue(jsonstr, "rid", event.requestId);
                            if (event.params != null) {
                                Set<String> keys = event.params.keySet();
                                if (keys != null) {
                                    for (String key : keys) {
                                        if (key.compareTo("p") != 0) {
                                            jsonstr.key(key).value(event.params.get(key));
                                        }
                                    }
                                }
                            }
                            jsonstr.endObject();
                        }
                        jsonstr.endArray();
                    }
                }
            }
            jsonstr.endObject();
            rtn = jsonstr.toString();
            return rtn;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildMonitorEventsValue(): json error:", e);
        }
    }

    private String buildStatisticsEventsValue(int resultId) {
        String rtn = "";
        try {
            JSONStringer jsonstr = new JSONStringer();
            jsonstr.object();
            BaseEvent baseEvent = (BaseEvent) this.baseEvents.get(Integer.valueOf(resultId));
            if (baseEvent == null) {
                return rtn;
            }
            buildBaseEventsValue(baseEvent, jsonstr);
            jsonstr.key(STATISTICS_EVENT_KEY).array();
            List<BaseEvent> events = (List) this.statisticsEvents.get(Integer.valueOf(resultId));
            if (events != null) {
                for (BaseEvent event : events) {
                    jsonstr.object();
                    jsonstr.key(Keys.T).array();
                    jsonstr.value(event.subType);
                    jsonstr.endArray();
                    CupidUtils.setJsonKeyValue(jsonstr, "ai", event.adInfo);
                    CupidUtils.setJsonKeyValue(jsonstr, WebConstants.PARAM_KEY_X, event.customInfo);
                    CupidUtils.setJsonKeyValue(jsonstr, "rid", event.requestId);
                    jsonstr.endObject();
                }
            }
            jsonstr.endArray();
            jsonstr.endObject();
            rtn = jsonstr.toString();
            return rtn;
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildStatisticsEventsValue(): json error:", e);
        }
    }

    private void buildBaseEventsValue(BaseEvent event, JSONStringer jsonstr) {
        try {
            CupidUtils.setJsonKeyValue(jsonstr, "u", event.uaaUserId);
            CupidUtils.setJsonKeyValue(jsonstr, "a", event.cupidUserId);
            CupidUtils.setJsonKeyValue(jsonstr, WebConstants.PARAM_KEY_Y, event.playerId);
            CupidUtils.setJsonKeyValue(jsonstr, "vv", event.appVersion);
            CupidUtils.setJsonKeyValue(jsonstr, WebConstants.AV, event.sdkVersion);
            CupidUtils.setJsonKeyValue(jsonstr, "e", event.videoEventId);
            CupidUtils.setJsonKeyValue(jsonstr, "mk", event.mobileKey);
            CupidUtils.setJsonKeyValue(jsonstr, com.gala.video.app.epg.ui.search.ad.Keys.NW, event.network);
            CupidUtils.setJsonKeyValue(jsonstr, "v", event.tvId);
            jsonstr.key("ol").value((long) event.isOffline);
            jsonstr.key("s").value(new Date().getTime() + this.debugTime);
            Map<String, String> pingbackExtras = event.pingbackExtras;
            if (pingbackExtras != null && !pingbackExtras.isEmpty()) {
                for (String key : pingbackExtras.keySet()) {
                    CupidUtils.setJsonKeyValue(jsonstr, key, (String) pingbackExtras.get(key));
                }
            }
        } catch (JSONException e) {
            Log.e("a71_ads_client", "buildBaseEventsValue(): json error:", e);
        } catch (NumberFormatException ex) {
            Log.d("a71_ads_client", "buildBaseEventsValue():", ex);
        }
    }

    private void initBasicEvents() {
        this.kBasicEvents.put("visit", new BasicInfo("v", "s", ""));
        this.kBasicEvents.put("inventory", new BasicInfo("i", "s", ""));
        this.kBasicEvents.put("success", new BasicInfo("m", "s", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_MIXER_HTTP_ERROR, new BasicInfo("m", "e", "701"));
        this.kBasicEvents.put(PingbackConstants.ACT_MIXER_TIMEOUT, new BasicInfo("m", "e", "702"));
        this.kBasicEvents.put(PingbackConstants.ACT_MIXER_PARSE_ERROR, new BasicInfo("m", "e", "704"));
        this.kBasicEvents.put("start", new BasicInfo("a", "st", ""));
        this.kBasicEvents.put("firstQuartile", new BasicInfo("a", "1q", ""));
        this.kBasicEvents.put("midpoint", new BasicInfo("a", "mid", ""));
        this.kBasicEvents.put("thirdQuartile", new BasicInfo("a", "3q", ""));
        this.kBasicEvents.put("complete", new BasicInfo("a", "sp", ""));
        this.kBasicEvents.put("skip", new BasicInfo("a", "sk", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_TRACKING_SUCCESS, new BasicInfo(Keys.T, "s", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_TRACKING_HTTP_ERROR, new BasicInfo(Keys.T, "e", "601"));
        this.kBasicEvents.put(PingbackConstants.ACT_TRACKING_TIMEOUT, new BasicInfo(Keys.T, "e", "602"));
        this.kBasicEvents.put(PingbackConstants.ACT_TRACKING_PARAM_ERROR, new BasicInfo(Keys.T, "e", "603"));
        this.kBasicEvents.put(PingbackConstants.ACT_ADX_TRACKING_SUCCESS, new BasicInfo(WebConstants.PARAM_KEY_X, "s", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_ADX_TRACKING_HTTP_ERROR, new BasicInfo(WebConstants.PARAM_KEY_X, "e", "1101"));
        this.kBasicEvents.put(PingbackConstants.ACT_ADX_TRACKING_TIMEOUT, new BasicInfo(WebConstants.PARAM_KEY_X, "e", "1102"));
        this.kBasicEvents.put(PingbackConstants.ACT_ADX_TRACKING_PARAM_ERROR, new BasicInfo(WebConstants.PARAM_KEY_X, "e", "1103"));
        this.kBasicEvents.put(PingbackConstants.ACT_THIRD_TRACKING_SUCCESS, new BasicInfo("", "s", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_THIRD_TRACKING_HTTP_ERROR, new BasicInfo("", "e", "1501"));
        this.kBasicEvents.put(PingbackConstants.ACT_THIRD_TRACKING_TIMEOUT, new BasicInfo("", "e", "1502"));
        this.kBasicEvents.put(PingbackConstants.ACT_THIRD_TRACKING_PARAM_ERROR, new BasicInfo("", "e", "1503"));
        this.kBasicEvents.put(PingbackConstants.ACT_AD_PLAY_DURATION, new BasicInfo("st", "vpd", ""));
        this.kBasicEvents.put(PingbackConstants.ACT_AD_AREA_CLICK, new BasicInfo("st", "clk", ""));
    }

    private BasicInfo getBasicInfo(String actType) {
        if (this.kBasicEvents == null || this.kBasicEvents.isEmpty()) {
            return null;
        }
        return (BasicInfo) this.kBasicEvents.get(actType);
    }

    public void setTriggerSendingSize(int size) {
        this.triggerSendingSize = size;
    }

    public static synchronized void removeCurrentTvId(String tvId) {
        synchronized (PingbackController.class) {
            visitSentList.remove(tvId);
        }
    }
}
