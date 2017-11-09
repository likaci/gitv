package com.xiaomi.mistatistic.sdk.controller.a;

import android.text.TextUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.push.pushservice.constants.DataConst;
import com.xiaomi.mistatistic.sdk.BuildSetting;
import com.xiaomi.mistatistic.sdk.controller.a;
import com.xiaomi.mistatistic.sdk.controller.e;
import com.xiaomi.mistatistic.sdk.controller.g;
import com.xiaomi.mistatistic.sdk.controller.o;
import com.xiaomi.mistatistic.sdk.controller.q;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class d implements e {
    private e a;
    private String b;

    public d(String str, e eVar) {
        this.a = eVar;
        this.b = str;
    }

    public void a() {
        boolean z = false;
        o oVar = new o();
        try {
            List arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, a.b()));
            arrayList.add(new BasicNameValuePair("app_key", a.c()));
            arrayList.add(new BasicNameValuePair("device_id", new g().a()));
            arrayList.add(new BasicNameValuePair("channel", a.d()));
            Object e = a.e();
            if (!TextUtils.isEmpty(e)) {
                arrayList.add(new BasicNameValuePair("version", e));
            }
            arrayList.add(new BasicNameValuePair("stat_value", this.b));
            e = q.a(a.a(), BuildSetting.isTest() ? "http://10.99.168.145:8097/mistats" : "https://data.mistat.xiaomi.com/mistats", arrayList);
            oVar.a("Upload MiStat data complete, result=" + e);
            if (!TextUtils.isEmpty(e)) {
                if (ScreenSaverPingBack.SEAT_KEY_OK.equals(new JSONObject(e).getString("status"))) {
                    z = true;
                }
            }
        } catch (Throwable e2) {
            oVar.a("Upload MiStat data failed", e2);
        } catch (Throwable e22) {
            oVar.a("Result parse failed", e22);
        }
        this.a.a(z);
    }
}
