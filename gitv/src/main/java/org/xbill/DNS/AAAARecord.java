package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AAAARecord extends Record {
    private static final long serialVersionUID = -4588601512069748050L;
    private byte[] address;

    AAAARecord() {
    }

    Record getObject() {
        return new AAAARecord();
    }

    public AAAARecord(Name name, int dclass, long ttl, InetAddress address) {
        super(name, 28, dclass, ttl);
        if (Address.familyOf(address) != 2) {
            throw new IllegalArgumentException("invalid IPv6 address");
        }
        this.address = address.getAddress();
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.address = in.readByteArray(16);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.address = st.getAddressBytes(2);
    }

    String rrToString() {
        try {
            InetAddress addr = InetAddress.getByAddress(null, this.address);
            if (addr.getAddress().length != 4) {
                return addr.getHostAddress();
            }
            StringBuffer sb = new StringBuffer("0:0:0:0:0:ffff:");
            int low = ((this.address[14] & 255) << 8) + (this.address[15] & 255);
            sb.append(Integer.toHexString(((this.address[12] & 255) << 8) + (this.address[13] & 255)));
            sb.append(':');
            sb.append(Integer.toHexString(low));
            return sb.toString();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public InetAddress getAddress() {
        try {
            if (this.name == null) {
                return InetAddress.getByAddress(this.address);
            }
            return InetAddress.getByAddress(this.name.toString(), this.address);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeByteArray(this.address);
    }
}
