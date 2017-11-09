package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Lookup {
    public static final int HOST_NOT_FOUND = 3;
    public static final int SUCCESSFUL = 0;
    public static final int TRY_AGAIN = 2;
    public static final int TYPE_NOT_FOUND = 4;
    public static final int UNRECOVERABLE = 1;
    static Class class$org$xbill$DNS$Lookup;
    private static Map defaultCaches;
    private static int defaultNdots;
    private static Resolver defaultResolver;
    private static Name[] defaultSearchPath;
    private static final Name[] noAliases = new Name[0];
    private List aliases;
    private Record[] answers;
    private boolean badresponse;
    private String badresponse_error;
    private Cache cache;
    private int credibility;
    private int dclass;
    private boolean done;
    private boolean doneCurrent;
    private String error;
    private boolean foundAlias;
    private int iterations;
    private Name name;
    private boolean nametoolong;
    private boolean networkerror;
    private boolean nxdomain;
    private boolean referral;
    private Resolver resolver;
    private int result;
    private Name[] searchPath;
    private boolean temporary_cache;
    private boolean timedout;
    private int type;
    private boolean verbose;

    static {
        refreshDefault();
    }

    public static synchronized void refreshDefault() {
        synchronized (Lookup.class) {
            try {
                defaultResolver = new ExtendedResolver();
                defaultSearchPath = ResolverConfig.getCurrentConfig().searchPath();
                defaultCaches = new HashMap();
                defaultNdots = ResolverConfig.getCurrentConfig().ndots();
            } catch (UnknownHostException e) {
                throw new RuntimeException("Failed to initialize resolver");
            }
        }
    }

    public static synchronized Resolver getDefaultResolver() {
        Resolver resolver;
        synchronized (Lookup.class) {
            resolver = defaultResolver;
        }
        return resolver;
    }

    public static synchronized void setDefaultResolver(Resolver resolver) {
        synchronized (Lookup.class) {
            defaultResolver = resolver;
        }
    }

    public static synchronized Cache getDefaultCache(int dclass) {
        Cache c;
        synchronized (Lookup.class) {
            DClass.check(dclass);
            c = (Cache) defaultCaches.get(Mnemonic.toInteger(dclass));
            if (c == null) {
                c = new Cache(dclass);
                defaultCaches.put(Mnemonic.toInteger(dclass), c);
            }
        }
        return c;
    }

    public static synchronized void setDefaultCache(Cache cache, int dclass) {
        synchronized (Lookup.class) {
            DClass.check(dclass);
            defaultCaches.put(Mnemonic.toInteger(dclass), cache);
        }
    }

    public static synchronized Name[] getDefaultSearchPath() {
        Name[] nameArr;
        synchronized (Lookup.class) {
            nameArr = defaultSearchPath;
        }
        return nameArr;
    }

    public static synchronized void setDefaultSearchPath(Name[] domains) {
        synchronized (Lookup.class) {
            defaultSearchPath = domains;
        }
    }

    public static synchronized void setDefaultSearchPath(String[] domains) throws TextParseException {
        synchronized (Lookup.class) {
            if (domains == null) {
                defaultSearchPath = null;
            } else {
                Name[] newdomains = new Name[domains.length];
                for (int i = 0; i < domains.length; i++) {
                    newdomains[i] = Name.fromString(domains[i], Name.root);
                }
                defaultSearchPath = newdomains;
            }
        }
    }

    public static synchronized void setPacketLogger(PacketLogger logger) {
        synchronized (Lookup.class) {
            Client.setPacketLogger(logger);
        }
    }

    private final void reset() {
        this.iterations = 0;
        this.foundAlias = false;
        this.done = false;
        this.doneCurrent = false;
        this.aliases = null;
        this.answers = null;
        this.result = -1;
        this.error = null;
        this.nxdomain = false;
        this.badresponse = false;
        this.badresponse_error = null;
        this.networkerror = false;
        this.timedout = false;
        this.nametoolong = false;
        this.referral = false;
        if (this.temporary_cache) {
            this.cache.clearCache();
        }
    }

    public Lookup(Name name, int type, int dclass) {
        Type.check(type);
        DClass.check(dclass);
        if (Type.isRR(type) || type == 255) {
            this.name = name;
            this.type = type;
            this.dclass = dclass;
            Class class$;
            if (class$org$xbill$DNS$Lookup == null) {
                class$ = class$("org.xbill.DNS.Lookup");
                class$org$xbill$DNS$Lookup = class$;
            } else {
                class$ = class$org$xbill$DNS$Lookup;
            }
            synchronized (r0) {
                this.resolver = getDefaultResolver();
                this.searchPath = getDefaultSearchPath();
                this.cache = getDefaultCache(dclass);
            }
            this.credibility = 3;
            this.verbose = Options.check("verbose");
            this.result = -1;
            return;
        }
        throw new IllegalArgumentException("Cannot query for meta-types other than ANY");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public Lookup(Name name, int type) {
        this(name, type, 1);
    }

    public Lookup(Name name) {
        this(name, 1, 1);
    }

    public Lookup(String name, int type, int dclass) throws TextParseException {
        this(Name.fromString(name), type, dclass);
    }

    public Lookup(String name, int type) throws TextParseException {
        this(Name.fromString(name), type, 1);
    }

    public Lookup(String name) throws TextParseException {
        this(Name.fromString(name), 1, 1);
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public void setSearchPath(Name[] domains) {
        this.searchPath = domains;
    }

    public void setSearchPath(String[] domains) throws TextParseException {
        if (domains == null) {
            this.searchPath = null;
            return;
        }
        Name[] newdomains = new Name[domains.length];
        for (int i = 0; i < domains.length; i++) {
            newdomains[i] = Name.fromString(domains[i], Name.root);
        }
        this.searchPath = newdomains;
    }

    public void setCache(Cache cache) {
        if (cache == null) {
            this.cache = new Cache(this.dclass);
            this.temporary_cache = true;
            return;
        }
        this.cache = cache;
        this.temporary_cache = false;
    }

    public void setNdots(int ndots) {
        if (ndots < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal ndots value: ").append(ndots).toString());
        }
        defaultNdots = ndots;
    }

    public void setCredibility(int credibility) {
        this.credibility = credibility;
    }

    private void follow(Name name, Name oldname) {
        this.foundAlias = true;
        this.badresponse = false;
        this.networkerror = false;
        this.timedout = false;
        this.nxdomain = false;
        this.referral = false;
        this.iterations++;
        if (this.iterations >= 6 || name.equals(oldname)) {
            this.result = 1;
            this.error = "CNAME loop";
            this.done = true;
            return;
        }
        if (this.aliases == null) {
            this.aliases = new ArrayList();
        }
        this.aliases.add(oldname);
        lookup(name);
    }

    private void processResponse(Name name, SetResponse response) {
        if (response.isSuccessful()) {
            RRset[] rrsets = response.answers();
            List l = new ArrayList();
            for (RRset rrs : rrsets) {
                Iterator it = rrs.rrs();
                while (it.hasNext()) {
                    l.add(it.next());
                }
            }
            this.result = 0;
            this.answers = (Record[]) l.toArray(new Record[l.size()]);
            this.done = true;
        } else if (response.isNXDOMAIN()) {
            this.nxdomain = true;
            this.doneCurrent = true;
            if (this.iterations > 0) {
                this.result = 3;
                this.done = true;
            }
        } else if (response.isNXRRSET()) {
            this.result = 4;
            this.answers = null;
            this.done = true;
        } else if (response.isCNAME()) {
            follow(response.getCNAME().getTarget(), name);
        } else if (response.isDNAME()) {
            try {
                follow(name.fromDNAME(response.getDNAME()), name);
            } catch (NameTooLongException e) {
                this.result = 1;
                this.error = "Invalid DNAME target";
                this.done = true;
            }
        } else if (response.isDelegation()) {
            this.referral = true;
        }
    }

    private void lookup(Name current) {
        SetResponse sr = this.cache.lookupRecords(current, this.type, this.credibility);
        if (this.verbose) {
            System.err.println(new StringBuffer().append("lookup ").append(current).append(" ").append(Type.string(this.type)).toString());
            System.err.println(sr);
        }
        processResponse(current, sr);
        if (!this.done && !this.doneCurrent) {
            Message query = Message.newQuery(Record.newRecord(current, this.type, this.dclass));
            try {
                Message response = this.resolver.send(query);
                int rcode = response.getHeader().getRcode();
                if (rcode != 0 && rcode != 3) {
                    this.badresponse = true;
                    this.badresponse_error = Rcode.string(rcode);
                } else if (query.getQuestion().equals(response.getQuestion())) {
                    sr = this.cache.addMessage(response);
                    if (sr == null) {
                        sr = this.cache.lookupRecords(current, this.type, this.credibility);
                    }
                    if (this.verbose) {
                        System.err.println(new StringBuffer().append("queried ").append(current).append(" ").append(Type.string(this.type)).toString());
                        System.err.println(sr);
                    }
                    processResponse(current, sr);
                } else {
                    this.badresponse = true;
                    this.badresponse_error = "response does not match query";
                }
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    this.timedout = true;
                } else {
                    this.networkerror = true;
                }
            }
        }
    }

    private void resolve(Name current, Name suffix) {
        Name tname;
        this.doneCurrent = false;
        if (suffix == null) {
            tname = current;
        } else {
            try {
                tname = Name.concatenate(current, suffix);
            } catch (NameTooLongException e) {
                this.nametoolong = true;
                return;
            }
        }
        lookup(tname);
    }

    public Record[] run() {
        if (this.done) {
            reset();
        }
        if (!this.name.isAbsolute()) {
            if (this.searchPath != null) {
                if (this.name.labels() > defaultNdots) {
                    resolve(this.name, Name.root);
                }
                if (!this.done) {
                    int i = 0;
                    while (i < this.searchPath.length) {
                        resolve(this.name, this.searchPath[i]);
                        if (!this.done) {
                            if (this.foundAlias) {
                                break;
                            }
                            i++;
                        } else {
                            return this.answers;
                        }
                    }
                }
                return this.answers;
            }
            resolve(this.name, Name.root);
        } else {
            resolve(this.name, null);
        }
        if (!this.done) {
            if (this.badresponse) {
                this.result = 2;
                this.error = this.badresponse_error;
                this.done = true;
            } else if (this.timedout) {
                this.result = 2;
                this.error = "timed out";
                this.done = true;
            } else if (this.networkerror) {
                this.result = 2;
                this.error = "network error";
                this.done = true;
            } else if (this.nxdomain) {
                this.result = 3;
                this.done = true;
            } else if (this.referral) {
                this.result = 1;
                this.error = "referral";
                this.done = true;
            } else if (this.nametoolong) {
                this.result = 1;
                this.error = "name too long";
                this.done = true;
            }
        }
        return this.answers;
    }

    private void checkDone() {
        if (!this.done || this.result == -1) {
            StringBuffer sb = new StringBuffer(new StringBuffer().append("Lookup of ").append(this.name).append(" ").toString());
            if (this.dclass != 1) {
                sb.append(new StringBuffer().append(DClass.string(this.dclass)).append(" ").toString());
            }
            sb.append(new StringBuffer().append(Type.string(this.type)).append(" isn't done").toString());
            throw new IllegalStateException(sb.toString());
        }
    }

    public Record[] getAnswers() {
        checkDone();
        return this.answers;
    }

    public Name[] getAliases() {
        checkDone();
        if (this.aliases == null) {
            return noAliases;
        }
        return (Name[]) this.aliases.toArray(new Name[this.aliases.size()]);
    }

    public int getResult() {
        checkDone();
        return this.result;
    }

    public String getErrorString() {
        checkDone();
        if (this.error != null) {
            return this.error;
        }
        switch (this.result) {
            case 0:
                return "successful";
            case 1:
                return "unrecoverable error";
            case 2:
                return "try again";
            case 3:
                return "host not found";
            case 4:
                return "type not found";
            default:
                throw new IllegalStateException("unknown result");
        }
    }
}
