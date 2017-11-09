package com.gala.video.lib.share.ifimpl.openplay.service;

public interface IAccessWatcher {
    void increaseAccessCount();

    boolean isAllowedAccess();

    void replace(int i);
}
