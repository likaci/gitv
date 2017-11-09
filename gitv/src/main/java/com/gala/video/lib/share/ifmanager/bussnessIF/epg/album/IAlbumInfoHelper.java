package com.gala.video.lib.share.ifmanager.bussnessIF.epg.album;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IAlbumInfoHelper extends IInterfaceWrapper {

    public static abstract class Wrapper implements IAlbumInfoHelper {
        public Object getInterface() {
            return this;
        }

        public static IAlbumInfoHelper asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IAlbumInfoHelper)) {
                return null;
            }
            return (IAlbumInfoHelper) wrapper;
        }
    }

    public enum AlbumKind {
        SIGLE_VIDEO,
        SIGLE_SERIES,
        SIGLE_UNIT,
        SERIES_ALBUM,
        SOURCE_ALBUM
    }

    public enum HistoryJumpKind {
        HISTORY_PLAY,
        HISTORY_DETAILS
    }

    public enum JumpKind {
        PLAY,
        DETAILS,
        PLAY_LIST
    }

    AlbumKind getAlbumType(Album album);

    HistoryJumpKind getHistoryJumpKind(Album album);

    JumpKind getJumpType(Album album);

    boolean isSingleType(Album album);

    boolean isSingleType1(Album album);

    boolean isSingleType2(Album album);

    boolean isSingleType3(Album album);
}
