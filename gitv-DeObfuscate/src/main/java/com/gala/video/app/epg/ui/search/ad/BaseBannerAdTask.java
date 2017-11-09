package com.gala.video.app.epg.ui.search.ad;

import android.os.SystemClock;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.HttpUtil;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner.BannerAdResultModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.constants.AdCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseBannerAdTask {
    private static String TAG = "BaseBannerAdTask";
    protected BaseAdUrlConfig mAdUrlConfigParams;
    private AdsClient mAdsClient = AdsClientUtils.getInstance();
    private List<BannerImageAdModel> mBannerImageAdModelList = null;
    private IFetchBannerAdListener mListener;
    private long mStartTime;

    class C09721 implements Runnable {
        C09721() {
        }

        public void run() {
            LogUtils.m1568d(BaseBannerAdTask.TAG, "on run : current name = " + Thread.currentThread().getName());
            if (ListUtils.isEmpty(BaseBannerAdTask.this.mBannerImageAdModelList)) {
                BaseBannerAdTask.this.mStartTime = SystemClock.elapsedRealtime();
                BaseBannerAdTask.this.handleAdJsonResult(BaseBannerAdTask.this.fetchBannerAdJson());
            }
            if (ListUtils.isEmpty(BaseBannerAdTask.this.mBannerImageAdModelList)) {
                BaseBannerAdTask.this.mListener.onFailed(null);
            } else {
                BaseBannerAdTask.this.mListener.onSuccess(BaseBannerAdTask.this.mBannerImageAdModelList);
            }
        }
    }

    protected abstract String getBannerUrl();

    public BaseBannerAdTask(BaseAdUrlConfig adUrlConfigParams, IFetchBannerAdListener listener) {
        this.mAdUrlConfigParams = adUrlConfigParams;
        this.mListener = listener;
    }

    public void execute() {
        new Thread8K(new C09721(), "BaseBannerAdTask#").start();
    }

    private String fetchBannerAdJson() {
        String response = "";
        try {
            HttpUtil hu = new HttpUtil(getBannerUrl());
            LogUtils.m1568d(TAG, "HttpUtil hu = " + hu);
            response = hu.get();
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "Exception= " + e);
        }
        LogUtils.m1568d(TAG, " the ad json response = " + response);
        return response;
    }

    private void handleAdJsonResult(String adJson) {
        long elapse = SystemClock.elapsedRealtime() - this.mStartTime;
        LogUtils.m1568d(TAG, "fetch advertisement time cost : " + elapse);
        String st = "";
        if (StringUtils.isEmpty((CharSequence) adJson)) {
            this.mAdsClient.onRequestMobileServerFailed();
            this.mAdsClient.sendAdPingBacks();
            LogUtils.m1568d(TAG, "error result");
            st = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        } else {
            BannerAdResultModel model = CreateInterfaceTools.createBannerAdProvider().parseAd(this.mAdsClient, adJson);
            List listAd = model.getModels();
            if (ListUtils.getCount(listAd) > 0) {
                LogUtils.m1568d(TAG, "has " + ListUtils.getCount(listAd) + " ad");
                this.mBannerImageAdModelList = new ArrayList();
                this.mBannerImageAdModelList.addAll(listAd);
                st = "1";
            } else {
                LogUtils.m1568d(TAG, "no ad");
                st = "0";
                AdsClientUtils.getInstance().onAdCardShowWithProperties(model.getResultId(), AdCard.AD_CARD_TV_BANNER, new HashMap());
            }
        }
        PingBackParams pingbackInfos = new PingBackParams();
        pingbackInfos.add(Keys.f2035T, "11");
        pingbackInfos.add("ct", "150619_request");
        pingbackInfos.add("td", String.valueOf(elapse));
        pingbackInfos.add("st", st);
        this.mListener.onSendPingback(pingbackInfos);
    }
}
