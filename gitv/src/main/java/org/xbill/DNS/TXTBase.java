package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer.Token;

abstract class TXTBase extends Record {
    private static final long serialVersionUID = -4319510507246305931L;
    protected List strings;

    protected TXTBase() {
    }

    protected TXTBase(Name name, int type, int dclass, long ttl) {
        super(name, type, dclass, ttl);
    }

    protected TXTBase(Name name, int type, int dclass, long ttl, List strings) {
        super(name, type, dclass, ttl);
        if (strings == null) {
            throw new IllegalArgumentException("strings must not be null");
        }
        this.strings = new ArrayList(strings.size());
        for (String s : strings) {
            try {
                this.strings.add(Record.byteArrayFromString(s));
            } catch (TextParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    protected TXTBase(Name name, int type, int dclass, long ttl, String string) {
        this(name, type, dclass, ttl, Collections.singletonList(string));
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.strings = new ArrayList(2);
        while (in.remaining() > 0) {
            this.strings.add(in.readCountedString());
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.strings = new ArrayList(2);
        while (true) {
            Token t = st.get();
            if (t.isString()) {
                try {
                    this.strings.add(Record.byteArrayFromString(t.value));
                } catch (TextParseException e) {
                    throw st.exception(e.getMessage());
                }
            }
            st.unget();
            return;
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = this.strings.iterator();
        while (it.hasNext()) {
            sb.append(Record.byteArrayToString((byte[]) it.next(), true));
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public List getStrings() {
        List list = new ArrayList(this.strings.size());
        for (int i = 0; i < this.strings.size(); i++) {
            list.add(Record.byteArrayToString((byte[]) this.strings.get(i), false));
        }
        return list;
    }

    public List getStringsAsByteArrays() {
        return this.strings;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        for (byte[] writeCountedString : this.strings) {
            out.writeCountedString(writeCountedString);
        }
    }
}
