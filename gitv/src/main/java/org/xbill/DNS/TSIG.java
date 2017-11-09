package org.xbill.DNS;

import com.tvos.downloadmanager.data.DownloadRecordColumns;
import java.util.Date;
import org.xbill.DNS.utils.HMAC;
import org.xbill.DNS.utils.base64;

public class TSIG {
    public static final short FUDGE = (short) 300;
    public static final Name HMAC = HMAC_MD5;
    public static final Name HMAC_MD5 = Name.fromConstantString(HMAC_MD5_STR);
    private static final String HMAC_MD5_STR = "HMAC-MD5.SIG-ALG.REG.INT.";
    public static final Name HMAC_SHA1 = Name.fromConstantString(HMAC_SHA1_STR);
    private static final String HMAC_SHA1_STR = "hmac-sha1.";
    public static final Name HMAC_SHA224 = Name.fromConstantString(HMAC_SHA224_STR);
    private static final String HMAC_SHA224_STR = "hmac-sha224.";
    public static final Name HMAC_SHA256 = Name.fromConstantString(HMAC_SHA256_STR);
    private static final String HMAC_SHA256_STR = "hmac-sha256.";
    public static final Name HMAC_SHA384 = Name.fromConstantString(HMAC_SHA384_STR);
    private static final String HMAC_SHA384_STR = "hmac-sha384.";
    public static final Name HMAC_SHA512 = Name.fromConstantString(HMAC_SHA512_STR);
    private static final String HMAC_SHA512_STR = "hmac-sha512.";
    private Name alg;
    private String digest;
    private int digestBlockLength;
    private byte[] key;
    private Name name;

    public static class StreamVerifier {
        private TSIG key;
        private TSIGRecord lastTSIG;
        private int lastsigned;
        private int nresponses = 0;
        private HMAC verifier = new HMAC(TSIG.access$000(this.key), TSIG.access$100(this.key), TSIG.access$200(this.key));

        public StreamVerifier(TSIG tsig, TSIGRecord old) {
            this.key = tsig;
            this.lastTSIG = old;
        }

        public int verify(Message m, byte[] b) {
            TSIGRecord tsig = m.getTSIG();
            this.nresponses++;
            if (this.nresponses == 1) {
                int result = this.key.verify(m, b, this.lastTSIG);
                if (result == 0) {
                    byte[] signature = tsig.getSignature();
                    DNSOutput out = new DNSOutput();
                    out.writeU16(signature.length);
                    this.verifier.update(out.toByteArray());
                    this.verifier.update(signature);
                }
                this.lastTSIG = tsig;
                return result;
            }
            int len;
            if (tsig != null) {
                m.getHeader().decCount(3);
            }
            byte[] header = m.getHeader().toWire();
            if (tsig != null) {
                m.getHeader().incCount(3);
            }
            this.verifier.update(header);
            if (tsig == null) {
                len = b.length - header.length;
            } else {
                len = m.tsigstart - header.length;
            }
            this.verifier.update(b, header.length, len);
            if (tsig != null) {
                this.lastsigned = this.nresponses;
                this.lastTSIG = tsig;
                if (tsig.getName().equals(TSIG.access$300(this.key)) && tsig.getAlgorithm().equals(TSIG.access$400(this.key))) {
                    out = new DNSOutput();
                    long time = tsig.getTimeSigned().getTime() / 1000;
                    long timeLow = time & 4294967295L;
                    out.writeU16((int) (time >> 32));
                    out.writeU32(timeLow);
                    out.writeU16(tsig.getFudge());
                    this.verifier.update(out.toByteArray());
                    if (this.verifier.verify(tsig.getSignature())) {
                        this.verifier.clear();
                        out = new DNSOutput();
                        out.writeU16(tsig.getSignature().length);
                        this.verifier.update(out.toByteArray());
                        this.verifier.update(tsig.getSignature());
                        m.tsigState = 1;
                        return 0;
                    }
                    if (Options.check("verbose")) {
                        System.err.println("BADSIG failure");
                    }
                    m.tsigState = 4;
                    return 16;
                }
                if (Options.check("verbose")) {
                    System.err.println("BADKEY failure");
                }
                m.tsigState = 4;
                return 17;
            }
            if (this.nresponses - this.lastsigned >= 100) {
                m.tsigState = 4;
                return 1;
            }
            m.tsigState = 2;
            return 0;
        }
    }

    static String access$000(TSIG x0) {
        return x0.digest;
    }

    static int access$100(TSIG x0) {
        return x0.digestBlockLength;
    }

    static byte[] access$200(TSIG x0) {
        return x0.key;
    }

    static Name access$300(TSIG x0) {
        return x0.name;
    }

    static Name access$400(TSIG x0) {
        return x0.alg;
    }

    private void getDigest() {
        if (this.alg.equals(HMAC_MD5)) {
            this.digest = DownloadRecordColumns.COLUMN_MD5;
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA1)) {
            this.digest = "sha-1";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA224)) {
            this.digest = "sha-224";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA256)) {
            this.digest = "sha-256";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA512)) {
            this.digest = "sha-512";
            this.digestBlockLength = 128;
        } else if (this.alg.equals(HMAC_SHA384)) {
            this.digest = "sha-384";
            this.digestBlockLength = 128;
        } else {
            throw new IllegalArgumentException("Invalid algorithm");
        }
    }

    public TSIG(Name algorithm, Name name, byte[] key) {
        this.name = name;
        this.alg = algorithm;
        this.key = key;
        getDigest();
    }

    public TSIG(Name name, byte[] key) {
        this(HMAC_MD5, name, key);
    }

    public TSIG(Name algorithm, String name, String key) {
        this.key = base64.fromString(key);
        if (this.key == null) {
            throw new IllegalArgumentException("Invalid TSIG key string");
        }
        try {
            this.name = Name.fromString(name, Name.root);
            this.alg = algorithm;
            getDigest();
        } catch (TextParseException e) {
            throw new IllegalArgumentException("Invalid TSIG key name");
        }
    }

    public TSIG(String algorithm, String name, String key) {
        this(HMAC_MD5, name, key);
        if (algorithm.equalsIgnoreCase("hmac-md5")) {
            this.alg = HMAC_MD5;
        } else if (algorithm.equalsIgnoreCase("hmac-sha1")) {
            this.alg = HMAC_SHA1;
        } else if (algorithm.equalsIgnoreCase("hmac-sha224")) {
            this.alg = HMAC_SHA224;
        } else if (algorithm.equalsIgnoreCase("hmac-sha256")) {
            this.alg = HMAC_SHA256;
        } else if (algorithm.equalsIgnoreCase("hmac-sha384")) {
            this.alg = HMAC_SHA384;
        } else if (algorithm.equalsIgnoreCase("hmac-sha512")) {
            this.alg = HMAC_SHA512;
        } else {
            throw new IllegalArgumentException("Invalid TSIG algorithm");
        }
        getDigest();
    }

    public TSIG(String name, String key) {
        this(HMAC_MD5, name, key);
    }

    public static TSIG fromString(String str) {
        String[] parts = str.split("[:/]", 3);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid TSIG key specification");
        }
        if (parts.length == 3) {
            try {
                return new TSIG(parts[0], parts[1], parts[2]);
            } catch (IllegalArgumentException e) {
                parts = str.split("[:/]", 2);
            }
        }
        return new TSIG(HMAC_MD5, parts[0], parts[1]);
    }

    public TSIGRecord generate(Message m, byte[] b, int error, TSIGRecord old) {
        Date timeSigned;
        DNSOutput out;
        byte[] signature;
        if (error != 18) {
            timeSigned = new Date();
        } else {
            timeSigned = old.getTimeSigned();
        }
        HMAC hmac = null;
        if (error == 0 || error == 18) {
            hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        }
        int fudge = Options.intValue("tsigfudge");
        if (fudge < 0 || fudge > 32767) {
            fudge = 300;
        }
        if (old != null) {
            out = new DNSOutput();
            out.writeU16(old.getSignature().length);
            if (hmac != null) {
                hmac.update(out.toByteArray());
                hmac.update(old.getSignature());
            }
        }
        if (hmac != null) {
            hmac.update(b);
        }
        out = new DNSOutput();
        this.name.toWireCanonical(out);
        out.writeU16(255);
        out.writeU32(0);
        this.alg.toWireCanonical(out);
        long time = timeSigned.getTime() / 1000;
        long timeLow = time & 4294967295L;
        out.writeU16((int) (time >> 32));
        out.writeU32(timeLow);
        out.writeU16(fudge);
        out.writeU16(error);
        out.writeU16(0);
        if (hmac != null) {
            hmac.update(out.toByteArray());
        }
        if (hmac != null) {
            signature = hmac.sign();
        } else {
            signature = new byte[0];
        }
        byte[] other = null;
        if (error == 18) {
            out = new DNSOutput();
            time = new Date().getTime() / 1000;
            timeLow = time & 4294967295L;
            out.writeU16((int) (time >> 32));
            out.writeU32(timeLow);
            other = out.toByteArray();
        }
        return new TSIGRecord(this.name, 255, 0, this.alg, timeSigned, fudge, signature, m.getHeader().getID(), error, other);
    }

    public void apply(Message m, int error, TSIGRecord old) {
        m.addRecord(generate(m, m.toWire(), error, old), 3);
        m.tsigState = 3;
    }

    public void apply(Message m, TSIGRecord old) {
        apply(m, 0, old);
    }

    public void applyStream(Message m, TSIGRecord old, boolean first) {
        if (first) {
            apply(m, old);
            return;
        }
        Date timeSigned = new Date();
        HMAC hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        int fudge = Options.intValue("tsigfudge");
        if (fudge < 0 || fudge > 32767) {
            fudge = 300;
        }
        DNSOutput out = new DNSOutput();
        out.writeU16(old.getSignature().length);
        hmac.update(out.toByteArray());
        hmac.update(old.getSignature());
        hmac.update(m.toWire());
        out = new DNSOutput();
        long time = timeSigned.getTime() / 1000;
        long timeLow = time & 4294967295L;
        out.writeU16((int) (time >> 32));
        out.writeU32(timeLow);
        out.writeU16(fudge);
        hmac.update(out.toByteArray());
        Message message = m;
        message.addRecord(new TSIGRecord(this.name, 255, 0, this.alg, timeSigned, fudge, hmac.sign(), m.getHeader().getID(), 0, null), 3);
        m.tsigState = 3;
    }

    public byte verify(Message m, byte[] b, int length, TSIGRecord old) {
        m.tsigState = 4;
        TSIGRecord tsig = m.getTSIG();
        HMAC hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        if (tsig == null) {
            return (byte) 1;
        }
        if (tsig.getName().equals(this.name) && tsig.getAlgorithm().equals(this.alg)) {
            if (Math.abs(System.currentTimeMillis() - tsig.getTimeSigned().getTime()) > 1000 * ((long) tsig.getFudge())) {
                if (Options.check("verbose")) {
                    System.err.println("BADTIME failure");
                }
                return (byte) 18;
            }
            DNSOutput out;
            if (!(old == null || tsig.getError() == 17 || tsig.getError() == 16)) {
                out = new DNSOutput();
                out.writeU16(old.getSignature().length);
                hmac.update(out.toByteArray());
                hmac.update(old.getSignature());
            }
            m.getHeader().decCount(3);
            byte[] header = m.getHeader().toWire();
            m.getHeader().incCount(3);
            hmac.update(header);
            hmac.update(b, header.length, m.tsigstart - header.length);
            out = new DNSOutput();
            tsig.getName().toWireCanonical(out);
            out.writeU16(tsig.dclass);
            out.writeU32(tsig.ttl);
            tsig.getAlgorithm().toWireCanonical(out);
            long time = tsig.getTimeSigned().getTime() / 1000;
            long timeLow = time & 4294967295L;
            out.writeU16((int) (time >> 32));
            out.writeU32(timeLow);
            out.writeU16(tsig.getFudge());
            out.writeU16(tsig.getError());
            if (tsig.getOther() != null) {
                out.writeU16(tsig.getOther().length);
                out.writeByteArray(tsig.getOther());
            } else {
                out.writeU16(0);
            }
            hmac.update(out.toByteArray());
            byte[] signature = tsig.getSignature();
            int digestLength = hmac.digestLength();
            int minDigestLength = this.digest.equals(DownloadRecordColumns.COLUMN_MD5) ? 10 : digestLength / 2;
            if (signature.length > digestLength) {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature too long");
                }
                return (byte) 16;
            } else if (signature.length < minDigestLength) {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature too short");
                }
                return (byte) 16;
            } else if (hmac.verify(signature, true)) {
                m.tsigState = 1;
                return (byte) 0;
            } else {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature verification");
                }
                return (byte) 16;
            }
        }
        if (Options.check("verbose")) {
            System.err.println("BADKEY failure");
        }
        return (byte) 17;
    }

    public int verify(Message m, byte[] b, TSIGRecord old) {
        return verify(m, b, b.length, old);
    }

    public int recordLength() {
        return (((((this.name.length() + 10) + this.alg.length()) + 8) + 18) + 4) + 8;
    }
}
