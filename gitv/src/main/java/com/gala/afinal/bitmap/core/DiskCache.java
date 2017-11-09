package com.gala.afinal.bitmap.core;

import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.zip.Adler32;

public class DiskCache implements Closeable {
    private static final String a = DiskCache.class.getSimpleName();
    private int f34a;
    private LookupRequest f35a;
    private RandomAccessFile f36a;
    private MappedByteBuffer f37a;
    private FileChannel f38a;
    private Adler32 f39a;
    private byte[] f40a;
    private int b;
    private RandomAccessFile f41b;
    private String f42b;
    private byte[] f43b;
    private int c;
    private RandomAccessFile f44c;
    private int d;
    private RandomAccessFile f45d;
    private int e;
    private RandomAccessFile f46e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;

    public static class LookupRequest {
        public byte[] buffer;
        public long key;
        public int length;
    }

    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset) throws IOException {
        this(path, maxEntries, maxBytes, reset, 0);
    }

    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset, int version) throws IOException {
        this.f40a = new byte[32];
        this.f43b = new byte[20];
        this.f39a = new Adler32();
        this.f35a = new LookupRequest();
        File file = new File(path);
        if (file.exists() || file.mkdirs()) {
            this.f42b = path;
            this.f36a = new RandomAccessFile(path + ".idx", "rw");
            this.f41b = new RandomAccessFile(path + ".0", "rw");
            this.f44c = new RandomAccessFile(path + ".1", "rw");
            this.f = version;
            if (reset || !a()) {
                this.f36a.setLength(0);
                this.f36a.setLength((long) (((maxEntries * 12) << 1) + 32));
                this.f36a.seek(0);
                byte[] bArr = this.f40a;
                a(bArr, 0, -1289277392);
                a(bArr, 4, maxEntries);
                a(bArr, 8, maxBytes);
                a(bArr, 12, 0);
                a(bArr, 16, 0);
                a(bArr, 20, 4);
                a(bArr, 24, this.f);
                a(bArr, 28, a(bArr, 28));
                this.f36a.write(bArr);
                this.f41b.setLength(0);
                this.f44c.setLength(0);
                this.f41b.seek(0);
                this.f44c.seek(0);
                a(bArr, 0, -1121680112);
                this.f41b.write(bArr, 0, 4);
                this.f44c.write(bArr, 0, 4);
                if (!a()) {
                    a();
                    throw new IOException("unable to load index");
                }
                return;
            }
            return;
        }
        throw new IOException("unable to make dirs");
    }

    public void delete() {
        a(this.f42b + ".idx");
        a(this.f42b + ".0");
        a(this.f42b + ".1");
    }

    private static void a(String str) {
        try {
            new File(str).delete();
        } catch (Throwable th) {
        }
    }

    public void close() {
        syncAll();
        a();
    }

    private void a() {
        a(this.f38a);
        a(this.f36a);
        a(this.f41b);
        a(this.f44c);
    }

    private boolean m11a() {
        try {
            this.f36a.seek(0);
            this.f41b.seek(0);
            this.f44c.seek(0);
            byte[] bArr = this.f40a;
            if (this.f36a.read(bArr) != 32) {
                Log.w(a, "cannot read header");
                return false;
            } else if (b(bArr, 0) != -1289277392) {
                Log.w(a, "cannot read header magic");
                return false;
            } else if (b(bArr, 24) != this.f) {
                Log.w(a, "version mismatch");
                return false;
            } else {
                this.f34a = b(bArr, 4);
                this.b = b(bArr, 8);
                this.c = b(bArr, 12);
                this.d = b(bArr, 16);
                this.e = b(bArr, 20);
                if (a(bArr, 28) != b(bArr, 28)) {
                    Log.w(a, "header checksum does not match");
                    return false;
                } else if (this.f34a <= 0) {
                    Log.w(a, "invalid max entries");
                    return false;
                } else if (this.b <= 0) {
                    Log.w(a, "invalid max bytes");
                    return false;
                } else if (this.c != 0 && this.c != 1) {
                    Log.w(a, "invalid active region");
                    return false;
                } else if (this.d < 0 || this.d > this.f34a) {
                    Log.w(a, "invalid active entries");
                    return false;
                } else if (this.e < 4 || this.e > this.b) {
                    Log.w(a, "invalid active bytes");
                    return false;
                } else if (this.f36a.length() != ((long) (((this.f34a * 12) << 1) + 32))) {
                    Log.w(a, "invalid index file length");
                    return false;
                } else {
                    bArr = new byte[4];
                    if (this.f41b.read(bArr) != 4) {
                        Log.w(a, "cannot read data file magic");
                        return false;
                    } else if (b(bArr, 0) != -1121680112) {
                        Log.w(a, "invalid data file magic");
                        return false;
                    } else if (this.f44c.read(bArr) != 4) {
                        Log.w(a, "cannot read data file magic");
                        return false;
                    } else if (b(bArr, 0) != -1121680112) {
                        Log.w(a, "invalid data file magic");
                        return false;
                    } else {
                        this.f38a = this.f36a.getChannel();
                        this.f37a = this.f38a.map(MapMode.READ_WRITE, 0, this.f36a.length());
                        this.f37a.order(ByteOrder.LITTLE_ENDIAN);
                        b();
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(a, "loadIndex failed.", e);
            return false;
        }
    }

    private void b() throws IOException {
        this.f45d = this.c == 0 ? this.f41b : this.f44c;
        this.f46e = this.c == 1 ? this.f41b : this.f44c;
        this.f45d.setLength((long) this.e);
        this.f45d.seek((long) this.e);
        this.g = 32;
        this.h = 32;
        if (this.c == 0) {
            this.h += this.f34a * 12;
        } else {
            this.g += this.f34a * 12;
        }
    }

    private void c() throws IOException {
        this.c = 1 - this.c;
        this.d = 0;
        this.e = 4;
        a(this.f40a, 12, this.c);
        a(this.f40a, 16, this.d);
        a(this.f40a, 20, this.e);
        d();
        b();
        byte[] bArr = new byte[1024];
        this.f37a.position(this.g);
        int i = this.f34a * 12;
        while (i > 0) {
            int min = Math.min(i, 1024);
            this.f37a.put(bArr, 0, min);
            i -= min;
        }
        syncIndex();
    }

    private void d() {
        a(this.f40a, 28, a(this.f40a, 28));
        this.f37a.position(0);
        this.f37a.put(this.f40a);
    }

    public void insert(long key, byte[] data) throws IOException {
        if (data.length + 24 > this.b) {
            throw new RuntimeException("blob is too large!");
        }
        if ((this.e + 20) + data.length > this.b || (this.d << 1) >= this.f34a) {
            c();
        }
        if (!a(key, this.g)) {
            this.d++;
            a(this.f40a, 16, this.d);
        }
        a(key, data, data.length);
        d();
    }

    private void a(long j, byte[] bArr, int i) throws IOException {
        byte[] bArr2 = this.f43b;
        this.f39a.reset();
        this.f39a.update(bArr);
        int value = (int) this.f39a.getValue();
        long j2 = j;
        for (int i2 = 0; i2 < 8; i2++) {
            bArr2[i2] = (byte) ((int) (255 & j2));
            j2 >>= 8;
        }
        a(bArr2, 8, value);
        a(bArr2, 12, this.e);
        a(bArr2, 16, i);
        this.f45d.write(bArr2);
        this.f45d.write(bArr, 0, i);
        this.f37a.putLong(this.i, j);
        this.f37a.putInt(this.i + 8, this.e);
        this.e += i + 20;
        a(this.f40a, 20, this.e);
    }

    public byte[] lookup(long key) throws IOException {
        this.f35a.key = key;
        this.f35a.buffer = null;
        if (lookup(this.f35a)) {
            return this.f35a.buffer;
        }
        return null;
    }

    public boolean lookup(LookupRequest req) throws IOException {
        if (a(req.key, this.g) && a(this.f45d, this.j, req)) {
            return true;
        }
        int i = this.i;
        if (!a(req.key, this.h) || !a(this.f46e, this.j, req)) {
            return false;
        }
        if ((this.e + 20) + req.length > this.b || (this.d << 1) >= this.f34a) {
            return true;
        }
        this.i = i;
        try {
            a(req.key, req.buffer, req.length);
            this.d++;
            a(this.f40a, 16, this.d);
            d();
            return true;
        } catch (Throwable th) {
            Log.e(a, "cannot copy over");
            return true;
        }
    }

    private boolean a(RandomAccessFile randomAccessFile, int i, LookupRequest lookupRequest) throws IOException {
        byte[] bArr = this.f43b;
        long filePointer = randomAccessFile.getFilePointer();
        try {
            randomAccessFile.seek((long) i);
            if (randomAccessFile.read(bArr) != 20) {
                Log.w(a, "cannot read blob header");
                return false;
            }
            int i2;
            long j = (long) (bArr[7] & 255);
            for (i2 = 6; i2 >= 0; i2--) {
                j = (j << 8) | ((long) (bArr[i2] & 255));
            }
            if (j != lookupRequest.key) {
                Log.w(a, "blob key does not match: " + j);
                randomAccessFile.seek(filePointer);
                return false;
            }
            i2 = b(bArr, 8);
            int b = b(bArr, 12);
            if (b != i) {
                Log.w(a, "blob offset does not match: " + b);
                randomAccessFile.seek(filePointer);
                return false;
            }
            b = b(bArr, 16);
            if (b < 0 || b > (this.b - i) - 20) {
                Log.w(a, "invalid blob length: " + b);
                randomAccessFile.seek(filePointer);
                return false;
            }
            if (lookupRequest.buffer == null || lookupRequest.buffer.length < b) {
                lookupRequest.buffer = new byte[b];
            }
            byte[] bArr2 = lookupRequest.buffer;
            lookupRequest.length = b;
            if (randomAccessFile.read(bArr2, 0, b) != b) {
                Log.w(a, "cannot read blob data");
                randomAccessFile.seek(filePointer);
                return false;
            } else if (a(bArr2, b) != i2) {
                Log.w(a, "blob checksum does not match: " + i2);
                randomAccessFile.seek(filePointer);
                return false;
            } else {
                randomAccessFile.seek(filePointer);
                return true;
            }
        } catch (Throwable th) {
            Log.e(a, "getBlob failed.", th);
        } finally {
            randomAccessFile.seek(filePointer);
        }
    }

    private boolean a(long j, int i) {
        int i2 = (int) (j % ((long) this.f34a));
        if (i2 < 0) {
            i2 += this.f34a;
        }
        int i3 = i2;
        while (true) {
            int i4 = (i3 * 12) + i;
            long j2 = this.f37a.getLong(i4);
            int i5 = this.f37a.getInt(i4 + 8);
            if (i5 == 0) {
                this.i = i4;
                return false;
            } else if (j2 == j) {
                this.i = i4;
                this.j = i5;
                return true;
            } else {
                i3++;
                if (i3 >= this.f34a) {
                    i3 = 0;
                }
                if (i3 == i2) {
                    Log.w(a, "corrupted index: clear the slot.");
                    this.f37a.putInt(((i3 * 12) + i) + 8, 0);
                }
            }
        }
    }

    public void syncIndex() {
        try {
            this.f37a.force();
        } catch (Throwable th) {
            Log.w(a, "sync index failed", th);
        }
    }

    public void syncAll() {
        syncIndex();
        try {
            this.f41b.getFD().sync();
        } catch (Throwable th) {
            Log.w(a, "sync data file 0 failed", th);
        }
        try {
            this.f44c.getFD().sync();
        } catch (Throwable th2) {
            Log.w(a, "sync data file 1 failed", th2);
        }
    }

    private int a(byte[] bArr, int i) {
        this.f39a.reset();
        this.f39a.update(bArr, 0, i);
        return (int) this.f39a.getValue();
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    private static int b(byte[] bArr, int i) {
        return (((bArr[i] & 255) | ((bArr[i + 1] & 255) << 8)) | ((bArr[i + 2] & 255) << 16)) | ((bArr[i + 3] & 255) << 24);
    }

    private static void a(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i + i3] = (byte) i2;
            i2 >>= 8;
        }
    }
}
