package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.albumdetail.data.task.AlbumInfoCacheTask;
import com.gala.video.app.player.albumdetail.data.task.AlbumInfoCacheTask.IAlbumInfoCacheTaskListener;
import com.gala.video.app.player.data.DetailDataCacheManager;
import com.gala.video.app.player.utils.debug.DetailDebugHelper;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class FetchDetailInfoJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchDetailInfoJob";
    AlbumInfo albumInfo;
    IAlbumInfoCacheTaskListener albumInfoCacheTaskListener = new IAlbumInfoCacheTaskListener() {
        public void onSuccess(Album album) {
            LogUtils.d(FetchDetailInfoJob.TAG, " onRun onSuccess");
            ApiException fakeException = DetailDebugHelper.checkForSimulatedDataError();
            if (fakeException != null) {
                FetchDetailInfoJob.this.notifyJobFail(FetchDetailInfoJob.this.mController, new JobError(fakeException.getCode(), fakeException));
                return;
            }
            album.tvQid = FetchDetailInfoJob.this.mTvQid;
            FetchDetailInfoJob.this.albumInfo.setAlbum(album);
            FetchDetailInfoJob.this.albumInfo.setAlbumDetailPic(album.pic);
            FetchDetailInfoJob.this.albumInfo.setPlayOrder(FetchDetailInfoJob.this.mPlayOrder);
            if (LogUtils.mIsDebug) {
                LogUtils.d(FetchDetailInfoJob.TAG, "task.getAlbum() = " + FetchDetailInfoJob.this.albumInfo);
            }
            DetailDataCacheManager.instance().saveCurrentDetailInfo(album);
            FetchDetailInfoJob.this.notifyJobSuccess(FetchDetailInfoJob.this.mController);
        }

        public void onFailed(ApiException e) {
            LogUtils.d(FetchDetailInfoJob.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
            FetchDetailInfoJob.this.notifyJobFail(FetchDetailInfoJob.this.mController, new JobError(e.getCode(), e));
        }
    };
    JobController mController;
    int mPlayOrder;
    String mTvQid;

    public FetchDetailInfoJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(JobController controller) {
        this.albumInfo = (AlbumInfo) getData();
        this.mController = controller;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onRun, albumId=" + this.albumInfo.getAlbumId());
        }
        AlbumInfoCacheTask task = AlbumInfoCacheTask.getInstance(this.albumInfo.getAlbumId());
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> task =" + task);
        }
        this.mTvQid = this.albumInfo.getTvId();
        this.mPlayOrder = this.albumInfo.getPlayOrder();
        task.getAlbum(this.albumInfoCacheTaskListener);
    }
}
