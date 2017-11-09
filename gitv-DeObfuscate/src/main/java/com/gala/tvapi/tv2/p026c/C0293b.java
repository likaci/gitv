package com.gala.tvapi.tv2.p026c;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0255c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.constants.C0295a;
import com.gala.tvapi.tv2.p024a.C0281b;
import com.gala.tvapi.tv2.p025b.C0288a;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.tvapi.vr.p030a.C0320a;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class C0293b<T extends ApiResult> extends C0289c<T> {

    class C02911 implements ICommonApiCallback {
        private /* synthetic */ int f980a;
        private /* synthetic */ long f981a;

        C02911(long j, int i) {
            this.f981a = j;
            this.f980a = i;
        }

        public final void onException(Exception exception, String str) {
        }

        public final void onSuccess(String response) {
            if (response != null) {
                JSONObject parseObject = JSON.parseObject(response);
                if (parseObject != null) {
                    String string = parseObject.getString(Keys.f2035T);
                    C0262a.m629a("System Time", string);
                    TVApiBase.getTVApiProperty().setServerTime(Long.parseLong(string) * 1000);
                    TVApiBase.getTVApiProperty().setLaunchTime(SystemClock.elapsedRealtime());
                    C0262a.m628a("systemTime", SystemClock.elapsedRealtime() - this.f981a, response, this.f980a, true, true);
                }
            }
        }
    }

    class C02922 implements IHttpCallback {
        private /* synthetic */ int f982a;
        private /* synthetic */ long f983a;
        private /* synthetic */ C0281b f984a;
        private /* synthetic */ String f985a;

        C02922(String str, long j, int i, C0281b c0281b) {
            this.f985a = str;
            this.f983a = j;
            this.f982a = i;
            this.f984a = c0281b;
        }

        public final String onCalling(String str) {
            return this.f985a;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }

        public final void onSuccess(String response, String httpCode, List<Integer> list) {
            if (!C0214a.m592a(response)) {
                if (JSON.parseObject(response).getString(PingbackConstants.CODE).equals("A000000")) {
                    C0262a.m628a("yinhe", SystemClock.elapsedRealtime() - this.f983a, response, this.f982a, true, true);
                    this.f984a.mo836a();
                    return;
                }
                C0262a.m628a("yinhe", SystemClock.elapsedRealtime() - this.f983a, response, this.f982a, false, true);
                ApiException apiException = new ApiException(response, "-1012", httpCode, this.f985a);
                apiException.setApiName("yinhe");
                this.f984a.mo837a(apiException);
            }
        }

        public final void onException(Exception e, String httpCode, String str, List<Integer> list) {
            ApiException apiException = new ApiException(e.getMessage(), "HTTP_ERR_" + httpCode, httpCode, this.f985a);
            apiException.setApiName("yinhe");
            this.f984a.mo837a(apiException);
        }
    }

    public C0293b() {
        this.a = new C0288a();
    }

    public final T mo850a(String str, Class<T> cls) {
        T t;
        if (!(!TVApiBase.getTVApiProperty().isCacheDeviceCheck() || this.a == null || str == null)) {
            this.a.mo848a(str);
            this.a.mo849b(System.currentTimeMillis());
        }
        if (str == null) {
            t = (ApiResultDeviceCheck) JSON.parseObject(this.a.mo846a(), (Class) cls);
        } else {
            ApiResultDeviceCheck apiResultDeviceCheck = (ApiResultDeviceCheck) JSON.parseObject(str, (Class) cls);
            if (!(apiResultDeviceCheck == null || !TVApiBase.getTVApiProperty().isCacheDeviceCheck() || this.a == null)) {
                this.a.mo847a(System.currentTimeMillis());
            }
        }
        if (!(t == null || t.data == null)) {
            String str2 = t.data.version;
            if (!(str2 == null || str2.equals("") || !str2.equals(TVApiBase.getTVApiProperty().getVersion()))) {
                t.data.version = "";
                t.data.url = "";
                t.data.upgradeType = -1;
            }
            TVApiBase.getTVApiProperty().setApiKey(t.data.apiKey);
            TVApiBase.getTVApiProperty().setAuthId(t.data.authId);
            TVApiBase.getTVApiProperty().setHideString(t.data.hide);
            TVApiBase.getTVApiProperty().setIpLoc(t.data.getIpRegion());
            TVApiBase.getTVApiProperty().setIpAddress_server(t.data.ip);
        }
        return t;
    }

    public final boolean mo856a(String str, String... strArr) {
        if (C0214a.m592a(TVApiBase.getTVApiProperty().getMacAddress())) {
            C0262a.m634b("TVApiProcessor", "mac地址为空");
            return false;
        }
        if (C0214a.m592a(TVApiBase.getTVApiProperty().getUUID())) {
            C0262a.m634b("TVApiProcessor", "UUID为空");
            return false;
        }
        if (!C0214a.m592a(TVApiBase.getTVApiProperty().getVersion()) && !TVApiBase.getTVApiProperty().getVersion().equals(".0")) {
            return true;
        }
        C0262a.m634b("TVApiProcessor", "apk version为空");
        return false;
    }

    public final synchronized String mo852a(String str) {
        String parseLicenceUrl;
        parseLicenceUrl = TVApiTool.parseLicenceUrl(str);
        String parseLicenceUrl2 = TVApiTool.parseLicenceUrl(C0295a.f1004S);
        C0255c.m619a();
        int a = C0255c.m619a();
        C0262a.m630a("systemTime", parseLicenceUrl2, a);
        this.a.call(parseLicenceUrl2, new C02911(SystemClock.elapsedRealtime(), a), false, "systemTime");
        return parseLicenceUrl;
    }

    public final synchronized void mo854a(boolean z, C0281b c0281b) {
        if (TVApiBase.getTVApiProperty().isCheckYinHe()) {
            String format = String.format("https://auth.api.gitv.tv/nc_auth/?partnerCode=IQIYI&partnerKey=07ceddca1fef4d1d95080136808a9151&authenType=3&authInfo=%s", new Object[]{TVApiBase.getTVApiProperty().getYinHeInfo()});
            C0255c.m619a();
            int a = C0255c.m619a();
            C0262a.m630a("yinhe", format, a);
            C02922 c02922 = new C02922(format, SystemClock.elapsedRealtime(), a, c0281b);
            if (z) {
                C0320a.m746a().m749b(format, null, c02922, false, "yinhe", null);
            } else {
                C0320a.m746a().m748a(format, null, c02922, false, "yinhe", null);
            }
        } else {
            c0281b.mo836a();
        }
    }
}
