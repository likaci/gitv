package com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast;

public class ActionHolder {
    BaseAction action;
    String key;

    public ActionHolder(String key, BaseAction action) {
        this.key = key;
        this.action = action;
    }

    public String getKey() {
        return this.key;
    }

    public BaseAction getAction() {
        return this.action;
    }

    public String toString() {
        return this.key;
    }
}
