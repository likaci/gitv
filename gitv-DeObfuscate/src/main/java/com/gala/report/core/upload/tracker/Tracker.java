package com.gala.report.core.upload.tracker;

import java.util.Map;

public class Tracker {
    private final String apiName;
    private final TrackerBIZType bizType;
    private final String crashDetail;
    private final String crashType;
    private final String errorCode;
    private final String exception;
    private final String hardInfo;
    private Map<String, String> keyValueMaps;
    private final String logContent;
    private final String logType;
    private final String macAddress;
    private final String messagePushID;
    private final String qyid;
    private final String uuid;
    private final String versionCode;

    public static class TrackerBuilder {
        private String apiName = "";
        private final TrackerBIZType bizType;
        private String crashDetail = "";
        private String crashType = "";
        private String errorCode = "";
        private String exception = "";
        private final String hardInfo;
        private Map<String, String> keyValueMaps;
        private String logContent = "";
        private final String logType;
        private final String macAddress;
        private String messagePushID = "";
        private String qyid = "";
        private final String uuid;
        private final String versionCode;

        public TrackerBuilder(TrackerBIZType bizType, String logType, String versionCode, String hardInfo, String uuid, String macAddress) {
            this.bizType = bizType;
            this.logType = logType;
            this.versionCode = versionCode;
            this.hardInfo = hardInfo;
            this.uuid = uuid;
            this.macAddress = macAddress;
        }

        public TrackerBuilder setQyid(String val) {
            this.qyid = val;
            return this;
        }

        public TrackerBuilder setLogContent(String val) {
            this.logContent = val;
            return this;
        }

        public TrackerBuilder setMessagePushID(String val) {
            this.messagePushID = val;
            return this;
        }

        public TrackerBuilder setCrashType(String val) {
            this.crashType = val;
            return this;
        }

        public TrackerBuilder setException(String val) {
            this.exception = val;
            return this;
        }

        public TrackerBuilder setCrashDetail(String val) {
            this.crashDetail = val;
            return this;
        }

        public TrackerBuilder setKeyValueMaps(Map<String, String> val) {
            this.keyValueMaps = val;
            return this;
        }

        public TrackerBuilder setErrorCode(String val) {
            this.errorCode = val;
            return this;
        }

        public TrackerBuilder setApiName(String val) {
            this.apiName = val;
            return this;
        }

        public Tracker build() {
            return new Tracker();
        }
    }

    private Tracker(TrackerBuilder builder) {
        this.bizType = builder.bizType;
        this.logType = builder.logType;
        this.versionCode = builder.versionCode;
        this.hardInfo = builder.hardInfo;
        this.uuid = builder.uuid;
        this.macAddress = builder.macAddress;
        this.qyid = builder.qyid;
        this.logContent = builder.logContent;
        this.messagePushID = builder.messagePushID;
        this.crashType = builder.crashType;
        this.exception = builder.exception;
        this.crashDetail = builder.crashDetail;
        this.keyValueMaps = builder.keyValueMaps;
        this.errorCode = builder.errorCode;
        this.apiName = builder.apiName;
    }

    public TrackerBIZType getBizType() {
        return this.bizType;
    }

    public String getLogType() {
        return this.logType;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public String getHardInfo() {
        return this.hardInfo;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getQyid() {
        return this.qyid;
    }

    public String getLogContent() {
        return this.logContent;
    }

    public String getMessagePushID() {
        return this.messagePushID;
    }

    public String getCrashType() {
        return this.crashType;
    }

    public String getException() {
        return this.exception;
    }

    public String getCrashDetail() {
        return this.crashDetail;
    }

    public Map<String, String> getKeyValueMaps() {
        return this.keyValueMaps;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getApiName() {
        return this.apiName;
    }
}
