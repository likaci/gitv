package org.xbill.DNS;

import java.io.IOException;

public class MINFORecord extends Record {
    private static final long serialVersionUID = -3962147172340353796L;
    private Name errorAddress;
    private Name responsibleAddress;

    MINFORecord() {
    }

    Record getObject() {
        return new MINFORecord();
    }

    public MINFORecord(Name name, int dclass, long ttl, Name responsibleAddress, Name errorAddress) {
        super(name, 14, dclass, ttl);
        this.responsibleAddress = checkName("responsibleAddress", responsibleAddress);
        this.errorAddress = checkName("errorAddress", errorAddress);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.responsibleAddress = new Name(in);
        this.errorAddress = new Name(in);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.responsibleAddress = st.getName(origin);
        this.errorAddress = st.getName(origin);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.responsibleAddress);
        sb.append(" ");
        sb.append(this.errorAddress);
        return sb.toString();
    }

    public Name getResponsibleAddress() {
        return this.responsibleAddress;
    }

    public Name getErrorAddress() {
        return this.errorAddress;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.responsibleAddress.toWire(out, null, canonical);
        this.errorAddress.toWire(out, null, canonical);
    }
}
