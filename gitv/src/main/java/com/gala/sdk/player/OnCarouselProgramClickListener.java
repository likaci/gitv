package com.gala.sdk.player;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;

public interface OnCarouselProgramClickListener {
    void startAlbumDetailActivity(Context context, Album album, String str, int i, String str2, String str3);

    void startPlayActivity(Context context, Album album, String str, String str2, String str3);
}
