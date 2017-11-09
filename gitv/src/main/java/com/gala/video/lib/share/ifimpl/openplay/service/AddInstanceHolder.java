package com.gala.video.lib.share.ifimpl.openplay.service;

import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IAddInstanceHolder;

public class AddInstanceHolder extends IAddInstanceHolder {
    public ServerCommand<?> command;
    public long count;
    public int dataMaxCount;
    public long duration;
    public long interval;
    public String key;

    public AddInstanceHolder(String key, long duration, long count, long interval, int dataMaxCount, ServerCommand<?> command) {
        this.key = key;
        this.duration = duration;
        this.count = count;
        this.interval = interval;
        this.dataMaxCount = dataMaxCount;
        this.command = command;
    }
}
