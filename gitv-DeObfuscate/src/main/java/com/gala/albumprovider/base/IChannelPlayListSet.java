package com.gala.albumprovider.base;

import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultChannelPlayList;

public interface IChannelPlayListSet {
    void loadDataAsync(int i, int i2, IVrsCallback<ApiResultChannelPlayList> iVrsCallback);
}
