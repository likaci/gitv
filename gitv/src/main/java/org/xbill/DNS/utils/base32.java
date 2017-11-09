package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class base32 {
    private String alphabet;
    private boolean lowercase;
    private boolean padding;

    public static class Alphabet {
        public static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";
        public static final String BASE32HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUV=";

        private Alphabet() {
        }
    }

    public base32(String alphabet, boolean padding, boolean lowercase) {
        this.alphabet = alphabet;
        this.padding = padding;
        this.lowercase = lowercase;
    }

    private static int blockLenToPadding(int blocklen) {
        switch (blocklen) {
            case 1:
                return 6;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 1;
            case 5:
                return 0;
            default:
                return -1;
        }
    }

    private static int paddingToBlockLen(int padlen) {
        switch (padlen) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 6:
                return 1;
            default:
                return -1;
        }
    }

    public String toString(byte[] b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int i = 0; i < (b.length + 4) / 5; i++) {
            int j;
            short[] s = new short[5];
            int[] t = new int[8];
            int blocklen = 5;
            for (j = 0; j < 5; j++) {
                if ((i * 5) + j < b.length) {
                    s[j] = (short) (b[(i * 5) + j] & 255);
                } else {
                    s[j] = (short) 0;
                    blocklen--;
                }
            }
            int padlen = blockLenToPadding(blocklen);
            t[0] = (byte) ((s[0] >> 3) & 31);
            t[1] = (byte) (((s[0] & 7) << 2) | ((s[1] >> 6) & 3));
            t[2] = (byte) ((s[1] >> 1) & 31);
            t[3] = (byte) (((s[1] & 1) << 4) | ((s[2] >> 4) & 15));
            t[4] = (byte) (((s[2] & 15) << 1) | ((s[3] >> 7) & 1));
            t[5] = (byte) ((s[3] >> 2) & 31);
            t[6] = (byte) (((s[3] & 3) << 3) | ((s[4] >> 5) & 7));
            t[7] = (byte) (s[4] & 31);
            for (j = 0; j < t.length - padlen; j++) {
                char c = this.alphabet.charAt(t[j]);
                if (this.lowercase) {
                    c = Character.toLowerCase(c);
                }
                os.write(c);
            }
            if (this.padding) {
                for (j = t.length - padlen; j < t.length; j++) {
                    os.write(61);
                }
            }
        }
        return new String(os.toByteArray());
    }

    public byte[] fromString(String str) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte[] raw = str.getBytes();
        for (byte b : raw) {
            char c = (char) b;
            if (!Character.isWhitespace(c)) {
                bs.write((byte) Character.toUpperCase(c));
            }
        }
        if (!this.padding) {
            while (bs.size() % 8 != 0) {
                bs.write(61);
            }
        } else if (bs.size() % 8 != 0) {
            return null;
        }
        byte[] in = bs.toByteArray();
        bs.reset();
        DataOutputStream ds = new DataOutputStream(bs);
        int i = 0;
        while (i < in.length / 8) {
            short[] s = new short[8];
            int[] t = new int[5];
            int padlen = 8;
            int j = 0;
            while (j < 8 && ((char) in[(i * 8) + j]) != '=') {
                s[j] = (short) this.alphabet.indexOf(in[(i * 8) + j]);
                if (s[j] < (short) 0) {
                    return null;
                }
                padlen--;
                j++;
            }
            int blocklen = paddingToBlockLen(padlen);
            if (blocklen < 0) {
                return null;
            }
            t[0] = (s[0] << 3) | (s[1] >> 2);
            t[1] = (((s[1] & 3) << 6) | (s[2] << 1)) | (s[3] >> 4);
            t[2] = ((s[3] & 15) << 4) | ((s[4] >> 1) & 15);
            t[3] = ((s[4] << 7) | (s[5] << 2)) | (s[6] >> 3);
            t[4] = ((s[6] & 7) << 5) | s[7];
            j = 0;
            while (j < blocklen) {
                try {
                    ds.writeByte((byte) (t[j] & 255));
                    j++;
                } catch (IOException e) {
                }
            }
            i++;
        }
        return bs.toByteArray();
    }
}
