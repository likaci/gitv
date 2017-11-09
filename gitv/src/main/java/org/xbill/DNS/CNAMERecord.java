package org.xbill.DNS;

public class CNAMERecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = -4020373886892538580L;

    CNAMERecord() {
    }

    Record getObject() {
        return new CNAMERecord();
    }

    public CNAMERecord(Name name, int dclass, long ttl, Name alias) {
        super(name, 5, dclass, ttl, alias, "alias");
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAlias() {
        return getSingleName();
    }
}
