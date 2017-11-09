package com.push.pushservice.data;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String appVer;
    private short appid;
    private String deviceId;
    private boolean isRegister;
    private String packageName;

    public AppInfo(short appid, String deviceId, String packageName, String appVer) {
        this.appid = appid;
        this.deviceId = deviceId;
        this.packageName = packageName;
        this.appVer = appVer;
        this.isRegister = false;
    }

    public AppInfo(short appid, String deviceId, String packageName, String appVer, boolean isRegister) {
        this.appid = appid;
        this.deviceId = deviceId;
        this.packageName = packageName;
        this.appVer = appVer;
        this.isRegister = isRegister;
    }

    public String getAppVer() {
        return this.appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public boolean isRegister() {
        return this.isRegister;
    }

    public void setRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }

    public short getAppid() {
        return this.appid;
    }

    public void setAppid(short appid) {
        this.appid = appid;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean getIsRegister() {
        return this.isRegister;
    }

    public void setIsRegister(boolean isregister) {
        this.isRegister = isregister;
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("AppInfo : ");
        strBuild.append("appid = " + getAppid());
        strBuild.append(", deviceID = " + getDeviceId());
        strBuild.append(", packageName = " + getPackageName());
        strBuild.append(", appVer = " + getAppVer());
        strBuild.append(", isRegister = " + getIsRegister());
        return strBuild.toString();
    }
}
