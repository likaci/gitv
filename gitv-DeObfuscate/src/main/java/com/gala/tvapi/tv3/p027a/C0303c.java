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
import com.gala.tvapi.tv3.ITVApi;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.RegisterDataCache;
import com.gala.tvapi.tv3.p028b.C0311a;
import com.gala.tvapi.tv3.p028b.C0312b;
import com.gala.tvapi.tv3.p029c.C0314a;
import com.gala.tvapi.tv3.result.RegisterResult;
import com.gala.tvapi.tv3.result.TimeResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class C0303c<T extends ApiResult> extends Api<T> {
    private int f1085a;
    private long f1086a = -1;
    private final RegisterDataCache f1087a = ApiDataCache.getRegisterDataCache();
    private final String f1088b = this.f1087a.getPublicKey();

    class C03022 implements IApiCallback<TimeResult> {
        private /* synthetic */ C0303c f1084a;

        C03022(C0303c c0303c) {
            this.f1084a = c0303c;
        }

        public final /* synthetic */ void onSuccess(Object obj) {
            this.f1084a.f1086a = ((TimeResult) obj).time;
        }

        public final void onException(ApiException apiException) {
            this.f1084a.f1086a = System.currentTimeMillis() / 1000;
        }
    }

    public C0303c(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, String... strArr) {
        C0314a.m728a().execute(new Runnable(this) {
            private /* synthetic */ C0303c f1083a;

            public final void run() {
                this.f1083a.m715a(callback);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... strArr) {
        m715a((IApiCallback) callback);
    }

    private void m715a(IApiCallback<T> iApiCallback) {
        TVApiConfig tVApiConfig = TVApiConfig.get();
        Object apkVersion = tVApiConfig.getApkVersion();
        String uuid = tVApiConfig.getUuid();
        String mac = tVApiConfig.getMac();
        if (!C0214a.m592a(apkVersion)) {
            if (!C0214a.m592a(uuid)) {
                if (!C0214a.m592a(mac)) {
                    this.f1085a = Api.m703a();
                    C0262a.m629a("RegisterApi id=" + this.f1085a, "url-http://itv.ptqy.gitv.tv/api/register");
                    ITVApi.timeApi().callSync(new C03022(this), new String[0]);
                    a = this.f1087a.getAuthorization();
                    Object b = m717b();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("apkVer", apkVersion);
                    jSONObject.put(PingbackConstants.CODE, b);
                    if (this.f1087a.isRegisterCacheAvailable()) {
                        jSONObject.put("uniqueId", this.f1087a.getUniqueId());
                    } else {
                        this.f1087a.putPublicKeyMd5(C0214a.m580a(this.f1088b));
                        this.f1087a.putUniqueId(null);
                    }
                    C0262a.m629a("RegisterApi id=" + this.f1085a, "params-" + jSONObject.toString());
                    C0312b a = new C0311a().m724a("http://itv.ptqy.gitv.tv/api/register").m727c(jSONObject.toString()).m725a(true);
                    C0262a.m629a("RegisterApi id=" + this.f1085a + ",time=" + a.f1102a, "response-" + a.f1101a + "-" + a.f1104a);
                    ApiResult a2;
                    RegisterResult registerResult;
                    if (a.f1101a > 0 && a.f1101a < 300) {
                        a2 = m714a(a.f1104a);
                        if (a2 == null || a2.getClass() != this.a) {
                            iApiCallback.onException(new ApiException(a.f1101a, "-100", new Exception("json parse error!")));
                            return;
                        }
                        registerResult = (RegisterResult) a2;
                        this.f1087a.putApkVersion(apkVersion);
                        this.f1087a.putUUID(uuid);
                        if (!(registerResult.uniqueId == null || registerResult.uniqueId.isEmpty())) {
                            this.f1087a.putUniqueId(registerResult.uniqueId);
                        }
                        if (!(registerResult.secret == null || registerResult.secret.isEmpty())) {
                            this.f1087a.putSecret(registerResult.secret);
                            a = this.f1087a.getUniqueId() + " " + m714a(registerResult.secret);
                            this.f1087a.putAuthorization(a);
                            this.f1087a.putRequestTime(ApiDataCache.getTimeDataCache().getServiceTime() + ((System.currentTimeMillis() / 1000) - ApiDataCache.getTimeDataCache().getDeviceTime()));
                        }
                        if (registerResult.expiredIn != 0) {
                            this.f1087a.putExpiredln(registerResult.expiredIn);
                        }
                        iApiCallback.onSuccess(registerResult);
                        return;
                    } else if (a.f1101a == UPnPStatus.INVALID_ACTION) {
                        r0 = new JSONObject();
                        r0.put("apkVer", apkVersion);
                        r0.put(PingbackConstants.CODE, m717b());
                        C0262a.m629a("RegisterApi id=" + this.f1085a, "params-" + r0.toString());
                        C0312b a3 = new C0311a().m724a("http://itv.ptqy.gitv.tv/api/register").m727c(r0.toString()).m725a(true);
                        C0262a.m629a("RegisterApi id=" + this.f1085a + ",time=" + a3.f1102a, "response-" + a3.f1101a + "-" + a3.f1104a);
                        if (a3.f1101a > 0 && a3.f1101a < 300) {
                            a2 = m714a(a3.f1104a);
                            if (a2 == null || a2.getClass() != this.a) {
                                iApiCallback.onException(new ApiException(a3.f1101a, "-100", new Exception("json parse error!")));
                                return;
                            }
                            registerResult = (RegisterResult) a2;
                            if (!(registerResult.uniqueId == null || registerResult.uniqueId.isEmpty())) {
                                this.f1087a.putUniqueId(registerResult.uniqueId);
                            }
                            if (!(registerResult.secret == null || registerResult.secret.isEmpty())) {
                                this.f1087a.putSecret(registerResult.secret);
                                this.f1087a.putAuthorization(this.f1087a.getUniqueId() + " " + m714a(registerResult.secret));
                                this.f1087a.putRequestTime(ApiDataCache.getTimeDataCache().getServiceTime() + ((System.currentTimeMillis() / 1000) - ApiDataCache.getTimeDataCache().getDeviceTime()));
                            }
                            if (registerResult.expiredIn != 0) {
                                this.f1087a.putExpiredln(registerResult.expiredIn);
                            }
                            iApiCallback.onSuccess(registerResult);
                            return;
                        } else if (a3.f1104a == null || a3.f1104a.isEmpty()) {
                            iApiCallback.onException(new ApiException(a3.f1101a, "", a.f1103a));
                            return;
                        } else {
                            try {
                                r0 = JSON.parseObject(a3.f1104a);
                                iApiCallback.onException(new ApiException(a3.f1101a, r0.getString(PingbackConstants.CODE), new Exception(r0.getString("msg"))));
                                return;
                            } catch (JSONException e) {
                                iApiCallback.onException(new ApiException(a3.f1101a, "-100", new Exception("json parse error!")));
                                return;
                            }
                        }
                    } else if (a.f1104a == null || a.f1104a.isEmpty()) {
                        iApiCallback.onException(new ApiException(a.f1101a, "", a.f1103a));
                        return;
                    } else {
                        try {
                            r0 = JSON.parseObject(a.f1104a);
                            iApiCallback.onException(new ApiException(a.f1101a, r0.getString(PingbackConstants.CODE), new Exception(r0.getString("msg"))));
                            return;
                        } catch (JSONException e2) {
                            iApiCallback.onException(new ApiException(a.f1101a, "-100", new Exception("json parse error!")));
                            return;
                        }
                    }
                }
            }
        }
        iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("mac=" + mac + ",uuid=" + uuid + ",apkversion=" + apkVersion)));
    }

    private String m717b() {
        TVApiConfig tVApiConfig = TVApiConfig.get();
        String str = tVApiConfig.getUuid() + tVApiConfig.getMac() + this.f1086a;
        C0262a.m629a("RegisterApi id=" + this.f1085a, "s-" + str);
        String str2 = "";
        try {
            str2 = C0256d.m621a(str, this.f1088b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        C0262a.m629a("RegisterApi id=" + this.f1085a, "result=" + str2);
        return str2;
    }

    private String m714a(String str) {
        String b = C0256d.m623b(str, this.f1088b);
        C0262a.m629a("RegisterApi", "key1=" + b);
        TVApiConfig tVApiConfig = TVApiConfig.get();
        try {
            return C0214a.m591a(tVApiConfig.getUuid() + tVApiConfig.getMac(), b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
