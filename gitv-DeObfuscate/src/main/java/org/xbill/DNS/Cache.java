package org.xbill.DNS;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Cache {
    private static final int defaultMaxEntries = 50000;
    private CacheMap data;
    private int dclass;
    private int maxcache;
    private int maxncache;

    private static class CacheMap extends LinkedHashMap {
        private int maxsize = -1;

        CacheMap(int maxsize) {
            super(16, 0.75f, true);
            this.maxsize = maxsize;
        }

        int getMaxSize() {
            return this.maxsize;
        }

        void setMaxSize(int maxsize) {
            this.maxsize = maxsize;
        }

        protected boolean removeEldestEntry(Entry eldest) {
            return this.maxsize >= 0 && size() > this.maxsize;
        }
    }

    private interface Element {
        int compareCredibility(int i);

        boolean expired();

        int getType();
    }

    private static class CacheRRset extends RRset implements Element {
        private static final long serialVersionUID = 5971755205903597024L;
        int credibility;
        int expire;

        public CacheRRset(Record rec, int cred, long maxttl) {
            this.credibility = cred;
            this.expire = Cache.access$000(rec.getTTL(), maxttl);
            addRR(rec);
        }

        public CacheRRset(RRset rrset, int cred, long maxttl) {
            super(rrset);
            this.credibility = cred;
            this.expire = Cache.access$000(rrset.getTTL(), maxttl);
        }

        public final boolean expired() {
            return ((int) (System.currentTimeMillis() / 1000)) >= this.expire;
        }

        public final int compareCredibility(int cred) {
            return this.credibility - cred;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(super.toString());
            sb.append(" cl = ");
            sb.append(this.credibility);
            return sb.toString();
        }
    }

    private static class NegativeElement implements Element {
        int credibility;
        int expire;
        Name name;
        int type;

        public NegativeElement(Name name, int type, SOARecord soa, int cred, long maxttl) {
            this.name = name;
            this.type = type;
            long cttl = 0;
            if (soa != null) {
                cttl = soa.getMinimum();
            }
            this.credibility = cred;
            this.expire = Cache.access$000(cttl, maxttl);
        }

        public int getType() {
            return this.type;
        }

        public final boolean expired() {
            return ((int) (System.currentTimeMillis() / 1000)) >= this.expire;
        }

        public final int compareCredibility(int cred) {
            return this.credibility - cred;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (this.type == 0) {
                sb.append(new StringBuffer().append("NXDOMAIN ").append(this.name).toString());
            } else {
                sb.append(new StringBuffer().append("NXRRSET ").append(this.name).append(" ").append(Type.string(this.type)).toString());
            }
            sb.append(" cl = ");
            sb.append(this.credibility);
            return sb.toString();
        }
    }

    static int access$000(long x0, long x1) {
        return limitExpire(x0, x1);
    }

    private static int limitExpire(long ttl, long maxttl) {
        if (maxttl >= 0 && maxttl < ttl) {
            ttl = maxttl;
        }
        long expire = (System.currentTimeMillis() / 1000) + ttl;
        if (expire < 0 || expire > TTL.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) expire;
    }

    public Cache(int dclass) {
        this.maxncache = -1;
        this.maxcache = -1;
        this.dclass = dclass;
        this.data = new CacheMap(defaultMaxEntries);
    }

    public Cache() {
        this(1);
    }

    public Cache(String file) throws IOException {
        this.maxncache = -1;
        this.maxcache = -1;
        this.data = new CacheMap(defaultMaxEntries);
        Master m = new Master(file);
        while (true) {
            Record record = m.nextRecord();
            if (record != null) {
                addRecord(record, 0, m);
            } else {
                return;
            }
        }
    }

    private synchronized Object exactName(Name name) {
        return this.data.get(name);
    }

    private synchronized void removeName(Name name) {
        this.data.remove(name);
    }

    private synchronized Element[] allElements(Object types) {
        Element[] elementArr;
        if (types instanceof List) {
            List typelist = (List) types;
            elementArr = (Element[]) typelist.toArray(new Element[typelist.size()]);
        } else {
            elementArr = new Element[]{(Element) types};
        }
        return elementArr;
    }

    private synchronized Element oneElement(Name name, Object types, int type, int minCred) {
        Element found;
        found = null;
        if (type == 255) {
            throw new IllegalArgumentException("oneElement(ANY)");
        }
        Element set;
        if (types instanceof List) {
            List list = (List) types;
            for (int i = 0; i < list.size(); i++) {
                set = (Element) list.get(i);
                if (set.getType() == type) {
                    found = set;
                    break;
                }
            }
        } else {
            set = (Element) types;
            if (set.getType() == type) {
                found = set;
            }
        }
        if (found == null) {
            found = null;
        } else if (found.expired()) {
            removeElement(name, type);
            found = null;
        } else if (found.compareCredibility(minCred) < 0) {
            found = null;
        }
        return found;
    }

    private synchronized Element findElement(Name name, int type, int minCred) {
        Element element;
        Object types = exactName(name);
        if (types == null) {
            element = null;
        } else {
            element = oneElement(name, types, type, minCred);
        }
        return element;
    }

    private synchronized void addElement(Name name, Element element) {
        Object types = this.data.get(name);
        if (types == null) {
            this.data.put(name, element);
        } else {
            int type = element.getType();
            if (types instanceof List) {
                List list = (List) types;
                for (int i = 0; i < list.size(); i++) {
                    if (((Element) list.get(i)).getType() == type) {
                        list.set(i, element);
                        break;
                    }
                }
                list.add(element);
            } else {
                Element elt = (Element) types;
                if (elt.getType() == type) {
                    this.data.put(name, element);
                } else {
                    LinkedList list2 = new LinkedList();
                    list2.add(elt);
                    list2.add(element);
                    this.data.put(name, list2);
                }
            }
        }
    }

    private synchronized void removeElement(Name name, int type) {
        Object types = this.data.get(name);
        if (types != null) {
            if (types instanceof List) {
                List list = (List) types;
                int i = 0;
                while (i < list.size()) {
                    if (((Element) list.get(i)).getType() == type) {
                        list.remove(i);
                        if (list.size() == 0) {
                            this.data.remove(name);
                        }
                    } else {
                        i++;
                    }
                }
            } else if (((Element) types).getType() == type) {
                this.data.remove(name);
            }
        }
    }

    public synchronized void clearCache() {
        this.data.clear();
    }

    public synchronized void addRecord(Record r, int cred, Object o) {
        Name name = r.getName();
        int type = r.getRRsetType();
        if (Type.isRR(type)) {
            Element element = findElement(name, type, cred);
            if (element == null) {
                addRRset(new CacheRRset(r, cred, (long) this.maxcache), cred);
            } else if (element.compareCredibility(cred) == 0 && (element instanceof CacheRRset)) {
                ((CacheRRset) element).addRR(r);
            }
        }
    }

    public synchronized void addRRset(RRset rrset, int cred) {
        long ttl = rrset.getTTL();
        Name name = rrset.getName();
        int type = rrset.getType();
        Element element = findElement(name, type, 0);
        if (ttl != 0) {
            if (element != null) {
                if (element.compareCredibility(cred) <= 0) {
                    element = null;
                }
            }
            if (element == null) {
                CacheRRset crrset;
                if (rrset instanceof CacheRRset) {
                    crrset = (CacheRRset) rrset;
                } else {
                    crrset = new CacheRRset(rrset, cred, (long) this.maxcache);
                }
                addElement(name, crrset);
            }
        } else if (element != null && element.compareCredibility(cred) <= 0) {
            removeElement(name, type);
        }
    }

    public synchronized void addNegative(Name name, int type, SOARecord soa, int cred) {
        long ttl = 0;
        if (soa != null) {
            ttl = soa.getTTL();
        }
        Element element = findElement(name, type, 0);
        if (ttl != 0) {
            if (element != null) {
                if (element.compareCredibility(cred) <= 0) {
                    element = null;
                }
            }
            if (element == null) {
                addElement(name, new NegativeElement(name, type, soa, cred, (long) this.maxncache));
            }
        } else if (element != null && element.compareCredibility(cred) <= 0) {
            removeElement(name, type);
        }
    }

    protected synchronized SetResponse lookup(Name name, int type, int minCred) {
        SetResponse setResponse;
        int labels = name.labels();
        int tlabels = labels;
        while (tlabels >= 1) {
            Name tname;
            boolean isRoot = tlabels == 1;
            boolean isExact = tlabels == labels;
            if (isRoot) {
                tname = Name.root;
            } else if (isExact) {
                tname = name;
            } else {
                tname = new Name(name, labels - tlabels);
            }
            Object types = this.data.get(tname);
            if (types != null) {
                Element element;
                if (!isExact || type != 255) {
                    if (!isExact) {
                        element = oneElement(tname, types, 39, minCred);
                        if (element != null && (element instanceof CacheRRset)) {
                            setResponse = new SetResponse(5, (CacheRRset) element);
                            break;
                        }
                    }
                    element = oneElement(tname, types, type, minCred);
                    if (element == null || !(element instanceof CacheRRset)) {
                        if (element == null) {
                            element = oneElement(tname, types, 5, minCred);
                            if (element != null && (element instanceof CacheRRset)) {
                                setResponse = new SetResponse(4, (CacheRRset) element);
                                break;
                            }
                        }
                        setResponse = new SetResponse(2);
                        break;
                    }
                    setResponse = new SetResponse(6);
                    setResponse.addRRset((CacheRRset) element);
                    break;
                }
                setResponse = new SetResponse(6);
                Element[] elements = allElements(types);
                int added = 0;
                for (Element element2 : elements) {
                    if (element2.expired()) {
                        removeElement(tname, element2.getType());
                    } else if ((element2 instanceof CacheRRset) && element2.compareCredibility(minCred) >= 0) {
                        setResponse.addRRset((CacheRRset) element2);
                        added++;
                    }
                }
                if (added > 0) {
                    break;
                }
                element2 = oneElement(tname, types, 2, minCred);
                if (element2 == null || !(element2 instanceof CacheRRset)) {
                    if (isExact && oneElement(tname, types, 0, minCred) != null) {
                        setResponse = SetResponse.ofType(1);
                        break;
                    }
                }
                setResponse = new SetResponse(3, (CacheRRset) element2);
                break;
            }
            tlabels--;
        }
        setResponse = SetResponse.ofType(0);
        return setResponse;
    }

    public SetResponse lookupRecords(Name name, int type, int minCred) {
        return lookup(name, type, minCred);
    }

    private RRset[] findRecords(Name name, int type, int minCred) {
        SetResponse cr = lookupRecords(name, type, minCred);
        if (cr.isSuccessful()) {
            return cr.answers();
        }
        return null;
    }

    public RRset[] findRecords(Name name, int type) {
        return findRecords(name, type, 3);
    }

    public RRset[] findAnyRecords(Name name, int type) {
        return findRecords(name, type, 2);
    }

    private final int getCred(int section, boolean isAuth) {
        if (section == 1) {
            if (isAuth) {
                return 4;
            }
            return 3;
        } else if (section == 2) {
            if (isAuth) {
                return 4;
            }
            return 3;
        } else if (section == 3) {
            return 1;
        } else {
            throw new IllegalArgumentException("getCred: invalid section");
        }
    }

    private static void markAdditional(RRset rrset, Set names) {
        if (rrset.first().getAdditionalName() != null) {
            Iterator it = rrset.rrs();
            while (it.hasNext()) {
                Name name = ((Record) it.next()).getAdditionalName();
                if (name != null) {
                    names.add(name);
                }
            }
        }
    }

    public SetResponse addMessage(Message in) {
        boolean isAuth = in.getHeader().getFlag(5);
        Record question = in.getQuestion();
        int rcode = in.getHeader().getRcode();
        boolean completed = false;
        SetResponse response = null;
        boolean verbose = Options.check("verbosecache");
        if ((rcode != 0 && rcode != 3) || question == null) {
            return null;
        }
        int i;
        Name qname = question.getName();
        int qtype = question.getType();
        int qclass = question.getDClass();
        Name curname = qname;
        HashSet additionalNames = new HashSet();
        RRset[] answers = in.getSectionRRsets(1);
        for (i = 0; i < answers.length; i++) {
            int type;
            int cred;
            SetResponse setResponse;
            if (answers[i].getDClass() == qclass) {
                type = answers[i].getType();
                Name name = answers[i].getName();
                cred = getCred(1, isAuth);
                if ((type == qtype || qtype == 255) && name.equals(curname)) {
                    addRRset(answers[i], cred);
                    completed = true;
                    if (curname == qname) {
                        if (response == null) {
                            setResponse = new SetResponse(6);
                        }
                        response.addRRset(answers[i]);
                    }
                    markAdditional(answers[i], additionalNames);
                } else if (type == 5 && name.equals(curname)) {
                    addRRset(answers[i], cred);
                    if (curname == qname) {
                        setResponse = new SetResponse(4, answers[i]);
                    }
                    curname = ((CNAMERecord) answers[i].first()).getTarget();
                } else if (type == 39 && curname.subdomain(name)) {
                    addRRset(answers[i], cred);
                    if (curname == qname) {
                        setResponse = new SetResponse(5, answers[i]);
                    }
                    try {
                        curname = curname.fromDNAME((DNAMERecord) answers[i].first());
                    } catch (NameTooLongException e) {
                    }
                }
            }
        }
        RRset[] auth = in.getSectionRRsets(2);
        RRset soa = null;
        RRset ns = null;
        i = 0;
        while (i < auth.length) {
            if (auth[i].getType() == 6 && curname.subdomain(auth[i].getName())) {
                soa = auth[i];
            } else if (auth[i].getType() == 2 && curname.subdomain(auth[i].getName())) {
                ns = auth[i];
            }
            i++;
        }
        if (!completed) {
            int cachetype;
            if (rcode == 3) {
                cachetype = 0;
            } else {
                cachetype = qtype;
            }
            if (rcode == 3 || soa != null || ns == null) {
                cred = getCred(2, isAuth);
                SOARecord soarec = null;
                if (soa != null) {
                    soarec = (SOARecord) soa.first();
                }
                addNegative(curname, cachetype, soarec, cred);
                if (response == null) {
                    int responseType;
                    if (rcode == 3) {
                        responseType = 1;
                    } else {
                        responseType = 2;
                    }
                    response = SetResponse.ofType(responseType);
                }
            } else {
                addRRset(ns, getCred(2, isAuth));
                markAdditional(ns, additionalNames);
                if (response == null) {
                    setResponse = new SetResponse(3, ns);
                }
            }
        } else if (rcode == 0 && ns != null) {
            addRRset(ns, getCred(2, isAuth));
            markAdditional(ns, additionalNames);
        }
        RRset[] addl = in.getSectionRRsets(3);
        i = 0;
        while (i < addl.length) {
            type = addl[i].getType();
            if ((type == 1 || type == 28 || type == 38) && additionalNames.contains(addl[i].getName())) {
                addRRset(addl[i], getCred(3, isAuth));
            }
            i++;
        }
        if (verbose) {
            System.out.println(new StringBuffer().append("addMessage: ").append(response).toString());
        }
        return response;
    }

    public void flushSet(Name name, int type) {
        removeElement(name, type);
    }

    public void flushName(Name name) {
        removeName(name);
    }

    public void setMaxNCache(int seconds) {
        this.maxncache = seconds;
    }

    public int getMaxNCache() {
        return this.maxncache;
    }

    public void setMaxCache(int seconds) {
        this.maxcache = seconds;
    }

    public int getMaxCache() {
        return this.maxcache;
    }

    public int getSize() {
        return this.data.size();
    }

    public int getMaxEntries() {
        return this.data.getMaxSize();
    }

    public void setMaxEntries(int entries) {
        this.data.setMaxSize(entries);
    }

    public int getDClass() {
        return this.dclass;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        synchronized (this) {
            for (Object allElements : this.data.values()) {
                Element[] elements = allElements(allElements);
                for (Object allElements2 : elements) {
                    sb.append(allElements2);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
