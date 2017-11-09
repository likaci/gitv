package com.gala.tvapi.vrs.core;

import android.os.SystemClock;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.a.b;
import com.gala.tvapi.tv2.a.c;
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

public class g<T extends ApiResult> implements IVrsServer<T> {
    protected c<T> a = null;
    protected IApiUrlBuilder f440a = null;
    protected IHttpEngine f441a = HttpEngineFactory.defaultEngine();
    protected Class<T> f442a = null;
    protected String f443a = "";
    protected boolean f444a = true;
    protected boolean b = true;

    public g(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str, boolean z, boolean z2) {
        this.a = cVar;
        this.f440a = iApiUrlBuilder;
        this.f442a = cls;
        this.f443a = str;
        this.f444a = z;
        this.b = z2;
    }

    protected void a(boolean z, IVrsCallback<T> iVrsCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.a != null) {
            boolean a = this.a.a();
            a.a("isUpdateData", "isUpdateData=" + this.f443a + "-" + a);
            if (a) {
                final String build = this.f440a.build(strArr);
                if (this.a.a(build, strArr)) {
                    final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
                    final TVApiHeader tVApiHeader2 = tVApiHeader;
                    final boolean z2 = z;
                    this.a.a(z, new b(this) {
                        private /* synthetic */ g f446a;

                        public final void a() {
                            com.gala.tvapi.c.c.a();
                            IHttpCallback a = this.f446a.a(iVrsCallback2, build, com.gala.tvapi.c.c.a(), SystemClock.elapsedRealtime());
                            List linkedList = new LinkedList();
                            if (this.f446a.f440a.header() != null && this.f446a.f440a.header().size() > 0) {
                                linkedList.addAll(this.f446a.f440a.header());
                            }
                            if (tVApiHeader2 != null && tVApiHeader2.getHeaders().size() > 0) {
                                linkedList.addAll(tVApiHeader2.getHeaders());
                            }
                            if (z2) {
                                this.f446a.f441a.callSync(build, linkedList, a, this.f446a.f444a, this.f446a.f443a);
                            } else {
                                this.f446a.f441a.call(build, linkedList, a, this.f446a.f444a, this.f446a.f443a);
                            }
                        }

                        public final void a(ApiException apiException) {
                            iVrsCallback2.onException(apiException);
                        }
                    });
                    return;
                }
                iVrsCallback.onException(new ApiException(com.gala.tvapi.b.a.a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.a.a(null, this.f442a);
            if (a2 == null) {
                iVrsCallback.onException(new ApiException(this.a.a(), "-1011", "", ""));
                return;
            } else {
                iVrsCallback.onSuccess(a2);
                return;
            }
        }
        iVrsCallback.onException(new ApiException("mProcessor is null", "-1012", "", ""));
    }

    protected void a(final boolean z, final IVrsCallback<T> iVrsCallback, String... strArr) {
        if (this.a == null) {
            iVrsCallback.onException(new ApiException("mProcessor is null", "-1012", "", ""));
        } else if (this.a.a()) {
            String build = this.f440a.build(strArr);
            if (!build.contains("qd_sc")) {
                if (build == null || !build.contains("?")) {
                    build = build + "?qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
                } else {
                    build = build + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
                }
            }
            if (this.a.a(build, strArr)) {
                this.a.a(z, new b(this) {
                    private /* synthetic */ g f449a;

                    public final void a() {
                        com.gala.tvapi.c.c.a();
                        IHttpCallback a = this.f449a.a(iVrsCallback, build, com.gala.tvapi.c.c.a(), SystemClock.elapsedRealtime());
                        if (z) {
                            this.f449a.f441a.callSync(build, this.f449a.f440a.header(), a, this.f449a.f444a, this.f449a.f443a);
                        } else {
                            this.f449a.f441a.call(build, this.f449a.f440a.header(), a, this.f449a.f444a, this.f449a.f443a);
                        }
                    }

                    public final void a(ApiException apiException) {
                        iVrsCallback.onException(apiException);
                    }
                });
            } else {
                iVrsCallback.onException(new ApiException(com.gala.tvapi.b.a.a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
            }
        } else {
            ApiResult a = this.a.a(null, this.f442a);
            if (a == null) {
                iVrsCallback.onException(new ApiException(this.a.a(), "-1011", "", ""));
            } else {
                iVrsCallback.onSuccess(a);
            }
        }
    }

    protected IHttpCallback a(IVrsCallback<T> iVrsCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IVrsCallback<T> iVrsCallback2 = iVrsCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ g f454a;
            private String f455a = str2;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                ApiException apiException;
                try {
                    if (TVApiTool.checkStringSize(response)) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (this.f454a.a != null) {
                            ApiResult a = this.f454a.a.a(response, this.f454a.f442a);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (a != null) {
                                Class cls = a.getClass();
                                a.json = response;
                                if (cls == this.f454a.f442a) {
                                    if (com.gala.tvapi.b.a.a(a.getCode())) {
                                        this.f454a.a.a(true, this.f455a, response, a.getCode());
                                        a.setRequestTimes(requestTimes);
                                        a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                        a.a(this.f454a.f443a, elapsedRealtime - j2, response, i2, true, this.f454a.b);
                                        iVrsCallback2.onSuccess(a);
                                        return;
                                    }
                                    if (a != null) {
                                        if (a.isSuccessfull() || (a.getCode() != null && a.getCode().equals("A000000"))) {
                                            this.f454a.a.a(true, this.f455a, response, a.getCode());
                                            a.setRequestTimes(requestTimes);
                                            a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                            a.json = response;
                                            a.a(this.f454a.f443a, elapsedRealtime - j2, response, i2, true, this.f454a.b);
                                            iVrsCallback2.onSuccess(a);
                                            return;
                                        }
                                    }
                                    ApiException apiException2 = new ApiException(a.getMsg(), a.getCode(), httpCode, this.f455a);
                                    apiException2.setDetailMessage(this.f455a + "\n" + response);
                                    apiException2.setRequestTimes(requestTimes);
                                    apiException2.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                    a(apiException2, response);
                                    return;
                                }
                                return;
                            }
                            apiException = new ApiException("", "-100", httpCode, this.f455a, "");
                            apiException.setDetailMessage(this.f455a + "\n" + response + "\n");
                            apiException.setRequestTimes(requestTimes);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            a(apiException, response);
                            return;
                        }
                        return;
                    }
                    a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f455a, ""), "");
                } catch (Exception e) {
                    Exception exception = e;
                    exception.printStackTrace();
                    apiException = new ApiException("", "-100", httpCode, this.f455a, exception.getClass().toString());
                    apiException.setDetailMessage(this.f455a + "\n" + response + "\n" + exception.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    a(apiException, response);
                }
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> requestTimes) {
                if (e == null) {
                    return;
                }
                if (TVApiTool.checkStringSize(response)) {
                    this.f454a.a.a();
                    ApiException apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f455a, e.getClass().toString());
                    apiException.setDetailMessage(this.f455a + "\n" + e.getMessage());
                    apiException.setRequestTimes(requestTimes);
                    a(apiException, response);
                    return;
                }
                a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f455a, e.getClass().toString()), "");
            }

            private void a(ApiException apiException, String str) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (com.gala.tvapi.b.a.a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f454a.a != null) {
                    this.f454a.a.a(false, this.f455a, str, httpCode);
                }
                iVrsCallback2.onException(apiException);
                a.a(this.f454a.f443a, elapsedRealtime - j2, str, i2, false, this.f454a.b);
            }

            public final String onCalling(String url) {
                if (this.f454a.a != null) {
                    this.f455a = this.f454a.a.a(url);
                } else {
                    this.f455a = url;
                }
                a.a(this.f454a.f443a, this.f455a, i2);
                return this.f455a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f454a.a != null) {
                    header2 = this.f454a.a.a((List) header);
                }
                a.a(this.f454a.f443a, header2, i2);
                return header2;
            }
        };
    }

    private static void a(IVrsCallback<T> iVrsCallback) {
        if (iVrsCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    public void callSync(IVrsCallback<T> callback, String... args) {
        a(callback);
        a(true, callback, args);
    }

    public void call(IVrsCallback<T> callback, String... args) {
        a(callback);
        a(false, callback, args);
    }

    public void callSync(IVrsCallback<T> callback, TVApiHeader header, String... args) {
        a(callback);
        a(true, (IVrsCallback) callback, header, args);
    }

    public void call(IVrsCallback<T> callback, TVApiHeader header, String... args) {
        a(callback);
        a(false, (IVrsCallback) callback, header, args);
    }
}
