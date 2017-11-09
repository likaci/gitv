package com.gala.imageprovider.private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.List;

public final class r extends s {
    protected final void a(List<FileRequest> list, IFileCallback iFileCallback) {
        Object obj;
        if (iFileCallback == null) {
            obj = null;
        } else if (list == null || list.size() == 0) {
            iFileCallback.onFailure(null, new y("Params is wrong!"));
            obj = null;
        } else {
            int i = 1;
        }
        if (obj != null) {
            for (FileRequest fileRequest : list) {
                Object obj2;
                FileRequest a = this.a.a(fileRequest);
                if (a != null) {
                    a.getSameTaskQueue().a(fileRequest, iFileCallback);
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                if (obj2 == null) {
                    this.a.execute(new A(fileRequest, iFileCallback));
                }
            }
        }
    }

    protected final void a(List<FileRequest> list, IGifCallback iGifCallback) {
        Object obj;
        if (iGifCallback == null) {
            obj = null;
        } else if (list == null || list.size() == 0) {
            iGifCallback.onFailure(null, new y("Params is wrong!"));
            obj = null;
        } else {
            int i = 1;
        }
        if (obj != null) {
            for (FileRequest fileRequest : list) {
                Object obj2;
                FileRequest a = this.a.a(fileRequest);
                if (a != null) {
                    a.getSameTaskQueue().a(fileRequest, iGifCallback);
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                if (obj2 == null) {
                    this.a.execute(new B(fileRequest, iGifCallback));
                }
            }
        }
    }

    protected final void a(C c) {
        this.a.execute(c);
    }

    protected final void a(Runnable runnable) {
        this.a.a(runnable);
    }

    protected final void b(Runnable runnable) {
        this.a.b(runnable);
    }
}
