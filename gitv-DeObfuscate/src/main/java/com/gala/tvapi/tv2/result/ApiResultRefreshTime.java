package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.RefreshTime;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultRefreshTime extends ApiResult {
    public String code;
    public List<RefreshTime> data;

    public List<RefreshTime> getData() {
        return this.data;
    }
}
