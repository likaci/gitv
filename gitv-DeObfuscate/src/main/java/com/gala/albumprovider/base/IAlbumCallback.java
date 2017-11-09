package com.gala.albumprovider.base;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import java.util.List;

public interface IAlbumCallback {
    void onFailure(int i, ApiException apiException);

    void onSuccess(int i, List<Album> list);
}
