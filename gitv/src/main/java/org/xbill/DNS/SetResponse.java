package org.xbill.DNS;

import java.util.ArrayList;
import java.util.List;

public class SetResponse {
    static final int CNAME = 4;
    static final int DELEGATION = 3;
    static final int DNAME = 5;
    static final int NXDOMAIN = 1;
    static final int NXRRSET = 2;
    static final int SUCCESSFUL = 6;
    static final int UNKNOWN = 0;
    private static final SetResponse nxdomain = new SetResponse(1);
    private static final SetResponse nxrrset = new SetResponse(2);
    private static final SetResponse unknown = new SetResponse(0);
    private Object data;
    private int type;

    private SetResponse() {
    }

    SetResponse(int type, RRset rrset) {
        if (type < 0 || type > 6) {
            throw new IllegalArgumentException("invalid type");
        }
        this.type = type;
        this.data = rrset;
    }

    SetResponse(int type) {
        if (type < 0 || type > 6) {
            throw new IllegalArgumentException("invalid type");
        }
        this.type = type;
        this.data = null;
    }

    static SetResponse ofType(int type) {
        switch (type) {
            case 0:
                return unknown;
            case 1:
                return nxdomain;
            case 2:
                return nxrrset;
            case 3:
            case 4:
            case 5:
            case 6:
                SetResponse sr = new SetResponse();
                sr.type = type;
                sr.data = null;
                return sr;
            default:
                throw new IllegalArgumentException("invalid type");
        }
    }

    void addRRset(RRset rrset) {
        if (this.data == null) {
            this.data = new ArrayList();
        }
        this.data.add(rrset);
    }

    public boolean isUnknown() {
        return this.type == 0;
    }

    public boolean isNXDOMAIN() {
        return this.type == 1;
    }

    public boolean isNXRRSET() {
        return this.type == 2;
    }

    public boolean isDelegation() {
        return this.type == 3;
    }

    public boolean isCNAME() {
        return this.type == 4;
    }

    public boolean isDNAME() {
        return this.type == 5;
    }

    public boolean isSuccessful() {
        return this.type == 6;
    }

    public RRset[] answers() {
        if (this.type != 6) {
            return null;
        }
        List l = this.data;
        return (RRset[]) l.toArray(new RRset[l.size()]);
    }

    public CNAMERecord getCNAME() {
        return (CNAMERecord) ((RRset) this.data).first();
    }

    public DNAMERecord getDNAME() {
        return (DNAMERecord) ((RRset) this.data).first();
    }

    public RRset getNS() {
        return (RRset) this.data;
    }

    public String toString() {
        switch (this.type) {
            case 0:
                return "unknown";
            case 1:
                return "NXDOMAIN";
            case 2:
                return "NXRRSET";
            case 3:
                return new StringBuffer().append("delegation: ").append(this.data).toString();
            case 4:
                return new StringBuffer().append("CNAME: ").append(this.data).toString();
            case 5:
                return new StringBuffer().append("DNAME: ").append(this.data).toString();
            case 6:
                return "successful";
            default:
                throw new IllegalStateException();
        }
    }
}
