package org.xbill.DNS;

import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class PTRRecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = -8321636610425434192L;

    PTRRecord() {
    }

    Record getObject() {
        return new PTRRecord();
    }

    public PTRRecord(Name name, int dclass, long ttl, Name target) {
        super(name, 12, dclass, ttl, target, Keys.TARGET);
    }

    public Name getTarget() {
        return getSingleName();
    }
}
