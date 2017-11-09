package com.gala.speedrunner.speedrunner;

public interface IRunCheckCallback {
    void onDownloadProgress(String str, int i, int i2);

    void onFailed(String str);

    void onReportStatus(String str, int i);

    void onSendLogResult(int i);

    void onSuccess(int i, int i2, String str);

    void onTestResult(String str, String str2);
}
