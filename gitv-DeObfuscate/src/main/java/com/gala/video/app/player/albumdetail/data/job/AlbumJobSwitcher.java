package com.gala.video.app.player.albumdetail.data.job;

import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class AlbumJobSwitcher extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/VideoJobSwitcher";
    private AlbumJob mDefaultJob;
    private final List<JobHolder> mJobMap = new ArrayList();

    public interface ISwitchCondition {
        boolean checkPass(AlbumInfo albumInfo);
    }

    private class JobHolder {
        ISwitchCondition condition;
        AlbumJob job;

        public JobHolder(ISwitchCondition condition, AlbumJob job) {
            this.condition = condition;
            this.job = job;
        }
    }

    public AlbumJobSwitcher(AlbumInfo video, AlbumJobListener listener) {
        super(TAG, video, listener);
    }

    public void onRun(JobController controller) {
        AlbumJob job = null;
        synchronized (this.mJobMap) {
            for (JobHolder holder : this.mJobMap) {
                if (holder.condition.checkPass((AlbumInfo) getData())) {
                    job = holder.job;
                    break;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "onRun() find job " + job);
            }
            if (job == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "onRun() find null job " + job);
                }
                job = this.mDefaultJob;
            }
        }
        if (job != null) {
            job.run(controller);
        }
    }

    public synchronized void link(ISwitchCondition condition, AlbumJob job) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "link(" + condition + ", " + job + ")");
        }
        if (condition != null) {
            this.mJobMap.add(new JobHolder(condition, job));
        } else {
            this.mDefaultJob = job;
        }
    }

    public void link(Job<AlbumInfo>... jobArr) {
        throw new UnsupportedOperationException("Don't link jobs for they will not be run.");
    }

    public List<Job<AlbumInfo>> getNextJobs() {
        List<Job<AlbumInfo>> jobList = new ArrayList();
        synchronized (this.mJobMap) {
            for (JobHolder holder : this.mJobMap) {
                jobList.add(holder.job);
            }
        }
        return jobList;
    }
}
