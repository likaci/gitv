package com.gala.sdk.utils.job;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;

public final class JobError {
    private final ApiException f726a;
    private final String f727a;
    private final String f728b;
    private final String f729c;
    private final String f730d;

    public JobError(String errorCode, String shortMsg, String detailMsg, String apiName, ApiException exception) {
        this.f727a = errorCode;
        this.f729c = shortMsg;
        this.f728b = detailMsg;
        this.f730d = apiName;
        this.f726a = exception;
        if (LogUtils.mIsDebug && exception != null) {
            LogUtils.m1578w("Player/Lib/Data/JobError", "JobError(" + errorCode + "), message = " + this.f728b, exception);
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

    public final String getCode() {
        return this.f727a;
    }

    public final String getMessage() {
        return this.f728b;
    }

    public final ApiException getException() {
        return this.f726a;
    }

    public final String getShortMsg() {
        return this.f729c;
    }

    public final String getApiName() {
        return this.f730d;
    }

    public final String toString() {
        return "JobError(mCode=[" + this.f727a + "], mMessage=[" + this.f728b + "], mException=[" + this.f726a + "])";
    }
}
