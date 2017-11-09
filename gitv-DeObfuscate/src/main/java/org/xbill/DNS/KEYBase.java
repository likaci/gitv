package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.DNSSEC.DNSSECException;
import org.xbill.DNS.utils.base64;

abstract class KEYBase extends Record {
    private static final long serialVersionUID = 3469321722693285454L;
    protected int alg;
    protected int flags;
    protected int footprint = -1;
    protected byte[] key;
    protected int proto;
    protected PublicKey publicKey = null;

    protected KEYBase() {
    }

    public KEYBase(Name name, int type, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, type, dclass, ttl);
        this.flags = checkU16("flags", flags);
        this.proto = checkU8("proto", proto);
        this.alg = checkU8("alg", alg);
        this.key = key;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.flags = in.readU16();
        this.proto = in.readU8();
        this.alg = in.readU8();
        if (in.remaining() > 0) {
            this.key = in.readByteArray();
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.flags);
        sb.append(" ");
        sb.append(this.proto);
        sb.append(" ");
        sb.append(this.alg);
        if (this.key != null) {
            if (Options.check("multiline")) {
                sb.append(" (\n");
                sb.append(base64.formatString(this.key, 64, HTTP.TAB, true));
                sb.append(" ; key_tag = ");
                sb.append(getFootprint());
            } else {
                sb.append(" ");
                sb.append(base64.toString(this.key));
            }
        }
        return sb.toString();
    }

    public int getFlags() {
        return this.flags;
    }

    public int getProtocol() {
        return this.proto;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getFootprint() {
        if (this.footprint >= 0) {
            return this.footprint;
        }
        int foot = 0;
        DNSOutput out = new DNSOutput();
        rrToWire(out, null, false);
        byte[] rdata = out.toByteArray();
        int d2;
        if (this.alg == 1) {
            d2 = rdata[rdata.length - 2] & 255;
            foot = ((rdata[rdata.length - 3] & 255) << 8) + d2;
        } else {
            int i = 0;
            while (i < rdata.length - 1) {
                d2 = rdata[i + 1] & 255;
                foot += ((rdata[i] & 255) << 8) + d2;
                i += 2;
            }
            if (i < rdata.length) {
                foot += (rdata[i] & 255) << 8;
            }
            foot += (foot >> 16) & Message.MAXLENGTH;
        }
        this.footprint = foot & Message.MAXLENGTH;
        return this.footprint;
    }

    public PublicKey getPublicKey() throws DNSSECException {
        if (this.publicKey != null) {
            return this.publicKey;
        }
        this.publicKey = DNSSEC.toPublicKey(this);
        return this.publicKey;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.flags);
        out.writeU8(this.proto);
        out.writeU8(this.alg);
        if (this.key != null) {
            out.writeByteArray(this.key);
        }
    }
}
