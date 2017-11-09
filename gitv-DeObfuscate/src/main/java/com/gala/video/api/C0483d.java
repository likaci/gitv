package com.gala.video.api;

import com.gala.video.api.http.HttpEngineFactory;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.api.http.IHttpEngine;
import com.gala.video.api.log.ApiEngineLog;
import java.util.List;

public final class C0483d extends C0474a implements IPingbackApi {
    private static final C0483d f1870a = new C0483d();
    private IHttpEngine f1871a = HttpEngineFactory.newEngineDelay("PingbackApi", 1, false);

    class C04811 implements IHttpCallback {
        private /* synthetic */ IPinbackCallback f1866a;
        private /* synthetic */ String f1867a;

        C04811(IPinbackCallback iPinbackCallback, String str) {
            this.f1866a = iPinbackCallback;
            this.f1867a = str;
        }

        public final void onSuccess(String str, String str2, List<Integer> list) {
            this.f1866a.onSuccess(this.f1867a);
            ApiEngineLog.m1530d("Pingback", "Done.");
        }

        public final void onException(Exception e, String str, String str2, List<Integer> list) {
            this.f1866a.onException(this.f1867a, e);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }
    }

    class C04822 implements IHttpCallback {
        private /* synthetic */ IPinbackCallback f1868a;
        private /* synthetic */ String f1869a;

        C04822(IPinbackCallback iPinbackCallback, String str) {
            this.f1868a = iPinbackCallback;
            this.f1869a = str;
        }

        public final void onSuccess(String str, String str2, List<Integer> list) {
            this.f1868a.onSuccess(this.f1869a);
            ApiEngineLog.m1530d("Pingback", "Done.");
        }

        public final void onException(Exception e, String str, String str2, List<Integer> list) {
            this.f1868a.onException(this.f1869a, e);
        }

        public final String onCalling(String url) {
            return url;
        }

        public final List<String> onHeader(List<String> list) {
            return null;
        }
    }

    public static C0483d m1519a() {
        return f1870a;
    }

    private C0483d() {
    }

    public final void call(IPinbackCallback callback, String url) {
        C0483d.m1520a(callback);
        this.f1871a.call(url, null, new C04811(callback, url), false, null);
    }

    private static void m1520a(IPinbackCallback iPinbackCallback) {
        if (iPinbackCallback == null) {
            throw new NullPointerException("A callback is needed for PingbackApi call().");
        }
    }

    public final void setDelayDuration(long time) {
        this.f1871a.setDelayDuration(time);
    }

    public final void callSync(IPinbackCallback callback, String url) {
        C0483d.m1520a(callback);
        this.f1871a.callSync(url, null, new C04822(callback, url), false, null);
    }
}
