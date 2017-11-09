package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.GroupDetail;
import com.gala.video.api.ApiResult;

public class ApiResultGroupDetail extends ApiResult {
    public GroupDetail data;
    public boolean latest = false;
    public String timestamp;
}
