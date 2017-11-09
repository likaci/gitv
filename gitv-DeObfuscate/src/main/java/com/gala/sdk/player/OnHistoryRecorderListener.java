package com.gala.sdk.player;

import com.gala.tvapi.tv2.model.Album;

public interface OnHistoryRecorderListener {
    void onAddLocalCarouselPlayRecord(String str, String str2, String str3, long j, long j2);

    void onWatchTrackAddPlayRecord(Album album, boolean z);
}
