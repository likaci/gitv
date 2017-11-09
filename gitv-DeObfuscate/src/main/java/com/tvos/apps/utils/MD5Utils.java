package com.tvos.apps.utils;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Utils {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdInst.digest()) {
                int i = k + 1;
                str[k] = HEX_DIGITS[(byte0 >>> 4) & 15];
                k = i + 1;
                str[i] = HEX_DIGITS[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String MD5(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            byte[] tmp = md.digest();
            char[] str = new char[32];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                int i2 = k + 1;
                str[k] = HEX_DIGITS[(byte0 >>> 4) & 15];
                k = i2 + 1;
                str[i2] = HEX_DIGITS[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkMd5(String filename, String md5) {
        String file_md5 = MD5(filename);
        if (file_md5 == null || !file_md5.equalsIgnoreCase(md5)) {
            return false;
        }
        return true;
    }

    public static boolean verifyFileByMd5(String filePath, String md5) {
        if (TextUtils.isEmpty(md5) || !new File(filePath).exists()) {
            return false;
        }
        String updatefile_md5 = md5sum(filePath);
        if (TextUtils.isEmpty(updatefile_md5) || !updatefile_md5.equalsIgnoreCase(md5)) {
            return false;
        }
        return true;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String md5sum(String filename) {
        byte[] buffer = new byte[1024];
        try {
            InputStream fis = new FileInputStream(filename);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while (true) {
                int numRead = fis.read(buffer);
                if (numRead <= 0) {
                    fis.close();
                    return toHexString(md5.digest());
                }
                md5.update(buffer, 0, numRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
