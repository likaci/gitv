package org.xbill.DNS;

public class MGRecord extends SingleNameBase {
    private static final long serialVersionUID = -3980055550863644582L;

    MGRecord() {
    }

    Record getObject() {
        return new MGRecord();
    }

    public MGRecord(Name name, int dclass, long ttl, Name mailbox) {
        super(name, 8, dclass, ttl, mailbox, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }
}
