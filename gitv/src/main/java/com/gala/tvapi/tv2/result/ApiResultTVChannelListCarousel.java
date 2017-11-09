package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultTVChannelListCarousel extends ApiResult {
    public List<TVChannelCarousel> data;
    public long t;
    public List<TVChannelCarouselTag> tags;
}
