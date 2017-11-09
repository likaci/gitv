package org.xbill.DNS;

public class DNSInput {
    private byte[] array;
    private int end = this.array.length;
    private int pos = 0;
    private int saved_end = -1;
    private int saved_pos = -1;

    public DNSInput(byte[] input) {
        this.array = input;
    }

    public int current() {
        return this.pos;
    }

    public int remaining() {
        return this.end - this.pos;
    }

    private void require(int n) throws WireParseException {
        if (n > remaining()) {
            throw new WireParseException("end of input");
        }
    }

    public void setActive(int len) {
        if (len > this.array.length - this.pos) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.end = this.pos + len;
    }

    public void clearActive() {
        this.end = this.array.length;
    }

    public int saveActive() {
        return this.end;
    }

    public void restoreActive(int pos) {
        if (pos > this.array.length) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        this.end = pos;
    }

    public void jump(int index) {
        if (index >= this.array.length) {
            throw new IllegalArgumentException("cannot jump past end of input");
        }
        this.pos = index;
        this.end = this.array.length;
    }

    public void save() {
        this.saved_pos = this.pos;
        this.saved_end = this.end;
    }

    public void restore() {
        if (this.saved_pos < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.pos = this.saved_pos;
        this.end = this.saved_end;
        this.saved_pos = -1;
        this.saved_end = -1;
    }

    public int readU8() throws WireParseException {
        require(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public int readU16() throws WireParseException {
        require(2);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        int b1 = bArr[i] & 255;
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        return (b1 << 8) + (bArr[i] & 255);
    }

    public long readU32() throws WireParseException {
        require(4);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        int b1 = bArr[i] & 255;
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        int b2 = bArr[i] & 255;
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        int b3 = bArr[i] & 255;
        bArr = this.array;
        i = this.pos;
        this.pos = i + 1;
        return (((((long) b1) << 24) + ((long) (b2 << 16))) + ((long) (b3 << 8))) + ((long) (bArr[i] & 255));
    }

    public void readByteArray(byte[] b, int off, int len) throws WireParseException {
        require(len);
        System.arraycopy(this.array, this.pos, b, off, len);
        this.pos += len;
    }

    public byte[] readByteArray(int len) throws WireParseException {
        require(len);
        byte[] out = new byte[len];
        System.arraycopy(this.array, this.pos, out, 0, len);
        this.pos += len;
        return out;
    }

    public byte[] readByteArray() {
        int len = remaining();
        byte[] out = new byte[len];
        System.arraycopy(this.array, this.pos, out, 0, len);
        this.pos += len;
        return out;
    }

    public byte[] readCountedString() throws WireParseException {
        require(1);
        byte[] bArr = this.array;
        int i = this.pos;
        this.pos = i + 1;
        return readByteArray(bArr[i] & 255);
    }
}
