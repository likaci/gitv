package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class C0479b extends C0474a implements ICommonApi {
    private static final C0479b f1864a = new C0479b();
    private IHttpEngine f1865a = HttpEngineFactory.newCommonEngine("CommonApi", 2);

    class C04751 implements IHttpCallback {
        private /* synthetic */ ICommonApiCallback f1854a;
        private /* synthetic */ String f1855a;

        C04751(ICommonApiCallback iCommonApiCallback, String str) {
            this.f1854a = iCommonApiCallback;
            this.f1855a = str;
        }

        public final void onSuccess(String response, String str, List<Integer> list) {
            this.f1854a.onSuccess(response);
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
            ApiEngineLog.m1530d("CommonApi-" + this.f1855a, "response=" + response);
            this.f1854a.onException(e, httpCode);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> header) {
            if (header != null && header.size() > 0) {
                for (String str : header) {
                    ApiEngineLog.m1530d("CommonApi-" + this.f1855a, "header=" + str);
                }
            }
            return header;
        }
    }

    class C04784 implements IHttpCallback {
        private /* synthetic */ ICommonApiCallback f1862a;
        private /* synthetic */ String f1863a;

        C04784(ICommonApiCallback iCommonApiCallback, String str) {
            this.f1862a = iCommonApiCallback;
            this.f1863a = str;
        }

        public final void onSuccess(String response, String str, List<Integer> list) {
            this.f1862a.onSuccess(response);
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
            ApiEngineLog.m1530d("CommonApi-" + this.f1863a, "response=" + response);
            this.f1862a.onException(e, httpCode);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> header) {
            if (header != null && header.size() > 0) {
                for (String str : header) {
                    ApiEngineLog.m1530d("CommonApi-" + this.f1863a, "header=" + str);
                }
            }
            return header;
        }
    }

    public static C0479b m1517a() {
        return f1864a;
    }

    private C0479b() {
    }

    private static void m1518a(ICommonApiCallback iCommonApiCallback) {
        if (iCommonApiCallback == null) {
            throw new NullPointerException("A callback is needed for CommonApi call().");
        }
    }

    public final void call(String url, ICommonApiCallback callback, boolean supportPostRequest, String name) {
        ApiEngineLog.m1530d("CommonApi-" + name, "url=" + url);
        C0479b.m1518a(callback);
        this.f1865a.call(url, null, new C04751(callback, name), supportPostRequest, name);
    }

    public final void call(String url, final ICommonApiCallback callback, IApiHeader header, boolean supportPostRequest, final String name) {
        ApiEngineLog.m1530d("CommonApi-" + name, "url=" + url);
        C0479b.m1518a(callback);
        this.f1865a.call(url, header.getHeader(), new IHttpCallback(this) {
            private /* synthetic */ C0479b f1857a;

            public final void onSuccess(String response, String str, List<Integer> list) {
                callback.onSuccess(response);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                ApiEngineLog.m1530d("CommonApi-" + name, "response=" + response);
                callback.onException(e, httpCode);
            }

            public final String onCalling(String url) {
                return url;
            }

            public final List<String> onHeader(List<String> header) {
                List<String> a = this.f1857a.m1515a((List) header);
                if (a != null && a.size() > 0) {
                    for (String str : a) {
                        ApiEngineLog.m1530d("CommonApi-" + name, "header=" + str);
                    }
                }
                return a;
            }
        }, supportPostRequest, name);
    }

    public final void callSync(String url, final ICommonApiCallback callback, IApiHeader header, boolean supportPostRequest, final String name) {
        ApiEngineLog.m1530d("CommonApi-" + name, "url=" + url);
        C0479b.m1518a(callback);
        this.f1865a.callSync(url, header.getHeader(), new IHttpCallback(this) {
            private /* synthetic */ C0479b f1860a;

            public final void onSuccess(String response, String str, List<Integer> list) {
                callback.onSuccess(response);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                ApiEngineLog.m1530d("CommonApi-" + name, "response=" + response);
                callback.onException(e, httpCode);
            }

            public final String onCalling(String url) {
                return url;
            }

            public final List<String> onHeader(List<String> header) {
                List<String> a = this.f1860a.m1515a((List) header);
                if (a != null && a.size() > 0) {
                    for (String str : a) {
                        ApiEngineLog.m1530d("CommonApi-" + name, "header=" + str);
                    }
                }
                return a;
            }
        }, supportPostRequest, name);
    }

    public final void callSync(String url, ICommonApiCallback callback, boolean supportPostRequest, String name) {
        ApiEngineLog.m1530d("CommonApi-" + name, "url=" + url);
        C0479b.m1518a(callback);
        this.f1865a.callSync(url, null, new C04784(callback, name), supportPostRequest, name);
    }
}
