package com.gala.video.app.player.albumdetail.data.task;

import android.os.SystemClock;
import com.gala.tvapi.type.UserType;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.data.DetailDataCacheManager;
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
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.model.AdResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner.BannerAdResultModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FetchBannerAdTask {
    private static String AD_URL_DETAIL_BANNER = (WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s");
    private static String MIXER = "mixer.cupid.ptqy.gitv.tv";
    private static String TAG;
    private List<BannerImageAdModel> mAds = null;
    private AdsClient mAdsClient;
    private String mAlbumId = "";
    private String mChannelId = "";
    private String mId = "";
    private IFetchBannerAdListener mListener;
    private String mResponse = "";
    private long mStartTime;
    private String mTvQId = "";

    public interface IFetchBannerAdListener {
        void onFailed(ApiException apiException);

        void onSuccess(BannerImageAdModel bannerImageAdModel);
    }

    public FetchBannerAdTask(AlbumInfo albumInfo) {
        TAG = "FetchBannerAdTask" + hashCode();
        this.mAlbumId = albumInfo.getAlbumId();
        this.mTvQId = albumInfo.getTvId();
        this.mChannelId = String.valueOf(albumInfo.getChannelId());
        this.mAdsClient = AdsClientUtils.getInstance();
    }

    public void setTaskListener(IFetchBannerAdListener listener) {
        this.mListener = listener;
    }

    public void setNowId(String id) {
        LogUtils.d(TAG, "FetchBannerAdTask id = " + id);
        this.mId = id;
    }

    private String getDetailBannerUrl(String albumId, String tvQid, String channelId) {
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        LogUtils.d(TAG, "getDetailBannerUrl --- pi = " + pi + " pc = " + pc + " isVip = " + isVip);
        String str = AD_URL_DETAIL_BANNER;
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
        r10[28] = "607";
        String url = UrlUtils.urlFormat(str, r10);
        LogUtils.d(TAG, "getDetailBannerUrl ---url is " + url);
        String response = "";
        try {
            HttpUtil hu = new HttpUtil(url);
            LogUtils.d(TAG, "FetchBannerAdTask hu = " + hu);
            response = hu.get();
        } catch (Exception e) {
            LogUtils.e(TAG, "Exception= " + e);
        }
        LogUtils.d(TAG, "FetchBannerAdTask response = " + response);
        return response;
    }

    private void handleAdResult(AdResult result) {
        long elapse = SystemClock.elapsedRealtime() - this.mStartTime;
        LogUtils.d(TAG, "fetch advertisement time cost : " + elapse);
        String st = "";
        if (StringUtils.isEmpty(result.ad)) {
            this.mAdsClient.onRequestMobileServerFailed();
            this.mAdsClient.sendAdPingBacks();
            LogUtils.d(TAG, "error result");
            st = MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        } else {
            BannerAdResultModel model = CreateInterfaceTools.createBannerAdProvider().parseAd(this.mAdsClient, result.ad);
            List listAd = model.getModels();
            DetailDataCacheManager.instance().putBannerResultId(model.getResultId());
            if (ListUtils.getCount(listAd) > 0) {
                LogUtils.d(TAG, "has " + ListUtils.getCount(listAd) + " ad");
                this.mAds = new ArrayList();
                this.mAds.addAll(listAd);
                st = "1";
            } else {
                LogUtils.d(TAG, "no ad");
                st = "0";
            }
        }
        PingBackParams pingbackInfos = new PingBackParams();
        pingbackInfos.add(Keys.T, "11");
        pingbackInfos.add("ct", "150619_request");
        pingbackInfos.add(Keys.RI, "ad_banner_detail");
        pingbackInfos.add("td", String.valueOf(elapse));
        pingbackInfos.add("st", st);
        PingBack.getInstance().postPingBackToLongYuan(pingbackInfos.build());
    }

    public void execute(final String id) {
        new Thread8K(new Runnable() {
            public void run() {
                LogUtils.d(FetchBannerAdTask.TAG, "on run : ");
                if (ListUtils.isEmpty(FetchBannerAdTask.this.mAds)) {
                    FetchBannerAdTask.this.mStartTime = SystemClock.elapsedRealtime();
                    AdResult result = new AdResult();
                    LogUtils.d(FetchBannerAdTask.TAG, "on run : mAlbumId" + FetchBannerAdTask.this.mAlbumId + "mTvQId" + FetchBannerAdTask.this.mTvQId + "mChannelId" + FetchBannerAdTask.this.mChannelId);
                    result.ad = FetchBannerAdTask.this.getDetailBannerUrl(FetchBannerAdTask.this.mAlbumId, FetchBannerAdTask.this.mTvQId, FetchBannerAdTask.this.mChannelId);
                    FetchBannerAdTask.this.handleAdResult(result);
                }
                if (ListUtils.isEmpty(FetchBannerAdTask.this.mAds)) {
                    FetchBannerAdTask.this.mListener.onFailed(null);
                    return;
                }
                for (BannerImageAdModel ad : FetchBannerAdTask.this.mAds) {
                    if (StringUtils.equals(id, ad.getAdZoneId())) {
                        FetchBannerAdTask.this.mListener.onSuccess(ad);
                        LogUtils.d(FetchBannerAdTask.TAG, "success fetch ad zone id is->" + id);
                        LogUtils.d(FetchBannerAdTask.TAG, "success fetch ad id is->" + ad.getAdId());
                        return;
                    }
                }
                FetchBannerAdTask.this.mListener.onSuccess(null);
                LogUtils.d(FetchBannerAdTask.TAG, "success but no ad");
            }
        }, "FetchBannerAdTask").start();
    }
}
