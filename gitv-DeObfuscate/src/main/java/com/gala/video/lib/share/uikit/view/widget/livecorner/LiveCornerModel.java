package com.gala.video.lib.share.uikit.view.widget.livecorner;

import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class LiveCornerModel {
    public long endTime;
    public LivePlayingType livePlayingType;

    public String toString() {
        return "LiveCornerRefreshModel [livePlayingType=" + this.livePlayingType + ", endTime=" + this.endTime + AlbumEnterFactory.SIGN_STR;
    }
}
