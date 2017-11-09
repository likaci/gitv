package com.gala.video.lib.share.ifmanager.bussnessIF.startup;

import java.util.List;

public class InitTaskInput {
    private long mDelay = 0;
    private int mInitPattern = 100;
    private List<Integer> mInitProcess;
    private Runnable mInitTask;

    public InitTaskInput(Runnable runnable, List<Integer> initProcess, int initPattern) {
        this.mInitProcess = initProcess;
        this.mInitTask = runnable;
        this.mInitPattern = initPattern;
    }

    public InitTaskInput(Runnable runnable, List<Integer> initProcess, int initPattern, long delay) {
        this.mInitProcess = initProcess;
        this.mInitTask = runnable;
        this.mInitPattern = initPattern;
        this.mDelay = delay;
    }

    public List<Integer> getInitProcess() {
        return this.mInitProcess;
    }

    public long getDelay() {
        return this.mDelay;
    }

    public int getInitPattern() {
        return this.mInitPattern;
    }

    public Runnable getTask() {
        return this.mInitTask;
    }
}
