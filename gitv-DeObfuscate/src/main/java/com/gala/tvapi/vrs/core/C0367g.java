package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0255c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p024a.C0281b;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.LinkedList;
import java.util.List;

public class C0367g<T extends ApiResult> implements IVrsServer<T> {
    protected C0286c<T> f1266a = null;
    protected IApiUrlBuilder f1267a = null;
    protected IHttpEngine f1268a = HttpEngineFactory.defaultEngine();
    protected Class<T> f1269a = null;
    protected String f1270a = "";
    protected boolean f1271a = true;
    protected boolean f1272b = true;

    public C0367g(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z, boolean z2) {
        this.f1266a = c0286c;
        this.f1267a = iApiUrlBuilder;
        this.f1269a = cls;
        this.f1270a = str;
        this.f1271a = z;
        this.f1272b = z2;
    }

    protected void mo872a(boolean z, IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.f1266a != null) {
            boolean a = this.f1266a.mo851a();
            C0262a.m629a("isUpdateData", "isUpdateData=" + this.f1270a + "-" + a);
            if (a) {
                final String build = this.f1267a.build(strArr);
                if (this.f1266a.mo856a(build, strArr)) {
                    final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
                    final TVApiHeader tVApiHeader2 = tVApiHeader;
                    final boolean z2 = z;
                    this.f1266a.mo854a(z, new C0281b(this) {
                        private /* synthetic */ C0367g f1311a;

                        public final void mo836a() {
                            C0255c.m619a();
                            IHttpCallback a = this.f1311a.mo871a(iVrsCallback2, build, C0255c.m619a(), SystemClock.elapsedRealtime());
                            List linkedList = new LinkedList();
                            if (this.f1311a.f1267a.header() != null && this.f1311a.f1267a.header().size() > 0) {
                                linkedList.addAll(this.f1311a.f1267a.header());
                            }
                            if (tVApiHeader2 != null && tVApiHeader2.getHeaders().size() > 0) {
                                linkedList.addAll(tVApiHeader2.getHeaders());
                            }
                            if (z2) {
                                this.f1311a.f1268a.callSync(build, linkedList, a, this.f1311a.f1271a, this.f1311a.f1270a);
                            } else {
                                this.f1311a.f1268a.call(build, linkedList, a, this.f1311a.f1271a, this.f1311a.f1270a);
                            }
                        }

                        public final void mo837a(ApiException apiException) {
                            iVrsCallback2.onException(apiException);
                        }
                    });
                    return;
                }
                iVrsCallback.onException(new ApiException(C0214a.m592a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.f1266a.mo850a(null, this.f1269a);
            if (a2 == null) {
                iVrsCallback.onException(new ApiException(this.f1266a.mo851a(), "-1011", "", ""));
                return;
            } else {
                iVrsCallback.onSuccess(a2);
                return;
            }
        }
        iVrsCallback.onException(new ApiException("mProcessor is null", "-1012", "", ""));
    }

    protected void mo873a(final boolean z, final IVrsCallback<T> iVrsCallback, String... strArr) {
        if (this.f1266a == null) {
            iVrsCallback.onException(new ApiException("mProcessor is null", "-1012", "", ""));
        } else if (this.f1266a.mo851a()) {
            String build = this.f1267a.build(strArr);
            if (!build.contains("qd_sc")) {
                if (build == null || !build.contains("?")) {
                    build = build + "?qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
                } else {
                    build = build + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
                }
            }
            if (this.f1266a.mo856a(build, strArr)) {
                this.f1266a.mo854a(z, new C0281b(this) {
                    private /* synthetic */ C0367g f1315a;

                    public final void mo836a() {
                        C0255c.m619a();
                        IHttpCallback a = this.f1315a.mo871a(iVrsCallback, build, C0255c.m619a(), SystemClock.elapsedRealtime());
                        if (z) {
                            this.f1315a.f1268a.callSync(build, this.f1315a.f1267a.header(), a, this.f1315a.f1271a, this.f1315a.f1270a);
                        } else {
                            this.f1315a.f1268a.call(build, this.f1315a.f1267a.header(), a, this.f1315a.f1271a, this.f1315a.f1270a);
                        }
                    }

                    public final void mo837a(ApiException apiException) {
                        iVrsCallback.onException(apiException);
                    }
                });
            } else {
                iVrsCallback.onException(new ApiException(C0214a.m592a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
            }
        } else {
            ApiResult a = this.f1266a.mo850a(null, this.f1269a);
            if (a == null) {
                iVrsCallback.onException(new ApiException(this.f1266a.mo851a(), "-1011", "", ""));
            } else {
                iVrsCallback.onSuccess(a);
            }
        }
    }

    protected IHttpCallback mo871a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ C0367g f1321a;
            private String f1322a = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f1321a.f1266a != null) {
                            ApiResult a = this.f1321a.f1266a.mo850a(response, this.f1321a.f1269a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a != null) {
                                Class cls = a.getClass();
                                a.json = response;
                                if (cls == this.f1321a.f1269a) {
                                    if (C0214a.m592a(a.getCode())) {
                                        this.f1321a.f1266a.mo855a(true, this.f1322a, response, a.getCode());
                                        a.setRequestTimes(requestTimes);
                                        a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                        C0262a.m628a(this.f1321a.f1270a, elapsedRealtime - j2, response, i2, true, this.f1321a.f1272b);
                                        iVrsCallback2.onSuccess(a);
                                        return;
                                    }
                                    if (a != null) {
                                        if (a.isSuccessfull() || (a.getCode() != null && a.getCode().equals("A000000"))) {
                                            this.f1321a.f1266a.mo855a(true, this.f1322a, response, a.getCode());
                                            a.setRequestTimes(requestTimes);
                                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                            a.json = response;
                                            C0262a.m628a(this.f1321a.f1270a, elapsedRealtime - j2, response, i2, true, this.f1321a.f1272b);
                                            iVrsCallback2.onSuccess(a);
                                            return;
                                        }
                                    }
                                    ApiException apiException2 = new ApiException(a.getMsg(), a.getCode(), httpCode, this.f1322a);
                                    apiException2.setDetailMessage(this.f1322a + "\n" + response);
                                    apiException2.setRequestTimes(requestTimes);
                                    apiException2.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    m836a(apiException2, response);
                                    return;
                                }
                                return;
                            }
                            apiException = new ApiException("", "-100", httpCode, this.f1322a, "");
                            apiException.setDetailMessage(this.f1322a + "\n" + response + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            m836a(apiException, response);
                            return;
                        }
                        return;
                    }
                    m836a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1322a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.f1322a, exception.getClass().toString());
                    apiException.setDetailMessage(this.f1322a + "\n" + response + "\n" + exception.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    m836a(apiException, response);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    this.f1321a.f1266a.mo851a();
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1322a, e.getClass().toString());
                    apiException.setDetailMessage(this.f1322a + "\n" + e.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    m836a(apiException, response);
                    return;
                }
                m836a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f1322a, e.getClass().toString()), "");
            }

            private void m836a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (C0214a.m592a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f1321a.f1266a != null) {
                    this.f1321a.f1266a.mo855a(false, this.f1322a, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                C0262a.m628a(this.f1321a.f1270a, elapsedRealtime - j2, str, i2, false, this.f1321a.f1272b);
            }

            public final String onCalling(String url) {
                if (this.f1321a.f1266a != null) {
                    this.f1322a = this.f1321a.f1266a.mo852a(url);
                } else {
                    this.f1322a = url;
                }
                C0262a.m630a(this.f1321a.f1270a, this.f1322a, i2);
                return this.f1322a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f1321a.f1266a != null) {
                    header2 = this.f1321a.f1266a.mo853a((List) header);
                }
                C0262a.m631a(this.f1321a.f1270a, header2, i2);
                return header2;
            }
        };
    }

    private static void m785a(IVrsCallback<T> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    public void callSync(IVrsCallback<T> callback, String... args) {
        C0367g.m785a(callback);
        mo873a(true, callback, args);
    }

    public void call(IVrsCallback<T> callback, String... args) {
        C0367g.m785a(callback);
        mo873a(false, callback, args);
    }

    public void callSync(IVrsCallback<T> callback, TVApiHeader header, String... args) {
        C0367g.m785a(callback);
        mo872a(true, (IVrsCallback) callback, header, args);
    }

    public void call(IVrsCallback<T> callback, TVApiHeader header, String... args) {
        C0367g.m785a(callback);
        mo872a(false, (IVrsCallback) callback, header, args);
    }
}
