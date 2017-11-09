package com.tvos.appdetailpage.rest;

import retrofit.ResponseCallback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PingbackRest {
    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    Response appStatsPingback(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22);

    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    void appStatsPingbackAsync(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22, ResponseCallback responseCallback);

    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    Response pushedAppStatsPingback(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22, @Query("yunsourceid") String str23, @Query("sourcetype") String str24);

    @GET("/tmpstats.gif?type=140704yyzx&os=android")
    void pushedAppStatsPingbackAsync(@Query("pf") String str, @Query("p") String str2, @Query("p1") String str3, @Query("p2") String str4, @Query("u") String str5, @Query("pu") String str6, @Query("rn") String str7, @Query("action") String str8, @Query("appid") String str9, @Query("appversion") String str10, @Query("deviceID") String str11, @Query("macid") String str12, @Query("osversion") String str13, @Query("v") String str14, @Query("channelid") String str15, @Query("tvchannelid") String str16, @Query("rpage") String str17, @Query("block") String str18, @Query("rseat") String str19, @Query("r") String str20, @Query("serverid") String str21, @Query("errorcode") String str22, @Query("yunsourceid") String str23, @Query("sourcetype") String str24, ResponseCallback responseCallback);

    @GET("/tmpstats.gif")
    Response recPingback(@Query("type") String str, @Query("usract") String str2, @Query("ppuid") String str3, @Query("uid") String str4, @Query("event_id") String str5, @Query("bkt") String str6, @Query("area") String str7, @Query("platform") String str8, @Query("applist") String str9, @Query("rank") String str10, @Query("taid") String str11, @Query("aid") String str12);

    @GET("/tmpstats.gif")
    void recPingbackAsync(@Query("type") String str, @Query("usract") String str2, @Query("ppuid") String str3, @Query("uid") String str4, @Query("event_id") String str5, @Query("bkt") String str6, @Query("area") String str7, @Query("platform") String str8, @Query("applist") String str9, @Query("rank") String str10, @Query("taid") String str11, @Query("aid") String str12, ResponseCallback responseCallback);
}
