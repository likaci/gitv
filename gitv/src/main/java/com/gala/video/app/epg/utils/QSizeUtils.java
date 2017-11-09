package com.gala.video.app.epg.utils;

import android.widget.TextView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class QSizeUtils {
    public static void setTextSize(TextView tv, int deminId) {
        if (tv != null) {
            tv.setTextSize(0, (float) ResourceUtil.getDimensionPixelSize(deminId));
        }
    }
}
