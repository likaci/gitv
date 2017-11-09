package com.gala.video.app.player.albumdetail.data.loader;

import android.content.Context;
import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.albumdetail.data.job.AlbumJob;
import com.gala.video.app.player.controller.DataDispatcher;
import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerImpl;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class DetailAlbumLoader {
    private static final String TAG = "Detail/Data/DetailAlbumLoader";
    private AlbumInfo mAlbumInfo;
    private Context mContext;
    private WeakReference<Context> mContextRef;
    private JobController mController = new JobControllerImpl((Context) this.mContextRef.get());
    ScheduledThreadPoolExecutor mExecutor;

    public enum LoadType {
        FULLLOAD_QUICK,
        FULLLOAD_NORMAL,
        NO_CREATE_PLAYER,
        RESUME_LOAD,
        SWITCH_LOAD,
        TOTAL_SWITCH_LOAD
    }

    public static class MyVideoJobListener extends BaseWeakListener<DetailAlbumLoader> implements AlbumJobListener {
        private final int mMsg;

        public MyVideoJobListener(DetailAlbumLoader outer, int msg) {
            super(outer);
            this.mMsg = msg;
        }

        public void onJobDone(Job<AlbumInfo> job) {
            DetailAlbumLoader outer = (DetailAlbumLoader) get();
            if (outer != null) {
                if (job.getState() != 2) {
                    LogRecordUtils.loge(DetailAlbumLoader.TAG, ">> onJobFail, job name=" + job.getName() + ", job state=" + job.getState());
                } else if (17 == this.mMsg && ListUtils.isEmpty(outer.getInfo().getEpisodeVideos())) {
                    LogRecordUtils.logd(DetailAlbumLoader.TAG, "onJobDone, mMsg is DATA_MSG_EPISODE_CACHED_READY, cached episodes is empty.");
                } else {
                    DataDispatcher.instance().postOnMainThread(outer.getContext(), this.mMsg, outer.getInfo());
                }
            }
        }
    }

    public abstract void dataLoad(LoadType loadType);

    protected Context getContext() {
        return this.mContext;
    }

    public void cancelJobLoad() {
        if (this.mController != null) {
            this.mController.cancel();
        }
    }

    public void setExecutor(ScheduledThreadPoolExecutor executor) {
        this.mExecutor = executor;
    }

    public DetailAlbumLoader(Context context, AlbumInfo albumInfo) {
        this.mContextRef = new WeakReference(context);
        this.mContext = context;
        this.mAlbumInfo = albumInfo;
    }

    public void setAlbum(Album album) {
        if (this.mAlbumInfo != null) {
            this.mAlbumInfo.setAlbum(album);
        }
    }

    public void clearAlbumInfo() {
        this.mAlbumInfo = null;
    }

    protected void submit(AlbumJob header) {
        submit(header, true, 0);
    }

    protected void submit(AlbumJob header, boolean runOnNewThread) {
        submit(header, runOnNewThread, 0);
    }

    private void printJobInfo(AlbumJob job) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "!!@@## printJobInfo, job=" + job);
        }
        List<Job<AlbumInfo>> list = job.getNextJobs();
        if (!ListUtils.isEmpty((List) list)) {
            for (Job<AlbumInfo> each : list) {
                printJobInfo((AlbumJob) each);
            }
        }
    }

    protected void submit(final AlbumJob header, boolean runOnNewThread, final long delay) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> submit, header=" + header + ", runOnNewThread=" + runOnNewThread);
        }
        if (header == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "submit, header is null.");
            }
        } else if (!runOnNewThread) {
            header.run(this.mController);
        } else if (this.mExecutor != null) {
            Log.v(TAG, "mExecutor hascode = " + this.mExecutor.hashCode());
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    header.run(DetailAlbumLoader.this.mController);
                }
            });
        } else {
            Log.v(TAG, "mExecutor hascode is null ");
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    header.run(DetailAlbumLoader.this.mController);
                }
            });
        }
    }

    protected AlbumInfo getInfo() {
        return this.mAlbumInfo;
    }
}
