package com.gala.video.app.player.controller;

import com.gala.multiscreen.dmr.model.msg.Notify;

public interface IDetailMultiListener {
    long getPlayPosition();

    boolean onKeyChanged(int i);

    Notify onPhoneSync();

    boolean onResolutionChanged(String str);

    boolean onSeekChanged(long j);
}
