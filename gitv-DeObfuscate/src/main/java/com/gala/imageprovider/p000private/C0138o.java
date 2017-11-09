package com.gala.imageprovider.p000private;

import android.content.Context;
import com.gala.download.base.FileRequest;

public final class C0138o {
    private static C0138o f573a = new C0138o();
    private C0141p f574a = new C0141p();

    private C0138o() {
    }

    public static C0138o m350a() {
        return f573a;
    }

    public final void m354a(Context context) {
        this.f574a.m362a(context);
    }

    public static void m351a() {
    }

    public final String m352a(FileRequest fileRequest) {
        return this.f574a.m360a(fileRequest);
    }

    public final String m353a(FileRequest fileRequest, byte[] bArr) {
        return this.f574a.m361a(fileRequest, bArr);
    }
}
