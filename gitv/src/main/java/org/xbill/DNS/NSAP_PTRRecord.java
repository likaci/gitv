package org.xbill.DNS;

import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class NSAP_PTRRecord extends SingleNameBase {
    private static final long serialVersionUID = 2386284746382064904L;

    NSAP_PTRRecord() {
    }

    Record getObject() {
        return new NSAP_PTRRecord();
    }

    public NSAP_PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 23, dclass, ttl, target, Keys.TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }
}
