package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.model.ResId;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class PlayListChannelCheck extends CheckTask {
    private static final String TAG = "PlayListChannelCheck";
    private IVrsCallback<ApiResultChannelLabels> iVrsCallback = new C16951();
    String isFree;
    private boolean isSuccess;
    private long startTime;

    class C16951 implements IVrsCallback<ApiResultChannelLabels> {
        C16951() {
        }

        public void onSuccess(ApiResultChannelLabels channelLabels) {
            PlayListChannelCheck.this.mCheckEntity.setQipu(((ChannelLabel) channelLabels.getChannelLabels().getChannelLabelList().get(0)).getResourceItem().plId);
            LogUtils.m1568d(PlayListChannelCheck.TAG, "PlayListChannelCheck onSuccess()" + channelLabels.data);
            PlayListChannelCheck.this.mCheckEntity.add("PlayListChannelCheck result success  , use time:" + (System.currentTimeMillis() - PlayListChannelCheck.this.startTime) + ", result = " + channelLabels.data);
            PlayListChannelCheck.this.isSuccess = true;
        }

        public void onException(ApiException e) {
            LogUtils.m1568d(PlayListChannelCheck.TAG, "---PlayListChannelCheck onException---apicode=" + e.getCode() + "httpcode=" + e.getHttpCode());
            PlayListChannelCheck.this.mCheckEntity.add("---PlayListChannelCheck onException---apicode=" + e.getCode() + "httpcode=" + e.getHttpCode() + ", url=" + e.getUrl());
            PlayListChannelCheck.this.isSuccess = false;
        }
    }

    public PlayListChannelCheck(CheckEntity checkEntity) {
        super(checkEntity);
        this.isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
    }

    public boolean runCheck() {
        this.startTime = System.currentTimeMillis();
        VrsHelper.channelLabelsFilter.callSync(this.iVrsCallback, ((ResId) CreateInterfaceTools.createDeviceCheckProxy().getHomeResId().get(2)).id, "2", this.isFree + "");
        return this.isSuccess;
    }
}
