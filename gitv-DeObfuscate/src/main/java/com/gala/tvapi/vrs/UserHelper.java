package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.BaseHelper.C0328a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.C0376f;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.p031a.C0337a;
import com.gala.tvapi.vrs.p031a.C0349h;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.tvapi.vrs.result.ApiResultCollectList;
import com.gala.tvapi.vrs.result.ApiResultHistoryAlbumInfos;
import com.gala.tvapi.vrs.result.ApiResultHistoryList;
import com.gala.tvapi.vrs.result.ApiResultHistoryListForUser;
import com.gala.tvapi.vrs.result.ApiResultHistoryTvInfo;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.tvapi.vrs.result.ApiResultSubscribeState;
import com.gala.video.api.IApiUrlBuilder;
import java.util.List;

public class UserHelper extends BaseHelper {
    private static C0349h f1208a = new C0349h();
    private static C0336k f1209a = new C0336k();
    public static final IVrsServer<ApiResultCode> cancelCollect = C0214a.m585a(new C0335d(C0365a.f1253W), f1208a, ApiResultCode.class, "unsubscribe", false);
    public static final IVrsServer<ApiResultCode> cancelCollectForAnonymity = C0214a.m585a(new C0335d(C0365a.at), f1209a, ApiResultCode.class, "unsubscribe_anonymity", false);
    public static final IVrsServer<ApiResultCode> checkCollect = C0214a.m585a(new C0328a(C0365a.f1254X), f1208a, ApiResultCode.class, "isSubscribed", false);
    public static final IVrsServer<ApiResultCode> checkCollectForAnonymity = C0214a.m585a(new C0328a(C0365a.au), f1209a, ApiResultCode.class, "isSubscribed_anonymity", false);
    public static final IVrsServer<ApiResultKeepaliveInterval> checkVipAccount = C0214a.m581a(new C0332a(C0365a.aQ), new C0337a(), ApiResultKeepaliveInterval.class, "checkVipAccount", false, false);
    public static final IVrsServer<ApiResultCode> clearCollect = C0214a.m585a(new C0333b(C0365a.f1251U), f1208a, ApiResultCode.class, "deleteAllSubscriptions", false);
    public static final IVrsServer<ApiResultCode> clearCollectForAnonymity = C0214a.m585a(new C0333b(C0365a.ax), f1209a, ApiResultCode.class, "deleteAllSubscriptions_anonymity", false);
    public static final IVrsServer<ApiResultCode> clearHistory = C0214a.m585a(new C0328a(C0365a.f1234D), f1208a, ApiResultCode.class, "deleteAllHistory", false);
    public static final IVrsServer<ApiResultCode> clearHistoryForAnonymity = C0214a.m585a(new C0328a(C0365a.f1242L), f1209a, ApiResultCode.class, "deleteAllHistory_anonymity", false);
    public static final IVrsServer<ApiResultCollectList> collectList = C0214a.m585a(C0214a.m580a(C0365a.f1250T), f1208a, ApiResultCollectList.class, "collectList", true);
    public static final IVrsServer<ApiResultCollectList> collectListForAnonymity = C0214a.m585a(C0214a.m580a(C0365a.aw), f1209a, ApiResultCollectList.class, "collectList_anonymity", true);
    public static final IVrsServer<ApiResultCode> deleteHistoryAlbum = C0214a.m585a(new C0328a(C0365a.f1238H), f1208a, ApiResultCode.class, "deleteHistory", false);
    public static final IVrsServer<ApiResultCode> deleteHistoryAlbumForForAnonymity = C0214a.m585a(new C0328a(C0365a.f1249S), f1209a, ApiResultCode.class, "deleteHistory_anonymity", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyAlbumInfo = C0214a.m585a(new C0328a(C0365a.f1237G), f1208a, ApiResultHistoryTvInfo.class, "historyInfo", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyAlbumInfoForAnonymity = C0214a.m585a(new C0328a(C0365a.f1244N), f1209a, ApiResultHistoryTvInfo.class, "historyInfo_anonymity", false);
    public static final IVrsServer<ApiResultHistoryAlbumInfos> historyAlbumInfos = C0214a.m585a(new C0328a(C0365a.f1247Q), f1208a, ApiResultHistoryAlbumInfos.class, "historyInfos", false);
    public static final IVrsServer<ApiResultHistoryAlbumInfos> historyAlbumInfosForAnonymity = C0214a.m585a(new C0328a(C0365a.f1248R), f1209a, ApiResultHistoryAlbumInfos.class, "historyInfos_anonymity", false);
    public static final IVrsServer<ApiResultHistoryList> historyList = C0214a.m585a(new C0328a(C0365a.f1233C), f1208a, ApiResultHistoryList.class, "historyList", true);
    public static final IVrsServer<ApiResultHistoryList> historyListForAnonymity = C0214a.m585a(new C0328a(C0365a.f1241K), f1209a, ApiResultHistoryList.class, "historyList_anonymity", true);
    public static final IVrsServer<ApiResultHistoryListForUser> historyListForUser = C0214a.m585a(new C0328a(C0365a.f1235E), f1208a, ApiResultHistoryListForUser.class, "historyList", true);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyTvInfo = C0214a.m585a(new C0328a(C0365a.f1236F), f1208a, ApiResultHistoryTvInfo.class, "historyTVInfo", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyTvInfoForAnonymity = C0214a.m585a(new C0328a(C0365a.f1243M), f1209a, ApiResultHistoryTvInfo.class, "historyTVInfo_anonymity", false);
    public static final IVrsServer<ApiResultKeepaliveInterval> keepAlive = C0214a.m581a(new C0332a(C0365a.aP), f1209a, ApiResultKeepaliveInterval.class, "keepAlive", false, true);
    public static final IVrsServer<ApiResultKeepaliveInterval> keepAliveInterval = C0214a.m581a(new C0328a(C0365a.aO), f1209a, ApiResultKeepaliveInterval.class, "keepAliveInterval", false, true);
    public static final IVrsServer<ApiResultCode> mergeCollects = C0214a.m585a(new C0334c(C0365a.av), f1208a, ApiResultCode.class, "mergeCollect", false);
    public static final IVrsServer<ApiResultCode> mergeHistory = C0214a.m585a(new C0328a(C0365a.f1246P), f1208a, ApiResultCode.class, "mergeHistory", false);
    public static final IVrsServer<ApiResultCode> subscribe = C0214a.m581a(new C0332a(C0365a.bN), f1209a, ApiResultCode.class, "subscribe", false, true);
    public static final IVrsServer<ApiResultSubScribeList> subscribeList = C0214a.m581a(new C0332a(C0365a.bQ), f1209a, ApiResultSubScribeList.class, "subscribeList", false, true);
    public static final IVrsServer<ApiResultSubscribeState> subscribeState = C0214a.m581a(new C0332a(C0365a.bP), f1209a, ApiResultSubscribeState.class, "subscribeState", false, true);
    public static final IVrsServer<ApiResultCode> unsubscribe = C0214a.m581a(new C0332a(C0365a.bO), f1209a, ApiResultCode.class, "unsubscribe", false, true);
    public static final IVrsServer<ApiResultHistoryList> updateHistoryReminder = C0214a.m585a(new C0328a(C0365a.f1239I), f1208a, ApiResultHistoryList.class, "updateHistoryReminder", false);
    public static final IVrsServer<ApiResultHistoryList> updateHistoryReminderForAnonymity = C0214a.m585a(new C0328a(C0365a.f1245O), f1209a, ApiResultHistoryList.class, "updateHistoryReminder_anonymity", false);
    public static final IVrsServer<ApiResultCode> uploadCollect = C0214a.m585a(new C0335d(C0365a.f1252V), f1208a, ApiResultCode.class, "subscribe", false);
    public static final IVrsServer<ApiResultCode> uploadCollectForAnonymity = C0214a.m585a(new C0335d(C0365a.as), f1209a, ApiResultCode.class, "subscribe_anonymity", false);
    public static final IVrsServer<ApiResultCode> uploadHistory = C0214a.m585a(new C0328a(C0365a.f1232B), f1208a, ApiResultCode.class, "uploadHistory", false);
    public static final IVrsServer<ApiResultCode> uploadHistoryForAnonymity = C0214a.m585a(new C0328a(C0365a.f1240J), f1209a, ApiResultCode.class, "uploadHistory_anonymity", false);

    public static final class C0332a implements IApiUrlBuilder {
        private String f1204a = null;

        public C0332a(String str) {
            this.f1204a = str;
        }

        public final String build(String... params) {
            String e = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform()).mo833e();
            String c;
            if (this.f1204a.contains("keepalive.action") && params != null && params.length == 3) {
                c = C0376f.m824c(params[0], params[1], params[2], e);
                return BaseHelper.m757b(this.f1204a, params[0], e, params[1], params[2], c);
            } else if (this.f1204a.contains("secure_check_vip.action") && params != null && params.length == 1) {
                c = C0376f.m828e(params[0], e);
                return BaseHelper.m757b(this.f1204a, params[0], e, c, TVApiBase.getTVApiProperty().getPassportDeviceId());
            } else if ((this.f1204a.contains("subscribe/add.htm") || this.f1204a.contains("subscribe/cancel.htm") || this.f1204a.contains("subscribe/countAndState.htm")) && params != null && params.length == 2) {
                return BaseHelper.m757b(this.f1204a, params[0], params[1], e);
            } else if (!this.f1204a.contains("subscribe/tv/list.htm") || params == null || params.length != 5) {
                return null;
            } else {
                return BaseHelper.m757b(this.f1204a, params[0], params[1], params[2], params[3], params[4], e);
            }
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class C0333b implements IApiUrlBuilder {
        private String f1205a = null;

        public C0333b(String str) {
            this.f1205a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 1) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1205a);
            }
            return BaseHelper.m757b(this.f1205a, params[0], C0214a.m580a(params[0]));
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class C0334c implements IApiUrlBuilder {
        private String f1206a = null;

        public C0334c(String str) {
            this.f1206a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 2) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1206a);
            }
            String[] strArr = new String[4];
            for (int i = 0; i < 2; i++) {
                strArr[i] = params[i];
            }
            strArr[2] = C0214a.m580a(params[0]);
            return BaseHelper.m757b(this.f1206a, strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class C0335d implements IApiUrlBuilder {
        private String f1207a = null;

        public C0335d(String str) {
            this.f1207a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 4) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1207a);
            }
            String[] strArr = new String[5];
            for (int i = 0; i < 4; i++) {
                strArr[i] = params[i];
            }
            strArr[4] = C0214a.m580a(params[2]);
            return BaseHelper.m757b(this.f1207a, strArr);
        }

        public final List<String> header() {
            return null;
        }
    }
}
