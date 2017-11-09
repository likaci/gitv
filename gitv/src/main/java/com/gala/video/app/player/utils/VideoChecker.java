package com.gala.video.app.player.utils;

import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.List;

public class VideoChecker {
    private static final String TAG = "Player/Lib/Data/VideoChecker";

    private VideoChecker() {
    }

    public static boolean checkHistory(IVideo video) {
        boolean sanity = true;
        if (video == null) {
            return false;
        }
        int markSize = video.getEpisodeMaxOrder();
        int playOrder = video.getPlayOrder();
        int realSize = 0;
        if (video.getEpisodeVideos() != null) {
            realSize = video.getEpisodeVideos().size();
        }
        if (realSize != markSize) {
            LogUtils.w(TAG, "wrong episodes info! real(" + realSize + "), mark(" + markSize + ")");
            sanity = false;
        }
        if (playOrder <= realSize) {
            return sanity;
        }
        LogUtils.w(TAG, "wrong play order! play order(" + playOrder + "), real(" + realSize + ")");
        return false;
    }

    public static boolean isValidAlbumId(String albumId) {
        boolean valid = false;
        if (!StringUtils.isEmpty((CharSequence) albumId)) {
            valid = StringUtils.parse(albumId, -1) > 0;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isValidAlbumId(" + albumId + ") return " + valid);
        }
        return valid;
    }

    public static boolean isValidTvId(String vrsTvId) {
        boolean valid = false;
        if (!StringUtils.isEmpty((CharSequence) vrsTvId)) {
            valid = StringUtils.parse(vrsTvId, -1) > 0;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isValidTvId(" + vrsTvId + ") return " + valid);
        }
        return valid;
    }

    public static boolean isValidVid(String vid) {
        boolean valid = !StringUtils.isEmpty((CharSequence) vid) && vid.length() == 32;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isValidVid(" + vid + ") return " + valid);
        }
        return valid;
    }

    public static boolean isExistInEpisodeList(List<IVideo> episodeList, int playOrder) {
        boolean ret = false;
        if (!ListUtils.isEmpty((List) episodeList)) {
            for (IVideo e : episodeList) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "isExistInEpisodeList: episode order: " + e.getAlbum().order);
                }
                if (e.getAlbum().order == playOrder) {
                    ret = true;
                    break;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isExistInEpisodeList(" + playOrder + "): result=" + ret);
        }
        return ret;
    }

    public static boolean isExistInEpisodeListAlbum(List<AlbumInfo> episodeList, int playOrder) {
        boolean ret = false;
        if (!ListUtils.isEmpty((List) episodeList)) {
            for (AlbumInfo e : episodeList) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "isExistInEpisodeList: episode order: " + e.getAlbum().order);
                }
                if (e.getAlbum().order == playOrder) {
                    ret = true;
                    break;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isExistInEpisodeList(" + playOrder + "): result=" + ret);
        }
        return ret;
    }
}
