package com.alibaba.fastjson.parser.deserializer;

public class StackTraceElementDeserializer implements ObjectDeserializer {
    public static final StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r11, java.lang.reflect.Type r12, java.lang.Object r13) {
        /*
        r10 = this;
        r4 = r11.lexer;
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x000f;
    L_0x000a:
        r4.nextToken();
        r7 = 0;
    L_0x000e:
        return r7;
    L_0x000f:
        r7 = r4.token();
        r8 = 12;
        if (r7 == r8) goto L_0x0041;
    L_0x0017:
        r7 = r4.token();
        r8 = 16;
        if (r7 == r8) goto L_0x0041;
    L_0x001f:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error: ";
        r8 = r8.append(r9);
        r9 = r4.token();
        r9 = com.alibaba.fastjson.parser.JSONToken.name(r9);
        r8 = r8.append(r9);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0041:
        r0 = 0;
        r6 = 0;
        r2 = 0;
        r5 = 0;
    L_0x0045:
        r7 = r11.getSymbolTable();
        r3 = r4.scanSymbol(r7);
        if (r3 != 0) goto L_0x0072;
    L_0x004f:
        r7 = r4.token();
        r8 = 13;
        if (r7 != r8) goto L_0x0062;
    L_0x0057:
        r7 = 16;
        r4.nextToken(r7);
    L_0x005c:
        r7 = new java.lang.StackTraceElement;
        r7.<init>(r0, r6, r2, r5);
        goto L_0x000e;
    L_0x0062:
        r7 = r4.token();
        r8 = 16;
        if (r7 != r8) goto L_0x0072;
    L_0x006a:
        r7 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;
        r7 = r4.isEnabled(r7);
        if (r7 != 0) goto L_0x0045;
    L_0x0072:
        r7 = 4;
        r4.nextTokenWithColon(r7);
        r7 = "className";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00ab;
    L_0x007f:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x0096;
    L_0x0087:
        r0 = 0;
    L_0x0088:
        r7 = r4.token();
        r8 = 13;
        if (r7 != r8) goto L_0x0045;
    L_0x0090:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x005c;
    L_0x0096:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00a2;
    L_0x009d:
        r0 = r4.stringVal();
        goto L_0x0088;
    L_0x00a2:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00ab:
        r7 = "methodName";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00d3;
    L_0x00b4:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x00be;
    L_0x00bc:
        r6 = 0;
        goto L_0x0088;
    L_0x00be:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00ca;
    L_0x00c5:
        r6 = r4.stringVal();
        goto L_0x0088;
    L_0x00ca:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00d3:
        r7 = "fileName";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00fb;
    L_0x00dc:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x00e6;
    L_0x00e4:
        r2 = 0;
        goto L_0x0088;
    L_0x00e6:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00f2;
    L_0x00ed:
        r2 = r4.stringVal();
        goto L_0x0088;
    L_0x00f2:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00fb:
        r7 = "lineNumber";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x0125;
    L_0x0104:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x010f;
    L_0x010c:
        r5 = 0;
        goto L_0x0088;
    L_0x010f:
        r7 = r4.token();
        r8 = 2;
        if (r7 != r8) goto L_0x011c;
    L_0x0116:
        r5 = r4.intValue();
        goto L_0x0088;
    L_0x011c:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x0125:
        r7 = "nativeMethod";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x0162;
    L_0x012e:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x013d;
    L_0x0136:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x0088;
    L_0x013d:
        r7 = r4.token();
        r8 = 6;
        if (r7 != r8) goto L_0x014b;
    L_0x0144:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x0088;
    L_0x014b:
        r7 = r4.token();
        r8 = 7;
        if (r7 != r8) goto L_0x0159;
    L_0x0152:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x0088;
    L_0x0159:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x0162:
        r7 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;
        if (r3 != r7) goto L_0x01a5;
    L_0x0166:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x0194;
    L_0x016d:
        r1 = r4.stringVal();
        r7 = "java.lang.StackTraceElement";
        r7 = r1.equals(r7);
        if (r7 != 0) goto L_0x0088;
    L_0x017a:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error : ";
        r8 = r8.append(r9);
        r8 = r8.append(r1);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0194:
        r7 = r4.token();
        r8 = 8;
        if (r7 == r8) goto L_0x0088;
    L_0x019c:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x01a5:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error : ";
        r8 = r8.append(r9);
        r8 = r8.append(r3);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):T");
    }

    public int getFastMatchToken() {
        return 12;
    }
}
