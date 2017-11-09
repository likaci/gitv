package com.gala.video.api.http;

import com.gala.video.api.log.ApiEngineLog;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.cybergarage.soap.SOAP;

public abstract class C0489b implements IHttpEngine {
    protected long f1885a;
    private final ExecutorService f1886a;
    protected boolean f1887a;
    protected long f1888b;

    class C04861 implements ThreadFactory {
        private /* synthetic */ int f1874a;
        private /* synthetic */ String f1875a;
        private AtomicInteger f1876a = new AtomicInteger(0);

        C04861(String str, int i) {
            this.f1875a = str;
            this.f1874a = i;
        }

        public final Thread newThread(Runnable runnable) {
            return new C0485a(runnable, this.f1875a + SOAP.DELIM + this.f1876a.incrementAndGet() + "/" + this.f1874a);
        }
    }

    class C04872 implements ThreadFactory {
        private /* synthetic */ int f1877a;
        private /* synthetic */ String f1878a;
        private AtomicInteger f1879a = new AtomicInteger(0);

        C04872(String str, int i) {
            this.f1878a = str;
            this.f1877a = i;
        }

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, this.f1878a + SOAP.DELIM + this.f1879a.incrementAndGet() + "/" + this.f1877a);
        }
    }

    class C04883 implements Runnable {
        private /* synthetic */ IHttpCallback f1880a;
        private /* synthetic */ C0489b f1881a;
        private /* synthetic */ String f1882a;
        private /* synthetic */ List f1883a;
        private /* synthetic */ boolean f1884a;

        C04883(C0489b c0489b, String str, List list, IHttpCallback iHttpCallback, boolean z, String str2) {
            this.f1881a = c0489b;
            this.f1882a = str;
            this.f1883a = list;
            this.f1880a = iHttpCallback;
            this.f1884a = z;
        }

        public final void run() {
            if (this.f1881a.f1887a && System.currentTimeMillis() < this.f1881a.f1885a) {
                ApiEngineLog.m1530d("syncCall", "delay " + this.f1881a.f1885a);
                try {
                    Thread.sleep(this.f1881a.f1888b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.f1881a.m1523a(this.f1882a, this.f1883a, this.f1880a, this.f1884a);
        }
    }

    protected abstract String mo1055a();

    protected abstract String mo1056a(String str, List<String> list, boolean z, List<Integer> list2) throws Exception;

    public C0489b(String str) {
        int i = 2;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ApiEngineLog.m1530d("syncCall", "cpu: " + availableProcessors);
        if (availableProcessors > 2) {
            i = 4;
        }
        this(str, i);
    }

    public C0489b(String str, int i) {
        this.f1887a = false;
        this.f1885a = 0;
        this.f1888b = 0;
        int min = Math.min(i, 4);
        this.f1886a = Executors.newFixedThreadPool(min, new C04861(str, min));
    }

    public C0489b(String str, int i, boolean z) {
        this.f1887a = false;
        this.f1885a = 0;
        this.f1888b = 0;
        this.f1887a = z;
        int min = Math.min(i, 4);
        this.f1886a = Executors.newFixedThreadPool(min, new C04872(str, min));
    }

    public void setDelayDuration(long time) {
        this.f1888b = time;
        this.f1885a = System.currentTimeMillis() + time;
    }

    public void call(String url, List<String> extraInfo, IHttpCallback callback, boolean supportPostRequest, String name) {
        this.f1886a.execute(new C04883(this, url, extraInfo, callback, supportPostRequest, name));
    }

    private void m1523a(String str, List<String> list, IHttpCallback iHttpCallback, boolean z) {
        String str2 = "";
        List arrayList = new ArrayList(2);
        try {
            str2 = mo1056a(iHttpCallback.onCalling(str), iHttpCallback.onHeader(list), z, arrayList);
            iHttpCallback.onSuccess(str2, mo1055a(), arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            iHttpCallback.onException(e, mo1055a(), str2, arrayList);
        } catch (Error e2) {
            e2.printStackTrace();
            iHttpCallback.onException(new Exception(e2.fillInStackTrace()), mo1055a(), str2, arrayList);
        }
    }

    public void callSync(String url, List<String> extraInfo, IHttpCallback callback, boolean supportPostRequest, String str) {
        try {
            m1523a(url, (List) extraInfo, callback, supportPostRequest);
        } catch (Exception e) {
            callback.onException(e, mo1055a(), "", null);
        }
    }
}
