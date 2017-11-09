package defpackage;

import com.gala.sdk.plugin.server.PluginManager;
import com.gala.tvapi.tv2.model.Album;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.xbill.DNS.Address;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.OPTRecord;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Record;
import org.xbill.DNS.SetResponse;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TSIGRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.Zone;
import org.xbill.DNS.ZoneTransferException;

public class jnamed {
    static final int FLAG_DNSSECOK = 1;
    static final int FLAG_SIGONLY = 2;
    Map TSIGs;
    Map caches;
    Map znames;

    private static String addrport(InetAddress addr, int port) {
        return new StringBuffer().append(addr.getHostAddress()).append("#").append(port).toString();
    }

    public jnamed(String conffile) throws IOException, ZoneTransferException {
        List<Integer> ports = new ArrayList();
        List<InetAddress> addresses = new ArrayList();
        try {
            FileInputStream fs = new FileInputStream(conffile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            try {
                this.caches = new HashMap();
                this.znames = new HashMap();
                this.TSIGs = new HashMap();
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    StringTokenizer stringTokenizer = new StringTokenizer(line);
                    if (stringTokenizer.hasMoreTokens()) {
                        String keyword = stringTokenizer.nextToken();
                        if (!stringTokenizer.hasMoreTokens()) {
                            System.out.println(new StringBuffer().append("Invalid line: ").append(line).toString());
                        } else if (keyword.charAt(0) != '#') {
                            if (keyword.equals("primary")) {
                                addPrimaryZone(stringTokenizer.nextToken(), stringTokenizer.nextToken());
                            } else if (keyword.equals("secondary")) {
                                addSecondaryZone(stringTokenizer.nextToken(), stringTokenizer.nextToken());
                            } else if (keyword.equals("cache")) {
                                Cache cache = new Cache(stringTokenizer.nextToken());
                                this.caches.put(new Integer(1), cache);
                            } else if (keyword.equals(Album.KEY)) {
                                String s1 = stringTokenizer.nextToken();
                                String s2 = stringTokenizer.nextToken();
                                if (stringTokenizer.hasMoreTokens()) {
                                    addTSIG(s1, s2, stringTokenizer.nextToken());
                                } else {
                                    addTSIG("hmac-md5", s1, s2);
                                }
                            } else if (keyword.equals("port")) {
                                ports.add(Integer.valueOf(stringTokenizer.nextToken()));
                            } else if (keyword.equals("address")) {
                                addresses.add(Address.getByAddress(stringTokenizer.nextToken()));
                            } else {
                                System.out.println(new StringBuffer().append("unknown keyword: ").append(keyword).toString());
                            }
                        }
                    }
                }
                if (ports.size() == 0) {
                    ports.add(new Integer(53));
                }
                if (addresses.size() == 0) {
                    addresses.add(Address.getByAddress(PluginManager.DEFAULT_PLUGIN_VERSION));
                }
                for (InetAddress addr : addresses) {
                    for (Integer intValue : ports) {
                        int port = intValue.intValue();
                        addUDP(addr, port);
                        addTCP(addr, port);
                        System.out.println(new StringBuffer().append("jnamed: listening on ").append(jnamed.addrport(addr, port)).toString());
                    }
                }
                System.out.println("jnamed: running");
            } finally {
                fs.close();
            }
        } catch (Exception e) {
            System.out.println(new StringBuffer().append("Cannot open ").append(conffile).toString());
        }
    }

    public void addPrimaryZone(String zname, String zonefile) throws IOException {
        Name origin = null;
        if (zname != null) {
            origin = Name.fromString(zname, Name.root);
        }
        Zone newzone = new Zone(origin, zonefile);
        this.znames.put(newzone.getOrigin(), newzone);
    }

    public void addSecondaryZone(String zone, String remote) throws IOException, ZoneTransferException {
        Name zname = Name.fromString(zone, Name.root);
        this.znames.put(zname, new Zone(zname, 1, remote));
    }

    public void addTSIG(String algstr, String namestr, String key) throws IOException {
        this.TSIGs.put(Name.fromString(namestr, Name.root), new TSIG(algstr, namestr, key));
    }

    public Cache getCache(int dclass) {
        Cache c = (Cache) this.caches.get(new Integer(dclass));
        if (c != null) {
            return c;
        }
        c = new Cache(dclass);
        this.caches.put(new Integer(dclass), c);
        return c;
    }

    public Zone findBestZone(Name name) {
        Zone foundzone = (Zone) this.znames.get(name);
        if (foundzone != null) {
            return foundzone;
        }
        int labels = name.labels();
        for (int i = 1; i < labels; i++) {
            foundzone = (Zone) this.znames.get(new Name(name, i));
            if (foundzone != null) {
                return foundzone;
            }
        }
        return null;
    }

    public RRset findExactMatch(Name name, int type, int dclass, boolean glue) {
        Zone zone = findBestZone(name);
        if (zone != null) {
            return zone.findExactMatch(name, type);
        }
        RRset[] rrsets;
        Cache cache = getCache(dclass);
        if (glue) {
            rrsets = cache.findAnyRecords(name, type);
        } else {
            rrsets = cache.findRecords(name, type);
        }
        if (rrsets == null) {
            return null;
        }
        return rrsets[0];
    }

    void addRRset(Name name, Message response, RRset rrset, int section, int flags) {
        Iterator it;
        Record r;
        int s = 1;
        while (s <= section) {
            if (!response.findRRset(name, rrset.getType(), s)) {
                s++;
            } else {
                return;
            }
        }
        if ((flags & 2) == 0) {
            it = rrset.rrs();
            while (it.hasNext()) {
                r = (Record) it.next();
                if (r.getName().isWild() && !name.isWild()) {
                    r = r.withName(name);
                }
                response.addRecord(r, section);
            }
        }
        if ((flags & 3) != 0) {
            it = rrset.sigs();
            while (it.hasNext()) {
                r = (Record) it.next();
                if (r.getName().isWild() && !name.isWild()) {
                    r = r.withName(name);
                }
                response.addRecord(r, section);
            }
        }
    }

    private final void addSOA(Message response, Zone zone) {
        response.addRecord(zone.getSOA(), 2);
    }

    private final void addNS(Message response, Zone zone, int flags) {
        RRset nsRecords = zone.getNS();
        addRRset(nsRecords.getName(), response, nsRecords, 2, flags);
    }

    private final void addCacheNS(Message response, Cache cache, Name name) {
        SetResponse sr = cache.lookupRecords(name, 2, 0);
        if (sr.isDelegation()) {
            Iterator it = sr.getNS().rrs();
            while (it.hasNext()) {
                response.addRecord((Record) it.next(), 2);
            }
        }
    }

    private void addGlue(Message response, Name name, int flags) {
        RRset a = findExactMatch(name, 1, 1, true);
        if (a != null) {
            addRRset(name, response, a, 3, flags);
        }
    }

    private void addAdditional2(Message response, int section, int flags) {
        Record[] records = response.getSectionArray(section);
        for (Record r : records) {
            Name glueName = r.getAdditionalName();
            if (glueName != null) {
                addGlue(response, glueName, flags);
            }
        }
    }

    private final void addAdditional(Message response, int flags) {
        addAdditional2(response, 1, flags);
        addAdditional2(response, 2, flags);
    }

    byte addAnswer(Message response, Name name, int type, int dclass, int iterations, int flags) {
        byte rcode = (byte) 0;
        if (iterations > 6) {
            return (byte) 0;
        }
        SetResponse sr;
        if (type == 24 || type == 46) {
            type = 255;
            flags |= 2;
        }
        Zone zone = findBestZone(name);
        if (zone != null) {
            sr = zone.findRecords(name, type);
        } else {
            sr = getCache(dclass).lookupRecords(name, type, 3);
        }
        if (sr.isUnknown()) {
            addCacheNS(response, getCache(dclass), name);
        }
        if (sr.isNXDOMAIN()) {
            response.getHeader().setRcode(3);
            if (zone != null) {
                addSOA(response, zone);
                if (iterations == 0) {
                    response.getHeader().setFlag(5);
                }
            }
            rcode = (byte) 3;
        } else if (sr.isNXRRSET()) {
            if (zone != null) {
                addSOA(response, zone);
                if (iterations == 0) {
                    response.getHeader().setFlag(5);
                }
            }
        } else if (sr.isDelegation()) {
            RRset nsRecords = sr.getNS();
            addRRset(nsRecords.getName(), response, nsRecords, 2, flags);
        } else if (sr.isCNAME()) {
            Record cname = sr.getCNAME();
            addRRset(name, response, new RRset(cname), 1, flags);
            if (zone != null && iterations == 0) {
                response.getHeader().setFlag(5);
            }
            rcode = addAnswer(response, cname.getTarget(), type, dclass, iterations + 1, flags);
        } else if (sr.isDNAME()) {
            Record dname = sr.getDNAME();
            addRRset(name, response, new RRset(dname), 1, flags);
            try {
                Name newname = name.fromDNAME(dname);
                addRRset(name, response, new RRset(new CNAMERecord(name, dclass, 0, newname)), 1, flags);
                if (zone != null && iterations == 0) {
                    response.getHeader().setFlag(5);
                }
                rcode = addAnswer(response, newname, type, dclass, iterations + 1, flags);
            } catch (NameTooLongException e) {
                return (byte) 6;
            }
        } else if (sr.isSuccessful()) {
            RRset[] rrsets = sr.answers();
            for (RRset addRRset : rrsets) {
                addRRset(name, response, addRRset, 1, flags);
            }
            if (zone != null) {
                addNS(response, zone, flags);
                if (iterations == 0) {
                    response.getHeader().setFlag(5);
                }
            } else {
                addCacheNS(response, getCache(dclass), name);
            }
        }
        return rcode;
    }

    byte[] doAXFR(Name name, Message query, TSIG tsig, TSIGRecord qtsig, Socket s) {
        Zone zone = (Zone) this.znames.get(name);
        boolean first = true;
        if (zone == null) {
            return errorMessage(query, 5);
        }
        Iterator it = zone.AXFR();
        try {
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
            int id = query.getHeader().getID();
            while (it.hasNext()) {
                RRset rrset = (RRset) it.next();
                Message response = new Message(id);
                Header header = response.getHeader();
                header.setFlag(0);
                header.setFlag(5);
                addRRset(rrset.getName(), response, rrset, 1, 1);
                if (tsig != null) {
                    tsig.applyStream(response, qtsig, first);
                    qtsig = response.getTSIG();
                }
                first = false;
                byte[] out = response.toWire();
                dataOut.writeShort(out.length);
                dataOut.write(out);
            }
        } catch (IOException e) {
            System.out.println("AXFR failed");
        }
        try {
            s.close();
        } catch (IOException e2) {
        }
        return null;
    }

    byte[] generateReply(Message query, byte[] in, int length, Socket s) throws IOException {
        int flags = 0;
        Header header = query.getHeader();
        if (header.getFlag(0)) {
            return null;
        }
        if (header.getRcode() != 0) {
            return errorMessage(query, 1);
        }
        if (header.getOpcode() != 0) {
            return errorMessage(query, 4);
        }
        Record queryRecord = query.getQuestion();
        TSIGRecord queryTSIG = query.getTSIG();
        TSIG tsig = null;
        if (queryTSIG != null) {
            tsig = (TSIG) this.TSIGs.get(queryTSIG.getName());
            if (tsig == null || tsig.verify(query, in, length, null) != (byte) 0) {
                return formerrMessage(in);
            }
        }
        OPTRecord queryOPT = query.getOPT();
        int maxLength;
        Message response;
        Name name;
        int type;
        int dclass;
        byte rcode;
        if (queryOPT == null || queryOPT.getVersion() > 0) {
            if (s != null) {
                maxLength = Message.MAXLENGTH;
            } else if (queryOPT == null) {
                maxLength = Math.max(queryOPT.getPayloadSize(), 512);
            } else {
                maxLength = 512;
            }
            if (!(queryOPT == null || (queryOPT.getFlags() & 32768) == 0)) {
                flags = 1;
            }
            response = new Message(query.getHeader().getID());
            response.getHeader().setFlag(0);
            if (query.getHeader().getFlag(7)) {
                response.getHeader().setFlag(7);
            }
            response.addRecord(queryRecord, 0);
            name = queryRecord.getName();
            type = queryRecord.getType();
            dclass = queryRecord.getDClass();
            if (type != 252 && s != null) {
                return doAXFR(name, query, tsig, queryTSIG, s);
            }
            if (Type.isRR(type) && type != 255) {
                return errorMessage(query, 4);
            }
            rcode = addAnswer(response, name, type, dclass, 0, flags);
            if (rcode == (byte) 0 && rcode != (byte) 3) {
                return errorMessage(query, rcode);
            }
            addAdditional(response, flags);
            if (queryOPT != null) {
                response.addRecord(new OPTRecord(4096, rcode, 0, flags != 1 ? 32768 : 0), 3);
            }
            response.setTSIG(tsig, 0, queryTSIG);
            return response.toWire(maxLength);
        }
        if (s != null) {
            maxLength = Message.MAXLENGTH;
        } else if (queryOPT == null) {
            maxLength = 512;
        } else {
            maxLength = Math.max(queryOPT.getPayloadSize(), 512);
        }
        flags = 1;
        response = new Message(query.getHeader().getID());
        response.getHeader().setFlag(0);
        if (query.getHeader().getFlag(7)) {
            response.getHeader().setFlag(7);
        }
        response.addRecord(queryRecord, 0);
        name = queryRecord.getName();
        type = queryRecord.getType();
        dclass = queryRecord.getDClass();
        if (type != 252) {
        }
        if (Type.isRR(type)) {
        }
        rcode = addAnswer(response, name, type, dclass, 0, flags);
        if (rcode == (byte) 0) {
        }
        addAdditional(response, flags);
        if (queryOPT != null) {
            if (flags != 1) {
            }
            response.addRecord(new OPTRecord(4096, rcode, 0, flags != 1 ? 32768 : 0), 3);
        }
        response.setTSIG(tsig, 0, queryTSIG);
        return response.toWire(maxLength);
    }

    byte[] buildErrorMessage(Header header, int rcode, Record question) {
        Message response = new Message();
        response.setHeader(header);
        for (int i = 0; i < 4; i++) {
            response.removeAllRecords(i);
        }
        if (rcode == 2) {
            response.addRecord(question, 0);
        }
        header.setRcode(rcode);
        return response.toWire();
    }

    public byte[] formerrMessage(byte[] in) {
        try {
            return buildErrorMessage(new Header(in), 1, null);
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] errorMessage(Message query, int rcode) {
        return buildErrorMessage(query.getHeader(), rcode, query.getQuestion());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void TCPclient(java.net.Socket r13) {
        /*
        r12 = this;
        r5 = r13.getInputStream();	 Catch:{ IOException -> 0x003e }
        r0 = new java.io.DataInputStream;	 Catch:{ IOException -> 0x003e }
        r0.<init>(r5);	 Catch:{ IOException -> 0x003e }
        r4 = r0.readUnsignedShort();	 Catch:{ IOException -> 0x003e }
        r3 = new byte[r4];	 Catch:{ IOException -> 0x003e }
        r0.readFully(r3);	 Catch:{ IOException -> 0x003e }
        r7 = 0;
        r6 = new org.xbill.DNS.Message;	 Catch:{ IOException -> 0x0023 }
        r6.<init>(r3);	 Catch:{ IOException -> 0x0023 }
        r8 = r3.length;	 Catch:{ IOException -> 0x0023 }
        r7 = r12.generateReply(r6, r3, r8, r13);	 Catch:{ IOException -> 0x0023 }
        if (r7 != 0) goto L_0x0028;
    L_0x001f:
        r13.close();	 Catch:{ IOException -> 0x007a }
    L_0x0022:
        return;
    L_0x0023:
        r2 = move-exception;
        r7 = r12.formerrMessage(r3);	 Catch:{ IOException -> 0x003e }
    L_0x0028:
        r1 = new java.io.DataOutputStream;	 Catch:{ IOException -> 0x003e }
        r8 = r13.getOutputStream();	 Catch:{ IOException -> 0x003e }
        r1.<init>(r8);	 Catch:{ IOException -> 0x003e }
        r8 = r7.length;	 Catch:{ IOException -> 0x003e }
        r1.writeShort(r8);	 Catch:{ IOException -> 0x003e }
        r1.write(r7);	 Catch:{ IOException -> 0x003e }
        r13.close();	 Catch:{ IOException -> 0x003c }
        goto L_0x0022;
    L_0x003c:
        r8 = move-exception;
        goto L_0x0022;
    L_0x003e:
        r2 = move-exception;
        r8 = java.lang.System.out;	 Catch:{ all -> 0x0075 }
        r9 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0075 }
        r9.<init>();	 Catch:{ all -> 0x0075 }
        r10 = "TCPclient(";
        r9 = r9.append(r10);	 Catch:{ all -> 0x0075 }
        r10 = r13.getLocalAddress();	 Catch:{ all -> 0x0075 }
        r11 = r13.getLocalPort();	 Catch:{ all -> 0x0075 }
        r10 = defpackage.jnamed.addrport(r10, r11);	 Catch:{ all -> 0x0075 }
        r9 = r9.append(r10);	 Catch:{ all -> 0x0075 }
        r10 = "): ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x0075 }
        r9 = r9.append(r2);	 Catch:{ all -> 0x0075 }
        r9 = r9.toString();	 Catch:{ all -> 0x0075 }
        r8.println(r9);	 Catch:{ all -> 0x0075 }
        r13.close();	 Catch:{ IOException -> 0x0073 }
        goto L_0x0022;
    L_0x0073:
        r8 = move-exception;
        goto L_0x0022;
    L_0x0075:
        r8 = move-exception;
        r13.close();	 Catch:{ IOException -> 0x007c }
    L_0x0079:
        throw r8;
    L_0x007a:
        r8 = move-exception;
        goto L_0x0022;
    L_0x007c:
        r9 = move-exception;
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: jnamed.TCPclient(java.net.Socket):void");
    }

    public void serveTCP(InetAddress addr, int port) {
        try {
            while (true) {
                new Thread(new jnamed$1(this, new ServerSocket(port, 128, addr).accept())).start();
            }
        } catch (IOException e) {
            System.out.println(new StringBuffer().append("serveTCP(").append(jnamed.addrport(addr, port)).append("): ").append(e).toString());
        }
    }

    public void serveUDP(InetAddress addr, int port) {
        try {
            DatagramSocket sock = new DatagramSocket(port, addr);
            byte[] in = new byte[512];
            DatagramPacket indp = new DatagramPacket(in, in.length);
            DatagramPacket outdp = null;
            while (true) {
                indp.setLength(in.length);
                try {
                    byte[] response;
                    sock.receive(indp);
                    try {
                        response = generateReply(new Message(in), in, indp.getLength(), null);
                        if (response == null) {
                            continue;
                        }
                    } catch (IOException e) {
                        response = formerrMessage(in);
                    }
                    if (outdp == null) {
                        outdp = new DatagramPacket(response, response.length, indp.getAddress(), indp.getPort());
                    } else {
                        outdp.setData(response);
                        outdp.setLength(response.length);
                        outdp.setAddress(indp.getAddress());
                        outdp.setPort(indp.getPort());
                    }
                    sock.send(outdp);
                } catch (InterruptedIOException e2) {
                }
            }
        } catch (IOException e3) {
            System.out.println(new StringBuffer().append("serveUDP(").append(jnamed.addrport(addr, port)).append("): ").append(e3).toString());
        }
    }

    public void addTCP(InetAddress addr, int port) {
        new Thread(new jnamed$2(this, addr, port)).start();
    }

    public void addUDP(InetAddress addr, int port) {
        new Thread(new jnamed$3(this, addr, port)).start();
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("usage: jnamed [conf]");
            System.exit(0);
        }
        try {
            String conf;
            if (args.length == 1) {
                conf = args[0];
            } else {
                conf = "jnamed.conf";
            }
            jnamed jnamed = new jnamed(conf);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ZoneTransferException e2) {
            System.out.println(e2);
        }
    }
}
