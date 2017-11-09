package com.gala.tvapi.tv3.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.b.b;
import com.gala.tvapi.tv3.c.a;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.RegisterDataCache;
import com.gala.tvapi.tv3.result.SignResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class d<T extends ApiResult> extends Api<T> {
    public d(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        a.a().execute(new Runnable(this) {
            private /* synthetic */ d f526a;

            public final void run() {
                this.f526a.a(callback, params);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... params) {
        a(callback, params);
    }

    private void a(IApiCallback<T> iApiCallback, String... strArr) {
        int a = Api.a();
        if (strArr == null || strArr.length != 3) {
            iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("params error!")));
            return;
        }
        Api.a();
        String a2 = Api.a("http://itv.ptqy.gitv.tv/api/sign?code=%s&uid=%s&deviceId=%s&type=%s&agenttype=28", new String[]{a(strArr[0]), strArr[1], TVApiConfig.get().getPassportId(), strArr[2]});
        com.gala.tvapi.log.a.a("SignApi id=" + a, "url-" + a2);
        String a3 = Api.a();
        com.gala.tvapi.log.a.a("SignApi id=" + a, "header-" + a3);
        b a4 = new com.gala.tvapi.tv3.b.a().a(a2).b(a3).a(true);
        com.gala.tvapi.log.a.a("SignApi id=" + a + ",time=" + a4.f515a, "response-" + a4.a + "-" + a4.f517a);
        if (a4.a < 300) {
            ApiResult a5 = a(a4.f517a);
            if (a5 == null || a5.getClass() != this.a) {
                iApiCallback.onException(new ApiException(a4.a, "-100", new Exception("json parse error!")));
                return;
            }
            SignResult signResult = (SignResult) a5;
            if (signResult.code == null || signResult.code.equals("E352") || signResult.code.equals("E353") || signResult.code.equals("E350")) {
                iApiCallback.onSuccess(signResult);
                return;
            } else {
                iApiCallback.onException(new ApiException(a4.a, signResult.code, new Exception(signResult.code)));
                return;
            }
        }
        if (a4.f517a == null || a4.f517a.isEmpty()) {
            iApiCallback.onException(new ApiException(a4.a, "", a4.f516a));
        } else {
            try {
                JSONObject parseObject = JSON.parseObject(a4.f517a);
                iApiCallback.onException(new ApiException(a4.a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a4.a, "-100", new Exception("json parse error!")));
            }
        }
        if (a4.a == UPnPStatus.INVALID_ACTION) {
            com.gala.tvapi.log.a.a("SignApi", "response httpcode=401");
            ApiDataCache.getRegisterDataCache().putUniqueId(null);
        }
    }

    private static String a(String str) {
        RegisterDataCache registerDataCache = ApiDataCache.getRegisterDataCache();
        String b = com.gala.tvapi.c.d.b(registerDataCache.getSecret(), registerDataCache.getPublicKey());
        com.gala.tvapi.log.a.a("SignApi", "key1=" + b);
        try {
            return com.gala.tvapi.b.a.a(str, b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
