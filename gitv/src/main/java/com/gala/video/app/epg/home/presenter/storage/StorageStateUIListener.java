package com.gala.video.app.epg.home.presenter.storage;

public interface StorageStateUIListener {
    void onUsbConnected();

    void onUsbDisconnected();

    void showStorageIndicators();
}
