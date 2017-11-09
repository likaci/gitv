package com.gala.tvapi.tv2.c;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.c.c;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class b<T extends ApiResult> extends c<T> {

    class AnonymousClass1 implements ICommonApiCallback {
        private /* synthetic */ int a;
        private /* synthetic */ long f492a;

        AnonymousClass1(long j, int i) {
            this.f492a = j;
            this.a = i;
        }

        public final void onException(Exception exception, String str) {
        }

        public final void onSuccess(String response) {
            if (response != null) {
                JSONObject parseObject = JSON.parseObject(response);
                if (parseObject != null) {
                    String string = parseObject.getString(Keys.T);
                    a.a("System Time", string);
                    TVApiBase.getTVApiProperty().setServerTime(Long.parseLong(string) * 1000);
                    TVApiBase.getTVApiProperty().setLaunchTime(SystemClock.elapsedRealtime());
                    a.a("systemTime", SystemClock.elapsedRealtime() - this.f492a, response, this.a, true, true);
                }
            }
        }
    }

    class AnonymousClass2 implements IHttpCallback {
        private /* synthetic */ int a;
        private /* synthetic */ long f493a;
        private /* synthetic */ com.gala.tvapi.tv2.a.b f494a;
        private /* synthetic */ String f495a;

        AnonymousClass2(String str, long j, int i, com.gala.tvapi.tv2.a.b bVar) {
            this.f495a = str;
            this.f493a = j;
            this.a = i;
            this.f494a = bVar;
        }

        public final String onCalling(String str) {
            return this.f495a;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }

        public final void onSuccess(String response, String httpCode, List<Integer> list) {
            if (!com.gala.tvapi.b.a.a(response)) {
                if (JSON.parseObject(response).getString(PingbackConstants.CODE).equals("A000000")) {
                    a.a("yinhe", SystemClock.elapsedRealtime() - this.f493a, response, this.a, true, true);
                    this.f494a.a();
                    return;
                }
                a.a("yinhe", SystemClock.elapsedRealtime() - this.f493a, response, this.a, false, true);
                ApiException apiException = new ApiException(response, "-1012", httpCode, this.f495a);
                apiException.setApiName("yinhe");
                this.f494a.a(apiException);
            }
        }

        public final void onException(Exception e, String httpCode, String str, List<Integer> list) {
            ApiException apiException = new ApiException(e.getMessage(), "HTTP_ERR_" + httpCode, httpCode, this.f495a);
            apiException.setApiName("yinhe");
            this.f494a.a(apiException);
        }
    }

    public b() {
        this.a = new com.gala.tvapi.tv2.b.a();
    }

    public final T a(String str, Class<T> cls) {
        T t;
        if (!(!TVApiBase.getTVApiProperty().isCacheDeviceCheck() || this.a == null || str == null)) {
            this.a.a(str);
            this.a.b(System.currentTimeMillis());
        }
        if (str == null) {
            t = (ApiResultDeviceCheck) JSON.parseObject(this.a.a(), (Class) cls);
        } else {
            ApiResultDeviceCheck apiResultDeviceCheck = (ApiResultDeviceCheck) JSON.parseObject(str, (Class) cls);
            if (!(apiResultDeviceCheck == null || !TVApiBase.getTVApiProperty().isCacheDeviceCheck() || this.a == null)) {
                this.a.a(System.currentTimeMillis());
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

    public final boolean a(String str, String... strArr) {
        if (com.gala.tvapi.b.a.a(TVApiBase.getTVApiProperty().getMacAddress())) {
            a.b("TVApiProcessor", "mac地址为空");
            return false;
        }
        if (com.gala.tvapi.b.a.a(TVApiBase.getTVApiProperty().getUUID())) {
            a.b("TVApiProcessor", "UUID为空");
            return false;
        }
        if (!com.gala.tvapi.b.a.a(TVApiBase.getTVApiProperty().getVersion()) && !TVApiBase.getTVApiProperty().getVersion().equals(".0")) {
            return true;
        }
        a.b("TVApiProcessor", "apk version为空");
        return false;
    }

    public final synchronized String a(String str) {
        String parseLicenceUrl;
        parseLicenceUrl = TVApiTool.parseLicenceUrl(str);
        String parseLicenceUrl2 = TVApiTool.parseLicenceUrl(com.gala.tvapi.tv2.constants.a.S);
        c.a();
        int a = c.a();
        a.a("systemTime", parseLicenceUrl2, a);
        this.a.call(parseLicenceUrl2, new AnonymousClass1(SystemClock.elapsedRealtime(), a), false, "systemTime");
        return parseLicenceUrl;
    }

    public final synchronized void a(boolean z, com.gala.tvapi.tv2.a.b bVar) {
        if (TVApiBase.getTVApiProperty().isCheckYinHe()) {
            String format = String.format("https://auth.api.gitv.tv/nc_auth/?partnerCode=IQIYI&partnerKey=07ceddca1fef4d1d95080136808a9151&authenType=3&authInfo=%s", new Object[]{TVApiBase.getTVApiProperty().getYinHeInfo()});
            c.a();
            int a = c.a();
            a.a("yinhe", format, a);
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(format, SystemClock.elapsedRealtime(), a, bVar);
            if (z) {
                com.gala.tvapi.vr.a.a.a().b(format, null, anonymousClass2, false, "yinhe", null);
            } else {
                com.gala.tvapi.vr.a.a.a().a(format, null, anonymousClass2, false, "yinhe", null);
            }
        } else {
            bVar.a();
        }
    }
}
