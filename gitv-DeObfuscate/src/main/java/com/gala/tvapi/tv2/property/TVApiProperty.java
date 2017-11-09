package com.gala.tvapi.tv2.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.provider.Settings.System;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p006a.C0211a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.api.ApiEngine;
import java.util.Stack;
import org.cybergarage.soap.SOAP;

public class TVApiProperty extends ITVApiProperty {
    public static final String APIKEY_PLACEHOLDER = "APIKEY";
    public static final String AUTHID_PLACEHOLDER = "AUTHID";
    public static final int SDK_VERSION_6 = 23;
    private long f1017a = 0;
    private Context f1018a = null;
    private PlatformType f1019a = PlatformType.NORMAL;
    private String f1020a = "";
    private Stack<Boolean> f1021a = new Stack();
    private boolean f1022a = false;
    private String[] f1023a;
    private long f1024b = 0;
    private String f1025b = "";
    private boolean f1026b = false;
    private String f1027c = "";
    private boolean f1028c = false;
    private String f1029d = "";
    private boolean f1030d = false;
    private String f1031e = "";
    private boolean f1032e = true;
    private String f1033f = "AUTHID";
    private boolean f1034f = true;
    private String f1035g = "";
    private boolean f1036g = true;
    private String f1037h = VERSION.RELEASE.toString();
    private String f1038i = "APIKEY";
    private String f1039j = "";
    private String f1040k = BuildDefaultDocument.APK_DOMAIN_NAME;
    private String f1041l = "";
    private String f1042m;
    private String f1043n = "";
    private String f1044o = "";
    private String f1045p = "";
    private String f1046q = null;
    private String f1047r = "";
    private String f1048s = "";

    public void setCacheDeviceCheckFlag(boolean flag) {
        this.f1022a = flag;
    }

    public boolean isCacheDeviceCheck() {
        return this.f1022a;
    }

    public void setPlatform(PlatformType type) {
        this.f1019a = type;
        C0218c.m606a();
    }

    public PlatformType getPlatform() {
        return this.f1019a;
    }

    public void setShowVipFlag(boolean isShowVip) {
        this.f1026b = isShowVip;
    }

    public boolean isShowVip() {
        return this.f1026b;
    }

    public void setShowLiveFlag(boolean isShowLive) {
        this.f1028c = isShowLive;
    }

    public boolean isShowLive() {
        return this.f1028c;
    }

    public void setMacAddress(String mac) {
        this.f1020a = mac;
    }

    public String getMacAddress() {
        if (C0214a.m592a(this.f1020a)) {
            C0262a.m634b("Check MacAddress", "mac address is " + this.f1020a);
        }
        return this.f1020a;
    }

    public void setVersion(String version) {
        if (version != null) {
            if (version.split("\\.").length >= 4) {
                this.f1025b = version;
            } else {
                this.f1025b = version + ".0";
            }
        }
        ApiEngine.get().setClientVersion(this.f1025b);
    }

    public String getVersion() {
        return this.f1025b;
    }

    public void setUUID(String uuid) {
        this.f1027c = uuid;
        if (this.f1018a == null) {
            return;
        }
        if (VERSION.SDK_INT < 23) {
            System.putString(this.f1018a.getContentResolver(), "itv.uuid", uuid);
            return;
        }
        Editor edit = this.f1018a.getSharedPreferences("tvapi", 0).edit();
        edit.putString("itv.uuid", uuid);
        edit.commit();
    }

    public String getUUID() {
        return this.f1027c;
    }

    public void setRegisterKey(String key) {
        this.f1029d = key;
    }

    public String getRegisterKey() {
        return this.f1029d;
    }

    public void setSecretKey(String key) {
        this.f1031e = key;
    }

    public String getSecretKey() {
        return this.f1031e;
    }

    @SuppressLint({"DefaultLocale"})
    public String getYinHeInfo() {
        return ITVApiProperty.m702a(this.f1020a.toLowerCase() + ",dfc18b4dbbc14d52b914c7724ba8a459");
    }

    public void setAuthId(String id) {
        this.f1033f = id;
    }

    public String getAuthId() {
        return this.f1033f;
    }

    public boolean checkAuthIdAndApiKeyAvailable() {
        if (this.f1038i.equals("APIKEY") || this.f1033f.equals("AUTHID")) {
            return false;
        }
        return true;
    }

    public void initAuthIdAndApiKey() {
        this.f1038i = "APIKEY";
        this.f1033f = "AUTHID";
    }

    public void setCheckYinHe(boolean flag) {
        this.f1030d = flag;
    }

    public boolean isCheckYinHe() {
        return this.f1030d;
    }

    public void setContext(Context context) {
        this.f1018a = context;
        C0211a.m566a().m569a(context);
    }

    public Context getContext() {
        return this.f1018a;
    }

    public void setEncodeM3u8LocalFlag(boolean z) {
    }

    public void setHideString(String hide) {
        this.f1035g = hide;
    }

    public String getHideString() {
        return this.f1035g;
    }

    public void setOverSeaFlag(boolean flag) {
        this.f1021a.push(Boolean.valueOf(flag));
    }

    public void clearOverSeaFlag() {
        if (this.f1021a.size() > 0) {
            this.f1021a.pop();
        }
    }

    public boolean isOpenOverSea() {
        if (this.f1021a.size() == 0) {
            return false;
        }
        return ((Boolean) this.f1021a.peek()).booleanValue();
    }

    public void setOSVersion(String os) {
        this.f1037h = os;
    }

    public String getOSVersion() {
        return this.f1037h;
    }

    public void setDebugFlag(boolean flag) {
        C0262a.m632a(flag);
    }

    public boolean isDebugEnable() {
        return C0262a.m633a();
    }

    public void setApiKey(String key) {
        this.f1038i = key;
    }

    public String getApiKey() {
        return this.f1038i;
    }

    public void setSendLogRecordFlag(boolean flag) {
        this.f1032e = flag;
    }

    public boolean isSendLogRecord() {
        return this.f1032e;
    }

    public void setServerTime(long time) {
        this.f1017a = time;
    }

    public long getServerTime() {
        return this.f1017a;
    }

    public void setLaunchTime(long time) {
        this.f1024b = time;
    }

    public long getCurrentTime() {
        if (this.f1017a == 0) {
            return System.currentTimeMillis();
        }
        return this.f1017a + (SystemClock.elapsedRealtime() - this.f1024b);
    }

    @SuppressLint({"DefaultLocale"})
    public String getAnonymity(String localMacAddr, String wifiMacAddr) {
        if (C0214a.m592a(this.f1039j)) {
            if (C0214a.m592a(localMacAddr)) {
                if (C0214a.m592a(wifiMacAddr)) {
                    this.f1039j = "tv_" + C0214a.m580a("00:00:00:00:00:00".replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
                } else {
                    this.f1039j = "tv_" + C0214a.m580a(wifiMacAddr.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
                    return this.f1039j;
                }
            }
            this.f1039j = "tv_" + C0214a.m580a(localMacAddr.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
            return this.f1039j;
        }
        return this.f1039j;
    }

    @SuppressLint({"DefaultLocale"})
    public String getAnonymity() {
        String str = this.f1020a;
        if (C0214a.m592a(str)) {
            str = "00:00:00:00:00:00";
        }
        return "tv_" + C0214a.m580a(str.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
    }

    public void setPlayerAreaControlByPhone(boolean flag) {
        this.f1034f = flag;
    }

    public void setPlayerAuthVipByPhone(boolean flag) {
        this.f1036g = flag;
    }

    public boolean isPlayerAreaControlByPhone() {
        return this.f1034f;
    }

    public boolean isPlayerAuthVipByPhone() {
        return this.f1036g;
    }

    public void setDomain(String s) {
        if (s != null && !s.isEmpty()) {
            this.f1040k = s;
        }
    }

    public String getDomain() {
        return this.f1040k;
    }

    @SuppressLint({"DefaultLocale"})
    public String getPassportDeviceId() {
        C0262a.m629a("TVApiProperty", "read passport-device-id=" + this.f1042m);
        if (this.f1042m != null && !this.f1042m.isEmpty()) {
            return this.f1042m;
        }
        if (this.f1018a != null) {
            if (VERSION.SDK_INT < 23) {
                this.f1042m = System.getString(this.f1018a.getContentResolver(), "itv.passport.deviceid");
                if (this.f1042m == null || this.f1042m.isEmpty()) {
                    this.f1042m = getAnonymity() + "_" + System.currentTimeMillis();
                    System.putString(this.f1018a.getContentResolver(), "itv.passport.deviceid", this.f1042m);
                    this.f1042m = System.getString(this.f1018a.getContentResolver(), "itv.passport.deviceid");
                    C0262a.m629a("TVApiProperty", "set passport-device-id=" + this.f1042m);
                } else {
                    C0262a.m629a("TVApiProperty", "passport-device-id-system=" + this.f1042m);
                }
            } else {
                SharedPreferences sharedPreferences = this.f1018a.getSharedPreferences("tvapi", 0);
                this.f1042m = sharedPreferences.getString("itv.passport.deviceid", null);
                if (this.f1042m == null || this.f1042m.isEmpty()) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("itv.passport.deviceid", getAnonymity() + "_" + System.currentTimeMillis());
                    edit.commit();
                }
                this.f1042m = sharedPreferences.getString("itv.passport.deviceid", null);
                C0262a.m629a("TVApiProperty", "passport-device-id-sharepreference=" + this.f1042m);
            }
        }
        return this.f1042m;
    }

    public boolean isDeviceCheckParamsAvailable() {
        C0262a.m629a("DeviceCheck Params", "mac=" + this.f1020a + "  uuid=" + this.f1027c + " version=" + this.f1025b);
        if (!C0214a.m592a(this.f1020a)) {
            if (!C0214a.m592a(this.f1027c)) {
                if (!C0214a.m592a(this.f1025b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String[] getIpLoc() {
        return this.f1023a;
    }

    public void setIpLoc(String[] ipLoc) {
        this.f1023a = ipLoc;
    }

    public void setUid(String uid) {
        this.f1041l = uid;
    }

    public String getUid() {
        return this.f1041l;
    }

    public String getIpAddress() {
        return this.f1043n;
    }

    public void setIpAddress(String ipAddress) {
        this.f1043n = ipAddress;
    }

    public String getMemorySize() {
        return this.f1044o;
    }

    public void setMemorySize(String memorySize) {
        this.f1044o = memorySize;
    }

    public String getHardware() {
        return this.f1045p;
    }

    public void setHardware(String hardware) {
        this.f1045p = hardware;
    }

    public String getPassportId() {
        return this.f1046q;
    }

    public void setPassportId(String passportId) {
        this.f1046q = passportId;
    }

    public String getIpAddress_server() {
        return this.f1047r.equals("") ? this.f1043n : this.f1047r;
    }

    public void setIpAddress_server(String ipAddress_server) {
        this.f1047r = ipAddress_server;
    }

    public String getHostVersion() {
        return this.f1048s;
    }

    public void setHostVersion(String hostVersion) {
        this.f1048s = hostVersion;
    }
}
