package org.xbill.DNS;

import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Message implements Cloneable {
    public static final int MAXLENGTH = 65535;
    static final int TSIG_FAILED = 4;
    static final int TSIG_INTERMEDIATE = 2;
    static final int TSIG_SIGNED = 3;
    static final int TSIG_UNSIGNED = 0;
    static final int TSIG_VERIFIED = 1;
    private static RRset[] emptyRRsetArray = new RRset[0];
    private static Record[] emptyRecordArray = new Record[0];
    private Header header;
    private TSIGRecord querytsig;
    private List[] sections;
    int sig0start;
    private int size;
    int tsigState;
    private int tsigerror;
    private TSIG tsigkey;
    int tsigstart;

    private Message(Header header) {
        this.sections = new List[4];
        this.header = header;
    }

    public Message(int id) {
        this(new Header(id));
    }

    public Message() {
        this(new Header());
    }

    public static Message newQuery(Record r) {
        Message m = new Message();
        m.header.setOpcode(0);
        m.header.setFlag(7);
        m.addRecord(r, 0);
        return m;
    }

    public static Message newUpdate(Name zone) {
        return new Update(zone);
    }

    Message(DNSInput in) throws IOException {
        this(new Header(in));
        boolean isUpdate = this.header.getOpcode() == 5;
        boolean truncated = this.header.getFlag(6);
        int i = 0;
        while (i < 4) {
            try {
                int count = this.header.getCount(i);
                if (count > 0) {
                    this.sections[i] = new ArrayList(count);
                }
                for (int j = 0; j < count; j++) {
                    int pos = in.current();
                    Record rec = Record.fromWire(in, i, isUpdate);
                    this.sections[i].add(rec);
                    if (i == 3) {
                        if (rec.getType() == 250) {
                            this.tsigstart = pos;
                        }
                        if (rec.getType() == 24 && ((SIGRecord) rec).getTypeCovered() == 0) {
                            this.sig0start = pos;
                        }
                    }
                }
                i++;
            } catch (WireParseException e) {
                if (!truncated) {
                    throw e;
                }
            }
        }
        this.size = in.current();
    }

    public Message(byte[] b) throws IOException {
        this(new DNSInput(b));
    }

    public void setHeader(Header h) {
        this.header = h;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addRecord(Record r, int section) {
        if (this.sections[section] == null) {
            this.sections[section] = new LinkedList();
        }
        this.header.incCount(section);
        this.sections[section].add(r);
    }

    public boolean removeRecord(Record r, int section) {
        if (this.sections[section] == null || !this.sections[section].remove(r)) {
            return false;
        }
        this.header.decCount(section);
        return true;
    }

    public void removeAllRecords(int section) {
        this.sections[section] = null;
        this.header.setCount(section, 0);
    }

    public boolean findRecord(Record r, int section) {
        return this.sections[section] != null && this.sections[section].contains(r);
    }

    public boolean findRecord(Record r) {
        int i = 1;
        while (i <= 3) {
            if (this.sections[i] != null && this.sections[i].contains(r)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean findRRset(Name name, int type, int section) {
        if (this.sections[section] == null) {
            return false;
        }
        for (int i = 0; i < this.sections[section].size(); i++) {
            Record r = (Record) this.sections[section].get(i);
            if (r.getType() == type && name.equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean findRRset(Name name, int type) {
        return findRRset(name, type, 1) || findRRset(name, type, 2) || findRRset(name, type, 3);
    }

    public Record getQuestion() {
        List l = this.sections[0];
        if (l == null || l.size() == 0) {
            return null;
        }
        return (Record) l.get(0);
    }

    public TSIGRecord getTSIG() {
        int count = this.header.getCount(3);
        if (count == 0) {
            return null;
        }
        Record rec = (Record) this.sections[3].get(count - 1);
        if (rec.type != 250) {
            return null;
        }
        return (TSIGRecord) rec;
    }

    public boolean isSigned() {
        return this.tsigState == 3 || this.tsigState == 1 || this.tsigState == 4;
    }

    public boolean isVerified() {
        return this.tsigState == 1;
    }

    public OPTRecord getOPT() {
        Record[] additional = getSectionArray(3);
        for (int i = 0; i < additional.length; i++) {
            if (additional[i] instanceof OPTRecord) {
                return (OPTRecord) additional[i];
            }
        }
        return null;
    }

    public int getRcode() {
        int rcode = this.header.getRcode();
        OPTRecord opt = getOPT();
        if (opt != null) {
            return rcode + (opt.getExtendedRcode() << 4);
        }
        return rcode;
    }

    public Record[] getSectionArray(int section) {
        if (this.sections[section] == null) {
            return emptyRecordArray;
        }
        List l = this.sections[section];
        return (Record[]) l.toArray(new Record[l.size()]);
    }

    private static boolean sameSet(Record r1, Record r2) {
        return r1.getRRsetType() == r2.getRRsetType() && r1.getDClass() == r2.getDClass() && r1.getName().equals(r2.getName());
    }

    public RRset[] getSectionRRsets(int section) {
        if (this.sections[section] == null) {
            return emptyRRsetArray;
        }
        List sets = new LinkedList();
        Record[] recs = getSectionArray(section);
        Set hash = new HashSet();
        int i = 0;
        while (i < recs.length) {
            Name name = recs[i].getName();
            boolean newset = true;
            if (hash.contains(name)) {
                for (int j = sets.size() - 1; j >= 0; j--) {
                    RRset set = (RRset) sets.get(j);
                    if (set.getType() == recs[i].getRRsetType() && set.getDClass() == recs[i].getDClass() && set.getName().equals(name)) {
                        set.addRR(recs[i]);
                        newset = false;
                        break;
                    }
                }
            }
            if (newset) {
                sets.add(new RRset(recs[i]));
                hash.add(name);
            }
            i++;
        }
        return (RRset[]) sets.toArray(new RRset[sets.size()]);
    }

    void toWire(DNSOutput out) {
        this.header.toWire(out);
        Compression c = new Compression();
        for (int i = 0; i < 4; i++) {
            if (this.sections[i] != null) {
                for (int j = 0; j < this.sections[i].size(); j++) {
                    ((Record) this.sections[i].get(j)).toWire(out, i, c);
                }
            }
        }
    }

    private int sectionToWire(DNSOutput out, int section, Compression c, int maxLength) {
        int n = this.sections[section].size();
        int pos = out.current();
        int rendered = 0;
        int skipped = 0;
        Record lastrec = null;
        for (int i = 0; i < n; i++) {
            Record rec = (Record) this.sections[section].get(i);
            if (section == 3 && (rec instanceof OPTRecord)) {
                skipped++;
            } else {
                if (!(lastrec == null || sameSet(rec, lastrec))) {
                    pos = out.current();
                    rendered = i;
                }
                lastrec = rec;
                rec.toWire(out, section, c);
                if (out.current() > maxLength) {
                    out.jump(pos);
                    return skipped + (n - rendered);
                }
            }
        }
        return skipped;
    }

    private boolean toWire(DNSOutput out, int maxLength) {
        if (maxLength < 12) {
            return false;
        }
        int tempMaxLength = maxLength;
        if (this.tsigkey != null) {
            tempMaxLength -= this.tsigkey.recordLength();
        }
        OPTRecord opt = getOPT();
        byte[] optBytes = null;
        if (opt != null) {
            optBytes = opt.toWire(3);
            tempMaxLength -= optBytes.length;
        }
        int startpos = out.current();
        this.header.toWire(out);
        Compression c = new Compression();
        int flags = this.header.getFlagsByte();
        int additionalCount = 0;
        int i = 0;
        while (i < 4) {
            if (this.sections[i] != null) {
                int skipped = sectionToWire(out, i, c, tempMaxLength);
                if (skipped != 0 && i != 3) {
                    flags = Header.setFlag(flags, 6, true);
                    out.writeU16At(this.header.getCount(i) - skipped, (startpos + 4) + (i * 2));
                    for (int j = i + 1; j < 3; j++) {
                        out.writeU16At(0, (startpos + 4) + (j * 2));
                    }
                    if (optBytes != null) {
                        out.writeByteArray(optBytes);
                        additionalCount++;
                    }
                    if (flags != this.header.getFlagsByte()) {
                        out.writeU16At(flags, startpos + 2);
                    }
                    if (additionalCount != this.header.getCount(3)) {
                        out.writeU16At(additionalCount, startpos + 10);
                    }
                    if (this.tsigkey != null) {
                        this.tsigkey.generate(this, out.toByteArray(), this.tsigerror, this.querytsig).toWire(out, 3, c);
                        out.writeU16At(additionalCount + 1, startpos + 10);
                    }
                    return true;
                } else if (i == 3) {
                    additionalCount = this.header.getCount(i) - skipped;
                }
            }
            i++;
        }
        if (optBytes != null) {
            out.writeByteArray(optBytes);
            additionalCount++;
        }
        if (flags != this.header.getFlagsByte()) {
            out.writeU16At(flags, startpos + 2);
        }
        if (additionalCount != this.header.getCount(3)) {
            out.writeU16At(additionalCount, startpos + 10);
        }
        if (this.tsigkey != null) {
            this.tsigkey.generate(this, out.toByteArray(), this.tsigerror, this.querytsig).toWire(out, 3, c);
            out.writeU16At(additionalCount + 1, startpos + 10);
        }
        return true;
    }

    public byte[] toWire() {
        DNSOutput out = new DNSOutput();
        toWire(out);
        this.size = out.current();
        return out.toByteArray();
    }

    public byte[] toWire(int maxLength) {
        DNSOutput out = new DNSOutput();
        toWire(out, maxLength);
        this.size = out.current();
        return out.toByteArray();
    }

    public void setTSIG(TSIG key, int error, TSIGRecord querytsig) {
        this.tsigkey = key;
        this.tsigerror = error;
        this.querytsig = querytsig;
    }

    public int numBytes() {
        return this.size;
    }

    public String sectionToString(int i) {
        if (i > 3) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Record[] records = getSectionArray(i);
        for (Record rec : records) {
            if (i == 0) {
                sb.append(new StringBuffer().append(";;\t").append(rec.name).toString());
                sb.append(new StringBuffer().append(", type = ").append(Type.string(rec.type)).toString());
                sb.append(new StringBuffer().append(", class = ").append(DClass.string(rec.dclass)).toString());
            } else {
                sb.append(rec);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getOPT() != null) {
            sb.append(new StringBuffer().append(this.header.toStringWithRcode(getRcode())).append("\n").toString());
        } else {
            sb.append(new StringBuffer().append(this.header).append("\n").toString());
        }
        if (isSigned()) {
            sb.append(";; TSIG ");
            if (isVerified()) {
                sb.append(ScreenSaverPingBack.SEAT_KEY_OK);
            } else {
                sb.append("invalid");
            }
            sb.append('\n');
        }
        for (int i = 0; i < 4; i++) {
            if (this.header.getOpcode() != 5) {
                sb.append(new StringBuffer().append(";; ").append(Section.longString(i)).append(":\n").toString());
            } else {
                sb.append(new StringBuffer().append(";; ").append(Section.updString(i)).append(":\n").toString());
            }
            sb.append(new StringBuffer().append(sectionToString(i)).append("\n").toString());
        }
        sb.append(new StringBuffer().append(";; Message size: ").append(numBytes()).append(" bytes").toString());
        return sb.toString();
    }

    public Object clone() {
        Message m = new Message();
        for (int i = 0; i < this.sections.length; i++) {
            if (this.sections[i] != null) {
                m.sections[i] = new LinkedList(this.sections[i]);
            }
        }
        m.header = (Header) this.header.clone();
        m.size = this.size;
        return m;
    }
}
