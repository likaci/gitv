package com.tvos.appdetailpage.rest;

import com.tvos.appdetailpage.info.AppUpdateResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TVUpdateRest {
    @GET("/apis/app/check_update.action")
    AppUpdateResponse getAppUpdateInfo(@Query("agent_type") String str, @Query("app_id") String str2, @Query("ver") int i, @Query("uuid") String str3);

    @GET("/apis/app/check_update.action")
    void getAppUpdateInfoAsync(@Query("agent_type") String str, @Query("app_id") String str2, @Query("ver") int i, @Query("uuid") String str3, Callback<AppUpdateResponse> callback);
}
