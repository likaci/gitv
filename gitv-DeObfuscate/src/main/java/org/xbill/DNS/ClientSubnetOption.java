package org.xbill.DNS;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientSubnetOption extends EDNSOption {
    private static final long serialVersionUID = -3868158449890266347L;
    private InetAddress address;
    private int family;
    private int scopeNetmask;
    private int sourceNetmask;

    ClientSubnetOption() {
        super(8);
    }

    private static int checkMaskLength(String field, int family, int val) {
        int max = Address.addressLength(family) * 8;
        if (val >= 0 && val <= max) {
            return val;
        }
        throw new IllegalArgumentException(new StringBuffer().append("\"").append(field).append("\" ").append(val).append(" must be in the range ").append("[0..").append(max).append(AlbumEnterFactory.SIGN_STR).toString());
    }

    public ClientSubnetOption(int sourceNetmask, int scopeNetmask, InetAddress address) {
        super(8);
        this.family = Address.familyOf(address);
        this.sourceNetmask = checkMaskLength("source netmask", this.family, sourceNetmask);
        this.scopeNetmask = checkMaskLength("scope netmask", this.family, scopeNetmask);
        this.address = Address.truncate(address, sourceNetmask);
        if (!address.equals(this.address)) {
            throw new IllegalArgumentException("source netmask is not valid for address");
        }
    }

    public ClientSubnetOption(int sourceNetmask, InetAddress address) {
        this(sourceNetmask, 0, address);
    }

    public int getFamily() {
        return this.family;
    }

    public int getSourceNetmask() {
        return this.sourceNetmask;
    }

    public int getScopeNetmask() {
        return this.scopeNetmask;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    void optionFromWire(DNSInput in) throws WireParseException {
        this.family = in.readU16();
        if (this.family == 1 || this.family == 2) {
            this.sourceNetmask = in.readU8();
            if (this.sourceNetmask > Address.addressLength(this.family) * 8) {
                throw new WireParseException("invalid source netmask");
            }
            this.scopeNetmask = in.readU8();
            if (this.scopeNetmask > Address.addressLength(this.family) * 8) {
                throw new WireParseException("invalid scope netmask");
            }
            byte[] addr = in.readByteArray();
            if (addr.length != (this.sourceNetmask + 7) / 8) {
                throw new WireParseException("invalid address");
            }
            byte[] fulladdr = new byte[Address.addressLength(this.family)];
            System.arraycopy(addr, 0, fulladdr, 0, addr.length);
            try {
                this.address = InetAddress.getByAddress(fulladdr);
                if (!Address.truncate(this.address, this.sourceNetmask).equals(this.address)) {
                    throw new WireParseException("invalid padding");
                }
                return;
            } catch (UnknownHostException e) {
                throw new WireParseException("invalid address", e);
            }
        }
        throw new WireParseException("unknown address family");
    }

    void optionToWire(DNSOutput out) {
        out.writeU16(this.family);
        out.writeU8(this.sourceNetmask);
        out.writeU8(this.scopeNetmask);
        out.writeByteArray(this.address.getAddress(), 0, (this.sourceNetmask + 7) / 8);
    }

    String optionToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.address.getHostAddress());
        sb.append("/");
        sb.append(this.sourceNetmask);
        sb.append(", scope netmask ");
        sb.append(this.scopeNetmask);
        return sb.toString();
    }
}
