package com.gala.sdk.utils.job;

public interface JobExecutor<DataType> {
    void submit(JobController jobController, Job<DataType> job);
}
