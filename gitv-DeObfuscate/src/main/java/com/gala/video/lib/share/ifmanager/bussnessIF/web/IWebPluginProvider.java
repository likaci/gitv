package com.gala.video.lib.share.ifmanager.bussnessIF.web;

import com.gala.video.crosswalkinterface.IXWalkPlugin;

public interface IWebPluginProvider {
    IXWalkPlugin getIXWalkPlugin();

    boolean isAlready();

    void load();
}
