package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ChannelTable;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultChannelTable extends ApiResult {
    public List<ChannelTable> data = null;
    public String timestamp = "";

    public void setData(List<ChannelTable> table) {
        this.data = table;
    }

    public List<ChannelTable> getData() {
        return this.data;
    }
}
