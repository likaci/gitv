package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Trailer;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultTrailersList extends ApiResult {
    public List<Trailer> data;
    public int total = 0;

    public List<Album> getAlbumList() {
        if (this.data == null || this.data.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (Trailer trailer : this.data) {
            Album album = new Album();
            album.albumFrom = AlbumFrom.TRAILERS;
            album.qpId = trailer.tvQid;
            album.tvQid = trailer.tvQid;
            album.epProbation = trailer.epProbation;
            album.vid = trailer.vid;
            album.isFlower = trailer.type == 1 ? 0 : 1;
            album.chnId = trailer.chnId;
            album.contentType = trailer.contentType;
            album.name = trailer.name;
            album.initIssueTime = trailer.year;
            album.time = trailer.year;
            album.focus = trailer.focus;
            album.len = trailer.len;
            album.order = trailer.order;
            album.pic = trailer.pic;
            album.cast = trailer.cast;
            album.shortName = trailer.shortName;
            album.desc = trailer.desc;
            album.isPurchase = trailer.tvIsPurchase;
            album.vipInfo = trailer.vipInfo;
            album.tvName = trailer.name;
            arrayList.add(album);
        }
        return arrayList;
    }
}
