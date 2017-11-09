package com.netdoc;

public interface NetDocListenerInterface {
    void onDownloadProgress(String str, int i, int i2);

    void onSendlogResult(int i);

    void onTestResult(String str, String str2);

    void onTestState(String str, int i);
}
