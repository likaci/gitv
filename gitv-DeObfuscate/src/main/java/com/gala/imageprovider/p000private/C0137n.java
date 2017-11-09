package com.gala.imageprovider.p000private;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public final class C0137n {
    public static Object m349a(HttpEntity httpEntity, C0040l c0040l, String str) throws IOException {
        if (httpEntity == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        long contentLength = httpEntity.getContentLength();
        long j = 0;
        InputStream content = httpEntity.getContent();
        while (true) {
            int read = content.read(bArr);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, read);
            j += (long) read;
            if (c0040l != null) {
                c0040l.callBack(contentLength, j, false);
            }
        }
        if (c0040l != null) {
            c0040l.callBack(contentLength, j, true);
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        content.close();
        return new String(toByteArray, str);
    }
}
