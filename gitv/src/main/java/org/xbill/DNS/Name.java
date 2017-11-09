package org.xbill.DNS;

import com.alibaba.fastjson.asm.Opcodes;
import com.gala.multiscreen.dmr.model.MSMessage.RemoteCode;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import org.xbill.DNS.WKSRecord.Service;

public class Name implements Comparable, Serializable {
    private static final int LABEL_COMPRESSION = 192;
    private static final int LABEL_MASK = 192;
    private static final int LABEL_NORMAL = 0;
    private static final int MAXLABEL = 63;
    private static final int MAXLABELS = 128;
    private static final int MAXNAME = 255;
    private static final int MAXOFFSETS = 7;
    private static final DecimalFormat byteFormat = new DecimalFormat();
    public static final Name empty = new Name();
    private static final byte[] emptyLabel = new byte[]{(byte) 0};
    private static final byte[] lowercase = new byte[256];
    public static final Name root = new Name();
    private static final long serialVersionUID = -7257019940971525644L;
    private static final Name wild = new Name();
    private static final byte[] wildLabel = new byte[]{(byte) 1, (byte) 42};
    private int hashcode;
    private byte[] name;
    private long offsets;

    static {
        byteFormat.setMinimumIntegerDigits(3);
        int i = 0;
        while (i < lowercase.length) {
            if (i < 65 || i > 90) {
                lowercase[i] = (byte) i;
            } else {
                lowercase[i] = (byte) ((i - 65) + 97);
            }
            i++;
        }
        root.appendSafe(emptyLabel, 0, 1);
        empty.name = new byte[0];
        wild.appendSafe(wildLabel, 0, 1);
    }

    private Name() {
    }

    private final void setoffset(int n, int offset) {
        if (n < 7) {
            int shift = (7 - n) * 8;
            this.offsets &= (255 << shift) ^ -1;
            this.offsets |= ((long) offset) << shift;
        }
    }

    private final int offset(int n) {
        if (n == 0 && getlabels() == 0) {
            return 0;
        }
        if (n < 0 || n >= getlabels()) {
            throw new IllegalArgumentException("label out of range");
        } else if (n < 7) {
            return ((int) (this.offsets >>> ((7 - n) * 8))) & 255;
        } else {
            int pos = offset(6);
            for (int i = 6; i < n; i++) {
                pos += this.name[pos] + 1;
            }
            return pos;
        }
    }

    private final void setlabels(int labels) {
        this.offsets &= -256;
        this.offsets |= (long) labels;
    }

    private final int getlabels() {
        return (int) (this.offsets & 255);
    }

    private static final void copy(Name src, Name dst) {
        if (src.offset(0) == 0) {
            dst.name = src.name;
            dst.offsets = src.offsets;
            return;
        }
        int offset0 = src.offset(0);
        int namelen = src.name.length - offset0;
        int labels = src.labels();
        dst.name = new byte[namelen];
        System.arraycopy(src.name, offset0, dst.name, 0, namelen);
        int i = 0;
        while (i < labels && i < 7) {
            dst.setoffset(i, src.offset(i) - offset0);
            i++;
        }
        dst.setlabels(labels);
    }

    private final void append(byte[] array, int start, int n) throws NameTooLongException {
        int i;
        int length = this.name == null ? 0 : this.name.length - offset(0);
        int alength = 0;
        int pos = start;
        for (i = 0; i < n; i++) {
            int len = array[pos];
            if (len > 63) {
                throw new IllegalStateException("invalid label");
            }
            len++;
            pos += len;
            alength += len;
        }
        int newlength = length + alength;
        if (newlength > 255) {
            throw new NameTooLongException();
        }
        int labels = getlabels();
        int newlabels = labels + n;
        if (newlabels > 128) {
            throw new IllegalStateException("too many labels");
        }
        byte[] newname = new byte[newlength];
        if (length != 0) {
            System.arraycopy(this.name, offset(0), newname, 0, length);
        }
        System.arraycopy(array, start, newname, length, alength);
        this.name = newname;
        pos = length;
        for (i = 0; i < n; i++) {
            setoffset(labels + i, pos);
            pos += newname[pos] + 1;
        }
        setlabels(newlabels);
    }

    private static TextParseException parseException(String str, String message) {
        return new TextParseException(new StringBuffer().append("'").append(str).append("': ").append(message).toString());
    }

    private final void appendFromString(String fullName, byte[] array, int start, int n) throws TextParseException {
        try {
            append(array, start, n);
        } catch (NameTooLongException e) {
            throw parseException(fullName, "Name too long");
        }
    }

    private final void appendSafe(byte[] array, int start, int n) {
        try {
            append(array, start, n);
        } catch (NameTooLongException e) {
        }
    }

    public Name(String s, Name origin) throws TextParseException {
        if (s.equals("")) {
            throw parseException(s, "empty name");
        } else if (s.equals("@")) {
            if (origin == null) {
                copy(empty, this);
            } else {
                copy(origin, this);
            }
        } else if (s.equals(".")) {
            copy(root, this);
        } else {
            int labelstart = -1;
            int pos = 1;
            byte[] label = new byte[64];
            boolean escaped = false;
            int digits = 0;
            int intval = 0;
            boolean absolute = false;
            for (int i = 0; i < s.length(); i++) {
                byte b = (byte) s.charAt(i);
                int pos2;
                if (escaped) {
                    if (b >= (byte) 48 && b <= RemoteCode.FLING_LEFT && digits < 3) {
                        digits++;
                        intval = (intval * 10) + (b - 48);
                        if (intval > 255) {
                            throw parseException(s, "bad escape");
                        } else if (digits < 3) {
                            continue;
                        } else {
                            b = (byte) intval;
                        }
                    } else if (digits > 0 && digits < 3) {
                        throw parseException(s, "bad escape");
                    }
                    if (pos > 63) {
                        throw parseException(s, "label too long");
                    }
                    labelstart = pos;
                    pos2 = pos + 1;
                    label[pos] = b;
                    escaped = false;
                    pos = pos2;
                } else if (b == (byte) 92) {
                    escaped = true;
                    digits = 0;
                    intval = 0;
                } else if (b != (byte) 46) {
                    if (labelstart == -1) {
                        labelstart = i;
                    }
                    if (pos > 63) {
                        throw parseException(s, "label too long");
                    }
                    pos2 = pos + 1;
                    label[pos] = b;
                    pos = pos2;
                } else if (labelstart == -1) {
                    throw parseException(s, "invalid empty label");
                } else {
                    label[0] = (byte) (pos - 1);
                    appendFromString(s, label, 0, 1);
                    labelstart = -1;
                    pos = 1;
                }
            }
            if (digits > 0 && digits < 3) {
                throw parseException(s, "bad escape");
            } else if (escaped) {
                throw parseException(s, "bad escape");
            } else {
                if (labelstart == -1) {
                    appendFromString(s, emptyLabel, 0, 1);
                    absolute = true;
                } else {
                    label[0] = (byte) (pos - 1);
                    appendFromString(s, label, 0, 1);
                }
                if (origin != null && !absolute) {
                    appendFromString(s, origin.name, origin.offset(0), origin.getlabels());
                }
            }
        }
    }

    public Name(String s) throws TextParseException {
        this(s, null);
    }

    public static Name fromString(String s, Name origin) throws TextParseException {
        if (s.equals("@") && origin != null) {
            return origin;
        }
        if (s.equals(".")) {
            return root;
        }
        return new Name(s, origin);
    }

    public static Name fromString(String s) throws TextParseException {
        return fromString(s, null);
    }

    public static Name fromConstantString(String s) {
        try {
            return fromString(s, null);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid name '").append(s).append("'").toString());
        }
    }

    public Name(DNSInput in) throws WireParseException {
        boolean done = false;
        byte[] label = new byte[64];
        boolean savedState = false;
        while (!done) {
            int len = in.readU8();
            switch (len & Opcodes.CHECKCAST) {
                case 0:
                    if (getlabels() < 128) {
                        if (len != 0) {
                            label[0] = (byte) len;
                            in.readByteArray(label, 1, len);
                            append(label, 0, 1);
                            break;
                        }
                        append(emptyLabel, 0, 1);
                        done = true;
                        break;
                    }
                    throw new WireParseException("too many labels");
                case Opcodes.CHECKCAST /*192*/:
                    int pos = in.readU8() + ((len & -193) << 8);
                    if (Options.check("verbosecompression")) {
                        System.err.println(new StringBuffer().append("currently ").append(in.current()).append(", pointer to ").append(pos).toString());
                    }
                    if (pos < in.current() - 2) {
                        if (!savedState) {
                            in.save();
                            savedState = true;
                        }
                        in.jump(pos);
                        if (!Options.check("verbosecompression")) {
                            break;
                        }
                        System.err.println(new StringBuffer().append("current name '").append(this).append("', seeking to ").append(pos).toString());
                        break;
                    }
                    throw new WireParseException("bad compression");
                default:
                    throw new WireParseException("bad label type");
            }
        }
        if (savedState) {
            in.restore();
        }
    }

    public Name(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    public Name(Name src, int n) {
        int slabels = src.labels();
        if (n > slabels) {
            throw new IllegalArgumentException("attempted to remove too many labels");
        }
        this.name = src.name;
        setlabels(slabels - n);
        int i = 0;
        while (i < 7 && i < slabels - n) {
            setoffset(i, src.offset(i + n));
            i++;
        }
    }

    public static Name concatenate(Name prefix, Name suffix) throws NameTooLongException {
        if (prefix.isAbsolute()) {
            return prefix;
        }
        Name newname = new Name();
        copy(prefix, newname);
        newname.append(suffix.name, suffix.offset(0), suffix.getlabels());
        return newname;
    }

    public Name relativize(Name origin) {
        if (origin == null || !subdomain(origin)) {
            return this;
        }
        Name newname = new Name();
        copy(this, newname);
        int length = length() - origin.length();
        newname.setlabels(newname.labels() - origin.labels());
        newname.name = new byte[length];
        System.arraycopy(this.name, offset(0), newname.name, 0, length);
        return newname;
    }

    public Name wild(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("must replace 1 or more labels");
        }
        try {
            Name newname = new Name();
            copy(wild, newname);
            newname.append(this.name, offset(n), getlabels() - n);
            return newname;
        } catch (NameTooLongException e) {
            throw new IllegalStateException("Name.wild: concatenate failed");
        }
    }

    public Name canonicalize() {
        int i;
        boolean canonical = true;
        for (i = 0; i < this.name.length; i++) {
            if (lowercase[this.name[i] & 255] != this.name[i]) {
                canonical = false;
                break;
            }
        }
        if (canonical) {
            return this;
        }
        Name newname = new Name();
        newname.appendSafe(this.name, offset(0), getlabels());
        for (i = 0; i < newname.name.length; i++) {
            newname.name[i] = lowercase[newname.name[i] & 255];
        }
        return newname;
    }

    public Name fromDNAME(DNAMERecord dname) throws NameTooLongException {
        Name dnameowner = dname.getName();
        Name dnametarget = dname.getTarget();
        if (!subdomain(dnameowner)) {
            return null;
        }
        int plabels = labels() - dnameowner.labels();
        int plength = length() - dnameowner.length();
        int pstart = offset(0);
        int dlabels = dnametarget.labels();
        int dlength = dnametarget.length();
        if (plength + dlength > 255) {
            throw new NameTooLongException();
        }
        Name newname = new Name();
        newname.setlabels(plabels + dlabels);
        newname.name = new byte[(plength + dlength)];
        System.arraycopy(this.name, pstart, newname.name, 0, plength);
        System.arraycopy(dnametarget.name, 0, newname.name, plength, dlength);
        int i = 0;
        int pos = 0;
        while (i < 7 && i < plabels + dlabels) {
            newname.setoffset(i, pos);
            pos += newname.name[pos] + 1;
            i++;
        }
        return newname;
    }

    public boolean isWild() {
        boolean z = true;
        if (labels() == 0) {
            return false;
        }
        if (!(this.name[0] == (byte) 1 && this.name[1] == (byte) 42)) {
            z = false;
        }
        return z;
    }

    public boolean isAbsolute() {
        int nlabels = labels();
        if (nlabels != 0 && this.name[offset(nlabels - 1)] == (byte) 0) {
            return true;
        }
        return false;
    }

    public short length() {
        if (getlabels() == 0) {
            return (short) 0;
        }
        return (short) (this.name.length - offset(0));
    }

    public int labels() {
        return getlabels();
    }

    public boolean subdomain(Name domain) {
        int labels = labels();
        int dlabels = domain.labels();
        if (dlabels > labels) {
            return false;
        }
        if (dlabels == labels) {
            return equals(domain);
        }
        return domain.equals(this.name, offset(labels - dlabels));
    }

    private String byteString(byte[] array, int pos) {
        StringBuffer sb = new StringBuffer();
        int pos2 = pos + 1;
        int len = array[pos];
        for (int i = pos2; i < pos2 + len; i++) {
            int b = array[i] & 255;
            if (b <= 32 || b >= Service.LOCUS_CON) {
                sb.append('\\');
                sb.append(byteFormat.format((long) b));
            } else if (b == 34 || b == 40 || b == 41 || b == 46 || b == 59 || b == 92 || b == 64 || b == 36) {
                sb.append('\\');
                sb.append((char) b);
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }

    public String toString(boolean omitFinalDot) {
        int labels = labels();
        if (labels == 0) {
            return "@";
        }
        if (labels == 1 && this.name[offset(0)] == (byte) 0) {
            return ".";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        int pos = offset(0);
        while (i < labels) {
            int len = this.name[pos];
            if (len > 63) {
                throw new IllegalStateException("invalid label");
            } else if (len == 0) {
                if (!omitFinalDot) {
                    sb.append('.');
                }
                return sb.toString();
            } else {
                if (i > 0) {
                    sb.append('.');
                }
                sb.append(byteString(this.name, pos));
                pos += len + 1;
                i++;
            }
        }
        return sb.toString();
    }

    public String toString() {
        return toString(false);
    }

    public byte[] getLabel(int n) {
        int pos = offset(n);
        byte len = (byte) (this.name[pos] + 1);
        byte[] label = new byte[len];
        System.arraycopy(this.name, pos, label, 0, len);
        return label;
    }

    public String getLabelString(int n) {
        return byteString(this.name, offset(n));
    }

    public void toWire(DNSOutput out, Compression c) {
        if (isAbsolute()) {
            int labels = labels();
            for (int i = 0; i < labels - 1; i++) {
                Name tname;
                if (i == 0) {
                    tname = this;
                } else {
                    tname = new Name(this, i);
                }
                int pos = -1;
                if (c != null) {
                    pos = c.get(tname);
                }
                if (pos >= 0) {
                    out.writeU16(pos | 49152);
                    return;
                }
                if (c != null) {
                    c.add(out.current(), tname);
                }
                int off = offset(i);
                out.writeByteArray(this.name, off, this.name[off] + 1);
            }
            out.writeU8(0);
            return;
        }
        throw new IllegalArgumentException("toWire() called on non-absolute name");
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out, null);
        return out.toByteArray();
    }

    public void toWireCanonical(DNSOutput out) {
        out.writeByteArray(toWireCanonical());
    }

    public byte[] toWireCanonical() {
        int labels = labels();
        if (labels == 0) {
            return new byte[0];
        }
        byte[] b = new byte[(this.name.length - offset(0))];
        int dpos = 0;
        int spos = offset(0);
        for (int i = 0; i < labels; i++) {
            int len = this.name[spos];
            if (len > 63) {
                throw new IllegalStateException("invalid label");
            }
            int dpos2 = dpos + 1;
            int spos2 = spos + 1;
            b[dpos] = this.name[spos];
            int j = 0;
            dpos = dpos2;
            spos = spos2;
            while (j < len) {
                dpos2 = dpos + 1;
                spos2 = spos + 1;
                b[dpos] = lowercase[this.name[spos] & 255];
                j++;
                dpos = dpos2;
                spos = spos2;
            }
        }
        return b;
    }

    public void toWire(DNSOutput out, Compression c, boolean canonical) {
        if (canonical) {
            toWireCanonical(out);
        } else {
            toWire(out, c);
        }
    }

    private final boolean equals(byte[] b, int bpos) {
        int i;
        int labels = labels();
        int i2 = 0;
        int pos = offset(0);
        while (i2 < labels) {
            if (this.name[pos] != b[bpos]) {
                i = pos;
                return false;
            }
            i = pos + 1;
            int len = this.name[pos];
            bpos++;
            if (len > 63) {
                throw new IllegalStateException("invalid label");
            }
            int j = 0;
            pos = i;
            int bpos2 = bpos;
            while (j < len) {
                i = pos + 1;
                bpos = bpos2 + 1;
                if (lowercase[this.name[pos] & 255] != lowercase[b[bpos2] & 255]) {
                    return false;
                }
                j++;
                pos = i;
                bpos2 = bpos;
            }
            i2++;
            bpos = bpos2;
        }
        i = pos;
        return true;
    }

    public boolean equals(Object arg) {
        if (arg == this) {
            return true;
        }
        if (arg == null || !(arg instanceof Name)) {
            return false;
        }
        Name d = (Name) arg;
        if (d.hashcode == 0) {
            d.hashCode();
        }
        if (this.hashcode == 0) {
            hashCode();
        }
        if (d.hashcode == this.hashcode && d.labels() == labels()) {
            return equals(d.name, d.offset(0));
        }
        return false;
    }

    public int hashCode() {
        if (this.hashcode != 0) {
            return this.hashcode;
        }
        int code = 0;
        for (int i = offset(0); i < this.name.length; i++) {
            code += (code << 3) + lowercase[this.name[i] & 255];
        }
        this.hashcode = code;
        return this.hashcode;
    }

    public int compareTo(Object o) {
        Name arg = (Name) o;
        if (this == arg) {
            return 0;
        }
        int compares;
        int labels = labels();
        int alabels = arg.labels();
        if (labels > alabels) {
            compares = alabels;
        } else {
            compares = labels;
        }
        for (int i = 1; i <= compares; i++) {
            int start = offset(labels - i);
            int astart = arg.offset(alabels - i);
            int length = this.name[start];
            int alength = arg.name[astart];
            int j = 0;
            while (j < length && j < alength) {
                int n = lowercase[this.name[(j + start) + 1] & 255] - lowercase[arg.name[(j + astart) + 1] & 255];
                if (n != 0) {
                    return n;
                }
                j++;
            }
            if (length != alength) {
                return length - alength;
            }
        }
        return labels - alabels;
    }
}
