package org.xbill.DNS;

import java.io.IOException;

public class RPRecord extends Record {
    private static final long serialVersionUID = 8124584364211337460L;
    private Name mailbox;
    private Name textDomain;

    RPRecord() {
    }

    Record getObject() {
        return new RPRecord();
    }

    public RPRecord(Name name, int dclass, long ttl, Name mailbox, Name textDomain) {
        super(name, 17, dclass, ttl);
        this.mailbox = checkName("mailbox", mailbox);
        this.textDomain = checkName("textDomain", textDomain);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.mailbox = new Name(in);
        this.textDomain = new Name(in);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.mailbox = st.getName(origin);
        this.textDomain = st.getName(origin);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.mailbox);
        sb.append(" ");
        sb.append(this.textDomain);
        return sb.toString();
    }

    public Name getMailbox() {
        return this.mailbox;
    }

    public Name getTextDomain() {
        return this.textDomain;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.mailbox.toWire(out, null, canonical);
        this.textDomain.toWire(out, null, canonical);
    }
}
