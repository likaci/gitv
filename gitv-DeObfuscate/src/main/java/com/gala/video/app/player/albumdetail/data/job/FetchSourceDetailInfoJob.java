package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.albumdetail.data.task.FetchSourceCacheTask;
import com.gala.video.app.player.albumdetail.data.task.FetchSourceCacheTask.IFetchSourceCacheTaskListener;
import com.gala.video.app.player.utils.debug.DetailDebugHelper;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class FetchSourceDetailInfoJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchSourceDetailInfoJob";
    IFetchSourceCacheTaskListener fetchSourceCacheTaskListener = new C13041();
    AlbumInfo mAlbumInfo;
    JobController mController;

    class C13041 implements IFetchSourceCacheTaskListener {
        C13041() {
        }

        public void onSuccess(Album album) {
            ApiException fakeException = DetailDebugHelper.checkForSimulatedDataError();
            if (fakeException != null) {
                FetchSourceDetailInfoJob.this.notifyJobFail(FetchSourceDetailInfoJob.this.mController, new JobError(fakeException.getCode(), fakeException));
                return;
            }
            FetchSourceDetailInfoJob.this.mAlbumInfo.getAlbum().tvPic = album.tvPic;
            FetchSourceDetailInfoJob.this.mAlbumInfo.getAlbum().time = album.time;
            FetchSourceDetailInfoJob.this.mAlbumInfo.getAlbum().cast = album.cast;
            FetchSourceDetailInfoJob.this.mAlbumInfo.getAlbum().desc = album.desc;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(FetchSourceDetailInfoJob.TAG, "task.getAlbum() = " + FetchSourceDetailInfoJob.this.mAlbumInfo);
            }
            FetchSourceDetailInfoJob.this.notifyJobSuccess(FetchSourceDetailInfoJob.this.mController);
        }

        public void onFailed(ApiException e) {
            LogUtils.m1568d(FetchSourceDetailInfoJob.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
            FetchSourceDetailInfoJob.this.notifyJobFail(FetchSourceDetailInfoJob.this.mController, new JobError(e.getCode(), e));
        }
    }

    public FetchSourceDetailInfoJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(JobController controller) {
        this.mAlbumInfo = (AlbumInfo) getData();
        this.mController = controller;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> onRun: tvId=" + this.mAlbumInfo.getTvId());
        }
        FetchSourceCacheTask.getInstance(this.mAlbumInfo.getTvId()).getAlbum(this.fetchSourceCacheTaskListener);
    }
}
