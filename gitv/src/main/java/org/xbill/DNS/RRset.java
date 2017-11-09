package org.xbill.DNS;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RRset implements Serializable {
    private static final long serialVersionUID = -3270249290171239695L;
    private short nsigs;
    private short position;
    private List rrs;

    public RRset() {
        this.rrs = new ArrayList(1);
        this.nsigs = (short) 0;
        this.position = (short) 0;
    }

    public RRset(Record record) {
        this();
        safeAddRR(record);
    }

    public RRset(RRset rrset) {
        synchronized (rrset) {
            this.rrs = (List) ((ArrayList) rrset.rrs).clone();
            this.nsigs = rrset.nsigs;
            this.position = rrset.position;
        }
    }

    private void safeAddRR(Record r) {
        if (r instanceof RRSIGRecord) {
            this.rrs.add(r);
            this.nsigs = (short) (this.nsigs + 1);
        } else if (this.nsigs == (short) 0) {
            this.rrs.add(r);
        } else {
            this.rrs.add(this.rrs.size() - this.nsigs, r);
        }
    }

    public synchronized void addRR(Record r) {
        if (this.rrs.size() == 0) {
            safeAddRR(r);
        } else {
            Record first = first();
            if (r.sameRRset(first)) {
                if (r.getTTL() != first.getTTL()) {
                    if (r.getTTL() > first.getTTL()) {
                        r = r.cloneRecord();
                        r.setTTL(first.getTTL());
                    } else {
                        for (int i = 0; i < this.rrs.size(); i++) {
                            Record tmp = ((Record) this.rrs.get(i)).cloneRecord();
                            tmp.setTTL(r.getTTL());
                            this.rrs.set(i, tmp);
                        }
                    }
                }
                if (!this.rrs.contains(r)) {
                    safeAddRR(r);
                }
            } else {
                throw new IllegalArgumentException("record does not match rrset");
            }
        }
    }

    public synchronized void deleteRR(Record r) {
        if (this.rrs.remove(r) && (r instanceof RRSIGRecord)) {
            this.nsigs = (short) (this.nsigs - 1);
        }
    }

    public synchronized void clear() {
        this.rrs.clear();
        this.position = (short) 0;
        this.nsigs = (short) 0;
    }

    private synchronized Iterator iterator(boolean data, boolean cycle) {
        Iterator it;
        short size;
        int total = this.rrs.size();
        if (data) {
            size = total - this.nsigs;
        } else {
            size = this.nsigs;
        }
        if (size == (short) 0) {
            it = Collections.EMPTY_LIST.iterator();
        } else {
            int start;
            if (!data) {
                start = total - this.nsigs;
            } else if (cycle) {
                if (this.position >= size) {
                    this.position = (short) 0;
                }
                start = this.position;
                this.position = (short) (start + 1);
            } else {
                start = 0;
            }
            List list = new ArrayList(size);
            if (data) {
                list.addAll(this.rrs.subList(start, size));
                if (start != 0) {
                    list.addAll(this.rrs.subList(0, start));
                }
            } else {
                list.addAll(this.rrs.subList(start, total));
            }
            it = list.iterator();
        }
        return it;
    }

    public synchronized Iterator rrs(boolean cycle) {
        return iterator(true, cycle);
    }

    public synchronized Iterator rrs() {
        return iterator(true, true);
    }

    public synchronized Iterator sigs() {
        return iterator(false, false);
    }

    public synchronized int size() {
        return this.rrs.size() - this.nsigs;
    }

    public Name getName() {
        return first().getName();
    }

    public int getType() {
        return first().getRRsetType();
    }

    public int getDClass() {
        return first().getDClass();
    }

    public synchronized long getTTL() {
        return first().getTTL();
    }

    public synchronized Record first() {
        if (this.rrs.size() == 0) {
            throw new IllegalStateException("rrset is empty");
        }
        return (Record) this.rrs.get(0);
    }

    private String iteratorToString(Iterator it) {
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Record rr = (Record) it.next();
            sb.append("[");
            sb.append(rr.rdataToString());
            sb.append(AlbumEnterFactory.SIGN_STR);
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public String toString() {
        if (this.rrs.size() == 0) {
            return "{empty}";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{ ");
        sb.append(new StringBuffer().append(getName()).append(" ").toString());
        sb.append(new StringBuffer().append(getTTL()).append(" ").toString());
        sb.append(new StringBuffer().append(DClass.string(getDClass())).append(" ").toString());
        sb.append(new StringBuffer().append(Type.string(getType())).append(" ").toString());
        sb.append(iteratorToString(iterator(true, false)));
        if (this.nsigs > (short) 0) {
            sb.append(" sigs: ");
            sb.append(iteratorToString(iterator(false, false)));
        }
        sb.append(" }");
        return sb.toString();
    }
}
