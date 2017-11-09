package com.qiyi.tv.client.impl;

public interface ClientInfo {
    String getPackageName();

    String getSignature();

    String getUuid();

    int getVersionCode();

    String getVersionName();
}
