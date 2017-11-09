package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.List;

public final class C0144r extends C0143s {
    protected final void m372a(List<FileRequest> list, IFileCallback iFileCallback) {
        Object obj;
        if (iFileCallback == null) {
            obj = null;
        } else if (list == null || list.size() == 0) {
            iFileCallback.onFailure(null, new C0153y("Params is wrong!"));
            obj = null;
        } else {
            int i = 1;
        }
        if (obj != null) {
            for (FileRequest fileRequest : list) {
                Object obj2;
                FileRequest a = this.a.m388a(fileRequest);
                if (a != null) {
                    a.getSameTaskQueue().m382a(fileRequest, iFileCallback);
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                if (obj2 == null) {
                    this.a.execute(new C0115A(fileRequest, iFileCallback));
                }
            }
        }
    }

    protected final void m373a(List<FileRequest> list, IGifCallback iGifCallback) {
        Object obj;
        if (iGifCallback == null) {
            obj = null;
        } else if (list == null || list.size() == 0) {
            iGifCallback.onFailure(null, new C0153y("Params is wrong!"));
            obj = null;
        } else {
            int i = 1;
        }
        if (obj != null) {
            for (FileRequest fileRequest : list) {
                Object obj2;
                FileRequest a = this.a.m388a(fileRequest);
                if (a != null) {
                    a.getSameTaskQueue().m383a(fileRequest, iGifCallback);
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                if (obj2 == null) {
                    this.a.execute(new C0119B(fileRequest, iGifCallback));
                }
            }
        }
    }

    protected final void m370a(C0114C c0114c) {
        this.a.execute(c0114c);
    }

    protected final void m371a(Runnable runnable) {
        this.a.m386a(runnable);
    }

    protected final void m374b(Runnable runnable) {
        this.a.m387b(runnable);
    }
}
