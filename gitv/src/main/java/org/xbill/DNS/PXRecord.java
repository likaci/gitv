package org.xbill.DNS;

import java.io.IOException;

public class PXRecord extends Record {
    private static final long serialVersionUID = 1811540008806660667L;
    private Name map822;
    private Name mapX400;
    private int preference;

    PXRecord() {
    }

    Record getObject() {
        return new PXRecord();
    }

    public PXRecord(Name name, int dclass, long ttl, int preference, Name map822, Name mapX400) {
        super(name, 26, dclass, ttl);
        this.preference = checkU16("preference", preference);
        this.map822 = checkName("map822", map822);
        this.mapX400 = checkName("mapX400", mapX400);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.preference = in.readU16();
        this.map822 = new Name(in);
        this.mapX400 = new Name(in);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.preference = st.getUInt16();
        this.map822 = st.getName(origin);
        this.mapX400 = st.getName(origin);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.preference);
        sb.append(" ");
        sb.append(this.map822);
        sb.append(" ");
        sb.append(this.mapX400);
        return sb.toString();
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.preference);
        this.map822.toWire(out, null, canonical);
        this.mapX400.toWire(out, null, canonical);
    }

    public int getPreference() {
        return this.preference;
    }

    public Name getMap822() {
        return this.map822;
    }

    public Name getMapX400() {
        return this.mapX400;
    }
}
