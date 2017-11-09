package com.gala.tvapi.tv2.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.provider.Settings.System;
import com.gala.tvapi.b.a;
import com.gala.tvapi.b.c;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.api.ApiEngine;
import java.util.Stack;
import org.cybergarage.soap.SOAP;

public class TVApiProperty extends ITVApiProperty {
    public static final String APIKEY_PLACEHOLDER = "APIKEY";
    public static final String AUTHID_PLACEHOLDER = "AUTHID";
    public static final int SDK_VERSION_6 = 23;
    private long a = 0;
    private Context f182a = null;
    private PlatformType f183a = PlatformType.NORMAL;
    private String f184a = "";
    private Stack<Boolean> f185a = new Stack();
    private boolean f186a = false;
    private String[] f187a;
    private long b = 0;
    private String f188b = "";
    private boolean f189b = false;
    private String c = "";
    private boolean f190c = false;
    private String d = "";
    private boolean f191d = false;
    private String e = "";
    private boolean f192e = true;
    private String f = "AUTHID";
    private boolean f193f = true;
    private String g = "";
    private boolean f194g = true;
    private String h = VERSION.RELEASE.toString();
    private String i = "APIKEY";
    private String j = "";
    private String k = BuildDefaultDocument.APK_DOMAIN_NAME;
    private String l = "";
    private String m;
    private String n = "";
    private String o = "";
    private String p = "";
    private String q = null;
    private String r = "";
    private String s = "";

    public void setCacheDeviceCheckFlag(boolean flag) {
        this.f186a = flag;
    }

    public boolean isCacheDeviceCheck() {
        return this.f186a;
    }

    public void setPlatform(PlatformType type) {
        this.f183a = type;
        c.a();
    }

    public PlatformType getPlatform() {
        return this.f183a;
    }

    public void setShowVipFlag(boolean isShowVip) {
        this.f189b = isShowVip;
    }

    public boolean isShowVip() {
        return this.f189b;
    }

    public void setShowLiveFlag(boolean isShowLive) {
        this.f190c = isShowLive;
    }

    public boolean isShowLive() {
        return this.f190c;
    }

    public void setMacAddress(String mac) {
        this.f184a = mac;
    }

    public String getMacAddress() {
        if (a.a(this.f184a)) {
            com.gala.tvapi.log.a.b("Check MacAddress", "mac address is " + this.f184a);
        }
        return this.f184a;
    }

    public void setVersion(String version) {
        if (version != null) {
            if (version.split("\\.").length >= 4) {
                this.f188b = version;
            } else {
                this.f188b = version + ".0";
            }
        }
        ApiEngine.get().setClientVersion(this.f188b);
    }

    public String getVersion() {
        return this.f188b;
    }

    public void setUUID(String uuid) {
        this.c = uuid;
        if (this.f182a == null) {
            return;
        }
        if (VERSION.SDK_INT < 23) {
            System.putString(this.f182a.getContentResolver(), "itv.uuid", uuid);
            return;
        }
        Editor edit = this.f182a.getSharedPreferences("tvapi", 0).edit();
        edit.putString("itv.uuid", uuid);
        edit.commit();
    }

    public String getUUID() {
        return this.c;
    }

    public void setRegisterKey(String key) {
        this.d = key;
    }

    public String getRegisterKey() {
        return this.d;
    }

    public void setSecretKey(String key) {
        this.e = key;
    }

    public String getSecretKey() {
        return this.e;
    }

    @SuppressLint({"DefaultLocale"})
    public String getYinHeInfo() {
        return ITVApiProperty.a(this.f184a.toLowerCase() + ",dfc18b4dbbc14d52b914c7724ba8a459");
    }

    public void setAuthId(String id) {
        this.f = id;
    }

    public String getAuthId() {
        return this.f;
    }

    public boolean checkAuthIdAndApiKeyAvailable() {
        if (this.i.equals("APIKEY") || this.f.equals("AUTHID")) {
            return false;
        }
        return true;
    }

    public void initAuthIdAndApiKey() {
        this.i = "APIKEY";
        this.f = "AUTHID";
    }

    public void setCheckYinHe(boolean flag) {
        this.f191d = flag;
    }

    public boolean isCheckYinHe() {
        return this.f191d;
    }

    public void setContext(Context context) {
        this.f182a = context;
        com.gala.tvapi.a.a.a().a(context);
    }

    public Context getContext() {
        return this.f182a;
    }

    public void setEncodeM3u8LocalFlag(boolean z) {
    }

    public void setHideString(String hide) {
        this.g = hide;
    }

    public String getHideString() {
        return this.g;
    }

    public void setOverSeaFlag(boolean flag) {
        this.f185a.push(Boolean.valueOf(flag));
    }

    public void clearOverSeaFlag() {
        if (this.f185a.size() > 0) {
            this.f185a.pop();
        }
    }

    public boolean isOpenOverSea() {
        if (this.f185a.size() == 0) {
            return false;
        }
        return ((Boolean) this.f185a.peek()).booleanValue();
    }

    public void setOSVersion(String os) {
        this.h = os;
    }

    public String getOSVersion() {
        return this.h;
    }

    public void setDebugFlag(boolean flag) {
        com.gala.tvapi.log.a.a(flag);
    }

    public boolean isDebugEnable() {
        return com.gala.tvapi.log.a.a();
    }

    public void setApiKey(String key) {
        this.i = key;
    }

    public String getApiKey() {
        return this.i;
    }

    public void setSendLogRecordFlag(boolean flag) {
        this.f192e = flag;
    }

    public boolean isSendLogRecord() {
        return this.f192e;
    }

    public void setServerTime(long time) {
        this.a = time;
    }

    public long getServerTime() {
        return this.a;
    }

    public void setLaunchTime(long time) {
        this.b = time;
    }

    public long getCurrentTime() {
        if (this.a == 0) {
            return System.currentTimeMillis();
        }
        return this.a + (SystemClock.elapsedRealtime() - this.b);
    }

    @SuppressLint({"DefaultLocale"})
    public String getAnonymity(String localMacAddr, String wifiMacAddr) {
        if (a.a(this.j)) {
            if (a.a(localMacAddr)) {
                if (a.a(wifiMacAddr)) {
                    this.j = "tv_" + a.a("00:00:00:00:00:00".replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
                } else {
                    this.j = "tv_" + a.a(wifiMacAddr.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
                    return this.j;
                }
            }
            this.j = "tv_" + a.a(localMacAddr.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
            return this.j;
        }
        return this.j;
    }

    @SuppressLint({"DefaultLocale"})
    public String getAnonymity() {
        String str = this.f184a;
        if (a.a(str)) {
            str = "00:00:00:00:00:00";
        }
        return "tv_" + a.a(str.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
    }

    public void setPlayerAreaControlByPhone(boolean flag) {
        this.f193f = flag;
    }

    public void setPlayerAuthVipByPhone(boolean flag) {
        this.f194g = flag;
    }

    public boolean isPlayerAreaControlByPhone() {
        return this.f193f;
    }

    public boolean isPlayerAuthVipByPhone() {
        return this.f194g;
    }

    public void setDomain(String s) {
        if (s != null && !s.isEmpty()) {
            this.k = s;
        }
    }

    public String getDomain() {
        return this.k;
    }

    @SuppressLint({"DefaultLocale"})
    public String getPassportDeviceId() {
        com.gala.tvapi.log.a.a("TVApiProperty", "read passport-device-id=" + this.m);
        if (this.m != null && !this.m.isEmpty()) {
            return this.m;
        }
        if (this.f182a != null) {
            if (VERSION.SDK_INT < 23) {
                this.m = System.getString(this.f182a.getContentResolver(), "itv.passport.deviceid");
                if (this.m == null || this.m.isEmpty()) {
                    this.m = getAnonymity() + "_" + System.currentTimeMillis();
                    System.putString(this.f182a.getContentResolver(), "itv.passport.deviceid", this.m);
                    this.m = System.getString(this.f182a.getContentResolver(), "itv.passport.deviceid");
                    com.gala.tvapi.log.a.a("TVApiProperty", "set passport-device-id=" + this.m);
                } else {
                    com.gala.tvapi.log.a.a("TVApiProperty", "passport-device-id-system=" + this.m);
                }
            } else {
                SharedPreferences sharedPreferences = this.f182a.getSharedPreferences("tvapi", 0);
                this.m = sharedPreferences.getString("itv.passport.deviceid", null);
                if (this.m == null || this.m.isEmpty()) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("itv.passport.deviceid", getAnonymity() + "_" + System.currentTimeMillis());
                    edit.commit();
                }
                this.m = sharedPreferences.getString("itv.passport.deviceid", null);
                com.gala.tvapi.log.a.a("TVApiProperty", "passport-device-id-sharepreference=" + this.m);
            }
        }
        return this.m;
    }

    public boolean isDeviceCheckParamsAvailable() {
        com.gala.tvapi.log.a.a("DeviceCheck Params", "mac=" + this.f184a + "  uuid=" + this.c + " version=" + this.f188b);
        if (!a.a(this.f184a)) {
            if (!a.a(this.c)) {
                if (!a.a(this.f188b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String[] getIpLoc() {
        return this.f187a;
    }

    public void setIpLoc(String[] ipLoc) {
        this.f187a = ipLoc;
    }

    public void setUid(String uid) {
        this.l = uid;
    }

    public String getUid() {
        return this.l;
    }

    public String getIpAddress() {
        return this.n;
    }

    public void setIpAddress(String ipAddress) {
        this.n = ipAddress;
    }

    public String getMemorySize() {
        return this.o;
    }

    public void setMemorySize(String memorySize) {
        this.o = memorySize;
    }

    public String getHardware() {
        return this.p;
    }

    public void setHardware(String hardware) {
        this.p = hardware;
    }

    public String getPassportId() {
        return this.q;
    }

    public void setPassportId(String passportId) {
        this.q = passportId;
    }

    public String getIpAddress_server() {
        return this.r.equals("") ? this.n : this.r;
    }

    public void setIpAddress_server(String ipAddress_server) {
        this.r = ipAddress_server;
    }

    public String getHostVersion() {
        return this.s;
    }

    public void setHostVersion(String hostVersion) {
        this.s = hostVersion;
    }
}
