package org.xbill.DNS.utils;

import com.mcto.ads.internal.net.TrackingConstants;

public class hexdump {
    private static final char[] hex = "0123456789ABCDEF".toCharArray();

    public static String dump(String description, byte[] b, int offset, int length) {
        StringBuffer sb = new StringBuffer();
        sb.append(new StringBuffer().append(length).append(TrackingConstants.TRACKING_KEY_TIMESTAMP).toString());
        if (description != null) {
            sb.append(new StringBuffer().append(" (").append(description).append(")").toString());
        }
        sb.append(':');
        int prefixlen = (sb.toString().length() + 8) & -8;
        sb.append('\t');
        int perline = (80 - prefixlen) / 3;
        int i = 0;
        while (i < length) {
            if (i != 0 && i % perline == 0) {
                sb.append('\n');
                for (int j = 0; j < prefixlen / 8; j++) {
                    sb.append('\t');
                }
            }
            int value = b[i + offset] & 255;
            sb.append(hex[value >> 4]);
            sb.append(hex[value & 15]);
            sb.append(' ');
            i++;
        }
        sb.append('\n');
        return sb.toString();
    }

    public static String dump(String s, byte[] b) {
        return dump(s, b, 0, b.length);
    }
}
