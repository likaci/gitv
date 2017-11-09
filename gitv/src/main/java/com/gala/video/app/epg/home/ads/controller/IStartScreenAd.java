package com.gala.video.app.epg.home.ads.controller;

import android.view.KeyEvent;
import android.view.ViewGroup;

public interface IStartScreenAd {
    boolean dispatchKeyEvent(KeyEvent keyEvent);

    boolean enableJump();

    void loadData(long j);

    void setAdStatusCallBack(AdStatusCallBack adStatusCallBack);

    void showAd(ViewGroup viewGroup);

    void stop();
}
