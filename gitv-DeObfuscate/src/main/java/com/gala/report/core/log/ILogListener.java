package com.gala.report.core.log;

public interface ILogListener {
    void initFail();

    void initSuccess();

    void onStartRecordFail();

    void onStartRecordSuccess();

    void onStopRecordFail();

    void onStopRecordSuccess();

    void releaseFail(String str);

    void releaseSuccess();
}
