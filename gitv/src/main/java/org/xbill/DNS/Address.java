package org.xbill.DNS;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.cybergarage.soap.SOAP;

public final class Address {
    public static final int IPv4 = 1;
    public static final int IPv6 = 2;

    private Address() {
    }

    private static byte[] parseV4(String s) {
        byte[] values = new byte[4];
        int length = s.length();
        int currentValue = 0;
        int numDigits = 0;
        int i = 0;
        int currentOctet = 0;
        while (i < length) {
            int currentOctet2;
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                if (c != '.') {
                    return null;
                }
                if (currentOctet == 3) {
                    return null;
                }
                if (numDigits == 0) {
                    return null;
                }
                currentOctet2 = currentOctet + 1;
                values[currentOctet] = (byte) currentValue;
                currentValue = 0;
                numDigits = 0;
            } else if (numDigits == 3) {
                return null;
            } else {
                if (numDigits > 0 && currentValue == 0) {
                    return null;
                }
                numDigits++;
                currentValue = (currentValue * 10) + (c - 48);
                if (currentValue > 255) {
                    return null;
                }
                currentOctet2 = currentOctet;
            }
            i++;
            currentOctet = currentOctet2;
        }
        if (currentOctet != 3) {
            return null;
        }
        if (numDigits == 0) {
            return null;
        }
        values[currentOctet] = (byte) currentValue;
        return values;
    }

    private static byte[] parseV6(String s) {
        int range = -1;
        byte[] data = new byte[16];
        String[] tokens = s.split(SOAP.DELIM, -1);
        int first = 0;
        int last = tokens.length - 1;
        if (tokens[0].length() == 0) {
            if (last - 0 <= 0 || tokens[1].length() != 0) {
                return null;
            }
            first = 0 + 1;
        }
        if (tokens[last].length() == 0) {
            if (last - first <= 0 || tokens[last - 1].length() != 0) {
                return null;
            }
            last--;
        }
        if ((last - first) + 1 > 8) {
            return null;
        }
        int j;
        int empty;
        int i = first;
        int j2 = 0;
        while (i <= last) {
            if (tokens[i].length() == 0) {
                if (range >= 0) {
                    return null;
                }
                range = j2;
                j = j2;
            } else if (tokens[i].indexOf(46) < 0) {
                for (k = 0; k < tokens[i].length(); k++) {
                    if (Character.digit(tokens[i].charAt(k), 16) < 0) {
                        return null;
                    }
                }
                int x = Integer.parseInt(tokens[i], 16);
                if (x > 65535 || x < 0) {
                    return null;
                }
                j = j2 + 1;
                try {
                    data[j2] = (byte) (x >>> 8);
                    j2 = j + 1;
                } catch (NumberFormatException e) {
                }
                try {
                    data[j] = (byte) (x & 255);
                    j = j2;
                } catch (NumberFormatException e2) {
                    j = j2;
                }
            } else if (i < last) {
                return null;
            } else {
                if (i > 6) {
                    return null;
                }
                byte[] v4addr = toByteArray(tokens[i], 1);
                if (v4addr == null) {
                    return null;
                }
                k = 0;
                while (k < 4) {
                    j = j2 + 1;
                    data[j2] = v4addr[k];
                    k++;
                    j2 = j;
                }
                j = j2;
                if (j >= 16 && range < 0) {
                    return null;
                }
                if (range >= 0) {
                    return data;
                }
                empty = 16 - j;
                System.arraycopy(data, range, data, range + empty, j - range);
                for (i = range; i < range + empty; i++) {
                    data[i] = (byte) 0;
                }
                return data;
            }
            i++;
            j2 = j;
        }
        j = j2;
        if (j >= 16) {
        }
        if (range >= 0) {
            return data;
        }
        empty = 16 - j;
        System.arraycopy(data, range, data, range + empty, j - range);
        for (i = range; i < range + empty; i++) {
            data[i] = (byte) 0;
        }
        return data;
        return null;
    }

    public static int[] toArray(String s, int family) {
        byte[] byteArray = toByteArray(s, family);
        if (byteArray == null) {
            return null;
        }
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i] & 255;
        }
        return intArray;
    }

    public static int[] toArray(String s) {
        return toArray(s, 1);
    }

    public static byte[] toByteArray(String s, int family) {
        if (family == 1) {
            return parseV4(s);
        }
        if (family == 2) {
            return parseV6(s);
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static boolean isDottedQuad(String s) {
        if (toByteArray(s, 1) != null) {
            return true;
        }
        return false;
    }

    public static String toDottedQuad(byte[] addr) {
        return new StringBuffer().append(addr[0] & 255).append(".").append(addr[1] & 255).append(".").append(addr[2] & 255).append(".").append(addr[3] & 255).toString();
    }

    public static String toDottedQuad(int[] addr) {
        return new StringBuffer().append(addr[0]).append(".").append(addr[1]).append(".").append(addr[2]).append(".").append(addr[3]).toString();
    }

    private static Record[] lookupHostName(String name, boolean all) throws UnknownHostException {
        try {
            Lookup lookup = new Lookup(name, 1);
            Record[] a = lookup.run();
            Record[] aaaa;
            if (a == null) {
                if (lookup.getResult() == 4) {
                    aaaa = new Lookup(name, 28).run();
                    if (aaaa != null) {
                        return aaaa;
                    }
                }
                throw new UnknownHostException("unknown host");
            } else if (!all) {
                return a;
            } else {
                aaaa = new Lookup(name, 28).run();
                if (aaaa == null) {
                    return a;
                }
                Record[] merged = new Record[(a.length + aaaa.length)];
                System.arraycopy(a, 0, merged, 0, a.length);
                System.arraycopy(aaaa, 0, merged, a.length, aaaa.length);
                return merged;
            }
        } catch (TextParseException e) {
            throw new UnknownHostException("invalid name");
        }
    }

    private static InetAddress addrFromRecord(String name, Record r) throws UnknownHostException {
        InetAddress addr;
        if (r instanceof ARecord) {
            addr = ((ARecord) r).getAddress();
        } else {
            addr = ((AAAARecord) r).getAddress();
        }
        return InetAddress.getByAddress(name, addr.getAddress());
    }

    public static InetAddress getByName(String name) throws UnknownHostException {
        int i = 0;
        try {
            return getByAddress(name);
        } catch (UnknownHostException e) {
            return addrFromRecord(name, lookupHostName(name, i)[i]);
        }
    }

    public static InetAddress[] getAllByName(String name) throws UnknownHostException {
        try {
            return new InetAddress[]{getByAddress(name)};
        } catch (UnknownHostException e) {
            Record[] records = lookupHostName(name, true);
            InetAddress[] addrs = new InetAddress[records.length];
            for (int i = 0; i < records.length; i++) {
                addrs[i] = addrFromRecord(name, records[i]);
            }
            return addrs;
        }
    }

    public static InetAddress getByAddress(String addr) throws UnknownHostException {
        byte[] bytes = toByteArray(addr, 1);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        bytes = toByteArray(addr, 2);
        if (bytes != null) {
            return InetAddress.getByAddress(addr, bytes);
        }
        throw new UnknownHostException(new StringBuffer().append("Invalid address: ").append(addr).toString());
    }

    public static InetAddress getByAddress(String addr, int family) throws UnknownHostException {
        if (family == 1 || family == 2) {
            byte[] bytes = toByteArray(addr, family);
            if (bytes != null) {
                return InetAddress.getByAddress(addr, bytes);
            }
            throw new UnknownHostException(new StringBuffer().append("Invalid address: ").append(addr).toString());
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static String getHostName(InetAddress addr) throws UnknownHostException {
        Record[] records = new Lookup(ReverseMap.fromAddress(addr), 12).run();
        if (records != null) {
            return records[0].getTarget().toString();
        }
        throw new UnknownHostException("unknown address");
    }

    public static int familyOf(InetAddress address) {
        if (address instanceof Inet4Address) {
            return 1;
        }
        if (address instanceof Inet6Address) {
            return 2;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static int addressLength(int family) {
        if (family == 1) {
            return 4;
        }
        if (family == 2) {
            return 16;
        }
        throw new IllegalArgumentException("unknown address family");
    }

    public static InetAddress truncate(InetAddress address, int maskLength) {
        int maxMaskLength = addressLength(familyOf(address)) * 8;
        if (maskLength < 0 || maskLength > maxMaskLength) {
            throw new IllegalArgumentException("invalid mask length");
        }
        if (maskLength != maxMaskLength) {
            int i;
            byte[] bytes = address.getAddress();
            for (i = (maskLength / 8) + 1; i < bytes.length; i++) {
                bytes[i] = (byte) 0;
            }
            int bitmask = 0;
            for (i = 0; i < maskLength % 8; i++) {
                bitmask |= 1 << (7 - i);
            }
            int i2 = maskLength / 8;
            bytes[i2] = (byte) (bytes[i2] & bitmask);
            try {
                address = InetAddress.getByAddress(bytes);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("invalid address");
            }
        }
        return address;
    }
}
