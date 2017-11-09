package com.tvos.appdetailpage.rest;

import com.tvos.appdetailpage.info.MutualAppsListResponse;
import com.tvos.appdetailpage.info.MyAppInfoResponse;
import com.tvos.appdetailpage.info.MyAppsListResponse;
import com.tvos.appdetailpage.info.MySearchHistoryResponse;
import com.tvos.appdetailpage.info.StatusResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface UserGameRest {
    @GET("/usergameInfo/downloadApp.htm")
    StatusResponse downloadGame(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("ppsAppId") String str5, @Query("appType") String str6, @Query("appPlatform") String str7, @Query("installSource") String str8);

    @GET("/usergameInfo/downloadApp.htm")
    void downloadGameAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("ppsAppId") String str5, @Query("appType") String str6, @Query("appPlatform") String str7, @Query("installSource") String str8, Callback<StatusResponse> callback);

    @GET("/usergameInfo/mutualAppList.htm")
    MutualAppsListResponse getMutualAppsList(@Query("authcookie") String str, @Query("uid") String str2, @Query("anotherUid") String str3, @Query("appType") String str4, @Query("platform") String str5, @Query("page") int i, @Query("rows") int i2, @Query("all") String str6);

    @GET("/usergameInfo/mutualAppList.htm")
    void getMutualAppsListAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("anotherUid") String str3, @Query("appType") String str4, @Query("platform") String str5, @Query("page") int i, @Query("rows") int i2, @Query("all") String str6, Callback<MutualAppsListResponse> callback);

    @GET("/usergameInfo/appInfo.htm")
    MyAppInfoResponse getMyAppInfo(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3);

    @GET("/usergameInfo/appInfo.htm")
    void getMyAppInfoAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3, Callback<MyAppInfoResponse> callback);

    @GET("/usergameInfo/appList.htm")
    MyAppsListResponse getMyAppsList(@Query("authcookie") String str, @Query("uid") String str2, @Query("appType") String str3, @Query("platform") String str4, @Query("page") int i, @Query("rows") int i2, @Query("all") String str5, @Query("sort") String str6, @Query("order") String str7);

    @GET("/usergameInfo/appList.htm")
    void getMyAppsListAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("appType") String str3, @Query("platform") String str4, @Query("page") int i, @Query("rows") int i2, @Query("all") String str5, @Query("sort") String str6, @Query("order") String str7, Callback<MyAppsListResponse> callback);

    @GET("/searchHistory/query.htm")
    MySearchHistoryResponse getSearchHistory(@Query("authcookie") String str, @Query("uid") String str2, @Query("page") int i, @Query("rows") int i2);

    @GET("/searchHistory/query.htm")
    void getSearchHistoryAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("page") int i, @Query("rows") int i2, Callback<MySearchHistoryResponse> callback);

    @GET("/usergameInfo/installApp.htm")
    StatusResponse installGame(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("ppsAppId") String str5, @Query("appType") String str6, @Query("appPlatform") String str7, @Query("installSource") String str8);

    @GET("/usergameInfo/installApp.htm")
    void installGameAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("ppsAppId") String str5, @Query("appType") String str6, @Query("appPlatform") String str7, @Query("installSource") String str8, Callback<StatusResponse> callback);

    @GET("/usergameInfo/loginApp.htm")
    StatusResponse loginApp(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3, @Query("serverId") String str4, @Query("platform") String str5, @Query("qipuId") String str6, @Query("loginTime") String str7);

    @GET("/usergameInfo/loginApp.htm")
    void loginAppAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3, @Query("serverId") String str4, @Query("platform") String str5, @Query("qipuId") String str6, @Query("loginTime") String str7, Callback<StatusResponse> callback);

    @GET("/usergameInfo/operateHistory.htm")
    StatusResponse operateHistory(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("platform") String str5, @Query("operate") String str6, @Query("createTime") String str7);

    @GET("/usergameInfo/operateHistory.htm")
    void operateHistoryAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("platform") String str5, @Query("operate") String str6, @Query("createTime") String str7, Callback<StatusResponse> callback);

    @GET("/usergameInfo/topshow.htm")
    StatusResponse stickApp(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3, @Query("appType") String str4);

    @GET("/usergameInfo/topshow.htm")
    void stickAppAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("appId") String str3, @Query("appType") String str4, Callback<StatusResponse> callback);

    @GET("/usergameInfo/installApp.htm")
    StatusResponse uninstallGame(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("appPlatform") String str5);

    @GET("/usergameInfo/installApp.htm")
    void uninstallGameAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("appId") String str4, @Query("appPlatform") String str5, Callback<StatusResponse> callback);

    @GET("/searchHistory/upload.htm")
    StatusResponse uploadSearch(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("searchText") String str4);

    @GET("/searchHistory/upload.htm")
    void uploadSearchAsync(@Query("authcookie") String str, @Query("uid") String str2, @Query("deviceId") String str3, @Query("searchText") String str4, Callback<StatusResponse> callback);
}
