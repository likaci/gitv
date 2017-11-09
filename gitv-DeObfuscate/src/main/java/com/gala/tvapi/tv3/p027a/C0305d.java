package com.gala.tvapi.tv3.p027a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0256d;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.RegisterDataCache;
import com.gala.tvapi.tv3.p028b.C0311a;
import com.gala.tvapi.tv3.p028b.C0312b;
import com.gala.tvapi.tv3.p029c.C0314a;
import com.gala.tvapi.tv3.result.SignResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class C0305d<T extends ApiResult> extends Api<T> {
    public C0305d(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        C0314a.m728a().execute(new Runnable(this) {
            private /* synthetic */ C0305d f1090a;

            public final void run() {
                this.f1090a.m719a(callback, params);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... params) {
        m719a(callback, params);
    }

    private void m719a(IApiCallback<T> iApiCallback, String... strArr) {
        int a = Api.m703a();
        if (strArr == null || strArr.length != 3) {
            iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("params error!")));
            return;
        }
        Api.m703a();
        String a2 = Api.m706a("http://itv.ptqy.gitv.tv/api/sign?code=%s&uid=%s&deviceId=%s&type=%s&agenttype=28", new String[]{C0305d.m718a(strArr[0]), strArr[1], TVApiConfig.get().getPassportId(), strArr[2]});
        C0262a.m629a("SignApi id=" + a, "url-" + a2);
        String a3 = Api.m703a();
        C0262a.m629a("SignApi id=" + a, "header-" + a3);
        C0312b a4 = new C0311a().m724a(a2).m726b(a3).m725a(true);
        C0262a.m629a("SignApi id=" + a + ",time=" + a4.f1102a, "response-" + a4.f1101a + "-" + a4.f1104a);
        if (a4.f1101a < 300) {
            ApiResult a5 = m718a(a4.f1104a);
            if (a5 == null || a5.getClass() != this.a) {
                iApiCallback.onException(new ApiException(a4.f1101a, "-100", new Exception("json parse error!")));
                return;
            }
            SignResult signResult = (SignResult) a5;
            if (signResult.code == null || signResult.code.equals("E352") || signResult.code.equals("E353") || signResult.code.equals("E350")) {
                iApiCallback.onSuccess(signResult);
                return;
            } else {
                iApiCallback.onException(new ApiException(a4.f1101a, signResult.code, new Exception(signResult.code)));
                return;
            }
        }
        if (a4.f1104a == null || a4.f1104a.isEmpty()) {
            iApiCallback.onException(new ApiException(a4.f1101a, "", a4.f1103a));
        } else {
            try {
                JSONObject parseObject = JSON.parseObject(a4.f1104a);
                iApiCallback.onException(new ApiException(a4.f1101a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a4.f1101a, "-100", new Exception("json parse error!")));
            }
        }
        if (a4.f1101a == UPnPStatus.INVALID_ACTION) {
            C0262a.m629a("SignApi", "response httpcode=401");
            ApiDataCache.getRegisterDataCache().putUniqueId(null);
        }
    }

    private static String m718a(String str) {
        RegisterDataCache registerDataCache = ApiDataCache.getRegisterDataCache();
        String b = C0256d.m623b(registerDataCache.getSecret(), registerDataCache.getPublicKey());
        C0262a.m629a("SignApi", "key1=" + b);
        try {
            return C0214a.m591a(str, b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
