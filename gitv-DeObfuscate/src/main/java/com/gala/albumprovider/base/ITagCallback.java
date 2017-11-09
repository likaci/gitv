package com.gala.albumprovider.base;

import com.gala.albumprovider.model.Tag;
import com.gala.video.api.ApiException;
import java.util.List;

public interface ITagCallback {
    void onFailure(ApiException apiException);

    void onSuccess(List<Tag> list);
}
