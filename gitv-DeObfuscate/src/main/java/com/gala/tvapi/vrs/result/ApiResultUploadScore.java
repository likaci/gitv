package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.UploadScore;
import com.gala.video.api.ApiResult;

public class ApiResultUploadScore extends ApiResult {
    public UploadScore data;

    public UploadScore getUploadScore() {
        return this.data;
    }
}
