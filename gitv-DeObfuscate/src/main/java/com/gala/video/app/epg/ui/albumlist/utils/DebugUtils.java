package com.gala.video.app.epg.ui.albumlist.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;

public class DebugUtils {
    public static final boolean ALBUM4_NEEDLOG = LogUtils.mIsDebug;

    public static void limitNetSpeed() {
    }

    public static String print() {
        return "[DebugUtils] album4_testing:" + ALBUM4_NEEDLOG;
    }
}
