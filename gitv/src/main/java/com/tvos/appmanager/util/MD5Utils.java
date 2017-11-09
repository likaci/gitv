package com.tvos.appmanager.util;

import java.security.MessageDigest;

public class MD5Utils {
    private static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
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

    public static String MD5(byte[] b) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            byte[] tmp = md.digest();
            char[] str = new char[32];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                int i2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = i2 + 1;
                str[i2] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
