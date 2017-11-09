package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class PlayListQipuCheck extends CheckTask {
    private static final String TAG = "PlayListCheck";
    private String isFree;
    private boolean isSuccess;

    public PlayListQipuCheck(CheckEntity checkEntity) {
        super(checkEntity);
        this.isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
    }

    public boolean runCheck() {
        final long startTime = System.currentTimeMillis();
        VrsHelper.playListQipu.callSync(new IVrsCallback<ApiResultPlayListQipu>() {
            public void onException(ApiException e) {
                LogUtils.m1571e(PlayListQipuCheck.TAG, "fetchePlayList onException code = " + e.getCode());
                PlayListQipuCheck.this.mCheckEntity.add("PlayListQipuCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                PlayListQipuCheck.this.isSuccess = false;
            }

            public void onSuccess(ApiResultPlayListQipu apiResultPlayListQipu) {
                LogUtils.m1568d(PlayListQipuCheck.TAG, "fetche PlayList onSuccess albumInfos");
                PlayListQipuCheck.this.mCheckEntity.add("PlayListQipuCheck apiResultPlayListQipu success , use time:" + (System.currentTimeMillis() - startTime) + ", result =  " + apiResultPlayListQipu.code);
                PlayListQipuCheck.this.isSuccess = true;
            }
        }, this.mCheckEntity.getQipu(), this.isFree);
        return this.isSuccess;
    }
}
