package com.gala.video.app.player.utils;

import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class ChannelDataHelper {
    private static final String TAG = "Player/Utils/ChannelDataHelper";

    private ChannelDataHelper() {
    }

    public static boolean showEpisodeAsGallery(int chId) {
        int[] channelIds = PlayerAppConfig.showEpisodeAsGallery();
        boolean ret = false;
        for (int i : channelIds) {
            if (chId == i) {
                ret = true;
                break;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isEpisodesWithImageChannelIds: ret=" + ret);
        }
        return ret;
    }
}
