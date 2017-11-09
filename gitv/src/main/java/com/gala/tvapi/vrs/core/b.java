package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.a.k;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class b<T extends ApiResult> extends g<T> {
    public b(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, String str, boolean z) {
        super(iApiUrlBuilder, new k(), cls, str, z, true);
    }

    protected final ApiResult a(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return (ApiResult) JSON.parseObject(str, this.a);
    }

    protected final IHttpCallback a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final int i2 = i;
        final long j2 = j;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ b f550a;
            private String f551a = str2;

            public final String onCalling(String url) {
                if (this.f550a.a != null) {
                    this.f551a = this.f550a.a.a(url);
                } else {
                    this.f551a = url;
                }
                a.a(this.f550a.a, this.f551a, i2);
                return this.f551a;
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f551a, e.getClass().toString());
                    apiException.setDetailMessage(this.f551a + "\n" + e.getMessage());
                    a(apiException, response);
                    return;
                }
                a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f551a, e.getClass().toString()), "");
            }

            public final List<String> onHeader(List<String> header) {
                a.a(this.f550a.a, (List) header, i2);
                return header;
            }

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        ApiResult a = this.f550a.a(response);
                        long elapsedRealtime2 = SystemClock.elapsedRealtime();
                        if (a == null) {
                            apiException = new ApiException("", "-100", httpCode, this.f551a, "");
                            apiException.setDetailMessage(this.f551a + "\n" + response + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            a(apiException, response);
                            return;
                        } else if (a.getClass() == this.f550a.a && a.isSuccessfull()) {
                            a.setRequestTimes(requestTimes);
                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            a.a(this.f550a.a, elapsedRealtime - j2, response, i2, true, this.f550a.b);
                            iVrsCallback2.onSuccess(a);
                            return;
                        } else {
                            apiException = new ApiException(a.msg != null ? a.msg : "", a.code, httpCode, this.f551a, "");
                            apiException.setDetailMessage(this.f551a + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            a(apiException, response);
                            return;
                        }
                    }
                    a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f551a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.f551a, exception.getClass().toString());
                    apiException.setDetailMessage(this.f551a + "\n" + response + "\n" + exception.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    a(apiException, response);
                }
            }

            private void a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                iVrsCallback2.onException(apiException);
                a.a(this.f550a.a, elapsedRealtime - j2, str, i2, false, this.f550a.b);
            }
        };
    }
}
