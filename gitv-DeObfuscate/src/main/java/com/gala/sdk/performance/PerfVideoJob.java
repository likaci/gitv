package com.gala.sdk.performance;

import com.gala.sdk.performance.IPerformanceDataProvider.IPerformanceDataReadyListener;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.job.VideoJob;
import com.gala.sdk.player.data.job.VideoJobListener;
import com.gala.sdk.utils.job.JobController;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public abstract class PerfVideoJob extends VideoJob implements IPerformanceDataProvider {
    private PerfRecorder f629a = new PerfRecorder();
    private boolean f630a;

    public abstract String getRequestId();

    public abstract String getRequestName();

    public PerfVideoJob(String name, IVideo video, VideoJobListener listener) {
        super(name, video, listener);
    }

    public final void run(JobController controller) {
        this.f629a.setRequestName(getRequestName());
        this.f629a.setRequestId(getRequestId());
        this.f629a.notifyStart();
        super.run(controller);
    }

    protected void notifyDone() {
        this.f629a.notifyDone(getState() == 2);
        super.notifyDone();
    }

    public void setMainRoutine(boolean isMainRoutine) {
        this.f630a = isMainRoutine;
    }

    public boolean isMainRoutine() {
        return this.f630a;
    }

    public long getTotalTimeSpan() {
        return this.f629a.getTotalTimeSpan();
    }

    public long getJsonParseTimeSpan() {
        return this.f629a.getJsonParseTimeSpan();
    }

    public List<Long> getIndividualTimeSpans() {
        return this.f629a.getIndividualTimeSpans();
    }

    public boolean isSuccess() {
        return this.f629a.isSuccess();
    }

    public String getErrorCode() {
        return this.f629a.getErrorCode();
    }

    public void setPerformanceDataReadyListener(IPerformanceDataReadyListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("PerfVideoJob", "setPerformanceDataReadyListener: " + listener);
        }
        this.f629a.setPerformanceDataReadyListener(listener);
    }

    protected void parseResult(ApiException e) {
        this.f629a.parseResult(e);
    }

    protected void parseResult(ApiResult result) {
        this.f629a.parseResult(result);
    }
}
