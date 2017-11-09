package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public final class C0154z {
    private ArrayBlockingQueue<Runnable> f607a;

    public C0154z(int i) {
        this.f607a = new ArrayBlockingQueue(i);
    }

    public final ArrayBlockingQueue<Runnable> m389a() {
        return this.f607a;
    }

    public final synchronized FileRequest m388a(FileRequest fileRequest) {
        FileRequest fileRequest2;
        Iterator it = this.f607a.iterator();
        while (it.hasNext()) {
            Runnable runnable = (Runnable) it.next();
            if (runnable instanceof C0114C) {
                C0114C c0114c = (C0114C) runnable;
                FileRequest fileRequest3 = c0114c.f519a;
                if (fileRequest3 != null && fileRequest3.equals(fileRequest)) {
                    fileRequest2 = c0114c.f519a;
                    break;
                }
            }
        }
        fileRequest2 = null;
        return fileRequest2;
    }
}
