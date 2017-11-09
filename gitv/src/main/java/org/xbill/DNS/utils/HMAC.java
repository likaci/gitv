package org.xbill.DNS.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMAC {
    private static final byte IPAD = (byte) 54;
    private static final byte OPAD = (byte) 92;
    private int blockLength;
    private MessageDigest digest;
    private byte[] ipad;
    private byte[] opad;

    private void init(byte[] key) {
        if (key.length > this.blockLength) {
            key = this.digest.digest(key);
            this.digest.reset();
        }
        this.ipad = new byte[this.blockLength];
        this.opad = new byte[this.blockLength];
        int i = 0;
        while (i < key.length) {
            this.ipad[i] = (byte) (key[i] ^ 54);
            this.opad[i] = (byte) (key[i] ^ 92);
            i++;
        }
        while (i < this.blockLength) {
            this.ipad[i] = (byte) 54;
            this.opad[i] = OPAD;
            i++;
        }
        this.digest.update(this.ipad);
    }

    public HMAC(MessageDigest digest, int blockLength, byte[] key) {
        digest.reset();
        this.digest = digest;
        this.blockLength = blockLength;
        init(key);
    }

    public HMAC(String digestName, int blockLength, byte[] key) {
        try {
            this.digest = MessageDigest.getInstance(digestName);
            this.blockLength = blockLength;
            init(key);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(new StringBuffer().append("unknown digest algorithm ").append(digestName).toString());
        }
    }

    public HMAC(MessageDigest digest, byte[] key) {
        this(digest, 64, key);
    }

    public HMAC(String digestName, byte[] key) {
        this(digestName, 64, key);
    }

    public void update(byte[] b, int offset, int length) {
        this.digest.update(b, offset, length);
    }

    public void update(byte[] b) {
        this.digest.update(b);
    }

    public byte[] sign() {
        byte[] output = this.digest.digest();
        this.digest.reset();
        this.digest.update(this.opad);
        return this.digest.digest(output);
    }

    public boolean verify(byte[] signature) {
        return verify(signature, false);
    }

    public boolean verify(byte[] signature, boolean truncation_ok) {
        byte[] expected = sign();
        if (truncation_ok && signature.length < expected.length) {
            byte[] truncated = new byte[signature.length];
            System.arraycopy(expected, 0, truncated, 0, truncated.length);
            expected = truncated;
        }
        return Arrays.equals(signature, expected);
    }

    public void clear() {
        this.digest.reset();
        this.digest.update(this.ipad);
    }

    public int digestLength() {
        return this.digest.getDigestLength();
    }
}
