package com.gala.video.lib.share.uikit.loader.data;

import android.os.SystemClock;
import android.util.Log;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.loader.IUikitDataFetcherCallback;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class BannerAdRequest {
    private static final String TAB_BANNER_AD_ID = "70001";
    private static final String TAG = "uikit/BannerAdRequest";

    public static void callBannerAd(int channelId, String albumId, String tvQid, boolean isVip, IUikitDataFetcherCallback callback, boolean isFromDetailPage) {
        callBannerAd(channelId, albumId, tvQid, isVip, false, callback, isFromDetailPage);
    }

    public static void callBannerAd(int channelId, String albumId, String tvQid, boolean isVip, boolean isBatchCallback, IUikitDataFetcherCallback callback, boolean isFromDeatailPage) {
        Log.d(TAG, "channelid-" + channelId);
        try {
            String result;
            int st;
            long startTime = SystemClock.elapsedRealtime();
            AdsClient mAdsClient = AdsClientUtils.getInstance();
            if (isFromDeatailPage) {
                result = GetInterfaceTools.getIAdApi().fetchBannerAd(AdsClient.getSDKVersion(), String.valueOf(channelId), albumId, tvQid);
            } else {
                result = GetInterfaceTools.getIAdApi().fetchBannerAd(AdsClient.getSDKVersion(), TAB_BANNER_AD_ID);
            }
            LogUtils.d("xiaomi", "isFrom detail : " + isFromDeatailPage + ", banner ad result :" + result);
            List<BannerAd> list = parseAd(mAdsClient, result, isFromDeatailPage);
            if (list == null || list.size() <= 0) {
                st = 0;
                callback.onFailed();
            } else {
                st = 1;
                List<CardInfoModel> cardList = new ArrayList(3);
                for (BannerAd ad : list) {
                    CardInfoModel model = new CardInfoModel();
                    model.cardLayoutId = 1023;
                    model.isVIPTag = isVip;
                    CardInfoBuildTool.buildBannerCard(model, ad);
                    model.setId(String.valueOf(ad.mAdZoneId));
                    model.mCardId = ad.mAdZoneId;
                    if (!isFromDeatailPage) {
                        UikitSourceDataCache.addBannerAdPosFromAdServer(ad.mAdZoneId);
                        LogUtils.d("xiaomi", "callBannerAd, adZoneId : " + ad.mAdZoneId);
                    }
                    cardList.add(model);
                    if (!isBatchCallback) {
                        callback.onSuccess(cardList, "");
                    }
                }
                if (isBatchCallback) {
                    callback.onSuccess(cardList, "");
                }
            }
            sendPingback(st, (int) (SystemClock.elapsedRealtime() - startTime));
        } catch (Exception e) {
            callback.onFailed();
            Log.e(TAG, "fetchBannerAdData, Exception :" + e);
        }
    }

    private static List<BannerAd> parseAd(AdsClient adclient, String adJson, boolean isFromDetailPage) {
        List<BannerAd> models = new ArrayList();
        try {
            Map<String, Object> passportMap = new HashMap();
            passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
            adclient.setSdkStatus(passportMap);
            int resultId = adclient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145");
            if (!isFromDetailPage) {
                UikitSourceDataCache.writeBannerAdId(resultId);
                LogUtils.d("xiaomi", "parseAd, ad resultId : " + resultId);
            }
            adclient.flushCupidPingback();
            List<CupidAdSlot> slots = adclient.getSlotsByType(0);
            if (slots == null || slots.size() <= 0) {
                Log.d(TAG, "no slot with the type of SLOT_TYPE_PAGE, asjson=" + adJson);
                adclient.sendAdPingBacks();
            } else {
                for (CupidAdSlot slot : slots) {
                    if (slot != null) {
                        List<CupidAd> ads = adclient.getAdSchedules(slot.getSlotId());
                        if (ads == null || ads.isEmpty()) {
                            Log.d(TAG, "adId is null.");
                        } else {
                            CupidAd ad = (CupidAd) ads.get(0);
                            if (ad == null) {
                                Log.d(TAG, "CupidAd object is null.");
                            } else if (CupidAd.TEMPLATE_TYPE_TV_BANNER.equals(ad.getTemplateType())) {
                                BannerAd model = new BannerAd();
                                model.adId = ad.getAdId();
                                model.clickThroughType = ad.getClickThroughType();
                                model.clickThroughInfo = ad.getClickThroughUrl();
                                Map<String, Object> screenAds = ad.getCreativeObject();
                                Object imgUrlObj = screenAds.get("url");
                                if (imgUrlObj != null) {
                                    model.imageUrl = imgUrlObj.toString();
                                    Object needBadge = screenAds.get("needAdBadge");
                                    if (needBadge != null) {
                                        model.needAdBadge = "true".equals(needBadge.toString());
                                    }
                                    BannerAdProcessingUtils.parseClickInfo(ad.getClickThroughType(), ad.getClickThroughUrl(), model);
                                    model.mAdZoneId = slot.getAdZoneId();
                                    models.add(model);
                                    Log.d(TAG, "get banner {" + model.mAdZoneId + "}");
                                } else {
                                    Log.e(TAG, "banner url is null.");
                                }
                            } else {
                                Log.e(TAG, "creative type: " + ad.getCreativeType());
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "banner ad size = " + models.size());
        } catch (JSONException e1) {
            Log.e(TAG, "parseAd exception", e1);
        } catch (Exception e) {
            Log.e(TAG, "parseAd exception", e);
        }
        return models;
    }

    private static void sendPingback(int st, int elapse) {
        PingBackParams pingbackInfos = new PingBackParams();
        pingbackInfos.add(Keys.T, "11");
        pingbackInfos.add("ct", "150619_request");
        pingbackInfos.add(Keys.RI, "ad_banner_detail");
        pingbackInfos.add("td", String.valueOf(elapse));
        pingbackInfos.add("st", String.valueOf(st));
        PingBack.getInstance().postPingBackToLongYuan(pingbackInfos.build());
    }
}
