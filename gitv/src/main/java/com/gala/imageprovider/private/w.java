package com.gala.imageprovider.private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.ArrayList;
import java.util.List;

public final class w {
    private List<v> a = new ArrayList();

    public final void a(FileRequest fileRequest, IFileCallback iFileCallback) {
        this.a.add(new t(fileRequest, iFileCallback));
    }

    public final void a(FileRequest fileRequest, IGifCallback iGifCallback) {
        this.a.add(new u(fileRequest, iGifCallback));
    }

    public final void a(String str) {
        synchronized (this.a) {
            for (v vVar : this.a) {
                if (vVar instanceof t) {
                    ((t) vVar).a(str);
                } else if (vVar instanceof u) {
                    ((u) vVar).a(str);
                }
            }
            this.a.clear();
        }
    }

    public final void a(Exception exception) {
        synchronized (this.a) {
            for (v vVar : this.a) {
                ((t) vVar).a(exception);
            }
            this.a.clear();
        }
    }
}
