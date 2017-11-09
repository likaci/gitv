package org.xbill.DNS;

import java.util.Date;

public class RRSIGRecord extends SIGBase {
    private static final long serialVersionUID = -2609150673537226317L;

    RRSIGRecord() {
    }

    Record getObject() {
        return new RRSIGRecord();
    }

    public RRSIGRecord(Name name, int dclass, long ttl, int covered, int alg, long origttl, Date expire, Date timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, 46, dclass, ttl, covered, alg, origttl, expire, timeSigned, footprint, signer, signature);
    }
}
