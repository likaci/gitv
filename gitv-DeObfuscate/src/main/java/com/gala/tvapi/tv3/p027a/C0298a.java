package com.gala.tvapi.tv3.p027a;

import android.os.Build;
import android.os.Build.VERSION;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.p028b.C0311a;
import com.gala.tvapi.tv3.p028b.C0312b;
import com.gala.tvapi.tv3.p029c.C0314a;
import com.gala.tvapi.tv3.result.HostUpgradeResult;
import com.gala.tvapi.tv3.result.model.HostUpgrade;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.List;

public final class C0298a<T extends ApiResult> extends Api<T> {
    private int f1077a;

    public C0298a(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, final String... params) {
        C0314a.m728a().execute(new Runnable(this) {
            private /* synthetic */ C0298a f1075a;

            public final void run() {
                this.f1075a.m709a(callback, params);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... params) {
        m709a(callback, params);
    }

    private void m709a(IApiCallback<T> iApiCallback, String... strArr) {
        this.f1077a = Api.m703a();
        Api.m703a();
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
        String a = Api.m706a("http://itv.ptqy.gitv.tv/api/upgrade?opVer=%s&chip=%s&platformModel=%s&productModel=%s&mem=%s&sdkVer=%s&apkVer=%s", strArr2);
        C0262a.m629a("HostUpgradeApi id=" + this.f1077a, "url=" + a);
        String a2 = Api.m703a();
        C0262a.m629a("HostUpgradeApi id=" + this.f1077a, "header=" + a2);
        C0312b a3 = new C0311a().m724a(a).m726b(a2).m725a(false);
        C0262a.m629a("HostUpgradeApi id=" + this.f1077a + ",time=" + a3.f1102a, "response=" + a3.f1101a + "-" + a3.f1104a);
        if (a3.f1101a == 200) {
            try {
                List parseArray = JSON.parseArray(a3.f1104a, HostUpgrade.class);
                if (parseArray == null || parseArray.size() <= 0) {
                    iApiCallback.onException(new ApiException(a3.f1101a, "", new Exception("no upgrade version available!")));
                    return;
                }
                HostUpgradeResult hostUpgradeResult = new HostUpgradeResult();
                hostUpgradeResult.updateList = parseArray;
                iApiCallback.onSuccess(hostUpgradeResult);
                return;
            } catch (JSONException e) {
                iApiCallback.onException(new ApiException(a3.f1101a, "-100", new Exception("json parse error!")));
                return;
            }
        }
        iApiCallback.onException(new ApiException(a3.f1101a, "", new Exception("no upgrade version available!")));
    }
}
