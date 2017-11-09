package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultChannelPlayList extends ApiResult {
    public int count;
    public List<ChannelPlayListLabel> data;

    public void setData(List<ChannelPlayListLabel> list) {
        this.data = list;
    }

    public List<ChannelPlayListLabel> getPlayListLabels() {
        return this.data;
    }
}
