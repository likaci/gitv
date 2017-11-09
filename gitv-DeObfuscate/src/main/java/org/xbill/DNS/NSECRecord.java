package org.xbill.DNS;

import java.io.IOException;

public class NSECRecord extends Record {
    private static final long serialVersionUID = -5165065768816265385L;
    private Name next;
    private TypeBitmap types;

    NSECRecord() {
    }

    Record getObject() {
        return new NSECRecord();
    }

    public NSECRecord(Name name, int dclass, long ttl, Name next, int[] types) {
        super(name, 47, dclass, ttl);
        this.next = checkName("next", next);
        for (int check : types) {
            Type.check(check);
        }
        this.types = new TypeBitmap(types);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.next = new Name(in);
        this.types = new TypeBitmap(in);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.next.toWire(out, null, false);
        this.types.toWire(out);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.next = st.getName(origin);
        this.types = new TypeBitmap(st);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.next);
        if (!this.types.empty()) {
            sb.append(' ');
            sb.append(this.types.toString());
        }
        return sb.toString();
    }

    public Name getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int type) {
        return this.types.contains(type);
    }
}
