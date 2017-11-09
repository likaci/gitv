package com.push.pushservice.data;

import android.content.Context;
import com.push.pushservice.constants.DataConst.LocalArea;

public class BasicPushInitParam {
    private short appId;
    private String appVer;
    private String clientId;
    private Context context;
    private String deviceId;
    private short keplerAppId;
    private LocalArea localArea;
    private String p2;
    private String packageName;
    private String passPortId;
    private int platForm;

    public BasicPushInitParam(Context context, short appId, String packageName, String appVer, String deviceId, String passPortId, String p2, String clientId, LocalArea localArea, short keplerAppId, int platForm) {
        this.context = context;
        this.appId = appId;
        this.packageName = packageName;
        this.appVer = appVer;
        this.deviceId = deviceId;
        this.passPortId = passPortId;
        this.p2 = p2;
        this.clientId = clientId;
        this.localArea = localArea;
        this.keplerAppId = keplerAppId;
        this.platForm = platForm;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public short getAppId() {
        return this.appId;
    }

    public void setAppId(short appId) {
        this.appId = appId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppVer() {
        return this.appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassPortId() {
        return this.passPortId;
    }

    public void setPassPortId(String passPortId) {
        this.passPortId = passPortId;
    }

    public String getP2() {
        return this.p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalArea getLocalArea() {
        return this.localArea;
    }

    public void setLocalArea(LocalArea localArea) {
        this.localArea = localArea;
    }

    public short getKeplerAppId() {
        return this.keplerAppId;
    }

    public void setKeplerAppId(short keplerAppId) {
        this.keplerAppId = keplerAppId;
    }

    public int getPlatForm() {
        return this.platForm;
    }

    public void setPlatForm(int platForm) {
        this.platForm = platForm;
    }
}
