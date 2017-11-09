package org.xbill.DNS;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import org.xbill.DNS.utils.base64;

public class IPSECKEYRecord extends Record {
    private static final long serialVersionUID = 3050449702765909687L;
    private int algorithmType;
    private Object gateway;
    private int gatewayType;
    private byte[] key;
    private int precedence;

    public static class Algorithm {
        public static final int DSA = 1;
        public static final int RSA = 2;

        private Algorithm() {
        }
    }

    public static class Gateway {
        public static final int IPv4 = 1;
        public static final int IPv6 = 2;
        public static final int Name = 3;
        public static final int None = 0;

        private Gateway() {
        }
    }

    IPSECKEYRecord() {
    }

    Record getObject() {
        return new IPSECKEYRecord();
    }

    public IPSECKEYRecord(Name name, int dclass, long ttl, int precedence, int gatewayType, int algorithmType, Object gateway, byte[] key) {
        super(name, 45, dclass, ttl);
        this.precedence = checkU8("precedence", precedence);
        this.gatewayType = checkU8("gatewayType", gatewayType);
        this.algorithmType = checkU8("algorithmType", algorithmType);
        switch (gatewayType) {
            case 0:
                this.gateway = null;
                break;
            case 1:
                if (gateway instanceof InetAddress) {
                    this.gateway = gateway;
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be an IPv4 address");
            case 2:
                if (gateway instanceof Inet6Address) {
                    this.gateway = gateway;
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be an IPv6 address");
            case 3:
                if (gateway instanceof Name) {
                    this.gateway = checkName("gateway", (Name) gateway);
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be a DNS name");
            default:
                throw new IllegalArgumentException("\"gatewayType\" must be between 0 and 3");
        }
        this.key = key;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.precedence = in.readU8();
        this.gatewayType = in.readU8();
        this.algorithmType = in.readU8();
        switch (this.gatewayType) {
            case 0:
                this.gateway = null;
                break;
            case 1:
                this.gateway = InetAddress.getByAddress(in.readByteArray(4));
                break;
            case 2:
                this.gateway = InetAddress.getByAddress(in.readByteArray(16));
                break;
            case 3:
                this.gateway = new Name(in);
                break;
            default:
                throw new WireParseException("invalid gateway type");
        }
        if (in.remaining() > 0) {
            this.key = in.readByteArray();
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.precedence = st.getUInt8();
        this.gatewayType = st.getUInt8();
        this.algorithmType = st.getUInt8();
        switch (this.gatewayType) {
            case 0:
                if (st.getString().equals(".")) {
                    this.gateway = null;
                    break;
                }
                throw new TextParseException("invalid gateway format");
            case 1:
                this.gateway = st.getAddress(1);
                break;
            case 2:
                this.gateway = st.getAddress(2);
                break;
            case 3:
                this.gateway = st.getName(origin);
                break;
            default:
                throw new WireParseException("invalid gateway type");
        }
        this.key = st.getBase64(false);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.precedence);
        sb.append(" ");
        sb.append(this.gatewayType);
        sb.append(" ");
        sb.append(this.algorithmType);
        sb.append(" ");
        switch (this.gatewayType) {
            case 0:
                sb.append(".");
                break;
            case 1:
            case 2:
                sb.append(this.gateway.getHostAddress());
                break;
            case 3:
                sb.append(this.gateway);
                break;
        }
        if (this.key != null) {
            sb.append(" ");
            sb.append(base64.toString(this.key));
        }
        return sb.toString();
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public int getGatewayType() {
        return this.gatewayType;
    }

    public int getAlgorithmType() {
        return this.algorithmType;
    }

    public Object getGateway() {
        return this.gateway;
    }

    public byte[] getKey() {
        return this.key;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.precedence);
        out.writeU8(this.gatewayType);
        out.writeU8(this.algorithmType);
        switch (this.gatewayType) {
            case 1:
            case 2:
                out.writeByteArray(this.gateway.getAddress());
                break;
            case 3:
                this.gateway.toWire(out, null, canonical);
                break;
        }
        if (this.key != null) {
            out.writeByteArray(this.key);
        }
    }
}
