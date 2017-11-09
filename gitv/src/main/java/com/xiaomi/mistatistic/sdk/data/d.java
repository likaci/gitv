package com.xiaomi.mistatistic.sdk.data;

import com.xiaomi.mistatistic.sdk.controller.o;
import java.util.Map;
import org.json.JSONObject;

public class d extends a {
    protected long b;
    private String c;
    private String d;
    private Map e;

    public d(String str, String str2, long j) {
        this(str, str2, j, null);
    }

    public d(String str, String str2, long j, Map map) {
        this.c = str;
        this.d = str2;
        this.b = j;
        this.e = map;
    }

    private String a(Map map) {
        if (map != null) {
            try {
                if (!map.isEmpty()) {
                    JSONObject jSONObject = new JSONObject();
                    for (String str : map.keySet()) {
                        jSONObject.put(str, map.get(str));
                    }
                    return jSONObject.toString();
                }
            } catch (Throwable e) {
                new o().a("json error", e);
            }
        }
        return null;
    }

    public h a() {
        h hVar = new h();
        hVar.a = this.c;
        hVar.c = this.d;
        hVar.b = this.a;
        hVar.d = b();
        hVar.e = String.valueOf(this.b);
        hVar.f = a(this.e);
        return hVar;
    }

    public String b() {
        return "numeric";
    }
}
