package org.xbill.DNS;

import com.xcrash.crashreporter.utils.CrashConst;
import java.io.IOException;

public class SOARecord extends Record {
    private static final long serialVersionUID = 1049740098229303931L;
    private Name admin;
    private long expire;
    private Name host;
    private long minimum;
    private long refresh;
    private long retry;
    private long serial;

    SOARecord() {
    }

    Record getObject() {
        return new SOARecord();
    }

    public SOARecord(Name name, int dclass, long ttl, Name host, Name admin, long serial, long refresh, long retry, long expire, long minimum) {
        super(name, 6, dclass, ttl);
        this.host = Record.checkName(CrashConst.KEY_HOST, host);
        this.admin = Record.checkName("admin", admin);
        this.serial = Record.checkU32("serial", serial);
        this.refresh = Record.checkU32("refresh", refresh);
        this.retry = Record.checkU32("retry", retry);
        this.expire = Record.checkU32("expire", expire);
        this.minimum = Record.checkU32("minimum", minimum);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.host = new Name(in);
        this.admin = new Name(in);
        this.serial = in.readU32();
        this.refresh = in.readU32();
        this.retry = in.readU32();
        this.expire = in.readU32();
        this.minimum = in.readU32();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.host = st.getName(origin);
        this.admin = st.getName(origin);
        this.serial = st.getUInt32();
        this.refresh = st.getTTLLike();
        this.retry = st.getTTLLike();
        this.expire = st.getTTLLike();
        this.minimum = st.getTTLLike();
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.host);
        sb.append(" ");
        sb.append(this.admin);
        if (Options.check("multiline")) {
            sb.append(" (\n\t\t\t\t\t");
            sb.append(this.serial);
            sb.append("\t; serial\n\t\t\t\t\t");
            sb.append(this.refresh);
            sb.append("\t; refresh\n\t\t\t\t\t");
            sb.append(this.retry);
            sb.append("\t; retry\n\t\t\t\t\t");
            sb.append(this.expire);
            sb.append("\t; expire\n\t\t\t\t\t");
            sb.append(this.minimum);
            sb.append(" )\t; minimum");
        } else {
            sb.append(" ");
            sb.append(this.serial);
            sb.append(" ");
            sb.append(this.refresh);
            sb.append(" ");
            sb.append(this.retry);
            sb.append(" ");
            sb.append(this.expire);
            sb.append(" ");
            sb.append(this.minimum);
        }
        return sb.toString();
    }

    public Name getHost() {
        return this.host;
    }

    public Name getAdmin() {
        return this.admin;
    }

    public long getSerial() {
        return this.serial;
    }

    public long getRefresh() {
        return this.refresh;
    }

    public long getRetry() {
        return this.retry;
    }

    public long getExpire() {
        return this.expire;
    }

    public long getMinimum() {
        return this.minimum;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        this.host.toWire(out, c, canonical);
        this.admin.toWire(out, c, canonical);
        out.writeU32(this.serial);
        out.writeU32(this.refresh);
        out.writeU32(this.retry);
        out.writeU32(this.expire);
        out.writeU32(this.minimum);
    }
}
