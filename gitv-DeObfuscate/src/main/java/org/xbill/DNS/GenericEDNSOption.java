package org.xbill.DNS;

import java.io.IOException;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.xbill.DNS.utils.base16;

public class GenericEDNSOption extends EDNSOption {
    private byte[] data;

    GenericEDNSOption(int code) {
        super(code);
    }

    public GenericEDNSOption(int code, byte[] data) {
        super(code);
        this.data = Record.checkByteArrayLength("option data", data, Message.MAXLENGTH);
    }

    void optionFromWire(DNSInput in) throws IOException {
        this.data = in.readByteArray();
    }

    void optionToWire(DNSOutput out) {
        out.writeByteArray(this.data);
    }

    String optionToString() {
        return new StringBuffer().append(SearchCriteria.LT).append(base16.toString(this.data)).append(SearchCriteria.GT).toString();
    }
}
