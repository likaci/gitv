package com.gala.tvapi.tv3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import com.gala.tvapi.b.a;
import org.cybergarage.soap.SOAP;

public class TVApiConfig {
    private static final TVApiConfig a = new TVApiConfig();
    private int f510a;
    private Context f511a;
    private String f512a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;

    public static TVApiConfig get() {
        return a;
    }

    private TVApiConfig() {
    }

    public String getUuid() {
        return this.f512a;
    }

    public void setUuid(String uuid) {
        this.f512a = uuid;
    }

    public String getMac() {
        return this.b;
    }

    public void setMac(String mac) {
        this.b = mac;
    }

    public String getApkVersion() {
        return this.c;
    }

    public void setApkVersion(String apkVersion) {
        this.c = apkVersion;
    }

    public Context getContext() {
        return this.f511a;
    }

    public void setContext(Context context) {
        this.f511a = context;
    }

    public String getAnonymity() {
        String str = this.b;
        if (a.a(str)) {
            str = "00:00:00:00:00:00";
        }
        return "tv_" + a.a(str.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
    }

    public String getPassportId() {
        com.gala.tvapi.log.a.a("TVApiProperty", "read passport-device-id=" + this.d);
        if (this.d != null && !this.d.isEmpty()) {
            return this.d;
        }
        if (this.f511a != null) {
            if (VERSION.SDK_INT < 23) {
                this.d = System.getString(this.f511a.getContentResolver(), "itv.passport.deviceid");
                if (this.d == null || this.d.isEmpty()) {
                    this.d = getAnonymity() + "_" + System.currentTimeMillis();
                    System.putString(this.f511a.getContentResolver(), "itv.passport.deviceid", this.d);
                    this.d = System.getString(this.f511a.getContentResolver(), "itv.passport.deviceid");
                    com.gala.tvapi.log.a.a("TVApiProperty", "set passport-device-id=" + this.d);
                } else {
                    com.gala.tvapi.log.a.a("TVApiProperty", "passport-device-id-system=" + this.d);
                }
            } else {
                SharedPreferences sharedPreferences = this.f511a.getSharedPreferences("tvapi", 0);
                this.d = sharedPreferences.getString("itv.passport.deviceid", null);
                if (this.d == null || this.d.isEmpty()) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("itv.passport.deviceid", getAnonymity() + "_" + System.currentTimeMillis());
                    edit.commit();
                }
                this.d = sharedPreferences.getString("itv.passport.deviceid", null);
                com.gala.tvapi.log.a.a("TVApiProperty", "passport-device-id-sharepreference=" + this.d);
            }
        }
        return this.d;
    }

    public String getHardware() {
        return this.e;
    }

    public void setHardware(String hardware) {
        this.e = hardware;
    }

    public int getMemorySize() {
        return this.f510a;
    }

    public void setMemorySize(int memorySize) {
        this.f510a = memorySize;
    }

    public String getHostVersion() {
        return this.f;
    }

    public void setHostVersion(String hostVersion) {
        this.f = hostVersion;
    }
}
