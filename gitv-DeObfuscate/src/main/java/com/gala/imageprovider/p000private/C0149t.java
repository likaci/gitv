package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;

public final class C0149t extends C0148v {
    private IFileCallback f602a;

    public C0149t(FileRequest fileRequest, IFileCallback iFileCallback) {
        super(fileRequest);
        this.f602a = iFileCallback;
    }

    public final void mo670a(String str) {
        this.f602a.onSuccess(m375a(), str);
    }

    public final void mo669a(Exception exception) {
        this.f602a.onFailure(m375a(), exception);
    }
}
