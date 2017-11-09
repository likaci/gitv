package org.xbill.DNS;

import java.io.IOException;

public class NAPTRRecord extends Record {
    private static final long serialVersionUID = 5191232392044947002L;
    private byte[] flags;
    private int order;
    private int preference;
    private byte[] regexp;
    private Name replacement;
    private byte[] service;

    NAPTRRecord() {
    }

    Record getObject() {
        return new NAPTRRecord();
    }

    public NAPTRRecord(Name name, int dclass, long ttl, int order, int preference, String flags, String service, String regexp, Name replacement) {
        super(name, 35, dclass, ttl);
        this.order = checkU16("order", order);
        this.preference = checkU16("preference", preference);
        try {
            this.flags = byteArrayFromString(flags);
            this.service = byteArrayFromString(service);
            this.regexp = byteArrayFromString(regexp);
            this.replacement = checkName("replacement", replacement);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.order = in.readU16();
        this.preference = in.readU16();
        this.flags = in.readCountedString();
        this.service = in.readCountedString();
        this.regexp = in.readCountedString();
        this.replacement = new Name(in);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.order = st.getUInt16();
        this.preference = st.getUInt16();
        try {
            this.flags = byteArrayFromString(st.getString());
            this.service = byteArrayFromString(st.getString());
            this.regexp = byteArrayFromString(st.getString());
            this.replacement = st.getName(origin);
        } catch (TextParseException e) {
            throw st.exception(e.getMessage());
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.order);
        sb.append(" ");
        sb.append(this.preference);
        sb.append(" ");
        sb.append(byteArrayToString(this.flags, true));
        sb.append(" ");
        sb.append(byteArrayToString(this.service, true));
        sb.append(" ");
        sb.append(byteArrayToString(this.regexp, true));
        sb.append(" ");
        sb.append(this.replacement);
        return sb.toString();
    }

    public int getOrder() {
        return this.order;
    }

    public int getPreference() {
        return this.preference;
    }

    public String getFlags() {
        return byteArrayToString(this.flags, false);
    }

    public String getService() {
        return byteArrayToString(this.service, false);
    }

    public String getRegexp() {
        return byteArrayToString(this.regexp, false);
    }

    public Name getReplacement() {
        return this.replacement;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.order);
        out.writeU16(this.preference);
        out.writeCountedString(this.flags);
        out.writeCountedString(this.service);
        out.writeCountedString(this.regexp);
        this.replacement.toWire(out, null, canonical);
    }

    public Name getAdditionalName() {
        return this.replacement;
    }
}
