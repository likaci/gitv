package com.gala.video.lib.share.network;

import android.widget.ImageView;

public interface NetworkStateUIActualOperateListener {
    void init(ImageView imageView, ImageView imageView2);

    void onDestroy();

    void onStop();

    void onWifiNetworkNormal();

    void onWiredNetworkNormal();

    void postCancelCallback();

    void removeCancelCallback();

    void setNetImageNull();

    void setNetImageWifi();

    void setNetImageWired();

    void showNetErrorAnimation(int i);

    void updatePhoneState();
}
