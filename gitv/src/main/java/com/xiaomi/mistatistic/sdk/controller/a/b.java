package com.xiaomi.mistatistic.sdk.controller.a;

import android.database.Cursor;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.xiaomi.mistatistic.sdk.controller.e;
import com.xiaomi.mistatistic.sdk.controller.i;
import com.xiaomi.mistatistic.sdk.controller.o;
import com.xiaomi.mistatistic.sdk.data.h;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class b implements e {
    private long a;
    private HashMap b = new HashMap();
    private long c = System.currentTimeMillis();
    private long d = 0;
    private JSONArray e = new JSONArray();
    private JSONObject f = null;
    private ArrayList g = new ArrayList();
    private c h;
    private HashMap i = new HashMap();

    public b(long j, c cVar) {
        this.a = j;
        this.h = cVar;
    }

    private void a(h hVar) {
        JSONObject jSONObject = (JSONObject) this.b.get("mistat_session");
        if (jSONObject == null) {
            JSONArray jSONArray = new JSONArray();
            jSONObject = new JSONObject();
            jSONObject.put("category", "mistat_session");
            jSONObject.put("values", jSONArray);
            this.b.put("mistat_session", jSONObject);
            this.f.getJSONArray("content").put(jSONObject);
        }
        JSONObject jSONObject2 = new JSONObject();
        String[] split = hVar.e.split(",");
        long parseLong = Long.parseLong(split[0]);
        long parseLong2 = Long.parseLong(split[1]);
        jSONObject2.put("start", parseLong);
        jSONObject2.put("end", parseLong2);
        jSONObject2.put("env", hVar.f);
        jSONObject.getJSONArray("values").put(jSONObject2);
    }

    private void b(h hVar) {
        JSONObject jSONObject = (JSONObject) this.b.get("mistat_pv");
        if (jSONObject == null) {
            jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONObject.put("category", "mistat_pv");
            jSONObject.put("values", jSONArray);
            this.b.put("mistat_pv", jSONObject);
            this.f.getJSONArray("content").put(jSONObject);
        }
        String[] split = hVar.e.trim().split(",");
        String[] strArr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            int indexOf = this.g.indexOf(split[i]);
            if (indexOf >= 0) {
                strArr[i] = String.valueOf(indexOf + 1);
            } else {
                strArr[i] = String.valueOf(this.g.size() + 1);
                this.g.add(split[i]);
            }
        }
        jSONObject.getJSONArray("values").put(TextUtils.join(",", strArr));
        jSONObject.put("index", TextUtils.join(",", this.g));
    }

    private void c(h hVar) {
        JSONObject jSONObject;
        JSONObject jSONObject2 = (JSONObject) this.b.get(hVar.a);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONObject2.put("category", hVar.a);
            jSONObject2.put("values", jSONArray);
            this.b.put(hVar.a, jSONObject2);
            this.f.getJSONArray("content").put(jSONObject2);
            jSONObject = jSONObject2;
        } else {
            jSONObject = jSONObject2;
        }
        if ("event".equals(hVar.d) && TextUtils.isEmpty(hVar.f)) {
            jSONObject2 = (JSONObject) this.i.get(hVar.c);
            if (jSONObject2 != null) {
                jSONObject2.put("value", jSONObject2.getLong("value") + Long.parseLong(hVar.e));
                return;
            }
            jSONObject2 = new JSONObject();
            jSONObject2.put(Album.KEY, hVar.c);
            jSONObject2.put("type", hVar.d);
            jSONObject2.put("value", Long.parseLong(hVar.e));
            jSONObject.getJSONArray("values").put(jSONObject2);
            this.i.put(hVar.c, jSONObject2);
            return;
        }
        jSONObject2 = new JSONObject();
        jSONObject2.put(Album.KEY, hVar.c);
        jSONObject2.put("type", hVar.d);
        if ("count".equals(hVar.d) || "numeric".equals(hVar.d)) {
            jSONObject2.put("value", Long.parseLong(hVar.e));
        } else {
            jSONObject2.put("value", hVar.e);
        }
        if (!TextUtils.isEmpty(hVar.f)) {
            jSONObject2.put(JsonBundleConstants.A71_TRACKING_PARAMS, new JSONObject(hVar.f));
        }
        jSONObject.getJSONArray("values").put(jSONObject2);
    }

    public void a() {
        try {
            JSONArray b = b();
            if (b == null) {
                this.h.a("", this.d);
            } else {
                this.h.a(b.toString(), this.d);
            }
        } catch (JSONException e) {
            this.h.a("", this.d);
        }
    }

    public JSONArray b() {
        o oVar = new o();
        i iVar = new i();
        iVar.c();
        Cursor b = iVar.b();
        oVar.a("Begin to packing data from local DB");
        int i = 0;
        if (b.moveToFirst()) {
            do {
                i++;
                h a = i.a(b);
                oVar.a("Packing " + a.toString());
                if (this.d == 0) {
                    this.d = a.b;
                    this.c = this.d;
                }
                if (this.a > 0 && this.c - a.b > this.a && this.f != null) {
                    this.f = null;
                    this.b.clear();
                    this.g.clear();
                    this.c = a.b;
                    this.i.clear();
                }
                if (this.f == null) {
                    this.f = new JSONObject();
                    this.f.put("endTS", a.b);
                    this.f.put("content", new JSONArray());
                    this.e.put(this.f);
                }
                if ("mistat_session".equals(a.a)) {
                    a(a);
                } else {
                    try {
                        if ("mistat_pv".equals(a.a)) {
                            b(a);
                        } else {
                            c(a);
                        }
                    } catch (Throwable th) {
                        if (b != null) {
                            b.close();
                        }
                    }
                }
                this.f.put("startTS", a.b);
            } while (b.moveToNext());
            oVar.a("Packing complete, total " + i + " records were packed and to be uploaded");
        } else {
            oVar.a("No data available to be packed");
            this.e = null;
        }
        if (b != null) {
            b.close();
        }
        return this.e;
    }
}
