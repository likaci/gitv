package com.gala.sdk.utils.job;

public interface JobListener<JobType extends Job<?>> {
    void onJobDone(JobType jobType);
}
