package com.gala.tvapi.tv2.model;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class TVProgramCarousel extends Model {
    private static final long serialVersionUID = 1;
    public long beAid;
    public String beAname;
    public int beCid;
    public int beOrder;
    public long beQid;
    public long beSid;
    public int bea;
    public long bt;
    public long et;
    public String icon;
    public long id;
    public String name;

    public Album getAlbum() {
        Album album = new Album();
        album.type = 0;
        album.sliveTime = String.valueOf(this.bt);
        album.eliveTime = String.valueOf(this.et);
        album.name = this.name;
        album.type = 0;
        album.program_id = String.valueOf(this.id);
        if (this.beQid != 0) {
            album.tvQid = String.valueOf(this.beQid);
            album.isSeries = this.bea;
            album.qpId = this.bea == 1 ? String.valueOf(this.beAid) : String.valueOf(this.beQid);
            album.sourceCode = String.valueOf(this.beSid);
            album.chnId = this.beCid;
            album.isLive = 0;
            album.order = this.beOrder;
        } else {
            album.live_channelId = String.valueOf(this.id);
            album.isLive = 1;
            album.qpId = String.valueOf(this.id);
            album.tvQid = String.valueOf(this.id);
        }
        return album;
    }

    public String toString() {
        return "TVProgramCarousel [hashcode=" + super.hashCode() + ", id=" + this.id + ", name=" + this.name + ", bt=" + this.bt + ", et=" + this.et + ", beQid=" + this.beQid + ", bea=" + this.bea + ", beAid=" + this.beAid + ", beAname=" + this.beAname + ", beSid=" + this.beSid + ", beCid=" + this.beCid + ", beOrder=" + this.beOrder + ", icon=" + this.icon + AlbumEnterFactory.SIGN_STR;
    }
}
