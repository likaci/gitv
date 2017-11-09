package com.mcto.ads.internal.net;

import com.mcto.ads.internal.common.CupidUtils;
import java.util.HashMap;
import java.util.Map;

public class TrackingConstants {
    public static String ADX_TRACKING_URL = ("http://t7z.cupid." + CupidUtils.strReverse("iyiqi") + ".com/etx?");
    public static String CUPID_TRACKING_URL = ("http://t7z.cupid." + CupidUtils.strReverse("iyiqi") + ".com/track2?");
    public static final String TRACKING_EVENT_1Q = "firstQuartile";
    public static final String TRACKING_EVENT_3Q = "thirdQuartile";
    public static final String TRACKING_EVENT_DOWNLOADED = "downloaded";
    public static final String TRACKING_EVENT_DOWNLOAD_START = "downloadStart";
    public static final String TRACKING_EVENT_MID = "midpoint";
    public static final String TRACKING_EVENT_SKIP = "skip";
    public static final String TRACKING_EVENT_SP = "complete";
    public static final String TRACKING_EVENT_ST = "start";
    public static final String TRACKING_EVENT_TRUEVIEW = "trueview";
    public static final String TRACKING_EVENT_TYPE_CLICK = "click";
    public static final String TRACKING_EVENT_TYPE_EVENT = "event";
    public static final String TRACKING_EVENT_TYPE_IMPRESSION = "impression";
    public static final String TRACKING_KEY_APP_VERSION = "cv";
    public static final String TRACKING_KEY_CHECKSUM = "s";
    public static final String TRACKING_KEY_ENCRYPTION_VER = "ve";
    public static final String TRACKING_KEY_EVENT_TYPE = "a";
    public static final String TRACKING_KEY_SDK_VERSION = "sv";
    public static final String TRACKING_KEY_START_TIME = "st";
    public static final String TRACKING_KEY_TIMESTAMP = "b";
    public static final String TRACKING_KEY_VIDEOEVENTID = "r";
    private static Map<String, String> map = new HashMap();

    static {
        map.put("impression", "0");
        map.put("click", "1");
        map.put(TRACKING_EVENT_TRUEVIEW, "3");
        map.put("start", "10");
        map.put("firstQuartile", "11");
        map.put("midpoint", "12");
        map.put("thirdQuartile", "13");
        map.put("complete", "14");
        map.put(TRACKING_EVENT_DOWNLOAD_START, "20");
        map.put(TRACKING_EVENT_DOWNLOADED, "21");
    }

    public static String mapToNumEvent(String key) {
        return (String) map.get(key);
    }

    public static String mapToStringEvent(String value) {
        for (String key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }
}
