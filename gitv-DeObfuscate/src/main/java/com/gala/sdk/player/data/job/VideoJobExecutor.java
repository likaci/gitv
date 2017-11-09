package com.gala.sdk.player.data.job;

import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.utils.job.Job;
import com.gala.sdk.utils.job.JobController;
import com.gala.sdk.utils.job.JobExecutor;

public class VideoJobExecutor implements JobExecutor<IVideo> {
    public void submit(JobController controller, Job<IVideo> job) {
        if (job != null) {
            job.run(controller);
        }
    }
}
