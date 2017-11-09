package com.gala.video.lib.share.ifimpl.netdiagnose.collection;

public class PingConfig {
    public static String CACHE_M = "cache.m.gala.com";
    private static final String CACHE_M_PTQY_GITV_TV = "cache.m.ptqy.gitv.tv";
    public static String CACHE_VIDEO = "cache.video.gala.com";
    private static final String CACHE_VIDEO_PTQY_GITV_TV = "cache.video.ptqy.gitv.tv";
    public static String DATA2_ITV = "data2.itv.gala.com";
    public static final String DATA2_ITV_PTQY_GITV_TV = "data2.itv.ptqy.gitv.tv";
    public static final String DATA_VIDEO_PTQY_GITV_TV = "data.video.ptqy.gitv.tv";
    public static String ITV_VIDEO = "itv.video.gala.com";
    private static final String ITV_VIDEO_PTQY_GITV_TV = "itv.video.ptqy.gitv.tv";
    public static String PDATA_VIDEO = "pdata.video.gala.com";
    private static final String PTQY_GITV_TV = "ptqy.gitv.tv";

    public static void config(String domainName) {
        DATA2_ITV = DATA2_ITV_PTQY_GITV_TV;
        CACHE_VIDEO = CACHE_VIDEO_PTQY_GITV_TV;
        CACHE_M = CACHE_M_PTQY_GITV_TV;
        PDATA_VIDEO = DATA_VIDEO_PTQY_GITV_TV;
        ITV_VIDEO = ITV_VIDEO_PTQY_GITV_TV;
    }
}
