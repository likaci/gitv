package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.MultiChannelLabels;
import com.gala.video.api.ApiResult;
import java.util.Map;

public class ApiResultMultiChannelLabels extends ApiResult {
    public Map<String, MultiChannelLabels> data;

    public Map<String, MultiChannelLabels> getData() {
        return this.data;
    }

    public void setData(Map<String, MultiChannelLabels> map) {
        this.data = map;
    }
}
