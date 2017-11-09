package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;
import org.xbill.DNS.Tokenizer.Token;

final class TypeBitmap implements Serializable {
    private static final long serialVersionUID = -125354057735389003L;
    private TreeSet types;

    private TypeBitmap() {
        this.types = new TreeSet();
    }

    public TypeBitmap(int[] array) {
        this();
        for (int i = 0; i < array.length; i++) {
            Type.check(array[i]);
            this.types.add(new Integer(array[i]));
        }
    }

    public TypeBitmap(DNSInput in) throws WireParseException {
        this();
        while (in.remaining() > 0) {
            if (in.remaining() < 2) {
                throw new WireParseException("invalid bitmap descriptor");
            }
            int mapbase = in.readU8();
            if (mapbase < -1) {
                throw new WireParseException("invalid ordering");
            }
            int maplength = in.readU8();
            if (maplength > in.remaining()) {
                throw new WireParseException("invalid bitmap");
            }
            for (int i = 0; i < maplength; i++) {
                int current = in.readU8();
                if (current != 0) {
                    for (int j = 0; j < 8; j++) {
                        if (((1 << (7 - j)) & current) != 0) {
                            this.types.add(Mnemonic.toInteger(((mapbase * 256) + (i * 8)) + j));
                        }
                    }
                }
            }
        }
    }

    public TypeBitmap(Tokenizer st) throws IOException {
        this();
        while (true) {
            Token t = st.get();
            if (t.isString()) {
                int typecode = Type.value(t.value);
                if (typecode < 0) {
                    throw st.exception(new StringBuffer().append("Invalid type: ").append(t.value).toString());
                }
                this.types.add(Mnemonic.toInteger(typecode));
            } else {
                st.unget();
                return;
            }
        }
    }

    public int[] toArray() {
        int[] array = new int[this.types.size()];
        int n = 0;
        Iterator it = this.types.iterator();
        while (it.hasNext()) {
            int n2 = n + 1;
            array[n] = ((Integer) it.next()).intValue();
            n = n2;
        }
        return array;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = this.types.iterator();
        while (it.hasNext()) {
            sb.append(Type.string(((Integer) it.next()).intValue()));
            if (it.hasNext()) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static void mapToWire(DNSOutput out, TreeSet map, int mapbase) {
        int arraylength = ((((Integer) map.last()).intValue() & 255) / 8) + 1;
        int[] array = new int[arraylength];
        out.writeU8(mapbase);
        out.writeU8(arraylength);
        Iterator it = map.iterator();
        while (it.hasNext()) {
            int typecode = ((Integer) it.next()).intValue();
            int i = (typecode & 255) / 8;
            array[i] = array[i] | (1 << (7 - (typecode % 8)));
        }
        for (int j = 0; j < arraylength; j++) {
            out.writeU8(array[j]);
        }
    }

    public void toWire(DNSOutput out) {
        if (this.types.size() != 0) {
            int mapbase = -1;
            TreeSet map = new TreeSet();
            Iterator it = this.types.iterator();
            while (it.hasNext()) {
                int t = ((Integer) it.next()).intValue();
                int base = t >> 8;
                if (base != mapbase) {
                    if (map.size() > 0) {
                        mapToWire(out, map, mapbase);
                        map.clear();
                    }
                    mapbase = base;
                }
                map.add(new Integer(t));
            }
            mapToWire(out, map, mapbase);
        }
    }

    public boolean empty() {
        return this.types.isEmpty();
    }

    public boolean contains(int typecode) {
        return this.types.contains(Mnemonic.toInteger(typecode));
    }
}
