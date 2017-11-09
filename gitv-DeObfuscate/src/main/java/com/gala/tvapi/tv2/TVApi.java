package com.gala.tvapi.tv2;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tv2.constants.C0295a;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.tv2.p026c.C0289c;
import com.gala.tvapi.tv2.p026c.C0290a;
import com.gala.tvapi.tv2.p026c.C0293b;
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
    private static C0286c f951a = new C0289c();
    public static final ITVApiServer<ApiResultAlbum> albumInfo = buildDefaultApi(C0295a.f986A, ApiResultAlbum.class, "albumInfo", false, true);
    public static final ITVApiServer<ApiResultAlbumList> albumList = buildSearchApi(C0295a.f987B, ApiResultAlbumList.class, WebConstants.KEY_PLAY_ALBUMLIST, true, false);
    public static final ITVApiServer<ApiResultAlbumList> albumSearch = buildSearchApi(C0295a.f989D, ApiResultAlbumList.class, "albumSearch", true, false);
    public static final ITVApiServer<ApiResultTVChannelListCarousel> channelCarousel = buildDefaultApi(C0295a.af, ApiResultTVChannelListCarousel.class, "channelCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultChannelList> channelList = buildDefaultApi(C0295a.f1014z, ApiResultChannelList.class, "chnList", true, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheck = buildDeviceCheckApi(C0295a.f1012x, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheckForUpdate = buildDeviceCheckForUpdateApi(C0295a.f1012x, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> deviceCheckP = buildDeviceCheckApi(C0295a.f1013y, ApiResultDeviceCheck.class, "devRegister", false, true);
    public static final ITVApiServer<ApiResultDeviceCheck> dynamicQ = buildDynamicQApi(C0295a.f997L, ApiResultDeviceCheck.class, "dynamicQ", true, true);
    public static final ITVApiServer<ApiResultDeviceCheck> dynamicQP = buildDynamicQApi(C0295a.f998M, ApiResultDeviceCheck.class, "dynamicQ", true, true);
    public static final ITVApiServer<ApiResultEpisodeList> episodeList = buildDefaultApi(C0295a.f990E, ApiResultEpisodeList.class, "albumVideo", true, true);
    public static final ITVApiServer<ApiResultTrailersList> episodeVideo = buildDefaultApi(C0295a.f991F, ApiResultTrailersList.class, "episodeVideo", false, true);
    public static final ITVApiServer<ApiResultCode> feedbackState = buildStateApi(C0295a.ae, ApiResultCode.class, "feedbackState", false, true);
    public static final ITVApiServer<ApiResultHotWords> hotWords = buildSearchApi(C0295a.f993H, ApiResultHotWords.class, "searchHotWords", false, true);
    public static final ITVApiServer<ApiResultLet2kb> let2kb = buildDefaultApi(C0295a.f1005T, ApiResultLet2kb.class, "let2kb", false, true);
    public static final ITVApiServer<ApiResultManInfo> manInfo = C0214a.m581a(new C0277h(C0295a.f1003R), new C0290a(), ApiResultManInfo.class, "manInfo", false, true);
    public static final ITVApiServer<ApiResultModuleUpdate> moduleUpdate = moduleUpdateApi(C0295a.f1008W, ApiResultModuleUpdate.class, "moduleUpdate", false, true);
    public static final ITVApiServer<ApiResultAlbumList> newestList = buildSearchApi(C0295a.f1001P, ApiResultAlbumList.class, "newestList", false, false);
    public static final ITVApiServer<ApiResultTVNextProgramListCarousel> nextProgramCarousel = buildDefaultApi(C0295a.ah, ApiResultTVNextProgramListCarousel.class, "nextProgramCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultCode> playCheck = buildPlayerApi(C0295a.f988C, ApiResultCode.class, "playCheck", false, true);
    public static final ITVApiServer<ApiResultCode> playCheckLive = buildDefaultApi(C0295a.aa, ApiResultCode.class, "playCheckLive", false, true);
    public static final ITVApiServer<ApiResultPlayFlag> playFlag = buildDefaultApi(C0295a.f996K, ApiResultPlayFlag.class, "playFlag", false, true);
    public static final ITVApiServer<ApiResultTVProgramListCarousel> programListCarousel = buildDefaultApi(C0295a.ag, ApiResultTVProgramListCarousel.class, "programListCarousel_TV", false, true);
    public static final ITVApiServer<ApiResultCode> queryState = buildStateApi(C0295a.ad, ApiResultCode.class, "queryState", false, true);
    public static final ITVApiServer<ApiResultRefreshTime> refreshTime = buildDefaultApi(C0295a.f1007V, ApiResultRefreshTime.class, "refreshTime", false, true);
    public static final ITVApiServer<ApiResultAlbumList> searchPersonAlbums = buildSearchApi(C0295a.f995J, ApiResultAlbumList.class, ISearchConstant.SUGGEST_TYPE_PERSON, true, false);
    public static final ITVApiServer<ApiResultSearchPy> searchPy = buildSearchApi(C0295a.f1011Z, ApiResultSearchPy.class, "searchPy", false, true);
    public static final ITVApiServer<ApiResultStars> stars = buildDefaultApi(C0295a.ab, ApiResultStars.class, "stars", false, true);
    public static final ITVApiServer<ApiResultHotWords> suggestWords = buildSearchApi(C0295a.f994I, ApiResultHotWords.class, "searchRealTime", false, false);
    public static final ITVApiServer<ApiResultSysTime> sysTime = buildDefaultApi(C0295a.f1002Q, ApiResultSysTime.class, "sysTime", false, true);
    public static final ITVApiServer<ApiResultTabInfo> tabInfo = buildDefaultApi(C0295a.f1006U, ApiResultTabInfo.class, "tabinfo", false, true);
    public static final ITVApiServer<ApiResultTheme> theme = buildDefaultApi(C0295a.ac, ApiResultTheme.class, "theme", false, true);
    public static final ITVApiServer<ApiResultTinyurl> tinyurl = buildCommonApi(C0295a.f1009X, ApiResultTinyurl.class, "tinyurl", false, true);
    public static final ITVApiServer<ApiResultAlbumList> topList = buildSearchApi(C0295a.f1000O, ApiResultAlbumList.class, "topList", false, false);
    public static final ITVApiServer<ApiResultVrsEpisodeList> vrsEpisodeList = buildOldApi(C0295a.f992G, ApiResultVrsEpisodeList.class, "vrsVideoList", true, true);
    public static final ITVApiServer<ApiResultVrsTv2TvQId> vrsTvId2tvQid = buildCommonApi(C0295a.f999N, ApiResultVrsTv2TvQId.class, "vrsTvId2tvQid", false, true);
    public static final ITVApiServer<ApiResultWaitOnline> waitOnline = buildSearchApi(C0295a.f1010Y, ApiResultWaitOnline.class, "waitOnline", false, true);

    static class C0270a implements IApiUrlBuilder {
        private String f936a;

        public C0270a(String str) {
            this.f936a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m647a(this.f936a, args);
            }
            return TVApiBase.m646a(this.f936a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return null;
        }
    }

    static class C0271b implements IApiUrlBuilder {
        private String f937a;

        public C0271b(String str) {
            this.f937a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m646a(this.f937a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.m646a(this.f937a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    static class C0272c implements IApiUrlBuilder {
        private String f938a;
        private String f939b;

        public C0272c(String str) {
            this.f938a = str;
        }

        public final String build(String... args) {
            if (!TVApiBase.m648a(args)) {
                return TVApiBase.m646a(this.f938a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
            }
            this.f939b = args[2];
            if (args.length == 3) {
                this.f938a = C0295a.f1012x;
            } else if (args.length == 4) {
                this.f938a = C0295a.f1013y;
            }
            return TVApiBase.m651b(this.f938a, args);
        }

        public final List<String> header() {
            List<String> arrayList = new ArrayList(1);
            arrayList.add("apkVer:" + this.f939b);
            return arrayList;
        }
    }

    static class C0273d implements IApiUrlBuilder {
        private String f940a;

        public C0273d(String str) {
            this.f940a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m645a(this.f940a, TVApiBase.m642a(), TVApiBase.m649b(), args);
            }
            return TVApiBase.m645a(this.f940a, TVApiBase.m642a(), TVApiBase.m649b(), new String[0]);
        }

        public final List<String> header() {
            return C0214a.mo829a();
        }
    }

    static class C0274e implements IApiUrlBuilder {
        private String f941a;

        public C0274e(String str) {
            this.f941a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m646a(this.f941a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.m646a(this.f941a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    static class C0275f implements IApiUrlBuilder {
        private String f942a;

        public C0275f(String str) {
            this.f942a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m645a(this.f942a, TVApiBase.getTVApiProperty().getApiKey(), "0", args);
            }
            return TVApiBase.m645a(this.f942a, TVApiBase.getTVApiProperty().getApiKey(), "0", new String[0]);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    static class C0276g implements IApiUrlBuilder {
        private String f943a;

        public C0276g(String str) {
            this.f943a = str;
        }

        public final String build(String... args) {
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m646a(this.f943a, TVApiBase.getTVApiProperty().getAuthId(), args);
            }
            return TVApiBase.m646a(this.f943a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
        }

        public final List<String> header() {
            List<String> b = C0214a.mo830b();
            if (TVApiBase.getTVApiProperty().isOpenOverSea()) {
                b.add("oversea:" + TVApiBase.getTVApiProperty().getHideString());
            }
            return b;
        }
    }

    static class C0277h implements IApiUrlBuilder {
        private String f944a;

        public C0277h(String str) {
            this.f944a = str;
        }

        public final String build(String... arg0) {
            if (!TVApiBase.m648a(arg0)) {
                return this.f944a;
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
            return TVApiBase.m647a(this.f944a, arg0[0], str);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    static class C0278i implements IApiUrlBuilder {
        private String f945a;

        public C0278i(String str) {
            this.f945a = str;
        }

        public final String build(String... args) {
            int i = 1;
            if (!TVApiBase.m648a(args)) {
                return TVApiBase.m646a(this.f945a, TVApiBase.getTVApiProperty().getAuthId(), new String[0]);
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
            return TVApiBase.m646a(this.f945a, TVApiBase.getTVApiProperty().getAuthId(), strArr);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    public static class C0279j implements IApiUrlBuilder {
        private String f946a;

        public C0279j(String str) {
            this.f946a = str;
        }

        public final String build(String... args) {
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            if (TVApiBase.m648a(args)) {
                return TVApiBase.m644a(this.f946a, tVApiProperty.getAuthId(), tVApiProperty.getAnonymity(), tVApiProperty.getUid(), args);
            }
            return TVApiBase.m644a(this.f946a, tVApiProperty.getAuthId(), tVApiProperty.getAnonymity(), tVApiProperty.getUid(), new String[0]);
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    public static class C0280k implements IApiUrlBuilder {
        private String f947a;

        public C0280k(String str) {
            this.f947a = str;
        }

        public final String build(String... args) {
            int i = 0;
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            if (TVApiBase.m648a(args)) {
                int length = args.length;
                String[] strArr = new String[(length + 3)];
                strArr[0] = tVApiProperty.getAuthId();
                strArr[1] = tVApiProperty.getMacAddress();
                strArr[2] = tVApiProperty.getVersion();
                while (i < length) {
                    strArr[i + 3] = args[i];
                    i++;
                }
                return TVApiBase.m647a(this.f947a, strArr);
            }
            return TVApiBase.m647a(this.f947a, tVApiProperty.getAuthId(), tVApiProperty.getMacAddress(), tVApiProperty.getVersion());
        }

        public final List<String> header() {
            return C0214a.mo830b();
        }
    }

    public static final <T extends ApiResult> ITVApiServer<T> moduleUpdateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0278i(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDefaultApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0271b(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildOldApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0275f(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDeviceCheckApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0273d(url), new C0293b(), (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildPlayerApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0276g(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildCommonApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0270a(url), new C0290a(), (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDynamicQApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0274e(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildDeviceCheckForUpdateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0272c(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildSearchApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0279j(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }

    public static final <T extends ApiResult> ITVApiServer<T> buildStateApi(String url, Class<T> clazz, String name, boolean supportPostRequest, boolean isNeedPrintResult) {
        return C0214a.m581a(new C0280k(url), f951a, (Class) clazz, name, supportPostRequest, isNeedPrintResult);
    }
}
