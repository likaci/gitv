package com.tvos.appdetailpage.rest;

import retrofit.ResponseCallback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface CloudPushRest {
    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    Response pushedAppStatsPingback(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22, @Query("yunsourceid") String str23, @Query("sourcetype") String str24);

    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    void pushedAppStatsPingbackAsync(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22, @Query("yunsourceid") String str23, @Query("sourcetype") String str24, ResponseCallback responseCallback);

    @GET("/b")
    Response receivePushedAppPingback(@Query("t") String str, @Query("pf") String str2, @Query("p") String str3, @Query("p1") String str4, @Query("p2") String str5, @Query("bstp") String str6, @Query("block") String str7, @Query("u") String str8, @Query("pu") String str9, @Query("rn") String str10, @Query("yunsourceid") String str11, @Query("qpid") String str12, @Query("sourcetype") String str13, @Query("datatype") String str14);

    @GET("/b")
    void receivePushedAppPingbackAsync(@Query("t") String str, @Query("pf") String str2, @Query("p") String str3, @Query("p1") String str4, @Query("p2") String str5, @Query("bstp") String str6, @Query("block") String str7, @Query("u") String str8, @Query("pu") String str9, @Query("rn") String str10, @Query("yunsourceid") String str11, @Query("qpid") String str12, @Query("sourcetype") String str13, @Query("datatype") String str14, ResponseCallback responseCallback);
}
