package org.xbill.DNS;

public class InvalidTTLException extends IllegalArgumentException {
    public InvalidTTLException(long ttl) {
        super(new StringBuffer().append("Invalid DNS TTL: ").append(ttl).toString());
    }
}
