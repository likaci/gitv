package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OPTRecord extends Record {
    private static final long serialVersionUID = -6254521894809367938L;
    private List options;

    OPTRecord() {
    }

    Record getObject() {
        return new OPTRecord();
    }

    public OPTRecord(int payloadSize, int xrcode, int version, int flags, List options) {
        super(Name.root, 41, payloadSize, 0);
        checkU16("payloadSize", payloadSize);
        checkU8("xrcode", xrcode);
        checkU8("version", version);
        checkU16("flags", flags);
        this.ttl = ((((long) xrcode) << 24) + (((long) version) << 16)) + ((long) flags);
        if (options != null) {
            this.options = new ArrayList(options);
        }
    }

    public OPTRecord(int payloadSize, int xrcode, int version, int flags) {
        this(payloadSize, xrcode, version, flags, null);
    }

    public OPTRecord(int payloadSize, int xrcode, int version) {
        this(payloadSize, xrcode, version, 0, null);
    }

    void rrFromWire(DNSInput in) throws IOException {
        if (in.remaining() > 0) {
            this.options = new ArrayList();
        }
        while (in.remaining() > 0) {
            this.options.add(EDNSOption.fromWire(in));
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("no text format defined for OPT");
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        if (this.options != null) {
            sb.append(this.options);
            sb.append(" ");
        }
        sb.append(" ; payload ");
        sb.append(getPayloadSize());
        sb.append(", xrcode ");
        sb.append(getExtendedRcode());
        sb.append(", version ");
        sb.append(getVersion());
        sb.append(", flags ");
        sb.append(getFlags());
        return sb.toString();
    }

    public int getPayloadSize() {
        return this.dclass;
    }

    public int getExtendedRcode() {
        return (int) (this.ttl >>> 24);
    }

    public int getVersion() {
        return (int) ((this.ttl >>> 16) & 255);
    }

    public int getFlags() {
        return (int) (this.ttl & 65535);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        if (this.options != null) {
            for (EDNSOption option : this.options) {
                option.toWire(out);
            }
        }
    }

    public List getOptions() {
        if (this.options == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(this.options);
    }

    public List getOptions(int code) {
        if (this.options == null) {
            return Collections.EMPTY_LIST;
        }
        List list = Collections.EMPTY_LIST;
        for (EDNSOption opt : this.options) {
            if (opt.getCode() == code) {
                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList();
                }
                list.add(opt);
            }
        }
        return list;
    }

    public boolean equals(Object arg) {
        return super.equals(arg) && this.ttl == ((OPTRecord) arg).ttl;
    }
}
