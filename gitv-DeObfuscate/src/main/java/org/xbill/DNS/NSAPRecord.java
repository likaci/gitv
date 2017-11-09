package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class NSAPRecord extends Record {
    private static final long serialVersionUID = -1037209403185658593L;
    private byte[] address;

    NSAPRecord() {
    }

    Record getObject() {
        return new NSAPRecord();
    }

    private static final byte[] checkAndConvertAddress(String address) {
        if (!address.substring(0, 2).equalsIgnoreCase("0x")) {
            return null;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        boolean partial = false;
        int current = 0;
        for (int i = 2; i < address.length(); i++) {
            char c = address.charAt(i);
            if (c != '.') {
                int value = Character.digit(c, 16);
                if (value == -1) {
                    return null;
                }
                if (partial) {
                    current += value;
                    bytes.write(current);
                    partial = false;
                } else {
                    current = value << 4;
                    partial = true;
                }
            }
        }
        if (partial) {
            return null;
        }
        return bytes.toByteArray();
    }

    public NSAPRecord(Name name, int dclass, long ttl, String address) {
        super(name, 22, dclass, ttl);
        this.address = checkAndConvertAddress(address);
        if (this.address == null) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid NSAP address ").append(address).toString());
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.address = in.readByteArray();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String addr = st.getString();
        this.address = checkAndConvertAddress(addr);
        if (this.address == null) {
            throw st.exception(new StringBuffer().append("invalid NSAP address ").append(addr).toString());
        }
    }

    public String getAddress() {
        return byteArrayToString(this.address, false);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.address);
    }

    String rrToString() {
        return new StringBuffer().append("0x").append(base16.toString(this.address)).toString();
    }
}
