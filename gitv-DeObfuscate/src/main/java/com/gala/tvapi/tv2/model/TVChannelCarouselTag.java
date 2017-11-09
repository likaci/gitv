package com.gala.tvapi.tv2.model;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.List;

public class TVChannelCarouselTag extends Model {
    private static final long serialVersionUID = 1;
    public List<String> cid;
    public String name;

    public String toString() {
        return "TVChannelCarouselTag [hashcode=" + super.hashCode() + ", name=" + this.name + ", cid.size=" + this.cid.size() + AlbumEnterFactory.SIGN_STR;
    }
}
