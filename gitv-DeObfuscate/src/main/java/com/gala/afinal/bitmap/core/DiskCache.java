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
    private static final String f24a = DiskCache.class.getSimpleName();
    private int f25a;
    private LookupRequest f26a;
    private RandomAccessFile f27a;
    private MappedByteBuffer f28a;
    private FileChannel f29a;
    private Adler32 f30a;
    private byte[] f31a;
    private int f32b;
    private RandomAccessFile f33b;
    private String f34b;
    private byte[] f35b;
    private int f36c;
    private RandomAccessFile f37c;
    private int f38d;
    private RandomAccessFile f39d;
    private int f40e;
    private RandomAccessFile f41e;
    private int f42f;
    private int f43g;
    private int f44h;
    private int f45i;
    private int f46j;

    public static class LookupRequest {
        public byte[] buffer;
        public long key;
        public int length;
    }

    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset) throws IOException {
        this(path, maxEntries, maxBytes, reset, 0);
    }

    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset, int version) throws IOException {
        this.f31a = new byte[32];
        this.f35b = new byte[20];
        this.f30a = new Adler32();
        this.f26a = new LookupRequest();
        File file = new File(path);
        if (file.exists() || file.mkdirs()) {
            this.f34b = path;
            this.f27a = new RandomAccessFile(path + ".idx", "rw");
            this.f33b = new RandomAccessFile(path + ".0", "rw");
            this.f37c = new RandomAccessFile(path + ".1", "rw");
            this.f42f = version;
            if (reset || !m8a()) {
                this.f27a.setLength(0);
                this.f27a.setLength((long) (((maxEntries * 12) << 1) + 32));
                this.f27a.seek(0);
                byte[] bArr = this.f31a;
                m12a(bArr, 0, -1289277392);
                m12a(bArr, 4, maxEntries);
                m12a(bArr, 8, maxBytes);
                m12a(bArr, 12, 0);
                m12a(bArr, 16, 0);
                m12a(bArr, 20, 4);
                m12a(bArr, 24, this.f42f);
                m12a(bArr, 28, m7a(bArr, 28));
                this.f27a.write(bArr);
                this.f33b.setLength(0);
                this.f37c.setLength(0);
                this.f33b.seek(0);
                this.f37c.seek(0);
                m12a(bArr, 0, -1121680112);
                this.f33b.write(bArr, 0, 4);
                this.f37c.write(bArr, 0, 4);
                if (!m8a()) {
                    m8a();
                    throw new IOException("unable to load index");
                }
                return;
            }
            return;
        }
        throw new IOException("unable to make dirs");
    }

    public void delete() {
        m11a(this.f34b + ".idx");
        m11a(this.f34b + ".0");
        m11a(this.f34b + ".1");
    }

    private static void m11a(String str) {
        try {
            new File(str).delete();
        } catch (Throwable th) {
        }
    }

    public void close() {
        syncAll();
        m8a();
    }

    private void m8a() {
        m10a(this.f29a);
        m10a(this.f27a);
        m10a(this.f33b);
        m10a(this.f37c);
    }

    private boolean m13a() {
        try {
            this.f27a.seek(0);
            this.f33b.seek(0);
            this.f37c.seek(0);
            byte[] bArr = this.f31a;
            if (this.f27a.read(bArr) != 32) {
                Log.w(f24a, "cannot read header");
                return false;
            } else if (m16b(bArr, 0) != -1289277392) {
                Log.w(f24a, "cannot read header magic");
                return false;
            } else if (m16b(bArr, 24) != this.f42f) {
                Log.w(f24a, "version mismatch");
                return false;
            } else {
                this.f25a = m16b(bArr, 4);
                this.f32b = m16b(bArr, 8);
                this.f36c = m16b(bArr, 12);
                this.f38d = m16b(bArr, 16);
                this.f40e = m16b(bArr, 20);
                if (m7a(bArr, 28) != m16b(bArr, 28)) {
                    Log.w(f24a, "header checksum does not match");
                    return false;
                } else if (this.f25a <= 0) {
                    Log.w(f24a, "invalid max entries");
                    return false;
                } else if (this.f32b <= 0) {
                    Log.w(f24a, "invalid max bytes");
                    return false;
                } else if (this.f36c != 0 && this.f36c != 1) {
                    Log.w(f24a, "invalid active region");
                    return false;
                } else if (this.f38d < 0 || this.f38d > this.f25a) {
                    Log.w(f24a, "invalid active entries");
                    return false;
                } else if (this.f40e < 4 || this.f40e > this.f32b) {
                    Log.w(f24a, "invalid active bytes");
                    return false;
                } else if (this.f27a.length() != ((long) (((this.f25a * 12) << 1) + 32))) {
                    Log.w(f24a, "invalid index file length");
                    return false;
                } else {
                    bArr = new byte[4];
                    if (this.f33b.read(bArr) != 4) {
                        Log.w(f24a, "cannot read data file magic");
                        return false;
                    } else if (m16b(bArr, 0) != -1121680112) {
                        Log.w(f24a, "invalid data file magic");
                        return false;
                    } else if (this.f37c.read(bArr) != 4) {
                        Log.w(f24a, "cannot read data file magic");
                        return false;
                    } else if (m16b(bArr, 0) != -1121680112) {
                        Log.w(f24a, "invalid data file magic");
                        return false;
                    } else {
                        this.f29a = this.f27a.getChannel();
                        this.f28a = this.f29a.map(MapMode.READ_WRITE, 0, this.f27a.length());
                        this.f28a.order(ByteOrder.LITTLE_ENDIAN);
                        m17b();
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            Log.e(f24a, "loadIndex failed.", e);
            return false;
        }
    }

    private void m17b() throws IOException {
        this.f39d = this.f36c == 0 ? this.f33b : this.f37c;
        this.f41e = this.f36c == 1 ? this.f33b : this.f37c;
        this.f39d.setLength((long) this.f40e);
        this.f39d.seek((long) this.f40e);
        this.f43g = 32;
        this.f44h = 32;
        if (this.f36c == 0) {
            this.f44h += this.f25a * 12;
        } else {
            this.f43g += this.f25a * 12;
        }
    }

    private void m18c() throws IOException {
        this.f36c = 1 - this.f36c;
        this.f38d = 0;
        this.f40e = 4;
        m12a(this.f31a, 12, this.f36c);
        m12a(this.f31a, 16, this.f38d);
        m12a(this.f31a, 20, this.f40e);
        m19d();
        m17b();
        byte[] bArr = new byte[1024];
        this.f28a.position(this.f43g);
        int i = this.f25a * 12;
        while (i > 0) {
            int min = Math.min(i, 1024);
            this.f28a.put(bArr, 0, min);
            i -= min;
        }
        syncIndex();
    }

    private void m19d() {
        m12a(this.f31a, 28, m7a(this.f31a, 28));
        this.f28a.position(0);
        this.f28a.put(this.f31a);
    }

    public void insert(long key, byte[] data) throws IOException {
        if (data.length + 24 > this.f32b) {
            throw new RuntimeException("blob is too large!");
        }
        if ((this.f40e + 20) + data.length > this.f32b || (this.f38d << 1) >= this.f25a) {
            m18c();
        }
        if (!m14a(key, this.f43g)) {
            this.f38d++;
            m12a(this.f31a, 16, this.f38d);
        }
        m9a(key, data, data.length);
        m19d();
    }

    private void m9a(long j, byte[] bArr, int i) throws IOException {
        byte[] bArr2 = this.f35b;
        this.f30a.reset();
        this.f30a.update(bArr);
        int value = (int) this.f30a.getValue();
        long j2 = j;
        for (int i2 = 0; i2 < 8; i2++) {
            bArr2[i2] = (byte) ((int) (255 & j2));
            j2 >>= 8;
        }
        m12a(bArr2, 8, value);
        m12a(bArr2, 12, this.f40e);
        m12a(bArr2, 16, i);
        this.f39d.write(bArr2);
        this.f39d.write(bArr, 0, i);
        this.f28a.putLong(this.f45i, j);
        this.f28a.putInt(this.f45i + 8, this.f40e);
        this.f40e += i + 20;
        m12a(this.f31a, 20, this.f40e);
    }

    public byte[] lookup(long key) throws IOException {
        this.f26a.key = key;
        this.f26a.buffer = null;
        if (lookup(this.f26a)) {
            return this.f26a.buffer;
        }
        return null;
    }

    public boolean lookup(LookupRequest req) throws IOException {
        if (m14a(req.key, this.f43g) && m15a(this.f39d, this.f46j, req)) {
            return true;
        }
        int i = this.f45i;
        if (!m14a(req.key, this.f44h) || !m15a(this.f41e, this.f46j, req)) {
            return false;
        }
        if ((this.f40e + 20) + req.length > this.f32b || (this.f38d << 1) >= this.f25a) {
            return true;
        }
        this.f45i = i;
        try {
            m9a(req.key, req.buffer, req.length);
            this.f38d++;
            m12a(this.f31a, 16, this.f38d);
            m19d();
            return true;
        } catch (Throwable th) {
            Log.e(f24a, "cannot copy over");
            return true;
        }
    }

    private boolean m15a(RandomAccessFile randomAccessFile, int i, LookupRequest lookupRequest) throws IOException {
        byte[] bArr = this.f35b;
        long filePointer = randomAccessFile.getFilePointer();
        try {
            randomAccessFile.seek((long) i);
            if (randomAccessFile.read(bArr) != 20) {
                Log.w(f24a, "cannot read blob header");
                return false;
            }
            int i2;
            long j = (long) (bArr[7] & 255);
            for (i2 = 6; i2 >= 0; i2--) {
                j = (j << 8) | ((long) (bArr[i2] & 255));
            }
            if (j != lookupRequest.key) {
                Log.w(f24a, "blob key does not match: " + j);
                randomAccessFile.seek(filePointer);
                return false;
            }
            i2 = m16b(bArr, 8);
            int b = m16b(bArr, 12);
            if (b != i) {
                Log.w(f24a, "blob offset does not match: " + b);
                randomAccessFile.seek(filePointer);
                return false;
            }
            b = m16b(bArr, 16);
            if (b < 0 || b > (this.f32b - i) - 20) {
                Log.w(f24a, "invalid blob length: " + b);
                randomAccessFile.seek(filePointer);
                return false;
            }
            if (lookupRequest.buffer == null || lookupRequest.buffer.length < b) {
                lookupRequest.buffer = new byte[b];
            }
            byte[] bArr2 = lookupRequest.buffer;
            lookupRequest.length = b;
            if (randomAccessFile.read(bArr2, 0, b) != b) {
                Log.w(f24a, "cannot read blob data");
                randomAccessFile.seek(filePointer);
                return false;
            } else if (m7a(bArr2, b) != i2) {
                Log.w(f24a, "blob checksum does not match: " + i2);
                randomAccessFile.seek(filePointer);
                return false;
            } else {
                randomAccessFile.seek(filePointer);
                return true;
            }
        } catch (Throwable th) {
            Log.e(f24a, "getBlob failed.", th);
        } finally {
            randomAccessFile.seek(filePointer);
        }
    }

    private boolean m14a(long j, int i) {
        int i2 = (int) (j % ((long) this.f25a));
        if (i2 < 0) {
            i2 += this.f25a;
        }
        int i3 = i2;
        while (true) {
            int i4 = (i3 * 12) + i;
            long j2 = this.f28a.getLong(i4);
            int i5 = this.f28a.getInt(i4 + 8);
            if (i5 == 0) {
                this.f45i = i4;
                return false;
            } else if (j2 == j) {
                this.f45i = i4;
                this.f46j = i5;
                return true;
            } else {
                i3++;
                if (i3 >= this.f25a) {
                    i3 = 0;
                }
                if (i3 == i2) {
                    Log.w(f24a, "corrupted index: clear the slot.");
                    this.f28a.putInt(((i3 * 12) + i) + 8, 0);
                }
            }
        }
    }

    public void syncIndex() {
        try {
            this.f28a.force();
        } catch (Throwable th) {
            Log.w(f24a, "sync index failed", th);
        }
    }

    public void syncAll() {
        syncIndex();
        try {
            this.f33b.getFD().sync();
        } catch (Throwable th) {
            Log.w(f24a, "sync data file 0 failed", th);
        }
        try {
            this.f37c.getFD().sync();
        } catch (Throwable th2) {
            Log.w(f24a, "sync data file 1 failed", th2);
        }
    }

    private int m7a(byte[] bArr, int i) {
        this.f30a.reset();
        this.f30a.update(bArr, 0, i);
        return (int) this.f30a.getValue();
    }

    private static void m10a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    private static int m16b(byte[] bArr, int i) {
        return (((bArr[i] & 255) | ((bArr[i + 1] & 255) << 8)) | ((bArr[i + 2] & 255) << 16)) | ((bArr[i + 3] & 255) << 24);
    }

    private static void m12a(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i + i3] = (byte) i2;
            i2 >>= 8;
        }
    }
}
