package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.SubscribeState;
import com.gala.video.api.ApiResult;
import java.util.Map;

public class ApiResultSubscribeState extends ApiResult {
    public Map<String, SubscribeState> data;
}
