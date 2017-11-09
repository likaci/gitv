package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class AlbumInfoCheck extends CheckTask {
    private static final String TAG = "AlbumInfoCheck";
    private String mQpid = this.mCheckEntity.getAlbum().qpId;

    public AlbumInfoCheck(CheckEntity checkEntity) {
        super(checkEntity);
    }

    public boolean runCheck() {
        final long startTime = System.currentTimeMillis();
        TVApi.albumInfo.callSync(new IApiCallback<ApiResultAlbum>() {
            public void onSuccess(ApiResultAlbum apiResult) {
                if (apiResult == null || apiResult.data == null) {
                    onException(new ApiException("apiResult is null"));
                    return;
                }
                LogUtils.m1568d(AlbumInfoCheck.TAG, "AlbumInfoCheck onSuccess: fetched info= " + apiResult.data);
                AlbumInfoCheck.this.mCheckEntity.add("AlbumInfoCheck result success , use time:" + (System.currentTimeMillis() - startTime) + ", result = " + apiResult.data);
            }

            public void onException(ApiException e) {
                LogUtils.m1568d(AlbumInfoCheck.TAG, "AlbumInfoCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                AlbumInfoCheck.this.mCheckEntity.add("AlbumInfoCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage() + ", url=" + e.getUrl());
            }
        }, this.mQpid);
        return true;
    }
}
