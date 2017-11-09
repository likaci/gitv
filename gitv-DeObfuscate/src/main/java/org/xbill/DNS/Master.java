package org.xbill.DNS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer.TokenizerException;

public class Master {
    private int currentDClass;
    private long currentTTL;
    private int currentType;
    private long defaultTTL;
    private File file;
    private Generator generator;
    private List generators;
    private Master included;
    private Record last;
    private boolean needSOATTL;
    private boolean noExpandGenerate;
    private Name origin;
    private Tokenizer st;

    Master(File file, Name origin, long initialTTL) throws IOException {
        this.last = null;
        this.included = null;
        if (origin == null || origin.isAbsolute()) {
            this.file = file;
            this.st = new Tokenizer(file);
            this.origin = origin;
            this.defaultTTL = initialTTL;
            return;
        }
        throw new RelativeNameException(origin);
    }

    public Master(String filename, Name origin, long ttl) throws IOException {
        this(new File(filename), origin, ttl);
    }

    public Master(String filename, Name origin) throws IOException {
        this(new File(filename), origin, -1);
    }

    public Master(String filename) throws IOException {
        this(new File(filename), null, -1);
    }

    public Master(InputStream in, Name origin, long ttl) {
        this.last = null;
        this.included = null;
        if (origin == null || origin.isAbsolute()) {
            this.st = new Tokenizer(in);
            this.origin = origin;
            this.defaultTTL = ttl;
            return;
        }
        throw new RelativeNameException(origin);
    }

    public Master(InputStream in, Name origin) {
        this(in, origin, -1);
    }

    public Master(InputStream in) {
        this(in, null, -1);
    }

    private Name parseName(String s, Name origin) throws TextParseException {
        try {
            return Name.fromString(s, origin);
        } catch (TextParseException e) {
            throw this.st.exception(e.getMessage());
        }
    }

    private void parseTTLClassAndType() throws IOException {
        boolean seen_class = false;
        String s = this.st.getString();
        int value = DClass.value(s);
        this.currentDClass = value;
        if (value >= 0) {
            s = this.st.getString();
            seen_class = true;
        }
        this.currentTTL = -1;
        try {
            this.currentTTL = TTL.parseTTL(s);
            s = this.st.getString();
        } catch (NumberFormatException e) {
            if (this.defaultTTL >= 0) {
                this.currentTTL = this.defaultTTL;
            } else if (this.last != null) {
                this.currentTTL = this.last.getTTL();
            }
        }
        if (!seen_class) {
            value = DClass.value(s);
            this.currentDClass = value;
            if (value >= 0) {
                s = this.st.getString();
            } else {
                this.currentDClass = 1;
            }
        }
        value = Type.value(s);
        this.currentType = value;
        if (value < 0) {
            throw this.st.exception(new StringBuffer().append("Invalid type '").append(s).append("'").toString());
        } else if (this.currentTTL >= 0) {
        } else {
            if (this.currentType != 6) {
                throw this.st.exception("missing TTL");
            }
            this.needSOATTL = true;
            this.currentTTL = 0;
        }
    }

    private long parseUInt32(String s) {
        if (!Character.isDigit(s.charAt(0))) {
            return -1;
        }
        try {
            long l = Long.parseLong(s);
            if (l < 0 || l > 4294967295L) {
                return -1;
            }
            return l;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void startGenerate() throws IOException {
        String s = this.st.getIdentifier();
        int n = s.indexOf("-");
        if (n < 0) {
            throw this.st.exception(new StringBuffer().append("Invalid $GENERATE range specifier: ").append(s).toString());
        }
        long step;
        String startstr = s.substring(0, n);
        String endstr = s.substring(n + 1);
        String stepstr = null;
        n = endstr.indexOf("/");
        if (n >= 0) {
            stepstr = endstr.substring(n + 1);
            endstr = endstr.substring(0, n);
        }
        long start = parseUInt32(startstr);
        long end = parseUInt32(endstr);
        if (stepstr != null) {
            step = parseUInt32(stepstr);
        } else {
            step = 1;
        }
        if (start < 0 || end < 0 || start > end || step <= 0) {
            throw this.st.exception(new StringBuffer().append("Invalid $GENERATE range specifier: ").append(s).toString());
        }
        String nameSpec = this.st.getIdentifier();
        parseTTLClassAndType();
        if (Generator.supportedType(this.currentType)) {
            String rdataSpec = this.st.getIdentifier();
            this.st.getEOL();
            this.st.unget();
            this.generator = new Generator(start, end, step, nameSpec, this.currentType, this.currentDClass, this.currentTTL, rdataSpec, this.origin);
            if (this.generators == null) {
                this.generators = new ArrayList(1);
            }
            this.generators.add(this.generator);
            return;
        }
        throw this.st.exception(new StringBuffer().append("$GENERATE does not support ").append(Type.string(this.currentType)).append(" records").toString());
    }

    private void endGenerate() throws IOException {
        this.st.getEOL();
        this.generator = null;
    }

    private Record nextGenerated() throws IOException {
        try {
            return this.generator.nextRecord();
        } catch (TokenizerException e) {
            throw this.st.exception(new StringBuffer().append("Parsing $GENERATE: ").append(e.getBaseMessage()).toString());
        } catch (TextParseException e2) {
            throw this.st.exception(new StringBuffer().append("Parsing $GENERATE: ").append(e2.getMessage()).toString());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xbill.DNS.Record _nextRecord() throws java.io.IOException {
        /*
        r22 = this;
        r0 = r22;
        r6 = r0.included;
        if (r6 == 0) goto L_0x0016;
    L_0x0006:
        r0 = r22;
        r6 = r0.included;
        r16 = r6.nextRecord();
        if (r16 == 0) goto L_0x0011;
    L_0x0010:
        return r16;
    L_0x0011:
        r6 = 0;
        r0 = r22;
        r0.included = r6;
    L_0x0016:
        r0 = r22;
        r6 = r0.generator;
        if (r6 == 0) goto L_0x0025;
    L_0x001c:
        r16 = r22.nextGenerated();
        if (r16 != 0) goto L_0x0010;
    L_0x0022:
        r22.endGenerate();
    L_0x0025:
        r0 = r22;
        r6 = r0.st;
        r7 = 1;
        r8 = 0;
        r18 = r6.get(r7, r8);
        r0 = r18;
        r6 = r0.type;
        r7 = 2;
        if (r6 != r7) goto L_0x00b6;
    L_0x0036:
        r0 = r22;
        r6 = r0.st;
        r14 = r6.get();
        r6 = r14.type;
        r7 = 1;
        if (r6 == r7) goto L_0x0025;
    L_0x0043:
        r6 = r14.type;
        if (r6 != 0) goto L_0x004a;
    L_0x0047:
        r16 = 0;
        goto L_0x0010;
    L_0x004a:
        r0 = r22;
        r6 = r0.st;
        r6.unget();
        r0 = r22;
        r6 = r0.last;
        if (r6 != 0) goto L_0x0063;
    L_0x0057:
        r0 = r22;
        r6 = r0.st;
        r7 = "no owner";
        r6 = r6.exception(r7);
        throw r6;
    L_0x0063:
        r0 = r22;
        r6 = r0.last;
        r5 = r6.getName();
    L_0x006b:
        r22.parseTTLClassAndType();
        r0 = r22;
        r6 = r0.currentType;
        r0 = r22;
        r7 = r0.currentDClass;
        r0 = r22;
        r8 = r0.currentTTL;
        r0 = r22;
        r10 = r0.st;
        r0 = r22;
        r11 = r0.origin;
        r6 = org.xbill.DNS.Record.fromString(r5, r6, r7, r8, r10, r11);
        r0 = r22;
        r0.last = r6;
        r0 = r22;
        r6 = r0.needSOATTL;
        if (r6 == 0) goto L_0x00ae;
    L_0x0090:
        r0 = r22;
        r6 = r0.last;
        r6 = (org.xbill.DNS.SOARecord) r6;
        r20 = r6.getMinimum();
        r0 = r22;
        r6 = r0.last;
        r0 = r20;
        r6.setTTL(r0);
        r0 = r20;
        r2 = r22;
        r2.defaultTTL = r0;
        r6 = 0;
        r0 = r22;
        r0.needSOATTL = r6;
    L_0x00ae:
        r0 = r22;
        r0 = r0.last;
        r16 = r0;
        goto L_0x0010;
    L_0x00b6:
        r0 = r18;
        r6 = r0.type;
        r7 = 1;
        if (r6 == r7) goto L_0x0025;
    L_0x00bd:
        r0 = r18;
        r6 = r0.type;
        if (r6 != 0) goto L_0x00c7;
    L_0x00c3:
        r16 = 0;
        goto L_0x0010;
    L_0x00c7:
        r0 = r18;
        r6 = r0.value;
        r7 = 0;
        r6 = r6.charAt(r7);
        r7 = 36;
        if (r6 != r7) goto L_0x01cd;
    L_0x00d4:
        r0 = r18;
        r0 = r0.value;
        r17 = r0;
        r6 = "$ORIGIN";
        r0 = r17;
        r6 = r0.equalsIgnoreCase(r6);
        if (r6 == 0) goto L_0x00fc;
    L_0x00e5:
        r0 = r22;
        r6 = r0.st;
        r7 = org.xbill.DNS.Name.root;
        r6 = r6.getName(r7);
        r0 = r22;
        r0.origin = r6;
        r0 = r22;
        r6 = r0.st;
        r6.getEOL();
        goto L_0x0025;
    L_0x00fc:
        r6 = "$TTL";
        r0 = r17;
        r6 = r0.equalsIgnoreCase(r6);
        if (r6 == 0) goto L_0x011c;
    L_0x0107:
        r0 = r22;
        r6 = r0.st;
        r6 = r6.getTTL();
        r0 = r22;
        r0.defaultTTL = r6;
        r0 = r22;
        r6 = r0.st;
        r6.getEOL();
        goto L_0x0025;
    L_0x011c:
        r6 = "$INCLUDE";
        r0 = r17;
        r6 = r0.equalsIgnoreCase(r6);
        if (r6 == 0) goto L_0x0180;
    L_0x0127:
        r0 = r22;
        r6 = r0.st;
        r4 = r6.getString();
        r0 = r22;
        r6 = r0.file;
        if (r6 == 0) goto L_0x017a;
    L_0x0135:
        r0 = r22;
        r6 = r0.file;
        r15 = r6.getParent();
        r13 = new java.io.File;
        r13.<init>(r15, r4);
    L_0x0142:
        r0 = r22;
        r12 = r0.origin;
        r0 = r22;
        r6 = r0.st;
        r18 = r6.get();
        r6 = r18.isString();
        if (r6 == 0) goto L_0x0167;
    L_0x0154:
        r0 = r18;
        r6 = r0.value;
        r7 = org.xbill.DNS.Name.root;
        r0 = r22;
        r12 = r0.parseName(r6, r7);
        r0 = r22;
        r6 = r0.st;
        r6.getEOL();
    L_0x0167:
        r6 = new org.xbill.DNS.Master;
        r0 = r22;
        r8 = r0.defaultTTL;
        r6.<init>(r13, r12, r8);
        r0 = r22;
        r0.included = r6;
        r16 = r22.nextRecord();
        goto L_0x0010;
    L_0x017a:
        r13 = new java.io.File;
        r13.<init>(r4);
        goto L_0x0142;
    L_0x0180:
        r6 = "$GENERATE";
        r0 = r17;
        r6 = r0.equalsIgnoreCase(r6);
        if (r6 == 0) goto L_0x01ae;
    L_0x018b:
        r0 = r22;
        r6 = r0.generator;
        if (r6 == 0) goto L_0x019a;
    L_0x0191:
        r6 = new java.lang.IllegalStateException;
        r7 = "cannot nest $GENERATE";
        r6.<init>(r7);
        throw r6;
    L_0x019a:
        r22.startGenerate();
        r0 = r22;
        r6 = r0.noExpandGenerate;
        if (r6 == 0) goto L_0x01a8;
    L_0x01a3:
        r22.endGenerate();
        goto L_0x0025;
    L_0x01a8:
        r16 = r22.nextGenerated();
        goto L_0x0010;
    L_0x01ae:
        r0 = r22;
        r6 = r0.st;
        r7 = new java.lang.StringBuffer;
        r7.<init>();
        r8 = "Invalid directive: ";
        r7 = r7.append(r8);
        r0 = r17;
        r7 = r7.append(r0);
        r7 = r7.toString();
        r6 = r6.exception(r7);
        throw r6;
    L_0x01cd:
        r0 = r18;
        r0 = r0.value;
        r17 = r0;
        r0 = r22;
        r6 = r0.origin;
        r0 = r22;
        r1 = r17;
        r5 = r0.parseName(r1, r6);
        r0 = r22;
        r6 = r0.last;
        if (r6 == 0) goto L_0x006b;
    L_0x01e5:
        r0 = r22;
        r6 = r0.last;
        r6 = r6.getName();
        r6 = r5.equals(r6);
        if (r6 == 0) goto L_0x006b;
    L_0x01f3:
        r0 = r22;
        r6 = r0.last;
        r5 = r6.getName();
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Master._nextRecord():org.xbill.DNS.Record");
    }

    public Record nextRecord() throws IOException {
        Record rec = null;
        try {
            rec = _nextRecord();
            return rec;
        } finally {
            if (rec == null) {
                this.st.close();
            }
        }
    }

    public void expandGenerate(boolean wantExpand) {
        this.noExpandGenerate = !wantExpand;
    }

    public Iterator generators() {
        if (this.generators != null) {
            return Collections.unmodifiableList(this.generators).iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    protected void finalize() {
        if (this.st != null) {
            this.st.close();
        }
    }
}
