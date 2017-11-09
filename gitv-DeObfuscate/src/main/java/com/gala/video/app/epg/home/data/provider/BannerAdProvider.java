package com.gala.video.app.epg.home.data.provider;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import com.gala.video.app.epg.home.ads.BannerAdProcessingUtils;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner.BannerAdResultModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner.IBannerAdProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class BannerAdProvider extends Wrapper {
    private static final String TAB_BANNER_AD_ID = "70001";
    private static final String TAG = "BannerAdProvider";
    private static long startRequestTime = SystemClock.elapsedRealtime();
    private AdsClient mAdsClient;
    private Map<Integer, String> mInvocationBannerMap;
    private int mResultId;

    private static class InstanceHolder {
        private static final BannerAdProvider INSTANCE = new BannerAdProvider();

        private InstanceHolder() {
        }
    }

    public static BannerAdProvider getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private BannerAdProvider() {
        this.mInvocationBannerMap = new HashMap();
        this.mAdsClient = AdsClientUtils.getInstance();
    }

    public Map<Integer, String> getInvocationBannerMap() {
        return this.mInvocationBannerMap;
    }

    public void setInvocationBannerMap(Map<Integer, String> invocationBannerList) {
        this.mInvocationBannerMap = invocationBannerList;
    }

    public List<BannerImageAdModel> fetchBannerAdData() {
        String result = null;
        startRequestTime = SystemClock.elapsedRealtime();
        try {
            IAdApi iAdApi = GetInterfaceTools.getIAdApi();
            AdsClient adsClient = this.mAdsClient;
            result = iAdApi.fetchBannerAd(AdsClient.getSDKVersion(), TAB_BANNER_AD_ID);
        } catch (Exception e) {
            LogUtils.m1571e("BannerAdProvider", "fetchBannerAdData, Exception :" + e);
            HomePingbackFactory.instance().createPingback(CommonPingback.BANNER_AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_banner_tab").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - startRequestTime)).addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem(Keys.f2035T, "11").addItem("ct", "150619_request").addItem("qtcurl", "tab首页").setOthersNull().post();
        }
        BannerAdResultModel bannerAdResultModel = parseAd(this.mAdsClient, result);
        List resultList = bannerAdResultModel != null ? bannerAdResultModel.getModels() : new ArrayList();
        if (bannerAdResultModel != null) {
            setResultId(bannerAdResultModel.getResultId());
        }
        LogUtils.m1568d("BannerAdProvider", "fetchBannerAdData, banner ad count = " + (!ListUtils.isEmpty(resultList) ? resultList.size() : 0));
        HomePingbackFactory.instance().createPingback(CommonPingback.BANNER_AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_banner_tab").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - startRequestTime)).addItem("st", !ListUtils.isEmpty(resultList) ? "1" : "0").addItem(Keys.f2035T, "11").addItem("ct", "150619_request").addItem("qtcurl", "tab首页").setOthersNull().post();
        return resultList;
    }

    public int getResultId() {
        return this.mResultId;
    }

    private void setResultId(int resultId) {
        this.mResultId = resultId;
    }

    public BannerAdResultModel parseAd(AdsClient adclient, String adJson) {
        BannerAdResultModel bannerAdResultModel = new BannerAdResultModel();
        List<BannerImageAdModel> models = new ArrayList();
        bannerAdResultModel.setModels(models);
        try {
            Map<String, Object> passportMap = new HashMap();
            passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
            adclient.setSdkStatus(passportMap);
            bannerAdResultModel.setResultId(adclient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145"));
            adclient.flushCupidPingback();
            List<CupidAdSlot> slots = adclient.getSlotsByType(0);
            if (slots == null || slots.size() <= 0) {
                LogUtils.m1568d("BannerAdProvider", "no slot with the type of SLOT_TYPE_PAGE, asjson=" + adJson);
                adclient.sendAdPingBacks();
            } else {
                for (CupidAdSlot slot : slots) {
                    if (slot != null) {
                        List<CupidAd> ads = adclient.getAdSchedules(slot.getSlotId());
                        if (ads == null || ads.isEmpty()) {
                            LogUtils.m1568d("BannerAdProvider", "adId is null.");
                        } else {
                            CupidAd ad = (CupidAd) ads.get(0);
                            if (ad == null) {
                                LogUtils.m1568d("BannerAdProvider", "CupidAd object is null.");
                            } else if (CupidAd.TEMPLATE_TYPE_TV_BANNER.equals(ad.getTemplateType())) {
                                BannerImageAdModel model = new BannerImageAdModel();
                                model.setAdId(ad.getAdId());
                                model.setClickThroughType(ad.getClickThroughType());
                                model.setClickThroughInfo(ad.getClickThroughUrl());
                                Map<String, Object> screenAds = ad.getCreativeObject();
                                Object imgUrlObj = screenAds.get("url");
                                if (imgUrlObj != null) {
                                    model.setImageUrl(imgUrlObj.toString());
                                    Object needBadge = screenAds.get("needAdBadge");
                                    if (needBadge != null) {
                                        model.setNeedAdBadge("true".equals(needBadge.toString()));
                                    }
                                    BannerAdProcessingUtils.parseClickInfo(ad.getClickThroughType(), ad.getClickThroughUrl(), model);
                                    model.setAdZoneId(slot.getAdZoneId());
                                    models.add(model);
                                    LogUtils.m1568d("BannerAdProvider", "get banner {" + model + "}");
                                } else {
                                    LogUtils.m1568d("BannerAdProvider", "banner url is null.");
                                }
                            } else {
                                LogUtils.m1571e("BannerAdProvider", "creative type: " + ad.getCreativeType());
                            }
                        }
                    }
                }
            }
            LogUtils.m1568d("BannerAdProvider", "banner ad size = " + models.size());
        } catch (JSONException e1) {
            LogUtils.m1572e("BannerAdProvider", "parseAd exception", e1);
        } catch (Exception e) {
            LogUtils.m1572e("BannerAdProvider", "parseAd exception", e);
        }
        return bannerAdResultModel;
    }

    public String getAdNetworkInfo() {
        String result = "0";
        NetworkInfo activeNetInfo = ((ConnectivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            switch (activeNetInfo.getType()) {
                case 1:
                    result = "1";
                    break;
                case 9:
                    result = "13";
                    break;
                default:
                    result = "0";
                    break;
            }
        }
        LogUtils.m1568d("BannerAdProvider", "get getAdNw value = : " + result);
        return result;
    }
}
