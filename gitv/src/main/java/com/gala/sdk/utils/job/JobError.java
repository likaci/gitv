package com.gala.sdk.utils.job;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;

public final class JobError {
    private final ApiException a;
    private final String f326a;
    private final String b;
    private final String c;
    private final String d;

    public JobError(String errorCode, String shortMsg, String detailMsg, String apiName, ApiException exception) {
        this.f326a = errorCode;
        this.c = shortMsg;
        this.b = detailMsg;
        this.d = apiName;
        this.a = exception;
        if (LogUtils.mIsDebug && exception != null) {
            LogUtils.w("Player/Lib/Data/JobError", "JobError(" + errorCode + "), message = " + this.b, exception);
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
        return this.f326a;
    }

    public final String getMessage() {
        return this.b;
    }

    public final ApiException getException() {
        return this.a;
    }

    public final String getShortMsg() {
        return this.c;
    }

    public final String getApiName() {
        return this.d;
    }

    public final String toString() {
        return "JobError(mCode=[" + this.f326a + "], mMessage=[" + this.b + "], mException=[" + this.a + "])";
    }
}
