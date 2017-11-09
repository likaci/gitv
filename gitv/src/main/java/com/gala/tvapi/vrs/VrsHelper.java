package com.gala.tvapi.vrs;

import com.gala.tvapi.b.a;
import com.gala.tvapi.vrs.a.b;
import com.gala.tvapi.vrs.a.c;
import com.gala.tvapi.vrs.a.e;
import com.gala.tvapi.vrs.a.f;
import com.gala.tvapi.vrs.a.g;
import com.gala.tvapi.vrs.a.i;
import com.gala.tvapi.vrs.a.j;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.b.h;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultChannelListCarousel;
import com.gala.tvapi.vrs.result.ApiResultChannelPlayList;
import com.gala.tvapi.vrs.result.ApiResultChannelTable;
import com.gala.tvapi.vrs.result.ApiResultCheckScore;
import com.gala.tvapi.vrs.result.ApiResultDailyLabels;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.tvapi.vrs.result.ApiResultIChannelTable;
import com.gala.tvapi.vrs.result.ApiResultLiveM3u8;
import com.gala.tvapi.vrs.result.ApiResultMap;
import com.gala.tvapi.vrs.result.ApiResultMultiChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultPackageContent;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.tvapi.vrs.result.ApiResultProgramListCarousel;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.tvapi.vrs.result.ApiResultSetInitMovie;
import com.gala.tvapi.vrs.result.ApiResultUploadScore;
import com.gala.tvapi.vrs.result.ApiResultVVScalePM;
import com.gala.tvapi.vrs.result.ApiResultVideoInfo;
import com.gala.tvapi.vrs.result.ApiResultViewership;

public class VrsHelper extends BaseHelper {
    private static k a = new k();
    public static final IVrsServer<ApiResultChannelLabels> channelLabels = a.a(a.a(com.gala.tvapi.vrs.core.a.ak, true), a, ApiResultChannelLabels.class, "channelLable", false, false);
    public static final IVrsServer<ApiResultChannelLabels> channelLabelsFilter = a.a(a.a(com.gala.tvapi.vrs.core.a.al, true), a, ApiResultChannelLabels.class, "channelLable", false, false);
    public static final IVrsServer<ApiResultChannelLabels> channelLabelsInterval = a.a(a.a(com.gala.tvapi.vrs.core.a.ap, true), a, ApiResultChannelLabels.class, "channelLable", false, false);
    public static final IVrsServer<ApiResultChannelLabels> channelLabelsLive = a.a(a.a(com.gala.tvapi.vrs.core.a.am, true), a, ApiResultChannelLabels.class, "channelLable", false, false);
    public static final IVrsServer<ApiResultChannelLabels> channelLabelsSize = a.a(a.a(com.gala.tvapi.vrs.core.a.an, true), a, ApiResultChannelLabels.class, "channelLable", false, false);
    public static final IVrsServer<ApiResultChannelListCarousel> channelListCarousel = a.a(a.a(com.gala.tvapi.vrs.core.a.bH, true), a, ApiResultChannelListCarousel.class, "liveChannelList", false, true);
    public static final IVrsServer<ApiResultChannelPlayList> channelPlayList = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.ao), a, ApiResultChannelPlayList.class, "playList_channel", false, false);
    public static final IVrsServer<ApiResultChannelTable> channelTable = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.Y), a, ApiResultChannelTable.class, "channelTable", false, true);
    public static final IVrsServer<ApiResultCheckScore> checkUserScore = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aS), a, ApiResultCheckScore.class, "getUserMovieScore", false, true);
    public static final IVrsServer<ApiResultRecommendListQipu> dailyInfo = a.a(a.a(com.gala.tvapi.vrs.core.a.ae, true), a, ApiResultRecommendListQipu.class, "daily", false, true);
    public static final IVrsServer<ApiResultDailyLabels> dailyLabels = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.af), new b(), ApiResultDailyLabels.class, "tvPop", false, true);
    public static final IVrsServer<ApiResultGroupDetail> groupDetail = a.a(a.b(com.gala.tvapi.vrs.core.a.bK), a, ApiResultGroupDetail.class, "groupDetail", false, false);
    public static final IVrsServer<ApiResultGroupDetail> groupDetailPage = a.a(a.b(com.gala.tvapi.vrs.core.a.bL), a, ApiResultGroupDetail.class, "groupDetail_page", false, false);
    public static final IVrsServer<ApiResultRecommendListQipu> guessLikeAlbums = a.a(a.a(com.gala.tvapi.vrs.core.a.ad, true), a, ApiResultRecommendListQipu.class, "recommend", false, false);
    public static final IVrsServer<ApiResultIChannelTable> iChannelTable = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.Z), a, ApiResultIChannelTable.class, "iChannel", false, true);
    public static final IVrsServer<ApiResultLiveM3u8> liveM3u8Free = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.ag), new c(), ApiResultLiveM3u8.class, "livenM3u8Free");
    public static final IVrsServer<ApiResultLiveM3u8> liveM3u8Vip = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.ah), new c(), ApiResultLiveM3u8.class, "livenM3u8Vip");
    public static final IVrsServer<ApiResultMultiChannelLabels> multiChannelLabels = a.a(new h(com.gala.tvapi.vrs.core.a.aT), new f(), ApiResultMultiChannelLabels.class, "multiChannelLabel", false, false);
    public static final IVrsServer<ApiResultRecommendListQipu> multiRecommendThemeInfos = a.a(a.a(com.gala.tvapi.vrs.core.a.aN, true), new i(), ApiResultRecommendListQipu.class, "recommendMixinVideos", false, false);
    public static final IVrsServer<ApiResultProgramListCarousel> nextProgramCarousel = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.bJ), new g(), ApiResultProgramListCarousel.class, "nextLiveProgram", false, true);
    public static final IVrsServer<ApiResultPackageContent> packageContentOfAlbum = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aq), a, ApiResultPackageContent.class, "contentBuy", false, true);
    public static final IVrsServer<ApiResultVVScalePM> platformVVScale = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.ai), a, ApiResultVVScalePM.class, "vvScale", false, true);
    public static final IVrsServer<ApiResultPlayListQipu> playListQipu = a.a(a.a(com.gala.tvapi.vrs.core.a.aa, true), a, ApiResultPlayListQipu.class, "playList", false, false);
    public static final IVrsServer<ApiResultPlayListQipu> playListQipuPage = a.a(a.a(com.gala.tvapi.vrs.core.a.ab, true), a, ApiResultPlayListQipu.class, "playList_page", false, false);
    public static final IVrsServer<ApiResultProgramListCarousel> programListCarousel = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.bI), a, ApiResultProgramListCarousel.class, "liveProgramList", false, true);
    public static final IVrsServer<ApiResultRecommendListQipu> recommendListQipu = a.a(a.a(com.gala.tvapi.vrs.core.a.ac, true), a, ApiResultRecommendListQipu.class, "recommend", false, false);
    public static final IVrsServer<ApiResultRecommendListQipu> recommendThemes = a.a(a.a(com.gala.tvapi.vrs.core.a.aM, false), a, ApiResultRecommendListQipu.class, "recommendThemes", false, true);
    public static final IVrsServer<ApiResultSetInitMovie> setInitMovie = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aL), a, ApiResultSetInitMovie.class, "movieSetInit", false, true);
    public static final IVrsServer<ApiResultUploadScore> uploadUserScore = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aR), a, ApiResultUploadScore.class, "addMovieScore?", false, true);
    public static final IVrsServer<ApiResultVideoInfo> videoInfo = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.bD), a, ApiResultVideoInfo.class, "videoInfo", false, true);
    public static final IVrsServer<ApiResultViewership> viewership = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.aj), new j(), ApiResultViewership.class, "viewership", false, true);
    public static final IVrsServer<ApiResultMap> vrsTvId2tvQid = a.a(new BaseHelper.a(com.gala.tvapi.vrs.core.a.bM), new e(), ApiResultMap.class, "vrsTvId2tvQid", false, true);
}