package com.gala.video.app.epg.ui.search.task;

import android.os.SystemClock;
import com.gala.tvapi.type.UserType;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.search.ad.Constants;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.UrlUtils;
import com.gala.video.lib.framework.core.utils.io.HttpUtil;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FetchSearchBannerAdTask {
    private static String AD_URL__SEARCH_BANNER = (WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s");
    private static String MIXER = "mixer.cupid.ptqy.gitv.tv";
    private static String TAG = "FetchSearchBannerAdTask";
    private List<BannerImageAdModel> mAds = null;
    private AdsClient mAdsClient = AdsClientUtils.getInstance();
    private String mId = "";
    private IFetchBannerAdListener mListener;
    private String mResponse = "";
    private long mStartTime;

    class C10191 implements Runnable {
        C10191() {
        }

        public void run() {
            LogUtils.m1568d(FetchSearchBannerAdTask.TAG, "on run : ");
            if (ListUtils.isEmpty(FetchSearchBannerAdTask.this.mAds)) {
                FetchSearchBannerAdTask.this.mStartTime = SystemClock.elapsedRealtime();
                FetchSearchBannerAdTask.this.handleAdResult(FetchSearchBannerAdTask.this.fetchSearchBannerAdJson());
            }
            if (ListUtils.isEmpty(FetchSearchBannerAdTask.this.mAds)) {
                FetchSearchBannerAdTask.this.mListener.onFailed(null);
                return;
            }
            FetchSearchBannerAdTask.this.mListener.onSuccess((BannerImageAdModel) FetchSearchBannerAdTask.this.mAds.get(0));
        }
    }

    public interface IFetchBannerAdListener {
        void onFailed(ApiException apiException);

        void onSuccess(BannerImageAdModel bannerImageAdModel);
    }

    public void setTaskListener(IFetchBannerAdListener listener) {
        this.mListener = listener;
    }

    private String fetchSearchBannerAdJson() {
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        LogUtils.m1568d(TAG, "getSearchBannerUrl --- pi = " + pi + " pc = " + pc + " isVip = " + isVip);
        String str = AD_URL__SEARCH_BANNER;
        r10 = new Object[29];
        AdsClient adsClient = this.mAdsClient;
        r10[17] = AdsClient.getSDKVersion();
        r10[18] = isVip;
        r10[19] = "";
        r10[20] = "1";
        r10[21] = "";
        r10[22] = "1";
        r10[23] = CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo();
        r10[24] = "";
        r10[25] = "";
        r10[26] = pi;
        r10[27] = pc;
        r10[28] = Constants.AD_AZT_SEARCH;
        String url = UrlUtils.urlFormat(str, r10);
        LogUtils.m1568d(TAG, "getSearchBannerUrl ---url is " + url);
        String response = "";
        try {
            HttpUtil hu = new HttpUtil(url);
            LogUtils.m1568d(TAG, "HttpUtil hu = " + hu);
            response = hu.get();
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "Exception= " + e);
        }
        LogUtils.m1568d(TAG, " the ad json response = " + response);
        return response;
    }

    private void handleAdResult(String adJson) {
        long elapse = SystemClock.elapsedRealtime() - this.mStartTime;
        LogUtils.m1568d(TAG, "fetch advertisement time cost : " + elapse);
        String st = "";
        if (StringUtils.isEmpty((CharSequence) adJson)) {
            this.mAdsClient.onRequestMobileServerFailed();
            this.mAdsClient.sendAdPingBacks();
            LogUtils.m1568d(TAG, "error result");
            st = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        } else {
            List listAd = CreateInterfaceTools.createBannerAdProvider().parseAd(this.mAdsClient, adJson).getModels();
            if (ListUtils.getCount(listAd) > 0) {
                LogUtils.m1568d(TAG, "has " + ListUtils.getCount(listAd) + " ad");
                this.mAds = new ArrayList();
                this.mAds.addAll(listAd);
                st = "1";
            } else {
                LogUtils.m1568d(TAG, "no ad");
                st = "0";
            }
        }
        PingBackParams pingbackInfos = new PingBackParams();
        pingbackInfos.add(Keys.f2035T, "11");
        pingbackInfos.add("ct", "150619_request");
        pingbackInfos.add(Keys.RI, "ad_banner_search");
        pingbackInfos.add("td", String.valueOf(elapse));
        pingbackInfos.add("st", st);
        PingBack.getInstance().postPingBackToLongYuan(pingbackInfos.build());
    }

    public void execute() {
        new Thread8K(new C10191(), "FetchSearchBannerAdTask#").start();
    }
}
