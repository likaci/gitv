package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.LocationContent;
import com.gala.video.api.ApiResult;

public class ApiResultLocation extends ApiResult {
    public String address = "";
    public LocationContent content;
    public int status = 0;
}
