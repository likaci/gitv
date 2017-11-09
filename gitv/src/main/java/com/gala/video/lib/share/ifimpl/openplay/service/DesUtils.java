package com.gala.video.lib.share.ifimpl.openplay.service;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
    private static final char[] CHAR_SET = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String DEFAULT_KEY = "com.qiyi.des.default.key";
    private static final int[] VALUE_SET = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private Cipher mDecryptCipher;
    private Cipher mEncryptCipher;

    public DesUtils() throws Exception {
        this(DEFAULT_KEY);
    }

    public DesUtils(String strKey) throws Exception {
        this.mEncryptCipher = null;
        this.mDecryptCipher = null;
        Key key = getKey(strKey.getBytes());
        this.mEncryptCipher = Cipher.getInstance("DES");
        this.mEncryptCipher.init(1, key);
        this.mDecryptCipher = Cipher.getInstance("DES");
        this.mDecryptCipher.init(2, key);
    }

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr2(strIn)));
    }

    private synchronized byte[] encrypt(byte[] arrB) throws Exception {
        return this.mEncryptCipher.doFinal(arrB);
    }

    private synchronized byte[] decrypt(byte[] arrB) throws Exception {
        return this.mDecryptCipher.doFinal(arrB);
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        int i = 0;
        while (i < arrBTmp.length && i < arrB.length) {
            arrB[i] = arrBTmp[i];
            i++;
        }
        return new SecretKeySpec(arrB, "DES");
    }

    private String byteArr2HexStr(byte[] arrB) throws Exception {
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int intTmp : arrB) {
            int intTmp2;
            while (intTmp2 < 0) {
                intTmp2 += 256;
            }
            if (intTmp2 < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp2, 16));
        }
        return sb.toString();
    }

    private static byte[] hexStr2ByteArr2(String strIn) {
        int length = strIn.length();
        byte[] arrOut = new byte[(length / 2)];
        for (int i = 0; i < length / 2; i++) {
            arrOut[i] = (byte) getValue(strIn.charAt(i * 2), strIn.charAt((i * 2) + 1));
        }
        return arrOut;
    }

    private static int getValue(char value1, char value2) {
        int ret1 = -1;
        int ret2 = -1;
        int i = 0;
        int size = CHAR_SET.length;
        while (i < size) {
            if (ret1 == -1 && CHAR_SET[i] == value1) {
                ret1 = VALUE_SET[i];
            }
            if (ret2 == -1 && CHAR_SET[i] == value2) {
                ret2 = VALUE_SET[i];
            }
            if (ret1 != -1 && ret2 != -1) {
                break;
            }
            i++;
        }
        return (ret1 * 16) + ret2;
    }
}
