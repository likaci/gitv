package com.gala.video.lib.framework.core.job;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;

public final class JobError {
    private static final String TAG = "Player/Lib/Data/JobError";
    private final String mApiName;
    private final String mCode;
    private final String mDetailMsg;
    private final ApiException mException;
    private final String mShortMsg;

    public JobError(String errorCode, String shortMsg, String detailMsg, String apiName, ApiException exception) {
        this.mCode = errorCode;
        this.mShortMsg = shortMsg;
        this.mDetailMsg = detailMsg;
        this.mApiName = apiName;
        this.mException = exception;
        if (LogUtils.mIsDebug && exception != null) {
            LogUtils.m1578w(TAG, "JobError(" + errorCode + "), message = " + this.mDetailMsg, exception);
        }
    }

    public JobError(String errorCode, String shortMsg, String detailMsg, String apiName) {
        this(errorCode, shortMsg, detailMsg, apiName, null);
    }

    public JobError(String errorCode, ApiException exception, String shortMsg, String apiName) {
        this(errorCode, shortMsg, null, apiName, exception);
    }

    public JobError(String errorCode, ApiException exception) {
        this(errorCode, null, null, null, exception);
    }

    public JobError(String errorCode) {
        this(errorCode, null, null, null, null);
    }

    public String getCode() {
        return this.mCode;
    }

    public String getMessage() {
        return this.mDetailMsg;
    }

    public ApiException getException() {
        return this.mException;
    }

    public String getShortMsg() {
        return this.mShortMsg;
    }

    public String getApiName() {
        return this.mApiName;
    }

    public String toString() {
        return "JobError(mCode=[" + this.mCode + "], mMessage=[" + this.mDetailMsg + "], mException=[" + this.mException + "])";
    }
}
