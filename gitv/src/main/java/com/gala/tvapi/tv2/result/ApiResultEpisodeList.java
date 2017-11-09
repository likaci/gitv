package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultEpisodeList extends ApiResult {
    public List<Episode> data;
    public int total = 0;

    public List<Album> getAlbumList() {
        if (this.data == null || this.data.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (Episode episode : this.data) {
            Album album = new Album();
            album.qpId = episode.tvQid;
            album.tvQid = episode.tvQid;
            album.epProbation = episode.epProbation;
            album.vid = episode.vid;
            album.isFlower = episode.type == 1 ? 0 : 1;
            album.contentType = episode.type == 1 ? 1 : 3;
            album.name = episode.name;
            album.initIssueTime = episode.year;
            album.time = episode.year;
            album.focus = episode.focus;
            album.len = episode.len;
            album.order = episode.order;
            album.pic = episode.pic;
            album.cast = episode.cast;
            album.shortName = episode.shortName;
            album.desc = episode.desc;
            album.isPurchase = episode.tvIsPurchase;
            album.vipInfo = episode.vipInfo;
            album.tvName = episode.name;
            arrayList.add(album);
        }
        return arrayList;
    }
}
