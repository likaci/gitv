package com.gala.video.app.epg.ui.albumlist;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ContentType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.HistoryJumpKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.JumpKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.Wrapper;

public class AlbumInfoHelper extends Wrapper {
    private static final String TAG = "EPG/album4/ItemUtils";

    public JumpKind getJumpType(Album album) {
        if (album.type == 0) {
            if (album.isSeries == 1) {
                if (album.chnId == 10) {
                    return JumpKind.PLAY;
                }
                if (album.getContentType() != ContentType.FEATURE_FILM) {
                    return JumpKind.PLAY;
                }
                return JumpKind.DETAILS;
            } else if (album.chnId == 1 || album.chnId == 2 || album.chnId == 4 || album.chnId == 15) {
                return JumpKind.DETAILS;
            } else {
                if (album.isVipForAccount() || (album.isSinglePay() && !album.isVipForAccount())) {
                    return JumpKind.DETAILS;
                }
                return JumpKind.PLAY;
            }
        } else if (album.type == 1) {
            if (TextUtils.isEmpty(album.sourceCode) || "0".equals(album.sourceCode)) {
                return JumpKind.DETAILS;
            }
            return JumpKind.DETAILS;
        } else if (album.type == 2) {
            return JumpKind.PLAY_LIST;
        } else {
            LogUtils.m1571e(TAG, "getJumpType --- error---info.type=" + album.type);
            return null;
        }
    }

    public AlbumKind getAlbumType(Album album) {
        if (album == null) {
            return AlbumKind.SIGLE_VIDEO;
        }
        if (album.type == 0) {
            if (album.isSeries == 0 && (TextUtils.isEmpty(album.sourceCode) || "0".equals(album.sourceCode))) {
                return AlbumKind.SIGLE_VIDEO;
            }
            if (album.isSeries == 1 && (TextUtils.isEmpty(album.sourceCode) || "0".equals(album.sourceCode))) {
                return AlbumKind.SIGLE_UNIT;
            }
            if (album.isSeries == 1 && !(TextUtils.isEmpty(album.sourceCode) && "0".equals(album.sourceCode))) {
                return AlbumKind.SIGLE_SERIES;
            }
        } else if (album.isSeries == 1 && (TextUtils.isEmpty(album.sourceCode) || "0".equals(album.sourceCode))) {
            return AlbumKind.SERIES_ALBUM;
        } else {
            if (album.isSeries == 1 && !(TextUtils.isEmpty(album.sourceCode) && "0".equals(album.sourceCode))) {
                return AlbumKind.SOURCE_ALBUM;
            }
        }
        return AlbumKind.SIGLE_VIDEO;
    }

    public boolean isSingleType(Album album) {
        AlbumKind albumType = getAlbumType(album);
        if (albumType.equals(AlbumKind.SIGLE_VIDEO) || albumType.equals(AlbumKind.SIGLE_SERIES) || albumType.equals(AlbumKind.SIGLE_UNIT)) {
            return true;
        }
        return false;
    }

    public boolean isSingleType1(Album album) {
        AlbumKind albumType = getAlbumType(album);
        if (albumType.equals(AlbumKind.SIGLE_SERIES) || albumType.equals(AlbumKind.SIGLE_UNIT)) {
            return true;
        }
        return false;
    }

    public boolean isSingleType2(Album album) {
        return getAlbumType(album).equals(AlbumKind.SIGLE_VIDEO);
    }

    public boolean isSingleType3(Album album) {
        AlbumKind albumType = getAlbumType(album);
        if (albumType.equals(AlbumKind.SERIES_ALBUM) || albumType.equals(AlbumKind.SOURCE_ALBUM)) {
            return true;
        }
        return false;
    }

    private boolean isTrailers(Album album) {
        if (album.chnId == 10 || album.getContentType() != ContentType.FEATURE_FILM) {
            return true;
        }
        return false;
    }

    public HistoryJumpKind getHistoryJumpKind(Album album) {
        if (isSingleType2(album)) {
            if (album.chnId == 1 || album.chnId == 2 || album.chnId == 4 || album.chnId == 15) {
                return HistoryJumpKind.HISTORY_DETAILS;
            }
            if (album.isVipForAccount() || (album.isSinglePay() && !album.isVipForAccount())) {
                return HistoryJumpKind.HISTORY_DETAILS;
            }
            return HistoryJumpKind.HISTORY_PLAY;
        } else if (isSingleType3(album)) {
            return HistoryJumpKind.HISTORY_DETAILS;
        } else {
            if (isSingleType1(album)) {
                return isTrailers(album) ? HistoryJumpKind.HISTORY_PLAY : HistoryJumpKind.HISTORY_DETAILS;
            } else {
                return HistoryJumpKind.HISTORY_PLAY;
            }
        }
    }
}
