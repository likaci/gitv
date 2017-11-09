package com.gala.report.core.upload;

public interface IFeedbackResultListener {
    void beginsendLog();

    void lastsendNotComplete();

    void sendReportFailed(String str);

    void sendReportSuccess(String str, String str2);
}
