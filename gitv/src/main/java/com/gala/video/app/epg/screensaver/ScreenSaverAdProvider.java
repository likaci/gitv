package com.gala.video.app.epg.screensaver;

import android.content.Context;
import android.os.SystemClock;
import com.gala.video.app.epg.screensaver.imagedownload.ImageDownload;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.model.AdResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.CupidAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.CupidAdSlot;
import com.mcto.ads.internal.net.PingbackConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

class ScreenSaverAdProvider {
    private static final String LOCAL_PATH_IMAGE_SCREEN_SAVER_ADS = "screensaveradfiles/";
    private static final String TAG = "screenaverad/ScreenSaverAdProvider";
    private final String PARENT_PATH;
    private final ArrayList<ScreenSaverAdModel> mAdInfoListWithImageDownLoaded = new ArrayList();
    private final ImageDownload mImageDownLoader = new ImageDownload();
    private volatile boolean mIsAdPrepared = false;
    private final ArrayList<ScreenSaverAdModel> mRawAdInfoList = new ArrayList();

    public ScreenSaverAdProvider() {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        this.PARENT_PATH = context.getFilesDir() != null ? context.getFilesDir().getPath() + "/" : "/";
        initDownloadDir();
    }

    private void initDownloadDir() {
        File imgDir = new File(this.PARENT_PATH + LOCAL_PATH_IMAGE_SCREEN_SAVER_ADS);
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }
        LogUtils.d(TAG, "initDownloadDir, path saving screensaver ad images :" + imgDir);
    }

    public boolean isAdPrepared() {
        LogUtils.d(TAG, "isAdPrepared, screensaver ad is prepared: " + this.mIsAdPrepared);
        return this.mIsAdPrepared;
    }

    public void reset() {
        LogUtils.d(TAG, LoginConstant.CLICK_RESEAT_CHANGE_PASSWORD);
        Iterator it = this.mAdInfoListWithImageDownLoaded.iterator();
        while (it.hasNext()) {
            this.mImageDownLoader.deleteImage(((ScreenSaverAdModel) it.next()).getImageLocalPath());
        }
        this.mAdInfoListWithImageDownLoaded.clear();
        this.mRawAdInfoList.clear();
        this.mIsAdPrepared = false;
    }

    public List<ScreenSaverAdModel> getAdData() {
        return this.mAdInfoListWithImageDownLoaded;
    }

    public void fetchAdData() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                long startTime = SystemClock.elapsedRealtime();
                ScreenSaverAdProvider.this.reset();
                CharSequence adResultJson = ScreenSaverAdProvider.this.requestAdData();
                if (!StringUtils.isEmpty(adResultJson)) {
                    ScreenSaverAdProvider.this.parseScreenSaverAdJson(adResultJson, AdsClientUtils.getInstance());
                    ScreenSaverAdProvider.this.downloadAdImages();
                }
                LogUtils.d(ScreenSaverAdProvider.TAG, "fetchAdData, all raw screensaver ads count :" + ListUtils.getCount(ScreenSaverAdProvider.this.mRawAdInfoList));
                LogUtils.d(ScreenSaverAdProvider.TAG, "fetchAdData, ad image downloaded success, the actual count of ads that can be shown :" + ListUtils.getCount(ScreenSaverAdProvider.this.mAdInfoListWithImageDownLoaded));
                LogUtils.d(ScreenSaverAdProvider.TAG, "fetchAdData, download screensaver ad image cost : " + (SystemClock.elapsedRealtime() - startTime) + " ms");
            }
        });
    }

    private String requestAdData() {
        AdResult result = new AdResult();
        LogUtils.d(TAG, "requestAdData, start requesting screen saver ad");
        result.ad = GetInterfaceTools.getIAdApi().getScreenSaverAds(AdsClient.getSDKVersion());
        if (result.isEmpty()) {
            LogUtils.w(TAG, "requestAdData, the result of request ad is empty");
        }
        return result.ad;
    }

    private void parseScreenSaverAdJson(String adJson, AdsClient adsClient) {
        LogUtils.i(TAG, "parseScreenSaverAdJson,");
        if (StringUtils.isEmpty((CharSequence) adJson) || adsClient == null) {
            String str;
            String str2 = TAG;
            if (StringUtils.isEmpty((CharSequence) adJson)) {
                str = " ad json is empty";
            } else {
                str = "" + (adsClient == null ? " the current AdsClient is null" : "");
            }
            LogUtils.w(str2, str);
            return;
        }
        int nameIndex = 0;
        Map<String, Object> passportMap = new HashMap();
        passportMap.put(PingbackConstants.PASSPORT_ID, GetInterfaceTools.getIGalaAccountManager().getUID());
        try {
            adsClient.setSdkStatus(passportMap);
            adsClient.onRequestMobileServerSucceededWithAdData(adJson, "", "qc_100001_100145");
            adsClient.flushCupidPingback();
            List<CupidAdSlot> slotList = adsClient.getSlotsByType(0);
            if (ListUtils.isEmpty((List) slotList)) {
                LogUtils.w(TAG, "parseScreenSaverAdJson, no CupidAdSlot's list with the type of SLOT_TYPE_PAGE");
                return;
            }
            for (CupidAdSlot slot : slotList) {
                int slotId = slot.getSlotId();
                LogUtils.d(TAG, "parseScreenSaverAdJson, CupidAdSlot id: " + slotId);
                List<CupidAd> cupidAdList = adsClient.getAdSchedules(slotId);
                if (ListUtils.isEmpty((List) cupidAdList)) {
                    LogUtils.w(TAG, "parseScreenSaverAdJson, the CupidAd's list with " + slotId + " is empty");
                } else {
                    for (CupidAd cupidAd : cupidAdList) {
                        if (cupidAd == null) {
                            LogUtils.w(TAG, "parseScreenSaverAdJson, the current CupidAd object is null.");
                        } else if ("screensaver".equals(cupidAd.getCreativeType())) {
                            Map screenAds = cupidAd.getCreativeObject();
                            if (ListUtils.isEmpty(screenAds)) {
                                LogUtils.w(TAG, "parseScreenSaverAdJson, the current CupidAd@" + cupidAd.hashCode() + " Map is empty");
                            } else {
                                CupidAdModel screenSaverAdModel = new ScreenSaverAdModel();
                                screenSaverAdModel.setAdId(cupidAd.getAdId());
                                Object adImageUrlObj = screenAds.get("imgUrl");
                                screenSaverAdModel.setAdImageUrl(adImageUrlObj == null ? "" : adImageUrlObj.toString());
                                screenSaverAdModel.setQrUrl(cupidAd.getClickThroughUrl());
                                screenSaverAdModel.setImageName("screensaverAdImage_" + nameIndex);
                                nameIndex++;
                                Object qrDesObj = screenAds.get("qrDescription");
                                screenSaverAdModel.setQrDescription(qrDesObj == null ? "" : qrDesObj.toString());
                                Object durationDescObj = screenAds.get("duration");
                                screenSaverAdModel.setDuration(durationDescObj == null ? "" : durationDescObj.toString());
                                Object qrPosObj = screenAds.get("qrPosition");
                                screenSaverAdModel.setQrPosition(qrPosObj == null ? "" : qrPosObj.toString());
                                Object needAdBadgeObj = screenAds.get("needAdBadge");
                                screenSaverAdModel.setNeedAdBadge(needAdBadgeObj == null ? "" : needAdBadgeObj.toString());
                                Object needQrObj = screenAds.get("needQR");
                                screenSaverAdModel.setNeedQR(needQrObj == null ? "" : needQrObj.toString());
                                Object qrTitleObj = screenAds.get("needAdBadge");
                                screenSaverAdModel.setQrTitle(qrTitleObj == null ? "" : qrTitleObj.toString());
                                GetInterfaceTools.getIAdProcessingUtils().parseAdRawData(cupidAd, screenSaverAdModel);
                                this.mRawAdInfoList.add(screenSaverAdModel);
                                LogUtils.d(TAG, "screen saver advertisement info = " + screenSaverAdModel);
                            }
                        }
                    }
                }
            }
            LogUtils.d(TAG, "parseScreenSaverAdJson, raw ad data size:" + ListUtils.getCount((List) this.mRawAdInfoList));
        } catch (JSONException e) {
            LogUtils.e(TAG, "parseScreenSaverAdJson, JSONException : ", e);
        } catch (Exception e2) {
            LogUtils.e(TAG, "parseScreenSaverAdJson, exception : ", e2);
        }
    }

    private void downloadAdImages() {
        if (ListUtils.isEmpty(this.mRawAdInfoList)) {
            LogUtils.w(TAG, "downloadAdImages, the current raw ad data is empty");
            return;
        }
        Iterator it = this.mRawAdInfoList.iterator();
        while (it.hasNext()) {
            ScreenSaverAdModel model = (ScreenSaverAdModel) it.next();
            String filename = this.PARENT_PATH + LOCAL_PATH_IMAGE_SCREEN_SAVER_ADS + model.getImageName();
            String imageUrl = model.getAdImageUrl();
            if (this.mImageDownLoader.downloadImage(model.getAdImageUrl(), filename)) {
                model.setImageLocalPath(filename);
                LogUtils.d(TAG, "downloadAdImages, downloading one of the screen ad image success, file path = " + filename);
                this.mAdInfoListWithImageDownLoaded.add(model);
            } else {
                LogUtils.e(TAG, "downloadAdImages, downloading one of the screen ad image failed, url = " + imageUrl);
            }
        }
        int count = ListUtils.getCount(this.mAdInfoListWithImageDownLoaded);
        LogUtils.d(TAG, "downloadAdImages, downloading a round screen ad images finished, result size = " + count);
        this.mIsAdPrepared = count != 0;
    }
}
