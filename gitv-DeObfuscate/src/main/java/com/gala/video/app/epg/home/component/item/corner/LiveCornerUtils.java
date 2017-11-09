package com.gala.video.app.epg.home.component.item.corner;

import android.util.Log;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.Date;

public class LiveCornerUtils {
    private static final String TAG = "LiveCornerUtils";

    public static boolean isLiveEndData(ChannelLabel label) {
        return LivePlayingType.END.equals(label.getLivePlayingType());
    }

    public static long formatTime(String time) {
        long j = 0;
        if (!StringUtils.isEmpty((CharSequence) time)) {
            try {
                j = DateLocalThread.parseYH(time).getTime();
            } catch (Exception e) {
                Log.e(TAG, "formatTime error, time=" + time);
            }
        }
        return j;
    }

    public static String getLiveBoforeDate(String sliveTime) {
        if (StringUtils.isEmpty((CharSequence) sliveTime)) {
            return "";
        }
        try {
            Long l = Long.valueOf(sliveTime);
            Date date = new Date();
            date.setTime(l.longValue());
            return DateLocalThread.formatM(date);
        } catch (NumberFormatException e) {
            Log.e(TAG, "getLiveDate error, sliveTime=" + sliveTime);
            return "";
        }
    }

    public static String getLivingCount(String viewerShip) {
        if (StringUtils.isEmpty((CharSequence) viewerShip)) {
            return "";
        }
        return viewerShip + "人正在看！";
    }

    public static String getLiveEndCount(String viewerShip) {
        if (StringUtils.isEmpty((CharSequence) viewerShip)) {
            return "";
        }
        return viewerShip + "人看过";
    }
}
