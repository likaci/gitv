package com.gala.imageprovider.p000private;

import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public final class C0136m {
    private boolean f572a = false;

    public final boolean m348a() {
        return this.f572a;
    }

    public final void m347a() {
        this.f572a = true;
    }

    public final Object m346a(HttpEntity httpEntity, C0040l c0040l, String str, boolean z) throws IOException {
        if (TextUtils.isEmpty(str) || str.trim().length() == 0) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (this.f572a) {
            return file;
        }
        FileOutputStream fileOutputStream;
        long j = 0;
        if (z) {
            j = file.length();
            fileOutputStream = new FileOutputStream(str, true);
        } else {
            fileOutputStream = new FileOutputStream(str);
        }
        if (this.f572a) {
            return file;
        }
        InputStream content = httpEntity.getContent();
        long contentLength = httpEntity.getContentLength() + j;
        if (j >= contentLength || this.f572a) {
            return file;
        }
        byte[] bArr = new byte[1024];
        long j2 = j;
        while (!this.f572a && j2 < contentLength) {
            int read = content.read(bArr, 0, 1024);
            if (read <= 0) {
                break;
            }
            fileOutputStream.write(bArr, 0, read);
            j2 += (long) read;
            c0040l.callBack(contentLength, j2, false);
        }
        c0040l.callBack(contentLength, j2, true);
        if (!this.f572a || j2 >= contentLength) {
            return file;
        }
        throw new IOException("user stop download thread");
    }
}
