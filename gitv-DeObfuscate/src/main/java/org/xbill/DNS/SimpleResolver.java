package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class SimpleResolver implements Resolver {
    public static final int DEFAULT_EDNS_PAYLOADSIZE = 1280;
    public static final int DEFAULT_PORT = 53;
    private static final short DEFAULT_UDPSIZE = (short) 512;
    private static String defaultResolver = "localhost";
    private static int uniqueID = 0;
    private InetSocketAddress address;
    private boolean ignoreTruncation;
    private InetSocketAddress localAddress;
    private OPTRecord queryOPT;
    private long timeoutValue;
    private TSIG tsig;
    private boolean useTCP;

    public SimpleResolver(String hostname) throws UnknownHostException {
        InetAddress addr;
        this.timeoutValue = 10000;
        if (hostname == null) {
            hostname = ResolverConfig.getCurrentConfig().server();
            if (hostname == null) {
                hostname = defaultResolver;
            }
        }
        if (hostname.equals("0")) {
            addr = InetAddress.getLocalHost();
        } else {
            addr = InetAddress.getByName(hostname);
        }
        this.address = new InetSocketAddress(addr, 53);
    }

    public SimpleResolver() throws UnknownHostException {
        this(null);
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public static void setDefaultResolver(String hostname) {
        defaultResolver = hostname;
    }

    public void setPort(int port) {
        this.address = new InetSocketAddress(this.address.getAddress(), port);
    }

    public void setAddress(InetSocketAddress addr) {
        this.address = addr;
    }

    public void setAddress(InetAddress addr) {
        this.address = new InetSocketAddress(addr, this.address.getPort());
    }

    public void setLocalAddress(InetSocketAddress addr) {
        this.localAddress = addr;
    }

    public void setLocalAddress(InetAddress addr) {
        this.localAddress = new InetSocketAddress(addr, 0);
    }

    public void setTCP(boolean flag) {
        this.useTCP = flag;
    }

    public void setIgnoreTruncation(boolean flag) {
        this.ignoreTruncation = flag;
    }

    public void setEDNS(int level, int payloadSize, int flags, List options) {
        if (level == 0 || level == -1) {
            if (payloadSize == 0) {
                payloadSize = 1280;
            }
            this.queryOPT = new OPTRecord(payloadSize, 0, level, flags, options);
            return;
        }
        throw new IllegalArgumentException("invalid EDNS level - must be 0 or -1");
    }

    public void setEDNS(int level) {
        setEDNS(level, 0, 0, null);
    }

    public void setTSIGKey(TSIG key) {
        this.tsig = key;
    }

    TSIG getTSIGKey() {
        return this.tsig;
    }

    public void setTimeout(int secs, int msecs) {
        this.timeoutValue = (((long) secs) * 1000) + ((long) msecs);
    }

    public void setTimeout(int secs) {
        setTimeout(secs, 0);
    }

    long getTimeout() {
        return this.timeoutValue;
    }

    private Message parseMessage(byte[] b) throws WireParseException {
        try {
            return new Message(b);
        } catch (IOException e) {
            IOException e2 = e;
            if (Options.check("verbose")) {
                e2.printStackTrace();
            }
            if (!(e2 instanceof WireParseException)) {
                e2 = new WireParseException("Error parsing message");
            }
            throw ((WireParseException) e2);
        }
    }

    private void verifyTSIG(Message query, Message response, byte[] b, TSIG tsig) {
        if (tsig != null) {
            int error = tsig.verify(response, b, query.getTSIG());
            if (Options.check("verbose")) {
                System.err.println(new StringBuffer().append("TSIG verify: ").append(Rcode.TSIGstring(error)).toString());
            }
        }
    }

    private void applyEDNS(Message query) {
        if (this.queryOPT != null && query.getOPT() == null) {
            query.addRecord(this.queryOPT, 3);
        }
    }

    private int maxUDPSize(Message query) {
        OPTRecord opt = query.getOPT();
        if (opt == null) {
            return 512;
        }
        return opt.getPayloadSize();
    }

    public Message send(Message query) throws IOException {
        if (Options.check("verbose")) {
            System.err.println(new StringBuffer().append("Sending to ").append(this.address.getAddress().getHostAddress()).append(SOAP.DELIM).append(this.address.getPort()).toString());
        }
        if (query.getHeader().getOpcode() == 0) {
            Record question = query.getQuestion();
            if (question != null && question.getType() == 252) {
                return sendAXFR(query);
            }
        }
        query = (Message) query.clone();
        applyEDNS(query);
        if (this.tsig != null) {
            this.tsig.apply(query, null);
        }
        byte[] out = query.toWire(Message.MAXLENGTH);
        int udpSize = maxUDPSize(query);
        boolean tcp = false;
        long endTime = System.currentTimeMillis() + this.timeoutValue;
        while (true) {
            byte[] in;
            if (this.useTCP || out.length > udpSize) {
                tcp = true;
            }
            if (tcp) {
                in = TCPClient.sendrecv(this.localAddress, this.address, out, endTime);
            } else {
                in = UDPClient.sendrecv(this.localAddress, this.address, out, udpSize, endTime);
            }
            if (in.length < 12) {
                throw new WireParseException("invalid DNS header - too short");
            }
            int id = ((in[0] & 255) << 8) + (in[1] & 255);
            int qid = query.getHeader().getID();
            if (id != qid) {
                String error = new StringBuffer().append("invalid message id: expected ").append(qid).append("; got id ").append(id).toString();
                if (tcp) {
                    throw new WireParseException(error);
                } else if (Options.check("verbose")) {
                    System.err.println(error);
                }
            } else {
                Message response = parseMessage(in);
                verifyTSIG(query, response, in, this.tsig);
                if (tcp || this.ignoreTruncation || !response.getHeader().getFlag(6)) {
                    return response;
                }
                tcp = true;
            }
        }
    }

    public Object sendAsync(Message query, ResolverListener listener) {
        Integer id;
        String qname;
        synchronized (this) {
            int i = uniqueID;
            uniqueID = i + 1;
            id = new Integer(i);
        }
        Record question = query.getQuestion();
        if (question != null) {
            qname = question.getName().toString();
        } else {
            qname = "(none)";
        }
        String name = new StringBuffer().append(getClass()).append(": ").append(qname).toString();
        Thread thread = new ResolveThread(this, query, id, listener);
        thread.setName(name);
        thread.setDaemon(true);
        thread.start();
        return id;
    }

    private Message sendAXFR(Message query) throws IOException {
        ZoneTransferIn xfrin = ZoneTransferIn.newAXFR(query.getQuestion().getName(), this.address, this.tsig);
        xfrin.setTimeout((int) (getTimeout() / 1000));
        xfrin.setLocalAddress(this.localAddress);
        try {
            xfrin.run();
            List<Record> records = xfrin.getAXFR();
            Message response = new Message(query.getHeader().getID());
            response.getHeader().setFlag(5);
            response.getHeader().setFlag(0);
            response.addRecord(query.getQuestion(), 0);
            for (Record addRecord : records) {
                response.addRecord(addRecord, 1);
            }
            return response;
        } catch (ZoneTransferException e) {
            throw new WireParseException(e.getMessage());
        }
    }
}
