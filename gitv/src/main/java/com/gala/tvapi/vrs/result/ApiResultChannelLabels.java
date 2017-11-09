package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ChannelLabels;
import com.gala.video.api.ApiResult;

public class ApiResultChannelLabels extends ApiResult {
    public ChannelLabels data;

    public void setData(ChannelLabels labels) {
        this.data = labels;
    }

    public ChannelLabels getChannelLabels() {
        return this.data;
    }
}
