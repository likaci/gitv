package com.gala.video.api.http;

import com.gala.video.api.log.ApiEngineLog;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.cybergarage.soap.SOAP;

public abstract class b implements IHttpEngine {
    protected long a;
    private final ExecutorService f790a;
    protected boolean f791a;
    protected long b;

    class AnonymousClass1 implements ThreadFactory {
        private /* synthetic */ int a;
        private /* synthetic */ String f792a;
        private AtomicInteger f793a = new AtomicInteger(0);

        AnonymousClass1(String str, int i) {
            this.f792a = str;
            this.a = i;
        }

        public final Thread newThread(Runnable runnable) {
            return new a(runnable, this.f792a + SOAP.DELIM + this.f793a.incrementAndGet() + "/" + this.a);
        }
    }

    class AnonymousClass2 implements ThreadFactory {
        private /* synthetic */ int a;
        private /* synthetic */ String f794a;
        private AtomicInteger f795a = new AtomicInteger(0);

        AnonymousClass2(String str, int i) {
            this.f794a = str;
            this.a = i;
        }

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, this.f794a + SOAP.DELIM + this.f795a.incrementAndGet() + "/" + this.a);
        }
    }

    class AnonymousClass3 implements Runnable {
        private /* synthetic */ IHttpCallback a;
        private /* synthetic */ b f796a;
        private /* synthetic */ String f797a;
        private /* synthetic */ List f798a;
        private /* synthetic */ boolean f799a;

        AnonymousClass3(b bVar, String str, List list, IHttpCallback iHttpCallback, boolean z, String str2) {
            this.f796a = bVar;
            this.f797a = str;
            this.f798a = list;
            this.a = iHttpCallback;
            this.f799a = z;
        }

        public final void run() {
            if (this.f796a.f791a && System.currentTimeMillis() < this.f796a.a) {
                ApiEngineLog.d("syncCall", "delay " + this.f796a.a);
                try {
                    Thread.sleep(this.f796a.b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.f796a.a(this.f797a, this.f798a, this.a, this.f799a);
        }
    }

    protected abstract String a();

    protected abstract String a(String str, List<String> list, boolean z, List<Integer> list2) throws Exception;

    public b(String str) {
        int i = 2;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ApiEngineLog.d("syncCall", "cpu: " + availableProcessors);
        if (availableProcessors > 2) {
            i = 4;
        }
        this(str, i);
    }

    public b(String str, int i) {
        this.f791a = false;
        this.a = 0;
        this.b = 0;
        int min = Math.min(i, 4);
        this.f790a = Executors.newFixedThreadPool(min, new AnonymousClass1(str, min));
    }

    public b(String str, int i, boolean z) {
        this.f791a = false;
        this.a = 0;
        this.b = 0;
        this.f791a = z;
        int min = Math.min(i, 4);
        this.f790a = Executors.newFixedThreadPool(min, new AnonymousClass2(str, min));
    }

    public void setDelayDuration(long time) {
        this.b = time;
        this.a = System.currentTimeMillis() + time;
    }

    public void call(String url, List<String> extraInfo, IHttpCallback callback, boolean supportPostRequest, String name) {
        this.f790a.execute(new AnonymousClass3(this, url, extraInfo, callback, supportPostRequest, name));
    }

    private void a(String str, List<String> list, IHttpCallback iHttpCallback, boolean z) {
        String str2 = "";
        List arrayList = new ArrayList(2);
        try {
            str2 = a(iHttpCallback.onCalling(str), iHttpCallback.onHeader(list), z, arrayList);
            iHttpCallback.onSuccess(str2, a(), arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            iHttpCallback.onException(e, a(), str2, arrayList);
        } catch (Error e2) {
            e2.printStackTrace();
            iHttpCallback.onException(new Exception(e2.fillInStackTrace()), a(), str2, arrayList);
        }
    }

    public void callSync(String url, List<String> extraInfo, IHttpCallback callback, boolean supportPostRequest, String str) {
        try {
            a(url, (List) extraInfo, callback, supportPostRequest);
        } catch (Exception e) {
            callback.onException(e, a(), "", null);
        }
    }
}
