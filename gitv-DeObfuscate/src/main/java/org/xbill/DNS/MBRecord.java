package org.xbill.DNS;

public class MBRecord extends SingleNameBase {
    private static final long serialVersionUID = 532349543479150419L;

    MBRecord() {
    }

    Record getObject() {
        return new MBRecord();
    }

    public MBRecord(Name name, int dclass, long ttl, Name mailbox) {
        super(name, 7, dclass, ttl, mailbox, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
