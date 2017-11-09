package org.xbill.DNS;

import java.io.IOException;

public class URIRecord extends Record {
    private static final long serialVersionUID = 7955422413971804232L;
    private int priority;
    private byte[] target;
    private int weight;

    URIRecord() {
        this.target = new byte[0];
    }

    Record getObject() {
        return new URIRecord();
    }

    public URIRecord(Name name, int dclass, long ttl, int priority, int weight, String target) {
        super(name, 256, dclass, ttl);
        this.priority = Record.checkU16("priority", priority);
        this.weight = Record.checkU16("weight", weight);
        try {
            this.target = Record.byteArrayFromString(target);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.priority = in.readU16();
        this.weight = in.readU16();
        this.target = in.readCountedString();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.priority = st.getUInt16();
        this.weight = st.getUInt16();
        try {
            this.target = Record.byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(new StringBuffer().append(this.priority).append(" ").toString());
        sb.append(new StringBuffer().append(this.weight).append(" ").toString());
        sb.append(Record.byteArrayToString(this.target, true));
        return sb.toString();
    }

    public int getPriority() {
        return this.priority;
    }

    public int getWeight() {
        return this.weight;
    }

    public String getTarget() {
        return Record.byteArrayToString(this.target, false);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.priority);
        out.writeU16(this.weight);
        out.writeCountedString(this.target);
    }
}
