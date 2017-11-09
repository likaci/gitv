package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class C2116g {
    private static String f2203a;

    public static String m1807a(Context context) {
        String str = null;
        if (null != null) {
            return null;
        }
        String string;
        String b = C2116g.m1810b(context);
        try {
            string = Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable th) {
            string = null;
        }
        if (VERSION.SDK_INT > 8) {
            str = Build.SERIAL;
        }
        return C2116g.m1811b(b + string + str);
    }

    public static String m1810b(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            String deviceId = telephonyManager.getDeviceId();
            int i = 10;
            while (deviceId == null) {
                int i2 = i - 1;
                if (i <= 0) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                deviceId = telephonyManager.getDeviceId();
                i = i2;
            }
            return deviceId;
        } catch (Throwable th) {
            return null;
        }
    }

    private static String m1811b(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(C2116g.m1812c(str));
            BigInteger bigInteger = new BigInteger(1, instance.digest());
            return String.format("%1$032X", new Object[]{bigInteger});
        } catch (NoSuchAlgorithmException e) {
            return str;
        }
    }

    private static byte[] m1812c(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    public String m1813a() {
        if (f2203a != null) {
            return f2203a;
        }
        C2112b.m1802a().m1805a(new C2117h(C2111a.m1779a()));
        return null;
    }
}
