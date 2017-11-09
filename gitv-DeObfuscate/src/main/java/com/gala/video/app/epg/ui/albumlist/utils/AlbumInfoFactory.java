package com.gala.video.app.epg.ui.albumlist.utils;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.type.ChannelType;
import com.gala.video.app.epg.home.data.provider.ChannelProvider;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class AlbumInfoFactory {
    public static boolean isTrueChannel(int channelId) {
        Channel channel = getChannelByChannelId(channelId);
        return !ChannelType.VIRTUAL_CHANNEL.equals(channel != null ? channel.getChannelType() : ChannelType.VIRTUAL_CHANNEL);
    }

    public static boolean isSearchResultPage(String type) {
        return IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD.equals(type);
    }

    public static boolean isFootPage(String type) {
        return IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY.equals(type) || IAlbumConfig.UNIQUE_FOOT_FAVOURITE.equals(type);
    }

    public static boolean isStarPage(String type) {
        return IAlbumConfig.UNIQUE_STAR_PAGE.equals(type);
    }

    public static boolean isNewVipChannel(int chnId, String keyword) {
        return chnId == 1000002 && keyword == null;
    }

    public static boolean isNewVipChannel(int channelId) {
        return channelId == 1000002;
    }

    public static boolean isLiveChannel(int chnId, String keyword) {
        return chnId == 1000004 && keyword == null;
    }

    public static boolean isHotChannel(int chnId, String type) {
        return 10009 == chnId && IAlbumConfig.CHANNEL_PAGE.equals(type);
    }

    public static String getChannelNameByChannelId(int channelId) {
        if (10006 == channelId) {
            return IAlbumConfig.STR_VIP;
        }
        if (channelId == 1000002) {
            return IAlbumConfig.STR_NEW_VIP;
        }
        Channel c = getChannelByChannelId(channelId);
        return c == null ? "" : c.name;
    }

    public static Channel getChannelByChannelId(int channelId) {
        return ChannelProvider.getInstance().getChannelById(channelId);
    }

    public static boolean hasRecommendTag(int channelId) {
        Channel channel = getChannelByChannelId(channelId);
        if (channel == null || TextUtils.isEmpty(channel.focus)) {
            return false;
        }
        return true;
    }

    public static String getChannelNameByPageType(String pageType) {
        if (IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY.equals(pageType)) {
            return IFootConstant.STR_FILM_FOOT;
        }
        if (IAlbumConfig.UNIQUE_FOOT_FAVOURITE.equals(pageType)) {
            return IFootConstant.STR_FILM_FOOT;
        }
        return "";
    }

    public static boolean needShowLoadingView(String pageType) {
        return StringUtils.equals(IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD, pageType);
    }

    public static int getLoadingViewDelayedMillis(String pageType) {
        return StringUtils.equals(IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD, pageType) ? IAlbumConfig.DELAY_SHOW_LOADING_VIEW : 0;
    }
}
