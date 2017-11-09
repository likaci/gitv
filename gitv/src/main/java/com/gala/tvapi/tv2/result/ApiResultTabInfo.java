package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.TabInfo;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultTabInfo extends ApiResult {
    public String code;
    public List<TabInfo> data;

    public List<TabInfo> getData() {
        return this.data;
    }
}
