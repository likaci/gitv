package com.mcto.ads.internal.common;

public class CupidGlobal {
    private static String _pingbackUrl;
    private String appVersion = null;
    private String cupidUserId = null;
    private String mobileKey = null;
    private String sdkVersion = null;
    private String uaaUserId = null;
    private int vvProgress = 0;

    public void setUaaUserId(String uaaUserId) {
        this.uaaUserId = uaaUserId;
    }

    public void setCupidUserId(String cupidUserId) {
        this.cupidUserId = cupidUserId;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getUaaUserId() {
        return this.uaaUserId;
    }

    public String getCupidUserId() {
        return this.cupidUserId;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public String getMobileKey() {
        return this.mobileKey;
    }

    public void setMobileKey(String mobileKey) {
        this.mobileKey = mobileKey;
    }

    public int getVVProgress() {
        return this.vvProgress;
    }

    public void setVVProgress(int vvProgress) {
        this.vvProgress = vvProgress;
    }

    public static String getPingbackUrl() {
        return _pingbackUrl;
    }

    public static void setPingbackUrl(String pingbackUrl) {
        _pingbackUrl = pingbackUrl;
    }
}
