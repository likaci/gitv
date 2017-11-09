package com.gala.tvapi.tv3.a;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.b.b;
import com.gala.tvapi.tv3.result.HostUpgradeResult;
import com.gala.tvapi.tv3.result.model.HostUpgrade;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.List;

public final class a<T extends ApiResult> extends Api<T> {
    private int a;

    public a(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        com.gala.tvapi.tv3.c.a.a().execute(new Runnable(this) {
            private /* synthetic */ a f513a;

            public final void run() {
                this.f513a.a(callback, params);
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
        if (strArr == null || strArr.length <= 0) {
            iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("no host version!")));
            return;
        }
        strArr2[6] = strArr[0];
        String a = Api.a("http://itv.ptqy.gitv.tv/api/upgrade?opVer=%s&chip=%s&platformModel=%s&productModel=%s&mem=%s&sdkVer=%s&apkVer=%s", strArr2);
        com.gala.tvapi.log.a.a("HostUpgradeApi id=" + this.a, "url=" + a);
        String a2 = Api.a();
        com.gala.tvapi.log.a.a("HostUpgradeApi id=" + this.a, "header=" + a2);
        b a3 = new com.gala.tvapi.tv3.b.a().a(a).b(a2).a(false);
        com.gala.tvapi.log.a.a("HostUpgradeApi id=" + this.a + ",time=" + a3.f515a, "response=" + a3.a + "-" + a3.f517a);
        if (a3.a == 200) {
            try {
                List parseArray = JSON.parseArray(a3.f517a, HostUpgrade.class);
                if (parseArray == null || parseArray.size() <= 0) {
                    iApiCallback.onException(new ApiException(a3.a, "", new Exception("no upgrade version available!")));
                    return;
                }
                HostUpgradeResult hostUpgradeResult = new HostUpgradeResult();
                hostUpgradeResult.updateList = parseArray;
                iApiCallback.onSuccess(hostUpgradeResult);
                return;
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a3.a, "-100", new Exception("json parse error!")));
                return;
            }
        }
        iApiCallback.onException(new ApiException(a3.a, "", new Exception("no upgrade version available!")));
    }
}
