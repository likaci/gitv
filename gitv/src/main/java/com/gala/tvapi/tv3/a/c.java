package com.gala.tvapi.tv3.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.c.d;
import com.gala.tvapi.tv3.Api;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.ITVApi;
import com.gala.tvapi.tv3.TVApiConfig;
import com.gala.tvapi.tv3.b.b;
import com.gala.tvapi.tv3.c.a;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.cache.RegisterDataCache;
import com.gala.tvapi.tv3.result.RegisterResult;
import com.gala.tvapi.tv3.result.TimeResult;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import org.cybergarage.upnp.UPnPStatus;

public final class c<T extends ApiResult> extends Api<T> {
    private int a;
    private long f523a = -1;
    private final RegisterDataCache f524a = ApiDataCache.getRegisterDataCache();
    private final String b = this.f524a.getPublicKey();

    public c(Class<T> cls) {
        super(cls);
    }

    public final void callAsync(final IApiCallback<T> callback, String... strArr) {
        a.a().execute(new Runnable(this) {
            private /* synthetic */ c f525a;

            public final void run() {
                this.f525a.a(callback);
            }
        });
    }

    public final void callSync(IApiCallback<T> callback, String... strArr) {
        a((IApiCallback) callback);
    }

    private void a(IApiCallback<T> iApiCallback) {
        TVApiConfig tVApiConfig = TVApiConfig.get();
        Object apkVersion = tVApiConfig.getApkVersion();
        String uuid = tVApiConfig.getUuid();
        String mac = tVApiConfig.getMac();
        if (!com.gala.tvapi.b.a.a(apkVersion)) {
            if (!com.gala.tvapi.b.a.a(uuid)) {
                if (!com.gala.tvapi.b.a.a(mac)) {
                    this.a = Api.a();
                    com.gala.tvapi.log.a.a("RegisterApi id=" + this.a, "url-http://itv.ptqy.gitv.tv/api/register");
                    ITVApi.timeApi().callSync(new IApiCallback<TimeResult>(this) {
                        private /* synthetic */ c a;

                        {
                            this.a = r1;
                        }

                        public final /* synthetic */ void onSuccess(Object obj) {
                            this.a.f523a = ((TimeResult) obj).time;
                        }

                        public final void onException(ApiException apiException) {
                            this.a.f523a = System.currentTimeMillis() / 1000;
                        }
                    }, new String[0]);
                    a = this.f524a.getAuthorization();
                    Object b = b();
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("apkVer", apkVersion);
                    jSONObject.put(PingbackConstants.CODE, b);
                    if (this.f524a.isRegisterCacheAvailable()) {
                        jSONObject.put("uniqueId", this.f524a.getUniqueId());
                    } else {
                        this.f524a.putPublicKeyMd5(com.gala.tvapi.b.a.a(this.b));
                        this.f524a.putUniqueId(null);
                    }
                    com.gala.tvapi.log.a.a("RegisterApi id=" + this.a, "params-" + jSONObject.toString());
                    b a = new com.gala.tvapi.tv3.b.a().a("http://itv.ptqy.gitv.tv/api/register").c(jSONObject.toString()).a(true);
                    com.gala.tvapi.log.a.a("RegisterApi id=" + this.a + ",time=" + a.f515a, "response-" + a.a + "-" + a.f517a);
                    ApiResult a2;
                    RegisterResult registerResult;
                    if (a.a > 0 && a.a < 300) {
                        a2 = a(a.f517a);
                        if (a2 == null || a2.getClass() != this.a) {
                            iApiCallback.onException(new ApiException(a.a, "-100", new Exception("json parse error!")));
                            return;
                        }
                        registerResult = (RegisterResult) a2;
                        this.f524a.putApkVersion(apkVersion);
                        this.f524a.putUUID(uuid);
                        if (!(registerResult.uniqueId == null || registerResult.uniqueId.isEmpty())) {
                            this.f524a.putUniqueId(registerResult.uniqueId);
                        }
                        if (!(registerResult.secret == null || registerResult.secret.isEmpty())) {
                            this.f524a.putSecret(registerResult.secret);
                            a = this.f524a.getUniqueId() + " " + a(registerResult.secret);
                            this.f524a.putAuthorization(a);
                            this.f524a.putRequestTime(ApiDataCache.getTimeDataCache().getServiceTime() + ((System.currentTimeMillis() / 1000) - ApiDataCache.getTimeDataCache().getDeviceTime()));
                        }
                        if (registerResult.expiredIn != 0) {
                            this.f524a.putExpiredln(registerResult.expiredIn);
                        }
                        iApiCallback.onSuccess(registerResult);
                        return;
                    } else if (a.a == UPnPStatus.INVALID_ACTION) {
                        r0 = new JSONObject();
                        r0.put("apkVer", apkVersion);
                        r0.put(PingbackConstants.CODE, b());
                        com.gala.tvapi.log.a.a("RegisterApi id=" + this.a, "params-" + r0.toString());
                        b a3 = new com.gala.tvapi.tv3.b.a().a("http://itv.ptqy.gitv.tv/api/register").c(r0.toString()).a(true);
                        com.gala.tvapi.log.a.a("RegisterApi id=" + this.a + ",time=" + a3.f515a, "response-" + a3.a + "-" + a3.f517a);
                        if (a3.a > 0 && a3.a < 300) {
                            a2 = a(a3.f517a);
                            if (a2 == null || a2.getClass() != this.a) {
                                iApiCallback.onException(new ApiException(a3.a, "-100", new Exception("json parse error!")));
                                return;
                            }
                            registerResult = (RegisterResult) a2;
                            if (!(registerResult.uniqueId == null || registerResult.uniqueId.isEmpty())) {
                                this.f524a.putUniqueId(registerResult.uniqueId);
                            }
                            if (!(registerResult.secret == null || registerResult.secret.isEmpty())) {
                                this.f524a.putSecret(registerResult.secret);
                                this.f524a.putAuthorization(this.f524a.getUniqueId() + " " + a(registerResult.secret));
                                this.f524a.putRequestTime(ApiDataCache.getTimeDataCache().getServiceTime() + ((System.currentTimeMillis() / 1000) - ApiDataCache.getTimeDataCache().getDeviceTime()));
                            }
                            if (registerResult.expiredIn != 0) {
                                this.f524a.putExpiredln(registerResult.expiredIn);
                            }
                            iApiCallback.onSuccess(registerResult);
                            return;
                        } else if (a3.f517a == null || a3.f517a.isEmpty()) {
                            iApiCallback.onException(new ApiException(a3.a, "", a.f516a));
                            return;
                        } else {
                            try {
                                r0 = JSON.parseObject(a3.f517a);
                                iApiCallback.onException(new ApiException(a3.a, r0.getString(PingbackConstants.CODE), new Exception(r0.getString("msg"))));
                                return;
                            } catch (JSONException e) {
                                iApiCallback.onException(new ApiException(a3.a, "-100", new Exception("json parse error!")));
                                return;
                            }
                        }
                    } else if (a.f517a == null || a.f517a.isEmpty()) {
                        iApiCallback.onException(new ApiException(a.a, "", a.f516a));
                        return;
                    } else {
                        try {
                            r0 = JSON.parseObject(a.f517a);
                            iApiCallback.onException(new ApiException(a.a, r0.getString(PingbackConstants.CODE), new Exception(r0.getString("msg"))));
                            return;
                        } catch (JSONException e2) {
                            iApiCallback.onException(new ApiException(a.a, "-100", new Exception("json parse error!")));
                            return;
                        }
                    }
                }
            }
        }
        iApiCallback.onException(new ApiException(0, ErrorEvent.API_CODE_GET_MAC_FAILD, new Exception("mac=" + mac + ",uuid=" + uuid + ",apkversion=" + apkVersion)));
    }

    private String b() {
        TVApiConfig tVApiConfig = TVApiConfig.get();
        String str = tVApiConfig.getUuid() + tVApiConfig.getMac() + this.f523a;
        com.gala.tvapi.log.a.a("RegisterApi id=" + this.a, "s-" + str);
        String str2 = "";
        try {
            str2 = d.a(str, this.b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.gala.tvapi.log.a.a("RegisterApi id=" + this.a, "result=" + str2);
        return str2;
    }

    private String a(String str) {
        String b = d.b(str, this.b);
        com.gala.tvapi.log.a.a("RegisterApi", "key1=" + b);
        TVApiConfig tVApiConfig = TVApiConfig.get();
        try {
            return com.gala.tvapi.b.a.a(tVApiConfig.getUuid() + tVApiConfig.getMac(), b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
