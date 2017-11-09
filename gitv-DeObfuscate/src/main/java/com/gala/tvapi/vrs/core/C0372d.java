package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.p031a.C0339d;
import com.gala.tvapi.vrs.result.ApiResultM3u8;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.IHttpCallback;
import java.util.List;

public final class C0372d<T extends ApiResult> extends C0367g<T> {
    private String f1286b;
    private boolean f1287c;

    public C0372d(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str) {
        super(iApiUrlBuilder, c0286c, cls, str, false, true);
        this.f1286b = null;
        this.f1287c = false;
        this.f1287c = false;
    }

    protected final void mo872a(boolean z, IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.a instanceof C0339d) {
            ((C0339d) this.a).m768a(this.f1286b, false);
        }
        super.mo872a(z, (IVrsCallback) iVrsCallback, tVApiHeader, strArr);
    }

    protected final void mo873a(boolean z, IVrsCallback<T> iVrsCallback, String... strArr) {
        if (this.a instanceof C0339d) {
            ((C0339d) this.a).m768a(this.f1286b, false);
        }
        super.mo873a(z, iVrsCallback, strArr);
    }

    protected final IHttpCallback mo871a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ C0372d f1282a;
            private String f1283a = "";
            private String f1284b = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f1282a.a != null) {
                            ApiResult a = this.f1282a.a.mo850a(response, this.f1282a.a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a == null) {
                                apiException = new ApiException("", "-100", httpCode, this.f1284b, "");
                                apiException.setDetailMessage(this.f1284b);
                                apiException.setRequestTimes(requestTimes);
                                apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                m793a(apiException, response);
                                return;
                            } else if (a.getClass() != this.f1282a.a) {
                                return;
                            } else {
                                if (a.isSuccessfull()) {
                                    this.f1282a.a.mo855a(true, this.f1284b, response, a.getCode());
                                    a.setRequestTimes(requestTimes);
                                    a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    C0262a.m628a(this.f1282a.a, elapsedRealtime - j2, response, i2, true, this.f1282a.b);
                                    iVrsCallback2.onSuccess(a);
                                    return;
                                }
                                String code = a.getCode();
                                String msg = a.getMsg();
                                try {
                                    this.f1283a = ((ApiResultM3u8) a).data.bossInfo.status;
                                } catch (Exception e) {
                                }
                                apiException = new ApiException(msg, code, httpCode, this.f1284b);
                                apiException.setCode2(this.f1283a);
                                apiException.setDetailMessage(this.f1284b + "\n" + response);
                                apiException.setRequestTimes(requestTimes);
                                apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                m793a(apiException, response);
                                return;
                            }
                        }
                        return;
                    }
                    m793a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1284b, ""), "");
                } catch (Exception e2) {
                    Exception exception = e2;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.f1284b, exception.getClass().toString());
                    apiException.setDetailMessage(this.f1284b + "\n" + exception.getMessage());
                    m793a(apiException, response);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1284b, e.getClass().toString());
                    apiException.setDetailMessage(this.f1284b + "\n" + e.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    m793a(apiException, response);
                    return;
                }
                m793a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1284b, e.getClass().toString()), "");
            }

            private void m793a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                String httpCode = apiException.getHttpCode();
                if (C0214a.m592a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f1282a.a != null) {
                    this.f1282a.a.mo855a(false, this.f1284b, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                C0262a.m628a(this.f1282a.a, elapsedRealtime - j2, str, i2, false, this.f1282a.b);
            }

            public final String onCalling(String url) {
                if (this.f1282a.a != null) {
                    this.f1284b = this.f1282a.a.mo852a(url);
                } else {
                    this.f1284b = url;
                }
                C0262a.m630a(this.f1282a.a, this.f1284b, i2);
                return this.f1284b;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f1282a.a != null) {
                    header2 = this.f1282a.a.mo853a((List) header);
                }
                C0262a.m631a(this.f1282a.a, header2, i2);
                return header2;
            }
        };
    }
}
