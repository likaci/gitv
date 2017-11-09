package com.gala.video.lib.framework.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MD5Util {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String md5sum(String filename) {
        Exception e;
        Throwable th;
        FileInputStream fileInputStream = null;
        byte[] buffer = new byte[1024];
        String md5Sum = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                while (true) {
                    int numRead1 = fis.read(buffer);
                    if (numRead1 <= 0) {
                        break;
                    }
                    md5.update(buffer, 0, numRead1);
                }
                md5Sum = toHexString(md5.digest());
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                        fileInputStream = fis;
                    }
                }
                fileInputStream = fis;
            } catch (Exception e2) {
                e = e2;
                fileInputStream = fis;
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException var142) {
                            var142.printStackTrace();
                        }
                    }
                    return md5Sum;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException var1422) {
                            var1422.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = fis;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return md5Sum;
        }
        return md5Sum;
    }

    public static boolean checkMd5(String filename, String md5) {
        String file_md5 = md5sum(filename);
        return file_md5 != null && file_md5.equalsIgnoreCase(md5);
    }

    public static String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
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
