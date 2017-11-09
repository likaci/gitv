package com.gala.video.lib.share.uikit.action.data;

import java.io.Serializable;

public class AppActionData implements Serializable {
    private String mAppDownloadUrl;
    private int mAppId;
    private String mAppName;
    private String mAppPackageName;
    private int mApplicationType;

    public void setAppPackageName(String appPackageName) {
        this.mAppPackageName = appPackageName;
    }

    public void setAppId(int appId) {
        this.mAppId = appId;
    }

    public void setApplicationType(int applicationType) {
        this.mApplicationType = applicationType;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.mAppDownloadUrl = appDownloadUrl;
    }

    public void setAppName(String appName) {
        this.mAppName = appName;
    }

    public String getAppPackageName() {
        return this.mAppPackageName;
    }

    public int getAppId() {
        return this.mAppId;
    }

    public int getApplicationType() {
        return this.mApplicationType;
    }

    public String getAppDownloadUrl() {
        return this.mAppDownloadUrl;
    }

    public String getAppName() {
        return this.mAppName;
    }
}
