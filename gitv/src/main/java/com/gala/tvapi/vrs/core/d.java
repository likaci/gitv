package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.a.c;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultM3u8;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class d<T extends ApiResult> extends g<T> {
    private String b;
    private boolean c;

    public d(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str) {
        super(iApiUrlBuilder, cVar, cls, str, false, true);
        this.b = null;
        this.c = false;
        this.c = false;
    }

    protected final void a(boolean z, IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.a instanceof com.gala.tvapi.vrs.a.d) {
            ((com.gala.tvapi.vrs.a.d) this.a).a(this.b, false);
        }
        super.a(z, (IVrsCallback) iVrsCallback, tVApiHeader, strArr);
    }

    protected final void a(boolean z, IVrsCallback<T> iVrsCallback, String... strArr) {
        if (this.a instanceof com.gala.tvapi.vrs.a.d) {
            ((com.gala.tvapi.vrs.a.d) this.a).a(this.b, false);
        }
        super.a(z, iVrsCallback, strArr);
    }

    protected final IHttpCallback a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ d f558a;
            private String f559a = "";
            private String b = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f558a.a != null) {
                            ApiResult a = this.f558a.a.a(response, this.f558a.a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a == null) {
                                apiException = new ApiException("", "-100", httpCode, this.b, "");
                                apiException.setDetailMessage(this.b);
                                apiException.setRequestTimes(requestTimes);
                                apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                a(apiException, response);
                                return;
                            } else if (a.getClass() != this.f558a.a) {
                                return;
                            } else {
                                if (a.isSuccessfull()) {
                                    this.f558a.a.a(true, this.b, response, a.getCode());
                                    a.setRequestTimes(requestTimes);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    a.a(this.f558a.a, elapsedRealtime - j2, response, i2, true, this.f558a.b);
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                }
                                String code = a.getCode();
                                String msg = a.getMsg();
                                try {
                                    this.f559a = ((ApiResultM3u8) a).data.bossInfo.status;
                                } catch (Exception e) {
                                }
                                apiException = new ApiException(msg, code, httpCode, this.b);
                                apiException.setCode2(this.f559a);
                                apiException.setDetailMessage(this.b + "\n" + response);
                                apiException.setRequestTimes(requestTimes);
                                apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                a(apiException, response);
                                return;
                            }
                        }
                        return;
                    }
                    a(new ApiException("response's size out of 10MB", "-100", httpCode, this.b, ""), "");
                } catch (Exception e2) {
                    Exception exception = e2;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.b, exception.getClass().toString());
                    apiException.setDetailMessage(this.b + "\n" + exception.getMessage());
                    a(apiException, response);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.b, e.getClass().toString());
                    apiException.setDetailMessage(this.b + "\n" + e.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    a(apiException, response);
                    return;
                }
                a(new ApiException("response's size out of 10MB", "-100", httpCode, this.b, e.getClass().toString()), "");
            }

            private void a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                String httpCode = apiException.getHttpCode();
                if (com.gala.tvapi.b.a.a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f558a.a != null) {
                    this.f558a.a.a(false, this.b, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                a.a(this.f558a.a, elapsedRealtime - j2, str, i2, false, this.f558a.b);
            }

            public final String onCalling(String url) {
                if (this.f558a.a != null) {
                    this.b = this.f558a.a.a(url);
                } else {
                    this.b = url;
                }
                a.a(this.f558a.a, this.b, i2);
                return this.b;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f558a.a != null) {
                    header2 = this.f558a.a.a((List) header);
                }
                a.a(this.f558a.a, header2, i2);
                return header2;
            }
        };
    }
}
