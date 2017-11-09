package com.gala.sdk.player.data;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;

public interface IHistoryFetcher {
    Album getAlbumHistory(String str);

    String getLoopPlayAlbumId(Context context, int i);

    int getLoopPlayIndex(Context context, int i);

    int getLoopPlayOffset(Context context, int i);

    Album getTvHistory(String str);
}
