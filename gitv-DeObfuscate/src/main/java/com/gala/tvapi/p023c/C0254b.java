package com.gala.tvapi.p023c;

public final class C0254b {
    private static String f905a = "0123456789ABCDEF";

    static {
        String[] strArr = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    }

    public static String m617a(byte[] bArr) {
        String str = "";
        for (int i = 0; i < bArr.length; i++) {
            str = str + (String.valueOf(f905a.charAt((bArr[i] & 240) >> 4)) + String.valueOf(f905a.charAt(bArr[i] & 15)));
        }
        return str;
    }

    public static byte[] m618a(String str) {
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) (((byte) (f905a.indexOf(toUpperCase.charAt(i * 2)) << 4)) | ((byte) f905a.indexOf(toUpperCase.charAt((i * 2) + 1))));
        }
        return bArr;
    }
}
