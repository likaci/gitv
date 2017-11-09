package org.xbill.DNS;

import java.util.Date;

public class SIGRecord extends SIGBase {
    private static final long serialVersionUID = 4963556060953589058L;

    SIGRecord() {
    }

    Record getObject() {
        return new SIGRecord();
    }

    public SIGRecord(Name name, int dclass, long ttl, int covered, int alg, long origttl, Date expire, Date timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, 24, dclass, ttl, covered, alg, origttl, expire, timeSigned, footprint, signer, signature);
    }
}
