package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.HistoryAlbum;
import com.gala.video.api.ApiResult;

public class ApiResultHistoryTvInfo extends ApiResult {
    public HistoryAlbum data = null;
    public Album info = null;

    public void setData(HistoryAlbum album) {
        this.data = album;
    }

    public HistoryAlbum getHistoryAlbum() {
        return this.data;
    }

    public Album getAlbum() {
        if (this.data != null && this.info == null) {
            String str;
            this.info = new Album();
            Album album = this.info;
            if (this.data.sourceId.equals("0")) {
                str = this.data.albumName;
            } else {
                str = this.data.sourceName;
            }
            album.name = str;
            this.info.pic = this.data.videoImageUrl;
            this.info.len = this.data.videoDuration;
            this.info.tvsets = this.data.allSets;
            this.info.is3D = this.data.is3D;
            this.info.isSeries = this.data.isSeries;
            this.info.tvQid = this.data.tvIdQipu;
            this.info.vid = this.data.videoId;
            this.info.qpId = this.data.albumIdQipu;
            this.info.chnId = this.data.channelId;
            this.info.tvPic = this.data.postImage;
            this.info.exclusive = this.data.exclusive;
            this.info.isPurchase = this.data.charge == 2 ? 1 : 0;
            if (this.data.is1080P == 1) {
                StringBuilder stringBuilder;
                Album album2;
                if (this.info.stream.length() == 0) {
                    stringBuilder = new StringBuilder();
                    album2 = this.info;
                    album2.stream = stringBuilder.append(album2.stream).append("1080P").toString();
                } else {
                    stringBuilder = new StringBuilder();
                    album2 = this.info;
                    album2.stream = stringBuilder.append(album2.stream).append(",1080P").toString();
                }
            }
            this.info.order = this.data.videoOrder;
            try {
                this.info.playTime = Integer.valueOf(this.data.videoPlayTime).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.info.sourceCode = this.data.sourceId;
            this.info.type = 0;
            this.info.tvName = this.data.albumName;
            this.info.payMarkType = TVApiTool.getPayMarkType(this.data.payMark);
            this.info.time = this.data.tvYear;
            this.info.drm = TVApiTool.getDrmType(this.data.drmType);
            this.info.contentType = this.data.contentType;
        }
        return this.info;
    }

    public void setAlbum(Album album) {
        this.info = album;
    }
}
