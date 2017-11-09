package com.gala.video.lib.framework.core.job;

public interface JobListener<JobType extends Job<?>> {
    void onJobDone(JobType jobType);
}
