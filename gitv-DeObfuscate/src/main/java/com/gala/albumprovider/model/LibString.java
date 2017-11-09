package com.gala.albumprovider.model;

import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;

public class LibString {
    public static String ChannelPlayListTagName = "专题";
    public static String DefaultTagName = "全部";
    public static String FavouritesName = "收藏";
    public static String HotTagName = "最近热播";
    public static String MySubscribe = "我的订阅";
    public static String NewestTagName = "最新上线";
    public static String PlayHistoryName = "播放记录";
    public static String ReasonGuessLike = "因为您喜欢";
    public static String ReasonHistory = "因为您看过";
    public static String RecommendTagName = "精彩推荐";
    public static String SearchName = ActionBarDataFactory.TOP_BAR_TIME_NAME_SEARCH;
    public static String offline = "离线";

    public static void InitLibString() {
        if (TVApiBase.getTVApiProperty().getPlatform().equals(PlatformType.TAIWAN)) {
            FavouritesName = "收藏";
            PlayHistoryName = "播放記錄";
            SearchName = ActionBarDataFactory.TOP_BAR_TIME_NAME_SEARCH;
            ChannelPlayListTagName = "專題";
            NewestTagName = "最新";
            HotTagName = "最熱";
            RecommendTagName = "推薦";
            DefaultTagName = "全部";
            ReasonGuessLike = "因為您喜歡";
            ReasonHistory = "因為您看過";
            MySubscribe = "我的影院";
            USALog.m147d(ReasonHistory);
        }
    }
}
