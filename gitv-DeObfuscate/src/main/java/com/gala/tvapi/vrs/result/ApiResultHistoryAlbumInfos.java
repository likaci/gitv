package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.HistoryAlbum;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultHistoryAlbumInfos extends ApiResult {
    public List<HistoryAlbum> data = null;
    public List<Album> infos = null;
    public String uid = "";

    public void setData(List<HistoryAlbum> albums) {
        this.data = albums;
    }

    public List<HistoryAlbum> getHistoryAlbum() {
        return this.data;
    }

    public List<Album> getAlbumList() {
        if (this.data != null && this.data.size() > 0 && this.infos == null) {
            this.infos = new ArrayList();
            for (HistoryAlbum historyAlbum : this.data) {
                Album album = new Album();
                album.name = historyAlbum.sourceId.equals("0") ? historyAlbum.albumName : historyAlbum.sourceName;
                album.pic = historyAlbum.videoImageUrl;
                album.len = historyAlbum.videoDuration;
                album.tvsets = historyAlbum.allSets;
                album.is3D = historyAlbum.is3D;
                album.isSeries = historyAlbum.isSeries;
                album.tvQid = historyAlbum.tvIdQipu;
                album.vid = historyAlbum.videoId;
                album.qpId = historyAlbum.albumIdQipu;
                album.chnId = historyAlbum.channelId;
                album.tvPic = historyAlbum.postImage;
                album.exclusive = historyAlbum.exclusive;
                album.isPurchase = historyAlbum.charge == 2 ? 1 : 0;
                if (historyAlbum.is1080P == 1) {
                    if (album.stream.length() == 0) {
                        album.stream += "1080P";
                    } else {
                        album.stream += ",1080P";
                    }
                }
                album.order = historyAlbum.videoOrder;
                try {
                    album.playTime = Integer.valueOf(historyAlbum.videoPlayTime).intValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                album.sourceCode = historyAlbum.sourceId;
                album.type = 0;
                album.tvName = historyAlbum.videoName;
                album.payMarkType = TVApiTool.getPayMarkType(historyAlbum.payMark);
                album.time = historyAlbum.tvYear;
                album.drm = TVApiTool.getDrmType(historyAlbum.drmType);
                album.contentType = historyAlbum.contentType;
                this.infos.add(album);
            }
        }
        return this.infos;
    }

    public void setAlbumInfo(List<Album> albums) {
        this.infos = albums;
    }
}
