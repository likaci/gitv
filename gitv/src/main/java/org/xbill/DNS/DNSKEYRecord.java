package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.DNSSEC.DNSSECException;

public class DNSKEYRecord extends KEYBase {
    private static final long serialVersionUID = -8679800040426675002L;

    public static class Flags {
        public static final int REVOKE = 128;
        public static final int SEP_KEY = 1;
        public static final int ZONE_KEY = 256;

        private Flags() {
        }
    }

    public static class Protocol {
        public static final int DNSSEC = 3;

        private Protocol() {
        }
    }

    DNSKEYRecord() {
    }

    Record getObject() {
        return new DNSKEYRecord();
    }

    public DNSKEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, byte[] key) {
        super(name, 48, dclass, ttl, flags, proto, alg, key);
    }

    public DNSKEYRecord(Name name, int dclass, long ttl, int flags, int proto, int alg, PublicKey key) throws DNSSECException {
        super(name, 48, dclass, ttl, flags, proto, alg, DNSSEC.fromPublicKey(key, alg));
        this.publicKey = key;
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.flags = st.getUInt16();
        this.proto = st.getUInt8();
        String algString = st.getString();
        this.alg = Algorithm.value(algString);
        if (this.alg < 0) {
            throw st.exception(new StringBuffer().append("Invalid algorithm: ").append(algString).toString());
        }
        this.key = st.getBase64();
    }
}
