package org.xbill.DNS;

import java.io.IOException;

public class X25Record extends Record {
    private static final long serialVersionUID = 4267576252335579764L;
    private byte[] address;

    X25Record() {
    }

    Record getObject() {
        return new X25Record();
    }

    private static final byte[] checkAndConvertAddress(String address) {
        int length = address.length();
        byte[] out = new byte[length];
        for (int i = 0; i < length; i++) {
            char c = address.charAt(i);
            if (!Character.isDigit(c)) {
                return null;
            }
            out[i] = (byte) c;
        }
        return out;
    }

    public X25Record(Name name, int dclass, long ttl, String address) {
        super(name, 19, dclass, ttl);
        this.address = checkAndConvertAddress(address);
        if (this.address == null) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid PSDN address ").append(address).toString());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.address = in.readCountedString();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String addr = st.getString();
        this.address = checkAndConvertAddress(addr);
        if (this.address == null) {
            throw st.exception(new StringBuffer().append("invalid PSDN address ").append(addr).toString());
        }
    }

    public String getAddress() {
        return Record.byteArrayToString(this.address, false);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeCountedString(this.address);
    }

    String rrToString() {
        return Record.byteArrayToString(this.address, true);
    }
}
