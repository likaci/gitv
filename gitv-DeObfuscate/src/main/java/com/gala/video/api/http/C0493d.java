package com.gala.video.api.http;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;

public final class C0493d extends C0489b {
    private static String f1890a = "";
    private static ThreadLocal<StringBuilder> f1891a = new C04911();
    private HostnameVerifier f1892a = new C04922();

    static class C04911 extends ThreadLocal<StringBuilder> {
        C04911() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m1526a();
        }

        private synchronized StringBuilder m1526a() {
            return new StringBuilder(1024);
        }
    }

    class C04922 implements HostnameVerifier {
        C04922() {
        }

        public final boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    }

    public C0493d(String str) {
        super(str);
    }

    public C0493d(String str, int i) {
        super(str, i);
    }

    public C0493d(String str, int i, boolean z) {
        super(str, i, z);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final java.lang.String mo1056a(java.lang.String r18, java.util.List<java.lang.String> r19, boolean r20, java.util.List<java.lang.Integer> r21) throws java.lang.Exception {
        /*
        r17 = this;
        r4 = 0;
        r3 = 0;
        r2 = 0;
        r5 = r4;
        r4 = r3;
        r3 = r2;
    L_0x0006:
        r10 = java.lang.System.currentTimeMillis();
        r7 = 0;
        r6 = 0;
        r2 = "https";
        r0 = r18;
        r2 = r0.startsWith(r2);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        if (r2 == 0) goto L_0x0040;
    L_0x0017:
        r2 = 1;
        r2 = new javax.net.ssl.TrustManager[r2];	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = 0;
        r9 = new com.gala.video.api.http.c;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r9.<init>();	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r2[r8] = r9;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = "SSL";
        r8 = javax.net.ssl.SSLContext.getInstance(r8);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r9 = 0;
        r12 = new java.security.SecureRandom;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r12.<init>();	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8.init(r9, r2, r12);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r2 = r8.getSocketFactory();	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(r2);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r0 = r17;
        r2 = r0.f1892a;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(r2);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
    L_0x0040:
        r2 = new java.net.URL;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r0 = r18;
        r2.<init>(r0);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r2 = r2.openConnection();	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r2 = (java.net.HttpURLConnection) r2;	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r2.setConnectTimeout(r8);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r2.setReadTimeout(r8);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = "Charset";
        r9 = "UTF-8";
        r2.setRequestProperty(r8, r9);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r8 = "Connection";
        r9 = "close";
        r2.setRequestProperty(r8, r9);	 Catch:{ Exception -> 0x01d4, all -> 0x01a4 }
        r0 = r19;
        r1 = r20;
        r7 = com.gala.video.api.http.C0493d.m1527a(r2, r0, r1);	 Catch:{ Exception -> 0x01da, all -> 0x01cc }
        r2 = r7.getResponseCode();	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 != r8) goto L_0x0100;
    L_0x0079:
        r2 = "200";
        f1890a = r2;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r6 = r7.getInputStream();	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        if (r6 == 0) goto L_0x00e4;
    L_0x0084:
        r12 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r2 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = "UTF-8";
        r2.<init>(r6, r8);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r12.<init>(r2);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r2 = f1891a;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r2 = r2.get();	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r2 = (java.lang.StringBuilder) r2;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = 0;
        r9 = r2.length();	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r2.delete(r8, r9);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r13 = new char[r8];	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = 0;
    L_0x00a7:
        r14 = r12.read(r13);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r15 = -1;
        if (r14 == r15) goto L_0x00c6;
    L_0x00ae:
        r8 = 0;
        r2.append(r13, r8, r14);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = r2.length();	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r8 = (long) r8;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r14 = 10485760; // 0xa00000 float:1.469368E-38 double:5.180654E-317;
        r14 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r14 <= 0) goto L_0x00a7;
    L_0x00be:
        r4 = new java.lang.Exception;	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r13 = "Connent length is too long";
        r4.<init>(r13);	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
    L_0x00c6:
        r14 = 10485760; // 0xa00000 float:1.469368E-38 double:5.180654E-317;
        r8 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r8 <= 0) goto L_0x00dd;
    L_0x00cd:
        if (r6 == 0) goto L_0x00d2;
    L_0x00cf:
        r6.close();	 Catch:{ IOException -> 0x00d8 }
    L_0x00d2:
        if (r7 == 0) goto L_0x00d7;
    L_0x00d4:
        r7.disconnect();
    L_0x00d7:
        throw r4;
    L_0x00d8:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x00d2;
    L_0x00dd:
        r5 = r2.toString();	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
        r12.close();	 Catch:{ Exception -> 0x01e4, all -> 0x01a4 }
    L_0x00e4:
        if (r6 == 0) goto L_0x00e9;
    L_0x00e6:
        r6.close();	 Catch:{ IOException -> 0x0199 }
    L_0x00e9:
        if (r7 == 0) goto L_0x00ee;
    L_0x00eb:
        r7.disconnect();
    L_0x00ee:
        if (r5 == 0) goto L_0x01b5;
    L_0x00f0:
        r2 = java.lang.System.currentTimeMillis();
        r2 = r2 - r10;
        r2 = (int) r2;
        r2 = java.lang.Integer.valueOf(r2);
        r0 = r21;
        r0.add(r2);
    L_0x00ff:
        return r5;
    L_0x0100:
        r4 = 204; // 0xcc float:2.86E-43 double:1.01E-321;
        if (r2 != r4) goto L_0x0112;
    L_0x0104:
        r2 = "204";
        f1890a = r2;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r5 = "";
        if (r7 == 0) goto L_0x00ff;
    L_0x010e:
        r7.disconnect();
        goto L_0x00ff;
    L_0x0112:
        r2 = java.lang.String.valueOf(r2);	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        f1890a = r2;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r4 = new java.lang.Exception;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r8 = "http error";
        r2.<init>(r8);	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r8 = f1890a;	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r2 = r2.append(r8);	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        r4.<init>(r2);	 Catch:{ Exception -> 0x0130, all -> 0x01a4 }
        goto L_0x00e4;
    L_0x0130:
        r2 = move-exception;
        r4 = r6;
        r6 = r5;
        r5 = r7;
    L_0x0134:
        r7 = "-50";
        f1890a = r7;	 Catch:{ all -> 0x01d0 }
        r2.printStackTrace();	 Catch:{ all -> 0x01d0 }
        r7 = "id=-1";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01d0 }
        r9 = "Error: ";
        r8.<init>(r9);	 Catch:{ all -> 0x01d0 }
        r0 = r18;
        r8 = r8.append(r0);	 Catch:{ all -> 0x01d0 }
        r8 = r8.toString();	 Catch:{ all -> 0x01d0 }
        com.gala.video.api.log.ApiEngineLog.m1531e(r7, r8);	 Catch:{ all -> 0x01d0 }
        r7 = "id=-1";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01d0 }
        r9 = "tried ";
        r8.<init>(r9);	 Catch:{ all -> 0x01d0 }
        r8 = r8.append(r3);	 Catch:{ all -> 0x01d0 }
        r9 = " times.";
        r8 = r8.append(r9);	 Catch:{ all -> 0x01d0 }
        r8 = r8.toString();	 Catch:{ all -> 0x01d0 }
        com.gala.video.api.log.ApiEngineLog.m1531e(r7, r8);	 Catch:{ all -> 0x01d0 }
        r7 = "id=-1";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01d0 }
        r9 = "Exception: ";
        r8.<init>(r9);	 Catch:{ all -> 0x01d0 }
        r9 = r2.getMessage();	 Catch:{ all -> 0x01d0 }
        r8 = r8.append(r9);	 Catch:{ all -> 0x01d0 }
        r8 = r8.toString();	 Catch:{ all -> 0x01d0 }
        com.gala.video.api.log.ApiEngineLog.m1531e(r7, r8);	 Catch:{ all -> 0x01d0 }
        if (r4 == 0) goto L_0x0190;
    L_0x018d:
        r4.close();	 Catch:{ IOException -> 0x019f }
    L_0x0190:
        if (r5 == 0) goto L_0x01ea;
    L_0x0192:
        r5.disconnect();
        r4 = r2;
        r5 = r6;
        goto L_0x00ee;
    L_0x0199:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x00e9;
    L_0x019f:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0190;
    L_0x01a4:
        r2 = move-exception;
    L_0x01a5:
        if (r6 == 0) goto L_0x01aa;
    L_0x01a7:
        r6.close();	 Catch:{ IOException -> 0x01b0 }
    L_0x01aa:
        if (r7 == 0) goto L_0x01af;
    L_0x01ac:
        r7.disconnect();
    L_0x01af:
        throw r2;
    L_0x01b0:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x01aa;
    L_0x01b5:
        r6 = java.lang.System.currentTimeMillis();
        r6 = r6 - r10;
        r2 = (int) r6;
        r2 = java.lang.Integer.valueOf(r2);
        r0 = r21;
        r0.add(r2);
        r2 = r3 + 1;
        r3 = 2;
        if (r2 >= r3) goto L_0x00d7;
    L_0x01c9:
        r3 = r2;
        goto L_0x0006;
    L_0x01cc:
        r3 = move-exception;
        r7 = r2;
        r2 = r3;
        goto L_0x01a5;
    L_0x01d0:
        r2 = move-exception;
        r6 = r4;
        r7 = r5;
        goto L_0x01a5;
    L_0x01d4:
        r2 = move-exception;
        r4 = r6;
        r6 = r5;
        r5 = r7;
        goto L_0x0134;
    L_0x01da:
        r4 = move-exception;
        r16 = r4;
        r4 = r6;
        r6 = r5;
        r5 = r2;
        r2 = r16;
        goto L_0x0134;
    L_0x01e4:
        r2 = move-exception;
        r4 = r6;
        r6 = r5;
        r5 = r7;
        goto L_0x0134;
    L_0x01ea:
        r4 = r2;
        r5 = r6;
        goto L_0x00ee;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.api.http.d.a(java.lang.String, java.util.List, boolean, java.util.List):java.lang.String");
    }

    private static HttpURLConnection m1527a(HttpURLConnection httpURLConnection, List<String> list, boolean z) throws ProtocolException {
        if (httpURLConnection != null) {
            if (z) {
                httpURLConnection.setRequestMethod(HTTP.POST);
            } else {
                httpURLConnection.setRequestMethod(HTTP.GET);
            }
            if (list != null && list.size() > 0) {
                for (String split : list) {
                    String[] split2 = split.split(SOAP.DELIM);
                    if (split2 != null && split2.length >= 2) {
                        httpURLConnection.setRequestProperty(split2[0], split2[1]);
                    }
                }
            }
        }
        return httpURLConnection;
    }

    protected final String mo1055a() {
        return f1890a;
    }
}
