package com.xiaomi.mistatistic.sdk.a;

import java.io.InputStream;

final class c extends InputStream {
    private InputStream a;
    private a b;
    private b c;
    private int d = 0;

    public c(a aVar, InputStream inputStream) {
        this.b = aVar;
        this.a = inputStream;
    }

    public c(b bVar, InputStream inputStream) {
        this.c = bVar;
        this.a = inputStream;
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

    public int available() {
        try {
            return this.a.available();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void close() {
        if (this.b != null) {
            this.b.a();
        }
        if (this.c != null) {
            this.c.a();
        }
        try {
            this.a.close();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void mark(int i) {
        this.a.mark(i);
    }

    public boolean markSupported() {
        return this.a.markSupported();
    }

    public int read() {
        try {
            int read = this.a.read();
            if (read != -1) {
                this.d++;
            }
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public int read(byte[] bArr) {
        try {
            int read = this.a.read(bArr);
            if (read != -1) {
                this.d += read;
            }
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        try {
            int read = this.a.read(bArr, i, i2);
            if (read != -1) {
                this.d += read;
            }
            return read;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public synchronized void reset() {
        try {
            this.a.reset();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public long skip(long j) {
        try {
            return this.a.skip(j);
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }
}
