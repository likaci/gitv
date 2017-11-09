package com.push.pushservice.utils;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;

public class DataUtil {
    private static final String TAG = "DataUtil";

    public static byte[] mergeByteArray(byte[] byte_1, byte[] byte_2) {
        if (byte_1 == null || byte_1.length == 0) {
            return byte_2;
        }
        if (byte_2 == null || byte_2.length == 0) {
            return byte_1;
        }
        byte[] byte_3 = new byte[(byte_1.length + byte_2.length)];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static String getNotNullString(String str) {
        return str == null ? "" : str;
    }

    public static String byteAryToString(byte[] msg) {
        if (msg == null || msg.length == 0) {
            return null;
        }
        try {
            return new String(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.logd(TAG, "byteAryToString UnsupportedEncodingException e = " + e);
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            LogUtils.logd(TAG, "byteAryToString Exception e = " + e2);
            e2.printStackTrace();
            return null;
        }
    }

    public static byte[] stringToByteAry(String msg) {
        byte[] bArr = null;
        if (!TextUtils.isEmpty(msg)) {
            try {
                bArr = msg.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                LogUtils.logd(TAG, "stringToByteAry UnsupportedEncodingException e = " + e);
                e.printStackTrace();
            } catch (Exception e2) {
                LogUtils.logd(TAG, "stringToByteAry Exception e = " + e2);
                e2.printStackTrace();
            }
        }
        return bArr;
    }

    public static String getUtf8String(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        } catch (Exception e2) {
            e2.printStackTrace();
            return str;
        }
    }
}
