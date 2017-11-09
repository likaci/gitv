package org.xbill.DNS;

abstract class SingleCompressedNameBase extends SingleNameBase {
    private static final long serialVersionUID = -236435396815460677L;

    protected SingleCompressedNameBase() {
    }

    protected SingleCompressedNameBase(Name name, int type, int dclass, long ttl, Name singleName, String description) {
        super(name, type, dclass, ttl, singleName, description);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.singleName.toWire(out, c, canonical);
    }
}
