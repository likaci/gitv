package com.gala.tvapi.tv2.result;

import com.gala.tvapi.p023c.C0257e;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultAlbumList extends ApiResult {
    public String bkt = "";
    public List<ChnList> chnList = null;
    public List<Album> data = null;
    public String docs = "";
    public String eventId = "";
    public int limit = 0;
    public String qisoURL = "";
    public String qisost = "";
    public int start = 0;
    public String time = "";
    public int total = 0;
    public String url = "";

    public List<Album> getAlbumList() {
        List<Album> arrayList = new ArrayList();
        if (this.data != null && this.data.size() > 0) {
            String url = getUrl();
            for (Album album : this.data) {
                if (!(album == null || album.getType() == AlbumType.OFFLINE)) {
                    album.bkt = this.bkt;
                    album.searchtime = this.time;
                    album.docs = this.docs;
                    album.url = this.url;
                    album.qisost = this.qisost;
                    album.eventId = this.eventId;
                    album.qisoURL = url;
                    arrayList.add(album);
                }
            }
        }
        return arrayList;
    }

    public String getUrl() {
        if (this.qisoURL == null || this.qisoURL.equals("")) {
            return "";
        }
        C0257e c0257e = new C0257e(this.qisoURL);
        return c0257e.m624a(Album.KEY) + "&" + c0257e.m624a(Album.PAGE_SIZE) + "&" + c0257e.m624a(Album.PAGE_NUM) + "&" + c0257e.m624a(Album.CTG_NAME) + "&" + c0257e.m624a("site");
    }
}
