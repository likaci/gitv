package com.gala.tvapi.tv2.model;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class TVChannelCarousel extends Model {
    private static final long serialVersionUID = 1;
    public String icon;
    public long id;
    public String name;
    public int ps;
    public long sid;
    public int type;

    public String toString() {
        return "TVChannelCarousel [hashcode=" + super.hashCode() + ", id=" + this.id + ", name=" + this.name + ", sid=" + this.sid + ", icon=" + this.icon + ", type=" + this.type + ", ps=" + this.ps + AlbumEnterFactory.SIGN_STR;
    }
}
