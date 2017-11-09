package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ShortenUrl;
import com.gala.video.api.ApiResult;

public class ApiResultShorten extends ApiResult {
    public String code;
    public ShortenUrl data;
    public String message;
}
