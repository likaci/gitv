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
    private PerfRecorder a = new PerfRecorder();
    private boolean f327a;

    public abstract String getRequestId();

    public abstract String getRequestName();

    public PerfVideoJob(String name, IVideo video, VideoJobListener listener) {
        super(name, video, listener);
    }

    public final void run(JobController controller) {
        this.a.setRequestName(getRequestName());
        this.a.setRequestId(getRequestId());
        this.a.notifyStart();
        super.run(controller);
    }

    protected void notifyDone() {
        this.a.notifyDone(getState() == 2);
        super.notifyDone();
    }

    public void setMainRoutine(boolean isMainRoutine) {
        this.f327a = isMainRoutine;
    }

    public boolean isMainRoutine() {
        return this.f327a;
    }

    public long getTotalTimeSpan() {
        return this.a.getTotalTimeSpan();
    }

    public long getJsonParseTimeSpan() {
        return this.a.getJsonParseTimeSpan();
    }

    public List<Long> getIndividualTimeSpans() {
        return this.a.getIndividualTimeSpans();
    }

    public boolean isSuccess() {
        return this.a.isSuccess();
    }

    public String getErrorCode() {
        return this.a.getErrorCode();
    }

    public void setPerformanceDataReadyListener(IPerformanceDataReadyListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("PerfVideoJob", "setPerformanceDataReadyListener: " + listener);
        }
        this.a.setPerformanceDataReadyListener(listener);
    }

    protected void parseResult(ApiException e) {
        this.a.parseResult(e);
    }

    protected void parseResult(ApiResult result) {
        this.a.parseResult(result);
    }
}
