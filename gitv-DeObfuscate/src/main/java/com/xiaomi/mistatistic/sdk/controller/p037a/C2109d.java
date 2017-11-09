package com.xiaomi.mistatistic.sdk.controller.p037a;

import android.text.TextUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.push.pushservice.constants.DataConst;
import com.xiaomi.mistatistic.sdk.BuildSetting;
import com.xiaomi.mistatistic.sdk.controller.C2105e;
import com.xiaomi.mistatistic.sdk.controller.C2111a;
import com.xiaomi.mistatistic.sdk.controller.C2116g;
import com.xiaomi.mistatistic.sdk.controller.C2124o;
import com.xiaomi.mistatistic.sdk.controller.C2126q;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class C2109d implements C2105e {
    private C2110e f2176a;
    private String f2177b;

    public C2109d(String str, C2110e c2110e) {
        this.f2176a = c2110e;
        this.f2177b = str;
    }

    public void mo4534a() {
        boolean z = false;
        C2124o c2124o = new C2124o();
        try {
            List arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, C2111a.m1781b()));
            arrayList.add(new BasicNameValuePair("app_key", C2111a.m1782c()));
            arrayList.add(new BasicNameValuePair("device_id", new C2116g().m1813a()));
            arrayList.add(new BasicNameValuePair("channel", C2111a.m1783d()));
            Object e = C2111a.m1784e();
            if (!TextUtils.isEmpty(e)) {
                arrayList.add(new BasicNameValuePair("version", e));
            }
            arrayList.add(new BasicNameValuePair("stat_value", this.f2177b));
            e = C2126q.m1842a(C2111a.m1779a(), BuildSetting.isTest() ? "http://10.99.168.145:8097/mistats" : "https://data.mistat.xiaomi.com/mistats", arrayList);
            c2124o.m1840a("Upload MiStat data complete, result=" + e);
            if (!TextUtils.isEmpty(e)) {
                if (ScreenSaverPingBack.SEAT_KEY_OK.equals(new JSONObject(e).getString("status"))) {
                    z = true;
                }
            }
        } catch (Throwable e2) {
            c2124o.m1841a("Upload MiStat data failed", e2);
        } catch (Throwable e22) {
            c2124o.m1841a("Result parse failed", e22);
        }
        this.f2176a.mo4536a(z);
    }
}
