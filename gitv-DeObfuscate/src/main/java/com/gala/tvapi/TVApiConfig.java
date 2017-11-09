package com.gala.tvapi;

import com.gala.tvapi.tv2.TVApiBase;

public class TVApiConfig {
    public static String VRS_CACHE_SERVER = "http://cache.video.gala.com/";
    public static String VRS_RECOMMEND_SERVER = "http://mixer.video.igala.com/";
    protected static String f857a = "http://data2.itv.igala.com/itv/";
    protected static String f858b = "http://data2.itv.igala.com/";
    protected static String f859c = "http://cache.m.igala.com/tmts/";
    protected static String f860d = "http://data.video.gala.com/";
    protected static String f861e = "http://serv.vip.gala.com/services/";
    protected static String f862f = "http://serv.vip.igala.com/";
    protected static String f863g = "http://i.vip.igala.com/";
    protected static String f864h = "http://api.vip.igala.com/services/";
    protected static String f865i = "http://qisu.video.gala.com/r/qisu/";
    protected static String f866j = "http://subscription.igala.com/";
    protected static String f867k = "http://subscription.igala.com/dingyue/api/";
    protected static String f868l = "http://subscription.igala.com/apis/";
    protected static String f869m = "http://nl.rcd.igala.com/apis/tv/device/";
    protected static String f870n = "http://l.rcd.igala.com/apis/tv/user/";
    protected static String f871o = "https://passport.igala.com/apis/";
    protected static String f872p = "https://cloudpush.igala.com/apis/";
    protected static String f873q = "http://store.igala.com/";
    protected static String f874r = "https://papaq.igala.com/";
    protected static String f875s = "http://qiyu.igala.com/";
    protected static String f876t = "http://score.video.igala.com/";
    protected static String f877u = "http://pdata.video.gala.com/";
    protected static String f878v = "http://cache.vip.gala.com/";
    protected static String f879w = "http://itv.video.igala.com/";

    public static void setDomain(String domain) {
        TVApiBase.getTVApiProperty().setDomain(domain);
    }
}
