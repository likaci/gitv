package com.gala.sdk.player.data.job;

import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.utils.job.JobController;

public class EmptyJob extends VideoJob {
    public EmptyJob(IVideo video, VideoJobListener listener) {
        super("Player/EmptyJob", video, listener);
    }

    public void onRun(JobController controller) {
        notifyJobSuccess(controller);
    }
}
