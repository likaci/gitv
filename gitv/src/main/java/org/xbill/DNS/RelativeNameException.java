package org.xbill.DNS;

public class RelativeNameException extends IllegalArgumentException {
    public RelativeNameException(Name name) {
        super(new StringBuffer().append("'").append(name).append("' is not an absolute name").toString());
    }

    public RelativeNameException(String s) {
        super(s);
    }
}
