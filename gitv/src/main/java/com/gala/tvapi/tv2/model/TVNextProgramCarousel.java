package com.gala.tvapi.tv2.model;

import java.util.List;

public class TVNextProgramCarousel extends Model {
    private static final long serialVersionUID = 1;
    public String cid;
    public List<TVProgramCarousel> live;

    public Album getAlbum() {
        Album album = new Album();
        if (this.live == null || this.live.size() <= 0) {
            return null;
        }
        TVProgramCarousel tVProgramCarousel = (TVProgramCarousel) this.live.get(0);
        album.live_channelId = this.cid;
        album.isLive = 1;
        album.name = tVProgramCarousel.name;
        album.sliveTime = String.valueOf(tVProgramCarousel.bt);
        album.eliveTime = String.valueOf(tVProgramCarousel.et);
        album.type = 0;
        album.qpId = String.valueOf(tVProgramCarousel.id);
        album.tvQid = String.valueOf(tVProgramCarousel.id);
        album.program_id = String.valueOf(tVProgramCarousel.id);
        return album;
    }
}
