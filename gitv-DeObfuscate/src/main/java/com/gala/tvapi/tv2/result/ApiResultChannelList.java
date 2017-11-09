package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultChannelList extends ApiResult {
    public List<Channel> data;
    public int total = 0;
}
