package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

import java.util.List;

public class KeyValue {
    private RunnableQueue mExactRunnableQueue = new RunnableQueue();
    private RunnableQueue mFuzzyRunnableQueue = new RunnableQueue();
    private boolean mIsAppendSeekMatch = false;
    private RunnableQueue mReservedRunnableQueue = new RunnableQueue();
    private String mSceneId;

    public void setSceneId(String sceneId) {
        this.mSceneId = sceneId;
    }

    public String getSceneId() {
        return this.mSceneId;
    }

    public void addFuzzyMatch(String key, Runnable runnable) {
        this.mFuzzyRunnableQueue.addMatch(key, runnable);
    }

    public List<String> getFuzzyKeys() {
        return this.mFuzzyRunnableQueue.getKeys();
    }

    public Runnable getFuzzyRunnable(String key) {
        return this.mFuzzyRunnableQueue.getRunnable(key);
    }

    public void addExactMatch(String key, Runnable runnable) {
        this.mExactRunnableQueue.addMatch(key, runnable);
    }

    public List<String> getExactKeys() {
        return this.mExactRunnableQueue.getKeys();
    }

    public Runnable getExactRunnable(String key) {
        return this.mExactRunnableQueue.getRunnable(key);
    }

    public void addReservedMatch(String key, Runnable runnable) {
        this.mReservedRunnableQueue.addMatch(key, runnable);
    }

    public List<String> getReservedKeys() {
        return this.mReservedRunnableQueue.getKeys();
    }

    public Runnable getReservedRunnable(String key) {
        return this.mReservedRunnableQueue.getRunnable(key);
    }

    public void setAppendSeek(boolean isAppend) {
        this.mIsAppendSeekMatch = isAppend;
    }

    public boolean isAppendSeek() {
        return this.mIsAppendSeekMatch;
    }
}
