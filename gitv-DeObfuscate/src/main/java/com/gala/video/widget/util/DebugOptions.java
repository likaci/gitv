package com.gala.video.widget.util;

import android.graphics.Color;
import java.util.concurrent.ConcurrentHashMap;

public class DebugOptions {
    public static final int DEBUG_BG_COLOR = Color.parseColor("#66AABBCC");
    public static final String DEBUG_LOG = "gala.gridpageview.dbglog";
    public static final String DEBUG_MODE = "gala.gridpageview.dbgmode";
    public static final boolean LOG = isOpenDebugLog();
    private static ConcurrentHashMap<String, Object> sKeyValCache = new ConcurrentHashMap();

    private DebugOptions() {
    }

    public static boolean isOpenDebugLog() {
        return fetchAndRecordBoolean(DEBUG_LOG, false);
    }

    public static boolean isInDebugMode() {
        return fetchAndRecordBoolean(DEBUG_MODE, false);
    }

    private static boolean fetchAndRecordBoolean(String key, boolean def) {
        if (sKeyValCache.containsKey(key)) {
            return ((Boolean) sKeyValCache.get(key)).booleanValue();
        }
        boolean ret = SysPropUtils.getBoolean(key, def);
        sKeyValCache.put(key, Boolean.valueOf(ret));
        return ret;
    }
}
