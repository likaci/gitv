package com.xiaomi.mistatistic.sdk.data;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.internal.net.PingbackConstants;
import com.xiaomi.mistatistic.sdk.controller.C2111a;
import com.xiaomi.mistatistic.sdk.controller.C2126q;
import org.json.JSONObject;

public class HttpEvent {
    private String exceptionName;
    private String net;
    private long netFlow;
    private String operator;
    private int responseCode;
    private long time;
    private long timeCost;
    private String url;

    public HttpEvent(String str, long j) {
        this(str, j, -1, null);
    }

    public HttpEvent(String str, long j, int i, String str2) {
        this(str, j, 0, i, str2);
    }

    public HttpEvent(String str, long j, long j2) {
        this(str, j, j2, -1, null);
    }

    public HttpEvent(String str, long j, long j2, int i) {
        this(str, j, j2, i, null);
    }

    public HttpEvent(String str, long j, long j2, int i, String str2) {
        this.time = System.currentTimeMillis();
        this.netFlow = 0;
        this.url = str;
        this.timeCost = j;
        this.responseCode = i;
        this.exceptionName = str2;
        this.netFlow = j2;
        setNet();
    }

    public HttpEvent(String str, String str2) {
        this(str, -1, -1, str2);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HttpEvent)) {
            return false;
        }
        HttpEvent httpEvent = (HttpEvent) obj;
        return TextUtils.equals(this.url, httpEvent.url) && TextUtils.equals(this.net, httpEvent.net) && TextUtils.equals(this.exceptionName, httpEvent.exceptionName) && this.responseCode == httpEvent.responseCode && this.timeCost == httpEvent.timeCost && this.time == httpEvent.time && this.netFlow == httpEvent.netFlow;
    }

    public String getUrl() {
        return this.url;
    }

    public void setNet() {
        String b = C2126q.m1847b(C2111a.m1779a());
        if (TextUtils.isEmpty(b)) {
            this.net = "NULL";
            return;
        }
        this.net = b;
        if (!"WIFI".equalsIgnoreCase(b)) {
            this.operator = ((TelephonyManager) C2111a.m1779a().getSystemService("phone")).getSimOperator();
        }
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("net", this.net);
        if (this.timeCost > 0) {
            jSONObject.put("cost", this.timeCost);
        }
        if (this.responseCode != -1) {
            jSONObject.put(PingbackConstants.CODE, this.responseCode);
        }
        if (!TextUtils.isEmpty(this.exceptionName)) {
            jSONObject.put("exception", this.exceptionName);
        }
        if (!TextUtils.isEmpty(this.operator)) {
            jSONObject.put("op", this.operator);
        }
        if (this.netFlow > 0) {
            jSONObject.put("flow", this.netFlow);
        }
        jSONObject.put(Keys.f2035T, this.time);
        return jSONObject;
    }
}
