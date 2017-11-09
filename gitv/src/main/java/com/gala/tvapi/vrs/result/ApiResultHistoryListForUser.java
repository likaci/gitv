package com.gala.tvapi.vrs.result;

import com.gala.tvapi.b.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.VipInfo;
import com.gala.tvapi.vrs.model.HistoryAlbum;
import com.gala.tvapi.vrs.model.HistoryAlbumForUser;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;

public class ApiResultHistoryListForUser extends ApiResult {
    private List<Album> a = null;
    public HistoryAlbumForUser data = null;

    public void setData(HistoryAlbumForUser album) {
        this.data = album;
    }

    public HistoryAlbumForUser getHistoryAlbumForUser() {
        return this.data;
    }

    public List<Album> getAlbumList() {
        this.a = new ArrayList();
        if (!(this.data == null || this.data.getRecords() == null)) {
            for (HistoryAlbum historyAlbum : this.data.getRecords()) {
                int i;
                Album album = new Album();
                album.name = historyAlbum.sourceId.equals("0") ? historyAlbum.albumName : historyAlbum.sourceName;
                album.pic = historyAlbum.albumImageUrl;
                album.len = historyAlbum.videoDuration;
                album.tvsets = historyAlbum.allSets;
                album.is3D = historyAlbum.is3D;
                album.isSeries = historyAlbum.isSeries;
                album.tvQid = historyAlbum.tvIdQipu;
                album.vid = historyAlbum.videoId;
                if (historyAlbum.charge == 2) {
                    i = 1;
                } else {
                    i = 0;
                }
                album.isPurchase = i;
                album.qpId = historyAlbum.albumIdQipu;
                album.chnId = historyAlbum.channelId;
                album.tvPic = historyAlbum.postImage;
                album.exclusive = historyAlbum.exclusive;
                if (historyAlbum.is1080P == 1) {
                    if (album.stream.length() == 0) {
                        album.stream += "1080P";
                    } else {
                        album.stream += ",1080P";
                    }
                }
                album.order = historyAlbum.videoOrder;
                if (!a.a(historyAlbum.videoPlayTime)) {
                    try {
                        album.playTime = Integer.valueOf(historyAlbum.videoPlayTime).intValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                album.sourceCode = historyAlbum.sourceId;
                album.tvName = historyAlbum.videoName;
                album.addTime = historyAlbum.addtime;
                if (historyAlbum.charge == 2 && historyAlbum.purType == 2) {
                    i = 1;
                } else {
                    i = 0;
                }
                album.indiviDemand = i;
                VipInfo vipInfo = new VipInfo();
                if (album.type == 1) {
                    if (historyAlbum.charge == 2 && historyAlbum.purType == 1) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.isVip = i;
                    if (historyAlbum.charge == 2 && historyAlbum.purType == 2) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.isTvod = i;
                } else {
                    if (historyAlbum.charge == 2 && historyAlbum.purType == 1) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.epIsVip = i;
                    if (historyAlbum.charge == 2 && historyAlbum.purType == 2) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    vipInfo.epIsTvod = i;
                }
                album.vipInfo = vipInfo;
                album.payMarkType = TVApiTool.getPayMarkType(historyAlbum.payMark);
                album.time = historyAlbum.tvYear;
                album.drm = TVApiTool.getDrmType(historyAlbum.drmType);
                album.dynamicRanges = historyAlbum.dynamicRanges;
                album.contentType = historyAlbum.contentType;
                this.a.add(album);
            }
        }
        return this.a;
    }
}
