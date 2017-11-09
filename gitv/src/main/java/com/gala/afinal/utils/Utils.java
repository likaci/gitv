package com.gala.afinal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;

public class Utils {
    private static final long INITIALCRC = -1;
    private static final long POLY64REV = -7661587058870466123L;
    private static final String TAG = "BitmapCommonUtils";
    private static long[] sCrcTable = new long[256];

    static {
        for (int i = 0; i < 256; i++) {
            long j = (long) i;
            for (int i2 = 0; i2 < 8; i2++) {
                j = (j >> 1) ^ ((((int) j) & 1) != 0 ? POLY64REV : 0);
            }
            sCrcTable[i] = j;
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        return new File(("mounted".equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath()) + File.separator + uniqueName);
    }

    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static File getExternalCacheDir(Context context) {
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    public static long getUsableSpace(File path) {
        try {
            StatFs statFs = new StatFs(path.getPath());
            return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        } catch (Exception e) {
            Log.e(TAG, "获取 sdcard 缓存大小 出错，请查看AndroidManifest.xml 是否添加了sdcard的访问权限");
            e.printStackTrace();
            return INITIALCRC;
        }
    }

    public static byte[] getBytes(String in) {
        int i = 0;
        byte[] bArr = new byte[(in.length() << 1)];
        char[] toCharArray = in.toCharArray();
        int length = toCharArray.length;
        int i2 = 0;
        while (i < length) {
            char c = toCharArray[i];
            int i3 = i2 + 1;
            bArr[i2] = (byte) c;
            i2 = i3 + 1;
            bArr[i3] = (byte) (c >> 8);
            i++;
        }
        return bArr;
    }

    public static boolean isSameKey(byte[] key, byte[] buffer) {
        int length = key.length;
        if (buffer.length < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (key[i] != buffer[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int i = to - from;
        if (i < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        Object obj = new byte[i];
        System.arraycopy(original, from, obj, 0, Math.min(original.length - from, i));
        return obj;
    }

    public static byte[] makeKey(String httpUrl) {
        return getBytes(httpUrl);
    }

    public static final long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(in));
    }

    public static final long crc64Long(byte[] buffer) {
        long j = INITIALCRC;
        for (byte b : buffer) {
            j = (j >> 8) ^ sCrcTable[(((int) j) ^ b) & 255];
        }
        return j;
    }
}
