package com.gala.sdk.performance;

import android.os.SystemClock;
import com.gala.sdk.performance.IPerformanceDataProvider.IPerformanceDataReadyListener;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;

public final class PerfRecorder implements IPerformanceDataProvider {
    private long a;
    private IPerformanceDataReadyListener f313a;
    private String f314a;
    private List<Long> f315a;
    private boolean f316a;
    private long b;
    private String f317b;
    private boolean f318b;
    private long c;
    private String f319c;

    public final boolean isMainRoutine() {
        return this.f316a;
    }

    public final long getTotalTimeSpan() {
        return this.a;
    }

    public final List<Long> getIndividualTimeSpans() {
        return this.f315a;
    }

    public final long getJsonParseTimeSpan() {
        return this.b;
    }

    public final boolean isSuccess() {
        return this.f318b;
    }

    public final String getErrorCode() {
        return this.f314a;
    }

    public final void setErrorCode(String code) {
        this.f314a = code;
    }

    public final void setMainRoutine(boolean isMain) {
        this.f316a = isMain;
    }

    public final void notifyStart() {
        this.c = SystemClock.uptimeMillis();
    }

    public final void notifyDone(boolean isSuccess) {
        this.a = SystemClock.uptimeMillis() - this.c;
        this.f318b = isSuccess;
        if (this.f313a != null) {
            this.f313a.onPerformanceDataReady(this);
        }
    }

    public final void parseResult(ApiResult result) {
        if (result != null && !ListUtils.isEmpty(result.getRequestTimes())) {
            this.f315a = a(result.getRequestTimes());
            this.b = (long) result.getParseTime();
        }
    }

    public final void parseResult(ApiException e) {
        if (e != null && !ListUtils.isEmpty(e.getRequestTimes())) {
            this.f315a = a(e.getRequestTimes());
            this.b = (long) e.getParseTime();
            this.f314a = e.getCode();
        }
    }

    private static final List<Long> a(List<Integer> list) {
        List<Long> arrayList = new ArrayList();
        for (Integer intValue : list) {
            arrayList.add(Long.valueOf((long) intValue.intValue()));
        }
        return arrayList;
    }

    public final void setPerformanceDataReadyListener(IPerformanceDataReadyListener listener) {
        this.f313a = listener;
    }

    public final void setRequestName(String name) {
        this.f317b = name;
    }

    public final void setRequestId(String id) {
        this.f319c = id;
    }

    public final String getRequestName() {
        return this.f317b;
    }

    public final String getRequestId() {
        return this.f319c;
    }
}
