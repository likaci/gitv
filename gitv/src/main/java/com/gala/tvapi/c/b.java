package com.gala.tvapi.c;

public final class b {
    private static String a = "0123456789ABCDEF";

    static {
        String[] strArr = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    }

    public static String a(byte[] bArr) {
        String str = "";
        for (int i = 0; i < bArr.length; i++) {
            str = str + (String.valueOf(a.charAt((bArr[i] & 240) >> 4)) + String.valueOf(a.charAt(bArr[i] & 15)));
        }
        return str;
    }

    public static byte[] a(String str) {
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) (((byte) (a.indexOf(toUpperCase.charAt(i * 2)) << 4)) | ((byte) a.indexOf(toUpperCase.charAt((i * 2) + 1))));
        }
        return bArr;
    }
}
