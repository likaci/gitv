package org.xbill.DNS;

public class Compression {
    private static final int MAX_POINTER = 16383;
    private static final int TABLE_SIZE = 17;
    private Entry[] table = new Entry[17];
    private boolean verbose = Options.check("verbosecompression");

    static class C22041 {
    }

    private static class Entry {
        Name name;
        Entry next;
        int pos;

        private Entry() {
        }

        Entry(C22041 x0) {
            this();
        }
    }

    public void add(int pos, Name name) {
        if (pos <= MAX_POINTER) {
            int row = (name.hashCode() & Integer.MAX_VALUE) % 17;
            Entry entry = new Entry(null);
            entry.name = name;
            entry.pos = pos;
            entry.next = this.table[row];
            this.table[row] = entry;
            if (this.verbose) {
                System.err.println(new StringBuffer().append("Adding ").append(name).append(" at ").append(pos).toString());
            }
        }
    }

    public int get(Name name) {
        int pos = -1;
        for (Entry entry = this.table[(name.hashCode() & Integer.MAX_VALUE) % 17]; entry != null; entry = entry.next) {
            if (entry.name.equals(name)) {
                pos = entry.pos;
            }
        }
        if (this.verbose) {
            System.err.println(new StringBuffer().append("Looking for ").append(name).append(", found ").append(pos).toString());
        }
        return pos;
    }
}
