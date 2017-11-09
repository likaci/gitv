package com.gala.tvapi.tv3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import org.cybergarage.soap.SOAP;

public class TVApiConfig {
    private static final TVApiConfig f1065a = new TVApiConfig();
    private int f1066a;
    private Context f1067a;
    private String f1068a;
    private String f1069b;
    private String f1070c;
    private String f1071d;
    private String f1072e;
    private String f1073f;

    public static TVApiConfig get() {
        return f1065a;
    }

    private TVApiConfig() {
    }

    public String getUuid() {
        return this.f1068a;
    }

    public void setUuid(String uuid) {
        this.f1068a = uuid;
    }

    public String getMac() {
        return this.f1069b;
    }

    public void setMac(String mac) {
        this.f1069b = mac;
    }

    public String getApkVersion() {
        return this.f1070c;
    }

    public void setApkVersion(String apkVersion) {
        this.f1070c = apkVersion;
    }

    public Context getContext() {
        return this.f1067a;
    }

    public void setContext(Context context) {
        this.f1067a = context;
    }

    public String getAnonymity() {
        String str = this.f1069b;
        if (C0214a.m592a(str)) {
            str = "00:00:00:00:00:00";
        }
        return "tv_" + C0214a.m580a(str.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", ""));
    }

    public String getPassportId() {
        C0262a.m629a("TVApiProperty", "read passport-device-id=" + this.f1071d);
        if (this.f1071d != null && !this.f1071d.isEmpty()) {
            return this.f1071d;
        }
        if (this.f1067a != null) {
            if (VERSION.SDK_INT < 23) {
                this.f1071d = System.getString(this.f1067a.getContentResolver(), "itv.passport.deviceid");
                if (this.f1071d == null || this.f1071d.isEmpty()) {
                    this.f1071d = getAnonymity() + "_" + System.currentTimeMillis();
                    System.putString(this.f1067a.getContentResolver(), "itv.passport.deviceid", this.f1071d);
                    this.f1071d = System.getString(this.f1067a.getContentResolver(), "itv.passport.deviceid");
                    C0262a.m629a("TVApiProperty", "set passport-device-id=" + this.f1071d);
                } else {
                    C0262a.m629a("TVApiProperty", "passport-device-id-system=" + this.f1071d);
                }
            } else {
                SharedPreferences sharedPreferences = this.f1067a.getSharedPreferences("tvapi", 0);
                this.f1071d = sharedPreferences.getString("itv.passport.deviceid", null);
                if (this.f1071d == null || this.f1071d.isEmpty()) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("itv.passport.deviceid", getAnonymity() + "_" + System.currentTimeMillis());
                    edit.commit();
                }
                this.f1071d = sharedPreferences.getString("itv.passport.deviceid", null);
                C0262a.m629a("TVApiProperty", "passport-device-id-sharepreference=" + this.f1071d);
            }
        }
        return this.f1071d;
    }

    public String getHardware() {
        return this.f1072e;
    }

    public void setHardware(String hardware) {
        this.f1072e = hardware;
    }

    public int getMemorySize() {
        return this.f1066a;
    }

    public void setMemorySize(int memorySize) {
        this.f1066a = memorySize;
    }

    public String getHostVersion() {
        return this.f1073f;
    }

    public void setHostVersion(String hostVersion) {
        this.f1073f = hostVersion;
    }
}
