package org.xbill.DNS;

import java.io.IOException;
import java.util.BitSet;
import org.xbill.DNS.Tokenizer.Token;

public class NXTRecord extends Record {
    private static final long serialVersionUID = -8851454400765507520L;
    private BitSet bitmap;
    private Name next;

    NXTRecord() {
    }

    Record getObject() {
        return new NXTRecord();
    }

    public NXTRecord(Name name, int dclass, long ttl, Name next, BitSet bitmap) {
        super(name, 30, dclass, ttl);
        this.next = checkName("next", next);
        this.bitmap = bitmap;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.next = new Name(in);
        this.bitmap = new BitSet();
        int bitmapLength = in.remaining();
        for (int i = 0; i < bitmapLength; i++) {
            int t = in.readU8();
            for (int j = 0; j < 8; j++) {
                if (((1 << (7 - j)) & t) != 0) {
                    this.bitmap.set((i * 8) + j);
                }
            }
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        Token t;
        this.next = st.getName(origin);
        this.bitmap = new BitSet();
        while (true) {
            t = st.get();
            if (t.isString()) {
                int typecode = Type.value(t.value, true);
                if (typecode > 0 && typecode <= 128) {
                    this.bitmap.set(typecode);
                }
            } else {
                st.unget();
                return;
            }
        }
        throw st.exception(new StringBuffer().append("Invalid type: ").append(t.value).toString());
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.next);
        short length = this.bitmap.length();
        for (short i = (short) 0; i < length; i = (short) (i + 1)) {
            if (this.bitmap.get(i)) {
                sb.append(" ");
                sb.append(Type.string(i));
            }
        }
        return sb.toString();
    }

    public Name getNext() {
        return this.next;
    }

    public BitSet getBitmap() {
        return this.bitmap;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.next.toWire(out, null, canonical);
        int length = this.bitmap.length();
        int i = 0;
        int t = 0;
        while (i < length) {
            t |= this.bitmap.get(i) ? 1 << (7 - (i % 8)) : 0;
            if (i % 8 == 7 || i == length - 1) {
                out.writeU8(t);
                t = 0;
            }
            i++;
        }
    }
}
