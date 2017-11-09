package com.gala.video.app.epg.home.ads.controller;

public interface AdStatusCallBack {
    void onAdPrepared();

    void onError();

    void onFinished();

    void onTimeOut();
}
