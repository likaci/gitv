package org.xbill.DNS;

import java.io.IOException;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.utils.base64;

public class CERTRecord extends Record {
    public static final int OID = 254;
    public static final int PGP = 3;
    public static final int PKIX = 1;
    public static final int SPKI = 2;
    public static final int URI = 253;
    private static final long serialVersionUID = 4763014646517016835L;
    private int alg;
    private byte[] cert;
    private int certType;
    private int keyTag;

    public static class CertificateType {
        public static final int ACPKIX = 7;
        public static final int IACPKIX = 8;
        public static final int IPGP = 6;
        public static final int IPKIX = 4;
        public static final int ISPKI = 5;
        public static final int OID = 254;
        public static final int PGP = 3;
        public static final int PKIX = 1;
        public static final int SPKI = 2;
        public static final int URI = 253;
        private static Mnemonic types = new Mnemonic("Certificate type", 2);

        private CertificateType() {
        }

        static {
            types.setMaximum(Message.MAXLENGTH);
            types.setNumericAllowed(true);
            types.add(1, "PKIX");
            types.add(2, "SPKI");
            types.add(3, "PGP");
            types.add(1, "IPKIX");
            types.add(2, "ISPKI");
            types.add(3, "IPGP");
            types.add(3, "ACPKIX");
            types.add(3, "IACPKIX");
            types.add(253, "URI");
            types.add(254, "OID");
        }

        public static String string(int type) {
            return types.getText(type);
        }

        public static int value(String s) {
            return types.getValue(s);
        }
    }

    CERTRecord() {
    }

    Record getObject() {
        return new CERTRecord();
    }

    public CERTRecord(Name name, int dclass, long ttl, int certType, int keyTag, int alg, byte[] cert) {
        super(name, 37, dclass, ttl);
        this.certType = checkU16("certType", certType);
        this.keyTag = checkU16("keyTag", keyTag);
        this.alg = checkU8("alg", alg);
        this.cert = cert;
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.certType = in.readU16();
        this.keyTag = in.readU16();
        this.alg = in.readU8();
        this.cert = in.readByteArray();
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String certTypeString = st.getString();
        this.certType = CertificateType.value(certTypeString);
        if (this.certType < 0) {
            throw st.exception(new StringBuffer().append("Invalid certificate type: ").append(certTypeString).toString());
        }
        this.keyTag = st.getUInt16();
        String algString = st.getString();
        this.alg = Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception(new StringBuffer().append("Invalid algorithm: ").append(algString).toString());
        }
        this.cert = st.getBase64();
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.certType);
        sb.append(" ");
        sb.append(this.keyTag);
        sb.append(" ");
        sb.append(this.alg);
        if (this.cert != null) {
            if (Options.check("multiline")) {
                sb.append(" (\n");
                sb.append(base64.formatString(this.cert, 64, HTTP.TAB, true));
            } else {
                sb.append(" ");
                sb.append(base64.toString(this.cert));
            }
        }
        return sb.toString();
    }

    public int getCertType() {
        return this.certType;
    }

    public int getKeyTag() {
        return this.keyTag;
    }

    public int getAlgorithm() {
        return this.alg;
    }

    public byte[] getCert() {
        return this.cert;
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU16(this.certType);
        out.writeU16(this.keyTag);
        out.writeU8(this.alg);
        out.writeByteArray(this.cert);
    }
}
