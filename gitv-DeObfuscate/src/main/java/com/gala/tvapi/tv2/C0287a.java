package com.gala.tvapi.tv2;

import android.os.SystemClock;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0255c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.p024a.C0281b;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import com.gala.video.api.IApiUrlBuilder;
import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.LinkedList;
import java.util.List;

public final class C0287a<T extends ApiResult> implements ITVApiServer<T> {
    protected C0286c<T> f967a = null;
    protected IApiUrlBuilder f968a = null;
    protected IHttpEngine f969a = HttpEngineFactory.defaultEngine();
    protected Class<T> f970a = null;
    protected String f971a = "";
    protected boolean f972a = false;
    protected boolean f973b = true;

    public C0287a(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z, boolean z2) {
        this.f968a = iApiUrlBuilder;
        this.f967a = c0286c;
        this.f970a = cls;
        this.f971a = str;
        this.f972a = z;
        this.f973b = z2;
    }

    public final void call(IApiCallback<T> callback, String... args) {
        C0287a.m676a(callback);
        m678a(false, callback, args);
    }

    public final void callSync(IApiCallback<T> callback, String... args) {
        C0287a.m676a(callback);
        m678a(true, callback, args);
    }

    public final void call(IApiCallback<T> callback, TVApiHeader header, String... args) {
        C0287a.m676a(callback);
        m677a(false, (IApiCallback) callback, header, args);
    }

    public final void callSync(IApiCallback<T> callback, TVApiHeader header, String... args) {
        C0287a.m676a(callback);
        m677a(true, (IApiCallback) callback, header, args);
    }

    private static void m676a(IApiCallback<T> iApiCallback) {
        if (iApiCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void m678a(final boolean z, final IApiCallback<T> iApiCallback, String... strArr) {
        if (this.f967a != null) {
            boolean a = this.f967a.mo851a();
            C0262a.m629a("isUpdateData", "isUpdateData=" + this.f971a + "-" + a);
            if (a) {
                final String build = this.f968a.build(strArr);
                if (this.f967a.mo856a(build, strArr)) {
                    this.f967a.mo854a(z, new C0281b(this) {
                        private /* synthetic */ C0287a f952a;

                        public final void mo836a() {
                            C0255c.m619a();
                            IHttpCallback a = this.f952a.m679a(iApiCallback, build, C0255c.m619a(), SystemClock.elapsedRealtime());
                            if (z) {
                                this.f952a.f969a.callSync(build, this.f952a.f968a.header(), a, this.f952a.f972a, this.f952a.f971a);
                            } else {
                                this.f952a.f969a.call(build, this.f952a.f968a.header(), a, this.f952a.f972a, this.f952a.f971a);
                            }
                        }

                        public final void mo837a(ApiException apiException) {
                            iApiCallback.onException(apiException);
                        }
                    });
                    return;
                }
                this.f967a.mo851a();
                iApiCallback.onException(new ApiException(C0214a.m592a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.f967a.mo850a(null, this.f970a);
            if (a2 == null) {
                iApiCallback.onException(new ApiException(this.f967a.mo851a(), "-1011", "", ""));
            } else {
                iApiCallback.onSuccess(a2);
            }
        }
    }

    private void m677a(boolean z, IApiCallback<T> iApiCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.f967a != null) {
            boolean a = this.f967a.mo851a();
            C0262a.m629a("isUpdateData", "isUpdateData=" + this.f971a + "-" + a);
            if (a) {
                final String build = this.f968a.build(strArr);
                if (this.f967a.mo856a(build, strArr)) {
                    final IApiCallback<T> iApiCallback2 = iApiCallback;
                    final TVApiHeader tVApiHeader2 = tVApiHeader;
                    final boolean z2 = z;
                    this.f967a.mo854a(z, new C0281b(this) {
                        private /* synthetic */ C0287a f957a;

                        public final void mo836a() {
                            C0255c.m619a();
                            IHttpCallback a = this.f957a.m679a(iApiCallback2, build, C0255c.m619a(), SystemClock.elapsedRealtime());
                            List linkedList = new LinkedList();
                            if (this.f957a.f968a.header() != null && this.f957a.f968a.header().size() > 0) {
                                linkedList.addAll(this.f957a.f968a.header());
                            }
                            if (tVApiHeader2 != null && tVApiHeader2.getHeaders().size() > 0) {
                                linkedList.addAll(tVApiHeader2.getHeaders());
                            }
                            if (z2) {
                                this.f957a.f969a.callSync(build, linkedList, a, this.f957a.f972a, this.f957a.f971a);
                            } else {
                                this.f957a.f969a.call(build, linkedList, a, this.f957a.f972a, this.f957a.f971a);
                            }
                        }

                        public final void mo837a(ApiException apiException) {
                            iApiCallback2.onException(apiException);
                        }
                    });
                    return;
                }
                iApiCallback.onException(new ApiException(C0214a.m592a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.f967a.mo850a(null, this.f970a);
            if (a2 == null) {
                iApiCallback.onException(new ApiException(this.f967a.mo851a(), "-1011", "", ""));
            } else {
                iApiCallback.onSuccess(a2);
            }
        }
    }

    protected final IHttpCallback m679a(IApiCallback<T> iApiCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IApiCallback<T> iApiCallback2 = iApiCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ C0287a f963a;
            private String f965a = str2;

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final void onSuccess(java.lang.String r12, java.lang.String r13, java.util.List<java.lang.Integer> r14) {
                /*
                r11 = this;
                r9 = 0;
                r0 = com.gala.tvapi.tools.TVApiTool.checkStringSize(r12);
                if (r0 != 0) goto L_0x001f;
            L_0x0007:
                r12 = "";
                r0 = new com.gala.video.api.ApiException;
                r1 = "response's size out of 10MB";
                r2 = "-100";
                r4 = r11.f965a;
                r5 = "";
                r3 = r13;
                r0.<init>(r1, r2, r3, r4, r5);
                r11.m660a(r0, r12);
            L_0x001e:
                return;
            L_0x001f:
                r0 = r11.f963a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.f967a;	 Catch:{ Exception -> 0x0149 }
                if (r0 == 0) goto L_0x001e;
            L_0x0025:
                r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0149 }
                r0 = r11.f963a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.f967a;	 Catch:{ Exception -> 0x0149 }
                r1 = r11.f963a;	 Catch:{ Exception -> 0x0149 }
                r1 = r1.f970a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.mo850a(r12, r1);	 Catch:{ Exception -> 0x0149 }
                r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0149 }
                if (r0 == 0) goto L_0x014d;
            L_0x003b:
                r1 = r0.getClass();	 Catch:{ Exception -> 0x0149 }
                r6 = r11.f963a;	 Catch:{ Exception -> 0x0149 }
                r6 = r6.f970a;	 Catch:{ Exception -> 0x0149 }
                if (r1 != r6) goto L_0x014d;
            L_0x0045:
                r8 = r0.code;	 Catch:{ Exception -> 0x0149 }
                if (r8 == 0) goto L_0x0052;
            L_0x0049:
                r1 = "N000000";
                r1 = r8.equals(r1);	 Catch:{ Exception -> 0x009d }
                if (r1 != 0) goto L_0x006d;
            L_0x0052:
                r1 = "A00000";
                r1 = r8.equals(r1);	 Catch:{ Exception -> 0x009d }
                if (r1 != 0) goto L_0x006d;
            L_0x005b:
                r1 = "N100001";
                r1 = r8.equals(r1);	 Catch:{ Exception -> 0x009d }
                if (r1 != 0) goto L_0x006d;
            L_0x0064:
                r1 = "N100002";
                r1 = r8.equals(r1);	 Catch:{ Exception -> 0x009d }
                if (r1 == 0) goto L_0x0105;
            L_0x006d:
                r0.json = r12;	 Catch:{ Exception -> 0x009d }
                r1 = r11.f963a;	 Catch:{ Exception -> 0x009d }
                r1 = r1.f967a;	 Catch:{ Exception -> 0x009d }
                r6 = 1;
                r7 = r11.f965a;	 Catch:{ Exception -> 0x009d }
                r10 = r0.getCode();	 Catch:{ Exception -> 0x009d }
                r1.mo855a(r6, r7, r12, r10);	 Catch:{ Exception -> 0x009d }
                r0.setRequestTimes(r14);	 Catch:{ Exception -> 0x009d }
                r4 = r4 - r2;
                r1 = (int) r4;	 Catch:{ Exception -> 0x009d }
                r0.setParseTime(r1);	 Catch:{ Exception -> 0x009d }
                r1 = r11.f963a;	 Catch:{ Exception -> 0x009d }
                r1 = r1.f971a;	 Catch:{ Exception -> 0x009d }
                r4 = r4;	 Catch:{ Exception -> 0x009d }
                r2 = r2 - r4;
                r5 = r6;	 Catch:{ Exception -> 0x009d }
                r6 = 1;
                r4 = r11.f963a;	 Catch:{ Exception -> 0x009d }
                r7 = r4.f973b;	 Catch:{ Exception -> 0x009d }
                r4 = r12;
                com.gala.tvapi.log.C0262a.m628a(r1, r2, r4, r5, r6, r7);	 Catch:{ Exception -> 0x009d }
                r1 = r7;	 Catch:{ Exception -> 0x009d }
                r1.onSuccess(r0);	 Catch:{ Exception -> 0x009d }
                goto L_0x001e;
            L_0x009d:
                r0 = move-exception;
                r1 = r8;
            L_0x009f:
                r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00fb }
                r4 = com.alibaba.fastjson.JSON.parseObject(r12);	 Catch:{ Exception -> 0x00fb }
                if (r4 == 0) goto L_0x00b7;
            L_0x00a9:
                r1 = "code";
                r1 = r4.getString(r1);	 Catch:{ Exception -> 0x00fb }
                r5 = "msg";
                r9 = r4.getString(r5);	 Catch:{ Exception -> 0x00fb }
            L_0x00b7:
                r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00fb }
                r0.printStackTrace();	 Catch:{ Exception -> 0x00fb }
                if (r1 == 0) goto L_0x0141;
            L_0x00c0:
                r0 = "";
                r0 = r1.equals(r0);	 Catch:{ Exception -> 0x00fb }
                if (r0 != 0) goto L_0x0141;
            L_0x00c9:
                r0 = new com.gala.video.api.ApiException;	 Catch:{ Exception -> 0x00fb }
                r6 = r11.f965a;	 Catch:{ Exception -> 0x00fb }
                r0.<init>(r9, r1, r13, r6);	 Catch:{ Exception -> 0x00fb }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00fb }
                r1.<init>();	 Catch:{ Exception -> 0x00fb }
                r6 = r11.f965a;	 Catch:{ Exception -> 0x00fb }
                r1 = r1.append(r6);	 Catch:{ Exception -> 0x00fb }
                r6 = "\n";
                r1 = r1.append(r6);	 Catch:{ Exception -> 0x00fb }
                r1 = r1.append(r12);	 Catch:{ Exception -> 0x00fb }
                r1 = r1.toString();	 Catch:{ Exception -> 0x00fb }
                r0.setDetailMessage(r1);	 Catch:{ Exception -> 0x00fb }
                r0.setRequestTimes(r14);	 Catch:{ Exception -> 0x00fb }
                r2 = r4 - r2;
                r1 = (int) r2;	 Catch:{ Exception -> 0x00fb }
                r0.setParseTime(r1);	 Catch:{ Exception -> 0x00fb }
                r11.m660a(r0, r12);	 Catch:{ Exception -> 0x00fb }
                goto L_0x001e;
            L_0x00fb:
                r0 = move-exception;
                r0.printStackTrace();
                r0 = 0;
                r11.m661a(r13, r12, r0, r14);
                goto L_0x001e;
            L_0x0105:
                r1 = r8;
            L_0x0106:
                if (r1 != 0) goto L_0x010b;
            L_0x0108:
                r1 = "-100";
            L_0x010b:
                r0 = new com.gala.video.api.ApiException;	 Catch:{ Exception -> 0x013e }
                r6 = 0;
                r7 = r11.f965a;	 Catch:{ Exception -> 0x013e }
                r0.<init>(r6, r1, r13, r7);	 Catch:{ Exception -> 0x013e }
                r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013e }
                r6.<init>();	 Catch:{ Exception -> 0x013e }
                r7 = r11.f965a;	 Catch:{ Exception -> 0x013e }
                r6 = r6.append(r7);	 Catch:{ Exception -> 0x013e }
                r7 = "\n";
                r6 = r6.append(r7);	 Catch:{ Exception -> 0x013e }
                r6 = r6.append(r12);	 Catch:{ Exception -> 0x013e }
                r6 = r6.toString();	 Catch:{ Exception -> 0x013e }
                r0.setDetailMessage(r6);	 Catch:{ Exception -> 0x013e }
                r0.setRequestTimes(r14);	 Catch:{ Exception -> 0x013e }
                r2 = r4 - r2;
                r2 = (int) r2;	 Catch:{ Exception -> 0x013e }
                r0.setParseTime(r2);	 Catch:{ Exception -> 0x013e }
                r11.m660a(r0, r12);	 Catch:{ Exception -> 0x013e }
                goto L_0x001e;
            L_0x013e:
                r0 = move-exception;
                goto L_0x009f;
            L_0x0141:
                r0 = r4 - r2;
                r0 = (int) r0;
                r11.m661a(r13, r12, r0, r14);	 Catch:{ Exception -> 0x00fb }
                goto L_0x001e;
            L_0x0149:
                r0 = move-exception;
                r1 = r9;
                goto L_0x009f;
            L_0x014d:
                r1 = r9;
                goto L_0x0106;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.gala.tvapi.tv2.a.3.onSuccess(java.lang.String, java.lang.String, java.util.List):void");
            }

            private void m661a(String str, String str2, int i, List<Integer> list) {
                ApiException apiException = new ApiException("", "-100", str, this.f965a, JSONException.class.toString());
                apiException.setDetailMessage(this.f965a + "\n" + str2 + "\n");
                apiException.setParseTime(i);
                apiException.setRequestTimes(list);
                m660a(apiException, str2);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> times) {
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException;
                    this.f963a.f967a.mo851a();
                    if (e != null) {
                        apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f965a, e.getClass().toString());
                        apiException.setDetailMessage(this.f965a + "\n" + e.getMessage());
                    } else {
                        apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f965a, "");
                        apiException.setDetailMessage(this.f965a + "\n");
                    }
                    apiException.setRequestTimes(times);
                    m660a(apiException, response);
                    return;
                }
                m660a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f965a, e.getClass().toString()), "");
            }

            private void m660a(ApiException apiException, String str) {
                apiException.setApiName(this.f963a.f971a);
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (C0214a.m592a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f963a.f967a != null) {
                    this.f963a.f967a.mo855a(false, this.f965a, str, httpCode);
                }
                iApiCallback2.onException(apiException);
                C0262a.m628a(this.f963a.f971a, elapsedRealtime - j2, str, i2, false, this.f963a.f973b);
            }

            public final String onCalling(String url) {
                if (this.f963a.f967a != null) {
                    this.f965a = this.f963a.f967a.mo852a(url);
                } else {
                    this.f965a = url;
                }
                C0262a.m630a(this.f963a.f971a, this.f965a, i2);
                return this.f965a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f963a.f967a != null) {
                    header2 = this.f963a.f967a.mo853a((List) header);
                }
                C0262a.m631a(this.f963a.f971a, header2, i2);
                return header2;
            }
        };
    }
}
