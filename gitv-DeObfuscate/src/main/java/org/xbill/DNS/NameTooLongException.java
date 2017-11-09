package org.xbill.DNS;

public class NameTooLongException extends WireParseException {
    public NameTooLongException(String s) {
        super(s);
    }
}
