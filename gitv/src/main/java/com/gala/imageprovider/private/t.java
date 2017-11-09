package com.gala.imageprovider.private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;

public final class t extends v {
    private IFileCallback a;

    public t(FileRequest fileRequest, IFileCallback iFileCallback) {
        super(fileRequest);
        this.a = iFileCallback;
    }

    public final void a(String str) {
        this.a.onSuccess(a(), str);
    }

    public final void a(Exception exception) {
        this.a.onFailure(a(), exception);
    }
}
