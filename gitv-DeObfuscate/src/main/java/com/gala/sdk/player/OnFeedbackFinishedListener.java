package com.gala.sdk.player;

public interface OnFeedbackFinishedListener {
    void onFailed(ISdkError iSdkError);

    void onSuccess(String str, String str2);
}
