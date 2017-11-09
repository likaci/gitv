package org.xbill.DNS;

import java.io.IOException;
import java.util.Date;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.utils.base64;

abstract class SIGBase extends Record {
    private static final long serialVersionUID = -3738444391533812369L;
    protected int alg;
    protected int covered;
    protected Date expire;
    protected int footprint;
    protected int labels;
    protected long origttl;
    protected byte[] signature;
    protected Name signer;
    protected Date timeSigned;

    protected SIGBase() {
    }

    public SIGBase(Name name, int type, int dclass, long ttl, int covered, int alg, long origttl, Date expire, Date timeSigned, int footprint, Name signer, byte[] signature) {
        super(name, type, dclass, ttl);
        Type.check(covered);
        TTL.check(origttl);
        this.covered = covered;
        this.alg = Record.checkU8("alg", alg);
        this.labels = name.labels() - 1;
        if (name.isWild()) {
            this.labels--;
        }
        this.origttl = origttl;
        this.expire = expire;
        this.timeSigned = timeSigned;
        this.footprint = Record.checkU16("footprint", footprint);
        this.signer = Record.checkName("signer", signer);
        this.signature = signature;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.covered = in.readU16();
        this.alg = in.readU8();
        this.labels = in.readU8();
        this.origttl = in.readU32();
        this.expire = new Date(in.readU32() * 1000);
        this.timeSigned = new Date(in.readU32() * 1000);
        this.footprint = in.readU16();
        this.signer = new Name(in);
        this.signature = in.readByteArray();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String typeString = st.getString();
        this.covered = Type.value(typeString);
        if (this.covered < 0) {
            throw st.exception(new StringBuffer().append("Invalid type: ").append(typeString).toString());
        }
        String algString = st.getString();
        this.alg = Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception(new StringBuffer().append("Invalid algorithm: ").append(algString).toString());
        }
        this.labels = st.getUInt8();
        this.origttl = st.getTTL();
        this.expire = FormattedTime.parse(st.getString());
        this.timeSigned = FormattedTime.parse(st.getString());
        this.footprint = st.getUInt16();
        this.signer = st.getName(origin);
        this.signature = st.getBase64();
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Type.string(this.covered));
        sb.append(" ");
        sb.append(this.alg);
        sb.append(" ");
        sb.append(this.labels);
        sb.append(" ");
        sb.append(this.origttl);
        sb.append(" ");
        if (Options.check("multiline")) {
            sb.append("(\n\t");
        }
        sb.append(FormattedTime.format(this.expire));
        sb.append(" ");
        sb.append(FormattedTime.format(this.timeSigned));
        sb.append(" ");
        sb.append(this.footprint);
        sb.append(" ");
        sb.append(this.signer);
        if (Options.check("multiline")) {
            sb.append("\n");
            sb.append(base64.formatString(this.signature, 64, HTTP.TAB, true));
        } else {
            sb.append(" ");
            sb.append(base64.toString(this.signature));
        }
        return sb.toString();
    }

    public int getTypeCovered() {
        return this.covered;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public int getLabels() {
        return this.labels;
    }

    public long getOrigTTL() {
        return this.origttl;
    }

    public Date getExpire() {
        return this.expire;
    }

    public Date getTimeSigned() {
        return this.timeSigned;
    }

    public int getFootprint() {
        return this.footprint;
    }

    public Name getSigner() {
        return this.signer;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    void setSignature(byte[] signature) {
        this.signature = signature;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.covered);
        out.writeU8(this.alg);
        out.writeU8(this.labels);
        out.writeU32(this.origttl);
        out.writeU32(this.expire.getTime() / 1000);
        out.writeU32(this.timeSigned.getTime() / 1000);
        out.writeU16(this.footprint);
        this.signer.toWire(out, null, canonical);
        out.writeByteArray(this.signature);
    }
}
