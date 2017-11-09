package com.gala.tvapi.vrs.model;

import java.util.List;

public class HistoryAlbumForUser extends Model {
    private static final long serialVersionUID = 1;
    public int pageNum = 0;
    public List<HistoryAlbum> records = null;
    public int total = 0;

    public void setRecords(List<HistoryAlbum> albums) {
        this.records = albums;
    }

    public List<HistoryAlbum> getRecords() {
        return this.records;
    }
}
