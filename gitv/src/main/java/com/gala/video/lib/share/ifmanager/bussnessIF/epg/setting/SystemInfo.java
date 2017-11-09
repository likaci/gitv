package com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting;

public class SystemInfo {
    private String mDeviceModel;
    private String mDeviceName;
    private String mIpAddr;
    private String mMac;
    private String mMacWifi;
    private String mSoftwareVersion;
    private String mSystemVersion;

    public SystemInfo(String deviceName, String deviceModel, String systemVersion, String softwareVersion, String ipAddr, String mac, String macWifi) {
        this.mDeviceName = deviceName;
        this.mDeviceModel = deviceModel;
        this.mSystemVersion = systemVersion;
        this.mSoftwareVersion = softwareVersion;
        this.mIpAddr = ipAddr;
        this.mMac = mac;
        this.mMacWifi = macWifi;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public String getDeviceModel() {
        return this.mDeviceModel;
    }

    public String getSystemVersion() {
        return this.mSystemVersion;
    }

    public String getSoftwareVersion() {
        return this.mSoftwareVersion;
    }

    public String getIpAddr() {
        return this.mIpAddr;
    }

    public String getMac() {
        return this.mMac;
    }

    public String getMacWifi() {
        return this.mMacWifi;
    }
}
