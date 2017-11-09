package org.xbill.DNS;

import com.gala.multiscreen.dmr.model.MSMessage.RemoteCode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.Tokenizer.Token;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.utils.base16;

public abstract class Record implements Cloneable, Comparable, Serializable {
    private static final DecimalFormat byteFormat = new DecimalFormat();
    private static final long serialVersionUID = 2694906050116005466L;
    protected int dclass;
    protected Name name;
    protected long ttl;
    protected int type;

    abstract Record getObject();

    abstract void rdataFromString(Tokenizer tokenizer, Name name) throws IOException;

    abstract void rrFromWire(DNSInput dNSInput) throws IOException;

    abstract String rrToString();

    abstract void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z);

    static {
        byteFormat.setMinimumIntegerDigits(3);
    }

    protected Record() {
    }

    Record(Name name, int type, int dclass, long ttl) {
        if (name.isAbsolute()) {
            Type.check(type);
            DClass.check(dclass);
            TTL.check(ttl);
            this.name = name;
            this.type = type;
            this.dclass = dclass;
            this.ttl = ttl;
            return;
        }
        throw new RelativeNameException(name);
    }

    private static final Record getEmptyRecord(Name name, int type, int dclass, long ttl, boolean hasData) {
        Record rec;
        if (hasData) {
            Record proto = Type.getProto(type);
            if (proto != null) {
                rec = proto.getObject();
            } else {
                rec = new UNKRecord();
            }
        } else {
            rec = new EmptyRecord();
        }
        rec.name = name;
        rec.type = type;
        rec.dclass = dclass;
        rec.ttl = ttl;
        return rec;
    }

    private static Record newRecord(Name name, int type, int dclass, long ttl, int length, DNSInput in) throws IOException {
        Record rec = getEmptyRecord(name, type, dclass, ttl, in != null);
        if (in != null) {
            if (in.remaining() < length) {
                throw new WireParseException("truncated record");
            }
            in.setActive(length);
            rec.rrFromWire(in);
            if (in.remaining() > 0) {
                throw new WireParseException("invalid record length");
            }
            in.clearActive();
        }
        return rec;
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl, int length, byte[] data) {
        if (name.isAbsolute()) {
            DNSInput in;
            Type.check(type);
            DClass.check(dclass);
            TTL.check(ttl);
            if (data != null) {
                in = new DNSInput(data);
            } else {
                in = null;
            }
            try {
                return newRecord(name, type, dclass, ttl, length, in);
            } catch (IOException e) {
                return null;
            }
        }
        throw new RelativeNameException(name);
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl, byte[] data) {
        return newRecord(name, type, dclass, ttl, data.length, data);
    }

    public static Record newRecord(Name name, int type, int dclass, long ttl) {
        if (name.isAbsolute()) {
            Type.check(type);
            DClass.check(dclass);
            TTL.check(ttl);
            return getEmptyRecord(name, type, dclass, ttl, false);
        }
        throw new RelativeNameException(name);
    }

    public static Record newRecord(Name name, int type, int dclass) {
        return newRecord(name, type, dclass, 0);
    }

    static Record fromWire(DNSInput in, int section, boolean isUpdate) throws IOException {
        Name name = new Name(in);
        int type = in.readU16();
        int dclass = in.readU16();
        if (section == 0) {
            return newRecord(name, type, dclass);
        }
        long ttl = in.readU32();
        int length = in.readU16();
        if (length == 0 && isUpdate && (section == 1 || section == 2)) {
            return newRecord(name, type, dclass, ttl);
        }
        return newRecord(name, type, dclass, ttl, length, in);
    }

    static Record fromWire(DNSInput in, int section) throws IOException {
        return fromWire(in, section, false);
    }

    public static Record fromWire(byte[] b, int section) throws IOException {
        return fromWire(new DNSInput(b), section, false);
    }

    void toWire(DNSOutput out, int section, Compression c) {
        this.name.toWire(out, c);
        out.writeU16(this.type);
        out.writeU16(this.dclass);
        if (section != 0) {
            out.writeU32(this.ttl);
            int lengthPosition = out.current();
            out.writeU16(0);
            rrToWire(out, c, false);
            out.writeU16At((out.current() - lengthPosition) - 2, lengthPosition);
        }
    }

    public byte[] toWire(int section) {
        DNSOutput out = new DNSOutput();
        toWire(out, section, null);
        return out.toByteArray();
    }

    private void toWireCanonical(DNSOutput out, boolean noTTL) {
        this.name.toWireCanonical(out);
        out.writeU16(this.type);
        out.writeU16(this.dclass);
        if (noTTL) {
            out.writeU32(0);
        } else {
            out.writeU32(this.ttl);
        }
        int lengthPosition = out.current();
        out.writeU16(0);
        rrToWire(out, null, true);
        out.writeU16At((out.current() - lengthPosition) - 2, lengthPosition);
    }

    private byte[] toWireCanonical(boolean noTTL) {
        DNSOutput out = new DNSOutput();
        toWireCanonical(out, noTTL);
        return out.toByteArray();
    }

    public byte[] toWireCanonical() {
        return toWireCanonical(false);
    }

    public byte[] rdataToWireCanonical() {
        DNSOutput out = new DNSOutput();
        rrToWire(out, null, true);
        return out.toByteArray();
    }

    public String rdataToString() {
        return rrToString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        if (sb.length() < 8) {
            sb.append(HTTP.TAB);
        }
        if (sb.length() < 16) {
            sb.append(HTTP.TAB);
        }
        sb.append(HTTP.TAB);
        if (Options.check("BINDTTL")) {
            sb.append(TTL.format(this.ttl));
        } else {
            sb.append(this.ttl);
        }
        sb.append(HTTP.TAB);
        if (!(this.dclass == 1 && Options.check("noPrintIN"))) {
            sb.append(DClass.string(this.dclass));
            sb.append(HTTP.TAB);
        }
        sb.append(Type.string(this.type));
        String rdata = rrToString();
        if (!rdata.equals("")) {
            sb.append(HTTP.TAB);
            sb.append(rdata);
        }
        return sb.toString();
    }

    protected static byte[] byteArrayFromString(String s) throws TextParseException {
        int i;
        byte[] array = s.getBytes();
        boolean escaped = false;
        boolean hasEscapes = false;
        for (byte b : array) {
            if (b == (byte) 92) {
                hasEscapes = true;
                break;
            }
        }
        if (hasEscapes) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int digits = 0;
            int intval = 0;
            for (i = 0; i < array.length; i++) {
                byte b2 = array[i];
                if (escaped) {
                    if (b2 >= (byte) 48 && b2 <= RemoteCode.FLING_LEFT && digits < 3) {
                        digits++;
                        intval = (intval * 10) + (b2 - 48);
                        if (intval > 255) {
                            throw new TextParseException("bad escape");
                        } else if (digits >= 3) {
                            b2 = (byte) intval;
                        }
                    } else if (digits > 0 && digits < 3) {
                        throw new TextParseException("bad escape");
                    }
                    os.write(b2);
                    escaped = false;
                } else if (array[i] == (byte) 92) {
                    escaped = true;
                    digits = 0;
                    intval = 0;
                } else {
                    os.write(array[i]);
                }
            }
            if (digits > 0 && digits < 3) {
                throw new TextParseException("bad escape");
            } else if (os.toByteArray().length <= 255) {
                return os.toByteArray();
            } else {
                throw new TextParseException("text string too long");
            }
        } else if (array.length <= 255) {
            return array;
        } else {
            throw new TextParseException("text string too long");
        }
    }

    protected static String byteArrayToString(byte[] array, boolean quote) {
        StringBuffer sb = new StringBuffer();
        if (quote) {
            sb.append('\"');
        }
        for (byte b : array) {
            int b2 = b & 255;
            if (b2 < 32 || b2 >= Service.LOCUS_CON) {
                sb.append('\\');
                sb.append(byteFormat.format((long) b2));
            } else if (b2 == 34 || b2 == 92) {
                sb.append('\\');
                sb.append((char) b2);
            } else {
                sb.append((char) b2);
            }
        }
        if (quote) {
            sb.append('\"');
        }
        return sb.toString();
    }

    protected static String unknownToString(byte[] data) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\# ");
        sb.append(data.length);
        sb.append(" ");
        sb.append(base16.toString(data));
        return sb.toString();
    }

    public static Record fromString(Name name, int type, int dclass, long ttl, Tokenizer st, Name origin) throws IOException {
        if (name.isAbsolute()) {
            Type.check(type);
            DClass.check(dclass);
            TTL.check(ttl);
            Token t = st.get();
            if (t.type == 3 && t.value.equals("\\#")) {
                int length = st.getUInt16();
                byte[] data = st.getHex();
                if (data == null) {
                    data = new byte[0];
                }
                if (length != data.length) {
                    throw st.exception("invalid unknown RR encoding: length mismatch");
                }
                return newRecord(name, type, dclass, ttl, length, new DNSInput(data));
            }
            st.unget();
            Record rec = getEmptyRecord(name, type, dclass, ttl, true);
            rec.rdataFromString(st, origin);
            t = st.get();
            if (t.type == 1 || t.type == 0) {
                return rec;
            }
            throw st.exception("unexpected tokens at end of record");
        }
        throw new RelativeNameException(name);
    }

    public static Record fromString(Name name, int type, int dclass, long ttl, String s, Name origin) throws IOException {
        return fromString(name, type, dclass, ttl, new Tokenizer(s), origin);
    }

    public Name getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public int getRRsetType() {
        if (this.type == 46) {
            return ((RRSIGRecord) this).getTypeCovered();
        }
        return this.type;
    }

    public int getDClass() {
        return this.dclass;
    }

    public long getTTL() {
        return this.ttl;
    }

    public boolean sameRRset(Record rec) {
        return getRRsetType() == rec.getRRsetType() && this.dclass == rec.dclass && this.name.equals(rec.name);
    }

    public boolean equals(Object arg) {
        if (arg == null || !(arg instanceof Record)) {
            return false;
        }
        Record r = (Record) arg;
        if (this.type == r.type && this.dclass == r.dclass && this.name.equals(r.name)) {
            return Arrays.equals(rdataToWireCanonical(), r.rdataToWireCanonical());
        }
        return false;
    }

    public int hashCode() {
        int code = 0;
        for (byte b : toWireCanonical(true)) {
            code += (code << 3) + (b & 255);
        }
        return code;
    }

    Record cloneRecord() {
        try {
            return (Record) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    public Record withName(Name name) {
        if (name.isAbsolute()) {
            Record rec = cloneRecord();
            rec.name = name;
            return rec;
        }
        throw new RelativeNameException(name);
    }

    Record withDClass(int dclass, long ttl) {
        Record rec = cloneRecord();
        rec.dclass = dclass;
        rec.ttl = ttl;
        return rec;
    }

    void setTTL(long ttl) {
        this.ttl = ttl;
    }

    public int compareTo(Object o) {
        Record arg = (Record) o;
        if (this == arg) {
            return 0;
        }
        int n = this.name.compareTo(arg.name);
        if (n != 0) {
            return n;
        }
        n = this.dclass - arg.dclass;
        if (n != 0) {
            return n;
        }
        n = this.type - arg.type;
        if (n != 0) {
            return n;
        }
        byte[] rdata1 = rdataToWireCanonical();
        byte[] rdata2 = arg.rdataToWireCanonical();
        int i = 0;
        while (i < rdata1.length && i < rdata2.length) {
            n = (rdata1[i] & 255) - (rdata2[i] & 255);
            if (n != 0) {
                return n;
            }
            i++;
        }
        return rdata1.length - rdata2.length;
    }

    public Name getAdditionalName() {
        return null;
    }

    static int checkU8(String field, int val) {
        if (val >= 0 && val <= 255) {
            return val;
        }
        throw new IllegalArgumentException(new StringBuffer().append("\"").append(field).append("\" ").append(val).append(" must be an unsigned 8 ").append("bit value").toString());
    }

    static int checkU16(String field, int val) {
        if (val >= 0 && val <= Message.MAXLENGTH) {
            return val;
        }
        throw new IllegalArgumentException(new StringBuffer().append("\"").append(field).append("\" ").append(val).append(" must be an unsigned 16 ").append("bit value").toString());
    }

    static long checkU32(String field, long val) {
        if (val >= 0 && val <= 4294967295L) {
            return val;
        }
        throw new IllegalArgumentException(new StringBuffer().append("\"").append(field).append("\" ").append(val).append(" must be an unsigned 32 ").append("bit value").toString());
    }

    static Name checkName(String field, Name name) {
        if (name.isAbsolute()) {
            return name;
        }
        throw new RelativeNameException(name);
    }

    static byte[] checkByteArrayLength(String field, byte[] array, int maxLength) {
        if (array.length > Message.MAXLENGTH) {
            throw new IllegalArgumentException(new StringBuffer().append("\"").append(field).append("\" array ").append("must have no more than ").append(maxLength).append(" elements").toString());
        }
        byte[] out = new byte[array.length];
        System.arraycopy(array, 0, out, 0, array.length);
        return out;
    }
}
