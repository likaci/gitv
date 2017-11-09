package com.gala.sdk.performance;

import android.os.SystemClock;
import com.gala.sdk.performance.IPerformanceDataProvider.IPerformanceDataReadyListener;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;

public final class PerfRecorder implements IPerformanceDataProvider {
    private long f611a;
    private IPerformanceDataReadyListener f612a;
    private String f613a;
    private List<Long> f614a;
    private boolean f615a;
    private long f616b;
    private String f617b;
    private boolean f618b;
    private long f619c;
    private String f620c;

    public final boolean isMainRoutine() {
        return this.f615a;
    }

    public final long getTotalTimeSpan() {
        return this.f611a;
    }

    public final List<Long> getIndividualTimeSpans() {
        return this.f614a;
    }

    public final long getJsonParseTimeSpan() {
        return this.f616b;
    }

    public final boolean isSuccess() {
        return this.f618b;
    }

    public final String getErrorCode() {
        return this.f613a;
    }

    public final void setErrorCode(String code) {
        this.f613a = code;
    }

    public final void setMainRoutine(boolean isMain) {
        this.f615a = isMain;
    }

    public final void notifyStart() {
        this.f619c = SystemClock.uptimeMillis();
    }

    public final void notifyDone(boolean isSuccess) {
        this.f611a = SystemClock.uptimeMillis() - this.f619c;
        this.f618b = isSuccess;
        if (this.f612a != null) {
            this.f612a.onPerformanceDataReady(this);
        }
    }

    public final void parseResult(ApiResult result) {
        if (result != null && !ListUtils.isEmpty(result.getRequestTimes())) {
            this.f614a = m414a(result.getRequestTimes());
            this.f616b = (long) result.getParseTime();
        }
    }

    public final void parseResult(ApiException e) {
        if (e != null && !ListUtils.isEmpty(e.getRequestTimes())) {
            this.f614a = m414a(e.getRequestTimes());
            this.f616b = (long) e.getParseTime();
            this.f613a = e.getCode();
        }
    }

    private static final List<Long> m414a(List<Integer> list) {
        List<Long> arrayList = new ArrayList();
        for (Integer intValue : list) {
            arrayList.add(Long.valueOf((long) intValue.intValue()));
        }
        return arrayList;
    }

    public final void setPerformanceDataReadyListener(IPerformanceDataReadyListener listener) {
        this.f612a = listener;
    }

    public final void setRequestName(String name) {
        this.f617b = name;
    }

    public final void setRequestId(String id) {
        this.f620c = id;
    }

    public final String getRequestName() {
        return this.f617b;
    }

    public final String getRequestId() {
        return this.f620c;
    }
}
