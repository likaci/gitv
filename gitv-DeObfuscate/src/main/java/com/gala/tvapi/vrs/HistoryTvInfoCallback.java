package com.gala.tvapi.vrs;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.result.ApiResultHistoryTvInfo;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;

public class HistoryTvInfoCallback implements IVrsCallback {
    private IVrsCallback f1198a = null;
    private String f1199a = null;

    public HistoryTvInfoCallback(IVrsCallback callback, String vrsTvId) {
        this.f1198a = callback;
        this.f1199a = vrsTvId;
    }

    public void onSuccess(ApiResult result) {
        ApiResultHistoryTvInfo apiResultHistoryTvInfo = (ApiResultHistoryTvInfo) result;
        Album album = apiResultHistoryTvInfo.getAlbum();
        album.tvQid = this.f1199a;
        apiResultHistoryTvInfo.setAlbum(album);
        this.f1198a.onSuccess(apiResultHistoryTvInfo);
    }

    public void onException(ApiException e) {
        this.f1198a.onException(e);
    }
}
