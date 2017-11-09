package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class C0368b<T extends ApiResult> extends C0367g<T> {
    public C0368b(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, String str, boolean z) {
        super(iApiUrlBuilder, new C0336k(), cls, str, z, true);
    }

    protected final ApiResult m789a(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return (ApiResult) JSON.parseObject(str, this.a);
    }

    protected final IHttpCallback mo871a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final int i2 = i;
        final long j2 = j;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ C0368b f1263a;
            private String f1264a = str2;

            public final String onCalling(String url) {
                if (this.f1263a.a != null) {
                    this.f1264a = this.f1263a.a.mo852a(url);
                } else {
                    this.f1264a = url;
                }
                C0262a.m630a(this.f1263a.a, this.f1264a, i2);
                return this.f1264a;
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1264a, e.getClass().toString());
                    apiException.setDetailMessage(this.f1264a + "\n" + e.getMessage());
                    m784a(apiException, response);
                    return;
                }
                m784a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1264a, e.getClass().toString()), "");
            }

            public final List<String> onHeader(List<String> header) {
                C0262a.m631a(this.f1263a.a, (List) header, i2);
                return header;
            }

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        ApiResult a = this.f1263a.m789a(response);
                        long elapsedRealtime2 = SystemClock.elapsedRealtime();
                        if (a == null) {
                            apiException = new ApiException("", "-100", httpCode, this.f1264a, "");
                            apiException.setDetailMessage(this.f1264a + "\n" + response + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            m784a(apiException, response);
                            return;
                        } else if (a.getClass() == this.f1263a.a && a.isSuccessfull()) {
                            a.setRequestTimes(requestTimes);
                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            C0262a.m628a(this.f1263a.a, elapsedRealtime - j2, response, i2, true, this.f1263a.b);
                            iVrsCallback2.onSuccess(a);
                            return;
                        } else {
                            apiException = new ApiException(a.msg != null ? a.msg : "", a.code, httpCode, this.f1264a, "");
                            apiException.setDetailMessage(this.f1264a + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            m784a(apiException, response);
                            return;
                        }
                    }
                    m784a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1264a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.f1264a, exception.getClass().toString());
                    apiException.setDetailMessage(this.f1264a + "\n" + response + "\n" + exception.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    m784a(apiException, response);
                }
            }

            private void m784a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                iVrsCallback2.onException(apiException);
                C0262a.m628a(this.f1263a.a, elapsedRealtime - j2, str, i2, false, this.f1263a.b);
            }
        };
    }
}
