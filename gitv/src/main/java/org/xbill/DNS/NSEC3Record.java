package org.xbill.DNS;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base32.Alphabet;

public class NSEC3Record extends Record {
    public static final int SHA1_DIGEST_ID = 1;
    private static final base32 b32 = new base32(Alphabet.BASE32HEX, false, false);
    private static final long serialVersionUID = -7123504635968932855L;
    private int flags;
    private int hashAlg;
    private int iterations;
    private byte[] next;
    private byte[] salt;
    private TypeBitmap types;

    public static class Digest {
        public static final int SHA1 = 1;

        private Digest() {
        }
    }

    public static class Flags {
        public static final int OPT_OUT = 1;

        private Flags() {
        }
    }

    NSEC3Record() {
    }

    Record getObject() {
        return new NSEC3Record();
    }

    public NSEC3Record(Name name, int dclass, long ttl, int hashAlg, int flags, int iterations, byte[] salt, byte[] next, int[] types) {
        super(name, 50, dclass, ttl);
        this.hashAlg = checkU8("hashAlg", hashAlg);
        this.flags = checkU8("flags", flags);
        this.iterations = checkU16("iterations", iterations);
        if (salt != null) {
            if (salt.length > 255) {
                throw new IllegalArgumentException("Invalid salt");
            } else if (salt.length > 0) {
                this.salt = new byte[salt.length];
                System.arraycopy(salt, 0, this.salt, 0, salt.length);
            }
        }
        if (next.length > 255) {
            throw new IllegalArgumentException("Invalid next hash");
        }
        this.next = new byte[next.length];
        System.arraycopy(next, 0, this.next, 0, next.length);
        this.types = new TypeBitmap(types);
    }

    void rrFromWire(DNSInput in) throws IOException {
        this.hashAlg = in.readU8();
        this.flags = in.readU8();
        this.iterations = in.readU16();
        int salt_length = in.readU8();
        if (salt_length > 0) {
            this.salt = in.readByteArray(salt_length);
        } else {
            this.salt = null;
        }
        this.next = in.readByteArray(in.readU8());
        this.types = new TypeBitmap(in);
    }

    void rrToWire(DNSOutput out, Compression c, boolean canonical) {
        out.writeU8(this.hashAlg);
        out.writeU8(this.flags);
        out.writeU16(this.iterations);
        if (this.salt != null) {
            out.writeU8(this.salt.length);
            out.writeByteArray(this.salt);
        } else {
            out.writeU8(0);
        }
        out.writeU8(this.next.length);
        out.writeByteArray(this.next);
        this.types.toWire(out);
    }

    void rdataFromString(Tokenizer st, Name origin) throws IOException {
        this.hashAlg = st.getUInt8();
        this.flags = st.getUInt8();
        this.iterations = st.getUInt16();
        if (st.getString().equals("-")) {
            this.salt = null;
        } else {
            st.unget();
            this.salt = st.getHexString();
            if (this.salt.length > 255) {
                throw st.exception("salt value too long");
            }
        }
        this.next = st.getBase32String(b32);
        this.types = new TypeBitmap(st);
    }

    String rrToString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.hashAlg);
        sb.append(' ');
        sb.append(this.flags);
        sb.append(' ');
        sb.append(this.iterations);
        sb.append(' ');
        if (this.salt == null) {
            sb.append('-');
        } else {
            sb.append(base16.toString(this.salt));
        }
        sb.append(' ');
        sb.append(b32.toString(this.next));
        if (!this.types.empty()) {
            sb.append(' ');
            sb.append(this.types.toString());
        }
        return sb.toString();
    }

    public int getHashAlgorithm() {
        return this.hashAlg;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getIterations() {
        return this.iterations;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int type) {
        return this.types.contains(type);
    }

    static byte[] hashName(Name name, int hashAlg, int iterations, byte[] salt) throws NoSuchAlgorithmException {
        switch (hashAlg) {
            case 1:
                MessageDigest digest = MessageDigest.getInstance("sha-1");
                byte[] hash = null;
                for (int i = 0; i <= iterations; i++) {
                    digest.reset();
                    if (i == 0) {
                        digest.update(name.toWireCanonical());
                    } else {
                        digest.update(hash);
                    }
                    if (salt != null) {
                        digest.update(salt);
                    }
                    hash = digest.digest();
                }
                return hash;
            default:
                throw new NoSuchAlgorithmException(new StringBuffer().append("Unknown NSEC3 algorithmidentifier: ").append(hashAlg).toString());
        }
    }

    public byte[] hashName(Name name) throws NoSuchAlgorithmException {
        return hashName(name, this.hashAlg, this.iterations, this.salt);
    }
}
