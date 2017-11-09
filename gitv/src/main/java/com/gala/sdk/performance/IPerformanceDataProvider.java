package com.gala.sdk.performance;

import java.util.List;

public interface IPerformanceDataProvider {

    public interface IPerformanceDataReadyListener {
        void onPerformanceDataReady(IPerformanceDataProvider iPerformanceDataProvider);
    }

    String getErrorCode();

    List<Long> getIndividualTimeSpans();

    long getJsonParseTimeSpan();

    String getRequestId();

    String getRequestName();

    long getTotalTimeSpan();

    boolean isMainRoutine();

    boolean isSuccess();

    void setPerformanceDataReadyListener(IPerformanceDataReadyListener iPerformanceDataReadyListener);
}
