package com.gala.sdk.utils;

public class PingbackUtils {
    public static final String REQUEST_ALBUMINFO = "tv_albumInfo";
    public static final String REQUEST_CHECKVIP = "boss_authVipVideo";
    public static final String REQUEST_CHECKVIPFORPUSH = "boss_authVipPushVideo";
    public static final String REQUEST_EPISODE = "tv_episodeList";
    public static final String REQUEST_LIVE_FREE = "vrs_livem3u8free";
    public static final String REQUEST_LIVE_VIP = "vrs_livem3u8vip";
    public static final String REQUEST_NATIVEPLAYERINIT = "player_nativePlayerInit";
    public static final String REQUEST_PASSPORT_USERINFO = "passport_userinfo";
    public static final String REQUEST_PLAYCHECK = "tv_playCheck";
    public static final String REQUEST_PLAYLOADING = "player_loading";
    public static final String REQUEST_PUSHALBUMACTION = "tv_pushAlbumAction";
    public static final String REQUEST_SYSTEMPLAYERINIT = "player_systemPlayerInit";
    public static final String REQUEST_TMTS = "vrs_m3u8FromTvidVid";
    public static final String REQUEST_TMTS_FOR_PUSH = "vrs_pushVideo";
    public static final String REQUEST_VIDEOINFO_FOR_PUSH = "vrs_videoInfo";

    public static boolean isDataRequest(String requestName) {
        if (REQUEST_ALBUMINFO.equals(requestName) || REQUEST_EPISODE.equals(requestName)) {
            return true;
        }
        return false;
    }

    public static boolean isAuthRequest(String requestName) {
        if (REQUEST_CHECKVIP.equals(requestName) || REQUEST_PLAYCHECK.equals(requestName) || REQUEST_TMTS.equals(requestName) || REQUEST_PUSHALBUMACTION.equals(requestName)) {
            return true;
        }
        return false;
    }
}
