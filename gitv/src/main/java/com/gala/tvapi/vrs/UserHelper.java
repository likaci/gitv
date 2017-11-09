package com.gala.tvapi.vrs;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.a.h;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.core.f;
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
    private static h a = new h();
    private static k f71a = new k();
    public static final IVrsServer<ApiResultCode> cancelCollect = com.gala.tvapi.b.a.a(new d(com.gala.tvapi.vrs.core.a.W), a, ApiResultCode.class, "unsubscribe", false);
    public static final IVrsServer<ApiResultCode> cancelCollectForAnonymity = com.gala.tvapi.b.a.a(new d(com.gala.tvapi.vrs.core.a.at), f71a, ApiResultCode.class, "unsubscribe_anonymity", false);
    public static final IVrsServer<ApiResultCode> checkCollect = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.X), a, ApiResultCode.class, "isSubscribed", false);
    public static final IVrsServer<ApiResultCode> checkCollectForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.au), f71a, ApiResultCode.class, "isSubscribed_anonymity", false);
    public static final IVrsServer<ApiResultKeepaliveInterval> checkVipAccount = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aQ), new com.gala.tvapi.vrs.a.a(), ApiResultKeepaliveInterval.class, "checkVipAccount", false, false);
    public static final IVrsServer<ApiResultCode> clearCollect = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.U), a, ApiResultCode.class, "deleteAllSubscriptions", false);
    public static final IVrsServer<ApiResultCode> clearCollectForAnonymity = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.ax), f71a, ApiResultCode.class, "deleteAllSubscriptions_anonymity", false);
    public static final IVrsServer<ApiResultCode> clearHistory = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.D), a, ApiResultCode.class, "deleteAllHistory", false);
    public static final IVrsServer<ApiResultCode> clearHistoryForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.L), f71a, ApiResultCode.class, "deleteAllHistory_anonymity", false);
    public static final IVrsServer<ApiResultCollectList> collectList = com.gala.tvapi.b.a.a(com.gala.tvapi.b.a.a(com.gala.tvapi.vrs.core.a.T), a, ApiResultCollectList.class, "collectList", true);
    public static final IVrsServer<ApiResultCollectList> collectListForAnonymity = com.gala.tvapi.b.a.a(com.gala.tvapi.b.a.a(com.gala.tvapi.vrs.core.a.aw), f71a, ApiResultCollectList.class, "collectList_anonymity", true);
    public static final IVrsServer<ApiResultCode> deleteHistoryAlbum = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.H), a, ApiResultCode.class, "deleteHistory", false);
    public static final IVrsServer<ApiResultCode> deleteHistoryAlbumForForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.S), f71a, ApiResultCode.class, "deleteHistory_anonymity", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyAlbumInfo = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.G), a, ApiResultHistoryTvInfo.class, "historyInfo", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyAlbumInfoForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.N), f71a, ApiResultHistoryTvInfo.class, "historyInfo_anonymity", false);
    public static final IVrsServer<ApiResultHistoryAlbumInfos> historyAlbumInfos = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.Q), a, ApiResultHistoryAlbumInfos.class, "historyInfos", false);
    public static final IVrsServer<ApiResultHistoryAlbumInfos> historyAlbumInfosForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.R), f71a, ApiResultHistoryAlbumInfos.class, "historyInfos_anonymity", false);
    public static final IVrsServer<ApiResultHistoryList> historyList = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.C), a, ApiResultHistoryList.class, "historyList", true);
    public static final IVrsServer<ApiResultHistoryList> historyListForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.K), f71a, ApiResultHistoryList.class, "historyList_anonymity", true);
    public static final IVrsServer<ApiResultHistoryListForUser> historyListForUser = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.E), a, ApiResultHistoryListForUser.class, "historyList", true);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyTvInfo = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.F), a, ApiResultHistoryTvInfo.class, "historyTVInfo", false);
    public static final IVrsServer<ApiResultHistoryTvInfo> historyTvInfoForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.M), f71a, ApiResultHistoryTvInfo.class, "historyTVInfo_anonymity", false);
    public static final IVrsServer<ApiResultKeepaliveInterval> keepAlive = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aP), f71a, ApiResultKeepaliveInterval.class, "keepAlive", false, true);
    public static final IVrsServer<ApiResultKeepaliveInterval> keepAliveInterval = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.aO), f71a, ApiResultKeepaliveInterval.class, "keepAliveInterval", false, true);
    public static final IVrsServer<ApiResultCode> mergeCollects = com.gala.tvapi.b.a.a(new c(com.gala.tvapi.vrs.core.a.av), a, ApiResultCode.class, "mergeCollect", false);
    public static final IVrsServer<ApiResultCode> mergeHistory = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.P), a, ApiResultCode.class, "mergeHistory", false);
    public static final IVrsServer<ApiResultCode> subscribe = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bN), f71a, ApiResultCode.class, "subscribe", false, true);
    public static final IVrsServer<ApiResultSubScribeList> subscribeList = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bQ), f71a, ApiResultSubScribeList.class, "subscribeList", false, true);
    public static final IVrsServer<ApiResultSubscribeState> subscribeState = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bP), f71a, ApiResultSubscribeState.class, "subscribeState", false, true);
    public static final IVrsServer<ApiResultCode> unsubscribe = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bO), f71a, ApiResultCode.class, "unsubscribe", false, true);
    public static final IVrsServer<ApiResultHistoryList> updateHistoryReminder = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.I), a, ApiResultHistoryList.class, "updateHistoryReminder", false);
    public static final IVrsServer<ApiResultHistoryList> updateHistoryReminderForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.O), f71a, ApiResultHistoryList.class, "updateHistoryReminder_anonymity", false);
    public static final IVrsServer<ApiResultCode> uploadCollect = com.gala.tvapi.b.a.a(new d(com.gala.tvapi.vrs.core.a.V), a, ApiResultCode.class, "subscribe", false);
    public static final IVrsServer<ApiResultCode> uploadCollectForAnonymity = com.gala.tvapi.b.a.a(new d(com.gala.tvapi.vrs.core.a.as), f71a, ApiResultCode.class, "subscribe_anonymity", false);
    public static final IVrsServer<ApiResultCode> uploadHistory = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.B), a, ApiResultCode.class, "uploadHistory", false);
    public static final IVrsServer<ApiResultCode> uploadHistoryForAnonymity = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.J), f71a, ApiResultCode.class, "uploadHistory_anonymity", false);

    public static final class a implements IApiUrlBuilder {
        private String a = null;

        public a(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            String e = com.gala.tvapi.b.c.a(TVApiBase.getTVApiProperty().getPlatform()).e();
            String c;
            if (this.a.contains("keepalive.action") && params != null && params.length == 3) {
                c = f.c(params[0], params[1], params[2], e);
                return BaseHelper.b(this.a, params[0], e, params[1], params[2], c);
            } else if (this.a.contains("secure_check_vip.action") && params != null && params.length == 1) {
                c = f.e(params[0], e);
                return BaseHelper.b(this.a, params[0], e, c, TVApiBase.getTVApiProperty().getPassportDeviceId());
            } else if ((this.a.contains("subscribe/add.htm") || this.a.contains("subscribe/cancel.htm") || this.a.contains("subscribe/countAndState.htm")) && params != null && params.length == 2) {
                return BaseHelper.b(this.a, params[0], params[1], e);
            } else if (!this.a.contains("subscribe/tv/list.htm") || params == null || params.length != 5) {
                return null;
            } else {
                return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], params[4], e);
            }
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class b implements IApiUrlBuilder {
        private String a = null;

        public b(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 1) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            return BaseHelper.b(this.a, params[0], com.gala.tvapi.b.a.a(params[0]));
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class c implements IApiUrlBuilder {
        private String a = null;

        public c(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 2) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            String[] strArr = new String[4];
            for (int i = 0; i < 2; i++) {
                strArr[i] = params[i];
            }
            strArr[2] = com.gala.tvapi.b.a.a(params[0]);
            return BaseHelper.b(this.a, strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final class d implements IApiUrlBuilder {
        private String a = null;

        public d(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 4) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            String[] strArr = new String[5];
            for (int i = 0; i < 4; i++) {
                strArr[i] = params[i];
            }
            strArr[4] = com.gala.tvapi.b.a.a(params[2]);
            return BaseHelper.b(this.a, strArr);
        }

        public final List<String> header() {
            return null;
        }
    }
}
