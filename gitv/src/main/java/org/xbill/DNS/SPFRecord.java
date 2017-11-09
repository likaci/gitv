package org.xbill.DNS;

import java.util.List;

public class SPFRecord extends TXTBase {
    private static final long serialVersionUID = -2100754352801658722L;

    SPFRecord() {
    }

    Record getObject() {
        return new SPFRecord();
    }

    public SPFRecord(Name name, int dclass, long ttl, List strings) {
        super(name, 99, dclass, ttl, strings);
    }

    public SPFRecord(Name name, int dclass, long ttl, String string) {
        super(name, 99, dclass, ttl, string);
    }
}
