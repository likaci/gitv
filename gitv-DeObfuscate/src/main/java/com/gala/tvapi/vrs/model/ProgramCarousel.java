package com.gala.tvapi.vrs.model;

import com.gala.tvapi.tv2.model.Album;
import java.util.List;

public class ProgramCarousel extends Model {
    private static final long serialVersionUID = 1;
    public List<Long> channelIds;
    public String endTime;
    public String id;
    public String imageUrl;
    public String name;
    public String startTime;
    public Video videoInfo;

    public boolean isAlbum() {
        if (this.videoInfo == null || this.videoInfo.videoId == null || this.videoInfo.videoId.isEmpty()) {
            return false;
        }
        return true;
    }

    public Album getAlbum() {
        Album album;
        if (this.videoInfo == null || this.videoInfo.videoId == null || this.videoInfo.videoId.isEmpty()) {
            album = new Album();
            if (this.channelIds != null && this.channelIds.size() > 0) {
                album.live_channelId = String.valueOf(this.channelIds.get(0));
            }
            album.isLive = 1;
            album.name = this.name;
            album.type = 0;
            album.sliveTime = this.startTime;
            album.eliveTime = this.endTime;
            album.qpId = this.id;
            album.tvQid = this.id;
            album.program_id = this.id;
            return album;
        }
        album = new Album();
        album.name = this.name;
        album.pic = this.videoInfo.videoImageUrl;
        album.tvPic = this.videoInfo.videoImageUrl;
        album.tvQid = this.videoInfo.videoId;
        album.isSeries = this.videoInfo.isSeries;
        album.qpId = this.videoInfo.albumId;
        album.sourceCode = this.videoInfo.sourceId;
        album.chnId = this.videoInfo.videoChannelId;
        album.sliveTime = this.startTime;
        album.eliveTime = this.endTime;
        album.type = 0;
        album.isLive = 0;
        album.program_id = this.id;
        album.order = this.videoInfo.order;
        return album;
    }
}
