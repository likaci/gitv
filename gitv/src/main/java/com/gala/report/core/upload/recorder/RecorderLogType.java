package com.gala.report.core.upload.recorder;

import com.gala.video.lib.share.ifimpl.logrecord.LogListener;

public enum RecorderLogType {
    CRASH_REPORT_DEFAULT("客户端崩溃异常"),
    ANR_REPORT("客户端ANR异常"),
    LOGRECORD_REPORT_AUTO("客户端自动发送"),
    LOGRECORD_CLICK_FEEDBACK(LogListener.MSG_LOGS_FEEDBACK),
    LOGRECORD_NETDINOSE_FEEDBACK(LogListener.MSG_LOG_NEWWORKL),
    LOGRECORD_SECOND_FEEDBACK(LogListener.MSG_LOGS_SECONDNARY_FEEDBACK),
    LOGRECORD_MANUAL_FEEDBACK(LogListener.MSG_LOG_MANUAL);
    
    private final String mShortName;

    private RecorderLogType(String shortName) {
        this.mShortName = shortName;
    }

    public String toString() {
        return this.mShortName;
    }
}
