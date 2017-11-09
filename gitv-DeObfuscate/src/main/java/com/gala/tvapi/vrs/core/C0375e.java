package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0255c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p024a.C0281b;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.C0354a;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.p031a.C0339d;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class C0375e<T extends ApiResult> implements IPushVideoServer<T> {
    protected C0286c<T> f1298a = null;
    private C0354a f1299a;
    private IHttpEngine f1300a = HttpEngineFactory.defaultEngine();
    protected Class<T> f1301a = null;
    private String f1302a;
    protected boolean f1303a = true;
    private String f1304b;
    private boolean f1305b;
    private boolean f1306c;

    class C03742 implements IHttpCallback {
        private /* synthetic */ int f1292a;
        private /* synthetic */ long f1293a;
        private /* synthetic */ IVrsCallback f1294a;
        private /* synthetic */ C0375e f1295a;
        private String f1296a = this.f1297b;
        private /* synthetic */ String f1297b;

        C03742(C0375e c0375e, String str, IVrsCallback iVrsCallback, long j, int i) {
            this.f1295a = c0375e;
            this.f1297b = str;
            this.f1294a = iVrsCallback;
            this.f1293a = j;
            this.f1292a = i;
        }

        public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
            String str = null;
            try {
                if (TVApiTool.checkStringSize(response)) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (response == null || response.isEmpty()) {
                        this.f1295a.f1298a.mo855a(true, this.f1296a, response, "");
                        this.f1294a.onSuccess(null);
                        return;
                    }
                    String string;
                    JSONObject parseObject = JSON.parseObject(response);
                    if (parseObject != null) {
                        string = parseObject.getString(PingbackConstants.CODE);
                        str = parseObject.getString("msg");
                    } else {
                        string = null;
                    }
                    if (string == null || !string.equals(IAlbumConfig.NET_ERROE_CODE)) {
                        ApiException apiException = new ApiException(str, string, httpCode, this.f1296a);
                        apiException.setDetailMessage(this.f1296a + "\n" + response);
                        apiException.setRequestTimes(requestTimes);
                        m799a(apiException, response);
                        return;
                    } else if (this.f1295a.f1298a != null) {
                        ApiResult a = this.f1295a.f1298a.mo850a(response, this.f1295a.f1301a);
                        long elapsedRealtime2 = SystemClock.elapsedRealtime();
                        if (a.getClass() == this.f1295a.f1301a) {
                            this.f1295a.f1298a.mo855a(true, this.f1296a, response, a.getCode());
                            a.setRequestTimes(requestTimes);
                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            C0262a.m628a(this.f1295a.f1300a, elapsedRealtime - this.f1293a, response, this.f1292a, true, this.f1295a.f1303a);
                            this.f1294a.onSuccess(a);
                            return;
                        }
                        ApiException apiException2 = new ApiException(str, string, httpCode, this.f1296a);
                        apiException2.setDetailMessage(this.f1296a + "\n" + response);
                        apiException2.setRequestTimes(requestTimes);
                        apiException2.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                        m799a(apiException2, response);
                        return;
                    } else {
                        return;
                    }
                }
                m799a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1296a, ""), "");
            } catch (Exception e) {
                Exception exception = e;
                exception.printStackTrace();
                ApiException apiException3 = new ApiException("", "-100", httpCode, this.f1296a, exception.getClass().toString());
                apiException3.setDetailMessage(this.f1296a + "\n" + exception.getMessage());
                m799a(apiException3, response);
            }
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
            if (e == null) {
                return;
            }
            if (TVApiTool.checkStringSize(response)) {
                ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1296a, e.getClass().toString());
                apiException.setDetailMessage(this.f1296a + "\n" + e.getMessage());
                apiException.setRequestTimes(requestTimes);
                m799a(apiException, response);
                return;
            }
            m799a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1296a, e.getClass().toString()), "");
        }

        private void m799a(ApiException apiException, String str) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            String httpCode = apiException.getHttpCode();
            if (C0214a.m592a(httpCode)) {
                httpCode = apiException.getCode();
            }
            if (this.f1295a.f1298a != null) {
                this.f1295a.f1298a.mo855a(false, this.f1296a, str, httpCode);
            }
            this.f1294a.onException(apiException);
            C0262a.m628a(this.f1295a.f1300a, elapsedRealtime - this.f1293a, str, this.f1292a, false, this.f1295a.f1303a);
        }

        public final String onCalling(String url) {
            if (this.f1295a.f1298a != null) {
                this.f1296a = this.f1295a.f1298a.mo852a(url);
            } else {
                this.f1296a = url;
            }
            C0262a.m630a(this.f1295a.f1300a, this.f1296a, this.f1292a);
            return this.f1296a;
        }

        public final List<String> onHeader(List<String> header) {
            List header2;
            if (this.f1295a.f1298a != null) {
                header2 = this.f1295a.f1298a.mo853a((List) header);
            }
            C0262a.m631a(this.f1295a.f1300a, header2, this.f1292a);
            return header2;
        }
    }

    public C0375e(C0354a c0354a, C0286c<T> c0286c, Class<T> cls, String str) {
        this.f1299a = c0354a;
        this.f1305b = false;
        this.f1306c = false;
        this.f1304b = str;
        this.f1298a = c0286c;
        this.f1301a = cls;
        this.f1303a = true;
    }

    public final void callSync(IVrsCallback<T> callback, PlatformType platform, String... strings) {
        C0375e.m802a((IVrsCallback) callback);
        m803a(true, callback, platform, strings);
    }

    public final void call(IVrsCallback<T> callback, PlatformType platform, String... strings) {
        C0375e.m802a((IVrsCallback) callback);
        m803a(false, callback, platform, strings);
    }

    private static void m802a(IVrsCallback<T> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void m803a(final boolean z, final IVrsCallback<T> iVrsCallback, PlatformType platformType, String... strArr) {
        final String str = this.f1299a.mo866a(platformType, strArr) + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        if (this.f1298a instanceof C0339d) {
            ((C0339d) this.f1298a).m768a(this.f1302a, false);
        }
        if (this.f1298a != null) {
            boolean a = this.f1298a.mo851a();
            C0262a.m629a("isUpdateData", "isUpdateData=" + this.f1304b + "-" + a);
            if (!a) {
                ApiResult a2 = this.f1298a.mo850a(null, this.f1301a);
                if (a2 == null) {
                    iVrsCallback.onException(new ApiException(this.f1298a.mo851a(), "-1011", "", ""));
                } else {
                    iVrsCallback.onSuccess(a2);
                }
            } else if (this.f1298a.mo856a(str, strArr)) {
                this.f1298a.mo854a(z, new C0281b(this) {
                    private /* synthetic */ C0375e f1289a;

                    public final void mo836a() {
                        C0255c.m619a();
                        int a = C0255c.m619a();
                        C03742 c03742 = new C03742(this.f1289a, str, iVrsCallback, SystemClock.elapsedRealtime(), a);
                        if (z) {
                            this.f1289a.f1300a.callSync(str, null, c03742, this.f1289a.f1300a, this.f1289a.f1300a);
                            return;
                        }
                        this.f1289a.f1300a.call(str, null, c03742, this.f1289a.f1300a, this.f1289a.f1300a);
                    }

                    public final void mo837a(ApiException apiException) {
                        iVrsCallback.onException(apiException);
                    }
                });
            } else {
                iVrsCallback.onException(new ApiException(C0214a.m592a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
            }
        }
    }
}
