package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVNextProgramCarousel;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultTVNextProgramListCarousel extends ApiResult {
    public List<TVNextProgramCarousel> data;
    public long t;

    public Album getAlbumByChannelId(long id) {
        Album album = new Album();
        Album album2 = album;
        for (TVNextProgramCarousel tVNextProgramCarousel : this.data) {
            if (tVNextProgramCarousel == null || tVNextProgramCarousel.cid == null || !tVNextProgramCarousel.cid.equals(String.valueOf(id))) {
                album = album2;
            } else {
                album = tVNextProgramCarousel.getAlbum();
            }
            album2 = album;
        }
        return album2;
    }
}
