package com.qiyi.tv.client;

public interface ConnectionListener {
    void onAuthSuccess();

    void onConnected();

    void onDisconnected();

    void onError(int i);
}
