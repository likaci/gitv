package com.gala.video.lib.framework.coreservice.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerHolder;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.framework.coreservice.netdiagnose.thirdspeed.DownloadStateListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.thirdspeed.DownloadTask;
import java.util.Timer;
import java.util.TimerTask;
import org.cybergarage.http.HTTP;

public class ThirdSpeedTestJob extends NetDiagnoseJob {
    private final String TAG = ("NetDiagnoseJob/ThirdSpeedTestJob@" + hashCode());
    private Callback mDownloadStateListener1;
    private Callback mDownloadStateListener2;
    private String mFirstUrl;
    private String mSecondUrl;
    private StringBuffer mSpeedTestResult;

    private class Callback extends JobControllerHolder implements DownloadStateListener {
        private boolean mControlJob = false;

        public Callback(JobController controller, boolean controlJob) {
            super(controller);
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "controlJob: " + controlJob);
            this.mControlJob = controlJob;
        }

        public void onDownloadComplete(String url, long time, long total) {
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "onDownloadComplete:" + time + "," + total);
            ThirdSpeedTestJob.this.mSpeedTestResult.append("Test Download File: " + url);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(ThirdSpeedTestJob.getSpeedThird(time, total));
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "mControlJob:" + this.mControlJob);
            if (this.mControlJob) {
                this.mControlJob = false;
                LogUtils.e(ThirdSpeedTestJob.this.TAG, "mSpeedTestResult = ", ThirdSpeedTestJob.this.mSpeedTestResult);
                ((NetDiagnoseInfo) ThirdSpeedTestJob.this.getData()).setThirdSpeedTestResult(ThirdSpeedTestJob.this.mSpeedTestResult.toString());
                ThirdSpeedTestJob.this.mIsJobComplete = true;
                ThirdSpeedTestJob.this.notifyJobSuccess(getController());
            }
        }

        public void onDownloadCancled(String url, long time, long total) {
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "onDownloadCanceled:" + time + "," + total);
            ThirdSpeedTestJob.this.mSpeedTestResult.append("Test Download File: " + url);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(ThirdSpeedTestJob.getSpeedThird(time, total));
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "mControlJob:" + this.mControlJob);
            if (this.mControlJob) {
                this.mControlJob = false;
                LogUtils.e(ThirdSpeedTestJob.this.TAG, "mSpeedTestResult = ", ThirdSpeedTestJob.this.mSpeedTestResult);
                ((NetDiagnoseInfo) ThirdSpeedTestJob.this.getData()).setThirdSpeedTestResult(ThirdSpeedTestJob.this.mSpeedTestResult.toString());
                ThirdSpeedTestJob.this.mIsJobComplete = true;
                ThirdSpeedTestJob.this.notifyJobFail(getController(), new JobError("onDownloadCancled"));
            }
        }

        public void onDownloadFailed(String url, String errorMsg) {
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "onDownloadFailed:" + errorMsg);
            ThirdSpeedTestJob.this.mSpeedTestResult.append("Test Download File Failed: " + url);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            ThirdSpeedTestJob.this.mSpeedTestResult.append("reason: " + errorMsg);
            ThirdSpeedTestJob.this.mSpeedTestResult.append(HTTP.CRLF);
            LogUtils.d(ThirdSpeedTestJob.this.TAG, "mControlJob:" + this.mControlJob);
            if (this.mControlJob) {
                this.mControlJob = false;
                LogUtils.e(ThirdSpeedTestJob.this.TAG, "mSpeedTestResult = ", ThirdSpeedTestJob.this.mSpeedTestResult);
                ((NetDiagnoseInfo) ThirdSpeedTestJob.this.getData()).setThirdSpeedTestResult(ThirdSpeedTestJob.this.mSpeedTestResult.toString());
                ThirdSpeedTestJob.this.mIsJobComplete = true;
                ThirdSpeedTestJob.this.notifyJobFail(getController(), new JobError("onDownloadFailed"));
            }
        }
    }

    public ThirdSpeedTestJob(NetDiagnoseInfo data) {
        super(data);
    }

    public ThirdSpeedTestJob(NetDiagnoseInfo data, String firstUrl, String secondUrl) {
        super(data);
        this.mFirstUrl = firstUrl;
        this.mSecondUrl = secondUrl;
    }

    public ThirdSpeedTestJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        LogUtils.d(this.TAG, ">> onRun");
        this.mSpeedTestResult = new StringBuffer();
        this.mDownloadStateListener1 = new Callback(controller, false);
        this.mDownloadStateListener2 = new Callback(controller, true);
        if (StringUtils.isEmpty(this.mFirstUrl)) {
            this.mSpeedTestResult.append("mFirstUrl is null \n");
            LogUtils.e(this.TAG, "mFirstUrl is null ");
        } else {
            startSpeedTest(this.mFirstUrl, this.mDownloadStateListener1);
        }
        if (StringUtils.isEmpty(this.mSecondUrl)) {
            this.mSpeedTestResult.append("mSecondUrl is null \n");
            LogUtils.e(this.TAG, "mSecondUrl is null ");
            this.mIsJobComplete = true;
            notifyJobFail(controller, new JobError("onDownloadCancled"));
        } else {
            startSpeedTest(this.mSecondUrl, this.mDownloadStateListener2);
        }
        LogUtils.d(this.TAG, "<< onRun");
    }

    private void startSpeedTest(String url, DownloadStateListener downloadStateListener) {
        final DownloadTask downloadTask = new DownloadTask(url, downloadStateListener);
        new Timer().schedule(new TimerTask() {
            public void run() {
                LogUtils.d(ThirdSpeedTestJob.this.TAG, "10s later, is Task Running:" + downloadTask.isRunning());
                if (downloadTask.isRunning()) {
                    downloadTask.cancel();
                }
            }
        }, 10000);
        downloadTask.startDownload();
    }

    private static String getSpeedThird(long time, long total) {
        long avg;
        if (time <= 0 || total <= 0) {
            avg = 0;
        } else {
            avg = ((total / 1024) / (time / 1000)) * 8;
        }
        return "running time:" + time + ", download length:" + total + ", average: " + avg + "Kb/s";
    }
}
