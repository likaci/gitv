package com.push.mqttv3.internal.security;

import com.gala.multiscreen.dmr.model.MSMessage.RemoteCode;

public class SimpleBase64Encoder {
    private static final char[] PWDCHARS_ARRAY = PWDCHARS_STRING.toCharArray();
    private static final String PWDCHARS_STRING = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(byte[] bytes) {
        int len = bytes.length;
        StringBuffer encoded = new StringBuffer(((len + 2) / 3) * 4);
        int i = 0;
        int j = len;
        while (j >= 3) {
            encoded.append(to64((long) ((((bytes[i] & 255) << 16) | ((bytes[i + 1] & 255) << 8)) | (bytes[i + 2] & 255)), 4));
            i += 3;
            j -= 3;
        }
        if (j == 2) {
            encoded.append(to64((long) (((bytes[i] & 255) << 8) | (bytes[i + 1] & 255)), 3));
        }
        if (j == 1) {
            encoded.append(to64((long) (bytes[i] & 255), 2));
        }
        return encoded.toString();
    }

    public static byte[] decode(String string) {
        int l;
        byte[] encoded = string.getBytes();
        int len = encoded.length;
        byte[] decoded = new byte[((len * 3) / 4)];
        int i = 0;
        int j = len;
        int k = 0;
        while (j >= 4) {
            long d = from64(encoded, i, 4);
            j -= 4;
            i += 4;
            for (l = 2; l >= 0; l--) {
                decoded[k + l] = (byte) ((int) (255 & d));
                d >>= 8;
            }
            k += 3;
        }
        if (j == 3) {
            d = from64(encoded, i, 3);
            for (l = 1; l >= 0; l--) {
                decoded[k + l] = (byte) ((int) (255 & d));
                d >>= 8;
            }
        }
        if (j == 2) {
            decoded[k] = (byte) ((int) (255 & from64(encoded, i, 2)));
        }
        return decoded;
    }

    private static final String to64(long input, int size) {
        StringBuffer result = new StringBuffer(size);
        while (size > 0) {
            size--;
            result.append(PWDCHARS_ARRAY[(int) (63 & input)]);
            input >>= 6;
        }
        return result.toString();
    }

    private static final long from64(byte[] encoded, int idx, int size) {
        long res = 0;
        int f = 0;
        int idx2 = idx;
        while (size > 0) {
            size--;
            long r = 0;
            idx = idx2 + 1;
            byte d = encoded[idx2];
            if (d == (byte) 47) {
                r = 1;
            }
            if (d >= (byte) 48 && d <= RemoteCode.FLING_LEFT) {
                r = (long) ((d + 2) - 48);
            }
            if (d >= (byte) 65 && d <= (byte) 90) {
                r = (long) ((d + 12) - 65);
            }
            if (d >= (byte) 97 && d <= (byte) 122) {
                r = (long) ((d + 38) - 97);
            }
            res += r << f;
            f += 6;
            idx2 = idx;
        }
        return res;
    }
}
