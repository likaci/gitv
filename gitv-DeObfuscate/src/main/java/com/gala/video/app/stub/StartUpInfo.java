package com.gala.video.app.stub;

public class StartUpInfo {
    private static StartUpInfo sIns = null;
    private long mPluginLoadInterval = 0;
    private String mPluginState = "";
    private long mStartTime = 0;
    private boolean mStartUpFromLaunch = false;

    public static StartUpInfo get() {
        if (sIns == null) {
            sIns = new StartUpInfo();
        }
        return sIns;
    }

    public void setPluginLoadInterval(long interval) {
        this.mPluginLoadInterval = interval;
    }

    public void setStartTime(long start) {
        this.mStartTime = start;
    }

    public void setStartFromLaunch(boolean v) {
        this.mStartUpFromLaunch = v;
    }

    public boolean isStartFromLaunch() {
        return this.mStartUpFromLaunch;
    }

    public long getPluginLoadInterval() {
        return this.mPluginLoadInterval;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setPluginState(String state) {
        this.mPluginState = state;
    }

    public String getPluginState() {
        return this.mPluginState;
    }
}
