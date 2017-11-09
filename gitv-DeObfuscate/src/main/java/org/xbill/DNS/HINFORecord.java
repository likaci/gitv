package org.xbill.DNS;

import java.io.IOException;

public class HINFORecord extends Record {
    private static final long serialVersionUID = -4732870630947452112L;
    private byte[] cpu;
    private byte[] os;

    HINFORecord() {
    }

    Record getObject() {
        return new HINFORecord();
    }

    public HINFORecord(Name name, int dclass, long ttl, String cpu, String os) {
        super(name, 13, dclass, ttl);
        try {
            this.cpu = byteArrayFromString(cpu);
            this.os = byteArrayFromString(os);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.cpu = in.readCountedString();
        this.os = in.readCountedString();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        try {
            this.cpu = byteArrayFromString(st.getString());
            this.os = byteArrayFromString(st.getString());
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    public String getCPU() {
        return byteArrayToString(this.cpu, false);
    }

    public String getOS() {
        return byteArrayToString(this.os, false);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.cpu);
        out.writeCountedString(this.os);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(byteArrayToString(this.cpu, true));
        sb.append(" ");
        sb.append(byteArrayToString(this.os, true));
        return sb.toString();
    }
}
