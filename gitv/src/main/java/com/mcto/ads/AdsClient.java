package com.mcto.ads;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import cn.com.mma.mobile.tracking.api.Countly;
import com.mcto.ads.constants.AdCard;
import com.mcto.ads.constants.AdEvent;
import com.mcto.ads.constants.ClickArea;
import com.mcto.ads.constants.EventProperty;
import com.mcto.ads.constants.Interaction;
import com.mcto.ads.constants.VVEvent;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.model.AdInfo;
import com.mcto.ads.internal.model.AdSourceHandler;
import com.mcto.ads.internal.model.AdsScheduleBundle;
import com.mcto.ads.internal.model.FutureSlotInfo;
import com.mcto.ads.internal.model.SlotInfo;
import com.mcto.ads.internal.net.HttpGetAsyncClient;
import com.mcto.ads.internal.net.IAdsCallback;
import com.mcto.ads.internal.net.PingbackConstants;
import com.mcto.ads.internal.net.PingbackController;
import com.mcto.ads.internal.net.SendFlag;
import com.mcto.ads.internal.net.TrackingConstants;
import com.mcto.ads.internal.net.TrackingController;
import com.mcto.ads.internal.net.TrackingParty;
import com.mcto.ads.internal.persist.DBAsyncCleaner;
import com.mcto.ads.internal.persist.StorageManager;
import com.mcto.ads.internal.thirdparty.ThirdPartyConfig;
import com.mcto.ads.internal.thirdparty.ThirdPartyConstants;
import com.mcto.ads.internal.thirdparty.TrackingProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class AdsClient implements IAdsSDK, IAdsCallback {
    private static final int DEFAULT_DEBUG_TIME = 0;
    private static final int DEFAULT_REQUEST_TIMEOUT = 20000;
    private static final int DEFAULT_THIRD_CONFIG_RESULT = 0;
    private static final int EMPTY_RESULT_ID = 0;
    private static final int INVALID_AD_ID = -1;
    private static final int INVALID_RESULT_ID = -1;
    private static final int MAX_FEEDBACK_LOG_NUM = 10;
    private static final String NL = "\n";
    public static final String SDK_VERSION = "3.11.004";
    private static final int THOUS_MILLIS = 1000;
    private static Context _context;
    private static boolean _enableThirdSdk = true;
    private static Queue<String> feedbackLogs = new LinkedList();
    private static int result_id_seed = 0;
    private AdsScheduleBundle adsScheduleBundle;
    private Map<Integer, CupidContext> cupidContextMap = new HashMap();
    private CupidGlobal cupidGlobal = new CupidGlobal();
    private HashMap<String, HashMap<Integer, Long>> frequentEvents = new HashMap();
    private PingbackController pingbackController = new PingbackController();
    private HashMap<Integer, AdsScheduleBundle> resultsMap = new HashMap();
    private HashMap<Integer, String> serverDatas = new HashMap();
    private StorageManager storageManager;
    private HashMap<Integer, ThirdPartyConfig> thirdPartyConfigMap = new HashMap();
    private ArrayList<Integer> triggeredSlots = new ArrayList();

    public AdsClient(String uaaUserId, String appVersion, String cupidUserId, String mobileKey) {
        Log.d("a71_ads_client", "AdsClient(): uaaUserId: " + uaaUserId + ", appVersion: " + appVersion + ", cupidUserId: " + cupidUserId + ", mobileKey: " + mobileKey);
        this.cupidGlobal.setUaaUserId(uaaUserId);
        this.cupidGlobal.setCupidUserId(cupidUserId);
        this.cupidGlobal.setAppVersion(appVersion);
        this.cupidGlobal.setSdkVersion(SDK_VERSION);
        this.cupidGlobal.setMobileKey(mobileKey);
        this.storageManager = new StorageManager();
    }

    public static void initialise(Context context) {
        _context = context;
        initialise(context, true);
    }

    public static void initialise(Context context, boolean enableThirdSDK) {
        _context = context;
        if (context == null) {
            Log.d("a71_ads_client", "error: null context.");
        }
        _enableThirdSdk = enableThirdSDK;
        try {
            if (_enableThirdSdk) {
                Countly.sharedInstance().init(context, ThirdPartyConstants.MMA_CONFIG_URL);
            }
        } catch (Exception ex) {
            Log.e("a71_ads_client", "mma init error", ex);
        }
    }

    public static void setTvDomain(String tvDomain) {
        Log.e("a71_ads_client", "setTvDomain(): " + tvDomain);
        if (tvDomain != null && !tvDomain.equals("")) {
            TrackingConstants.CUPID_TRACKING_URL = "http://t7z.cupid." + tvDomain + "/track2?";
            TrackingConstants.ADX_TRACKING_URL = "http://t7z.cupid." + tvDomain + "/etx?";
            PingbackConstants.PINGBACK_URL = "http://msga." + tvDomain + "/scp2.gif";
        }
    }

    public void onRequestMobileServer() {
        Log.d("a71_ads_client", "onRequestMobileServer():");
    }

    public int onHandleCupidInteractionData(String interactionData) {
        if (!CupidUtils.isValidStr(interactionData)) {
            return -1;
        }
        int resultId = -1;
        try {
            JSONObject interactionDataObj = new JSONObject(interactionData);
            String serverData = interactionDataObj.optString(Interaction.KEY_SERVER_DATA);
            String tvId = interactionDataObj.optString("tvId");
            String playerId = interactionDataObj.optString("playerId");
            String adZoneId = interactionDataObj.optString("adZoneId");
            String timeSlice = interactionDataObj.optString(Interaction.KEY_TIME_SLICE);
            return onRequestMobileServerSucceeded(serverData, tvId, playerId, interactionDataObj.optBoolean(Interaction.KEY_FROM_CACHE), adZoneId, timeSlice, interactionDataObj.optLong(Interaction.KEY_DEBUG_TIME));
        } catch (Exception ex) {
            Log.d("a71_ads_client", "onHandleCupidInteractionData(): ", ex);
            return resultId;
        }
    }

    public void onRequestMobileServerFailed() {
        Log.d("a71_ads_client", "onRequestMobileServerFailed():");
    }

    public int onRequestMobileServerSucceededWithAdData(String jsonBundle, String tvId, String playerId, boolean fromCache) throws JSONException {
        return onRequestMobileServerSucceeded(jsonBundle, tvId, playerId, fromCache, "", "", 0);
    }

    public int onRequestMobileServerSucceededWithAdData(String jsonBundle, String tvId, String playerId) throws JSONException {
        return onRequestMobileServerSucceeded(jsonBundle, tvId, playerId, false, "", "", 0);
    }

    private int onRequestMobileServerSucceeded(String jsonBundle, String tvId, String playerId, boolean fromCache, String adZoneId, String timeSlice, long debugTime) throws JSONException {
        Exception ex;
        setFeedbackLog(jsonBundle);
        Log.d("a71_ads_client", "onRequestMobileServerSucceeded(): tvId: " + tvId + ", playerId: " + playerId + ", from cache: " + fromCache + ", json: " + jsonBundle);
        if (!CupidUtils.isValidStr(jsonBundle)) {
            return -1;
        }
        int i = result_id_seed + 1;
        result_id_seed = i;
        int resultId = CupidUtils.generateResultId(i);
        CupidContext cupidContext = new CupidContext();
        cupidContext.setSystemContext(_context);
        cupidContext.setTvId(tvId);
        cupidContext.setPlayerId(playerId);
        cupidContext.setFromCache(fromCache);
        if (CupidUtils.isValidStr(adZoneId) && CupidUtils.isValidStr(timeSlice)) {
            cupidContext.setAdZoneId(adZoneId);
            cupidContext.setTimeSlice(timeSlice);
            cupidContext.setIsTargeted(true);
        }
        this.serverDatas.put(Integer.valueOf(resultId), jsonBundle);
        Boolean parsingError = Boolean.valueOf(false);
        AdsScheduleBundle adsScheduleBundle = null;
        try {
            AdsScheduleBundle adsScheduleBundle2 = new AdsScheduleBundle(resultId, jsonBundle, cupidContext);
            if (0 != debugTime) {
                try {
                    cupidContext.setDebugTime(debugTime);
                } catch (Exception e) {
                    ex = e;
                    adsScheduleBundle = adsScheduleBundle2;
                    parsingError = Boolean.valueOf(true);
                    resultId = -1;
                    Log.d("a71_ads_client", "onRequestMobileServerSucceeded(): addMixerEvent: parse error, ", ex);
                    if (!parsingError.booleanValue()) {
                        return resultId;
                    }
                    handleParseResults(resultId, adsScheduleBundle, cupidContext);
                    return resultId;
                }
            }
            Log.d("a71_ads_client", "onRequestMobileServerSucceeded(): reqUrl: " + adsScheduleBundle2.getReqUrl());
            adsScheduleBundle = adsScheduleBundle2;
        } catch (Exception e2) {
            ex = e2;
            parsingError = Boolean.valueOf(true);
            resultId = -1;
            Log.d("a71_ads_client", "onRequestMobileServerSucceeded(): addMixerEvent: parse error, ", ex);
            if (!parsingError.booleanValue()) {
                return resultId;
            }
            handleParseResults(resultId, adsScheduleBundle, cupidContext);
            return resultId;
        }
        if (!parsingError.booleanValue()) {
            return resultId;
        }
        handleParseResults(resultId, adsScheduleBundle, cupidContext);
        return resultId;
    }

    private synchronized void handleParseResults(int resultId, AdsScheduleBundle adsScheduleBundle, CupidContext cupidContext) {
        this.adsScheduleBundle = adsScheduleBundle;
        this.resultsMap.put(Integer.valueOf(resultId), adsScheduleBundle);
        this.cupidContextMap.put(Integer.valueOf(resultId), cupidContext);
        this.pingbackController.assembleBaseEvent(resultId, this.cupidGlobal, cupidContext);
        if (!(adsScheduleBundle.isOldInterstitials() || cupidContext.isFromCache() || cupidContext.isTargeted())) {
            this.pingbackController.sendVisitPingback(resultId, "visit", cupidContext);
        }
        Map cupidExtras = adsScheduleBundle.getCupidExtras();
        if (!cupidExtras.isEmpty()) {
            this.thirdPartyConfigMap.put(Integer.valueOf(resultId), new ThirdPartyConfig(cupidExtras));
        }
        List<String> reqTemplateTypes = cupidContext.getReqTemplateTypes();
        if (reqTemplateTypes.contains("exit")) {
            this.pingbackController.setTriggerSendingSize(1);
        } else {
            this.pingbackController.setTriggerSendingSize(1);
        }
        if (!cupidContext.isFromCache()) {
            sendEmptyTrackings(resultId, adsScheduleBundle, cupidContext);
        }
        if (_context != null && reqTemplateTypes.contains("native_video")) {
            this.storageManager.initialize(_context);
            loadNativeVideoItems(adsScheduleBundle);
        }
    }

    private void loadNativeVideoItems(AdsScheduleBundle adsScheduleBundle) {
        List<SlotInfo> slots = adsScheduleBundle.getSlotInfoList();
        for (int i = 0; i < slots.size(); i++) {
            List<AdInfo> ads = ((SlotInfo) slots.get(i)).getPlayableAds();
            for (int j = 0; j < ads.size(); j++) {
                AdInfo ad = (AdInfo) ads.get(j);
                ad.updateNativeVideoItem(this.storageManager.getNativeVideoItem(ad.getIdentifier()));
            }
        }
        new DBAsyncCleaner(this.storageManager).execute(new Void[0]);
    }

    public void onAdEvent(int adId, AdEvent adEvent, Map<String, Object> properties) {
        AdInfo ad = getAdInfoByAdId(adId);
        if (ad != null) {
            Log.d("a71_ads_client", "onAdEvent(): ad id: " + adId + ", event: " + adEvent.value() + ", send record: " + ad.getSendRecord());
            ad.addEventProperties(properties);
            CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId)));
            if (AdEvent.AD_EVENT_IMPRESSION == adEvent) {
                handleAdTrackingEvent(adId, "impression", 128);
            }
            if (AdEvent.AD_EVENT_START == adEvent) {
                handleAdTrackingEvent(adId, "start", 2);
                handleAdPingbackEvent(adId, "start", SendFlag.FLAG_KEY_PINGBACK_ST);
                saveAdEventSendRecord(ad);
            }
            if (AdEvent.AD_EVENT_STOP == adEvent) {
                this.pingbackController.addStatisticsPingback(PingbackConstants.ACT_AD_PLAY_DURATION, ad, cupidContext);
                if (ad.getProgress() >= ad.getDuration() - 1000) {
                    handleAdTrackingEvent(adId, "complete", 32);
                    handleAdPingbackEvent(adId, "complete", SendFlag.FLAG_KEY_PINGBACK_SP);
                }
                handleAdPingbackEvent(adId, "stop", 0);
                saveAdEventSendRecord(ad);
            }
            if (AdEvent.AD_EVENT_CLICK == adEvent) {
                onAdClickedWithProperties(ad, properties, cupidContext);
            }
        }
    }

    private void onAdClickedWithProperties(AdInfo ad, Map<String, Object> properties, CupidContext cupidContext) {
        int adId = ad.getAdId();
        String templateType = ad.getTemplateType();
        ClickArea clickAreaObj = properties != null ? properties.get(EventProperty.EVENT_PROP_KEY_CLICK_AREA.value()) : null;
        if (clickAreaObj == null || ClickArea.AD_CLICK_AREA_BUTTON == clickAreaObj || ClickArea.AD_CLICK_AREA_EXT_BUTTON == clickAreaObj) {
            onAdClicked(adId);
        }
        if (ClickArea.AD_CLICK_AREA_ACCOUNT == clickAreaObj || ClickArea.AD_CLICK_AREA_PORTRAIT == clickAreaObj) {
            if (templateType.equals(CupidAd.TEMPLATE_TYPE_NATIVE_MULTI_IMAGE) || templateType.equals(CupidAd.TEMPLATE_TYPIE_NATIVE_IMAGE) || templateType.equals(CupidAd.TEMPLATE_TYPE_HEADLINE_NATIVE_IMAGE)) {
                onAdClicked(adId);
            }
            if (templateType.equals("native_video")) {
                this.pingbackController.addStatisticsPingback(PingbackConstants.ACT_AD_AREA_CLICK, ad, cupidContext);
            }
        }
        if ((ClickArea.AD_CLICK_AREA_COMMENT == clickAreaObj || ClickArea.AD_CLICK_AREA_GRAPHIC == clickAreaObj) && templateType.equals("native_video")) {
            ad.setPlayType(1);
            if (!ad.isTrackingPingbackSent(SendFlag.FLAG_KEY_PINGBACK_ST)) {
                ad.addPlayCount(1);
            }
            handleAdTrackingEvent(adId, TrackingConstants.TRACKING_EVENT_TRUEVIEW, 64);
            if (!ad.isTrackingPingbackSent(SendFlag.FLAG_KEY_PINGBACK_ST)) {
                ad.addPlayCount(-1);
            }
            saveAdEventSendRecord(ad);
            this.pingbackController.addStatisticsPingback(PingbackConstants.ACT_AD_AREA_CLICK, ad, cupidContext);
        }
        if (ClickArea.AD_CLICK_AREA_GRAPHIC == clickAreaObj && !templateType.equals("native_video")) {
            onAdClicked(adId);
        }
        if (ClickArea.AD_CLICK_AREA_COMMENT != clickAreaObj) {
            return;
        }
        if (templateType.equals(CupidAd.TEMPLATE_TYPE_NATIVE_MULTI_IMAGE) || templateType.equals(CupidAd.TEMPLATE_TYPIE_NATIVE_IMAGE) || templateType.equals(CupidAd.TEMPLATE_TYPE_HEADLINE_NATIVE_IMAGE)) {
            this.pingbackController.addStatisticsPingback(PingbackConstants.ACT_AD_AREA_CLICK, ad, cupidContext);
        }
    }

    private void saveAdEventSendRecord(AdInfo adInfo) {
        if (adInfo != null && adInfo.getTemplateType().equals("native_video")) {
            ContentValues contentValues = adInfo.getNativeVideoItem();
            if (this.storageManager.getNativeVideoItem(adInfo.getIdentifier()).isEmpty()) {
                this.storageManager.insertNativeVideoItem(contentValues);
            } else {
                this.storageManager.updateNativeVideoItem(adInfo.getIdentifier(), contentValues);
            }
        }
    }

    public void onAdStarted(int adId) {
        Log.d("a71_ads_client", "onAdStarted(): adId: " + adId);
        handleAdTrackingEvent(adId, "impression", 128);
        handleSlotSequenceId(adId);
        SlotInfo slotInfo = getSlotInfo(CupidUtils.getSlotIdByAdId(adId));
        if (slotInfo != null && slotInfo.isRollType()) {
            handleAdTrackingEvent(adId, "start", 2);
            handleAdPingbackEvent(adId, "start", SendFlag.FLAG_KEY_PINGBACK_ST);
        }
    }

    private void handleAdTrackingEvent(int adId, String trackingEvent, int trackingFlag) {
        AdInfo adInfo = getAdInfoByAdId(adId);
        if (adInfo != null) {
            int resultId = CupidUtils.getResultIdByAdId(adId);
            if (!isFromCache(resultId)) {
                if (!adInfo.isTrackingPingbackSent(trackingFlag)) {
                    Log.d("a71_ads_client", "handleAdTrackingEvent(): adId: " + adId + ", trackingEvent: " + trackingEvent);
                    adInfo.setTrackingPingbackFlag(trackingFlag);
                    sendTrackings(adId, trackingEvent);
                }
                if ("impression".equals(trackingEvent)) {
                    handleEmptyTrackings("start", adInfo.getTimePosition(), getSlotInfo(CupidUtils.getSlotIdByAdId(adId)));
                    this.pingbackController.sendInventoryPingback(resultId, PingbackConstants.ACT_INVENTORY, (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId)));
                }
            }
        }
    }

    public void updateAdProgress(int adId, int progress) {
        Log.d("a71_ads_client", "updateAdProgress(): adId: " + adId + ", progress: " + progress);
        AdInfo adInfo = getAdInfoByAdId(adId);
        if (adInfo != null) {
            int duration = adInfo.getDuration();
            if (progress >= 0 && progress <= duration) {
                adInfo.setProgress(progress);
                int billingPoint = adInfo.getBillingPoint();
                if (billingPoint >= 0 && progress >= billingPoint - 1000) {
                    CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId)));
                    if (!(cupidContext == null || cupidContext.isTargeted())) {
                        handleAdTrackingEvent(adId, TrackingConstants.TRACKING_EVENT_TRUEVIEW, 64);
                    }
                }
                if (progress > duration / 4) {
                    handleAdTrackingEvent(adId, "firstQuartile", 4);
                    handleAdPingbackEvent(adId, "firstQuartile", SendFlag.FLAG_KEY_PINGBACK_1Q);
                }
                if (progress > duration / 2) {
                    handleAdTrackingEvent(adId, "midpoint", 8);
                    handleAdPingbackEvent(adId, "midpoint", SendFlag.FLAG_KEY_PINGBACK_MID);
                }
                if (progress > (duration / 4) * 3) {
                    handleAdTrackingEvent(adId, "thirdQuartile", 16);
                    handleAdPingbackEvent(adId, "thirdQuartile", 524288);
                }
            }
        }
    }

    public synchronized void updateVVProgress(int progress) {
        Log.d("a71_ads_client", "updateVVProgress(): progress: " + progress);
        this.cupidGlobal.setVVProgress(progress);
        for (AdsScheduleBundle adsScheduleBundle : this.resultsMap.values()) {
            if (adsScheduleBundle != null) {
                for (SlotInfo slotInfo : adsScheduleBundle.getSlotInfoList()) {
                    if (slotInfo.isRuntimeSlot() && !this.triggeredSlots.contains(Integer.valueOf(slotInfo.getSlotId())) && progress >= slotInfo.getStartTime() && progress <= slotInfo.getStartTime() + 20000) {
                        this.triggeredSlots.add(Integer.valueOf(slotInfo.getSlotId()));
                        handleEmptyTrackings("start", null, slotInfo);
                    }
                }
            }
        }
    }

    private void handleAdPingbackEvent(int adId, String actType, int pingbackFlag) {
        AdInfo ad = getAdInfoByAdId(adId);
        if (ad != null) {
            if ("stop".equals(actType)) {
                this.pingbackController.flushCupidPingback();
                return;
            }
            boolean isTarget = ((CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId)))).isTargeted();
            if (actType.equals("start")) {
                if (isTarget) {
                    ad.setPlayType(1);
                }
                ad.addPlayCount(1);
                ad.resetPingbackFlags();
            }
            if (!ad.isTrackingPingbackSent(pingbackFlag)) {
                Log.d("a71_ads_client", "handleAdPingbackEvent(): adId: " + adId + ", actType: " + actType);
                ad.setTrackingPingbackFlag(pingbackFlag);
                sendAdEventPingback(adId, actType);
            }
        }
    }

    private void sendAdEventPingback(int adId, String actType) {
        CupidContext context = (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId)));
        if (context != null) {
            AdInfo adInfo = getAdInfoByAdId(adId);
            if (adInfo != null) {
                this.pingbackController.addAdEvent(actType, adInfo, context);
            }
        }
    }

    public void onAdFirstQuartile(int adId) {
        Log.d("a71_ads_client", "onAdFirstQuartile(): adId: " + adId);
        handleAdTrackingEvent(adId, "firstQuartile", 4);
        handleAdPingbackEvent(adId, "firstQuartile", SendFlag.FLAG_KEY_PINGBACK_1Q);
    }

    public void onAdSecondQuartile(int adId) {
        Log.d("a71_ads_client", "onAdSecondQuartile(): adId: " + adId);
        handleAdTrackingEvent(adId, "midpoint", 8);
        handleAdPingbackEvent(adId, "midpoint", SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    public void onAdThirdQuartile(int adId) {
        Log.d("a71_ads_client", "onAdThirdQuartile(): adId: " + adId);
        handleAdTrackingEvent(adId, "thirdQuartile", 16);
        handleAdPingbackEvent(adId, "thirdQuartile", 524288);
    }

    public void onAdCompleted(int adId) {
        Log.d("a71_ads_client", "onAdCompleted(): adId: " + adId);
        handleAdTrackingEvent(adId, "complete", 32);
        handleAdPingbackEvent(adId, "complete", SendFlag.FLAG_KEY_PINGBACK_SP);
        SlotInfo slotInfo = getSlotInfo(CupidUtils.getSlotIdByAdId(adId));
        if (slotInfo != null) {
            AdInfo adInfo = getAdInfoByAdId(adId);
            if (adInfo != null && slotInfo.isLastAd(adInfo)) {
                handleEmptyTrackings("complete", adInfo.getTimePosition(), slotInfo);
                this.pingbackController.flushCupidPingback();
            }
        }
    }

    public void onAdClosed(int adId) {
        Log.d("a71_ads_client", "onAdClosed(): adId: " + adId);
        this.pingbackController.flushCupidPingback();
    }

    public void onAdSkipped(int adId) {
        Log.d("a71_ads_client", "onAdSkipped(): adId: " + adId);
        handleAdTrackingEvent(adId, "skip", 0);
        handleAdPingbackEvent(adId, "skip", 0);
    }

    public void onAdError(int adId) {
        Log.d("a71_ads_client", "onAdError(): adId: " + adId);
    }

    public void onAdClicked(int adId) {
        Log.d("a71_ads_client", "onAdClicked(): adId: " + adId);
        AdInfo adInfo = getAdInfoByAdId(adId);
        if (adInfo != null) {
            long now = System.currentTimeMillis();
            long last = lastTimeOfEvent("click", adId);
            addFrequencyAdEvent("click", adId, now);
            if (!isNear(Long.valueOf(now), Long.valueOf(last))) {
                sendTrackings(adId, "click");
                String templateType = adInfo.getTemplateType();
                if (templateType.equals("mobile_flow") || templateType.equals("mobile_flow_pair")) {
                    handleAdTrackingEvent(adId, TrackingConstants.TRACKING_EVENT_TRUEVIEW, 0);
                }
            }
        }
    }

    private JSONObject generateObjectByEvent(String event, AdInfo adInfo) throws JSONException {
        JSONObject object = new JSONObject();
        if (adInfo != null) {
            CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adInfo.getAdId())));
            if (cupidContext == null) {
                Log.d("a71_ads_client", "generateObjectByEvent(): context is null");
            } else {
                object.put("cupid", CupidUtils.generateJsonArray(adInfo.getCupidTrackingUrls(event, this.cupidGlobal, cupidContext)));
                object.put("adx", CupidUtils.generateJsonArray(adInfo.getAdxTrackingUrls(event, this.cupidGlobal, cupidContext)));
                object.put("thirdParty", CupidUtils.generateJsonArray(adInfo.getThirdPartyTrackings(event, cupidContext, false)));
            }
        }
        return object;
    }

    private int generateThirdPartyConfig(int resultId) throws JSONException {
        int rtn = 0;
        ThirdPartyConfig thirdPartyConfig = getThirdPartyConfigByResultId(resultId);
        if (thirdPartyConfig == null) {
            return 0;
        }
        Map<TrackingProvider, Boolean> mma = thirdPartyConfig.enableMmaConfig;
        if (!mma.isEmpty()) {
            for (TrackingProvider provider : mma.keySet()) {
                if (provider.equals(TrackingProvider.ADMASTER)) {
                    rtn |= 1;
                } else if (provider.equals(TrackingProvider.MIAOZHEN)) {
                    rtn |= 2;
                } else if (provider.equals(TrackingProvider.NIELSEN)) {
                    rtn |= 4;
                } else if (provider.equals(TrackingProvider.CTR)) {
                    rtn |= 8;
                }
            }
        }
        return rtn;
    }

    private String getAdTunnelData(AdInfo adInfo) {
        if (adInfo == null) {
            return null;
        }
        int resultId = CupidUtils.getResultIdByAdId(adInfo.getAdId());
        long debugTime = 0;
        CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
        if (cupidContext != null) {
            debugTime = cupidContext.getDebugTime();
        }
        JSONStringer jsonStr = new JSONStringer();
        try {
            jsonStr.object();
            JSONObject envObj = new JSONObject();
            envObj.put(Interaction.KEY_DEBUG_TIME, String.valueOf(debugTime));
            envObj.put("mmaSwitch", generateThirdPartyConfig(resultId));
            jsonStr.key("env").value(envObj);
            jsonStr.key("click").value(generateObjectByEvent("click", adInfo));
            jsonStr.key(TrackingConstants.TRACKING_EVENT_DOWNLOAD_START).value(generateObjectByEvent(TrackingConstants.TRACKING_EVENT_DOWNLOAD_START, adInfo));
            jsonStr.key(TrackingConstants.TRACKING_EVENT_DOWNLOADED).value(generateObjectByEvent(TrackingConstants.TRACKING_EVENT_DOWNLOADED, adInfo));
            jsonStr.endObject();
            return jsonStr.toString();
        } catch (JSONException e) {
            Log.d("a71_ads_client", "getAdTunnelData(): exception:" + e.getMessage());
            return null;
        }
    }

    public static void onAppDownloadStart(String json) {
        TrackingController.onEventCommon(json, TrackingConstants.TRACKING_EVENT_DOWNLOAD_START, _enableThirdSdk);
    }

    public static void onAppDownloaded(String json) {
        TrackingController.onEventCommon(json, TrackingConstants.TRACKING_EVENT_DOWNLOADED, _enableThirdSdk);
    }

    public static void onAdClicked(String json) {
        TrackingController.onEventCommon(json, "click", _enableThirdSdk);
    }

    public CupidAd getCupidAdByQipuId(int qipuId) {
        return getCupidAd(0, qipuId, null, false);
    }

    public CupidAd getCupidAdByQipuId(int resultId, int qipuId) {
        return getCupidAd(resultId, qipuId, null, false);
    }

    public CupidAd getCupidAdByQipuIdAndAdZoneId(int qipuId, String adZoneId) {
        return getCupidAd(0, qipuId, adZoneId, true);
    }

    public CupidAd getCupidAdByQipuIdAndAdZoneId(int resultId, int qipuId, String adZoneId) {
        return getCupidAd(resultId, qipuId, adZoneId, true);
    }

    public CupidAd getCupidAdByAdZoneIdAndTimeSlice(int resultId, String adZoneId, String timeSlice) {
        Log.d("a71_ads_client", "getCupidAdByAdZoneIdAndTimeSlice: resultId: " + resultId + ", adZoneId: " + adZoneId + ", timeSlice: " + timeSlice);
        if (resultId == 0 || !CupidUtils.isValidStr(adZoneId) || !CupidUtils.isValidStr(timeSlice)) {
            return null;
        }
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle == null || cupidContext == null) {
            return null;
        }
        List<SlotInfo> slotInfoList = adsScheduleBundle.getSlotInfoList();
        if (slotInfoList == null) {
            return null;
        }
        for (SlotInfo slotInfo : slotInfoList) {
            if (adZoneId.equals(slotInfo.getAdZoneId())) {
                List<AdInfo> adInfoList = slotInfo.getPlayableAds();
                if (adInfoList != null) {
                    for (AdInfo adInfo : adInfoList) {
                        if (timeSlice.equals(adInfo.getTimePosition())) {
                            return new CupidAd(adInfo, getAdTunnelData(adInfo), cupidContext);
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.mcto.ads.CupidAd getCupidAd(int r17, int r18, java.lang.String r19, boolean r20) {
        /*
        r16 = this;
        r13 = "a71_ads_client";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "getCupidAd(): resultId: ";
        r14 = r14.append(r15);
        r0 = r17;
        r14 = r14.append(r0);
        r15 = ", qipuId: ";
        r14 = r14.append(r15);
        r0 = r18;
        r14 = r14.append(r0);
        r15 = ", adZoneId: ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r15 = ", needAdZoneId: ";
        r14 = r14.append(r15);
        r0 = r20;
        r14 = r14.append(r0);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        if (r20 == 0) goto L_0x0054;
    L_0x0045:
        if (r19 == 0) goto L_0x0052;
    L_0x0047:
        r13 = "";
        r0 = r19;
        r13 = r0.equals(r13);
        if (r13 == 0) goto L_0x0054;
    L_0x0052:
        r13 = 0;
    L_0x0053:
        return r13;
    L_0x0054:
        r4 = 0;
        if (r17 != 0) goto L_0x005f;
    L_0x0057:
        r0 = r16;
        r4 = r0.adsScheduleBundle;
    L_0x005b:
        if (r4 != 0) goto L_0x006e;
    L_0x005d:
        r13 = 0;
        goto L_0x0053;
    L_0x005f:
        r0 = r16;
        r13 = r0.resultsMap;
        r14 = java.lang.Integer.valueOf(r17);
        r4 = r13.get(r14);
        r4 = (com.mcto.ads.internal.model.AdsScheduleBundle) r4;
        goto L_0x005b;
    L_0x006e:
        r11 = r4.getSlotInfoList();
        if (r11 == 0) goto L_0x007a;
    L_0x0074:
        r13 = r11.isEmpty();
        if (r13 == 0) goto L_0x007c;
    L_0x007a:
        r13 = 0;
        goto L_0x0053;
    L_0x007c:
        r12 = r11.size();
        r7 = 0;
    L_0x0081:
        if (r7 >= r12) goto L_0x00e2;
    L_0x0083:
        r10 = r11.get(r7);
        r10 = (com.mcto.ads.internal.model.SlotInfo) r10;
        if (r20 == 0) goto L_0x009a;
    L_0x008b:
        r13 = r10.getAdZoneId();
        r0 = r19;
        r13 = r0.equals(r13);
        if (r13 != 0) goto L_0x009a;
    L_0x0097:
        r7 = r7 + 1;
        goto L_0x0081;
    L_0x009a:
        r2 = r10.getPlayableAds();
        r3 = r2.size();
        r8 = 0;
    L_0x00a3:
        if (r8 >= r3) goto L_0x0097;
    L_0x00a5:
        r1 = r2.get(r8);
        r1 = (com.mcto.ads.internal.model.AdInfo) r1;
        r5 = r1.getCreativeObject();
        r13 = "qipuid";
        r9 = r5.get(r13);
        r9 = (java.lang.String) r9;
        if (r9 == 0) goto L_0x00df;
    L_0x00ba:
        r13 = java.lang.String.valueOf(r18);
        r13 = r9.equals(r13);
        if (r13 == 0) goto L_0x00df;
    L_0x00c4:
        r0 = r16;
        r13 = r0.cupidContextMap;
        r14 = java.lang.Integer.valueOf(r17);
        r6 = r13.get(r14);
        r6 = (com.mcto.ads.internal.common.CupidContext) r6;
        r13 = new com.mcto.ads.CupidAd;
        r0 = r16;
        r14 = r0.getAdTunnelData(r1);
        r13.<init>(r1, r14, r6);
        goto L_0x0053;
    L_0x00df:
        r8 = r8 + 1;
        goto L_0x00a3;
    L_0x00e2:
        r13 = 0;
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mcto.ads.AdsClient.getCupidAd(int, int, java.lang.String, boolean):com.mcto.ads.CupidAd");
    }

    public CupidAd getTargetedCupidAd(int resultId) {
        CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
        if (cupidContext == null) {
            return null;
        }
        String adZoneId = cupidContext.getAdZoneId();
        String timeSlice = cupidContext.getTimeSlice();
        Log.d("a71_ads_client", "getTargetedCupidAd(): resultId: " + resultId + ", ad zone id: " + adZoneId + ", time slice: " + timeSlice);
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle == null) {
            return null;
        }
        for (SlotInfo slotInfo : adsScheduleBundle.getSlotInfoList()) {
            if (adZoneId.equals(slotInfo.getAdZoneId())) {
                for (AdInfo adInfo : slotInfo.getPlayableAds()) {
                    if (timeSlice.equals(adInfo.getTimePosition())) {
                        return new CupidAd(adInfo, getAdTunnelData(adInfo), cupidContext);
                    }
                }
                continue;
            }
        }
        return null;
    }

    public String getCupidInteractionData(int resultId, int adId) {
        String interaction = "";
        AdInfo ad = getAdInfoByAdId(adId);
        SlotInfo slot = getSlotInfo(CupidUtils.getSlotIdByAdId(adId));
        if (ad == null || slot == null) {
            return interaction;
        }
        JSONStringer jsonstr = new JSONStringer();
        try {
            jsonstr.object();
            jsonstr.key(Interaction.KEY_TIME_SLICE).value(ad.getTimePosition());
            String serverData = (String) this.serverDatas.get(Integer.valueOf(resultId));
            if (CupidUtils.isValidStr(serverData)) {
                jsonstr.key(Interaction.KEY_SERVER_DATA).value(serverData);
            }
            CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
            if (cupidContext != null) {
                jsonstr.key(Interaction.KEY_FROM_CACHE).value(cupidContext.isFromCache());
                jsonstr.key("tvId").value(cupidContext.getTvId());
                jsonstr.key("playerId").value(cupidContext.getPlayerId());
                jsonstr.key(Interaction.KEY_DEBUG_TIME).value(cupidContext.getDebugTime());
            }
            String adZoneId = slot.getAdZoneId();
            if (CupidUtils.isValidStr(adZoneId)) {
                Log.d("a71_ads_client", "getCupidInteractionData(): ad zone id: " + adZoneId);
                jsonstr.key("adZoneId").value(adZoneId);
            }
            jsonstr.endObject();
            interaction = jsonstr.toString();
        } catch (JSONException e) {
        }
        return interaction;
    }

    public void onAdLike(int adId, int duration) {
    }

    public void onAdUnlike(int adId, int duration) {
    }

    public static void onVVEvent(String tvId, VVEvent event) {
        if (VVEvent.COMPLETE == event) {
            PingbackController.removeCurrentTvId(tvId);
        }
    }

    public void onAdCardShow(int resultId, AdCard adCard) {
        Map<String, Object> properties = new HashMap();
        properties.put("oldForm", Boolean.valueOf(true));
        onAdCardShowWithProperties(resultId, adCard, properties);
    }

    public void onAdCardShowWithProperties(int resultId, AdCard adCard, Map<String, Object> properties) {
        Log.d("a71_ads_client", "onAdCardShowWithProperties(): resultId: " + resultId + ", adCard: " + adCard + ", properties:" + (properties != null ? properties.toString() : ""));
        if (!isFromCache(resultId)) {
            AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
            if (adsScheduleBundle != null) {
                this.pingbackController.sendInventoryPingback(resultId, PingbackConstants.ACT_INVENTORY, (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId)));
                String adZoneId = null;
                String timeSlice = null;
                boolean oldForm = false;
                if (properties != null) {
                    adZoneId = (String) properties.get(EventProperty.EVENT_PROP_KEY_AD_ZONE_ID.value());
                    timeSlice = (String) properties.get(EventProperty.EVENT_PROP_KEY_TIME_SLICE.value());
                    if (properties.containsKey("oldForm")) {
                        oldForm = ((Boolean) properties.get("oldForm")).booleanValue();
                    }
                }
                for (SlotInfo slotInfo : adsScheduleBundle.getSlotInfoList()) {
                    if ((adZoneId != null && adZoneId.equals(slotInfo.getAdZoneId())) || oldForm) {
                        handleEmptyTrackings("impression", timeSlice, slotInfo);
                    }
                }
            }
        }
    }

    public void onMobileFlowShow(int resultId) {
        Log.d("a71_ads_client", "onMobileFlowShow(): resultId: " + resultId);
        if (!isFromCache(resultId)) {
            AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
            if (adsScheduleBundle != null) {
                this.pingbackController.sendInventoryPingback(resultId, PingbackConstants.ACT_INVENTORY, (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId)));
                for (SlotInfo slotInfo : adsScheduleBundle.getSlotInfoList()) {
                    handleEmptyTrackings("impression", null, slotInfo);
                }
            }
        }
    }

    public void setSdkStatus(Map<String, Object> status) {
        Log.d("a71_ads_client", "setSdkStatus(): ");
        CupidUtils.setSdkStatus(status);
    }

    public static String getSDKVersion() {
        return SDK_VERSION;
    }

    public static String getSDKVersionStatic() {
        return SDK_VERSION;
    }

    public String getFinalUrl() {
        if (this.adsScheduleBundle == null) {
            return "";
        }
        String finalUrl = this.adsScheduleBundle.getFinalUrl();
        if (finalUrl == null) {
            return "";
        }
        return finalUrl;
    }

    public int getAdIdByAdZoneId(String adZoneId) {
        int rtn = -1;
        if (adZoneId == null || adZoneId.equals("")) {
            return -1;
        }
        if (this.adsScheduleBundle == null) {
            return -1;
        }
        List<SlotInfo> slotInfoList = this.adsScheduleBundle.getSlotInfoList();
        if (slotInfoList == null || slotInfoList.isEmpty()) {
            return -1;
        }
        int slotInfoSize = slotInfoList.size();
        for (int i = 0; i < slotInfoSize; i++) {
            SlotInfo slotInfo = (SlotInfo) slotInfoList.get(i);
            if (slotInfo.getType() == 0 && adZoneId.equals(slotInfo.getAdZoneId())) {
                List<AdInfo> adInfoList = slotInfo.getPlayableAds();
                if (!adInfoList.isEmpty()) {
                    rtn = ((AdInfo) adInfoList.get(0)).getAdId();
                }
            }
        }
        return rtn;
    }

    public List<CupidAdSlot> getSlotSchedules() {
        List<CupidAdSlot> cupidAdSlotList = new ArrayList();
        if (this.adsScheduleBundle != null) {
            List<SlotInfo> slotInfoList = this.adsScheduleBundle.getSlotInfoList();
            if (slotInfoList != null) {
                int slotInfoSize = slotInfoList.size();
                for (int i = 0; i < slotInfoSize; i++) {
                    SlotInfo slotInfo = (SlotInfo) slotInfoList.get(i);
                    cupidAdSlotList.add(new CupidAdSlot(slotInfo.getSlotId(), slotInfo.getType(), slotInfo.getOffsetInTimeline(), slotInfo.getDuration(), slotInfo.getAdZoneId(), slotInfo.getSlotExtras()));
                }
            }
        }
        return cupidAdSlotList;
    }

    public List<CupidFutureSlot> getFutureSlots() {
        List<CupidFutureSlot> cupidFutureSlotList = new ArrayList();
        if (this.adsScheduleBundle != null) {
            List<FutureSlotInfo> futureSlotInfoList = this.adsScheduleBundle.getFutureSlotList();
            if (futureSlotInfoList != null) {
                int futureSlotInfoSize = futureSlotInfoList.size();
                for (int i = 0; i < futureSlotInfoSize; i++) {
                    FutureSlotInfo futureSlotInfo = (FutureSlotInfo) futureSlotInfoList.get(i);
                    cupidFutureSlotList.add(new CupidFutureSlot(futureSlotInfo.getStartTime(), futureSlotInfo.getType(), futureSlotInfo.getSequenceId()));
                }
            }
        }
        return cupidFutureSlotList;
    }

    public List<CupidAd> getAdSchedules(int slotId) {
        List<CupidAd> cupidAdList = new ArrayList();
        int resultId = CupidUtils.getResultIdBySlotId(slotId);
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle != null) {
            List<SlotInfo> slotInfoList = adsScheduleBundle.getSlotInfoList();
            if (!(slotInfoList == null || slotInfoList.isEmpty())) {
                int i;
                SlotInfo slotInfo = null;
                int slotInfoSize = slotInfoList.size();
                for (i = 0; i < slotInfoSize; i++) {
                    if (((SlotInfo) slotInfoList.get(i)).getSlotId() == slotId) {
                        slotInfo = (SlotInfo) slotInfoList.get(i);
                    }
                }
                if (slotInfo != null) {
                    List<AdInfo> adInfoList = slotInfo.getPlayableAds();
                    if (!(adInfoList == null || adInfoList.isEmpty())) {
                        int adInfoSize = adInfoList.size();
                        for (i = 0; i < adInfoSize; i++) {
                            AdInfo adInfo = (AdInfo) adInfoList.get(i);
                            cupidAdList.add(new CupidAd(adInfo, getAdTunnelData(adInfo), (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId))));
                        }
                    }
                }
            }
        }
        return cupidAdList;
    }

    public List<CupidAdSlot> getSlotsByType(int slotType) {
        List<CupidAdSlot> cupidAdSlotList = new ArrayList();
        if (this.adsScheduleBundle != null) {
            List<SlotInfo> slotInfoList = this.adsScheduleBundle.getSlotInfoList();
            if (slotInfoList != null) {
                int slotInfoSize = slotInfoList.size();
                for (int i = 0; i < slotInfoSize; i++) {
                    SlotInfo slotInfo = (SlotInfo) slotInfoList.get(i);
                    if (slotInfo.getType() == slotType) {
                        cupidAdSlotList.add(new CupidAdSlot(slotInfo.getSlotId(), slotInfo.getType(), slotInfo.getOffsetInTimeline(), slotInfo.getDuration(), slotInfo.getAdZoneId(), slotInfo.getSlotExtras()));
                    }
                }
            }
        }
        return cupidAdSlotList;
    }

    private AdInfo getAdInfoByAdId(int adId) {
        SlotInfo slotInfo = getSlotInfo(CupidUtils.getSlotIdByAdId(adId));
        if (slotInfo == null) {
            return null;
        }
        for (AdInfo adInfo : slotInfo.getPlayableAds()) {
            if (adInfo.getAdId() == adId) {
                return adInfo;
            }
        }
        for (AdInfo emptyTracking : slotInfo.getEmptyTrackings()) {
            if (emptyTracking.getAdId() == adId) {
                return emptyTracking;
            }
        }
        return null;
    }

    public void sendAdPingBacks() {
        Log.d("a71_ads_client", "sendAdPingBacks():");
        this.pingbackController.flushCupidPingback();
    }

    public void flushCupidPingback() {
        Log.d("a71_ads_client", "flushCupidPingback():");
        this.pingbackController.flushCupidPingback();
    }

    public Map<String, Object> getCupidExtras() {
        if (this.adsScheduleBundle == null || this.adsScheduleBundle.getCupidExtras() == null) {
            return new HashMap();
        }
        return this.adsScheduleBundle.getCupidExtras();
    }

    private synchronized void addFrequencyAdEvent(String event, int adId, long time) {
        HashMap<Integer, Long> map = (HashMap) this.frequentEvents.get(event);
        if (map == null) {
            map = new HashMap();
        }
        map.put(Integer.valueOf(adId), Long.valueOf(time));
        this.frequentEvents.put(event, map);
    }

    private synchronized long lastTimeOfEvent(String event, int adId) {
        long rtn;
        rtn = 0;
        HashMap<Integer, Long> map = (HashMap) this.frequentEvents.get(event);
        if (map != null) {
            Long l = (Long) map.get(Integer.valueOf(adId));
            if (l != null) {
                rtn = l.longValue();
            }
        }
        return rtn;
    }

    private boolean isNear(Long now, Long last) {
        return now.longValue() - last.longValue() < 500;
    }

    private void sendPartyTracking(int adId, List<String> urls, String type, TrackingParty party) {
        if (urls != null && getAdInfoByAdId(adId) != null) {
            int resultId = CupidUtils.getResultIdByAdId(adId);
            ThirdPartyConfig thirdPartyConfig = getThirdPartyConfigByResultId(resultId);
            if (thirdPartyConfig != null) {
                CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
                for (int i = 0; i < urls.size(); i++) {
                    if (((String) urls.get(i)).length() != 0) {
                        HttpGetAsyncClient client = new HttpGetAsyncClient(this, thirdPartyConfig, _enableThirdSdk, cupidContext);
                        if (VERSION.SDK_INT < 11) {
                            client.execute(new String[]{(String) urls.get(i), String.valueOf(adId), String.valueOf(type), party.value()});
                        } else {
                            client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{(String) urls.get(i), String.valueOf(adId), String.valueOf(type), party.value()});
                        }
                    }
                }
            }
        }
    }

    private synchronized void setFeedbackLog(String log) {
        Log.d("a71_ads_client", "setFeedbackLog():");
        if (feedbackLogs.size() >= 10) {
            feedbackLogs.poll();
        }
        feedbackLogs.offer("SetLogTime:" + (new Date().getTime() / 1000) + NL + log);
    }

    public static String getFeedbackLog() {
        String logs = "ANDROID:\n" + "ExportLogTime:" + (new Date().getTime() / 1000) + NL;
        if (!(feedbackLogs == null || feedbackLogs.isEmpty())) {
            for (String record : feedbackLogs) {
                logs = logs + record + NL;
            }
        }
        return logs;
    }

    public List<Map<String, String>> getAdCreativesByAdSource(String adSource) {
        return AdSourceHandler.getAdCreativesByAdSource(adSource);
    }

    public String getAdDataWithAdSource(String adSource, long debugTime, String mobileKey, String mobileUserAgent, String playerId) {
        Log.d("a71_ads_client", "getAdDataWithAdSource(): debugTime: " + debugTime + ", mobileKey: " + mobileKey + ", mobileUserAgent: " + mobileUserAgent + ", playerId: " + playerId);
        return new AdSourceHandler(this.cupidGlobal, playerId, _context).getAdDataWithAdSource(adSource, debugTime, mobileKey, mobileUserAgent);
    }

    public List<Map<String, String>> getAdCreativesByServerResponse(Context context, String response) {
        return AdSourceHandler.getAdCreativesByServerResponse(context, response);
    }

    public void addTrackingEventCallback(int adId, TrackingParty party, String actType, Map<String, String> notification) {
        AdInfo adInfo = getAdInfoByAdId(adId);
        if (adInfo != null && !adInfo.isOldInterstitials()) {
            this.pingbackController.addTrackEvent(party, actType, adInfo, notification, (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId))));
        }
    }

    public List<CupidAdSlot> getSlotSchedules(int resultId) {
        List<CupidAdSlot> cupidAdSlotList = new ArrayList();
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle != null) {
            List<SlotInfo> slotInfoList = adsScheduleBundle.getSlotInfoList();
            if (!(slotInfoList == null || slotInfoList.isEmpty())) {
                int slotInfoSize = slotInfoList.size();
                for (int i = 0; i < slotInfoSize; i++) {
                    SlotInfo slotInfo = (SlotInfo) slotInfoList.get(i);
                    cupidAdSlotList.add(new CupidAdSlot(slotInfo.getSlotId(), slotInfo.getType(), slotInfo.getOffsetInTimeline(), slotInfo.getDuration(), slotInfo.getAdZoneId(), slotInfo.getSlotExtras()));
                }
            }
        }
        return cupidAdSlotList;
    }

    public List<CupidAdSlot> getSlotsByType(int resultId, int slotType) {
        List<CupidAdSlot> cupidAdSlotList = new ArrayList();
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle != null) {
            List<SlotInfo> slotInfoList = adsScheduleBundle.getSlotInfoList();
            if (!(slotInfoList == null || slotInfoList.isEmpty())) {
                int slotInfoSize = slotInfoList.size();
                for (int i = 0; i < slotInfoSize; i++) {
                    SlotInfo slotInfo = (SlotInfo) slotInfoList.get(i);
                    if (slotType == slotInfo.getType()) {
                        cupidAdSlotList.add(new CupidAdSlot(slotInfo.getSlotId(), slotInfo.getType(), slotInfo.getOffsetInTimeline(), slotInfo.getDuration(), slotInfo.getAdZoneId(), slotInfo.getSlotExtras()));
                    }
                }
            }
        }
        return cupidAdSlotList;
    }

    public Map<String, Object> getCupidExtras(int resultId) {
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle == null || adsScheduleBundle.getCupidExtras() == null) {
            return new HashMap();
        }
        return adsScheduleBundle.getCupidExtras();
    }

    public List<CupidFutureSlot> getFutureSlots(int resultId) {
        List<CupidFutureSlot> cupidFutureSlotList = new ArrayList();
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle != null) {
            List<FutureSlotInfo> futureSlotInfoList = adsScheduleBundle.getFutureSlotList();
            if (!(futureSlotInfoList == null || futureSlotInfoList.isEmpty())) {
                int futureSlotInfoSize = futureSlotInfoList.size();
                for (int i = 0; i < futureSlotInfoSize; i++) {
                    FutureSlotInfo futureSlotInfo = (FutureSlotInfo) futureSlotInfoList.get(i);
                    cupidFutureSlotList.add(new CupidFutureSlot(futureSlotInfo.getStartTime(), futureSlotInfo.getType(), futureSlotInfo.getSequenceId()));
                }
            }
        }
        return cupidFutureSlotList;
    }

    public String getFinalUrl(int resultId) {
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle == null || adsScheduleBundle.getFinalUrl() == null) {
            return "";
        }
        return adsScheduleBundle.getFinalUrl();
    }

    public String getDspSessionId(int resultId) {
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
        if (adsScheduleBundle == null || adsScheduleBundle.getDspSessionId() == null) {
            return "";
        }
        return adsScheduleBundle.getDspSessionId();
    }

    private ThirdPartyConfig getThirdPartyConfigByResultId(int resultId) {
        ThirdPartyConfig thirdPartyConfig = (ThirdPartyConfig) this.thirdPartyConfigMap.get(Integer.valueOf(resultId));
        if (thirdPartyConfig == null) {
            AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(resultId));
            if (adsScheduleBundle == null) {
                return null;
            }
            Map cupidExtras = adsScheduleBundle.getCupidExtras();
            if (cupidExtras != null && cupidExtras.size() > 0) {
                thirdPartyConfig = new ThirdPartyConfig(cupidExtras);
            }
        }
        return thirdPartyConfig;
    }

    private SlotInfo getSlotInfo(int slotId) {
        AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(CupidUtils.getResultIdBySlotId(slotId)));
        if (adsScheduleBundle == null) {
            return null;
        }
        return adsScheduleBundle.getSlotInfoBySlotId(slotId);
    }

    private void sendTrackings(int adId, String event) {
        AdInfo adInfo = getAdInfoByAdId(adId);
        if (adInfo != null) {
            CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(CupidUtils.getResultIdByAdId(adId)));
            if (cupidContext != null) {
                sendPartyTracking(adId, adInfo.getCupidTrackingUrls(event, this.cupidGlobal, cupidContext), event, TrackingParty.CUPID);
                sendPartyTracking(adId, adInfo.getAdxTrackingUrls(event, this.cupidGlobal, cupidContext), event, TrackingParty.ADX);
                sendPartyTracking(adId, adInfo.getThirdPartyTrackings(event, cupidContext, true), event, TrackingParty.THIRD);
            }
        }
    }

    private synchronized void handleSlotSequenceId(int adId) {
        SlotInfo slotInfo = getSlotInfo(CupidUtils.getSlotIdByAdId(adId));
        if (slotInfo != null && 2 == slotInfo.getType()) {
            loop0:
            for (Integer intValue : this.resultsMap.keySet()) {
                AdsScheduleBundle adsScheduleBundle = (AdsScheduleBundle) this.resultsMap.get(Integer.valueOf(intValue.intValue()));
                if (adsScheduleBundle != null) {
                    for (FutureSlotInfo futureSlotInfo : adsScheduleBundle.getFutureSlotList()) {
                        if (slotInfo.getType() == futureSlotInfo.getType() && ((long) slotInfo.getStartTime()) == futureSlotInfo.getStartTime()) {
                            slotInfo.setSequenceId(futureSlotInfo.getSequenceId());
                            break loop0;
                        }
                    }
                    continue;
                }
            }
        }
    }

    private void handleEmptyTrackings(String actType, String timeSlice, SlotInfo slotInfo) {
        if (slotInfo != null) {
            List<AdInfo> emptyTrackings = slotInfo.getEmptyTrackings();
            if (emptyTrackings.isEmpty()) {
                Log.d("a71_ads_client", "handleEmptyTrackings(): no empty tracking.");
                return;
            }
            String targetedTimeSlice = "";
            if (timeSlice != null) {
                targetedTimeSlice = CupidUtils.getFirstPart(timeSlice, ",");
            }
            for (AdInfo emptyTracking : emptyTrackings) {
                String emptyTimeSlice = CupidUtils.getFirstPart(emptyTracking.getTimePosition(), ",");
                if ((timeSlice == null || (emptyTimeSlice != null && emptyTimeSlice.equals(targetedTimeSlice))) && !emptyTracking.isTrackingPingbackSent(128)) {
                    emptyTracking.setTrackingPingbackFlag(128);
                    int adId = emptyTracking.getAdId();
                    Log.d("a71_ads_client", "handleEmptyTrackings(): send empty tracking, adId: " + adId);
                    sendTrackings(adId, "impression");
                }
            }
        }
    }

    private void sendEmptyTrackings(int resultId, AdsScheduleBundle adsScheduleBundle, CupidContext cupidContext) {
        boolean needSendInventory = false;
        boolean relyOnCardShow = cupidContext.relyOnCardShow();
        List<SlotInfo> slotInfoList = adsScheduleBundle.getSlotInfoList();
        if (slotInfoList.isEmpty() && !relyOnCardShow) {
            needSendInventory = true;
        }
        for (SlotInfo slotInfo : slotInfoList) {
            if (slotInfo.isPlayableAdsEmpty()) {
                int slotType = slotInfo.getType();
                if (1 == slotType || 6 == slotType || (slotType == 0 && !relyOnCardShow)) {
                    needSendInventory = true;
                    handleEmptyTrackings("impression", null, slotInfo);
                }
            }
        }
        if (needSendInventory) {
            this.pingbackController.sendInventoryPingback(resultId, PingbackConstants.ACT_INVENTORY, cupidContext);
        }
    }

    private boolean isFromCache(int resultId) {
        CupidContext cupidContext = (CupidContext) this.cupidContextMap.get(Integer.valueOf(resultId));
        if (cupidContext == null) {
            return false;
        }
        return cupidContext.isFromCache();
    }
}
