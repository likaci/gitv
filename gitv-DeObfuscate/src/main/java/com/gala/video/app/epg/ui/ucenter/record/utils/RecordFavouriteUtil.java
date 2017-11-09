package com.gala.video.app.epg.ui.ucenter.record.utils;

import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;

public class RecordFavouriteUtil {
    public static String getS1FromForPingBack(FootLeftRefreshPage page) {
        if (page == FootLeftRefreshPage.FAVOURITE) {
            return "favorite";
        }
        if (page == FootLeftRefreshPage.PLAY_HISTORY_ALL) {
            return "history";
        }
        if (page == FootLeftRefreshPage.PLAY_HISTORY_LONG) {
            return LoginConstant.S1_FROM_LONGHIS;
        }
        return "";
    }
}
