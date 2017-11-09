package com.gala.sdk.player;

public interface FeedBackManager {

    public interface OnFeedbackFinishedListener {
        void onFailed(ISdkError iSdkError);

        void onSuccess(String str, String str2);
    }

    public interface OnNetDiagnoseFinishedListener {
        void onFailed(ISdkError iSdkError);

        void onSuccess();
    }

    void cancelNetDiagnose();

    void sendFeedback(IMedia iMedia, ISdkError iSdkError, OnFeedbackFinishedListener onFeedbackFinishedListener);

    void startNetDiagnose(IMedia iMedia, String str, OnNetDiagnoseFinishedListener onNetDiagnoseFinishedListener);
}
