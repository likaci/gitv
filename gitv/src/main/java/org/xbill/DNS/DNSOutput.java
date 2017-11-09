package org.xbill.DNS;

public class DNSOutput {
    private byte[] array;
    private int pos;
    private int saved_pos;

    public DNSOutput(int size) {
        this.array = new byte[size];
        this.pos = 0;
        this.saved_pos = -1;
    }

    public DNSOutput() {
        this(32);
    }

    public int current() {
        return this.pos;
    }

    private void check(long val, int bits) {
        long max = 1 << bits;
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(new StringBuffer().append(val).append(" out of range for ").append(bits).append(" bit value").toString());
        }
    }

    private void need(int n) {
        if (this.array.length - this.pos < n) {
            int newsize = this.array.length * 2;
            if (newsize < this.pos + n) {
                newsize = this.pos + n;
            }
            byte[] newarray = new byte[newsize];
            System.arraycopy(this.array, 0, newarray, 0, this.pos);
            this.array = newarray;
        }
    }

    public void jump(int index) {
        if (index > this.pos) {
            throw new IllegalArgumentException("cannot jump past end of data");
        }
        this.pos = index;
    }

    public void save() {
        this.saved_pos = this.pos;
    }

    public void restore() {
        if (this.saved_pos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.pos = this.saved_pos;
        this.saved_pos = -1;
    }

    public void writeU8(int val) {
        check((long) val, 8);
        need(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (val & 255);
    }

    public void writeU16(int val) {
        check((long) val, 16);
        need(2);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((val >>> 8) & 255);
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (val & 255);
    }

    public void writeU16At(int val, int where) {
        check((long) val, 16);
        if (where > this.pos - 2) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        int i = where + 1;
        this.array[where] = (byte) ((val >>> 8) & 255);
        where = i + 1;
        this.array[i] = (byte) (val & 255);
    }

    public void writeU32(long val) {
        check(val, 32);
        need(4);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((val >>> 24) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((val >>> 16) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) ((val >>> 8) & 255));
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) ((int) (val & 255));
    }

    public void writeByteArray(byte[] b, int off, int len) {
        need(len);
        System.arraycopy(b, off, this.array, this.pos, len);
        this.pos += len;
    }

    public void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b.length);
    }

    public void writeCountedString(byte[] s) {
        if (s.length > 255) {
            throw new IllegalArgumentException("Invalid counted string");
        }
        need(s.length + 1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        bArr[i] = (byte) (s.length & 255);
        writeByteArray(s, 0, s.length);
    }

    public byte[] toByteArray() {
        byte[] out = new byte[this.pos];
        System.arraycopy(this.array, 0, out, 0, this.pos);
        return out;
    }
}
