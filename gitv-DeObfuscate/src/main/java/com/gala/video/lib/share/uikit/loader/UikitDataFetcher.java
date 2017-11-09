package com.gala.video.lib.share.uikit.loader;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.loader.data.AppRequest;
import com.gala.video.lib.share.uikit.loader.data.BannerAdRequest;
import com.gala.video.lib.share.uikit.loader.data.ChannelListRequst;
import com.gala.video.lib.share.uikit.loader.data.GroupDetailRequest;

public class UikitDataFetcher {
    public static void callGroupDetail(String sourceId, int pageNo, int uikitEngineId, boolean isVip, boolean isNeedfetch, IUikitDataFetcherCallback callback) {
        GroupDetailRequest.callGroupDetail(sourceId, pageNo, uikitEngineId, isVip, isNeedfetch, callback);
    }

    public static void callChannelList(CardInfoModel model, IUikitDataFetcherCallback callback) {
        ChannelListRequst.callChannelList(model, callback);
    }

    public static void callApp(CardInfoModel model, IUikitDataFetcherCallback callback) {
        AppRequest.callApp(model, callback);
    }

    public static void callBannerAd(int channelId, String albumId, String tvQid, boolean isVip, IUikitDataFetcherCallback callback) {
        BannerAdRequest.callBannerAd(channelId, albumId, tvQid, isVip, callback, false);
    }

    public static void callBannerAd(int channelId, String albumId, String tvQid, boolean isVip, boolean isBatchCallback, IUikitDataFetcherCallback callback) {
        BannerAdRequest.callBannerAd(channelId, albumId, tvQid, isVip, isBatchCallback, callback, true);
    }
}
