package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class b extends a implements ICommonApi {
    private static final b a = new b();
    private IHttpEngine f780a = HttpEngineFactory.newCommonEngine("CommonApi", 2);

    class AnonymousClass1 implements IHttpCallback {
        private /* synthetic */ ICommonApiCallback a;
        private /* synthetic */ String f781a;

        AnonymousClass1(ICommonApiCallback iCommonApiCallback, String str) {
            this.a = iCommonApiCallback;
            this.f781a = str;
        }

        public final void onSuccess(String response, String str, List<Integer> list) {
            this.a.onSuccess(response);
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
            ApiEngineLog.d("CommonApi-" + this.f781a, "response=" + response);
            this.a.onException(e, httpCode);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> header) {
            if (header != null && header.size() > 0) {
                for (String str : header) {
                    ApiEngineLog.d("CommonApi-" + this.f781a, "header=" + str);
                }
            }
            return header;
        }
    }

    class AnonymousClass4 implements IHttpCallback {
        private /* synthetic */ ICommonApiCallback a;
        private /* synthetic */ String f786a;

        AnonymousClass4(ICommonApiCallback iCommonApiCallback, String str) {
            this.a = iCommonApiCallback;
            this.f786a = str;
        }

        public final void onSuccess(String response, String str, List<Integer> list) {
            this.a.onSuccess(response);
        }

        public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
            ApiEngineLog.d("CommonApi-" + this.f786a, "response=" + response);
            this.a.onException(e, httpCode);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> header) {
            if (header != null && header.size() > 0) {
                for (String str : header) {
                    ApiEngineLog.d("CommonApi-" + this.f786a, "header=" + str);
                }
            }
            return header;
        }
    }

    public static b a() {
        return a;
    }

    private b() {
    }

    private static void a(ICommonApiCallback iCommonApiCallback) {
        if (iCommonApiCallback == null) {
            throw new NullPointerException("A callback is needed for CommonApi call().");
        }
    }

    public final void call(String url, ICommonApiCallback callback, boolean supportPostRequest, String name) {
        ApiEngineLog.d("CommonApi-" + name, "url=" + url);
        a(callback);
        this.f780a.call(url, null, new AnonymousClass1(callback, name), supportPostRequest, name);
    }

    public final void call(String url, final ICommonApiCallback callback, IApiHeader header, boolean supportPostRequest, final String name) {
        ApiEngineLog.d("CommonApi-" + name, "url=" + url);
        a(callback);
        this.f780a.call(url, header.getHeader(), new IHttpCallback(this) {
            private /* synthetic */ b f782a;

            public final void onSuccess(String response, String str, List<Integer> list) {
                callback.onSuccess(response);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                ApiEngineLog.d("CommonApi-" + name, "response=" + response);
                callback.onException(e, httpCode);
            }

            public final String onCalling(String url) {
                return url;
            }

            public final List<String> onHeader(List<String> header) {
                List<String> a = this.f782a.a((List) header);
                if (a != null && a.size() > 0) {
                    for (String str : a) {
                        ApiEngineLog.d("CommonApi-" + name, "header=" + str);
                    }
                }
                return a;
            }
        }, supportPostRequest, name);
    }

    public final void callSync(String url, final ICommonApiCallback callback, IApiHeader header, boolean supportPostRequest, final String name) {
        ApiEngineLog.d("CommonApi-" + name, "url=" + url);
        a(callback);
        this.f780a.callSync(url, header.getHeader(), new IHttpCallback(this) {
            private /* synthetic */ b f784a;

            public final void onSuccess(String response, String str, List<Integer> list) {
                callback.onSuccess(response);
            }

            public final void onException(Exception e, String httpCode, String response, List<Integer> list) {
                ApiEngineLog.d("CommonApi-" + name, "response=" + response);
                callback.onException(e, httpCode);
            }

            public final String onCalling(String url) {
                return url;
            }

            public final List<String> onHeader(List<String> header) {
                List<String> a = this.f784a.a((List) header);
                if (a != null && a.size() > 0) {
                    for (String str : a) {
                        ApiEngineLog.d("CommonApi-" + name, "header=" + str);
                    }
                }
                return a;
            }
        }, supportPostRequest, name);
    }

    public final void callSync(String url, ICommonApiCallback callback, boolean supportPostRequest, String name) {
        ApiEngineLog.d("CommonApi-" + name, "url=" + url);
        a(callback);
        this.f780a.callSync(url, null, new AnonymousClass4(callback, name), supportPostRequest, name);
    }
}
