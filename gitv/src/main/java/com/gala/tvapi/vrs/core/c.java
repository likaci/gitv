package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.b.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.a.h;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class c<T extends ApiResult> extends g<T> {
    public c(IApiUrlBuilder iApiUrlBuilder, com.gala.tvapi.tv2.a.c<T> cVar, Class<T> cls, String str, boolean z) {
        super(iApiUrlBuilder, cVar, cls, str, z, true);
    }

    protected final IHttpCallback a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ c f554a;
            private String f555a = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f554a.a != null) {
                            ApiResult a = this.f554a.a.a(response, this.f554a.a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a.getClass() == this.f554a.a) {
                                if (a.a(a.getCode())) {
                                    a.json = response;
                                    a.setRequestTimes(requestTimes);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    com.gala.tvapi.log.a.a(this.f554a.a, elapsedRealtime - j2, response, i2, true, this.f554a.b);
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                } else if (a.isSuccessfull()) {
                                    a.json = response;
                                    this.f554a.a.a(true, this.f555a, response, a.getCode());
                                    a.setRequestTimes(requestTimes);
                                    com.gala.tvapi.log.a.a(this.f554a.a, elapsedRealtime - j2, response, i2, true, this.f554a.b);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                } else {
                                    ApiException apiException = new ApiException(a.getMsg(), a.getCode(), httpCode, this.f555a);
                                    apiException.setDetailMessage(this.f555a + "\n" + response);
                                    apiException.setRequestTimes(requestTimes);
                                    apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    a(apiException, response);
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f555a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    ApiException apiException2 = new ApiException("", "-100", httpCode, this.f555a, exception.getClass().toString());
                    apiException2.setDetailMessage(this.f555a + "\n" + response + "\n" + exception.getMessage());
                    a(apiException2, response);
                    apiException2.setRequestTimes(requestTimes);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (!TVApiTool.checkStringSize(response)) {
                    a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f555a, e.getClass().toString()), "");
                } else if (this.f554a.a == null || !(this.f554a.a instanceof h) || ((h) this.f554a.a).b()) {
                    r0 = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f555a, e.getClass().toString());
                    r0.setDetailMessage(this.f555a + "\n" + e.getMessage());
                    r0.setRequestTimes(requestTimes);
                    a(r0, response);
                } else {
                    r0 = new ApiException("", ((h) this.f554a.a).f545a, httpCode, this.f555a, e.getClass().toString());
                    r0.setDetailMessage(this.f555a + "\n" + e.getMessage());
                    r0.setRequestTimes(requestTimes);
                    a(r0, response);
                }
            }

            private void a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (a.a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f554a.a != null) {
                    this.f554a.a.a(false, this.f555a, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                com.gala.tvapi.log.a.a(this.f554a.a, elapsedRealtime - j2, str, i2, false, this.f554a.b);
            }

            public final String onCalling(String url) {
                if (this.f554a.a != null) {
                    this.f555a = this.f554a.a.a(url);
                } else {
                    this.f555a = url;
                }
                com.gala.tvapi.log.a.a(this.f554a.a, this.f555a, i2);
                return this.f555a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f554a.a != null) {
                    header2 = this.f554a.a.a((List) header);
                }
                com.gala.tvapi.log.a.a(this.f554a.a, header2, i2);
                return header2;
            }
        };
    }
}
