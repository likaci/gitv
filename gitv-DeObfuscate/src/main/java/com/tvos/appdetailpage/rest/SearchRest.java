package com.tvos.appdetailpage.rest;

import com.tvos.appdetailpage.info.SearchResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface SearchRest {
    @GET("/qiso3?if=app&type=list")
    SearchResponse listApps(@Query("mode") String str, @Query("app_type") String str2, @Query("game_type") String str3, @Query("price") String str4, @Query("total_rating") String str5, @Query("running_platform") String str6, @Query("package_size") String str7, @Query("game_style") String str8, @Query("painting_style") String str9, @Query("available_status") String str10, @Query("top_category_id") String str11, @Query("pageSize") int i, @Query("pageNum") int i2);

    @GET("/qiso3?if=app&type=list")
    void listAppsAsync(@Query("mode") String str, @Query("app_type") String str2, @Query("game_type") String str3, @Query("price") String str4, @Query("total_rating") String str5, @Query("running_platform") String str6, @Query("package_size") String str7, @Query("game_style") String str8, @Query("painting_style") String str9, @Query("available_status") String str10, @Query("top_category_id") String str11, @Query("pageSize") int i, @Query("pageNum") int i2, Callback<SearchResponse> callback);

    @GET("/qiso3/?if=app")
    SearchResponse searchApps(@Query("key") String str, @Query("mode") String str2, @Query("app_type") String str3, @Query("game_type") String str4, @Query("running_platform") String str5, @Query("pageSize") int i, @Query("pageNum") int i2);

    @GET("/qiso3/?if=app")
    void searchAppsAsync(@Query("key") String str, @Query("mode") String str2, @Query("app_type") String str3, @Query("game_type") String str4, @Query("running_platform") String str5, @Query("pageSize") int i, @Query("pageNum") int i2, Callback<SearchResponse> callback);

    @GET("/m?if=app_py")
    SearchResponse searchAppsByPingyin(@Query("key") String str);

    @GET("/m?if=app_py")
    void searchAppsByPingyinAsync(@Query("key") String str, Callback<SearchResponse> callback);
}
