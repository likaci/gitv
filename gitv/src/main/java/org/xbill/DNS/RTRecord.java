package org.xbill.DNS;

public class RTRecord extends U16NameBase {
    private static final long serialVersionUID = -3206215651648278098L;

    RTRecord() {
    }

    Record getObject() {
        return new RTRecord();
    }

    public RTRecord(Name name, int dclass, long ttl, int preference, Name intermediateHost) {
        super(name, 21, dclass, ttl, preference, "preference", intermediateHost, "intermediateHost");
    }

    public int getPreference() {
        return getU16Field();
    }

    public Name getIntermediateHost() {
        return getNameField();
    }
}
