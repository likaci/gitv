package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.gala.report.core.upload.tracker.TrackerRecord;
import com.gala.sdk.player.ISdkError;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.tvos.apps.utils.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedBackModel {
    protected String appVersion;
    private String custom;
    private String errorCode;
    private String errorLog;
    private String errorMsg;
    private String exceptionName;
    private String feedbackErrorCode;
    protected String ip;
    private boolean isShowQR = true;
    private String mApiName;
    private boolean mNeedLogcat = true;
    private TrackerRecord mRecord;
    private ISdkError mSDKError;
    protected String mac;
    protected String model;
    protected String osVersionCode;
    protected String packageTime;
    protected String time;
    private String url;

    public String getApiName() {
        return this.mApiName;
    }

    public void setApiName(String mApiName) {
        this.mApiName = mApiName;
    }

    public String getFeedbackErrorCode() {
        return this.feedbackErrorCode;
    }

    public void setFeedbackErrorCode(String feedbackErrorCode) {
        this.feedbackErrorCode = feedbackErrorCode;
    }

    public FeedBackModel() {
        initData();
    }

    public FeedBackModel(String url, String errorCode, String exceptionName, String errorMsg, String log) {
        this.url = url;
        this.errorCode = errorCode;
        this.exceptionName = exceptionName;
        this.errorMsg = errorMsg;
        this.errorLog = log;
        initData();
    }

    private void initData() {
        this.time = getTime();
        this.osVersionCode = getOsVersionCode();
        this.ip = AppRuntimeEnv.get().getDeviceIp();
        this.mac = DeviceUtils.getMacAddr();
        this.custom = Project.getInstance().getBuild().getCustomerName();
        this.model = Build.MODEL;
        this.appVersion = Project.getInstance().getBuild().getVersionString();
        this.packageTime = Project.getInstance().getBuild().getBuildTime();
    }

    public String getCustom() {
        return this.custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    @SuppressLint({"SimpleDateFormat"})
    private String getTime() {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD16H).format(new Date());
    }

    private String getOsVersionCode() {
        return VERSION.SDK_INT + "";
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorLog() {
        return this.errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getQRString() {
        StringBuilder builder = new StringBuilder();
        if (!("".equals(getFeedbackErrorCode()) || getFeedbackErrorCode() == null)) {
            builder.append("\nfeedback server return error code = " + getFeedbackErrorCode());
        }
        builder.append("{\nip=");
        builder.append(this.ip).append("; \nmac=").append(this.mac).append("; \nov=").append(this.osVersionCode).append("; \nm=").append(this.model).append("; \nav=").append(this.appVersion).append("; \nbt=").append(this.packageTime).append("; \nt=").append(this.time);
        if (!StringUtils.isEmpty(this.exceptionName)) {
            builder.append("; \nex=").append(this.exceptionName);
        }
        if (!StringUtils.isEmpty(this.errorCode)) {
            builder.append("; \nec=").append(this.errorCode);
        }
        if (!StringUtils.isEmpty(this.url)) {
            if (this.url.length() > LogRecordUtils.errUrlLength) {
                this.url = this.url.substring(0, LogRecordUtils.errUrlLength);
            }
            builder.append("; \nurl=").append(this.url);
        }
        if (!StringUtils.isEmpty(this.custom)) {
            builder.append("; \ncustom=").append(this.custom);
        }
        builder.append("\n}");
        return builder.toString();
    }

    public Map<String, String> getQRMap(String code, String time, String ip, Context mContext) {
        Map<String, String> map = new HashMap();
        map.put("fbid", code);
        map.put("time", time);
        map.put(WebConstants.IP, ip);
        map.put(Keys.PLATFORM, InterfaceKey.EPG_FB);
        if (!StringUtils.isEmpty(this.errorCode)) {
            map.put("errcode", this.errorCode);
        }
        if (!StringUtils.isEmpty(this.url)) {
            Log.v("FeedBackModel", "mApiException.getUrl() length = " + this.url.length());
            Log.v("FeedBackModel", "mApiException.getUrl() = " + this.url);
            if (this.url.length() > LogRecordUtils.errUrlLength) {
                this.url = this.url.substring(0, LogRecordUtils.errUrlLength);
            }
            Log.v("FeedBackModel", "url subString  = " + this.url);
            map.put(Keys.ERRURL, this.url);
        }
        LogRecordUtils.getDevicesInfoForQR(map, mContext);
        return map;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\nip=");
        builder.append(this.ip).append("; \nmac=").append(this.mac).append("; \nov=").append(this.osVersionCode).append("; \nm=").append(this.model).append("; \nav=").append(this.appVersion).append("; \nbt=").append(this.packageTime).append("; \nt=").append(this.time);
        if (!StringUtils.isEmpty(this.exceptionName)) {
            builder.append("; \nex=").append(this.exceptionName);
        }
        if (!StringUtils.isEmpty(this.errorCode)) {
            builder.append("; \nec=").append(this.errorCode);
        }
        if (!StringUtils.isEmpty(this.url)) {
            builder.append("; \nurl=").append(this.url);
        }
        if (!StringUtils.isEmpty(this.custom)) {
            builder.append("; \ncustom=").append(this.custom);
        }
        builder.append("\n}");
        return builder.toString();
    }

    public boolean isShowQR() {
        return this.isShowQR;
    }

    public void setShowQR(boolean isShowQR) {
        this.isShowQR = isShowQR;
    }

    public boolean isNeedLogcat() {
        return this.mNeedLogcat;
    }

    public void setNeedLogcat(boolean needLogcat) {
        this.mNeedLogcat = needLogcat;
    }

    public TrackerRecord getRecord() {
        return this.mRecord;
    }

    public void setRecord(TrackerRecord record) {
        this.mRecord = record;
    }

    public void setSDKError(ISdkError error) {
        this.mSDKError = error;
    }

    public ISdkError getSDKError() {
        return this.mSDKError;
    }
}
