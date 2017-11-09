package org.xbill.DNS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class base16 {
    private static final String Base16 = "0123456789ABCDEF";

    private base16() {
    }

    public static String toString(byte[] b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (byte b2 : b) {
            short value = (short) (b2 & 255);
            byte low = (byte) (value & 15);
            os.write(Base16.charAt((byte) (value >> 4)));
            os.write(Base16.charAt(low));
        }
        return new String(os.toByteArray());
    }

    public static byte[] fromString(String str) {
        int i;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        byte[] raw = str.getBytes();
        for (i = 0; i < raw.length; i++) {
            if (!Character.isWhitespace((char) raw[i])) {
                bs.write(raw[i]);
            }
        }
        byte[] in = bs.toByteArray();
        if (in.length % 2 != 0) {
            return null;
        }
        bs.reset();
        DataOutputStream ds = new DataOutputStream(bs);
        for (i = 0; i < in.length; i += 2) {
            try {
                ds.writeByte((((byte) Base16.indexOf(Character.toUpperCase((char) in[i]))) << 4) + ((byte) Base16.indexOf(Character.toUpperCase((char) in[i + 1]))));
            } catch (IOException e) {
            }
        }
        return bs.toByteArray();
    }
}
