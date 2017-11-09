package com.gala.video.app.player.pingback;

import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;

public class PingbackCommonFieldUtils {
    private static final String TAG = "PingbackCommonFieldUtils";

    public static String getStateField(IVideo video) {
        String state = "";
        if (video != null) {
            if (SourceType.LIVE != video.getSourceType()) {
                return state;
            }
            if (video.isTrailer()) {
                return WebConstants.STATE_COMING;
            }
            return WebConstants.STATE_ONAIR;
        }
        LogRecordUtils.logd(TAG, "getStateField, video is null.");
        return state;
    }
}
