package org.xbill.DNS;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import java.io.IOException;
import java.util.Date;
import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.xbill.DNS.utils.base64;

public class TSIGRecord extends Record {
    private static final long serialVersionUID = -88820909016649306L;
    private Name alg;
    private int error;
    private int fudge;
    private int originalID;
    private byte[] other;
    private byte[] signature;
    private Date timeSigned;

    TSIGRecord() {
    }

    Record getObject() {
        return new TSIGRecord();
    }

    public TSIGRecord(Name name, int dclass, long ttl, Name alg, Date timeSigned, int fudge, byte[] signature, int originalID, int error, byte[] other) {
        super(name, 250, dclass, ttl);
        this.alg = Record.checkName("alg", alg);
        this.timeSigned = timeSigned;
        this.fudge = Record.checkU16("fudge", fudge);
        this.signature = signature;
        this.originalID = Record.checkU16("originalID", originalID);
        this.error = Record.checkU16(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR, error);
        this.other = other;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.alg = new Name(in);
        this.timeSigned = new Date(1000 * ((((long) in.readU16()) << 32) + in.readU32()));
        this.fudge = in.readU16();
        this.signature = in.readByteArray(in.readU16());
        this.originalID = in.readU16();
        this.error = in.readU16();
        int otherLen = in.readU16();
        if (otherLen > 0) {
            this.other = in.readByteArray(otherLen);
        } else {
            this.other = null;
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for TSIG");
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.alg);
        sb.append(" ");
        if (Options.check("multiline")) {
            sb.append("(\n\t");
        }
        sb.append(this.timeSigned.getTime() / 1000);
        sb.append(" ");
        sb.append(this.fudge);
        sb.append(" ");
        sb.append(this.signature.length);
        if (Options.check("multiline")) {
            sb.append("\n");
            sb.append(base64.formatString(this.signature, 64, HTTP.TAB, false));
        } else {
            sb.append(" ");
            sb.append(base64.toString(this.signature));
        }
        sb.append(" ");
        sb.append(Rcode.TSIGstring(this.error));
        sb.append(" ");
        if (this.other == null) {
            sb.append(0);
        } else {
            sb.append(this.other.length);
            if (Options.check("multiline")) {
                sb.append("\n\n\n\t");
            } else {
                sb.append(" ");
            }
            if (this.error != 18) {
                sb.append(SearchCriteria.LT);
                sb.append(base64.toString(this.other));
                sb.append(SearchCriteria.GT);
            } else if (this.other.length != 6) {
                sb.append("<invalid BADTIME other data>");
            } else {
                long time = (((((((long) (this.other[0] & 255)) << 40) + (((long) (this.other[1] & 255)) << 32)) + ((long) ((this.other[2] & 255) << 24))) + ((long) ((this.other[3] & 255) << 16))) + ((long) ((this.other[4] & 255) << 8))) + ((long) (this.other[5] & 255));
                sb.append("<server time: ");
                sb.append(new Date(time * 1000));
                sb.append(SearchCriteria.GT);
            }
        }
        if (Options.check("multiline")) {
            sb.append(" )");
        }
        return sb.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Date getTimeSigned() {
        return this.timeSigned;
    }

    public int getFudge() {
        return this.fudge;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public int getOriginalID() {
        return this.originalID;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getOther() {
        return this.other;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.alg.toWire(out, null, canonical);
        long time = this.timeSigned.getTime() / 1000;
        long timeLow = time & 4294967295L;
        out.writeU16((int) (time >> 32));
        out.writeU32(timeLow);
        out.writeU16(this.fudge);
        out.writeU16(this.signature.length);
        out.writeByteArray(this.signature);
        out.writeU16(this.originalID);
        out.writeU16(this.error);
        if (this.other != null) {
            out.writeU16(this.other.length);
            out.writeByteArray(this.other);
            return;
        }
        out.writeU16(0);
    }
}
