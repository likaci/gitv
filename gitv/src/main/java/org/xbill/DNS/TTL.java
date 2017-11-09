package org.xbill.DNS;

public final class TTL {
    public static final long MAX_VALUE = 2147483647L;

    private TTL() {
    }

    static void check(long i) {
        if (i < 0 || i > MAX_VALUE) {
            throw new InvalidTTLException(i);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long parse(java.lang.String r12, boolean r13) {
        /*
        if (r12 == 0) goto L_0x0013;
    L_0x0002:
        r8 = r12.length();
        if (r8 == 0) goto L_0x0013;
    L_0x0008:
        r8 = 0;
        r8 = r12.charAt(r8);
        r8 = java.lang.Character.isDigit(r8);
        if (r8 != 0) goto L_0x0019;
    L_0x0013:
        r8 = new java.lang.NumberFormatException;
        r8.<init>();
        throw r8;
    L_0x0019:
        r6 = 0;
        r4 = 0;
        r1 = 0;
    L_0x001e:
        r8 = r12.length();
        if (r1 >= r8) goto L_0x0071;
    L_0x0024:
        r0 = r12.charAt(r1);
        r2 = r6;
        r8 = java.lang.Character.isDigit(r0);
        if (r8 == 0) goto L_0x0043;
    L_0x002f:
        r8 = 10;
        r8 = r8 * r6;
        r10 = java.lang.Character.getNumericValue(r0);
        r10 = (long) r10;
        r6 = r8 + r10;
        r8 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r8 >= 0) goto L_0x006e;
    L_0x003d:
        r8 = new java.lang.NumberFormatException;
        r8.<init>();
        throw r8;
    L_0x0043:
        r8 = java.lang.Character.toUpperCase(r0);
        switch(r8) {
            case 68: goto L_0x0053;
            case 72: goto L_0x0056;
            case 77: goto L_0x0059;
            case 83: goto L_0x005c;
            case 87: goto L_0x0050;
            default: goto L_0x004a;
        };
    L_0x004a:
        r8 = new java.lang.NumberFormatException;
        r8.<init>();
        throw r8;
    L_0x0050:
        r8 = 7;
        r6 = r6 * r8;
    L_0x0053:
        r8 = 24;
        r6 = r6 * r8;
    L_0x0056:
        r8 = 60;
        r6 = r6 * r8;
    L_0x0059:
        r8 = 60;
        r6 = r6 * r8;
    L_0x005c:
        r4 = r4 + r6;
        r6 = 0;
        r8 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 <= 0) goto L_0x006e;
    L_0x0068:
        r8 = new java.lang.NumberFormatException;
        r8.<init>();
        throw r8;
    L_0x006e:
        r1 = r1 + 1;
        goto L_0x001e;
    L_0x0071:
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0078;
    L_0x0077:
        r4 = r6;
    L_0x0078:
        r8 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 <= 0) goto L_0x0087;
    L_0x0081:
        r8 = new java.lang.NumberFormatException;
        r8.<init>();
        throw r8;
    L_0x0087:
        r8 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 <= 0) goto L_0x0093;
    L_0x008e:
        if (r13 == 0) goto L_0x0093;
    L_0x0090:
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x0093:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.TTL.parse(java.lang.String, boolean):long");
    }

    public static long parseTTL(String s) {
        return parse(s, true);
    }

    public static String format(long ttl) {
        check(ttl);
        StringBuffer sb = new StringBuffer();
        long secs = ttl % 60;
        ttl /= 60;
        long mins = ttl % 60;
        ttl /= 60;
        long hours = ttl % 24;
        ttl /= 24;
        long days = ttl % 7;
        long weeks = ttl / 7;
        if (weeks > 0) {
            sb.append(new StringBuffer().append(weeks).append("W").toString());
        }
        if (days > 0) {
            sb.append(new StringBuffer().append(days).append("D").toString());
        }
        if (hours > 0) {
            sb.append(new StringBuffer().append(hours).append("H").toString());
        }
        if (mins > 0) {
            sb.append(new StringBuffer().append(mins).append("M").toString());
        }
        if (secs > 0 || (weeks == 0 && days == 0 && hours == 0 && mins == 0)) {
            sb.append(new StringBuffer().append(secs).append("S").toString());
        }
        return sb.toString();
    }
}
