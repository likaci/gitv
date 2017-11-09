package com.gala.report.core.upload;

import android.content.Context;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;

public interface IUploadCore {
    void init(Context context, String str);

    void resetFeedbackValue();

    void resetTrackerValue();

    void sendRecorder(UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, IFeedbackResultListener iFeedbackResultListener);
}
