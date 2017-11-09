package com.gala.imageprovider.private;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public final class n {
    public static Object a(HttpEntity httpEntity, l lVar, String str) throws IOException {
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
            if (lVar != null) {
                lVar.callBack(contentLength, j, false);
            }
        }
        if (lVar != null) {
            lVar.callBack(contentLength, j, true);
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        content.close();
        return new String(toByteArray, str);
    }
}
