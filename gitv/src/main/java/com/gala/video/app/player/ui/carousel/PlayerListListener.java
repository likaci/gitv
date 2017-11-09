package com.gala.video.app.player.ui.carousel;

import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;

public interface PlayerListListener {
    void onItemClick(ViewHolder viewHolder, int i);

    void onItemFocusChanged(ViewHolder viewHolder, boolean z, TVChannelCarouselTag tVChannelCarouselTag, int i);

    void onItemRecycled(ViewHolder viewHolder);

    void onListShow(TVChannelCarouselTag tVChannelCarouselTag, int i, boolean z);
}
