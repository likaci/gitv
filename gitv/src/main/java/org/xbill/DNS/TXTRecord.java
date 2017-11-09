package org.xbill.DNS;

import java.util.List;

public class TXTRecord extends TXTBase {
    private static final long serialVersionUID = -5780785764284221342L;

    TXTRecord() {
    }

    Record getObject() {
        return new TXTRecord();
    }

    public TXTRecord(Name name, int dclass, long ttl, List strings) {
        super(name, 16, dclass, ttl, strings);
    }

    public TXTRecord(Name name, int dclass, long ttl, String string) {
        super(name, 16, dclass, ttl, string);
    }
}
