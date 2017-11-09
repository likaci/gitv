package com.gala.video.app.epg.home.ads.controller;

import android.content.Context;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.ViewGroup;
import com.gala.video.app.epg.home.ads.model.ImageAdInfo;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.model.AdResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class StartScreenAdHandler {
    private static final String TAG = "StartScreenAdHandler";
    private static StartScreenAdHandler sInstance = new StartScreenAdHandler();
    private AdStatusCallBack mAdStateCallback;
    private AdsClient mAdsClient = AdsClientUtils.getInstance();
    private AdStatusCallBack mCallBack = new C05632();
    private Context mContext;
    private String mDynamicUrl = "";
    private boolean mHasAd = false;
    private ImageAdInfo mImageAdInfo;
    private boolean mIsVideoAd = false;
    private long mStartRequestTime = 0;
    private IStartScreenAd mStartScreenAd;
    private long mStartTime = 0;

    class C05632 implements AdStatusCallBack {
        C05632() {
        }

        public void onAdPrepared() {
            if (StartScreenAdHandler.this.mAdStateCallback != null) {
                StartScreenAdHandler.this.mAdStateCallback.onAdPrepared();
            }
            StartScreenAdHandler.this.mHasAd = true;
        }

        public void onError() {
            if (StartScreenAdHandler.this.mAdStateCallback != null) {
                StartScreenAdHandler.this.mAdStateCallback.onError();
            }
            StartScreenAdHandler.this.mHasAd = false;
        }

        public void onFinished() {
            if (StartScreenAdHandler.this.mAdStateCallback != null) {
                StartScreenAdHandler.this.mAdStateCallback.onFinished();
            }
            StartScreenAdHandler.this.mHasAd = false;
        }

        public void onTimeOut() {
            if (StartScreenAdHandler.this.mAdStateCallback != null) {
                StartScreenAdHandler.this.mAdStateCallback.onTimeOut();
            }
            StartScreenAdHandler.this.mHasAd = false;
        }
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public static StartScreenAdHandler instance() {
        return sInstance;
    }

    public static void recycle() {
        sInstance = null;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.mStartScreenAd != null) {
            return this.mStartScreenAd.dispatchKeyEvent(keyEvent);
        }
        return false;
    }

    public void setRequestCallback(AdStatusCallBack callback) {
        LogUtils.m1568d(TAG, "set ad callback = " + callback);
        this.mAdStateCallback = callback;
    }

    public void showAd(ViewGroup container) {
        if (this.mStartScreenAd != null) {
            this.mStartScreenAd.showAd(container);
        }
    }

    public void stop() {
        if (this.mStartScreenAd != null) {
            this.mStartScreenAd.stop();
        }
    }

    public void start() {
        this.mStartTime = SystemClock.elapsedRealtime();
        AdsClient adsClient = this.mAdsClient;
        final String adVer = AdsClient.getSDKVersion();
        LogUtils.m1568d(TAG, "getScreenAd adsClientVersion:" + adVer);
        this.mStartRequestTime = SystemClock.elapsedRealtime();
        new Thread8K(new Runnable() {
            public void run() {
                AdResult result = new AdResult();
                try {
                    result.ad = GetInterfaceTools.getIAdApi().getScreenAd(adVer);
                    StartScreenAdHandler.this.handleAdResult(result, null);
                } catch (Exception e) {
                    LogUtils.m1572e(StartScreenAdHandler.TAG, "getScreenAd Exception ", e);
                    StartScreenAdHandler.this.handleAdResult(result, e);
                }
            }
        }).start();
    }

    private StartScreenAdHandler() {
    }

    public long getStartRequestTime() {
        return this.mStartRequestTime;
    }

    private boolean parseAd(String adJson) {
        long startTime = SystemClock.elapsedRealtime();
        try {
            if (StringUtils.isEmpty((CharSequence) adJson)) {
                LogUtils.m1568d(TAG, "[StartScreenVideoAd-Performance]parseAd timeCost=" + (SystemClock.elapsedRealtime() - startTime));
                return false;
            }
            Map<String, Object> passportMap = new HashMap();
            passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
            this.mAdsClient.setSdkStatus(passportMap);
            this.mAdsClient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145");
            this.mAdsClient.flushCupidPingback();
            List<CupidAdSlot> slots = this.mAdsClient.getSlotsByType(0);
            if (slots == null || slots.size() <= 0) {
                LogUtils.m1568d(TAG, "no slot with the type of SLOT_TYPE_PAGE, asjson=" + adJson);
                this.mAdsClient.sendAdPingBacks();
                LogUtils.m1568d(TAG, "[StartScreenVideoAd-Performance]parseAd timeCost=" + (SystemClock.elapsedRealtime() - startTime));
                return false;
            }
            CupidAdSlot slot = (CupidAdSlot) slots.get(0);
            if (slot != null) {
                List<CupidAd> ads = this.mAdsClient.getAdSchedules(slot.getSlotId());
                if (ads == null || ads.isEmpty()) {
                    LogUtils.m1568d(TAG, "adId is null.");
                } else {
                    CupidAd ad = (CupidAd) ads.get(0);
                    if (ad == null) {
                        LogUtils.m1568d(TAG, "CupidAd object is null.");
                    } else if (CupidAd.CREATIVE_TYPE_IMAGE_START_SCREEN.equals(ad.getCreativeType())) {
                        Map<String, Object> screenAds = ad.getCreativeObject();
                        Object obj = screenAds.get(AdsConstants.AD_SCREEN_RENDER_TYPE);
                        if (obj != null) {
                            String type = obj.toString();
                            LogUtils.m1568d(TAG, "screen ad render type is " + type);
                            if ("image".equals(type)) {
                                Object imageUrl = screenAds.get("landScapeUrl");
                                Object canSkip = screenAds.get(AdsConstants.AD_VIDEO_SKIPPABLE);
                                if (imageUrl != null) {
                                    String skip = "";
                                    if (canSkip != null) {
                                        skip = canSkip.toString();
                                    }
                                    this.mImageAdInfo = new ImageAdInfo(imageUrl.toString(), ad.getAdId(), ad, skip);
                                    this.mStartScreenAd = new StartScreenImageAd(this.mAdsClient, this.mImageAdInfo, this.mStartRequestTime);
                                    this.mStartScreenAd.setAdStatusCallBack(this.mCallBack);
                                }
                            } else if ("video".equals(type)) {
                                this.mIsVideoAd = true;
                                Object dynamicUrl = screenAds.get("dynamicUrl");
                                String raw_url = "";
                                if (dynamicUrl != null) {
                                    raw_url = dynamicUrl.toString();
                                }
                                this.mDynamicUrl = GetInterfaceTools.getIAdApi().getScreenVideoDownLoadUrl(raw_url);
                                this.mStartScreenAd = new StartScreenVideoAd(this.mContext, this.mAdsClient, this.mDynamicUrl, ad, screenAds, this.mStartRequestTime);
                                this.mStartScreenAd.setAdStatusCallBack(this.mCallBack);
                                LogUtils.m1568d(TAG, "video ad dynamic url : " + this.mDynamicUrl);
                            }
                            LogUtils.m1568d(TAG, "[StartScreenVideoAd-Performance]parseAd timeCost=" + (SystemClock.elapsedRealtime() - startTime));
                            return true;
                        }
                        LogUtils.m1568d(TAG, "lanscape url is null.");
                    } else {
                        LogUtils.m1571e(TAG, "creative type: " + ad.getCreativeType());
                    }
                }
            }
            LogUtils.m1568d(TAG, "[StartScreenVideoAd-Performance]parseAd timeCost=" + (SystemClock.elapsedRealtime() - startTime));
            return false;
        } catch (JSONException e1) {
            LogUtils.m1572e(TAG, "parseAd exception", e1);
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "parseAd exception", e);
        }
    }

    public boolean hasAd() {
        return this.mHasAd;
    }

    private void handleAdResult(AdResult result, Exception e) {
        if (e != null) {
            this.mAdsClient.onRequestMobileServerFailed();
            this.mAdsClient.sendAdPingBacks();
            HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).addItem("td", String.valueOf(SystemClock.elapsedRealtime() - this.mStartTime)).addItem(Keys.f2035T, "11").addItem("ct", "150619_request").setOthersNull().post();
            LogUtils.m1572e(TAG, "AdCallback, onGetAdDone, Exception", e);
        } else if (result != null) {
            long elapse = SystemClock.elapsedRealtime() - this.mStartTime;
            LogUtils.m1568d(TAG, "[StartScreenVideoAd-Performance]fetch advertisement timeCost=" + elapse);
            if (StringUtils.isEmpty(result.ad)) {
                this.mAdsClient.onRequestMobileServerFailed();
                this.mAdsClient.sendAdPingBacks();
                LogUtils.m1568d(TAG, "no ad");
                HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", "0").addItem("td", String.valueOf(elapse)).addItem(Keys.f2035T, "11").addItem("ct", "150619_request").setOthersNull().post();
            } else if (parseAd(result.ad)) {
                this.mStartScreenAd.loadData(elapse);
            } else {
                HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", "0").addItem("td", String.valueOf(elapse)).addItem(Keys.f2035T, "11").addItem("ct", "150619_request").setOthersNull().post();
            }
        }
    }
}
