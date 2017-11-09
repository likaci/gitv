package com.gala.imageprovider.private;

import com.gala.download.base.FileRequest;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public final class z {
    private ArrayBlockingQueue<Runnable> a;

    public z(int i) {
        this.a = new ArrayBlockingQueue(i);
    }

    public final ArrayBlockingQueue<Runnable> a() {
        return this.a;
    }

    public final synchronized FileRequest a(FileRequest fileRequest) {
        FileRequest fileRequest2;
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            Runnable runnable = (Runnable) it.next();
            if (runnable instanceof C) {
                C c = (C) runnable;
                FileRequest fileRequest3 = c.f294a;
                if (fileRequest3 != null && fileRequest3.equals(fileRequest)) {
                    fileRequest2 = c.f294a;
                    break;
                }
            }
        }
        fileRequest2 = null;
        return fileRequest2;
    }
}
