package com.gala.albumprovider.base;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import java.util.List;

public interface IChannelLabelsCallback {
    void onFailure(ApiException apiException);

    void onSuccess(List<ChannelLabel> list);
}
