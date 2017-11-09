package com.xiaomi.mistatistic.sdk.p036a;

import java.io.OutputStream;

final class C2101d extends OutputStream {
    private OutputStream f2157a;
    private C2098a f2158b;
    private C2099b f2159c;
    private int f2160d = 0;

    public C2101d(C2098a c2098a, OutputStream outputStream) {
        this.f2157a = outputStream;
        this.f2158b = c2098a;
    }

    public C2101d(C2099b c2099b, OutputStream outputStream) {
        this.f2157a = outputStream;
        this.f2159c = c2099b;
    }

    private void m1758a(Exception exception) {
        if (this.f2158b != null) {
            this.f2158b.m1750a(exception);
        }
        if (this.f2159c != null) {
            this.f2159c.m1754a(exception);
        }
    }

    public int m1759a() {
        return this.f2160d;
    }

    public void close() {
        try {
            this.f2157a.close();
        } catch (Exception e) {
            m1758a(e);
            throw e;
        }
    }

    public void flush() {
        try {
            this.f2157a.flush();
        } catch (Exception e) {
            m1758a(e);
            throw e;
        }
    }

    public void write(int i) {
        try {
            this.f2157a.write(i);
            this.f2160d++;
        } catch (Exception e) {
            m1758a(e);
            throw e;
        }
    }

    public void write(byte[] bArr) {
        try {
            this.f2157a.write(bArr);
            this.f2160d += bArr.length;
        } catch (Exception e) {
            m1758a(e);
            throw e;
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        try {
            this.f2157a.write(bArr, i, i2);
            this.f2160d += i2;
        } catch (Exception e) {
            m1758a(e);
            throw e;
        }
    }
}
