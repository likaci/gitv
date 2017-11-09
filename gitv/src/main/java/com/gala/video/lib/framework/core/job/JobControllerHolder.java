package com.gala.video.lib.framework.core.job;

public class JobControllerHolder {
    private final JobController mController;

    public JobControllerHolder(JobController controller) {
        this.mController = controller;
    }

    public JobController getController() {
        return this.mController;
    }
}
