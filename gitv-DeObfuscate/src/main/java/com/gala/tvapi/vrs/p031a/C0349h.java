package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.constants.ApiCode;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ApiResult;
import com.gala.video.api.ICommonApi;
import com.gala.video.api.ICommonApiCallback;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class C0349h<T extends ApiResult> extends C0336k<T> {
    private static ICommonApi f1217b = ApiFactory.getCommonApi();
    private Boolean f1218a = Boolean.valueOf(true);
    public String f1219a = "";

    class C03471 implements ICommonApiCallback {
        private /* synthetic */ C0349h f1216a;

        C03471(C0349h c0349h) {
            this.f1216a = c0349h;
        }

        public final void onException(Exception exception, String str) {
            this.f1216a.f1219a = "";
        }

        public final void onSuccess(String result) {
            if (result != null) {
                try {
                    JSONObject jSONObject = (JSONObject) JSON.parse(result);
                    this.f1216a.f1219a = jSONObject.getString(PingbackConstants.CODE);
                } catch (JSONException e) {
                    this.f1216a.f1219a = "";
                }
            }
        }
    }

    class C03482 implements ICommonApiCallback {
        C03482() {
        }

        public final void onException(Exception exception, String str) {
        }

        public final void onSuccess(String str) {
        }
    }

    public final String mo852a(String str) {
        boolean z = false;
        String a = super.mo852a(str);
        if (a != null && (a.contains("auth") || a.contains("authcookie") || a.contains("P00001"))) {
            Object obj;
            for (String split : a.split("&")) {
                String[] split2 = split.split(SearchCriteria.EQ);
                if (split2.length == 2 && (split2[0].contains("auth") || split2[0].contains("authcookie") || split2[0].contains("P00001"))) {
                    obj = split2[1];
                    break;
                }
            }
            obj = null;
            if (obj != null) {
                f1217b.callSync(super.mo852a(String.format(C0365a.bd, new Object[]{obj, TVApiBase.getTVApiProperty().getPassportDeviceId()})), new C03471(this), false, "userinfo");
                String str2 = this.f1219a;
                if (str2 != null && (str2.equals(ApiCode.USER_INFO_CHANGED) || str2.equals(ApiCode.ERROR_USER_IP))) {
                    this.f1218a = Boolean.valueOf(false);
                    f1217b.callSync(super.mo852a(String.format(C0365a.bk, new Object[]{obj, TVApiBase.getTVApiProperty().getPassportDeviceId()})), new C03482(), false, "layout");
                    return z ? a : null;
                }
            }
        }
        z = true;
        if (z) {
        }
    }

    public final boolean mo865b() {
        return this.f1218a.booleanValue();
    }
}
