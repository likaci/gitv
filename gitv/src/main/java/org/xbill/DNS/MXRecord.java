package org.xbill.DNS;

import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class MXRecord extends U16NameBase {
    private static final long serialVersionUID = 2914841027584208546L;

    MXRecord() {
    }

    Record getObject() {
        return new MXRecord();
    }

    public MXRecord(Name name, int dclass, long ttl, int priority, Name target) {
        super(name, 15, dclass, ttl, priority, "priority", target, Keys.TARGET);
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPriority() {
        return getU16Field();
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.u16Field);
        this.nameField.toWire(out, c, canonical);
    }

    public Name getAdditionalName() {
        return getNameField();
    }
}
