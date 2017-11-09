package org.cybergarage.util;

import java.security.MessageDigest;

public class MD5Util {
    public static final String getMd5(byte[] bytes, int count) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(bytes, 0, count);
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdInst.digest()) {
                int i = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = i + 1;
                str[i] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final boolean isSameMd5(String md51, String md52) {
        if (md51 == null && md52 == null) {
            return true;
        }
        if (md51 == null || md52 == null) {
            return false;
        }
        if (md51.toLowerCase().compareTo(md52.toLowerCase()) != 0) {
            return false;
        }
        return true;
    }
}
