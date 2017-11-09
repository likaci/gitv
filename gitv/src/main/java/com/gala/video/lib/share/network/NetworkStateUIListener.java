package com.gala.video.lib.share.network;

public interface NetworkStateUIListener {
    void onWifiNetworkNormal();

    void onWiredNetworkNormal();

    void postCancelCallback();

    void removeCancelCallback();

    void setNetImageNull();

    void setNetImageWifi();

    void setNetImageWired();

    void showNetErrorAnimation(int i);
}
