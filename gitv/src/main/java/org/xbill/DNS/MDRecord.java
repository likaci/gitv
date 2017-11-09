package org.xbill.DNS;

public class MDRecord extends SingleNameBase {
    private static final long serialVersionUID = 5268878603762942202L;

    MDRecord() {
    }

    Record getObject() {
        return new MDRecord();
    }

    public MDRecord(Name name, int dclass, long ttl, Name mailAgent) {
        super(name, 3, dclass, ttl, mailAgent, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
