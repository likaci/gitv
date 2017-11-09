package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import android.content.Context;
import com.gala.report.core.upload.IFeedbackResultListener;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.sdk.player.ISdkError;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IFeedbackResultCallback extends IInterfaceWrapper {
    public static final int PAGE_TYPE_DEFAULT = 0;
    public static final int PAGE_TYPE_FEEDBACK_ACTIVITY = 4;
    public static final int PAGE_TYPE_FEEDBACK_KEY = 3;
    public static final int PAGE_TYPE_GLOABAL_FEEDBACK_PANEL = 5;
    public static final int PAGE_TYPE_INIT_TASK = 1;
    public static final int PAGE_TYPE_NETDIAGNOSE_ACTIVITY = 2;

    public static abstract class Wrapper implements IFeedbackResultCallback {
        public Object getInterface() {
            return this;
        }

        public static IFeedbackResultCallback asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IFeedbackResultCallback)) {
                return null;
            }
            return (IFeedbackResultCallback) wrapper;
        }
    }

    public interface UploadResultControllerListener {
        void onFailure();

        void onSuccess();
    }

    public enum SourceType {
        menu,
        failfb,
        feedback,
        detector
    }

    IFeedbackResultListener getFeedbackResultListener();

    void init(Context context);

    void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, ApiException apiException, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType);

    void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, SourceType sourceType);

    void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType);

    void init(Context context, FeedbackData feedbackData, SourceType sourceType);

    void init(Context context, FeedbackData feedbackData, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType);

    FeedbackData makeFeedbackData(SourceType sourceType, boolean z);

    void setNormalReport(boolean z);

    void setPageType(int i);

    void setRecorderType(RecorderType recorderType);

    void setSdkError(ISdkError iSdkError);

    void setUploadOption(UploadOption uploadOption);
}
