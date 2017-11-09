package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.speedrunner.netdoctor.TVNetDoctor;
import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.IRunCheckCallback;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerHolder;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.netdoc.FileType;

public class CdnDiagnoseJob extends NetDiagnoseJob {
    private final String TAG = ("NetDiagnoseJob/CdnDiagnoseJob@" + hashCode());
    private FileType mFileType;
    private TVNetDoctor mNetDoctor;
    private INDUploadCallback mUploadCallback;

    private class Callback extends JobControllerHolder implements IRunCheckCallback {

        class C17001 implements Runnable {
            C17001() {
            }

            public void run() {
                CdnDiagnoseJob.this.stopCheck();
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "mNetDoctor onSuccess,this = " + this);
                CdnDiagnoseJob.this.mIsJobComplete = true;
                CdnDiagnoseJob.this.notifyJobSuccess(Callback.this.getController());
            }
        }

        class C17012 implements Runnable {
            C17012() {
            }

            public void run() {
                CdnDiagnoseJob.this.stopCheck();
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "mNetDoctor onFailed,this = " + this);
                CdnDiagnoseJob.this.mIsJobComplete = true;
                CdnDiagnoseJob.this.notifyJobFail(Callback.this.getController(), new JobError(null));
            }
        }

        public Callback(JobController controller) {
            super(controller);
        }

        public void onSuccess(int step, int speed, String jsonResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "mFileType = " + CdnDiagnoseJob.this.mFileType + " onSuccess(" + step + ", " + speed + ")");
            }
            ((NetDiagnoseInfo) CdnDiagnoseJob.this.getData()).setCdnDiagnoseResult("mFileType = " + CdnDiagnoseJob.this.mFileType + " jsonResult = " + jsonResult, speed * 8);
            new Thread8K(new C17001(), CdnDiagnoseJob.this.TAG).start();
        }

        public void onFailed(String jsonResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "mFileType = " + CdnDiagnoseJob.this.mFileType + " onFailed(" + jsonResult + ")");
            }
            ((NetDiagnoseInfo) CdnDiagnoseJob.this.getData()).setCdnDiagnoseResult("mFileType = " + CdnDiagnoseJob.this.mFileType + " jsonResult = " + jsonResult, 0);
            new Thread8K(new C17012(), CdnDiagnoseJob.this.TAG).start();
        }

        public void onTestResult(String arg0, String arg1) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "onTestResult");
            }
        }

        public void onReportStatus(String arg0, int arg1) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "onReportStatus");
            }
        }

        public void onSendLogResult(int arg0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "onSendLogResult");
            }
            if (CdnDiagnoseJob.this.mUploadCallback != null) {
                CdnDiagnoseJob.this.mUploadCallback.uploadNetDiagnoseDone();
            }
        }

        public void onDownloadProgress(String arg0, int arg1, int arg2) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "onDownloadProgress");
            }
        }
    }

    public CdnDiagnoseJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener, TVNetDoctor doctor, INDUploadCallback uploadCallback, FileType fileType) {
        super(data, listener);
        this.mFileType = fileType;
        this.mNetDoctor = doctor;
        this.mUploadCallback = uploadCallback;
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> onRun");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> onRun  mNetDocto.String = " + this.mNetDoctor.toString());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> onRun  this = " + this);
        }
        this.mNetDoctor.setSpeedListener(new Callback(controller));
        checkPlay(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< onRun");
        }
    }

    private void checkPlay(final JobController controller) {
        final CDNNetDiagnoseInfo info = (CDNNetDiagnoseInfo) getData();
        LogUtils.m1568d(this.TAG, "checkPlay getData().getAlbum():" + info.getAlbum());
        LogUtils.m1568d(this.TAG, "checkPlay get tvapi mac:" + TVApiBase.getTVApiProperty().getMacAddress());
        LogUtils.m1568d(this.TAG, "FileType = " + this.mFileType);
        if (info.getAlbum() == null) {
            CreateInterfaceTools.createGetAlbumProvider().getAlbumProviderAsync(new com.gala.video.lib.share.ifmanager.bussnessIF.epg.netdiagnose.IGetAlbumProvider.Callback() {

                class C16981 implements Runnable {
                    C16981() {
                    }

                    public void run() {
                        CdnDiagnoseJob.this.mIsJobComplete = true;
                        CdnDiagnoseJob.this.notifyJobFail(controller, new JobError(null));
                    }
                }

                public void onResult(IOneAlbumProvider albumProvider) {
                    if (albumProvider == null) {
                        LogUtils.m1568d(CdnDiagnoseJob.this.TAG, "go to onFailed");
                        ((NetDiagnoseInfo) CdnDiagnoseJob.this.getData()).setCdnDiagnoseResult("no history album cache albumProvider is null", 0);
                        new Thread8K(new C16981(), CdnDiagnoseJob.this.TAG).start();
                        return;
                    }
                    CdnDiagnoseJob.this.mNetDoctor.checkPlay(controller.getContext(), CdnDiagnoseJob.this.mFileType, 0, albumProvider, info.getRever());
                }
            });
        } else {
            this.mNetDoctor.checkPlay(controller.getContext(), info.getAlbum(), info.isVipUser(), this.mFileType, info.getUserCookie(), info.getUserId(), info.getStartTime(), String.valueOf(info.getBid()), null, info.getRever());
        }
    }

    public void stopCheck() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "stopCheck()");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "stopCheck, mTvNetDoctor is null ?" + (this.mNetDoctor == null));
        }
        if (this.mNetDoctor != null && this.mNetDoctor.isStart()) {
            this.mNetDoctor.stopPlay();
        }
    }
}
