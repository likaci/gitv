package com.gala.video.app.epg.utils;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class UtilsPreference {
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "channel_name";
    private static final String CHANNEL_TABLE_NO = "channel_no";
    private static final String NAME = "carousel_history";
    private static final String TAG = "UtilsPreference";

    public static String getCarouselChannelIdFromHistory(Context context) {
        String channelId = new AppPreference(context, NAME).get("channel_id", "");
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getCarouselChannelIdFromHistory: channelId=" + channelId);
        }
        return channelId;
    }

    public static String getCarouselChannelNameFromHistory(Context context) {
        String channelName = new AppPreference(context, NAME).get("channel_name", "");
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getCarouselChannelNameFromHistory: channelName=" + channelName);
        }
        return channelName;
    }

    public static String getCarouselChannelTableNoFromHistory(Context context) {
        return new AppPreference(context, NAME).get(CHANNEL_TABLE_NO, "");
    }
}
