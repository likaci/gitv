package com.xiaomi.mistatistic.sdk.controller.p037a;

import android.database.Cursor;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.xiaomi.mistatistic.sdk.controller.C2105e;
import com.xiaomi.mistatistic.sdk.controller.C2118i;
import com.xiaomi.mistatistic.sdk.controller.C2124o;
import com.xiaomi.mistatistic.sdk.data.C2144h;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class C2107b implements C2105e {
    private long f2167a;
    private HashMap f2168b = new HashMap();
    private long f2169c = System.currentTimeMillis();
    private long f2170d = 0;
    private JSONArray f2171e = new JSONArray();
    private JSONObject f2172f = null;
    private ArrayList f2173g = new ArrayList();
    private C2108c f2174h;
    private HashMap f2175i = new HashMap();

    public C2107b(long j, C2108c c2108c) {
        this.f2167a = j;
        this.f2174h = c2108c;
    }

    private void m1771a(C2144h c2144h) {
        JSONObject jSONObject = (JSONObject) this.f2168b.get("mistat_session");
        if (jSONObject == null) {
            JSONArray jSONArray = new JSONArray();
            jSONObject = new JSONObject();
            jSONObject.put("category", "mistat_session");
            jSONObject.put("values", jSONArray);
            this.f2168b.put("mistat_session", jSONObject);
            this.f2172f.getJSONArray("content").put(jSONObject);
        }
        JSONObject jSONObject2 = new JSONObject();
        String[] split = c2144h.f2246e.split(",");
        long parseLong = Long.parseLong(split[0]);
        long parseLong2 = Long.parseLong(split[1]);
        jSONObject2.put("start", parseLong);
        jSONObject2.put("end", parseLong2);
        jSONObject2.put("env", c2144h.f2247f);
        jSONObject.getJSONArray("values").put(jSONObject2);
    }

    private void m1772b(C2144h c2144h) {
        JSONObject jSONObject = (JSONObject) this.f2168b.get("mistat_pv");
        if (jSONObject == null) {
            jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONObject.put("category", "mistat_pv");
            jSONObject.put("values", jSONArray);
            this.f2168b.put("mistat_pv", jSONObject);
            this.f2172f.getJSONArray("content").put(jSONObject);
        }
        String[] split = c2144h.f2246e.trim().split(",");
        String[] strArr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            int indexOf = this.f2173g.indexOf(split[i]);
            if (indexOf >= 0) {
                strArr[i] = String.valueOf(indexOf + 1);
            } else {
                strArr[i] = String.valueOf(this.f2173g.size() + 1);
                this.f2173g.add(split[i]);
            }
        }
        jSONObject.getJSONArray("values").put(TextUtils.join(",", strArr));
        jSONObject.put("index", TextUtils.join(",", this.f2173g));
    }

    private void m1773c(C2144h c2144h) {
        JSONObject jSONObject;
        JSONObject jSONObject2 = (JSONObject) this.f2168b.get(c2144h.f2242a);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONObject2.put("category", c2144h.f2242a);
            jSONObject2.put("values", jSONArray);
            this.f2168b.put(c2144h.f2242a, jSONObject2);
            this.f2172f.getJSONArray("content").put(jSONObject2);
            jSONObject = jSONObject2;
        } else {
            jSONObject = jSONObject2;
        }
        if ("event".equals(c2144h.f2245d) && TextUtils.isEmpty(c2144h.f2247f)) {
            jSONObject2 = (JSONObject) this.f2175i.get(c2144h.f2244c);
            if (jSONObject2 != null) {
                jSONObject2.put("value", jSONObject2.getLong("value") + Long.parseLong(c2144h.f2246e));
                return;
            }
            jSONObject2 = new JSONObject();
            jSONObject2.put(Album.KEY, c2144h.f2244c);
            jSONObject2.put("type", c2144h.f2245d);
            jSONObject2.put("value", Long.parseLong(c2144h.f2246e));
            jSONObject.getJSONArray("values").put(jSONObject2);
            this.f2175i.put(c2144h.f2244c, jSONObject2);
            return;
        }
        jSONObject2 = new JSONObject();
        jSONObject2.put(Album.KEY, c2144h.f2244c);
        jSONObject2.put("type", c2144h.f2245d);
        if ("count".equals(c2144h.f2245d) || "numeric".equals(c2144h.f2245d)) {
            jSONObject2.put("value", Long.parseLong(c2144h.f2246e));
        } else {
            jSONObject2.put("value", c2144h.f2246e);
        }
        if (!TextUtils.isEmpty(c2144h.f2247f)) {
            jSONObject2.put(JsonBundleConstants.A71_TRACKING_PARAMS, new JSONObject(c2144h.f2247f));
        }
        jSONObject.getJSONArray("values").put(jSONObject2);
    }

    public void mo4534a() {
        try {
            JSONArray b = m1775b();
            if (b == null) {
                this.f2174h.mo4535a("", this.f2170d);
            } else {
                this.f2174h.mo4535a(b.toString(), this.f2170d);
            }
        } catch (JSONException e) {
            this.f2174h.mo4535a("", this.f2170d);
        }
    }

    public JSONArray m1775b() {
        C2124o c2124o = new C2124o();
        C2118i c2118i = new C2118i();
        c2118i.m1822c();
        Cursor b = c2118i.m1821b();
        c2124o.m1840a("Begin to packing data from local DB");
        int i = 0;
        if (b.moveToFirst()) {
            do {
                i++;
                C2144h a = C2118i.m1815a(b);
                c2124o.m1840a("Packing " + a.toString());
                if (this.f2170d == 0) {
                    this.f2170d = a.f2243b;
                    this.f2169c = this.f2170d;
                }
                if (this.f2167a > 0 && this.f2169c - a.f2243b > this.f2167a && this.f2172f != null) {
                    this.f2172f = null;
                    this.f2168b.clear();
                    this.f2173g.clear();
                    this.f2169c = a.f2243b;
                    this.f2175i.clear();
                }
                if (this.f2172f == null) {
                    this.f2172f = new JSONObject();
                    this.f2172f.put("endTS", a.f2243b);
                    this.f2172f.put("content", new JSONArray());
                    this.f2171e.put(this.f2172f);
                }
                if ("mistat_session".equals(a.f2242a)) {
                    m1771a(a);
                } else {
                    try {
                        if ("mistat_pv".equals(a.f2242a)) {
                            m1772b(a);
                        } else {
                            m1773c(a);
                        }
                    } catch (Throwable th) {
                        if (b != null) {
                            b.close();
                        }
                    }
                }
                this.f2172f.put("startTS", a.f2243b);
            } while (b.moveToNext());
            c2124o.m1840a("Packing complete, total " + i + " records were packed and to be uploaded");
        } else {
            c2124o.m1840a("No data available to be packed");
            this.f2171e = null;
        }
        if (b != null) {
            b.close();
        }
        return this.f2171e;
    }
}
