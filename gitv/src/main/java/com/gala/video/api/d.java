package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class d extends a implements IPingbackApi {
    private static final d a = new d();
    private IHttpEngine f787a = HttpEngineFactory.newEngineDelay("PingbackApi", 1, false);

    class AnonymousClass1 implements IHttpCallback {
        private /* synthetic */ IPinbackCallback a;
        private /* synthetic */ String f788a;

        AnonymousClass1(IPinbackCallback iPinbackCallback, String str) {
            this.a = iPinbackCallback;
            this.f788a = str;
        }

        public final void onSuccess(String str, String str2, List<Integer> list) {
            this.a.onSuccess(this.f788a);
            ApiEngineLog.d("Pingback", "Done.");
        }

        public final void onException(Exception e, String str, String str2, List<Integer> list) {
            this.a.onException(this.f788a, e);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }
    }

    class AnonymousClass2 implements IHttpCallback {
        private /* synthetic */ IPinbackCallback a;
        private /* synthetic */ String f789a;

        AnonymousClass2(IPinbackCallback iPinbackCallback, String str) {
            this.a = iPinbackCallback;
            this.f789a = str;
        }

        public final void onSuccess(String str, String str2, List<Integer> list) {
            this.a.onSuccess(this.f789a);
            ApiEngineLog.d("Pingback", "Done.");
        }

        public final void onException(Exception e, String str, String str2, List<Integer> list) {
            this.a.onException(this.f789a, e);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }
    }

    public static d a() {
        return a;
    }

    private d() {
    }

    public final void call(IPinbackCallback callback, String url) {
        a(callback);
        this.f787a.call(url, null, new AnonymousClass1(callback, url), false, null);
    }

    private static void a(IPinbackCallback iPinbackCallback) {
        if (iPinbackCallback == null) {
            throw new NullPointerException("A callback is needed for PingbackApi call().");
        }
    }

    public final void setDelayDuration(long time) {
        this.f787a.setDelayDuration(time);
    }

    public final void callSync(IPinbackCallback callback, String url) {
        a(callback);
        this.f787a.callSync(url, null, new AnonymousClass2(callback, url), false, null);
    }
}
