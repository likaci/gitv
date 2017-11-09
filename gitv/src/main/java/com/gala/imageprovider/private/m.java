package com.gala.imageprovider.private;

import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public final class m {
    private boolean a = false;

    public final boolean m12a() {
        return this.a;
    }

    public final void a() {
        this.a = true;
    }

    public final Object a(HttpEntity httpEntity, l lVar, String str, boolean z) throws IOException {
        if (TextUtils.isEmpty(str) || str.trim().length() == 0) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (this.a) {
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
        if (this.a) {
            return file;
        }
        InputStream content = httpEntity.getContent();
        long contentLength = httpEntity.getContentLength() + j;
        if (j >= contentLength || this.a) {
            return file;
        }
        byte[] bArr = new byte[1024];
        long j2 = j;
        while (!this.a && j2 < contentLength) {
            int read = content.read(bArr, 0, 1024);
            if (read <= 0) {
                break;
            }
            fileOutputStream.write(bArr, 0, read);
            j2 += (long) read;
            lVar.callBack(contentLength, j2, false);
        }
        lVar.callBack(contentLength, j2, true);
        if (!this.a || j2 >= contentLength) {
            return file;
        }
        throw new IOException("user stop download thread");
    }
}
