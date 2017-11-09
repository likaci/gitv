package com.xiaomi.mistatistic.sdk.data;

import com.xiaomi.mistatistic.sdk.controller.C2124o;
import java.util.Map;
import org.json.JSONObject;

public class C2138d extends C2137a {
    protected long f2231b;
    private String f2232c;
    private String f2233d;
    private Map f2234e;

    public C2138d(String str, String str2, long j) {
        this(str, str2, j, null);
    }

    public C2138d(String str, String str2, long j, Map map) {
        this.f2232c = str;
        this.f2233d = str2;
        this.f2231b = j;
        this.f2234e = map;
    }

    private String m1882a(Map map) {
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
                new C2124o().m1841a("json error", e);
            }
        }
        return null;
    }

    public C2144h mo4537a() {
        C2144h c2144h = new C2144h();
        c2144h.f2242a = this.f2232c;
        c2144h.f2244c = this.f2233d;
        c2144h.f2243b = this.a;
        c2144h.f2245d = mo4538b();
        c2144h.f2246e = String.valueOf(this.f2231b);
        c2144h.f2247f = m1882a(this.f2234e);
        return c2144h;
    }

    public String mo4538b() {
        return "numeric";
    }
}
