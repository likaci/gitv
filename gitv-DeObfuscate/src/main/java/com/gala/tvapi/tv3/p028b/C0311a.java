package com.gala.tvapi.tv3.p028b;

import android.util.Log;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class C0311a {
    private static ThreadLocal<StringBuilder> f1096a = new C03081();
    private String f1097a;
    private List<String> f1098a = new ArrayList(1);
    private HostnameVerifier f1099a = new C03092();
    private String f1100b;

    static class C03081 extends ThreadLocal<StringBuilder> {
        C03081() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m723a();
        }

        private synchronized StringBuilder m723a() {
            return new StringBuilder(1024);
        }
    }

    class C03092 implements HostnameVerifier {
        C03092() {
        }

        public final boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    }

    class C0310a implements X509TrustManager {
        private X509TrustManager f1095a;

        public C0310a() {
            try {
                KeyStore instance = KeyStore.getInstance("AndroidCAStore");
                instance.load(null, null);
                TrustManagerFactory instance2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance2.init(instance);
                TrustManager[] trustManagers = instance2.getTrustManagers();
                if (trustManagers != null && trustManagers.length > 0) {
                    for (int i = 0; i < trustManagers.length; i++) {
                        if (trustManagers[i] instanceof X509TrustManager) {
                            this.f1095a = (X509TrustManager) trustManagers[i];
                            return;
                        }
                    }
                }
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            } catch (CertificateException e3) {
                e3.printStackTrace();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }

        public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public final void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                if (this.f1095a != null) {
                    this.f1095a.checkServerTrusted(chain, authType);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                Throwable th = e;
                while (th != null) {
                    if ((th instanceof CertificateExpiredException) || (th instanceof CertificateNotYetValidException)) {
                        Log.e("HttpsTrustManager", "Certificate Exception has happened");
                        return;
                    }
                    th = th.getCause();
                }
                throw e;
            }
        }

        public final X509Certificate[] getAcceptedIssuers() {
            if (this.f1095a != null) {
                return this.f1095a.getAcceptedIssuers();
            }
            return null;
        }
    }

    public final C0311a m724a(String str) {
        this.f1097a = str;
        return this;
    }

    public final C0311a m726b(String str) {
        this.f1098a.add(str);
        return this;
    }

    public final C0311a m727c(String str) {
        this.f1100b = str;
        return this;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.gala.tvapi.tv3.p028b.C0312b m725a(boolean r16) {
        /*
        r15 = this;
        r3 = new com.gala.tvapi.tv3.b.b;
        r3.<init>();
        r0 = 0;
        r2 = r0;
    L_0x0007:
        r8 = android.os.SystemClock.elapsedRealtime();
        r0 = 0;
        r3.f1101a = r0;
        r0 = 0;
        r3.f1104a = r0;
        r0 = 0;
        r3.f1103a = r0;
        r0 = 0;
        r3.f1102a = r0;
        r1 = 0;
        r4 = 0;
        r0 = 1;
        r0 = new javax.net.ssl.TrustManager[r0];	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r5 = 0;
        r6 = new com.gala.tvapi.tv3.b.a$a;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r6.<init>();	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0[r5] = r6;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r5 = "SSL";
        r5 = javax.net.ssl.SSLContext.getInstance(r5);	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r6 = 0;
        r7 = new java.security.SecureRandom;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r7.<init>();	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r5.init(r6, r0, r7);	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0 = r5.getSocketFactory();	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(r0);	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0 = r15.f1099a;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(r0);	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0 = new java.net.URL;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r5 = r15.f1097a;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0.<init>(r5);	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x026f, Error -> 0x026c }
        r1 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0.setConnectTimeout(r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r0.setReadTimeout(r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = "Charset";
        r5 = "UTF-8";
        r0.setRequestProperty(r1, r5);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = "Connection";
        r5 = "close";
        r0.setRequestProperty(r1, r5);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = "Content-Type";
        r5 = "application/json";
        r0.setRequestProperty(r1, r5);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = r15.f1098a;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 == 0) goto L_0x00ce;
    L_0x0077:
        r1 = r15.f1098a;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = r1.size();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 <= 0) goto L_0x00ce;
    L_0x007f:
        r1 = r15.f1098a;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r5 = r1.iterator();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
    L_0x0085:
        r1 = r5.hasNext();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 == 0) goto L_0x00ce;
    L_0x008b:
        r1 = r5.next();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r6 = ":";
        r1 = r1.split(r6);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 == 0) goto L_0x0085;
    L_0x009a:
        r6 = r1.length;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r7 = 2;
        if (r6 < r7) goto L_0x0085;
    L_0x009e:
        r6 = 0;
        r6 = r1[r6];	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r7 = 1;
        r1 = r1[r7];	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r0.setRequestProperty(r6, r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        goto L_0x0085;
    L_0x00a8:
        r1 = move-exception;
        r14 = r1;
        r1 = r4;
        r4 = r0;
        r0 = r14;
    L_0x00ad:
        r5 = -50;
        r3.f1101a = r5;	 Catch:{ all -> 0x0267 }
        r3.f1103a = r0;	 Catch:{ all -> 0x0267 }
        r0.printStackTrace();	 Catch:{ all -> 0x0267 }
        if (r1 == 0) goto L_0x00bb;
    L_0x00b8:
        r1.close();	 Catch:{ IOException -> 0x024c }
    L_0x00bb:
        if (r4 == 0) goto L_0x00c0;
    L_0x00bd:
        r4.disconnect();
    L_0x00c0:
        r0 = r3.f1104a;
        if (r0 == 0) goto L_0x025d;
    L_0x00c4:
        r0 = r3.f1104a;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x025d;
    L_0x00cc:
        r0 = r3;
    L_0x00cd:
        return r0;
    L_0x00ce:
        if (r16 == 0) goto L_0x0112;
    L_0x00d0:
        r1 = "POST";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = r15.f1100b;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 == 0) goto L_0x00fa;
    L_0x00da:
        r1 = r15.f1100b;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = r1.isEmpty();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r1 != 0) goto L_0x00fa;
    L_0x00e2:
        r1 = 1;
        r0.setDoOutput(r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r5 = r0.getOutputStream();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1.<init>(r5);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r5 = r15.f1100b;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1.writeBytes(r5);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1.flush();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1.close();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
    L_0x00fa:
        r5 = r0.getResponseCode();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r3.f1101a = r5;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r1 = 204; // 0xcc float:2.86E-43 double:1.01E-321;
        if (r5 != r1) goto L_0x013a;
    L_0x0104:
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        r6 = r6 - r8;
        r3.f1102a = r6;	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r0 == 0) goto L_0x0110;
    L_0x010d:
        r0.disconnect();
    L_0x0110:
        r0 = r3;
        goto L_0x00cd;
    L_0x0112:
        r1 = "GET";
        r0.setRequestMethod(r1);	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        goto L_0x00fa;
    L_0x0119:
        r1 = move-exception;
        r14 = r1;
        r1 = r0;
        r0 = r14;
    L_0x011d:
        r5 = -50;
        r3.f1101a = r5;	 Catch:{ all -> 0x0265 }
        r5 = new java.lang.Exception;	 Catch:{ all -> 0x0265 }
        r6 = r0.fillInStackTrace();	 Catch:{ all -> 0x0265 }
        r5.<init>(r6);	 Catch:{ all -> 0x0265 }
        r3.f1103a = r5;	 Catch:{ all -> 0x0265 }
        r0.printStackTrace();	 Catch:{ all -> 0x0265 }
        if (r4 == 0) goto L_0x0134;
    L_0x0131:
        r4.close();	 Catch:{ IOException -> 0x0252 }
    L_0x0134:
        if (r1 == 0) goto L_0x00c0;
    L_0x0136:
        r1.disconnect();
        goto L_0x00c0;
    L_0x013a:
        r1 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r5 >= r1) goto L_0x01d5;
    L_0x013e:
        r4 = r0.getInputStream();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r4 == 0) goto L_0x01c9;
    L_0x0144:
        r5 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = "UTF-8";
        r1.<init>(r4, r6);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r5.<init>(r1);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = f1096a;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = r1.get();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = (java.lang.StringBuilder) r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = 0;
        r7 = r1.length();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1.delete(r6, r7);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r10 = new char[r6];	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = 0;
    L_0x0167:
        r11 = r5.read(r10);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r12 = -1;
        if (r11 == r12) goto L_0x018f;
    L_0x016e:
        r6 = 0;
        r1.append(r10, r6, r11);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = r1.length();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = (long) r6;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r12 = 10485760; // 0xa00000 float:1.469368E-38 double:5.180654E-317;
        r11 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r11 <= 0) goto L_0x0167;
    L_0x017e:
        r10 = new java.lang.Exception;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r11 = "Connent length is too long";
        r10.<init>(r11);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r3.f1103a = r10;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r10 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r10 = r10 - r8;
        r3.f1102a = r10;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
    L_0x018f:
        r10 = 10485760; // 0xa00000 float:1.469368E-38 double:5.180654E-317;
        r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r6 <= 0) goto L_0x01b9;
    L_0x0196:
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r5 = "Connent length is too long";
        r1.<init>(r5);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r3.f1103a = r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = r6 - r8;
        r3.f1102a = r6;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        if (r4 == 0) goto L_0x01ac;
    L_0x01a9:
        r4.close();	 Catch:{ IOException -> 0x01b4 }
    L_0x01ac:
        if (r0 == 0) goto L_0x01b1;
    L_0x01ae:
        r0.disconnect();
    L_0x01b1:
        r0 = r3;
        goto L_0x00cd;
    L_0x01b4:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x01ac;
    L_0x01b9:
        r1 = r1.toString();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r3.f1104a = r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = r6 - r8;
        r3.f1102a = r6;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r5.close();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
    L_0x01c9:
        if (r4 == 0) goto L_0x01ce;
    L_0x01cb:
        r4.close();	 Catch:{ IOException -> 0x0247 }
    L_0x01ce:
        if (r0 == 0) goto L_0x00c0;
    L_0x01d0:
        r0.disconnect();
        goto L_0x00c0;
    L_0x01d5:
        r4 = r0.getErrorStream();	 Catch:{ Exception -> 0x00a8, Error -> 0x0119, all -> 0x0238 }
        if (r4 == 0) goto L_0x01c9;
    L_0x01db:
        r6 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r7 = "UTF-8";
        r1.<init>(r4, r7);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6.<init>(r1);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = f1096a;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = r1.get();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = (java.lang.StringBuilder) r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r7 = 0;
        r10 = r1.length();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1.delete(r7, r10);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r7 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r7 = new char[r7];	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
    L_0x01fc:
        r10 = r6.read(r7);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r11 = -1;
        if (r10 == r11) goto L_0x020f;
    L_0x0203:
        r11 = 0;
        r1.append(r7, r11, r10);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        goto L_0x01fc;
    L_0x0208:
        r1 = move-exception;
        r14 = r1;
        r1 = r4;
        r4 = r0;
        r0 = r14;
        goto L_0x00ad;
    L_0x020f:
        r1 = r1.toString();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r3.f1104a = r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r10 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r8 = r10 - r8;
        r3.f1102a = r8;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6.close();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r7 = "http error";
        r6.<init>(r7);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r5 = r6.append(r5);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r1.<init>(r5);	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        r3.f1103a = r1;	 Catch:{ Exception -> 0x0208, Error -> 0x0119, all -> 0x0238 }
        goto L_0x01c9;
    L_0x0238:
        r1 = move-exception;
        r14 = r1;
        r1 = r0;
        r0 = r14;
    L_0x023c:
        if (r4 == 0) goto L_0x0241;
    L_0x023e:
        r4.close();	 Catch:{ IOException -> 0x0258 }
    L_0x0241:
        if (r1 == 0) goto L_0x0246;
    L_0x0243:
        r1.disconnect();
    L_0x0246:
        throw r0;
    L_0x0247:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x01ce;
    L_0x024c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x00bb;
    L_0x0252:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0134;
    L_0x0258:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0241;
    L_0x025d:
        r0 = r2 + 1;
        r1 = 2;
        if (r0 >= r1) goto L_0x01b1;
    L_0x0262:
        r2 = r0;
        goto L_0x0007;
    L_0x0265:
        r0 = move-exception;
        goto L_0x023c;
    L_0x0267:
        r0 = move-exception;
        r14 = r1;
        r1 = r4;
        r4 = r14;
        goto L_0x023c;
    L_0x026c:
        r0 = move-exception;
        goto L_0x011d;
    L_0x026f:
        r0 = move-exception;
        r14 = r4;
        r4 = r1;
        r1 = r14;
        goto L_0x00ad;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.tvapi.tv3.b.a.a(boolean):com.gala.tvapi.tv3.b.b");
    }
}
