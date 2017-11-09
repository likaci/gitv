package org.xbill.DNS;

import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.TSIG.StreamVerifier;

public class ZoneTransferIn {
    private static final int AXFR = 6;
    private static final int END = 7;
    private static final int FIRSTDATA = 1;
    private static final int INITIALSOA = 0;
    private static final int IXFR_ADD = 5;
    private static final int IXFR_ADDSOA = 4;
    private static final int IXFR_DEL = 3;
    private static final int IXFR_DELSOA = 2;
    private SocketAddress address;
    private TCPClient client;
    private long current_serial;
    private int dclass;
    private long end_serial;
    private ZoneTransferHandler handler;
    private Record initialsoa;
    private long ixfr_serial;
    private SocketAddress localAddress;
    private int qtype;
    private int rtype;
    private int state;
    private long timeout = IOpenApiCommandHolder.OAA_CONNECT_DURATION;
    private TSIG tsig;
    private StreamVerifier verifier;
    private boolean want_fallback;
    private Name zname;

    static class C22071 {
    }

    public interface ZoneTransferHandler {
        void handleRecord(Record record) throws ZoneTransferException;

        void startAXFR() throws ZoneTransferException;

        void startIXFR() throws ZoneTransferException;

        void startIXFRAdds(Record record) throws ZoneTransferException;

        void startIXFRDeletes(Record record) throws ZoneTransferException;
    }

    private static class BasicHandler implements ZoneTransferHandler {
        private List axfr;
        private List ixfr;

        private BasicHandler() {
        }

        BasicHandler(C22071 x0) {
            this();
        }

        static List access$300(BasicHandler x0) {
            return x0.axfr;
        }

        static List access$400(BasicHandler x0) {
            return x0.ixfr;
        }

        public void startAXFR() {
            this.axfr = new ArrayList();
        }

        public void startIXFR() {
            this.ixfr = new ArrayList();
        }

        public void startIXFRDeletes(Record soa) {
            Delta delta = new Delta(null);
            delta.deletes.add(soa);
            delta.start = ZoneTransferIn.access$100(soa);
            this.ixfr.add(delta);
        }

        public void startIXFRAdds(Record soa) {
            Delta delta = (Delta) this.ixfr.get(this.ixfr.size() - 1);
            delta.adds.add(soa);
            delta.end = ZoneTransferIn.access$100(soa);
        }

        public void handleRecord(Record r) {
            List list;
            if (this.ixfr != null) {
                Delta delta = (Delta) this.ixfr.get(this.ixfr.size() - 1);
                if (delta.adds.size() > 0) {
                    list = delta.adds;
                } else {
                    list = delta.deletes;
                }
            } else {
                list = this.axfr;
            }
            list.add(r);
        }
    }

    public static class Delta {
        public List adds;
        public List deletes;
        public long end;
        public long start;

        Delta(C22071 x0) {
            this();
        }

        private Delta() {
            this.adds = new ArrayList();
            this.deletes = new ArrayList();
        }
    }

    static long access$100(Record x0) {
        return getSOASerial(x0);
    }

    private ZoneTransferIn() {
    }

    private ZoneTransferIn(Name zone, int xfrtype, long serial, boolean fallback, SocketAddress address, TSIG key) {
        this.address = address;
        this.tsig = key;
        if (zone.isAbsolute()) {
            this.zname = zone;
        } else {
            try {
                this.zname = Name.concatenate(zone, Name.root);
            } catch (NameTooLongException e) {
                throw new IllegalArgumentException("ZoneTransferIn: name too long");
            }
        }
        this.qtype = xfrtype;
        this.dclass = 1;
        this.ixfr_serial = serial;
        this.want_fallback = fallback;
        this.state = 0;
    }

    public static ZoneTransferIn newAXFR(Name zone, SocketAddress address, TSIG key) {
        return new ZoneTransferIn(zone, 252, 0, false, address, key);
    }

    public static ZoneTransferIn newAXFR(Name zone, String host, int port, TSIG key) throws UnknownHostException {
        if (port == 0) {
            port = 53;
        }
        return newAXFR(zone, new InetSocketAddress(host, port), key);
    }

    public static ZoneTransferIn newAXFR(Name zone, String host, TSIG key) throws UnknownHostException {
        return newAXFR(zone, host, 0, key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, SocketAddress address, TSIG key) {
        return new ZoneTransferIn(zone, Type.IXFR, serial, fallback, address, key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, String host, int port, TSIG key) throws UnknownHostException {
        if (port == 0) {
            port = 53;
        }
        return newIXFR(zone, serial, fallback, new InetSocketAddress(host, port), key);
    }

    public static ZoneTransferIn newIXFR(Name zone, long serial, boolean fallback, String host, TSIG key) throws UnknownHostException {
        return newIXFR(zone, serial, fallback, host, 0, key);
    }

    public Name getName() {
        return this.zname;
    }

    public int getType() {
        return this.qtype;
    }

    public void setTimeout(int secs) {
        if (secs < 0) {
            throw new IllegalArgumentException("timeout cannot be negative");
        }
        this.timeout = 1000 * ((long) secs);
    }

    public void setDClass(int dclass) {
        DClass.check(dclass);
        this.dclass = dclass;
    }

    public void setLocalAddress(SocketAddress addr) {
        this.localAddress = addr;
    }

    private void openConnection() throws IOException {
        this.client = new TCPClient(System.currentTimeMillis() + this.timeout);
        if (this.localAddress != null) {
            this.client.bind(this.localAddress);
        }
        this.client.connect(this.address);
    }

    private void sendQuery() throws IOException {
        Record question = Record.newRecord(this.zname, this.qtype, this.dclass);
        Message query = new Message();
        query.getHeader().setOpcode(0);
        query.addRecord(question, 0);
        if (this.qtype == Type.IXFR) {
            query.addRecord(new SOARecord(this.zname, this.dclass, 0, Name.root, Name.root, this.ixfr_serial, 0, 0, 0, 0), 2);
        }
        if (this.tsig != null) {
            this.tsig.apply(query, null);
            this.verifier = new StreamVerifier(this.tsig, query.getTSIG());
        }
        this.client.send(query.toWire(Message.MAXLENGTH));
    }

    private static long getSOASerial(Record rec) {
        return ((SOARecord) rec).getSerial();
    }

    private void logxfr(String s) {
        if (Options.check("verbose")) {
            System.out.println(new StringBuffer().append(this.zname).append(": ").append(s).toString());
        }
    }

    private void fail(String s) throws ZoneTransferException {
        throw new ZoneTransferException(s);
    }

    private void fallback() throws ZoneTransferException {
        if (!this.want_fallback) {
            fail("server doesn't support IXFR");
        }
        logxfr("falling back to AXFR");
        this.qtype = 252;
        this.state = 0;
    }

    private void parseRR(Record rec) throws ZoneTransferException {
        int type = rec.getType();
        switch (this.state) {
            case 0:
                if (type != 6) {
                    fail("missing initial SOA");
                }
                this.initialsoa = rec;
                this.end_serial = getSOASerial(rec);
                if (this.qtype != Type.IXFR || Serial.compare(this.end_serial, this.ixfr_serial) > 0) {
                    this.state = 1;
                    return;
                }
                logxfr("up to date");
                this.state = 7;
                return;
            case 1:
                if (this.qtype == Type.IXFR && type == 6 && getSOASerial(rec) == this.ixfr_serial) {
                    this.rtype = Type.IXFR;
                    this.handler.startIXFR();
                    logxfr("got incremental response");
                    this.state = 2;
                } else {
                    this.rtype = 252;
                    this.handler.startAXFR();
                    this.handler.handleRecord(this.initialsoa);
                    logxfr("got nonincremental response");
                    this.state = 6;
                }
                parseRR(rec);
                return;
            case 2:
                this.handler.startIXFRDeletes(rec);
                this.state = 3;
                return;
            case 3:
                if (type == 6) {
                    this.current_serial = getSOASerial(rec);
                    this.state = 4;
                    parseRR(rec);
                    return;
                }
                this.handler.handleRecord(rec);
                return;
            case 4:
                this.handler.startIXFRAdds(rec);
                this.state = 5;
                return;
            case 5:
                if (type == 6) {
                    long soa_serial = getSOASerial(rec);
                    if (soa_serial == this.end_serial) {
                        this.state = 7;
                        return;
                    } else if (soa_serial != this.current_serial) {
                        fail(new StringBuffer().append("IXFR out of sync: expected serial ").append(this.current_serial).append(" , got ").append(soa_serial).toString());
                    } else {
                        this.state = 2;
                        parseRR(rec);
                        return;
                    }
                }
                this.handler.handleRecord(rec);
                return;
            case 6:
                if (type != 1 || rec.getDClass() == this.dclass) {
                    this.handler.handleRecord(rec);
                    if (type == 6) {
                        this.state = 7;
                        return;
                    }
                    return;
                }
                return;
            case 7:
                fail("extra data");
                return;
            default:
                fail("invalid state");
                return;
        }
    }

    private void closeConnection() {
        try {
            if (this.client != null) {
                this.client.cleanup();
            }
        } catch (IOException e) {
        }
    }

    private Message parseMessage(byte[] b) throws WireParseException {
        try {
            return new Message(b);
        } catch (IOException e) {
            if (e instanceof WireParseException) {
                throw ((WireParseException) e);
            }
            throw new WireParseException("Error parsing message");
        }
    }

    private void doxfr() throws IOException, ZoneTransferException {
        sendQuery();
        while (this.state != 7) {
            byte[] in = this.client.recv();
            Message response = parseMessage(in);
            if (response.getHeader().getRcode() == 0 && this.verifier != null) {
                TSIGRecord tsigrec = response.getTSIG();
                if (this.verifier.verify(response, in) != 0) {
                    fail("TSIG failure");
                }
            }
            Record[] answers = response.getSectionArray(1);
            if (this.state == 0) {
                int rcode = response.getRcode();
                if (rcode != 0) {
                    if (this.qtype == Type.IXFR && rcode == 4) {
                        fallback();
                        doxfr();
                        return;
                    }
                    fail(Rcode.string(rcode));
                }
                Record question = response.getQuestion();
                if (!(question == null || question.getType() == this.qtype)) {
                    fail("invalid question section");
                }
                if (answers.length == 0 && this.qtype == Type.IXFR) {
                    fallback();
                    doxfr();
                    return;
                }
            }
            for (Record parseRR : answers) {
                parseRR(parseRR);
            }
            if (!(this.state != 7 || this.verifier == null || response.isVerified())) {
                fail("last message must be signed");
            }
        }
    }

    public void run(ZoneTransferHandler handler) throws IOException, ZoneTransferException {
        this.handler = handler;
        try {
            openConnection();
            doxfr();
        } finally {
            closeConnection();
        }
    }

    public List run() throws IOException, ZoneTransferException {
        BasicHandler handler = new BasicHandler(null);
        run(handler);
        if (BasicHandler.access$300(handler) != null) {
            return BasicHandler.access$300(handler);
        }
        return BasicHandler.access$400(handler);
    }

    private BasicHandler getBasicHandler() throws IllegalArgumentException {
        if (this.handler instanceof BasicHandler) {
            return (BasicHandler) this.handler;
        }
        throw new IllegalArgumentException("ZoneTransferIn used callback interface");
    }

    public boolean isAXFR() {
        return this.rtype == 252;
    }

    public List getAXFR() {
        return BasicHandler.access$300(getBasicHandler());
    }

    public boolean isIXFR() {
        return this.rtype == Type.IXFR;
    }

    public List getIXFR() {
        return BasicHandler.access$400(getBasicHandler());
    }

    public boolean isCurrent() {
        BasicHandler handler = getBasicHandler();
        return BasicHandler.access$300(handler) == null && BasicHandler.access$400(handler) == null;
    }
}
