package com.tvos.appdetailpage.rest;

import com.tvos.appdetailpage.info.RecommendResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RecommendRest {
    @GET("/app/p13n?rltfmt=json")
    RecommendResponse appsForUser(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("rating") String str5);

    @GET("/app/p13n?rltfmt=json")
    void appsForUserAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("rating") String str5, Callback<RecommendResponse> callback);

    @GET("/app/resys?rltfmt=json")
    RecommendResponse associatedApps(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("qipu_id") String str5, @Query("rating") String str6);

    @GET("/app/resys?rltfmt=json")
    void associatedAppsAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("uid") String str3, @Query("ppuid") String str4, @Query("qipu_id") String str5, @Query("rating") String str6, Callback<RecommendResponse> callback);

    @GET("/app/top?rltfmt=json")
    RecommendResponse topApps(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("rating") String str3, @Query("category") String str4);

    @GET("/app/top?rltfmt=json")
    void topAppsAsync(@Query("area") String str, @Query("platform") String str2, @Query("rltnum") int i, @Query("rating") String str3, @Query("category") String str4, Callback<RecommendResponse> callback);
}
