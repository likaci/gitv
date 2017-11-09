package com.gala.tvapi.tv3.p027a;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.p028b.C0311a;
import com.gala.tvapi.tv3.p028b.C0312b;
import com.gala.tvapi.tv3.p029c.C0314a;
import com.gala.tvapi.tv3.result.PluginUpdateResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class C0300b<T extends ApiResult> extends Api<T> {
    private int f1081a;

    public C0300b(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        C0314a.m728a().execute(new Runnable(this) {
            private /* synthetic */ C0300b f1079a;

            public final void run() {
                this.f1079a.m711a(callback, params);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... params) {
        m711a(callback, params);
    }

    private void m711a(IApiCallback<T> iApiCallback, String... strArr) {
        this.f1081a = Api.m703a();
        Api.m703a();
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
        String a = Api.m706a("http://itv.ptqy.gitv.tv/api/pluginupgrade?opVer=%s&chip=%s&platformModel=%s&productModel=%s&mem=%s&sdkVer=%s&pluginKeys=%s&apkVer=%s", strArr2);
        C0262a.m629a("PluginUpgradeApi id=" + this.f1081a, "url=" + a);
        String a2 = Api.m703a();
        C0262a.m629a("PluginUpgradeApi id=" + this.f1081a, "header=" + a2);
        C0312b a3 = new C0311a().m724a(a).m726b(a2).m725a(false);
        C0262a.m629a("PluginUpgradeApi id=" + this.f1081a + ",time=" + a3.f1102a, "response=" + a3.f1101a + "-" + a3.f1104a);
        if (a3.f1101a == 200) {
            PluginUpdateResult pluginUpdateResult = (PluginUpdateResult) m705a(a3.f1104a);
            if (pluginUpdateResult != null) {
                pluginUpdateResult.response = a3.f1104a;
                iApiCallback.onSuccess(pluginUpdateResult);
                return;
            }
            iApiCallback.onException(new ApiException(a3.f1101a, "-100", new Exception("json parse error!")));
            return;
        }
        if (a3.f1104a == null || a3.f1104a.isEmpty()) {
            iApiCallback.onException(new ApiException(a3.f1101a, "", a3.f1103a));
        } else {
            try {
                JSONObject parseObject = JSON.parseObject(a3.f1104a);
                iApiCallback.onException(new ApiException(a3.f1101a, parseObject.getString(PingbackConstants.CODE), new Exception(parseObject.getString("msg"))));
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a3.f1101a, "-100", new Exception("json parse error!")));
            }
        }
        if (a3.f1101a == UPnPStatus.INVALID_ACTION) {
            C0262a.m629a("PluginUpgradeApi id=" + this.f1081a, "response httpcode=401");
            ApiDataCache.getRegisterDataCache().putUniqueId(null);
        }
    }
}
