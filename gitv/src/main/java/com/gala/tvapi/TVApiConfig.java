package com.gala.tvapi;

import com.gala.tvapi.tv2.TVApiBase;

public class TVApiConfig {
    public static String VRS_CACHE_SERVER = "http://cache.video.gala.com/";
    public static String VRS_RECOMMEND_SERVER = "http://mixer.video.igala.com/";
    protected static String a = "http://data2.itv.igala.com/itv/";
    protected static String b = "http://data2.itv.igala.com/";
    protected static String c = "http://cache.m.igala.com/tmts/";
    protected static String d = "http://data.video.gala.com/";
    protected static String e = "http://serv.vip.gala.com/services/";
    protected static String f = "http://serv.vip.igala.com/";
    protected static String g = "http://i.vip.igala.com/";
    protected static String h = "http://api.vip.igala.com/services/";
    protected static String i = "http://qisu.video.gala.com/r/qisu/";
    protected static String j = "http://subscription.igala.com/";
    protected static String k = "http://subscription.igala.com/dingyue/api/";
    protected static String l = "http://subscription.igala.com/apis/";
    protected static String m = "http://nl.rcd.igala.com/apis/tv/device/";
    protected static String n = "http://l.rcd.igala.com/apis/tv/user/";
    protected static String o = "https://passport.igala.com/apis/";
    protected static String p = "https://cloudpush.igala.com/apis/";
    protected static String q = "http://store.igala.com/";
    protected static String r = "https://papaq.igala.com/";
    protected static String s = "http://qiyu.igala.com/";
    protected static String t = "http://score.video.igala.com/";
    protected static String u = "http://pdata.video.gala.com/";
    protected static String v = "http://cache.vip.gala.com/";
    protected static String w = "http://itv.video.igala.com/";

    public static void setDomain(String domain) {
        TVApiBase.getTVApiProperty().setDomain(domain);
    }
}
