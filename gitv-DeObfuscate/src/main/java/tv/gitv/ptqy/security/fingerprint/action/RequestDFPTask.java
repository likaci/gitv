package tv.gitv.ptqy.security.fingerprint.action;

import android.content.Context;
import android.os.AsyncTask;
import java.net.URLEncoder;
import tv.gitv.ptqy.security.fingerprint.FingerPrintManager;
import tv.gitv.ptqy.security.fingerprint.LogMgr;
import tv.gitv.ptqy.security.fingerprint.Utils.Utils;
import tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;

public class RequestDFPTask extends AsyncTask<FingerPrintCallBack, Integer, String> {
    private Context context;

    private void requestFingerPrint(android.content.Context r27, tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack r28) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r26 = this;
        r2 = tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting;
        if (r2 == 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r2 = 1;
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r2;
        r20 = new java.net.URL;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "https://cook.ptqy.gitv.tv/security/dfp/sign";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r20;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.<init>(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9 = r20.openConnection();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9 = (java.net.HttpURLConnection) r9;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "POST";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.setRequestMethod(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "Content-type";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "application/x-www-form-urlencoded";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.setRequestProperty(r2, r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 1;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.setDoInput(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 1;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.setDoOutput(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.setConnectTimeout(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r16 = r26.getRequestData(r27);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "payload: ";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r16;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.append(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.LogMgr.m1899i(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r15 = r9.getOutputStream();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "UTF-8";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r16;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r0.getBytes(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r15.write(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r15.flush();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r15.close();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = r9.getResponseCode();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        if (r2 != r0) goto L_0x0113;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x0071:
        r12 = r9.getInputStream();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x0075:
        r2 = 0;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r2;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r17.<init>();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r8 = new byte[r2];	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x0081:
        r10 = r12.read(r8);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = -1;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        if (r10 != r2) goto L_0x0119;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x0088:
        r12.close();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r9.disconnect();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "Response : ";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = r17.toString();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.append(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.LogMgr.m1899i(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r13 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r17.toString();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r13.<init>(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "0";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "code";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = r13.getString(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.equals(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        if (r2 == 0) goto L_0x0147;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x00c6:
        r2 = "result";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r13.getJSONObject(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "dfp";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r3 = r2.getString(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = "result";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r13.getJSONObject(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "ttl";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r18 = r2.getLong(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r22 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r24 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r24 = r24 * r18;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r6 = r22 + r24;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        if (r3 == 0) goto L_0x010e;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x00f6:
        r21 = new tv.gitv.ptqy.security.fingerprint.action.LocalFingerPrintCacheHelper;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r1 = r27;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = new tv.gitv.ptqy.security.fingerprint.entity.FingerPrintData;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2.<init>(r3, r4, r6);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.writeFingerPrintLocalCache(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r28;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.onSuccess(r3);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x010e:
        r2 = 0;
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r2;
        goto L_0x0004;
    L_0x0113:
        r12 = r9.getErrorStream();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        goto L_0x0075;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
    L_0x0119:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = 0;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2.<init>(r8, r0, r10);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r17;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.append(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        goto L_0x0081;
    L_0x0129:
        r11 = move-exception;
        r11.printStackTrace();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 0;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r2;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r11.getMessage();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r28;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.onFailed(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r11.getMessage();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r27;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.pingback.PingBackAgent.saveFetchFingerprintError(r0, r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = 0;
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r2;
        goto L_0x0004;
    L_0x0147:
        r2 = "message";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r14 = r13.getString(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r28;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0.onFailed(r14);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r21 = "failed message : ";	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r21;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.LogMgr.m1899i(r2);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        r0 = r27;	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        tv.gitv.ptqy.security.fingerprint.pingback.PingBackAgent.saveFetchFingerprintError(r0, r14);	 Catch:{ Exception -> 0x0129, all -> 0x016e }
        goto L_0x010e;
    L_0x016e:
        r2 = move-exception;
        r21 = 0;
        tv.gitv.ptqy.security.fingerprint.FingerPrintManager.isExecuting = r21;
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: tv.gitv.ptqy.security.fingerprint.action.RequestDFPTask.requestFingerPrint(android.content.Context, tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack):void");
    }

    public RequestDFPTask(Context context) {
        this.context = context;
    }

    protected String doInBackground(FingerPrintCallBack... params) {
        requestFingerPrint(this.context, params[0]);
        return null;
    }

    private String getRequestData(Context context) {
        String odfp;
        String dim = FingerPrintManager.getInstance().getEnvInfo(context, true);
        try {
            odfp = new LocalFingerPrintCacheHelper(context).readFingerPrintLocalCache();
        } catch (Exception e) {
            odfp = e.getMessage();
        }
        if (odfp == null) {
            odfp = "";
        }
        String toSign = new StringBuilder(String.valueOf(odfp)).append(dim).append(Consts.PLATFORM).append("2.0").toString();
        try {
            dim = URLEncoder.encode(dim, "UTF-8");
        } catch (Exception e2) {
            LogMgr.m1899i("URLEncoder.encode: " + e2);
        }
        return "dfp=" + odfp + "&dim=" + dim + "&ver=" + "2.0" + "&plat=" + Consts.PLATFORM + "&sig=" + Utils.calcHmac(toSign);
    }
}
