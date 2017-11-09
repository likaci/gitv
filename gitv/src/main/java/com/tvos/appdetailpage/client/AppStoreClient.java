package com.tvos.appdetailpage.client;

import com.tvos.appdetailpage.helper.Dictionary;
import com.tvos.appdetailpage.info.AdverIndexResponse;
import com.tvos.appdetailpage.info.AppCategoriesResponse;
import com.tvos.appdetailpage.info.AppDetailResponse;
import com.tvos.appdetailpage.info.AppHistoryResponse;
import com.tvos.appdetailpage.info.AppUpdateResponse;
import com.tvos.appdetailpage.info.AppsCollectionDetailResponse;
import com.tvos.appdetailpage.info.AppsCollectionResponse;
import com.tvos.appdetailpage.info.AppsInfoResponse;
import com.tvos.appdetailpage.info.RankHistoryResponse;
import com.tvos.appdetailpage.info.RankResponse;
import com.tvos.appdetailpage.info.RecommendResponse;
import com.tvos.appdetailpage.info.SearchResponse;
import com.tvos.appdetailpage.info.StatusResponse;
import com.tvos.appdetailpage.rest.AppBackendRest;
import com.tvos.appdetailpage.rest.CloudPushRest;
import com.tvos.appdetailpage.rest.PingbackRest;
import com.tvos.appdetailpage.rest.RecommendRest;
import com.tvos.appdetailpage.rest.SearchRest;
import com.tvos.appdetailpage.rest.TVUpdateRest;
import java.util.Random;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppStoreClient {
    static final String TAG = "AppStoreClient";
    private static String anonymousUserID;
    private static String deviceID;
    private static String imei;
    private static AppStoreClient instance = null;
    private static String qiyuUrl;
    private static String uuID = "";
    private AppBackendRest appRest;
    private String appServerUrl;
    private final String appVersion;
    private String channelID;
    private final String charSet;
    private CloudPushRest cloudPushedPingbackRest;
    private String cloudPushedUrl;
    private String cookie;
    private String entry;
    private String longyuan4Url;
    private String longyuanUrl;
    private final String macID;
    private Boolean osRooted;
    private final String osVersion;
    private PingbackRest pingbackRest;
    private final String platformIDV3;
    private final String platform_1;
    private final String platform_2;
    private final String product_1;
    private final String product_2;
    private String qisoUrl;
    private String ratingServerUrl;
    private RecommendRest recRest;
    private SearchRest searchRest;
    private TVUpdateRest tvUpdateRest;
    private String tvUpdateUrl;
    private String userGameUrl;
    private String userID;
    private final String userPlatform;

    public static class Builder {
        private String anonymousUserID;
        private String appServerUrl = null;
        private String appStoreEntry;
        private String appVersion;
        private String channel;
        private String charSet;
        private String cloudPushedUrl = null;
        private String cookie;
        private String deviceID;
        private String imei;
        private String longyuan4Url = null;
        private String longyuanUrl = null;
        private String macID;
        private Boolean osRooted;
        private String osVersion;
        private String platform_1;
        private String platform_2;
        private String product_1;
        private String product_2;
        private String qisoUrl = null;
        private String qiyuUrl;
        private String ratingServerUrl = null;
        private String tvUpdateUrl = null;
        private String userID;
        private String userPlatform;

        public Builder setDeviceID(String deviceID) {
            if (deviceID == null || deviceID.trim().length() == 0) {
                throw new NullPointerException("DeviceID may not be blank.");
            }
            this.deviceID = deviceID;
            return this;
        }

        public Builder setUserID(String anonymousUserID, String userID) {
            if ((userID == null || userID.trim().length() == 0) && (anonymousUserID == null || anonymousUserID.trim().length() == 0)) {
                throw new NullPointerException("UserID can not be all blank.");
            }
            this.userID = userID;
            this.anonymousUserID = anonymousUserID;
            return this;
        }

        public Builder setProduct(String product_1, String product_2) {
            if (product_1 == null || product_1.trim().length() == 0) {
                throw new NullPointerException("Product should not be blank.");
            }
            this.product_1 = product_1;
            this.product_2 = product_2;
            return this;
        }

        public Builder setPlatform(String platform_1, String platform_2) {
            if (platform_1 == null || platform_1.trim().length() == 0 || platform_2 == null || platform_2.trim().length() == 0) {
                throw new NullPointerException("Platform should not be blank.");
            }
            this.platform_1 = platform_1;
            this.platform_2 = platform_2;
            return this;
        }

        public Builder setUserPlatform(String userPlatform) {
            this.userPlatform = userPlatform;
            return this;
        }

        public Builder setMacID(String macID) {
            this.macID = macID;
            return this;
        }

        public Builder setAppVersion(String version) {
            if (version == null || version.trim().length() == 0) {
                throw new NullPointerException("AppVersion may not be blank.");
            }
            this.appVersion = version;
            return this;
        }

        public Builder setOsVersion(String version) {
            if (version == null || version.trim().length() == 0) {
                throw new NullPointerException("OsVersion may not be blank.");
            }
            this.osVersion = version;
            return this;
        }

        public Builder setChannelID(String channel) {
            if (channel == null || channel.trim().length() == 0) {
                throw new NullPointerException("ChannelID may not be blank.");
            }
            this.channel = channel;
            return this;
        }

        public Builder setOsRooted(Boolean osRooted) {
            if (osRooted == null) {
                throw new NullPointerException("OsRooted may not be blank.");
            }
            this.osRooted = osRooted;
            return this;
        }

        public Builder setCharSet(String charSet) {
            if (charSet == null) {
                throw new NullPointerException("charSet can not be blank.");
            }
            this.charSet = charSet;
            return this;
        }

        public Builder setImei(String imei) {
            if (imei == null) {
                throw new NullPointerException("IMEI can not be blank.");
            }
            this.imei = imei;
            return this;
        }

        public Builder setServers(String recommendServerUrl, String qisoUrl, String longyuanUrl, String longyuan4Url, String appServerUrl, String cloudPushedUrl, String tvUpdateUrl) {
            if (recommendServerUrl != null && recommendServerUrl.trim().length() > 0) {
                this.qiyuUrl = recommendServerUrl;
            }
            if (qisoUrl != null && qisoUrl.trim().length() > 0) {
                this.qisoUrl = qisoUrl;
            }
            if (longyuanUrl != null && longyuanUrl.trim().length() > 0) {
                this.longyuanUrl = longyuanUrl;
            }
            if (appServerUrl != null && appServerUrl.trim().length() > 0) {
                this.appServerUrl = appServerUrl;
            }
            if (longyuan4Url != null && longyuan4Url.trim().length() > 0) {
                this.longyuan4Url = longyuan4Url;
            }
            if (cloudPushedUrl != null && cloudPushedUrl.trim().length() > 0) {
                this.cloudPushedUrl = cloudPushedUrl;
            }
            if (tvUpdateUrl != null && tvUpdateUrl.trim().length() > 0) {
                this.tvUpdateUrl = tvUpdateUrl;
            }
            return this;
        }

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder setAppStoreEntry(String entry) {
            this.appStoreEntry = entry;
            return this;
        }

        public AppStoreClient build() {
            AppStoreClient instance;
            synchronized (AppStoreClient.class) {
                if (AppStoreClient.getInstance() != null) {
                    instance = AppStoreClient.getInstance();
                } else {
                    ensureSaneDefaults();
                    instance = new AppStoreClient(this.deviceID, this.anonymousUserID, this.userID, this.platform_1, this.platform_2, this.userPlatform, this.product_1, this.product_2, this.macID, this.appVersion, this.osVersion, this.channel, this.osRooted, this.charSet, this.imei, this.cookie, this.appStoreEntry, this.qiyuUrl, this.qisoUrl, this.appServerUrl, this.longyuanUrl, this.longyuan4Url, this.cloudPushedUrl, this.tvUpdateUrl);
                    AppStoreClient.instance = instance;
                }
            }
            return instance;
        }

        private void ensureSaneDefaults() {
        }
    }

    public static class SearchBuilder {
        private String app_type = null;
        private String available_status = null;
        private AppStoreClient client = null;
        private String game_style_id = null;
        private String game_type = null;
        private String is_app_sugget = "0";
        private boolean is_pingyin = false;
        private String key = null;
        private String mode;
        private String package_size_id = null;
        private int page_num = 1;
        private int page_size = 20;
        private String painting_style_id = null;
        private String price_id = null;
        private String running_platform_id = "9";
        private String top_category_id = null;
        private String total_rating_id = null;

        public SearchBuilder(AppStoreClient client) {
            this.client = client;
        }

        public SearchBuilder setKey(String key) {
            this.key = key;
            return this;
        }

        public SearchBuilder setMode(String mode) {
            this.mode = mode;
            return this;
        }

        public SearchBuilder setAppType(String type) {
            this.app_type = type;
            return this;
        }

        public SearchBuilder setGameType(String type) {
            this.game_type = type;
            return this;
        }

        public SearchBuilder setGameStyle(String game_style_id, String painting_style_id) {
            this.game_style_id = game_style_id;
            this.painting_style_id = painting_style_id;
            return this;
        }

        public SearchBuilder setPrice(String price_id) {
            this.price_id = price_id;
            return this;
        }

        public SearchBuilder setStatus(String available_status) {
            this.available_status = available_status;
            return this;
        }

        public SearchBuilder setPinyinSearch(boolean pinyinSearch) {
            this.is_pingyin = pinyinSearch;
            return this;
        }

        public SearchBuilder setPaginate(int page_num, int page_size) {
            this.page_num = page_num;
            this.page_size = page_size;
            return this;
        }

        public SearchBuilder setCategory(int top_category_id) {
            this.top_category_id = String.valueOf(top_category_id);
            return this;
        }

        public SearchBuilder setIsSuggest(String is_app_sugget) {
            this.is_app_sugget = is_app_sugget;
            return this;
        }

        public SearchResponse search(boolean hasDetail, Callback<SearchResponse> cb) {
            if (this.is_pingyin) {
                return this.client.searchAppsByPingyin(this.key, Boolean.valueOf(hasDetail), this.is_app_sugget, cb);
            }
            if (this.key == null) {
                return this.client.listApps(this.mode, this.app_type, this.game_type, this.price_id, this.total_rating_id, this.package_size_id, this.game_style_id, this.painting_style_id, this.available_status, this.top_category_id, Boolean.valueOf(hasDetail), this.page_size, this.page_num, cb);
            }
            return this.client.searchApps(this.key, this.mode, this.app_type, this.game_type, Boolean.valueOf(hasDetail), this.page_size, this.page_num, cb);
        }
    }

    private AppStoreClient(String deviceID, String anonymousUserID, String userID, String platform_1, String platform_2, String userPlatform, String product_1, String product_2, String macID, String appVersion, String osVersion, String channelID, Boolean osRooted, String charSet, String imei, String cookie, String entry, String qiyuUrl, String qisoUrl, String appServerUrl, String longyuanUrl, String longyuan4Url, String cloudPushedUrl, String tvUpdateUrl) {
        deviceID = deviceID;
        anonymousUserID = anonymousUserID;
        this.userID = userID;
        this.platform_1 = platform_1;
        this.platform_2 = platform_2;
        this.userPlatform = userPlatform;
        this.product_1 = product_1;
        this.product_2 = product_2;
        this.platformIDV3 = Dictionary.platformIDFromVer4(platform_1, platform_2, product_1, product_2);
        this.macID = macID;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
        this.channelID = channelID;
        this.osRooted = osRooted;
        this.charSet = charSet;
        qiyuUrl = qiyuUrl;
        this.qisoUrl = qisoUrl;
        this.appServerUrl = appServerUrl;
        this.longyuanUrl = longyuanUrl;
        this.longyuan4Url = longyuan4Url;
        this.cloudPushedUrl = cloudPushedUrl;
        this.tvUpdateUrl = tvUpdateUrl;
        this.ratingServerUrl = this.ratingServerUrl;
        this.cookie = cookie;
        imei = imei;
        this.entry = entry;
        this.recRest = (RecommendRest) new retrofit.RestAdapter.Builder().setEndpoint(qiyuUrl).setLogLevel(LogLevel.NONE).build().create(RecommendRest.class);
        this.appRest = (AppBackendRest) new retrofit.RestAdapter.Builder().setEndpoint(this.appServerUrl).setLogLevel(LogLevel.NONE).build().create(AppBackendRest.class);
        this.pingbackRest = (PingbackRest) new retrofit.RestAdapter.Builder().setEndpoint(this.longyuanUrl).setLogLevel(LogLevel.NONE).build().create(PingbackRest.class);
        this.cloudPushedPingbackRest = (CloudPushRest) new retrofit.RestAdapter.Builder().setEndpoint(this.cloudPushedUrl).setLogLevel(LogLevel.NONE).build().create(CloudPushRest.class);
        this.searchRest = (SearchRest) new retrofit.RestAdapter.Builder().setEndpoint(this.qisoUrl).setLogLevel(LogLevel.NONE).build().create(SearchRest.class);
        this.tvUpdateRest = (TVUpdateRest) new retrofit.RestAdapter.Builder().setEndpoint(this.tvUpdateUrl).setLogLevel(LogLevel.NONE).build().create(TVUpdateRest.class);
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setUserID(String user_id) {
        this.userID = user_id;
    }

    public void setAppStoreEntry(String entry) {
        this.entry = entry;
    }

    public void setOSRooted(boolean osRooted) {
        this.osRooted = Boolean.valueOf(osRooted);
    }

    public static AppStoreClient getInstance() {
        return instance;
    }

    public static void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public RecommendResponse getRecommendedApps(String area, String rating, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.recRest.appsForUser(area, null, rltnum, anonymousUserID, this.userID, rating);
        }
        this.recRest.appsForUserAsync(area, null, rltnum, anonymousUserID, this.userID, this.userID, cb);
        return null;
    }

    public RecommendResponse getRecommendedAppsAndDetail(String area, String rating, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.appRest.appsForUser(area, null, rltnum, anonymousUserID, this.userID, rating, this.platformIDV3);
        }
        this.appRest.appsForUserAsync(area, null, rltnum, anonymousUserID, this.userID, this.userID, this.platformIDV3, cb);
        return null;
    }

    public RecommendResponse getAssociatedApps(String area, String rating, String app_id, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.recRest.associatedApps(area, null, rltnum, anonymousUserID, this.userID, app_id, rating);
        }
        this.recRest.associatedAppsAsync(area, null, rltnum, anonymousUserID, this.userID, app_id, rating, cb);
        return null;
    }

    public RecommendResponse getAssociatedAppsAndDetail(String area, String rating, String app_id, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.appRest.associatedApps(area, null, rltnum, anonymousUserID, this.userID, app_id, rating, this.platformIDV3);
        }
        this.appRest.associatedAppsAsync(area, null, rltnum, anonymousUserID, this.userID, app_id, rating, this.platformIDV3, cb);
        return null;
    }

    public RecommendResponse getTopApps(String area, String rating, String category, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.recRest.topApps(area, null, rltnum, rating, category);
        }
        this.recRest.topAppsAsync(area, null, rltnum, rating, category, cb);
        return null;
    }

    public RecommendResponse getTopAppsAndDetail(String area, String rating, String category, int rltnum, Callback<RecommendResponse> cb) {
        if (cb == null) {
            return this.appRest.topApps(area, null, rltnum, rating, category, this.platformIDV3);
        }
        this.appRest.topAppsAsync(area, null, rltnum, rating, category, this.platformIDV3, cb);
        return null;
    }

    public AppsInfoResponse getNecessaryApps(int pos, String agent_type, Callback<AppsInfoResponse> cb) {
        if (cb == null) {
            return this.appRest.getNecessaryApps(agent_type, pos);
        }
        this.appRest.getNecessaryAppsAsync(agent_type, pos, cb);
        return null;
    }

    public AppsCollectionResponse getAppsCollection(String collection_type, String agent_type, Callback<AppsCollectionResponse> cb) {
        if (cb == null) {
            return this.appRest.getCollectionApps(agent_type, collection_type);
        }
        this.appRest.getCollectionAppsAsync(agent_type, collection_type, cb);
        return null;
    }

    public AppsCollectionDetailResponse getAppsCollectionDetail(String collection_id, String agent_type, Callback<AppsCollectionDetailResponse> cb) {
        if (cb == null) {
            return this.appRest.getCollectionAppsDetail(agent_type, collection_id, uuID);
        }
        this.appRest.getCollectionAppsDetailAsync(agent_type, collection_id, uuID, cb);
        return null;
    }

    public AppCategoriesResponse getAppCategiries(int parent_id, Callback<AppCategoriesResponse> cb) {
        if (cb == null) {
            return this.appRest.appCategories(parent_id, this.platformIDV3);
        }
        this.appRest.appCategoriesAsync(parent_id, this.platformIDV3, cb);
        return null;
    }

    public AppCategoriesResponse getAppRecomCategiries(Callback<AppCategoriesResponse> cb) {
        if (cb == null) {
            return this.appRest.appRecomCategories(this.platformIDV3, 1);
        }
        this.appRest.appRecomCategoriesAsync(this.platformIDV3, 1, cb);
        return null;
    }

    public AppDetailResponse getAppDetail(String app_id, String app_package_name, String app_ver, String fields, Callback<AppDetailResponse> cb) {
        if (cb == null) {
            return this.appRest.appDetail(app_id, app_package_name, app_ver, fields, this.platformIDV3, uuID);
        }
        this.appRest.appDetailAsync(app_id, app_package_name, app_ver, fields, this.platformIDV3, uuID, cb);
        return null;
    }

    public AppsInfoResponse getMyApps(Callback<AppsInfoResponse> cb) {
        if (cb == null) {
            return this.appRest.myApps(this.userID, deviceID, Constants.APPBACKEND_MY_APPS_ALL, null, null, this.platformIDV3);
        }
        this.appRest.myAppsAsync(this.userID, deviceID, Constants.APPBACKEND_MY_APPS_ALL, null, null, this.platformIDV3, cb);
        return null;
    }

    public AppsInfoResponse getMyUpdateableApps(Callback<AppsInfoResponse> cb) {
        if (cb == null) {
            return this.appRest.myApps(this.userID, deviceID, "1", null, null, this.platformIDV3);
        }
        this.appRest.myAppsAsync(this.userID, deviceID, "1", null, null, this.platformIDV3, cb);
        return null;
    }

    public AppsInfoResponse getMyCloudApps(Callback<AppsInfoResponse> cb) {
        if (cb == null) {
            return this.appRest.myApps(this.userID, deviceID, "3", null, null, this.platformIDV3);
        }
        this.appRest.myAppsAsync(this.userID, deviceID, "3", null, null, this.platformIDV3, cb);
        return null;
    }

    public StatusResponse updateAppStatusBatch(String app_ids, String app_vers, String app_op, Callback<StatusResponse> cb) {
        if (cb == null) {
            return this.appRest.updateAppsStatus(this.userID, deviceID, app_ids, app_vers, app_op, this.platformIDV3);
        }
        this.appRest.updateAppsStatusAsync(this.userID, deviceID, app_ids, app_vers, app_op, this.platformIDV3, cb);
        return null;
    }

    public AppsInfoResponse getAppsUpdateInfo(String app_ids, Callback<AppsInfoResponse> cb) {
        if (cb == null) {
            return this.appRest.latestApps(app_ids, this.platformIDV3);
        }
        this.appRest.latestAppsAsync(app_ids, this.platformIDV3, cb);
        return null;
    }

    public AppHistoryResponse getAppHistory(String app_id, Callback<AppHistoryResponse> cb) {
        if (cb == null) {
            return this.appRest.appHistory(app_id, this.platformIDV3);
        }
        this.appRest.appHistoryAsync(app_id, this.platformIDV3, cb);
        return null;
    }

    public StatusResponse setAppRank(String app_id, String app_ver, int level, Callback<StatusResponse> cb) {
        if (cb == null) {
            return this.appRest.rank(this.cookie, app_id, app_ver, level, this.platformIDV3);
        }
        this.appRest.rankAsync(this.cookie, app_id, app_ver, level, this.platformIDV3, cb);
        return null;
    }

    public RankResponse getAppRank(String app_id, String app_ver, Callback<RankResponse> cb) {
        if (cb == null) {
            return this.appRest.rankStats(app_id, app_ver, this.platformIDV3);
        }
        this.appRest.rankStatsAsync(app_id, app_ver, this.platformIDV3, cb);
        return null;
    }

    public RankHistoryResponse getAppRankHistory(String app_id, String app_ver, Callback<RankHistoryResponse> cb) {
        if (cb == null) {
            return this.appRest.rankHistory(app_id, app_ver, this.platformIDV3);
        }
        this.appRest.rankHistoryAsync(app_id, app_ver, this.platformIDV3, cb);
        return null;
    }

    public AdverIndexResponse getAdverIndex(Callback<AdverIndexResponse> cb) {
        if (cb == null) {
            return this.appRest.adverIndex(this.platformIDV3, uuID);
        }
        this.appRest.adverIndexAsync(this.platformIDV3, uuID, cb);
        return null;
    }

    public Response onVisitRecommendedResult(RecommendResponse resp, String area, String app_ids, ResponseCallback cb) {
        if (cb == null) {
            return this.pingbackRest.recPingback(Constants.RECOMMEND_PINGBACK_TYPE_SHOW, "1", this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, app_ids, null, null, null);
        }
        this.pingbackRest.recPingbackAsync(Constants.RECOMMEND_PINGBACK_TYPE_SHOW, "1", this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, app_ids, null, null, null, cb);
        return null;
    }

    public Response onClickRecommendedResult(RecommendResponse resp, String area, String rank, String taid, ResponseCallback cb) {
        if (cb == null) {
            return this.pingbackRest.recPingback(Constants.RECOMMEND_PINGBACK_TYPE_CLICK, Constants.RECOMMEND_PINGBACK_USERACT_CLICK, this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, null, rank, taid, null);
        }
        this.pingbackRest.recPingbackAsync(Constants.RECOMMEND_PINGBACK_TYPE_CLICK, Constants.RECOMMEND_PINGBACK_USERACT_CLICK, this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, null, rank, taid, null, cb);
        return null;
    }

    public Response onDownloadRecommendedApp(RecommendResponse resp, String area, String rank, String taid, String aid, ResponseCallback cb) {
        if (cb == null) {
            return this.pingbackRest.recPingback(Constants.RECOMMEND_PINGBACK_TYPE_CLICK, "download", this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, null, rank, taid, aid);
        }
        this.pingbackRest.recPingbackAsync(Constants.RECOMMEND_PINGBACK_TYPE_CLICK, "download", this.userID, anonymousUserID, resp.attributes.event_id, resp.attributes.bucket, area, this.platformIDV3, null, rank, taid, aid, cb);
        return null;
    }

    public Response onLaunchApp(String app_id, String app_ver, String rpage, String block, String rseat, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "boot", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "boot", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null, cb);
        return null;
    }

    public Response onAppStoreLaunched(ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "boot", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, "boot", "boot", "0", null, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "boot", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, "boot", "boot", "0", null, this.entry, null, cb);
        return null;
    }

    public Response onAppStoreExited(String rpage, String block, String rseat, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "exit", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "exit", null, null, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null, cb);
        return null;
    }

    public Response onReceivePushedApp(String qpid, String yunsourceid, String sourcetype, String datatype, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.cloudPushedPingbackRest.receivePushedAppPingback("20", this.platform_1, this.platform_2, this.product_1, this.product_2, Constants.BSTP_TV_SUCC_RCV, Constants.BLOCK_TV_SUCC_RCV, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), yunsourceid, qpid, sourcetype, datatype);
        }
        this.cloudPushedPingbackRest.receivePushedAppPingbackAsync("20", this.platform_1, this.platform_2, this.product_1, this.product_2, Constants.BSTP_TV_SUCC_RCV, Constants.BLOCK_TV_SUCC_RCV, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), yunsourceid, qpid, sourcetype, datatype, cb);
        return null;
    }

    public Response onDownloadPushedApp(String app_id, String app_ver, String keywords, String yunsourceid, String sourcetype, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.pushedAppStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_DOWNLOAD, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype);
        }
        this.pingbackRest.pushedAppStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_DOWNLOAD, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype, cb);
        return null;
    }

    public Response onPushedAppDownloaded(String app_id, String app_ver, String keywords, String yunsourceid, String sourcetype, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.pushedAppStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype);
        }
        this.pingbackRest.pushedAppStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype, cb);
        return null;
    }

    public Response onInstallPushedApp(String app_id, String app_ver, String keywords, String yunsourceid, String sourcetype, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.pushedAppStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "install", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype);
        }
        this.pingbackRest.pushedAppStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "install", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype, cb);
        return null;
    }

    public StatusResponse onPushedAppInstalled(String app_id, String app_ver, String keywords, String yunsourceid, String sourcetype, Callback<StatusResponse> cb) {
        Random rand = new Random();
        if (cb == null) {
            this.pingbackRest.pushedAppStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype);
            return this.appRest.updateAppsStatus(this.userID, deviceID, app_id, app_ver, "1", this.platformIDV3);
        }
        this.pingbackRest.pushedAppStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, null, null, null, keywords, this.entry, null, yunsourceid, sourcetype, new ResponseCallback() {
            public void success(Response response) {
            }

            public void failure(RetrofitError error) {
            }
        });
        this.appRest.updateAppsStatusAsync(this.userID, deviceID, app_id, app_ver, "1", this.platformIDV3, cb);
        return null;
    }

    public Response onDownloadApp(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_DOWNLOAD, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_DOWNLOAD, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onAppDownloaded(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onAppDownloadCanceled(String app_id, String app_ver, String rpage, String block, String rseat, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_CANCELED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_CANCELED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, null, this.entry, null, cb);
        return null;
    }

    public Response onAppDownloadFailed(String app_id, String app_ver, String rpage, String block, String rseat, String errorcode, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_DOWNLOAD_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode, cb);
        return null;
    }

    public Response onRemoveApp(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_REMOVE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_REMOVE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onShortLogin(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_SHORT_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_SHORT_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onQRLogin(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_QR_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_QR_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onCommonLogin(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_COMMON_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_COMMON_LOGIN, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onLogout(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "logout", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "logout", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onSignup(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_SIGNUP, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_SIGNUP, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onInstallApp(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "install", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "install", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public StatusResponse onAppInstalled(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, Callback<StatusResponse> cb) {
        Random rand = new Random();
        if (cb == null) {
            Response r = this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
            return this.appRest.updateAppsStatus(this.userID, deviceID, app_id, app_ver, "1", this.platformIDV3);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, new ResponseCallback() {
            public void success(Response response) {
            }

            public void failure(RetrofitError error) {
            }
        });
        this.appRest.updateAppsStatusAsync(this.userID, deviceID, app_id, app_ver, "1", this.platformIDV3, cb);
        return null;
    }

    public Response onAppInstallFailed(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, String errorcode, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_INSTALL_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode, cb);
        return null;
    }

    public Response onUpdateApp(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_UPDATE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_START_UPDATE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public StatusResponse onAppUpdated(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, Callback<StatusResponse> cb) {
        Random rand = new Random();
        if (cb == null) {
            this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UPDATE_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
            return this.appRest.updateAppsStatus(this.userID, deviceID, app_id, app_ver, "2", this.platformIDV3);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UPDATE_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, new ResponseCallback() {
            public void success(Response response) {
            }

            public void failure(RetrofitError error) {
            }
        });
        this.appRest.updateAppsStatusAsync(this.userID, deviceID, app_id, app_ver, "2", this.platformIDV3, cb);
        return null;
    }

    public Response onAppUpdateFailed(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, String errorcode, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UPDATE_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UPDATE_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode, cb);
        return null;
    }

    public Response onUninstallApp(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "uninstall", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "uninstall", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onAppUninstallFailed(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, String errorcode, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UNINSTALL_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UNINSTALL_FAILED, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, errorcode, cb);
        return null;
    }

    public StatusResponse onAppUninstalled(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, Callback<StatusResponse> cb) {
        Random rand = new Random();
        if (cb == null) {
            this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UNINSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
            return this.appRest.updateAppsStatus(this.userID, deviceID, app_id, app_ver, "3", this.platformIDV3);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), Constants.PINGBACK_ACTION_UNINSTALL_DONE, app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, new ResponseCallback() {
            public void success(Response response) {
            }

            public void failure(RetrofitError err) {
            }
        });
        this.appRest.updateAppsStatusAsync(this.userID, deviceID, app_id, app_ver, "3", this.platformIDV3, cb);
        return null;
    }

    public Response onVisit(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "visit", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "visit", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public Response onClick(String app_id, String app_ver, String rpage, String block, String rseat, String keywords, ResponseCallback cb) {
        Random rand = new Random();
        if (cb == null) {
            return this.pingbackRest.appStatsPingback(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "click", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null);
        }
        this.pingbackRest.appStatsPingbackAsync(this.platform_1, this.platform_2, this.product_1, this.product_2, anonymousUserID, this.userID, String.valueOf(rand.nextInt()), "click", app_id, app_ver, deviceID, this.macID, this.osVersion, this.appVersion, this.channelID, Constants.TV_CHANNEL_ID_JDTV, rpage, block, rseat, keywords, this.entry, null, cb);
        return null;
    }

    public SearchResponse searchAppsByPingyin(String key, Boolean hasDetail, String is_app_sugget, Callback<SearchResponse> cb) {
        if (cb != null) {
            if (hasDetail.booleanValue()) {
                this.appRest.searchAppsByPingyinAsync(key, this.platformIDV3, is_app_sugget, uuID, cb);
            } else {
                this.searchRest.searchAppsByPingyinAsync(key, cb);
            }
            return null;
        } else if (hasDetail.booleanValue()) {
            return this.appRest.searchAppsByPingyin(key, this.platformIDV3, is_app_sugget, uuID);
        } else {
            return this.searchRest.searchAppsByPingyin(key);
        }
    }

    public SearchResponse searchApps(String key, String mode, String app_type, String game_type, Boolean hasDetail, int page_size, int page_num, Callback<SearchResponse> cb) {
        if (cb != null) {
            if (hasDetail.booleanValue()) {
                this.appRest.searchAppsAsync(key, mode, app_type, game_type, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), page_size, page_num, this.platformIDV3, uuID, cb);
            } else {
                this.searchRest.searchAppsAsync(key, mode, app_type, game_type, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), page_size, page_num, cb);
            }
            return null;
        } else if (hasDetail.booleanValue()) {
            return this.appRest.searchApps(key, mode, app_type, game_type, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), page_size, page_num, this.platformIDV3, uuID);
        } else {
            return this.searchRest.searchApps(key, mode, app_type, game_type, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), page_size, page_num);
        }
    }

    public SearchResponse listApps(String mode, String app_type, String game_type, String price_id, String total_rating_id, String package_size_id, String game_style_id, String painting_style_id, String available_status, String top_category_id, Boolean hasDetail, int page_size, int page_num, Callback<SearchResponse> cb) {
        if (cb != null) {
            if (hasDetail.booleanValue()) {
                this.appRest.listAppsAsync(mode, app_type, game_type, price_id, total_rating_id, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), package_size_id, game_style_id, painting_style_id, available_status, top_category_id, page_size, page_num, this.platformIDV3, uuID, cb);
            } else {
                this.searchRest.listAppsAsync(mode, app_type, game_type, price_id, total_rating_id, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), package_size_id, game_style_id, painting_style_id, available_status, top_category_id, page_size, page_num, cb);
            }
            return null;
        } else if (hasDetail.booleanValue()) {
            return this.appRest.listApps(mode, app_type, game_type, price_id, total_rating_id, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), package_size_id, game_style_id, painting_style_id, available_status, top_category_id, page_size, page_num, this.platformIDV3, uuID);
        } else {
            return this.searchRest.listApps(mode, app_type, game_type, price_id, total_rating_id, Dictionary.searchPlatformIDFromVer3(this.platformIDV3), package_size_id, game_style_id, painting_style_id, available_status, top_category_id, page_size, page_num);
        }
    }

    public AppUpdateResponse getUpdateInfo(String agent_type, String app_id, int ver, Callback<AppUpdateResponse> cb) {
        if (cb == null) {
            return this.tvUpdateRest.getAppUpdateInfo(agent_type, app_id, ver, uuID);
        }
        this.tvUpdateRest.getAppUpdateInfoAsync(agent_type, app_id, ver, uuID, cb);
        return null;
    }

    public static void setUUID(String uuid) {
        uuID = uuid;
    }

    public static void setDeviceID(String deviceid) {
        deviceID = deviceid;
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static String getUUID() {
        return uuID;
    }

    public static void setIMEI(String IMEI) {
        imei = IMEI;
    }

    public static void setAnonymousUserID(String anoUserId) {
        anonymousUserID = anoUserId;
    }
}
