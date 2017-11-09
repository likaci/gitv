package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.a.b;
import com.gala.tvapi.tv2.a.c;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.a;
import com.gala.tvapi.vrs.a.d;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class e<T extends ApiResult> implements IPushVideoServer<T> {
    protected c<T> a = null;
    private a f560a;
    private IHttpEngine f561a = HttpEngineFactory.defaultEngine();
    protected Class<T> f562a = null;
    private String f563a;
    protected boolean f564a = true;
    private String b;
    private boolean f565b;
    private boolean c;

    class AnonymousClass2 implements IHttpCallback {
        private /* synthetic */ int a;
        private /* synthetic */ long f569a;
        private /* synthetic */ IVrsCallback f570a;
        private /* synthetic */ e f571a;
        private String f572a = this.b;
        private /* synthetic */ String b;

        AnonymousClass2(e eVar, String str, IVrsCallback iVrsCallback, long j, int i) {
            this.f571a = eVar;
            this.b = str;
            this.f570a = iVrsCallback;
            this.f569a = j;
            this.a = i;
        }

        public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
            String str = null;
            try {
                if (TVApiTool.checkStringSize(response)) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (response == null || response.isEmpty()) {
                        this.f571a.a.a(true, this.f572a, response, "");
                        this.f570a.onSuccess(null);
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
                        ApiException apiException = new ApiException(str, string, httpCode, this.f572a);
                        apiException.setDetailMessage(this.f572a + "\n" + response);
                        apiException.setRequestTimes(requestTimes);
                        a(apiException, response);
                        return;
                    } else if (this.f571a.a != null) {
                        ApiResult a = this.f571a.a.a(response, this.f571a.f562a);
                        long elapsedRealtime2 = SystemClock.elapsedRealtime();
                        if (a.getClass() == this.f571a.f562a) {
                            this.f571a.a.a(true, this.f572a, response, a.getCode());
                            a.setRequestTimes(requestTimes);
                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            com.gala.tvapi.log.a.a(this.f571a.f561a, elapsedRealtime - this.f569a, response, this.a, true, this.f571a.f564a);
                            this.f570a.onSuccess(a);
                            return;
                        }
                        ApiException apiException2 = new ApiException(str, string, httpCode, this.f572a);
                        apiException2.setDetailMessage(this.f572a + "\n" + response);
                        apiException2.setRequestTimes(requestTimes);
                        apiException2.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                        a(apiException2, response);
                        return;
                    } else {
                        return;
                    }
                }
                a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f572a, ""), "");
            } catch (Exception e) {
                Exception exception = e;
                exception.printStackTrace();
                ApiException apiException3 = new ApiException("", "-100", httpCode, this.f572a, exception.getClass().toString());
                apiException3.setDetailMessage(this.f572a + "\n" + exception.getMessage());
                a(apiException3, response);
            }
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
            if (e == null) {
                return;
            }
            if (TVApiTool.checkStringSize(response)) {
                ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f572a, e.getClass().toString());
                apiException.setDetailMessage(this.f572a + "\n" + e.getMessage());
                apiException.setRequestTimes(requestTimes);
                a(apiException, response);
                return;
            }
            a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f572a, e.getClass().toString()), "");
        }

        private void a(ApiException apiException, String str) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            String httpCode = apiException.getHttpCode();
            if (com.gala.tvapi.b.a.a(httpCode)) {
                httpCode = apiException.getCode();
            }
            if (this.f571a.a != null) {
                this.f571a.a.a(false, this.f572a, str, httpCode);
            }
            this.f570a.onException(apiException);
            com.gala.tvapi.log.a.a(this.f571a.f561a, elapsedRealtime - this.f569a, str, this.a, false, this.f571a.f564a);
        }

        public final String onCalling(String url) {
            if (this.f571a.a != null) {
                this.f572a = this.f571a.a.a(url);
            } else {
                this.f572a = url;
            }
            com.gala.tvapi.log.a.a(this.f571a.f561a, this.f572a, this.a);
            return this.f572a;
        }

        public final List<String> onHeader(List<String> header) {
            List header2;
            if (this.f571a.a != null) {
                header2 = this.f571a.a.a((List) header);
            }
            com.gala.tvapi.log.a.a(this.f571a.f561a, header2, this.a);
            return header2;
        }
    }

    public e(a aVar, c<T> cVar, Class<T> cls, String str) {
        this.f560a = aVar;
        this.f565b = false;
        this.c = false;
        this.b = str;
        this.a = cVar;
        this.f562a = cls;
        this.f564a = true;
    }

    public final void callSync(IVrsCallback<T> callback, PlatformType platform, String... strings) {
        a((IVrsCallback) callback);
        a(true, callback, platform, strings);
    }

    public final void call(IVrsCallback<T> callback, PlatformType platform, String... strings) {
        a((IVrsCallback) callback);
        a(false, callback, platform, strings);
    }

    private static void a(IVrsCallback<T> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void a(final boolean z, final IVrsCallback<T> iVrsCallback, PlatformType platformType, String... strArr) {
        final String str = this.f560a.a(platformType, strArr) + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        if (this.a instanceof d) {
            ((d) this.a).a(this.f563a, false);
        }
        if (this.a != null) {
            boolean a = this.a.a();
            com.gala.tvapi.log.a.a("isUpdateData", "isUpdateData=" + this.b + "-" + a);
            if (!a) {
                ApiResult a2 = this.a.a(null, this.f562a);
                if (a2 == null) {
                    iVrsCallback.onException(new ApiException(this.a.a(), "-1011", "", ""));
                } else {
                    iVrsCallback.onSuccess(a2);
                }
            } else if (this.a.a(str, strArr)) {
                this.a.a(z, new b(this) {
                    private /* synthetic */ e f566a;

                    public final void a() {
                        com.gala.tvapi.c.c.a();
                        int a = com.gala.tvapi.c.c.a();
                        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.f566a, str, iVrsCallback, SystemClock.elapsedRealtime(), a);
                        if (z) {
                            this.f566a.f561a.callSync(str, null, anonymousClass2, this.f566a.f561a, this.f566a.f561a);
                            return;
                        }
                        this.f566a.f561a.call(str, null, anonymousClass2, this.f566a.f561a, this.f566a.f561a);
                    }

                    public final void a(ApiException apiException) {
                        iVrsCallback.onException(apiException);
                    }
                });
            } else {
                iVrsCallback.onException(new ApiException(com.gala.tvapi.b.a.a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
            }
        }
    }
}
