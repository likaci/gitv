package com.gala.video.lib.share.common.model.player;

import com.gala.tvapi.vrs.model.ChannelCarousel;

public class CarouselPlayParamBuilder extends AbsPlayParamBuilder {
    public ChannelCarousel mChannelCarousel;

    public CarouselPlayParamBuilder setChannel(ChannelCarousel channel) {
        this.mChannelCarousel = channel;
        return this;
    }
}
