package org.xbill.DNS;

import java.io.IOException;

public class UNKRecord extends Record {
    private static final long serialVersionUID = -4193583311594626915L;
    private byte[] data;

    UNKRecord() {
    }

    Record getObject() {
        return new UNKRecord();
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.data = in.readByteArray();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        throw st.exception("invalid unknown RR encoding");
    }

    String rrToString() {
        return Record.unknownToString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.data);
    }
}
