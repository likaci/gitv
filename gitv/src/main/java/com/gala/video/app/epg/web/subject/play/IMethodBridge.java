package com.gala.video.app.epg.web.subject.play;

public interface IMethodBridge {
    void onBackSubject();

    void onPlaybackFinished();

    void onScreenModeSwitched(int i);

    void onVideoSwitched(String str);

    void refreshVipBuyButton(int i);
}
