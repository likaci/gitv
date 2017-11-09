package org.xbill.DNS;

public class MRRecord extends SingleNameBase {
    private static final long serialVersionUID = -5617939094209927533L;

    MRRecord() {
    }

    Record getObject() {
        return new MRRecord();
    }

    public MRRecord(Name name, int dclass, long ttl, Name newName) {
        super(name, 9, dclass, ttl, newName, "new name");
    }

    public Name getNewName() {
        return getSingleName();
    }
}
