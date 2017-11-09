package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.albumdetail.data.DetailPageManage;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;

public class FetchGroupDetailJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/Data/FetchGroupDetailJob";
    private DetailPageManage mDetailPageManage;
    private boolean mIsSwitchSourceRefresh;

    public FetchGroupDetailJob(AlbumInfo albumInfo, AlbumJobListener listener, boolean isSwitchSourceRefresh, DetailPageManage detailPageManage) {
        super(TAG, albumInfo, listener);
        this.mDetailPageManage = detailPageManage;
        this.mIsSwitchSourceRefresh = isSwitchSourceRefresh;
    }

    public void onRun(JobController controller) {
        if (!controller.isCancelled()) {
            AlbumInfo albumInfo = (AlbumInfo) getData();
            int channelId = albumInfo.getChannelId();
            LogRecordUtils.logd(TAG, ">> onRun, mIsSwitchSourceRefresh = " + this.mIsSwitchSourceRefresh + " albumId=" + albumInfo.getAlbumId() + ",channelId=" + channelId);
            Channel channel = CreateInterfaceTools.createChannelProviderProxy().getChannelById(channelId);
            if (channel == null) {
                LogRecordUtils.loge(TAG, "resId is null!!");
                return;
            }
            String mResId;
            if (PlayerDebugUtils.testAlbumDetailGroupDetailSpecialData()) {
                mResId = "504958912";
                LogRecordUtils.logd(TAG, "execute debug , mChannelId=" + channelId + ",mResId=" + mResId);
            } else {
                mResId = channel.recResGroupId;
            }
            this.mDetailPageManage.loadData(mResId, this.mIsSwitchSourceRefresh, (AlbumInfo) getData(), controller);
            notifyJobSuccess(controller);
            this.mDetailPageManage.updateDetailDate(mResId);
            LogRecordUtils.logd(TAG, "still run");
        }
    }
}
