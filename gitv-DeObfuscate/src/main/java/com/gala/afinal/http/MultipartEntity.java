package com.gala.afinal.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.cybergarage.http.HTTP;

class MultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String boundary = null;
    boolean isSetFirst = false;
    boolean isSetLast = false;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    public MultipartEntity() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        while (i < 30) {
            stringBuffer.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
            i++;
        }
        this.boundary = stringBuffer.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!this.isSetFirst) {
            try {
                this.out.write(("--" + this.boundary + HTTP.CRLF).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!this.isSetLast) {
            try {
                this.out.write(("\r\n--" + this.boundary + "--\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isSetLast = true;
        }
    }

    public void addPart(String key, String value) {
        writeFirstBoundaryIfNeeds();
        try {
            this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            this.out.write(value.getBytes());
            this.out.write(("\r\n--" + this.boundary + HTTP.CRLF).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(String key, String fileName, InputStream fin, boolean isLast) {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addPart(java.lang.String r5, java.lang.String r6, java.io.InputStream r7, java.lang.String r8, boolean r9) {
        /*
        r4 = this;
        r4.writeFirstBoundaryIfNeeds();
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006c }
        r1 = "Content-Type: ";
        r0.<init>(r1);	 Catch:{ IOException -> 0x006c }
        r0 = r0.append(r8);	 Catch:{ IOException -> 0x006c }
        r1 = "\r\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x006c }
        r8 = r0.toString();	 Catch:{ IOException -> 0x006c }
        r0 = r4.out;	 Catch:{ IOException -> 0x006c }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006c }
        r2 = "Content-Disposition: form-data; name=\"";
        r1.<init>(r2);	 Catch:{ IOException -> 0x006c }
        r1 = r1.append(r5);	 Catch:{ IOException -> 0x006c }
        r2 = "\"; filename=\"";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006c }
        r1 = r1.append(r6);	 Catch:{ IOException -> 0x006c }
        r2 = "\"\r\n";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006c }
        r1 = r1.toString();	 Catch:{ IOException -> 0x006c }
        r1 = r1.getBytes();	 Catch:{ IOException -> 0x006c }
        r0.write(r1);	 Catch:{ IOException -> 0x006c }
        r0 = r4.out;	 Catch:{ IOException -> 0x006c }
        r1 = r8.getBytes();	 Catch:{ IOException -> 0x006c }
        r0.write(r1);	 Catch:{ IOException -> 0x006c }
        r0 = r4.out;	 Catch:{ IOException -> 0x006c }
        r1 = "Content-Transfer-Encoding: binary\r\n\r\n";
        r1 = r1.getBytes();	 Catch:{ IOException -> 0x006c }
        r0.write(r1);	 Catch:{ IOException -> 0x006c }
        r0 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = new byte[r0];	 Catch:{ IOException -> 0x006c }
    L_0x005e:
        r1 = r7.read(r0);	 Catch:{ IOException -> 0x006c }
        r2 = -1;
        if (r1 == r2) goto L_0x0074;
    L_0x0065:
        r2 = r4.out;	 Catch:{ IOException -> 0x006c }
        r3 = 0;
        r2.write(r0, r3, r1);	 Catch:{ IOException -> 0x006c }
        goto L_0x005e;
    L_0x006c:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00ab }
        r7.close();	 Catch:{ IOException -> 0x00a6 }
    L_0x0073:
        return;
    L_0x0074:
        if (r9 != 0) goto L_0x0098;
    L_0x0076:
        r0 = r4.out;	 Catch:{ IOException -> 0x006c }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006c }
        r2 = "\r\n--";
        r1.<init>(r2);	 Catch:{ IOException -> 0x006c }
        r2 = r4.boundary;	 Catch:{ IOException -> 0x006c }
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006c }
        r2 = "\r\n";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x006c }
        r1 = r1.toString();	 Catch:{ IOException -> 0x006c }
        r1 = r1.getBytes();	 Catch:{ IOException -> 0x006c }
        r0.write(r1);	 Catch:{ IOException -> 0x006c }
    L_0x0098:
        r0 = r4.out;	 Catch:{ IOException -> 0x006c }
        r0.flush();	 Catch:{ IOException -> 0x006c }
        r7.close();	 Catch:{ IOException -> 0x00a1 }
        goto L_0x0073;
    L_0x00a1:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0073;
    L_0x00a6:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0073;
    L_0x00ab:
        r0 = move-exception;
        r7.close();	 Catch:{ IOException -> 0x00b0 }
    L_0x00af:
        throw r0;
    L_0x00b0:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00af;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.http.MultipartEntity.addPart(java.lang.String, java.lang.String, java.io.InputStream, java.lang.String, boolean):void");
    }

    public void addPart(String key, File value, boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return (long) this.out.toByteArray().length;
    }

    public Header getContentType() {
        return new BasicHeader(HTTP.CONTENT_TYPE, "multipart/form-data; boundary=" + this.boundary);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        outstream.write(this.out.toByteArray());
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }
}
