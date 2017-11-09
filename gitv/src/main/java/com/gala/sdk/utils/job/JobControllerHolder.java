package com.gala.sdk.utils.job;

public class JobControllerHolder {
    private final JobController a;

    public JobControllerHolder(JobController controller) {
        this.a = controller;
    }

    public JobController getController() {
        return this.a;
    }
}
