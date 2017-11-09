package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;

public class FeedbackData {
    private Recorder mRecorder;
    private UploadExtraInfo mUploadExtraInfo;
    private UploadOption mUploadOption;

    public UploadExtraInfo getUploadExtraInfo() {
        return this.mUploadExtraInfo;
    }

    public void setUploadExtraInfo(UploadExtraInfo v) {
        this.mUploadExtraInfo = v;
    }

    public UploadOption getUploadOption() {
        return this.mUploadOption;
    }

    public void setUploadOption(UploadOption v) {
        this.mUploadOption = v;
    }

    public Recorder getRecorder() {
        return this.mRecorder;
    }

    public void setRecorder(Recorder v) {
        this.mRecorder = v;
    }
}
