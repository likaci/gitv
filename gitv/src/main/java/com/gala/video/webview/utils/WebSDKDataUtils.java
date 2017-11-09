package com.gala.video.webview.utils;

import android.annotation.SuppressLint;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebSDKDataUtils {
    private static final String TAG = "EPG/web/WebDataUtils";

    @SuppressLint({"SimpleDateFormat"})
    private static String timeStamp2Date(long seconds) {
        return new SimpleDateFormat("yyyy").format(new Date(seconds));
    }

    public static boolean isSSLProceed(SslError error) {
        if (error == null) {
            Log.e(TAG, "onReceivedSslError error is null!");
            return false;
        }
        SslCertificate mSslCertificate = error.getCertificate();
        if ((mSslCertificate == null || mSslCertificate.getValidNotAfterDate() == null) && mSslCertificate.getValidNotBeforeDate() == null) {
            return false;
        }
        long nowTime = System.currentTimeMillis();
        long beforeTime = mSslCertificate.getValidNotBeforeDate().getTime();
        long afterTime = mSslCertificate.getValidNotAfterDate().getTime();
        if (nowTime < beforeTime || nowTime > afterTime) {
            return true;
        }
        return false;
    }
}
