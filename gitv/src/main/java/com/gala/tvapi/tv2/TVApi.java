package com.gala.tvapi.tv2;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.tvapi.tv2.result.ApiResultEpisodeList;
import com.gala.tvapi.tv2.result.ApiResultHotWords;
import com.gala.tvapi.tv2.result.ApiResultLet2kb;
import com.gala.tvapi.tv2.result.ApiResultManInfo;
import com.gala.tvapi.tv2.result.ApiResultModuleUpdate;
import com.gala.tvapi.tv2.result.ApiResultPlayFlag;
import com.gala.tvapi.tv2.result.ApiResultRefreshTime;
import com.gala.tvapi.tv2.result.ApiResultSearchPy;
import com.gala.tvapi.tv2.result.ApiResultStars;
import com.gala.tvapi.tv2.result.ApiResultSysTime;
import com.gala.tvapi.tv2.result.ApiResultTVChannelListCarousel;
import com.gala.tvapi.tv2.result.ApiResultTVNextProgramListCarousel;
import com.gala.tvapi.tv2.result.ApiResultTVProgramListCarousel;
import com.gala.tvapi.tv2.result.ApiResultTabInfo;
import com.gala.tvapi.tv2.result.ApiResultTheme;
import com.gala.tvapi.tv2.result.ApiResultTinyurl;
import com.gala.tvapi.tv2.result.ApiResultTrailersList;
import com.gala.tvapi.tv2.result.ApiResultVrsEpisodeList;
import com.gala.tvapi.tv2.result.ApiResultVrsTv2TvQId;
import com.gala.tvapi.tv2.result.ApiResultWaitOnline;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.webview.utils.WebSDKConstants;
import java.util.ArrayList;
import java.util.List;

public class TVApi extends TVApiBase {
    private static com.gala.tvapi.tv2.a.c a = new com.gala.tvapi.tv2.c.c();
    public static final ITVApiServer<ApiResultAlbum> albumInfo = buildDefaultApi(com.gala.tvapi.tv2.constants.a.A, ApiResultAlbum.class, "albumInfo", false, true);
    public static final ITVApiServer<ApiResultAlbumList> albumList = buildSearchApi(com.gala.tvapi.tv2.constants.a.B, ApiResultAlbumList.class, WebConstants.KEY_PLAY_ALBUMLIST, true, false);
    public static final ITVApiServer<ApiResultAlbumList> albumSearch = buildSearchApi(com.gala.tvapi.tv2.constants.a.D, ApiResultAlbumList.class, "albumSearch", true, false);
    public static final ITVApiServer<ApiResultTVChannelListCarousel> channelCarousel = buildDefaultApi(com.gala.tvapi.tv2.constants.a.af, ApiResultTVChannelListCarousel.class, "channelCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultChannelList> channelList = buildDefaultApi(com.gala.tvapi.tv2.constants.a.z, ApiResultChannelList.class, "chnList", true, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheck = buildDeviceCheckApi(com.gala.tvapi.tv2.constants.a.x, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheckForUpdate = buildDeviceCheckForUpdateApi(com.gala.tvapi.tv2.constants.a.x, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheckP = buildDeviceCheckApi(com.gala.tvapi.tv2.constants.a.y, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> dynamicQ = buildDynamicQApi(com.gala.tvapi.tv2.constants.a.L, ApiResultDeviceCheck.class, "dynamicQ", true, true);
    public static final ITVApiServer<ApiResultDeviceCheck> dynamicQP = buildDynamicQApi(com.gala.tvapi.tv2.constants.a.M, ApiResultDeviceCheck.class, "dynamicQ", true, true);
    public static final ITVApiServer<ApiResultEpisodeList> episodeList = buildDefaultApi(com.gala.tvapi.tv2.constants.a.E, ApiResultEpisodeList.class, "albumVideo", true, true);
    public static final ITVApiServer<ApiResultTrailersList> episodeVideo = buildDefaultApi(com.gala.tvapi.tv2.constants.a.F, ApiResultTrailersList.class, "episodeVideo", false, true);
    public static final ITVApiServer<ApiResultCode> feedbackState = buildStateApi(com.gala.tvapi.tv2.constants.a.ae, ApiResultCode.class, "feedbackState", false, true);
    public static final ITVApiServer<ApiResultHotWords> hotWords = buildSearchApi(com.gala.tvapi.tv2.constants.a.H, ApiResultHotWords.class, "searchHotWords", false, true);
    public static final ITVApiServer<ApiResultLet2kb> let2kb = buildDefaultApi(com.gala.tvapi.tv2.constants.a.T, ApiResultLet2kb.class, "let2kb", false, true);
    public static final ITVApiServer<ApiResultManInfo> manInfo = com.gala.tvapi.b.a.a(new h(com.gala.tvapi.tv2.constants.a.R), new com.gala.tvapi.tv2.c.a(), ApiResultManInfo.class, "manInfo", false, true);
    public static final ITVApiServer<ApiResultModuleUpdate> moduleUpdate = moduleUpdateApi(com.gala.tvapi.tv2.constants.a.W, ApiResultModuleUpdate.class, "moduleUpdate", false, true);
    public static final ITVApiServer<ApiResultAlbumList> newestList = buildSearchApi(com.gala.tvapi.tv2.constants.a.P, ApiResultAlbumList.class, "newestList", false, false);
    public static final ITVApiServer<ApiResultTVNextProgramListCarousel> nextProgramCarousel = buildDefaultApi(com.gala.tvapi.tv2.constants.a.ah, ApiResultTVNextProgramListCarousel.class, "nextProgramCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultCode> playCheck = buildPlayerApi(com.gala.tvapi.tv2.constants.a.C, ApiResultCode.class, "playCheck", false, true);
    public static final ITVApiServer<ApiResultCode> playCheckLive = buildDefaultApi(com.gala.tvapi.tv2.constants.a.aa, ApiResultCode.class, "playCheckLive", false, true);
    public static final ITVApiServer<ApiResultPlayFlag> playFlag = buildDefaultApi(com.gala.tvapi.tv2.constants.a.K, ApiResultPlayFlag.class, "playFlag", false, true);
    public static final ITVApiServer<ApiResultTVProgramListCarousel> programListCarousel = buildDefaultApi(com.gala.tvapi.tv2.constants.a.ag, ApiResultTVProgramListCarousel.class, "programListCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultCode> queryState = buildStateApi(com.gala.tvapi.tv2.constants.a.ad, ApiResultCode.class, "queryState", false, true);
    public static final ITVApiServer<ApiResultRefreshTime> refreshTime = buildDefaultApi(com.gala.tvapi.tv2.constants.a.V, ApiResultRefreshTime.class, "refreshTime", false, true);
    public static final ITVApiServer<ApiResultAlbumList> searchPersonAlbums = buildSearchApi(com.gala.tvapi.tv2.constants.a.J, ApiResultAlbumList.class, ISearchConstant.SUGGEST_TYPE_PERSON, true, false);
    public static final ITVApiServer<ApiResultSearchPy> searchPy = buildSearchApi(com.gala.tvapi.tv2.constants.a.Z, ApiResultSearchPy.class, "searchPy", false, true);
    public static final ITVApiServer<ApiResultStars> stars = buildDefaultApi(com.gala.tvapi.tv2.constants.a.ab, ApiResultStars.class, "stars", false, true);
    public static final ITVApiServer<ApiResultHotWords> suggestWords = buildSearchApi(com.gala.tvapi.tv2.constants.a.I, ApiResultHotWords.class, "searchRealTime", false, false);
    public static final ITVApiServer<ApiResultSysTime> sysTime = buildDefaultApi(com.gala.tvapi.tv2.constants.a.Q, ApiResultSysTime.class, "sysTime", false, true);
    public static final ITVApiServer<ApiResultTabInfo> tabInfo = buildDefaultApi(com.gala.tvapi.tv2.constants.a.U, ApiResultTabInfo.class, "tabinfo", false, true);
    public static final ITVApiServer<ApiResultTheme> theme = buildDefaultApi(com.gala.tvapi.tv2.constants.a.ac, ApiResultTheme.class, "theme", false, true);
    public static final ITVApiServer<ApiResultTinyurl> tinyurl = buildCommonApi(com.gala.tvapi.tv2.constants.a.X, ApiResultTinyurl.class, "tinyurl", false, true);
    public static final ITVApiServer<ApiResultAlbumList> topList = buildSearchApi(com.gala.tvapi.tv2.constants.a.O, ApiResultAlbumList.class, "topList", false, false);
    public static final ITVApiServer<ApiResultVrsEpisodeList> vrsEpisodeList = buildOldApi(com.gala.tvapi.tv2.constants.a.G, ApiResultVrsEpisodeList.class, "vrsVideoList", true, true);
    public static final ITVApiServer<ApiResultVrsTv2TvQId> vrsTvId2tvQid = buildCommonApi(com.gala.tvapi.tv2.constants.a.N, ApiResultVrsTv2TvQId.class, "vrsTvId2tvQid", false, true);
    public static final ITVApiServer<ApiResultWaitOnline> waitOnline = buildSearchApi(com.gala.tvapi.tv2.constants.a.Y, ApiResultWaitOnline.class, "waitOnline", false, true);

    static class a implements IApiUrlBuilder {
        private String a;

        public a(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, args);
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return null;
        }
    }

    static class b implements IApiUrlBuilder {
        private String a;

        public b(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    static class c implements IApiUrlBuilder {
        private String a;
        private String b;

        public c(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (!TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
            }
            this.b = args[2];
            if (args.length == 3) {
                this.a = com.gala.tvapi.tv2.constants.a.x;
            } else if (args.length == 4) {
                this.a = com.gala.tvapi.tv2.constants.a.y;
            }
            return TVApiBase.b(this.a, args);
        }

        public final List<String> header() {
            List<String> arrayList = new ArrayList(1);
            arrayList.add("apkVer:" + this.b);
            return arrayList;
        }
    }

    static class d implements IApiUrlBuilder {
        private String a;

        public d(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.a(), TVApiBase.b(), args);
            }
            return TVApiBase.a(this.a, TVApiBase.a(), TVApiBase.b(), new String[0]);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.a();
        }
    }

    static class e implements IApiUrlBuilder {
        private String a;

        public e(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    static class f implements IApiUrlBuilder {
        private String a;

        public f(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getApiKey(), "0", args);
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getApiKey(), "0", new String[0]);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    static class g implements IApiUrlBuilder {
        private String a;

        public g(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            List<String> b = com.gala.tvapi.b.a.b();
            if (TVApiBase.getTVApiProperty().isOpenOverSea()) {
                b.add("oversea:" + TVApiBase.getTVApiProperty().getHideString());
            }
            return b;
        }
    }

    static class h implements IApiUrlBuilder {
        private String a;

        public h(String str) {
            this.a = str;
        }

        public final String build(String... arg0) {
            if (!TVApiBase.a(arg0)) {
                return this.a;
            }
            PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
            String str = "0";
            if (platform == PlatformType.HAIXIN) {
                str = "0";
            } else if (platform == PlatformType.ALIVIP) {
                str = "1";
            } else if (platform == PlatformType.SKYWORTH_VIPPROJECT) {
                str = "2";
            } else if (platform == PlatformType.LENOVOSHIYUNVIP) {
                str = "3";
            }
            return TVApiBase.a(this.a, arg0[0], str);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    static class i implements IApiUrlBuilder {
        private String a;

        public i(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            int i = 1;
            if (!TVApiBase.a(args)) {
                return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
            }
            JSONObject parseObject = JSON.parseObject(args[0]);
            parseObject.put("platModel", Build.MODEL);
            parseObject.put("prodModel", Build.PRODUCT);
            parseObject.put("osVer", Integer.valueOf(VERSION.SDK_INT));
            parseObject.put("chipVer", TVApiBase.getTVApiProperty().getHardware());
            parseObject.put("mem", TVApiBase.getTVApiProperty().getMemorySize());
            parseObject.put(WebSDKConstants.PARAM_KEY_MAC, TVApiBase.getTVApiProperty().getMacAddress());
            parseObject.put(WebConstants.IP, TVApiBase.getTVApiProperty().getIpAddress_server());
            String[] strArr = new String[(args.length + 1)];
            strArr[0] = TVApiBase.getTVApiProperty().getHostVersion();
            strArr[1] = parseObject.toJSONString();
            while (i < args.length) {
                strArr[i + 1] = args[i];
                i++;
            }
            return TVApiBase.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), strArr);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    public static class j implements IApiUrlBuilder {
        private String a;

        public j(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            if (TVApiBase.a(args)) {
                return TVApiBase.a(this.a, tVApiProperty.getAuthId(), tVApiProperty.getAnonymity(), tVApiProperty.getUid(), args);
            }
            return TVApiBase.a(this.a, tVApiProperty.getAuthId(), tVApiProperty.getAnonymity(), tVApiProperty.getUid(), new String[0]);
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    public static class k implements IApiUrlBuilder {
        private String a;

        public k(String str) {
            this.a = str;
        }

        public final String build(String... args) {
            int i = 0;
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            if (TVApiBase.a(args)) {
                int length = args.length;
                String[] strArr = new String[(length + 3)];
                strArr[0] = tVApiProperty.getAuthId();
                strArr[1] = tVApiProperty.getMacAddress();
                strArr[2] = tVApiProperty.getVersion();
                while (i < length) {
                    strArr[i + 3] = args[i];
                    i++;
                }
                return TVApiBase.a(this.a, strArr);
            }
            return TVApiBase.a(this.a, tVApiProperty.getAuthId(), tVApiProperty.getMacAddress(), tVApiProperty.getVersion());
        }

        public final List<String> header() {
            return com.gala.tvapi.b.a.b();
        }
    }

    public static final <T extends ApiResult> ITVApiServer<T> moduleUpdateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new i(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDefaultApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new b(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildOldApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new f(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDeviceCheckApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new d(url), new com.gala.tvapi.tv2.c.b(), (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildPlayerApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new g(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildCommonApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new a(url), new com.gala.tvapi.tv2.c.a(), (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDynamicQApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new e(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDeviceCheckForUpdateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new c(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildSearchApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new j(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildStateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return com.gala.tvapi.b.a.a(new k(url), a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }
}
