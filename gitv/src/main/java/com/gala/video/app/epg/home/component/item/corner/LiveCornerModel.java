package com.gala.video.app.epg.home.component.item.corner;

import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class LiveCornerModel {
    public long endTime;
    public LivePlayingType livePlayingType;

    public String toString() {
        return "LiveCornerModel [livePlayingType=" + this.livePlayingType + ", endTime=" + this.endTime + AlbumEnterFactory.SIGN_STR;
    }
}
