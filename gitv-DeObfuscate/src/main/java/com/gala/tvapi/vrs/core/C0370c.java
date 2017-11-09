package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.p031a.C0349h;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class C0370c<T extends ApiResult> extends C0367g<T> {
    public C0370c(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z) {
        super(iApiUrlBuilder, c0286c, cls, str, z, true);
    }

    protected final IHttpCallback mo871a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ C0370c f1276a;
            private String f1277a = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f1276a.a != null) {
                            ApiResult a = this.f1276a.a.mo850a(response, this.f1276a.a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a.getClass() == this.f1276a.a) {
                                if (C0214a.m592a(a.getCode())) {
                                    a.json = response;
                                    a.setRequestTimes(requestTimes);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    C0262a.m628a(this.f1276a.a, elapsedRealtime - j2, response, i2, true, this.f1276a.b);
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                } else if (a.isSuccessfull()) {
                                    a.json = response;
                                    this.f1276a.a.mo855a(true, this.f1277a, response, a.getCode());
                                    a.setRequestTimes(requestTimes);
                                    C0262a.m628a(this.f1276a.a, elapsedRealtime - j2, response, i2, true, this.f1276a.b);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                } else {
                                    ApiException apiException = new ApiException(a.getMsg(), a.getCode(), httpCode, this.f1277a);
                                    apiException.setDetailMessage(this.f1277a + "\n" + response);
                                    apiException.setRequestTimes(requestTimes);
                                    apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    m791a(apiException, response);
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    m791a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1277a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    ApiException apiException2 = new ApiException("", "-100", httpCode, this.f1277a, exception.getClass().toString());
                    apiException2.setDetailMessage(this.f1277a + "\n" + response + "\n" + exception.getMessage());
                    m791a(apiException2, response);
                    apiException2.setRequestTimes(requestTimes);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (!TVApiTool.checkStringSize(response)) {
                    m791a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1277a, e.getClass().toString()), "");
                } else if (this.f1276a.a == null || !(this.f1276a.a instanceof C0349h) || ((C0349h) this.f1276a.a).mo865b()) {
                    r0 = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1277a, e.getClass().toString());
                    r0.setDetailMessage(this.f1277a + "\n" + e.getMessage());
                    r0.setRequestTimes(requestTimes);
                    m791a(r0, response);
                } else {
                    r0 = new ApiException("", ((C0349h) this.f1276a.a).f1219a, httpCode, this.f1277a, e.getClass().toString());
                    r0.setDetailMessage(this.f1277a + "\n" + e.getMessage());
                    r0.setRequestTimes(requestTimes);
                    m791a(r0, response);
                }
            }

            private void m791a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (C0214a.m592a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f1276a.a != null) {
                    this.f1276a.a.mo855a(false, this.f1277a, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                C0262a.m628a(this.f1276a.a, elapsedRealtime - j2, str, i2, false, this.f1276a.b);
            }

            public final String onCalling(String url) {
                if (this.f1276a.a != null) {
                    this.f1277a = this.f1276a.a.mo852a(url);
                } else {
                    this.f1277a = url;
                }
                C0262a.m630a(this.f1276a.a, this.f1277a, i2);
                return this.f1277a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f1276a.a != null) {
                    header2 = this.f1276a.a.mo853a((List) header);
                }
                C0262a.m631a(this.f1276a.a, header2, i2);
                return header2;
            }
        };
    }
}
