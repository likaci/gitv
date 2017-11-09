package com.tvos.appdetailpage.rest;

import com.tvos.appdetailpage.info.AdverIndexResponse;
import com.tvos.appdetailpage.info.AppCategoriesResponse;
import com.tvos.appdetailpage.info.AppDetailResponse;
import com.tvos.appdetailpage.info.AppHistoryResponse;
import com.tvos.appdetailpage.info.AppsCollectionDetailResponse;
import com.tvos.appdetailpage.info.AppsCollectionResponse;
import com.tvos.appdetailpage.info.AppsInfoResponse;
import com.tvos.appdetailpage.info.RankHistoryResponse;
import com.tvos.appdetailpage.info.RankResponse;
import com.tvos.appdetailpage.info.RecommendResponse;
import com.tvos.appdetailpage.info.SearchResponse;
import com.tvos.appdetailpage.info.StatusResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface AppBackendRest {
    @GET("/apis/app/adver/tv/index.action")
    AdverIndexResponse adverIndex(@Query("agent_type") String str, @Query("uuid") String str2);

    @GET("/apis/app/adver/tv/index.action")
    void adverIndexAsync(@Query("agent_type") String str, @Query("uuid") String str2, Callback<AdverIndexResponse> callback);

    @GET("/apis/app/categories.action")
    AppCategoriesResponse appCategories(@Query("parent_id") int i, @Query("agent_type") String str);

    @GET("/apis/app/categories.action")
    void appCategoriesAsync(@Query("parent_id") int i, @Query("agent_type") String str, Callback<AppCategoriesResponse> callback);

    @GET("/apis/app/info.action")
    AppDetailResponse appDetail(@Query("app_id") String str, @Query("app_package_name") String str2, @Query("app_ver") String str3, @Query("fields") String str4, @Query("agent_type") String str5, @Query("uuid") String str6);

    @GET("/apis/app/info.action")
    void appDetailAsync(@Query("app_id") String str, @Query("app_package_name") String str2, @Query("app_ver") String str3, @Query("fields") String str4, @Query("agent_type") String str5, @Query("uuid") String str6, Callback<AppDetailResponse> callback);

    @GET("/apis/app/history.action")
    AppHistoryResponse appHistory(@Query("app_id") String str, @Query("agent_type") String str2);

    @GET("/apis/app/history.action")
    void appHistoryAsync(@Query("app_id") String str, @Query("agent_type") String str2, Callback<AppHistoryResponse> callback);

    @GET("/apis/app/get_categories.action")
    AppCategoriesResponse appRecomCategories(@Query("agent_type") String str, @Query("is_hot") int i);

    @GET("/apis/app/get_categories.action")
    void appRecomCategoriesAsync(@Query("agent_type") String str, @Query("is_hot") int i, Callback<AppCategoriesResponse> callback);

    @GET("/apis/app/recommand/byuser.action?rltfmt=json")
    RecommendResponse appsForUser(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("rating") String str5, @Query("agent_type") String str6);

    @GET("/apis/app/recommand/byuser.action?rltfmt=json")
    void appsForUserAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("rating") String str5, @Query("agent_type") String str6, Callback<RecommendResponse> callback);

    @GET("/apis/app/recommand/byapp.action?rltfmt=json")
    RecommendResponse associatedApps(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("qipu_id") String str5, @Query("rating") String str6, @Query("agent_type") String str7);

    @GET("/apis/app/recommand/byapp.action?rltfmt=json")
    void associatedAppsAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("qipu_id") String str5, @Query("rating") String str6, @Query("agent_type") String str7, Callback<RecommendResponse> callback);

    @GET("/apis/app/get_collection.action")
    AppsCollectionResponse getCollectionApps(@Query("agent_type") String str, @Query("collection_type") String str2);

    @GET("/apis/app/get_collection.action")
    void getCollectionAppsAsync(@Query("agent_type") String str, @Query("collection_type") String str2, Callback<AppsCollectionResponse> callback);

    @GET("/apis/app/collection.action")
    AppsCollectionDetailResponse getCollectionAppsDetail(@Query("agent_type") String str, @Query("collection_id") String str2, @Query("uuid") String str3);

    @GET("/apis/app/collection.action")
    void getCollectionAppsDetailAsync(@Query("agent_type") String str, @Query("collection_id") String str2, @Query("uuid") String str3, Callback<AppsCollectionDetailResponse> callback);

    @GET("/apis/app/recommand/manual.action?cid=216")
    AppsInfoResponse getNecessaryApps(@Query("agent_type") String str, @Query("pos") int i);

    @GET("/apis/app/recommand/manual.action?cid=216")
    void getNecessaryAppsAsync(@Query("agent_type") String str, @Query("pos") int i, Callback<AppsInfoResponse> callback);

    @GET("/apis/app/latest.action")
    AppsInfoResponse latestApps(@Query("app_ids") String str, @Query("agent_type") String str2);

    @GET("/apis/app/latest.action")
    void latestAppsAsync(@Query("app_ids") String str, @Query("agent_type") String str2, Callback<AppsInfoResponse> callback);

    @GET("/apis/app/latest.action")
    AppsInfoResponse latestVersion(@Query("app_ids") String str);

    @GET("/apis/app/latest.action")
    void latestVersionAsync(@Query("app_ids") String str, Callback<AppsInfoResponse> callback);

    @GET("/apis/app/search/list.action?if=app&type=list")
    SearchResponse listApps(@Query("mode") String str, @Query("app_type") String str2, @Query("game_type") String str3, @Query("price") String str4, @Query("total_rating") String str5, @Query("running_platform") String str6, @Query("package_size") String str7, @Query("game_style") String str8, @Query("painting_style") String str9, @Query("available_status") String str10, @Query("top_category_id") String str11, @Query("pageSize") int i, @Query("pageNum") int i2, @Query("agent_type") String str12, @Query("uuid") String str13);

    @GET("/apis/app/search/list.action?if=app&type=list")
    void listAppsAsync(@Query("mode") String str, @Query("app_type") String str2, @Query("game_type") String str3, @Query("price") String str4, @Query("total_rating") String str5, @Query("running_platform") String str6, @Query("package_size") String str7, @Query("game_style") String str8, @Query("painting_style") String str9, @Query("available_status") String str10, @Query("top_category_id") String str11, @Query("pageSize") int i, @Query("pageNum") int i2, @Query("agent_type") String str12, @Query("uuid") String str13, Callback<SearchResponse> callback);

    @GET("/apis/app/myapps.action")
    AppsInfoResponse myApps(@Query("authcookie") String str, @Query("device_id") String str2, @Query("types") String str3, @Query("start") String str4, @Query("offset") String str5, @Query("agent_type") String str6);

    @GET("/apis/app/myapps.action")
    void myAppsAsync(@Query("authcookie") String str, @Query("device_id") String str2, @Query("types") String str3, @Query("start") String str4, @Query("offset") String str5, @Query("agent_type") String str6, Callback<AppsInfoResponse> callback);

    @GET("/apis/app/setrank.action")
    StatusResponse rank(@Query("authcookie") String str, @Query("app_id") String str2, @Query("app_ver") String str3, @Query("rank") int i, @Query("agent_type") String str4);

    @GET("/apis/app/setrank.action")
    void rankAsync(@Query("authcookie") String str, @Query("app_id") String str2, @Query("app_ver") String str3, @Query("rank") int i, @Query("agent_type") String str4, Callback<StatusResponse> callback);

    @GET("/apis/app/rank_history.action")
    RankHistoryResponse rankHistory(@Query("app_id") String str, @Query("app_ver") String str2, @Query("agent_type") String str3);

    @GET("/apis/app/rank_history.action")
    void rankHistoryAsync(@Query("app_id") String str, @Query("app_ver") String str2, @Query("agent_type") String str3, Callback<RankHistoryResponse> callback);

    @GET("/apis/app/getrank.action")
    RankResponse rankStats(@Query("app_id") String str, @Query("app_ver") String str2, @Query("agent_type") String str3);

    @GET("/apis/app/getrank.action")
    void rankStatsAsync(@Query("app_id") String str, @Query("app_ver") String str2, @Query("agent_type") String str3, Callback<RankResponse> callback);

    @GET("/apis/app/search/normal.action?if=app")
    SearchResponse searchApps(@Query("key") String str, @Query("mode") String str2, @Query("app_type") String str3, @Query("game_type") String str4, @Query("running_platform") String str5, @Query("pageSize") int i, @Query("pageNum") int i2, @Query("agent_type") String str6, @Query("uuid") String str7);

    @GET("/apis/app/search/normal.action?if=app")
    void searchAppsAsync(@Query("key") String str, @Query("mode") String str2, @Query("app_type") String str3, @Query("game_type") String str4, @Query("running_platform") String str5, @Query("pageSize") int i, @Query("pageNum") int i2, @Query("agent_type") String str6, @Query("uuid") String str7, Callback<SearchResponse> callback);

    @GET("/apis/app/search/pinyin.action?if=app_py")
    SearchResponse searchAppsByPingyin(@Query("key") String str, @Query("agent_type") String str2, @Query("is_app_suggest") String str3, @Query("uuid") String str4);

    @GET("/apis/app/search/pinyin.action?if=app_py")
    void searchAppsByPingyinAsync(@Query("key") String str, @Query("agent_type") String str2, @Query("is_app_suggest") String str3, @Query("uuid") String str4, Callback<SearchResponse> callback);

    @GET("/apis/app/recommand/top.action?rltfmt=json")
    RecommendResponse topApps(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("rating") String str3, @Query("category") String str4, @Query("agent_type") String str5);

    @GET("/apis/app/recommand/top.action?rltfmt=json")
    void topAppsAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("rating") String str3, @Query("category") String str4, @Query("agent_type") String str5, Callback<RecommendResponse> callback);

    @GET("/apis/app/modify.action")
    StatusResponse updateAppsStatus(@Query("authcookie") String str, @Query("device_id") String str2, @Query("app_ids") String str3, @Query("app_vers") String str4, @Query("op_type") String str5, @Query("agent_type") String str6);

    @GET("/apis/app/modify.action")
    void updateAppsStatusAsync(@Query("authcookie") String str, @Query("device_id") String str2, @Query("app_ids") String str3, @Query("app_vers") String str4, @Query("op_type") String str5, @Query("agent_type") String str6, Callback<StatusResponse> callback);
}
