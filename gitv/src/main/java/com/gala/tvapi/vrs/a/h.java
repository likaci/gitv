package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.constants.ApiCode;
import com.gala.tvapi.vrs.core.a;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ApiResult;
import com.gala.video.api.ICommonApi;
import com.gala.video.api.ICommonApiCallback;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class h<T extends ApiResult> extends k<T> {
    private static ICommonApi b = ApiFactory.getCommonApi();
    private Boolean a = Boolean.valueOf(true);
    public String f545a = "";

    public final String a(String str) {
        boolean z = false;
        String a = super.a(str);
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
                b.callSync(super.a(String.format(a.bd, new Object[]{obj, TVApiBase.getTVApiProperty().getPassportDeviceId()})), new ICommonApiCallback(this) {
                    private /* synthetic */ h a;

                    {
                        this.a = r1;
                    }

                    public final void onException(Exception exception, String str) {
                        this.a.f545a = "";
                    }

                    public final void onSuccess(String result) {
                        if (result != null) {
                            try {
                                JSONObject jSONObject = (JSONObject) JSON.parse(result);
                                this.a.f545a = jSONObject.getString(PingbackConstants.CODE);
                            } catch (JSONException e) {
                                this.a.f545a = "";
                            }
                        }
                    }
                }, false, "userinfo");
                String str2 = this.f545a;
                if (str2 != null && (str2.equals(ApiCode.USER_INFO_CHANGED) || str2.equals(ApiCode.ERROR_USER_IP))) {
                    this.a = Boolean.valueOf(false);
                    b.callSync(super.a(String.format(a.bk, new Object[]{obj, TVApiBase.getTVApiProperty().getPassportDeviceId()})), new ICommonApiCallback() {
                        public final void onException(Exception exception, String str) {
                        }

                        public final void onSuccess(String str) {
                        }
                    }, false, "layout");
                    return z ? a : null;
                }
            }
        }
        z = true;
        if (z) {
        }
    }

    public final boolean b() {
        return this.a.booleanValue();
    }
}
