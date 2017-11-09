package com.gala.video.app.epg.ui.albumlist.data.factory;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.project.Project;
import java.util.List;

public class DataInfoProvider {
    public static String getLiveVersion() {
        return "5.0";
    }

    public static int getSelectViewType(List multiTags, int mChannelId) {
        boolean isLiveChannel;
        boolean isSupportSelect;
        if (mChannelId == 1000004) {
            isLiveChannel = true;
        } else {
            isLiveChannel = false;
        }
        if (ListUtils.isEmpty(multiTags)) {
            isSupportSelect = false;
        } else {
            isSupportSelect = true;
        }
        boolean isSupportCarousel = Project.getInstance().getControl().isOpenCarousel();
        if (isLiveChannel) {
            if (isSupportCarousel) {
                return 3;
            }
            return 0;
        } else if (isSupportSelect) {
            return 2;
        } else {
            return 0;
        }
    }

    public static boolean isRecommend1Type(int channelId) {
        if (channelId == 1 || channelId == 2 || channelId == 1000002 || channelId == 1000004) {
            return true;
        }
        return false;
    }

    public static boolean isRecommend3Type(int channelId) {
        if (channelId == 5) {
            return true;
        }
        return false;
    }

    public static boolean isRecommend2Type(int channelId) {
        if (channelId == 4 || channelId == 15) {
            return true;
        }
        return false;
    }

    public static boolean isCardShowing(IData info) {
        if (info == null) {
            return false;
        }
        return info.isShowingCard();
    }

    public static boolean isCardData(IData info) {
        if (info == null || info.getAlbum() == null) {
            return false;
        }
        return isCardData(info.getAlbum());
    }

    public static boolean isCardData(Album album) {
        if (album == null || album.getcard().type == 99) {
            return false;
        }
        return true;
    }

    public static int getCardType(IData info) {
        if (info == null || info.getAlbum() == null) {
            return 99;
        }
        return info.getAlbum().getcard().type;
    }

    public static int getCardType(Album album) {
        if (album == null) {
            return 99;
        }
        return album.getcard().type;
    }
}
