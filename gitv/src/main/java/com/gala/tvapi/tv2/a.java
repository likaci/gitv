package com.gala.tvapi.tv2;

import android.os.SystemClock;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.a.b;
import com.gala.tvapi.tv2.a.c;
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

public final class a<T extends ApiResult> implements ITVApiServer<T> {
    protected c<T> a = null;
    protected IApiUrlBuilder f472a = null;
    protected IHttpEngine f473a = HttpEngineFactory.defaultEngine();
    protected Class<T> f474a = null;
    protected String f475a = "";
    protected boolean f476a = false;
    protected boolean b = true;

    public a(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str, boolean z, boolean z2) {
        this.f472a = iApiUrlBuilder;
        this.a = cVar;
        this.f474a = cls;
        this.f475a = str;
        this.f476a = z;
        this.b = z2;
    }

    public final void call(IApiCallback<T> callback, String... args) {
        a(callback);
        a(false, callback, args);
    }

    public final void callSync(IApiCallback<T> callback, String... args) {
        a(callback);
        a(true, callback, args);
    }

    public final void call(IApiCallback<T> callback, TVApiHeader header, String... args) {
        a(callback);
        a(false, (IApiCallback) callback, header, args);
    }

    public final void callSync(IApiCallback<T> callback, TVApiHeader header, String... args) {
        a(callback);
        a(true, (IApiCallback) callback, header, args);
    }

    private static void a(IApiCallback<T> iApiCallback) {
        if (iApiCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void a(final boolean z, final IApiCallback<T> iApiCallback, String... strArr) {
        if (this.a != null) {
            boolean a = this.a.a();
            com.gala.tvapi.log.a.a("isUpdateData", "isUpdateData=" + this.f475a + "-" + a);
            if (a) {
                final String build = this.f472a.build(strArr);
                if (this.a.a(build, strArr)) {
                    this.a.a(z, new b(this) {
                        private /* synthetic */ a a;

                        public final void a() {
                            com.gala.tvapi.c.c.a();
                            IHttpCallback a = this.a.a(iApiCallback, build, com.gala.tvapi.c.c.a(), SystemClock.elapsedRealtime());
                            if (z) {
                                this.a.f473a.callSync(build, this.a.f472a.header(), a, this.a.f476a, this.a.f475a);
                            } else {
                                this.a.f473a.call(build, this.a.f472a.header(), a, this.a.f476a, this.a.f475a);
                            }
                        }

                        public final void a(ApiException apiException) {
                            iApiCallback.onException(apiException);
                        }
                    });
                    return;
                }
                this.a.a();
                iApiCallback.onException(new ApiException(com.gala.tvapi.b.a.a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.a.a(null, this.f474a);
            if (a2 == null) {
                iApiCallback.onException(new ApiException(this.a.a(), "-1011", "", ""));
            } else {
                iApiCallback.onSuccess(a2);
            }
        }
    }

    private void a(boolean z, IApiCallback<T> iApiCallback, TVApiHeader tVApiHeader, String... strArr) {
        if (this.a != null) {
            boolean a = this.a.a();
            com.gala.tvapi.log.a.a("isUpdateData", "isUpdateData=" + this.f475a + "-" + a);
            if (a) {
                final String build = this.f472a.build(strArr);
                if (this.a.a(build, strArr)) {
                    final IApiCallback<T> iApiCallback2 = iApiCallback;
                    final TVApiHeader tVApiHeader2 = tVApiHeader;
                    final boolean z2 = z;
                    this.a.a(z, new b(this) {
                        private /* synthetic */ a f480a;

                        public final void a() {
                            com.gala.tvapi.c.c.a();
                            IHttpCallback a = this.f480a.a(iApiCallback2, build, com.gala.tvapi.c.c.a(), SystemClock.elapsedRealtime());
                            List linkedList = new LinkedList();
                            if (this.f480a.f472a.header() != null && this.f480a.f472a.header().size() > 0) {
                                linkedList.addAll(this.f480a.f472a.header());
                            }
                            if (tVApiHeader2 != null && tVApiHeader2.getHeaders().size() > 0) {
                                linkedList.addAll(tVApiHeader2.getHeaders());
                            }
                            if (z2) {
                                this.f480a.f473a.callSync(build, linkedList, a, this.f480a.f476a, this.f480a.f475a);
                            } else {
                                this.f480a.f473a.call(build, linkedList, a, this.f480a.f476a, this.f480a.f475a);
                            }
                        }

                        public final void a(ApiException apiException) {
                            iApiCallback2.onException(apiException);
                        }
                    });
                    return;
                }
                iApiCallback.onException(new ApiException(com.gala.tvapi.b.a.a(strArr), ErrorEvent.API_CODE_GET_MAC_FAILD, "", ""));
                return;
            }
            ApiResult a2 = this.a.a(null, this.f474a);
            if (a2 == null) {
                iApiCallback.onException(new ApiException(this.a.a(), "-1011", "", ""));
            } else {
                iApiCallback.onSuccess(a2);
            }
        }
    }

    protected final IHttpCallback a(IApiCallback<T> iApiCallback, String str, int i, long j) {
        final String str2 = str;
        final long j2 = j;
        final int i2 = i;
        final IApiCallback<T> iApiCallback2 = iApiCallback;
        return new IHttpCallback(this) {
            private /* synthetic */ a f485a;
            private String f487a = str2;

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
                r4 = r11.f487a;
                r5 = "";
                r3 = r13;
                r0.<init>(r1, r2, r3, r4, r5);
                r11.a(r0, r12);
            L_0x001e:
                return;
            L_0x001f:
                r0 = r11.f485a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.a;	 Catch:{ Exception -> 0x0149 }
                if (r0 == 0) goto L_0x001e;
            L_0x0025:
                r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0149 }
                r0 = r11.f485a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.a;	 Catch:{ Exception -> 0x0149 }
                r1 = r11.f485a;	 Catch:{ Exception -> 0x0149 }
                r1 = r1.f474a;	 Catch:{ Exception -> 0x0149 }
                r0 = r0.a(r12, r1);	 Catch:{ Exception -> 0x0149 }
                r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0149 }
                if (r0 == 0) goto L_0x014d;
            L_0x003b:
                r1 = r0.getClass();	 Catch:{ Exception -> 0x0149 }
                r6 = r11.f485a;	 Catch:{ Exception -> 0x0149 }
                r6 = r6.f474a;	 Catch:{ Exception -> 0x0149 }
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
                r1 = r11.f485a;	 Catch:{ Exception -> 0x009d }
                r1 = r1.a;	 Catch:{ Exception -> 0x009d }
                r6 = 1;
                r7 = r11.f487a;	 Catch:{ Exception -> 0x009d }
                r10 = r0.getCode();	 Catch:{ Exception -> 0x009d }
                r1.a(r6, r7, r12, r10);	 Catch:{ Exception -> 0x009d }
                r0.setRequestTimes(r14);	 Catch:{ Exception -> 0x009d }
                r4 = r4 - r2;
                r1 = (int) r4;	 Catch:{ Exception -> 0x009d }
                r0.setParseTime(r1);	 Catch:{ Exception -> 0x009d }
                r1 = r11.f485a;	 Catch:{ Exception -> 0x009d }
                r1 = r1.f475a;	 Catch:{ Exception -> 0x009d }
                r4 = r4;	 Catch:{ Exception -> 0x009d }
                r2 = r2 - r4;
                r5 = r6;	 Catch:{ Exception -> 0x009d }
                r6 = 1;
                r4 = r11.f485a;	 Catch:{ Exception -> 0x009d }
                r7 = r4.b;	 Catch:{ Exception -> 0x009d }
                r4 = r12;
                com.gala.tvapi.log.a.a(r1, r2, r4, r5, r6, r7);	 Catch:{ Exception -> 0x009d }
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
                r6 = r11.f487a;	 Catch:{ Exception -> 0x00fb }
                r0.<init>(r9, r1, r13, r6);	 Catch:{ Exception -> 0x00fb }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00fb }
                r1.<init>();	 Catch:{ Exception -> 0x00fb }
                r6 = r11.f487a;	 Catch:{ Exception -> 0x00fb }
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
                r11.a(r0, r12);	 Catch:{ Exception -> 0x00fb }
                goto L_0x001e;
            L_0x00fb:
                r0 = move-exception;
                r0.printStackTrace();
                r0 = 0;
                r11.a(r13, r12, r0, r14);
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
                r7 = r11.f487a;	 Catch:{ Exception -> 0x013e }
                r0.<init>(r6, r1, r13, r7);	 Catch:{ Exception -> 0x013e }
                r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013e }
                r6.<init>();	 Catch:{ Exception -> 0x013e }
                r7 = r11.f487a;	 Catch:{ Exception -> 0x013e }
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
                r11.a(r0, r12);	 Catch:{ Exception -> 0x013e }
                goto L_0x001e;
            L_0x013e:
                r0 = move-exception;
                goto L_0x009f;
            L_0x0141:
                r0 = r4 - r2;
                r0 = (int) r0;
                r11.a(r13, r12, r0, r14);	 Catch:{ Exception -> 0x00fb }
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

            private void a(String str, String str2, int i, List<Integer> list) {
                ApiException apiException = new ApiException("", "-100", str, this.f487a, JSONException.class.toString());
                apiException.setDetailMessage(this.f487a + "\n" + str2 + "\n");
                apiException.setParseTime(i);
                apiException.setRequestTimes(list);
                a(apiException, str2);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> times) {
                if (TVApiTool.checkStringSize(response)) {
                    ApiException apiException;
                    this.f485a.a.a();
                    if (e != null) {
                        apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f487a, e.getClass().toString());
                        apiException.setDetailMessage(this.f487a + "\n" + e.getMessage());
                    } else {
                        apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f487a, "");
                        apiException.setDetailMessage(this.f487a + "\n");
                    }
                    apiException.setRequestTimes(times);
                    a(apiException, response);
                    return;
                }
                a(new ApiException("response's size out of 10MB", "-100", httpCode, this.f487a, e.getClass().toString()), "");
            }

            private void a(ApiException apiException, String str) {
                apiException.setApiName(this.f485a.f475a);
                long elapsedRealtime = SystemClock.elapsedRealtime();
                apiException.printStackTrace();
                String httpCode = apiException.getHttpCode();
                if (com.gala.tvapi.b.a.a(httpCode)) {
                    httpCode = apiException.getCode();
                }
                if (this.f485a.a != null) {
                    this.f485a.a.a(false, this.f487a, str, httpCode);
                }
                iApiCallback2.onException(apiException);
                com.gala.tvapi.log.a.a(this.f485a.f475a, elapsedRealtime - j2, str, i2, false, this.f485a.b);
            }

            public final String onCalling(String url) {
                if (this.f485a.a != null) {
                    this.f487a = this.f485a.a.a(url);
                } else {
                    this.f487a = url;
                }
                com.gala.tvapi.log.a.a(this.f485a.f475a, this.f487a, i2);
                return this.f487a;
            }

            public final List<String> onHeader(List<String> header) {
                List header2;
                if (this.f485a.a != null) {
                    header2 = this.f485a.a.a((List) header);
                }
                com.gala.tvapi.log.a.a(this.f485a.f475a, header2, i2);
                return header2;
            }
        };
    }
}
