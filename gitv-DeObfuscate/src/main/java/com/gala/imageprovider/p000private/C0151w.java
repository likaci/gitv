package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.ArrayList;
import java.util.List;

public final class C0151w {
    private List<C0148v> f604a = new ArrayList();

    public final void m382a(FileRequest fileRequest, IFileCallback iFileCallback) {
        this.f604a.add(new C0149t(fileRequest, iFileCallback));
    }

    public final void m383a(FileRequest fileRequest, IGifCallback iGifCallback) {
        this.f604a.add(new C0150u(fileRequest, iGifCallback));
    }

    public final void m385a(String str) {
        synchronized (this.f604a) {
            for (C0148v c0148v : this.f604a) {
                if (c0148v instanceof C0149t) {
                    ((C0149t) c0148v).mo670a(str);
                } else if (c0148v instanceof C0150u) {
                    ((C0150u) c0148v).mo670a(str);
                }
            }
            this.f604a.clear();
        }
    }

    public final void m384a(Exception exception) {
        synchronized (this.f604a) {
            for (C0148v c0148v : this.f604a) {
                ((C0149t) c0148v).mo669a(exception);
            }
            this.f604a.clear();
        }
    }
}
