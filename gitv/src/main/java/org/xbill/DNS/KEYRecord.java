package org.xbill.DNS;

import com.push.mqttv3.internal.security.SSLSocketFactoryFactory;
import java.io.IOException;
import java.security.PublicKey;
import java.util.StringTokenizer;
import org.cybergarage.http.HTTP;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class KEYRecord extends KEYBase {
    public static final int FLAG_NOAUTH = 32768;
    public static final int FLAG_NOCONF = 16384;
    public static final int FLAG_NOKEY = 49152;
    public static final int OWNER_HOST = 512;
    public static final int OWNER_USER = 0;
    public static final int OWNER_ZONE = 256;
    public static final int PROTOCOL_ANY = 255;
    public static final int PROTOCOL_DNSSEC = 3;
    public static final int PROTOCOL_EMAIL = 2;
    public static final int PROTOCOL_IPSEC = 4;
    public static final int PROTOCOL_TLS = 1;
    private static final long serialVersionUID = 6385613447571488906L;

    public static class Flags {
        public static final int EXTEND = 4096;
        public static final int FLAG10 = 32;
        public static final int FLAG11 = 16;
        public static final int FLAG2 = 8192;
        public static final int FLAG4 = 2048;
        public static final int FLAG5 = 1024;
        public static final int FLAG8 = 128;
        public static final int FLAG9 = 64;
        public static final int HOST = 512;
        public static final int NOAUTH = 32768;
        public static final int NOCONF = 16384;
        public static final int NOKEY = 49152;
        public static final int NTYP3 = 768;
        public static final int OWNER_MASK = 768;
        public static final int SIG0 = 0;
        public static final int SIG1 = 1;
        public static final int SIG10 = 10;
        public static final int SIG11 = 11;
        public static final int SIG12 = 12;
        public static final int SIG13 = 13;
        public static final int SIG14 = 14;
        public static final int SIG15 = 15;
        public static final int SIG2 = 2;
        public static final int SIG3 = 3;
        public static final int SIG4 = 4;
        public static final int SIG5 = 5;
        public static final int SIG6 = 6;
        public static final int SIG7 = 7;
        public static final int SIG8 = 8;
        public static final int SIG9 = 9;
        public static final int USER = 0;
        public static final int USE_MASK = 49152;
        public static final int ZONE = 256;
        private static Mnemonic flags = new Mnemonic("KEY flags", 2);

        private Flags() {
        }

        static {
            flags.setMaximum(Message.MAXLENGTH);
            flags.setNumericAllowed(false);
            flags.add(16384, "NOCONF");
            flags.add(32768, "NOAUTH");
            flags.add(49152, "NOKEY");
            flags.add(8192, "FLAG2");
            flags.add(4096, "EXTEND");
            flags.add(2048, "FLAG4");
            flags.add(1024, "FLAG5");
            flags.add(0, "USER");
            flags.add(256, "ZONE");
            flags.add(512, HTTP.HOST);
            flags.add(768, "NTYP3");
            flags.add(128, "FLAG8");
            flags.add(64, "FLAG9");
            flags.add(32, "FLAG10");
            flags.add(16, "FLAG11");
            flags.add(0, "SIG0");
            flags.add(1, "SIG1");
            flags.add(2, "SIG2");
            flags.add(3, "SIG3");
            flags.add(4, "SIG4");
            flags.add(5, "SIG5");
            flags.add(6, "SIG6");
            flags.add(7, "SIG7");
            flags.add(8, "SIG8");
            flags.add(9, "SIG9");
            flags.add(10, "SIG10");
            flags.add(11, "SIG11");
            flags.add(12, "SIG12");
            flags.add(13, "SIG13");
            flags.add(14, "SIG14");
            flags.add(15, "SIG15");
        }

        public static int value(String s) {
            int value;
            try {
                value = Integer.parseInt(s);
                if (value < 0 || value > Message.MAXLENGTH) {
                    return -1;
                }
                return value;
            } catch (NumberFormatException e) {
                StringTokenizer st = new StringTokenizer(s, "|");
                value = 0;
                while (st.hasMoreTokens()) {
                    int val = flags.getValue(st.nextToken());
                    if (val < 0) {
                        return -1;
                    }
                    value |= val;
                }
                return value;
            }
        }
    }

    public static class Protocol {
        public static final int ANY = 255;
        public static final int DNSSEC = 3;
        public static final int EMAIL = 2;
        public static final int IPSEC = 4;
        public static final int NONE = 0;
        public static final int TLS = 1;
        private static Mnemonic protocols = new Mnemonic("KEY protocol", 2);

        private Protocol() {
        }

        static {
            protocols.setMaximum(255);
            protocols.setNumericAllowed(true);
            protocols.add(0, "NONE");
            protocols.add(1, SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            protocols.add(2, "EMAIL");
            protocols.add(3, "DNSSEC");
            protocols.add(4, "IPSEC");
            protocols.add(255, "ANY");
        }

        public static String string(int type) {
            return protocols.getText(type);
        }

        public static int value(String s) {
            return protocols.getValue(s);
        }
    }

    KEYRecord() {
    }

    Record getObject() {
        return new KEYRecord();
    }

    public KEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, 25, dclass, ttl, flags, proto, alg, key);
    }

    public KEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, PublicKey key) throws DNSSECException {
        super(name, 25, dclass, ttl, flags, proto, alg, DNSSEC.fromPublicKey(key, alg));
        this.publicKey = key;
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        String flagString = st.getIdentifier();
        this.flags = Flags.value(flagString);
        if (this.flags < 0) {
            throw st.exception(new StringBuffer().append("Invalid flags: ").append(flagString).toString());
        }
        String protoString = st.getIdentifier();
        this.proto = Protocol.value(protoString);
        if (this.proto < 0) {
            throw st.exception(new StringBuffer().append("Invalid protocol: ").append(protoString).toString());
        }
        String algString = st.getIdentifier();
        this.alg = Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception(new StringBuffer().append("Invalid algorithm: ").append(algString).toString());
        } else if ((this.flags & 49152) == 49152) {
            this.key = null;
        } else {
            this.key = st.getBase64();
        }
    }
}
