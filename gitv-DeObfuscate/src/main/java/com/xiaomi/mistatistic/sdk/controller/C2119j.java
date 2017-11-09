package com.xiaomi.mistatistic.sdk.controller;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.push.pushservice.constants.DataConst;
import com.xiaomi.mistatistic.sdk.BuildSetting;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class C2119j {
    private static C2119j f2206a = new C2119j();
    private HttpEventFilter f2207b;
    private boolean f2208c = false;
    private List f2209d = new LinkedList();
    private Handler f2210e = new C2120k(this, Looper.getMainLooper());

    private C2119j() {
    }

    public static C2119j m1824a() {
        return f2206a;
    }

    private void m1825a(JSONObject jSONObject) {
        if (jSONObject.has("data")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("data");
            int optInt = jSONObject2.optInt("sample_rate", 10000);
            int optInt2 = jSONObject2.optInt("delay", UIKitConfig.ITEM_TYPE_HEADER);
            long optLong = jSONObject2.optLong("ban_time", 0);
            C2128s.m1854b(C2111a.m1779a(), "rt_upload_rate", optInt);
            C2128s.m1855b(C2111a.m1779a(), "rt_upload_delay", (long) optInt2);
            C2128s.m1855b(C2111a.m1779a(), "rt_ban_time", System.currentTimeMillis() + optLong);
        }
    }

    private String m1828e() {
        return BuildSetting.isTest() ? "http://10.99.168.145:8097/realtime_network" : "https://data.mistat.xiaomi.com/realtime_network";
    }

    private boolean m1829f() {
        Map hashMap = new HashMap();
        synchronized (this.f2209d) {
            for (HttpEvent httpEvent : this.f2209d) {
                CharSequence url = httpEvent.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    if (hashMap.containsKey(url)) {
                        ((List) hashMap.get(url)).add(httpEvent);
                    } else {
                        hashMap.put(url, new ArrayList());
                        ((List) hashMap.get(url)).add(httpEvent);
                    }
                }
            }
            if (hashMap.isEmpty()) {
                return false;
            }
            JSONArray jSONArray = new JSONArray();
            for (String str : hashMap.keySet()) {
                JSONArray jSONArray2 = new JSONArray();
                for (HttpEvent toJSON : (List) hashMap.get(str)) {
                    jSONArray2.put(toJSON.toJSON());
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("url", str);
                jSONObject.put("data", jSONArray2);
                jSONArray.put(jSONObject);
            }
            boolean a = m1832a(jSONArray.toString());
            return a;
        }
    }

    public void m1830a(HttpEventFilter httpEventFilter) {
        this.f2207b = httpEventFilter;
    }

    public void m1831a(HttpEvent httpEvent) {
        if (!httpEvent.getUrl().equals(m1828e()) || this.f2208c) {
            if (!(this.f2207b == null || httpEvent.getUrl().equals(m1828e()))) {
                httpEvent = this.f2207b.onEvent(httpEvent);
            }
            if (httpEvent != null && !TextUtils.isEmpty(httpEvent.getUrl())) {
                synchronized (this.f2209d) {
                    this.f2209d.add(httpEvent);
                    if (this.f2209d.size() > 100) {
                        this.f2209d.remove(0);
                    }
                }
                if (!this.f2210e.hasMessages(1023) && !httpEvent.getUrl().equals(m1828e())) {
                    this.f2210e.sendEmptyMessageDelayed(1023, m1835d());
                }
            }
        }
    }

    public boolean m1832a(String str) {
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, C2111a.m1781b()));
        arrayList.add(new BasicNameValuePair("app_package", C2111a.m1779a().getPackageName()));
        arrayList.add(new BasicNameValuePair("device_uuid", C2116g.m1807a(C2111a.m1779a())));
        arrayList.add(new BasicNameValuePair("device_os", "android" + VERSION.SDK_INT));
        arrayList.add(new BasicNameValuePair("device_model", Build.MODEL));
        arrayList.add(new BasicNameValuePair("app_version", C2111a.m1784e()));
        arrayList.add(new BasicNameValuePair("app_channel", C2111a.m1783d()));
        arrayList.add(new BasicNameValuePair("time", String.valueOf(System.currentTimeMillis())));
        arrayList.add(new BasicNameValuePair("net_value", str));
        Object a = C2126q.m1842a(C2111a.m1779a(), m1828e(), arrayList);
        new C2124o().m1840a("http data complete, result=" + a);
        if (!TextUtils.isEmpty(a)) {
            JSONObject jSONObject = new JSONObject(a);
            if (ScreenSaverPingBack.SEAT_KEY_OK.equals(jSONObject.getString("status"))) {
                m1825a(jSONObject);
                return true;
            }
        }
        return false;
    }

    public List m1833b() {
        List linkedList;
        synchronized (this.f2209d) {
            linkedList = new LinkedList(this.f2209d);
        }
        return linkedList;
    }

    public boolean m1834c() {
        return System.currentTimeMillis() > C2128s.m1851a(C2111a.m1779a(), "rt_ban_time", 0) && Math.random() * 10000.0d <= ((double) C2128s.m1850a(C2111a.m1779a(), "rt_upload_rate", 10000));
    }

    public long m1835d() {
        return C2128s.m1851a(C2111a.m1779a(), "rt_upload_delay", 300000);
    }
}
