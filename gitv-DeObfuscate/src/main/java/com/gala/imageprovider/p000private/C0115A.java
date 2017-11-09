package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;

public final class C0115A extends C0114C {
    private IFileCallback f524a;

    public C0115A(FileRequest fileRequest, IFileCallback iFileCallback) {
        super(fileRequest);
        this.f524a = iFileCallback;
    }

    public final void mo632a(String str) {
        this.f524a.onSuccess(this.f519a, str);
        this.f519a.getSameTaskQueue().m385a(str);
    }

    public final void mo631a(Exception exception) {
        this.f524a.onFailure(this.f519a, exception);
        this.f519a.getSameTaskQueue().m384a(exception);
    }
}
