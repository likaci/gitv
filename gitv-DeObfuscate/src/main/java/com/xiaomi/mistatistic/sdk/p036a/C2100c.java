package com.xiaomi.mistatistic.sdk.p036a;

import java.io.InputStream;

final class C2100c extends InputStream {
    private InputStream f2153a;
    private C2098a f2154b;
    private C2099b f2155c;
    private int f2156d = 0;

    public C2100c(C2098a c2098a, InputStream inputStream) {
        this.f2154b = c2098a;
        this.f2153a = inputStream;
    }

    public C2100c(C2099b c2099b, InputStream inputStream) {
        this.f2155c = c2099b;
        this.f2153a = inputStream;
    }

    private void m1756a(Exception exception) {
        if (this.f2154b != null) {
            this.f2154b.m1750a(exception);
        }
        if (this.f2155c != null) {
            this.f2155c.m1754a(exception);
        }
    }

    public int m1757a() {
        return this.f2156d;
    }

    public int available() {
        try {
            return this.f2153a.available();
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public void close() {
        if (this.f2154b != null) {
            this.f2154b.m1748a();
        }
        if (this.f2155c != null) {
            this.f2155c.m1752a();
        }
        try {
            this.f2153a.close();
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public void mark(int i) {
        this.f2153a.mark(i);
    }

    public boolean markSupported() {
        return this.f2153a.markSupported();
    }

    public int read() {
        try {
            int read = this.f2153a.read();
            if (read != -1) {
                this.f2156d++;
            }
            return read;
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public int read(byte[] bArr) {
        try {
            int read = this.f2153a.read(bArr);
            if (read != -1) {
                this.f2156d += read;
            }
            return read;
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        try {
            int read = this.f2153a.read(bArr, i, i2);
            if (read != -1) {
                this.f2156d += read;
            }
            return read;
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public synchronized void reset() {
        try {
            this.f2153a.reset();
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }

    public long skip(long j) {
        try {
            return this.f2153a.skip(j);
        } catch (Exception e) {
            m1756a(e);
            throw e;
        }
    }
}
