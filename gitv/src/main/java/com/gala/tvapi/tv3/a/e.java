package com.gala.tvapi.tv3.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.b.b;
import com.gala.tvapi.tv3.c.a;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.TimeDataCache;
import com.gala.tvapi.tv3.result.TimeResult;
import com.mcto.ads.internal.net.PingbackConstants;

public final class e<T extends ApiResult> extends Api<T> {
    private final TimeDataCache a = ApiDataCache.getTimeDataCache();

    public e(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, String... strArr) {
        a.a().execute(new Runnable(this) {
            private /* synthetic */ e f528a;

            public final void run() {
                this.f528a.a(callback);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... strArr) {
        a(callback);
    }

    private void a(IApiCallback<T> iApiCallback) {
        int a = Api.a();
        com.gala.tvapi.log.a.a("TimeApi id=" + a, "url-http://itv.ptqy.gitv.tv/api/time");
        b a2 = new com.gala.tvapi.tv3.b.a().a("http://itv.ptqy.gitv.tv/api/time").a(false);
        com.gala.tvapi.log.a.a("TimeApi id=" + a + ",time=" + a2.f515a, "response-" + a2.a + "-" + a2.f517a);
        if (a2.a <= 0 || a2.a >= 300) {
            try {
                if (a2.f517a == null || a2.f517a.isEmpty()) {
                    iApiCallback.onException(new ApiException(a2.a, "", a2.f516a));
                    return;
                }
                JSONObject parseObject = JSON.parseObject(a2.f517a);
                iApiCallback.onException(new ApiException(a2.a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
                return;
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a2.a, "-100", new Exception("json parse error!")));
                return;
            }
        }
        try {
            TimeResult timeResult = new TimeResult();
            JSONObject parseObject2 = JSON.parseObject(a2.f517a);
            if (parseObject2 != null) {
                timeResult.time = parseObject2.getLong("time").longValue();
                this.a.putServiceTime(timeResult.time);
                this.a.putDeviceTime(System.currentTimeMillis() / 1000);
                iApiCallback.onSuccess(timeResult);
                return;
            }
            iApiCallback.onException(new ApiException(a2.a, "-100", new Exception("json parse error!")));
        } catch (JSONException e2) {
            iApiCallback.onException(new ApiException(a2.a, "-100", new Exception("json parse error!")));
        }
    }
}
