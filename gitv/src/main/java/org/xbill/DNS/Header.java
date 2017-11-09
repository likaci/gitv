package org.xbill.DNS;

import java.io.IOException;
import java.util.Random;

public class Header implements Cloneable {
    public static final int LENGTH = 12;
    private static Random random = new Random();
    private int[] counts;
    private int flags;
    private int id;

    private void init() {
        this.counts = new int[4];
        this.flags = 0;
        this.id = -1;
    }

    public Header(int id) {
        init();
        setID(id);
    }

    public Header() {
        init();
    }

    Header(DNSInput in) throws IOException {
        this(in.readU16());
        this.flags = in.readU16();
        for (int i = 0; i < this.counts.length; i++) {
            this.counts[i] = in.readU16();
        }
    }

    public Header(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    void toWire(DNSOutput out) {
        out.writeU16(getID());
        out.writeU16(this.flags);
        for (int writeU16 : this.counts) {
            out.writeU16(writeU16);
        }
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        return out.toByteArray();
    }

    private static boolean validFlag(int bit) {
        return bit >= 0 && bit <= 15 && Flags.isFlag(bit);
    }

    private static void checkFlag(int bit) {
        if (!validFlag(bit)) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid flag bit ").append(bit).toString());
        }
    }

    static int setFlag(int flags, int bit, boolean value) {
        checkFlag(bit);
        if (value) {
            return flags | (1 << (15 - bit));
        }
        return flags & ((1 << (15 - bit)) ^ -1);
    }

    public void setFlag(int bit) {
        checkFlag(bit);
        this.flags = setFlag(this.flags, bit, true);
    }

    public void unsetFlag(int bit) {
        checkFlag(bit);
        this.flags = setFlag(this.flags, bit, false);
    }

    public boolean getFlag(int bit) {
        checkFlag(bit);
        if ((this.flags & (1 << (15 - bit))) != 0) {
            return true;
        }
        return false;
    }

    boolean[] getFlags() {
        boolean[] array = new boolean[16];
        for (int i = 0; i < array.length; i++) {
            if (validFlag(i)) {
                array[i] = getFlag(i);
            }
        }
        return array;
    }

    public int getID() {
        if (this.id >= 0) {
            return this.id;
        }
        int i;
        synchronized (this) {
            if (this.id < 0) {
                this.id = random.nextInt(Message.MAXLENGTH);
            }
            i = this.id;
        }
        return i;
    }

    public void setID(int id) {
        if (id < 0 || id > Message.MAXLENGTH) {
            throw new IllegalArgumentException(new StringBuffer().append("DNS message ID ").append(id).append(" is out of range").toString());
        }
        this.id = id;
    }

    public void setRcode(int value) {
        if (value < 0 || value > 15) {
            throw new IllegalArgumentException(new StringBuffer().append("DNS Rcode ").append(value).append(" is out of range").toString());
        }
        this.flags &= -16;
        this.flags |= value;
    }

    public int getRcode() {
        return this.flags & 15;
    }

    public void setOpcode(int value) {
        if (value < 0 || value > 15) {
            throw new IllegalArgumentException(new StringBuffer().append("DNS Opcode ").append(value).append("is out of range").toString());
        }
        this.flags &= 34815;
        this.flags |= value << 11;
    }

    public int getOpcode() {
        return (this.flags >> 11) & 15;
    }

    void setCount(int field, int value) {
        if (value < 0 || value > Message.MAXLENGTH) {
            throw new IllegalArgumentException(new StringBuffer().append("DNS section count ").append(value).append(" is out of range").toString());
        }
        this.counts[field] = value;
    }

    void incCount(int field) {
        if (this.counts[field] == 65535) {
            throw new IllegalStateException("DNS section count cannot be incremented");
        }
        int[] iArr = this.counts;
        iArr[field] = iArr[field] + 1;
    }

    void decCount(int field) {
        if (this.counts[field] == 0) {
            throw new IllegalStateException("DNS section count cannot be decremented");
        }
        int[] iArr = this.counts;
        iArr[field] = iArr[field] - 1;
    }

    public int getCount(int field) {
        return this.counts[field];
    }

    int getFlagsByte() {
        return this.flags;
    }

    public String printFlags() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < 16) {
            if (validFlag(i) && getFlag(i)) {
                sb.append(Flags.string(i));
                sb.append(" ");
            }
            i++;
        }
        return sb.toString();
    }

    String toStringWithRcode(int newrcode) {
        StringBuffer sb = new StringBuffer();
        sb.append(";; ->>HEADER<<- ");
        sb.append(new StringBuffer().append("opcode: ").append(Opcode.string(getOpcode())).toString());
        sb.append(new StringBuffer().append(", status: ").append(Rcode.string(newrcode)).toString());
        sb.append(new StringBuffer().append(", id: ").append(getID()).toString());
        sb.append("\n");
        sb.append(new StringBuffer().append(";; flags: ").append(printFlags()).toString());
        sb.append("; ");
        for (int i = 0; i < 4; i++) {
            sb.append(new StringBuffer().append(Section.string(i)).append(": ").append(getCount(i)).append(" ").toString());
        }
        return sb.toString();
    }

    public String toString() {
        return toStringWithRcode(getRcode());
    }

    public Object clone() {
        Header h = new Header();
        h.id = this.id;
        h.flags = this.flags;
        System.arraycopy(this.counts, 0, h.counts, 0, this.counts.length);
        return h;
    }
}
