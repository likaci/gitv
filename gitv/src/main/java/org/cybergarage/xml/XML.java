package org.cybergarage.xml;

import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class XML {
    public static final String CHARSET_UTF8 = "utf-8";
    public static final String CONTENT_TYPE = "text/xml; charset=\"utf-8\"";

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final java.lang.String escapeXMLChars(java.lang.String r7, boolean r8) {
        /*
        r6 = 0;
        if (r7 != 0) goto L_0x0005;
    L_0x0003:
        r7 = 0;
    L_0x0004:
        return r7;
    L_0x0005:
        r4 = new java.lang.StringBuffer;
        r4.<init>();
        r3 = r7.length();
        r2 = new char[r3];
        r7.getChars(r6, r3, r2, r6);
        r5 = 0;
        r0 = 0;
        r1 = 0;
    L_0x0016:
        if (r1 < r3) goto L_0x0024;
    L_0x0018:
        if (r5 == 0) goto L_0x0004;
    L_0x001a:
        r6 = r3 - r5;
        r4.append(r2, r5, r6);
        r7 = r4.toString();
        goto L_0x0004;
    L_0x0024:
        r6 = r2[r1];
        switch(r6) {
            case 34: goto L_0x004b;
            case 38: goto L_0x0039;
            case 39: goto L_0x0045;
            case 60: goto L_0x003d;
            case 62: goto L_0x0041;
            default: goto L_0x0029;
        };
    L_0x0029:
        if (r0 == 0) goto L_0x0036;
    L_0x002b:
        r6 = r1 - r5;
        r4.append(r2, r5, r6);
        r4.append(r0);
        r5 = r1 + 1;
        r0 = 0;
    L_0x0036:
        r1 = r1 + 1;
        goto L_0x0016;
    L_0x0039:
        r0 = "&amp;";
        goto L_0x0029;
    L_0x003d:
        r0 = "&lt;";
        goto L_0x0029;
    L_0x0041:
        r0 = "&gt;";
        goto L_0x0029;
    L_0x0045:
        if (r8 == 0) goto L_0x004b;
    L_0x0047:
        r0 = "&apos;";
        goto L_0x0029;
    L_0x004b:
        if (r8 == 0) goto L_0x0029;
    L_0x004d:
        r0 = "&quot;";
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cybergarage.xml.XML.escapeXMLChars(java.lang.String, boolean):java.lang.String");
    }

    public static final String escapeXMLChars(String input) {
        return escapeXMLChars(input, true);
    }

    public static final String unescapeXMLChars(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&amp;", "&").replace("&lt;", SearchCriteria.LT).replace("&gt;", SearchCriteria.GT).replace("&apos;", "'").replace("&quot;", "\"");
    }
}
