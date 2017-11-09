package org.xbill.DNS;

import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class NSRecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = 487170758138268838L;

    NSRecord() {
    }

    Record getObject() {
        return new NSRecord();
    }

    public NSRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 2, dclass, ttl, target, Keys.TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
