package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cybergarage.soap.SOAP;
import org.xbill.DNS.Tokenizer.Token;
import org.xbill.DNS.utils.base16;

public class APLRecord extends Record {
    private static final long serialVersionUID = -1348173791712935864L;
    private List elements;

    static class AnonymousClass1 {
    }

    public static class Element {
        public final Object address;
        public final int family;
        public final boolean negative;
        public final int prefixLength;

        Element(int x0, boolean x1, Object x2, int x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private Element(int family, boolean negative, Object address, int prefixLength) {
            this.family = family;
            this.negative = negative;
            this.address = address;
            this.prefixLength = prefixLength;
            if (!APLRecord.access$000(family, prefixLength)) {
                throw new IllegalArgumentException("invalid prefix length");
            }
        }

        public Element(boolean negative, InetAddress address, int prefixLength) {
            this(Address.familyOf(address), negative, address, prefixLength);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (this.negative) {
                sb.append("!");
            }
            sb.append(this.family);
            sb.append(SOAP.DELIM);
            if (this.family == 1 || this.family == 2) {
                sb.append(((InetAddress) this.address).getHostAddress());
            } else {
                sb.append(base16.toString((byte[]) this.address));
            }
            sb.append("/");
            sb.append(this.prefixLength);
            return sb.toString();
        }

        public boolean equals(Object arg) {
            if (arg == null || !(arg instanceof Element)) {
                return false;
            }
            Element elt = (Element) arg;
            if (this.family == elt.family && this.negative == elt.negative && this.prefixLength == elt.prefixLength && this.address.equals(elt.address)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.negative ? 1 : 0) + (this.prefixLength + this.address.hashCode());
        }
    }

    static boolean access$000(int x0, int x1) {
        return validatePrefixLength(x0, x1);
    }

    APLRecord() {
    }

    Record getObject() {
        return new APLRecord();
    }

    private static boolean validatePrefixLength(int family, int prefixLength) {
        if (prefixLength < 0 || prefixLength >= 256) {
            return false;
        }
        if (family == 1 && prefixLength > 32) {
            return false;
        }
        if (family != 2 || prefixLength <= 128) {
            return true;
        }
        return false;
    }

    public APLRecord(Name name, int dclass, long ttl, List elements) {
        super(name, 42, dclass, ttl);
        this.elements = new ArrayList(elements.size());
        for (Element o : elements) {
            if (o instanceof Element) {
                Element element = o;
                if (element.family == 1 || element.family == 2) {
                    this.elements.add(element);
                } else {
                    throw new IllegalArgumentException("unknown family");
                }
            }
            throw new IllegalArgumentException("illegal element");
        }
    }

    private static byte[] parseAddress(byte[] in, int length) throws WireParseException {
        if (in.length > length) {
            throw new WireParseException("invalid address length");
        } else if (in.length == length) {
            return in;
        } else {
            byte[] out = new byte[length];
            System.arraycopy(in, 0, out, 0, in.length);
            return out;
        }
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.elements = new ArrayList(1);
        while (in.remaining() != 0) {
            int family = in.readU16();
            int prefix = in.readU8();
            int length = in.readU8();
            boolean negative = (length & 128) != 0;
            byte[] data = in.readByteArray(length & -129);
            if (validatePrefixLength(family, prefix)) {
                Element element;
                if (family == 1 || family == 2) {
                    element = new Element(negative, InetAddress.getByAddress(parseAddress(data, Address.addressLength(family))), prefix);
                } else {
                    element = new Element(family, negative, data, prefix, null);
                }
                this.elements.add(element);
            } else {
                throw new WireParseException("invalid prefix length");
            }
        }
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.elements = new ArrayList(1);
        while (true) {
            Token t = st.get();
            if (t.isString()) {
                boolean negative = false;
                String s = t.value;
                int start = 0;
                if (s.startsWith("!")) {
                    negative = true;
                    start = 1;
                }
                int colon = s.indexOf(58, start);
                if (colon < 0) {
                    throw st.exception("invalid address prefix element");
                }
                int slash = s.indexOf(47, colon);
                if (slash < 0) {
                    throw st.exception("invalid address prefix element");
                }
                String familyString = s.substring(start, colon);
                String addressString = s.substring(colon + 1, slash);
                String prefixString = s.substring(slash + 1);
                try {
                    int family = Integer.parseInt(familyString);
                    if (family == 1 || family == 2) {
                        try {
                            int prefix = Integer.parseInt(prefixString);
                            if (validatePrefixLength(family, prefix)) {
                                byte[] bytes = Address.toByteArray(addressString, family);
                                if (bytes == null) {
                                    throw st.exception(new StringBuffer().append("invalid IP address ").append(addressString).toString());
                                } else {
                                    this.elements.add(new Element(negative, InetAddress.getByAddress(bytes), prefix));
                                }
                            } else {
                                throw st.exception("invalid prefix length");
                            }
                        } catch (NumberFormatException e) {
                            throw st.exception("invalid prefix length");
                        }
                    }
                    throw st.exception("unknown family");
                } catch (NumberFormatException e2) {
                    throw st.exception("invalid family");
                }
            }
            st.unget();
            return;
        }
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            sb.append((Element) it.next());
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public List getElements() {
        return this.elements;
    }

    private static int addressLength(byte[] addr) {
        for (int i = addr.length - 1; i >= 0; i--) {
            if (addr[i] != (byte) 0) {
                return i + 1;
            }
        }
        return 0;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        for (Element element : this.elements) {
            byte[] data;
            int length;
            if (element.family == 1 || element.family == 2) {
                data = element.address.getAddress();
                length = addressLength(data);
            } else {
                data = (byte[]) element.address;
                length = data.length;
            }
            int wlength = length;
            if (element.negative) {
                wlength |= 128;
            }
            out.writeU16(element.family);
            out.writeU8(element.prefixLength);
            out.writeU8(wlength);
            out.writeByteArray(data, 0, length);
        }
    }
}
