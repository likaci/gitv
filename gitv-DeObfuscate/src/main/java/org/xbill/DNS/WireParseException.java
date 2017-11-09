package org.xbill.DNS;

import java.io.IOException;

public class WireParseException extends IOException {
    public WireParseException(String s) {
        super(s);
    }

    public WireParseException(String s, Throwable cause) {
        super(s);
        initCause(cause);
    }
}
