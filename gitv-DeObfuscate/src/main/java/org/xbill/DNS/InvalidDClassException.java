package org.xbill.DNS;

public class InvalidDClassException extends IllegalArgumentException {
    public InvalidDClassException(int dclass) {
        super(new StringBuffer().append("Invalid DNS class: ").append(dclass).toString());
    }
}
