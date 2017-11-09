package com.gala.video.lib.framework.core.job;

public interface JobExecutor<DataType> {
    void submit(JobController jobController, Job<DataType> job);
}
