package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ReverseMap {
    private static Name inaddr4 = Name.fromConstantString("in-addr.arpa.");
    private static Name inaddr6 = Name.fromConstantString("ip6.arpa.");

    private ReverseMap() {
    }

    public static Name fromAddress(byte[] addr) {
        if (addr.length == 4 || addr.length == 16) {
            StringBuffer sb = new StringBuffer();
            int i;
            if (addr.length == 4) {
                for (i = addr.length - 1; i >= 0; i--) {
                    sb.append(addr[i] & 255);
                    if (i > 0) {
                        sb.append(".");
                    }
                }
            } else {
                int[] nibbles = new int[2];
                for (i = addr.length - 1; i >= 0; i--) {
                    nibbles[0] = (addr[i] & 255) >> 4;
                    nibbles[1] = (addr[i] & 255) & 15;
                    int j = nibbles.length - 1;
                    while (j >= 0) {
                        sb.append(Integer.toHexString(nibbles[j]));
                        if (i > 0 || j > 0) {
                            sb.append(".");
                        }
                        j--;
                    }
                }
            }
            try {
                if (addr.length == 4) {
                    return Name.fromString(sb.toString(), inaddr4);
                }
                return Name.fromString(sb.toString(), inaddr6);
            } catch (TextParseException e) {
                throw new IllegalStateException("name cannot be invalid");
            }
        }
        throw new IllegalArgumentException("array must contain 4 or 16 elements");
    }

    public static Name fromAddress(int[] addr) {
        byte[] bytes = new byte[addr.length];
        int i = 0;
        while (i < addr.length) {
            if (addr[i] < 0 || addr[i] > 255) {
                throw new IllegalArgumentException("array must contain values between 0 and 255");
            }
            bytes[i] = (byte) addr[i];
            i++;
        }
        return fromAddress(bytes);
    }

    public static Name fromAddress(InetAddress addr) {
        return fromAddress(addr.getAddress());
    }

    public static Name fromAddress(String addr, int family) throws UnknownHostException {
        byte[] array = Address.toByteArray(addr, family);
        if (array != null) {
            return fromAddress(array);
        }
        throw new UnknownHostException("Invalid IP address");
    }

    public static Name fromAddress(String addr) throws UnknownHostException {
        byte[] array = Address.toByteArray(addr, 1);
        if (array == null) {
            array = Address.toByteArray(addr, 2);
        }
        if (array != null) {
            return fromAddress(array);
        }
        throw new UnknownHostException("Invalid IP address");
    }
}
