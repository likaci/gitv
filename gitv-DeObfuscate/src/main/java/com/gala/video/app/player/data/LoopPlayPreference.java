package com.gala.video.app.player.data;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class LoopPlayPreference {
    private static final String LOOP_PLAY_ALBUM_ID = "albumid";
    private static final String LOOP_PLAY_INDEX = "index";
    private static final String LOOP_PLAY_OFFSET = "offset";
    private static final String NAME = "LoopPlayRecords";
    private static final String NEWS_PLAY_TAB_ID = "tabid";
    private static final String NEWS_PLAY_TAB_INDEX = "tabindex";
    private static final String NEWS_PLAY_TAB_PLAY_INDEX = "tab_playindex";
    private static final String NEWS_PLAY_TAB_PLAY_TVQID = "tab_playtvqid";
    private static final String TAG = "LoopPlayPreference";

    public static void setLoopPlayAlbumInfo(Context ctx, String albumId, int index, int offset, int aIndex) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setLoopPlayAlbumInfo(" + albumId + ", " + index + ", " + offset + ", " + aIndex + ")");
        }
        setLoopPlayAlbumId(ctx, albumId, aIndex);
        setLoopPlayIndex(ctx, index, aIndex);
        setLoopPlayOffset(ctx, offset, aIndex);
    }

    public static void setLoopPlayAlbumId(Context ctx, String albumId, int aIndex) {
        new AppPreference(ctx, NAME).save("albumid" + aIndex, albumId);
    }

    public static void setLoopPlayIndex(Context ctx, int index, int aIndex) {
        new AppPreference(ctx, NAME).save(LOOP_PLAY_INDEX + aIndex, index);
    }

    public static void setLoopPlayOffset(Context ctx, int offset, int aIndex) {
        new AppPreference(ctx, NAME).save(LOOP_PLAY_OFFSET + aIndex, offset);
    }

    public static String getLoopPlayAlbumId(Context ctx, int loopPosition) {
        return new AppPreference(ctx, NAME).get("albumid" + loopPosition, null);
    }

    public static int getLoopPlayIndex(Context ctx, int loopPosition) {
        return new AppPreference(ctx, NAME).getInt(LOOP_PLAY_INDEX + loopPosition, -1);
    }

    public static int getLoopPlayOffset(Context ctx, int loopPosition) {
        return new AppPreference(ctx, NAME).getInt(LOOP_PLAY_OFFSET + loopPosition, -1);
    }

    public static void savePlayTabIndex(Context ctx, String type, int tabIndex) {
        new AppPreference(ctx, NAME).save(NEWS_PLAY_TAB_INDEX + type, tabIndex);
    }

    public static int getPlayTabIndex(Context ctx, String type) {
        return new AppPreference(ctx, NAME).getInt(NEWS_PLAY_TAB_INDEX + type, -1);
    }

    public static void savePlayTabId(Context ctx, String type, String tabId) {
        new AppPreference(ctx, NAME).save("tabid" + type, tabId);
    }

    public static String getPlayTabId(Context ctx, String type) {
        return new AppPreference(ctx, NAME).get("tabid" + type, null);
    }

    public static void savePlayIndexForEachTab(Context ctx, String type, int playIndex, String tabId) {
        new AppPreference(ctx, NAME).save(NEWS_PLAY_TAB_PLAY_INDEX + type + tabId, playIndex);
    }

    public static int getPlayIndexForEachTab(Context ctx, String type, String tabId) {
        return new AppPreference(ctx, NAME).getInt(NEWS_PLAY_TAB_PLAY_INDEX + type + tabId, -1);
    }

    public static void savePlayTvQidForEachTab(Context ctx, String type, String tvQid, String tabId) {
        new AppPreference(ctx, NAME).save(NEWS_PLAY_TAB_PLAY_TVQID + type + tabId, tvQid);
    }

    public static String getPlayTvQidForEachTab(Context ctx, String type, String tabId) {
        return new AppPreference(ctx, NAME).get(NEWS_PLAY_TAB_PLAY_TVQID + type + tabId, null);
    }
}
