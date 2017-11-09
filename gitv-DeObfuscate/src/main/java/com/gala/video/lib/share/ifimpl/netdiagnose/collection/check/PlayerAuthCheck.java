package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PlayerAuthCheck extends CheckTask {
    private static final String TAG = "PlayAutoCheck";
    private String mTvid = this.mCheckEntity.getAlbum().tvQid;

    public PlayerAuthCheck(CheckEntity checkEntity) {
        super(checkEntity);
    }

    public boolean runCheck() {
        final long startTime = System.currentTimeMillis();
        TVApi.playCheck.callSync(new IApiCallback<ApiResultCode>() {
            public void onSuccess(ApiResultCode apiResult) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(PlayerAuthCheck.TAG, "PlayerAuthCheck onSuccess(" + apiResult.code + ")");
                }
                PlayerAuthCheck.this.mCheckEntity.add("PlayerAuthCheck result success , use time:" + (System.currentTimeMillis() - startTime) + ", result = " + apiResult.code);
            }

            public void onException(ApiException e) {
                LogUtils.m1568d(PlayerAuthCheck.TAG, "PlayerAuthCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                PlayerAuthCheck.this.mCheckEntity.add("PlayerAuthCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage() + ", url=" + e.getUrl());
            }
        }, this.mTvid);
        return true;
    }
}
