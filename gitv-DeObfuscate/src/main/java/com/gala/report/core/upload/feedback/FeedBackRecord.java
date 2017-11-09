package com.gala.report.core.upload.feedback;

import android.util.Log;
import com.gala.report.core.upload.config.LogRecordConfigUtils;

public class FeedBackRecord {
    private static final String TAG = "LogRecord/FeedBackRecord";
    private String errorApiName;
    private String errorCode;
    private String errorMessage;
    private ECMODULE errorModule;
    private String errorPage;
    private ECTYPE errorType;

    public static class Builder {
        private String errorApiName = "";
        private String errorCode = "";
        private String errorMessage = "";
        private ECMODULE errorModule;
        private String errorPage = "";
        private ECTYPE errorType;

        public Builder setErroCode(String val) {
            this.errorCode = val;
            return this;
        }

        public Builder setErrorPage(String val) {
            this.errorPage = val;
            return this;
        }

        public Builder setErrorModule(ECMODULE val) {
            this.errorModule = val;
            return this;
        }

        public Builder setErrorType(ECTYPE val) {
            this.errorType = val;
            return this;
        }

        public Builder setErrorMessage(String val) {
            this.errorMessage = val;
            return this;
        }

        public Builder setErrorApiName(String val) {
            this.errorApiName = val;
            return this;
        }

        public FeedBackRecord build() {
            return new FeedBackRecord();
        }
    }

    public enum ECMODULE {
        PLAYER("播放"),
        HOME("首页"),
        EPG("EPG");
        
        private String mModuleName;

        private ECMODULE(String name) {
            this.mModuleName = name;
        }

        public String toString() {
            return this.mModuleName;
        }
    }

    public enum ECTYPE {
        ERROR_NATIVEPLAYER,
        ERROR_SYSTEMPLAYER,
        ERROR_DATA,
        OTHER
    }

    private FeedBackRecord(Builder builder) {
        this.errorCode = "";
        this.errorPage = "";
        this.errorMessage = "";
        this.errorApiName = "";
        this.errorCode = builder.errorCode;
        this.errorPage = builder.errorPage;
        this.errorType = builder.errorType;
        this.errorMessage = builder.errorMessage;
        this.errorModule = builder.errorModule;
        this.errorApiName = builder.errorApiName;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LogRecordConfigUtils.getGlobalConfig().getString());
        sb.append("error_page=" + this.errorPage).append("&&");
        sb.append("error_code=" + this.errorCode).append("&&");
        sb.append("error_module=" + this.errorModule).append("&&");
        sb.append("error_type=" + this.errorType).append("&&");
        sb.append("error_message=" + this.errorMessage).append("&&");
        sb.append("error_apiname=" + this.errorApiName);
        Log.v(TAG, sb.toString());
        return sb.toString();
    }
}
