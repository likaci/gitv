package com.gala.video.app.epg.home.ads.task;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.sdk.player.AdCacheManager;
import com.gala.sdk.player.AdCacheManager.AdCacheItem;
import com.gala.video.app.epg.home.ads.AdCacheManagerProxy;
import com.gala.video.app.epg.home.ads.model.AdImageResInfoModel;
import com.gala.video.app.epg.home.ads.model.AdResInfo;
import com.gala.video.app.epg.home.ads.model.AdResInfo.VideoResUrl;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import java.util.concurrent.atomic.AtomicInteger;

public class AdImageResRequestTask {
    private static final String TAG = "ads/AdImageResRequestTask";
    private IDownloader mDownloader = DownloaderAPI.getDownloader();
    private IFileCallback mIFileCallback = new C05802();
    private AtomicInteger mImageLoadCounter = new AtomicInteger(0);

    class C05791 implements Runnable {
        C05791() {
        }

        public void run() {
            AdImageResRequestTask.this.requestAndParseAdInfo();
        }
    }

    class C05802 implements IFileCallback {
        C05802() {
        }

        public void onSuccess(FileRequest imageRequest, String path) {
            LogUtils.m1568d(AdImageResRequestTask.TAG, "IFIleCallback, onSuccess, path = " + path);
        }

        public void onFailure(FileRequest imageRequest, Exception e) {
            LogUtils.m1569d(AdImageResRequestTask.TAG, "IFIleCallback, onFailure, exception = ", e);
        }
    }

    public void execute() {
        ThreadUtils.execute(new C05791());
    }

    private void requestAndParseAdInfo() {
        CharSequence adJsonStr = GetInterfaceTools.getIAdApi().getAdImageResourceJSON();
        LogUtils.m1568d(TAG, "AD image resource JSON = " + adJsonStr);
        if (!StringUtils.isEmpty(adJsonStr)) {
            try {
                AdImageResInfoModel adImageResInfoModel = (AdImageResInfoModel) JSON.parseObject((String) adJsonStr, AdImageResInfoModel.class);
                LogUtils.m1568d(TAG, "adImageResInfoModel, " + adImageResInfoModel);
                if (adImageResInfoModel == null) {
                    LogUtils.m1571e(TAG, "parse AD json, adImageResInfoModel = null");
                    return;
                }
                AdResInfo[] adResInfoArr = adImageResInfoModel.getData();
                if (adResInfoArr == null || adResInfoArr.length == 0) {
                    LogUtils.m1571e(TAG, "parse AD json, adResInfoArr = null");
                    return;
                }
                for (AdResInfo adResInfo : adResInfoArr) {
                    if (isContainInterstitialAd(adResInfo) || isContainExitAppAd(adResInfo)) {
                        String[] adImageUrlArr = adResInfo.getCreativeUrl();
                        int length = adImageUrlArr.length;
                        int i = 0;
                        while (i < length) {
                            String adImageUrl = adImageUrlArr[i];
                            LogUtils.m1568d(TAG, "ad image url = " + adImageUrl);
                            if (this.mImageLoadCounter.get() < 10) {
                                this.mDownloader.loadFile(new FileRequest(adImageUrl), this.mIFileCallback);
                                this.mImageLoadCounter.incrementAndGet();
                                i++;
                            } else {
                                return;
                            }
                        }
                        if (isContainInterstitialAd(adResInfo)) {
                            VideoResUrl dynamicUrls = adResInfo.getDynamicUrl();
                            if (!(dynamicUrls == null || dynamicUrls.video == null || dynamicUrls.video.length <= 0)) {
                                AdCacheManager adCacheManager = AdCacheManagerProxy.getInstance();
                                for (String dynamicUrl : dynamicUrls.video) {
                                    LogUtils.m1568d(TAG, "request dynamic url : " + dynamicUrl);
                                    String url = GetInterfaceTools.getIAdApi().getScreenVideoDownLoadUrl(dynamicUrl);
                                    if (!TextUtils.isEmpty(url)) {
                                        adCacheManager.addTask(new AdCacheItem(url, 1));
                                        adCacheManager.setCurrentRunningState(0);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                LogUtils.m1569d(TAG, "requestAndParseAdInfo, JSONException ", e);
            } catch (Exception e2) {
                LogUtils.m1569d(TAG, "requestAndParseAdInfo, exception ", e2);
            }
        }
    }

    private boolean isContainInterstitialAd(AdResInfo adResInfo) {
        return AdsConstants.JSON_TEMPLATE_TYPE_VALUE_INTERSTITIAL.equals(adResInfo.getTemplateType());
    }

    private boolean isContainExitAppAd(AdResInfo adResInfo) {
        return "exit".equals(adResInfo.getTemplateType());
    }
}
