package org.xbill.DNS;

public class DNAMERecord extends SingleNameBase {
    private static final long serialVersionUID = 2670767677200844154L;

    DNAMERecord() {
    }

    Record getObject() {
        return new DNAMERecord();
    }

    public DNAMERecord(Name name, int dclass, long ttl, Name alias) {
        super(name, 39, dclass, ttl, alias, "alias");
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAlias() {
        return getSingleName();
    }
}
