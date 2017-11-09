package org.cybergarage.xml;

public class ParserException extends Exception {
    public ParserException(Exception e) {
        super(e);
    }

    public ParserException(String s) {
        super(s);
    }
}
