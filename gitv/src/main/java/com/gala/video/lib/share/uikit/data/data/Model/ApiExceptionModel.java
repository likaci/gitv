package com.gala.video.lib.share.uikit.data.data.Model;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class ApiExceptionModel {
    private static final String TAG = ApiExceptionModel.class.getName();
    protected String mApiCode;
    private String mApiName;
    protected ErrorEvent mErrorEvent;
    protected String mErrorLog;
    protected String mErrorMessage;
    protected String mErrorUrl;
    private String mExceptionName;
    protected String mHttpCode;

    public String getApiName() {
        return this.mApiName;
    }

    public void setApiName(String mApiName) {
        this.mApiName = mApiName;
    }

    public String getApiCode() {
        return this.mApiCode;
    }

    public void setApiCode(String apiCode) {
        this.mApiCode = apiCode;
    }

    public String getHttpCode() {
        return this.mHttpCode;
    }

    public void setHttpCode(String httpCode) {
        this.mHttpCode = httpCode;
    }

    public ErrorEvent getErrorEvent() {
        return this.mErrorEvent;
    }

    public void setErrorEvent(ErrorEvent mErrorEvent) {
        this.mErrorEvent = mErrorEvent;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    public String getErrorUrl() {
        return this.mErrorUrl;
    }

    public void setErrorUrl(String mErrorUrl) {
        this.mErrorUrl = mErrorUrl;
    }

    public String getErrorLog() {
        return this.mErrorLog;
    }

    public void setErrorLog(String mErrorLog) {
        this.mErrorLog = mErrorLog;
    }

    public String getExceptionName() {
        return this.mExceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.mExceptionName = exceptionName;
    }

    public void clear() {
        this.mApiCode = null;
        this.mHttpCode = null;
        this.mErrorEvent = null;
        this.mErrorMessage = null;
        this.mErrorUrl = null;
        this.mExceptionName = null;
    }

    public void readLog() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                try {
                    LogUtils.d(ApiExceptionModel.TAG, "readLog()---START---");
                    ApiExceptionModel.this.setErrorLog(GetInterfaceTools.getILogRecordProvider().getLogCore().getLog(50));
                    LogUtils.d(ApiExceptionModel.TAG, "readLog()---END---");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
