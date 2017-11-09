package com.gala.imageprovider.private;

import android.content.Context;
import com.gala.download.base.FileRequest;

public final class o {
    private static o a = new o();
    private p f296a = new p();

    private o() {
    }

    public static o a() {
        return a;
    }

    public final void a(Context context) {
        this.f296a.a(context);
    }

    public static void m69a() {
    }

    public final String a(FileRequest fileRequest) {
        return this.f296a.a(fileRequest);
    }

    public final String a(FileRequest fileRequest, byte[] bArr) {
        return this.f296a.a(fileRequest, bArr);
    }
}
