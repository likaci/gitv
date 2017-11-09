package com.gala.sdk.player.ui;

import com.gala.sdk.player.data.IVideoProvider.AllChannelCallback;
import com.gala.sdk.player.data.IVideoProvider.AllChannelDetailCallback;
import com.gala.sdk.player.data.IVideoProvider.ChannelDetailCallback;
import com.gala.sdk.player.data.IVideoProvider.ProgramListCallback;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;

public interface OnRequestChannelInfoListener {
    void onRequestChannelDetail(TVChannelCarousel tVChannelCarousel, ChannelDetailCallback channelDetailCallback);

    void onRequestFullChannel(TVChannelCarouselTag tVChannelCarouselTag, AllChannelCallback allChannelCallback);

    void onRequestFullChannelDetail(TVChannelCarouselTag tVChannelCarouselTag, AllChannelDetailCallback allChannelDetailCallback);

    void onRequestProgramList(TVChannelCarousel tVChannelCarousel, ProgramListCallback programListCallback);
}
