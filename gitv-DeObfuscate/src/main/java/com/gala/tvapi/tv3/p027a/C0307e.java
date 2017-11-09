package com.gala.tvapi.tv3.p027a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.TimeDataCache;
import com.gala.tvapi.tv3.p028b.C0311a;
import com.gala.tvapi.tv3.p028b.C0312b;
import com.gala.tvapi.tv3.p029c.C0314a;
import com.gala.tvapi.tv3.result.TimeResult;
import com.mcto.ads.internal.net.PingbackConstants;

public final class C0307e<T extends ApiResult> extends Api<T> {
    private final TimeDataCache f1094a = ApiDataCache.getTimeDataCache();

    public C0307e(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, String... strArr) {
        C0314a.m728a().execute(new Runnable(this) {
            private /* synthetic */ C0307e f1093a;

            public final void run() {
                this.f1093a.m721a(callback);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... strArr) {
        m721a(callback);
    }

    private void m721a(IApiCallback<T> iApiCallback) {
        int a = Api.m703a();
        C0262a.m629a("TimeApi id=" + a, "url-http://itv.ptqy.gitv.tv/api/time");
        C0312b a2 = new C0311a().m724a("http://itv.ptqy.gitv.tv/api/time").m725a(false);
        C0262a.m629a("TimeApi id=" + a + ",time=" + a2.f1102a, "response-" + a2.f1101a + "-" + a2.f1104a);
        if (a2.f1101a <= 0 || a2.f1101a >= 300) {
            try {
                if (a2.f1104a == null || a2.f1104a.isEmpty()) {
                    iApiCallback.onException(new ApiException(a2.f1101a, "", a2.f1103a));
                    return;
                }
                JSONObject parseObject = JSON.parseObject(a2.f1104a);
                iApiCallback.onException(new ApiException(a2.f1101a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
                return;
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a2.f1101a, "-100", new Exception("json parse error!")));
                return;
            }
        }
        try {
            TimeResult timeResult = new TimeResult();
            JSONObject parseObject2 = JSON.parseObject(a2.f1104a);
            if (parseObject2 != null) {
                timeResult.time = parseObject2.getLong("time").longValue();
                this.f1094a.putServiceTime(timeResult.time);
                this.f1094a.putDeviceTime(System.currentTimeMillis() / 1000);
                iApiCallback.onSuccess(timeResult);
                return;
            }
            iApiCallback.onException(new ApiException(a2.f1101a, "-100", new Exception("json parse error!")));
        } catch (JSONException e2) {
            iApiCallback.onException(new ApiException(a2.f1101a, "-100", new Exception("json parse error!")));
        }
    }
}
