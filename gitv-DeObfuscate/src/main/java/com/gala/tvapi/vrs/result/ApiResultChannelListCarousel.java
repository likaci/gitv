package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultChannelListCarousel extends ApiResult {
    public List<ChannelCarousel> channels;
    public int hasNext = 0;

    public List<ChannelCarousel> getCarouselChannelList() {
        return this.channels;
    }

    public boolean isHasNextPage() {
        return this.hasNext == 1;
    }
}
