package com.gala.tvapi.tv3.a;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.c.a;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.result.PluginUpdateResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class b<T extends ApiResult> extends Api<T> {
    private int a;

    public b(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        a.a().execute(new Runnable(this) {
            private /* synthetic */ b f521a;

            public final void run() {
                this.f521a.a(callback, params);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... params) {
        a(callback, params);
    }

    private void a(IApiCallback<T> iApiCallback, String... strArr) {
        this.a = Api.a();
        Api.a();
        TVApiConfig tVApiConfig = TVApiConfig.get();
        String[] strArr2 = new String[8];
        strArr2[0] = VERSION.RELEASE;
        strArr2[1] = tVApiConfig.getHardware();
        strArr2[2] = Build.MODEL;
        strArr2[3] = Build.PRODUCT;
        strArr2[4] = String.valueOf(tVApiConfig.getMemorySize());
        strArr2[5] = String.valueOf(VERSION.SDK_INT);
        if (strArr == null || strArr.length != 2) {
            iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("no sdk version!")));
            return;
        }
        strArr2[6] = strArr[0];
        strArr2[7] = strArr[1];
        String a = Api.a("http://itv.ptqy.gitv.tv/api/pluginupgrade?opVer=%s&chip=%s&platformModel=%s&productModel=%s&mem=%s&sdkVer=%s&pluginKeys=%s&apkVer=%s", strArr2);
        com.gala.tvapi.log.a.a("PluginUpgradeApi id=" + this.a, "url=" + a);
        String a2 = Api.a();
        com.gala.tvapi.log.a.a("PluginUpgradeApi id=" + this.a, "header=" + a2);
        com.gala.tvapi.tv3.b.b a3 = new com.gala.tvapi.tv3.b.a().a(a).b(a2).a(false);
        com.gala.tvapi.log.a.a("PluginUpgradeApi id=" + this.a + ",time=" + a3.f515a, "response=" + a3.a + "-" + a3.f517a);
        if (a3.a == 200) {
            PluginUpdateResult pluginUpdateResult = (PluginUpdateResult) a(a3.f517a);
            if (pluginUpdateResult != null) {
                pluginUpdateResult.response = a3.f517a;
                iApiCallback.onSuccess(pluginUpdateResult);
                return;
            }
            iApiCallback.onException(new ApiException(a3.a, "-100", new Exception("json parse error!")));
            return;
        }
        if (a3.f517a == null || a3.f517a.isEmpty()) {
            iApiCallback.onException(new ApiException(a3.a, "", a3.f516a));
        } else {
            try {
                JSONObject parseObject = JSON.parseObject(a3.f517a);
                iApiCallback.onException(new ApiException(a3.a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a3.a, "-100", new Exception("json parse error!")));
            }
        }
        if (a3.a == UPnPStatus.INVALID_ACTION) {
            com.gala.tvapi.log.a.a("PluginUpgradeApi id=" + this.a, "response httpcode=401");
            ApiDataCache.getRegisterDataCache().putUniqueId(null);
        }
    }
}
