package org.xbill.DNS;

import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.io.IOException;

public class SRVRecord extends Record {
    private static final long serialVersionUID = -3886460132387522052L;
    private int port;
    private int priority;
    private Name target;
    private int weight;

    SRVRecord() {
    }

    Record getObject() {
        return new SRVRecord();
    }

    public SRVRecord(Name name, int dclass, long ttl, int priority, int weight, int port, Name target) {
        super(name, 33, dclass, ttl);
        this.priority = Record.checkU16("priority", priority);
        this.weight = Record.checkU16("weight", weight);
        this.port = Record.checkU16("port", port);
        this.target = Record.checkName(Keys.TARGET, target);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.priority = in.readU16();
        this.weight = in.readU16();
        this.port = in.readU16();
        this.target = new Name(in);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.priority = st.getUInt16();
        this.weight = st.getUInt16();
        this.port = st.getUInt16();
        this.target = st.getName(origin);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(new StringBuffer().append(this.priority).append(" ").toString());
        sb.append(new StringBuffer().append(this.weight).append(" ").toString());
        sb.append(new StringBuffer().append(this.port).append(" ").toString());
        sb.append(this.target);
        return sb.toString();
    }

    public int getPriority() {
        return this.priority;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getPort() {
        return this.port;
    }

    public Name getTarget() {
        return this.target;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.priority);
        out.writeU16(this.weight);
        out.writeU16(this.port);
        this.target.toWire(out, null, canonical);
    }

    public Name getAdditionalName() {
        return this.target;
    }
}
