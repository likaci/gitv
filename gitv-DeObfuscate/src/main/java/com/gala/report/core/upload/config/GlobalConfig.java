package com.gala.report.core.upload.config;

public class GlobalConfig {
    private String androidModel = "";
    private String androidVersion = "";
    private String appVersion = "";
    private String hardwareInfo = "";
    private boolean hcdnStatus = false;
    private String record = "";
    private String uuid = "";

    public GlobalConfig setMac(String val) {
        this.record = val;
        return this;
    }

    public GlobalConfig setAndroidModel(String val) {
        this.androidModel = val;
        return this;
    }

    public GlobalConfig setAndroidVerion(String val) {
        this.androidVersion = val;
        return this;
    }

    public GlobalConfig setAppVersion(String val) {
        this.appVersion = val;
        return this;
    }

    public GlobalConfig setHardwareInfo(String val) {
        this.hardwareInfo = val;
        return this;
    }

    public GlobalConfig setUuid(String val) {
        this.uuid = val;
        return this;
    }

    public GlobalConfig setHcdnStatus(boolean val) {
        this.hcdnStatus = val;
        return this;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mac=" + this.record).append("&&");
        sb.append("app_version=" + this.appVersion).append("&&");
        sb.append("android_model=" + this.androidModel).append("&&");
        sb.append("android_version=" + this.androidVersion).append("&&");
        sb.append("hwer=" + this.hardwareInfo).append("&&");
        sb.append("stream=" + null).append("&&");
        sb.append("hcdn=" + this.hcdnStatus).append("&&");
        sb.append("uuid=" + this.uuid).append("&&");
        return sb.toString();
    }

    public String getRecord() {
        return this.record;
    }

    public String getAndroidModel() {
        return this.androidModel;
    }

    public String getAndroidVersion() {
        return this.androidVersion;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String getHardwareInfo() {
        return this.hardwareInfo;
    }

    public boolean isHcdnStatus() {
        return this.hcdnStatus;
    }

    public String getUuid() {
        return this.uuid;
    }
}
