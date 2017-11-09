package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.IChannelTable;
import com.gala.video.api.ApiResult;

public class ApiResultIChannelTable extends ApiResult {
    public IChannelTable data = null;
    public String timestamp = "";

    public void setData(IChannelTable table) {
        this.data = table;
    }

    public IChannelTable getIChannelTable() {
        return this.data;
    }
}
