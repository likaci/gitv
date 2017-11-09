package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class A6Record extends Record {
    private static final long serialVersionUID = -8815026887337346789L;
    private Name prefix;
    private int prefixBits;
    private InetAddress suffix;

    A6Record() {
    }

    Record getObject() {
        return new A6Record();
    }

    public A6Record(Name name, int dclass, long ttl, int prefixBits, InetAddress suffix, Name prefix) {
        super(name, 38, dclass, ttl);
        this.prefixBits = checkU8("prefixBits", prefixBits);
        if (suffix == null || Address.familyOf(suffix) == 2) {
            this.suffix = suffix;
            if (prefix != null) {
                this.prefix = checkName("prefix", prefix);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("invalid IPv6 address");
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.prefixBits = in.readU8();
        int suffixbytes = ((128 - this.prefixBits) + 7) / 8;
        if (this.prefixBits < 128) {
            byte[] bytes = new byte[16];
            in.readByteArray(bytes, 16 - suffixbytes, suffixbytes);
            this.suffix = InetAddress.getByAddress(bytes);
        }
        if (this.prefixBits > 0) {
            this.prefix = new Name(in);
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.prefixBits = st.getUInt8();
        if (this.prefixBits > 128) {
            throw st.exception("prefix bits must be [0..128]");
        }
        if (this.prefixBits < 128) {
            String s = st.getString();
            try {
                this.suffix = Address.getByAddress(s, 2);
            } catch (UnknownHostException e) {
                throw st.exception(new StringBuffer().append("invalid IPv6 address: ").append(s).toString());
            }
        }
        if (this.prefixBits > 0) {
            this.prefix = st.getName(origin);
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.prefixBits);
        if (this.suffix != null) {
            sb.append(" ");
            sb.append(this.suffix.getHostAddress());
        }
        if (this.prefix != null) {
            sb.append(" ");
            sb.append(this.prefix);
        }
        return sb.toString();
    }

    public int getPrefixBits() {
        return this.prefixBits;
    }

    public InetAddress getSuffix() {
        return this.suffix;
    }

    public Name getPrefix() {
        return this.prefix;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.prefixBits);
        if (this.suffix != null) {
            int suffixbytes = ((128 - this.prefixBits) + 7) / 8;
            out.writeByteArray(this.suffix.getAddress(), 16 - suffixbytes, suffixbytes);
        }
        if (this.prefix != null) {
            this.prefix.toWire(out, null, canonical);
        }
    }
}
