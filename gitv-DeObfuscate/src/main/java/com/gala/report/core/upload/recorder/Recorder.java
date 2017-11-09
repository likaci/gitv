package com.gala.report.core.upload.recorder;

import com.gala.report.core.upload.feedback.Feedback;
import com.gala.report.core.upload.feedback.FeedbackEntry;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.tracker.Tracker;
import com.gala.report.core.upload.tracker.Tracker.TrackerBuilder;
import com.gala.report.core.upload.tracker.TrackerBIZType;
import java.io.File;
import java.util.Map;

public class Recorder {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gala$report$core$upload$recorder$RecorderType;
    private final String apiName;
    private final String crashDetail;
    private final String crashType;
    private final String errorCode;
    private final String errorMessage;
    private final String exception;
    private final String fbType;
    private final File file;
    private final String hardInfo;
    private final String iddRecord;
    private final Map<String, String> keyValueMaps;
    private final String logContent;
    private final RecorderLogType logType;
    private final String macAddress;
    private final String messagePushID;
    private final String quesDetail;
    private final FeedbackEntry quesEntry;
    private final FeedbackType quesType;
    private final String qyid;
    private final RecorderType type;
    private final String userInfo;
    private final String uuid;
    private final String versionCode;

    public static class RecorderBuilder {
        private String apiName = "";
        private String crashDetail = "";
        private String crashType = "";
        private String errorCode = "";
        private String errorMessage = "";
        private String exception = "";
        private String fbType = "";
        private File file;
        private final String hardInfo;
        private String iddRecord = "";
        private Map<String, String> keyValueMaps;
        private String logContent = "";
        private final RecorderLogType logType;
        private final String macAddress;
        private String messagePushID = "";
        private String quesDetail = "";
        private FeedbackEntry quesEntry = FeedbackEntry.LOG_RECORD;
        private FeedbackType quesType = FeedbackType.COMMON;
        private String qyid = "";
        private final RecorderType type;
        private String userInfo = "";
        private final String uuid;
        private final String versionCode;

        public RecorderBuilder(RecorderType type, RecorderLogType logType, String versionCode, String hardInfo, String uuid, String macAddress) {
            this.type = type;
            this.logType = logType;
            this.versionCode = versionCode;
            this.hardInfo = hardInfo;
            this.uuid = uuid;
            this.macAddress = macAddress;
        }

        public RecorderBuilder setQyid(String val) {
            this.qyid = val;
            return this;
        }

        public RecorderBuilder setLogContent(String val) {
            this.logContent = val;
            return this;
        }

        public RecorderBuilder setMessagePushID(String val) {
            this.messagePushID = val;
            return this;
        }

        public RecorderBuilder setCrashType(String val) {
            this.crashType = val;
            return this;
        }

        public RecorderBuilder setException(String val) {
            this.exception = val;
            return this;
        }

        public RecorderBuilder setCrashDetail(String val) {
            this.crashDetail = val;
            return this;
        }

        public RecorderBuilder setErrorCode(String val) {
            this.errorCode = val;
            return this;
        }

        public RecorderBuilder setApiName(String val) {
            this.apiName = val;
            return this;
        }

        public RecorderBuilder setErrorMessagec(String val) {
            this.errorMessage = val;
            return this;
        }

        public RecorderBuilder setFbType(String val) {
            this.fbType = val;
            return this;
        }

        public RecorderBuilder setIddRecord(String val) {
            this.iddRecord = val;
            return this;
        }

        public RecorderBuilder setUserInfo(String val) {
            this.userInfo = val;
            return this;
        }

        public RecorderBuilder setQuesType(FeedbackType val) {
            this.quesType = val;
            return this;
        }

        public RecorderBuilder setQuesDetail(String val) {
            this.quesDetail = val;
            return this;
        }

        public RecorderBuilder setKeyValueMaps(Map<String, String> val) {
            this.keyValueMaps = val;
            return this;
        }

        public RecorderBuilder setFile(File val) {
            this.file = val;
            return this;
        }

        public Recorder build() {
            return new Recorder();
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gala$report$core$upload$recorder$RecorderType() {
        int[] iArr = $SWITCH_TABLE$com$gala$report$core$upload$recorder$RecorderType;
        if (iArr == null) {
            iArr = new int[RecorderType.values().length];
            try {
                iArr[RecorderType._CRASH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[RecorderType._ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[RecorderType._FEEDBACK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[RecorderType._FEEDBACK_AUTO.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$gala$report$core$upload$recorder$RecorderType = iArr;
        }
        return iArr;
    }

    private Recorder(RecorderBuilder builder) {
        this.type = builder.type;
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
        this.errorCode = builder.errorCode;
        this.apiName = builder.apiName;
        this.errorMessage = builder.errorMessage;
        this.fbType = builder.fbType;
        this.iddRecord = builder.iddRecord;
        this.userInfo = builder.userInfo;
        this.quesType = builder.quesType;
        this.quesEntry = builder.quesEntry;
        this.quesDetail = builder.quesDetail;
        this.keyValueMaps = builder.keyValueMaps;
        this.file = builder.file;
    }

    public RecorderType getRecorderType() {
        return this.type;
    }

    public RecorderLogType getLogType() {
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

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getApiName() {
        return this.apiName;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getFbType() {
        return this.fbType;
    }

    public String getIddRecord() {
        return this.iddRecord;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public FeedbackType getQuesType() {
        return this.quesType;
    }

    public String getQuesDetail() {
        return this.quesDetail;
    }

    public Map<String, String> getKeyValueMaps() {
        return this.keyValueMaps;
    }

    public File getFile() {
        return this.file;
    }

    public Tracker getTracker() {
        TrackerBIZType bizType;
        switch ($SWITCH_TABLE$com$gala$report$core$upload$recorder$RecorderType()[this.type.ordinal()]) {
            case 1:
                bizType = TrackerBIZType._BIZTYPE_CRASH;
                break;
            case 2:
                bizType = TrackerBIZType._BIZTYPE_AUTO;
                break;
            case 4:
                bizType = TrackerBIZType._BIZTYPE_FEEDBACK;
                break;
            default:
                bizType = TrackerBIZType._BIZTYPE_FEEDBACK;
                break;
        }
        return new TrackerBuilder(bizType, this.logType.toString(), this.versionCode, this.hardInfo, this.uuid, this.macAddress).setQyid(this.qyid).setLogContent(this.logContent).setException(this.exception).setCrashType(this.crashType).setCrashDetail(this.crashDetail).setMessagePushID(this.messagePushID).setKeyValueMaps(this.keyValueMaps).setLogContent(this.logContent).setErrorCode(this.errorCode).setApiName(this.apiName).build();
    }

    public Feedback getFeedback() {
        return new Feedback(this.quesType, this.quesEntry, this.logType.toString(), this.quesDetail, this.iddRecord, this.versionCode);
    }
}
