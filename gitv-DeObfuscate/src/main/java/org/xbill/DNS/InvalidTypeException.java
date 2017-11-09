package org.xbill.DNS;

public class InvalidTypeException extends IllegalArgumentException {
    public InvalidTypeException(int type) {
        super(new StringBuffer().append("Invalid DNS type: ").append(type).toString());
    }
}
