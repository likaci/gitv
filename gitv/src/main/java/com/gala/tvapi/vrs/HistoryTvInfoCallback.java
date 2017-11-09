package com.gala.tvapi.vrs;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.result.ApiResultHistoryTvInfo;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;

public class HistoryTvInfoCallback implements IVrsCallback {
    private IVrsCallback a = null;
    private String f544a = null;

    public HistoryTvInfoCallback(IVrsCallback callback, String vrsTvId) {
        this.a = callback;
        this.f544a = vrsTvId;
    }

    public void onSuccess(ApiResult result) {
        ApiResultHistoryTvInfo apiResultHistoryTvInfo = (ApiResultHistoryTvInfo) result;
        Album album = apiResultHistoryTvInfo.getAlbum();
        album.tvQid = this.f544a;
        apiResultHistoryTvInfo.setAlbum(album);
        this.a.onSuccess(apiResultHistoryTvInfo);
    }

    public void onException(ApiException e) {
        this.a.onException(e);
    }
}
