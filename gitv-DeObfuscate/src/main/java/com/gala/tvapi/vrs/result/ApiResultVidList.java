package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.QisuAlbum;
import com.gala.tvapi.vrs.model.QisuLanguage;
import com.gala.tvapi.vrs.model.QisuVid;
import com.gala.tvapi.vrs.model.QisuVideo;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultVidList extends ApiResult {
    public QisuAlbum album = null;

    public List<QisuVid> getVidList() {
        if (this.album == null || this.album.video == null || this.album.video.size() <= 0 || ((QisuVideo) this.album.video.get(0)).language_list == null || ((QisuVideo) this.album.video.get(0)).language_list.size() <= 0 || ((QisuLanguage) ((QisuVideo) this.album.video.get(0)).language_list.get(0)).vid_list == null || ((QisuLanguage) ((QisuVideo) this.album.video.get(0)).language_list.get(0)).vid_list.size() <= 0) {
            return new ArrayList();
        }
        return ((QisuLanguage) ((QisuVideo) this.album.video.get(0)).language_list.get(0)).vid_list;
    }
}
