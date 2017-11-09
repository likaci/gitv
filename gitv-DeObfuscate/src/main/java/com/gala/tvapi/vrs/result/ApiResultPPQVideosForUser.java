package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.PPQVideoData;
import com.gala.tvapi.vrs.model.UGCVideo;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultPPQVideosForUser extends ApiResult {
    public PPQVideoData data;

    public boolean isHasNextPageData() {
        if (this.data == null || this.data.next_cursor == -1) {
            return false;
        }
        return true;
    }

    public List<Album> getAlbumList() {
        if (this.data == null || this.data.videos == null || this.data.videos.size() <= 0) {
            return null;
        }
        List<Album> arrayList = new ArrayList();
        for (UGCVideo uGCVideo : this.data.videos) {
            Album album = new Album();
            album.len = uGCVideo.duration;
            album.tvPic = uGCVideo.image_url;
            album.pic = uGCVideo.image_url;
            album.name = uGCVideo.content;
            album.tvQid = uGCVideo.tv_id;
            album.qpId = uGCVideo.tv_id;
            album.type = 0;
            album.isSeries = 0;
            arrayList.add(album);
        }
        return arrayList;
    }
}
