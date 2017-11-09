package com.gala.tvapi.vrs.model;

import java.util.List;

public class CollectListData extends Model {
    private static final long serialVersionUID = 1;
    public int cnt = 0;
    private List<CollectAlbum> data = null;
    public int qidan_cnt = 0;

    public void setData(List<CollectAlbum> albums) {
        this.data = albums;
    }

    public List<CollectAlbum> getCollectAlbums() {
        return this.data;
    }
}
