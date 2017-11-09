package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class RecordActionModel extends BaseActionModel {
    private Album mHistoryInfoAlbum;
    private int mSearchRecordTypeOrdinal;

    public RecordActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public void setHistoryInfoAlbum(Album historyInfoAlbum) {
        this.mHistoryInfoAlbum = historyInfoAlbum;
    }

    public Album getHistoryInfoAlbum() {
        return this.mHistoryInfoAlbum;
    }

    public void setSearchRecordType(int searchRecordTypeOrdinal) {
        this.mSearchRecordTypeOrdinal = searchRecordTypeOrdinal;
    }

    public int getSearchRecordType() {
        return this.mSearchRecordTypeOrdinal;
    }
}
