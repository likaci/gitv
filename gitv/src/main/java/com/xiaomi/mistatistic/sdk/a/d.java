package com.xiaomi.mistatistic.sdk.a;

import java.io.OutputStream;

final class d extends OutputStream {
    private OutputStream a;
    private a b;
    private b c;
    private int d = 0;

    public d(a aVar, OutputStream outputStream) {
        this.a = outputStream;
        this.b = aVar;
    }

    public d(b bVar, OutputStream outputStream) {
        this.a = outputStream;
        this.c = bVar;
    }

    private void a(Exception exception) {
        if (this.b != null) {
            this.b.a(exception);
        }
        if (this.c != null) {
            this.c.a(exception);
        }
    }

    public int a() {
        return this.d;
    }

    public void close() {
        try {
            this.a.close();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void flush() {
        try {
            this.a.flush();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void write(int i) {
        try {
            this.a.write(i);
            this.d++;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void write(byte[] bArr) {
        try {
            this.a.write(bArr);
            this.d += bArr.length;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        try {
            this.a.write(bArr, i, i2);
            this.d += i2;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }
}
