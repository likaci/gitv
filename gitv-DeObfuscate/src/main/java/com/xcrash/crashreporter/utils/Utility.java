package com.xcrash.crashreporter.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class Utility {
    private static final String CHAR_SET = "UTF-8";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String TVGUO2 = "m201_512m";
    private static String mIMEI = null;
    private static String mMobileModel = null;
    private static String mOSVersionInfo = null;

    public static String getVersionName(Context activity) {
        if (activity == null) {
            return "";
        }
        try {
            return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String getOSVersionInfo() {
        if (!DeliverUtils.isEmpty(mOSVersionInfo)) {
            return mOSVersionInfo;
        }
        mOSVersionInfo = VERSION.RELEASE;
        return mOSVersionInfo;
    }

    public static String getMobileModel() {
        if (!DeliverUtils.isEmpty(mMobileModel)) {
            return mMobileModel;
        }
        mMobileModel = Build.MODEL;
        return mMobileModel;
    }

    public static String getDeviceName() {
        return Build.MANUFACTURER + "-" + Build.MODEL;
    }

    public static String getIMEI(Context _mContext) {
        if (!DeliverUtils.isEmpty(mIMEI)) {
            return mIMEI;
        }
        mIMEI = getDeviceId(_mContext);
        return mIMEI;
    }

    private static TelephonyManager getTelephonyManager(Context _mContext) {
        return (TelephonyManager) _mContext.getSystemService("phone");
    }

    public static String getDeviceId(Context context) {
        if (isTvGuo()) {
            return "";
        }
        String deviceId = "";
        if (context == null) {
            return deviceId;
        }
        try {
            if (hasSelfPermission(context, "android.permission.READ_PHONE_STATE")) {
                return getTelephonyManager(context).getDeviceId();
            }
            return deviceId;
        } catch (Throwable th) {
            return "";
        }
    }

    public static boolean hasSelfPermission(Context context, String permission) {
        try {
            return context.checkPermission(permission, Process.myPid(), Process.myUid()) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        for (RunningAppProcessInfo process : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (process.pid == pid) {
                return process.processName;
            }
        }
        return null;
    }

    public static String inputStreamToString(InputStream is) {
        Exception e;
        Throwable th;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            try {
                for (String s = br2.readLine(); s != null; s = br2.readLine()) {
                    sb.append(s + "\n");
                }
                if (br2 != null) {
                    try {
                        br2.close();
                        br = br2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        br = br2;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                br = br2;
                try {
                    e.printStackTrace();
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return sb.toString();
                } catch (Throwable th2) {
                    th = th2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                br = br2;
                if (br != null) {
                    br.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (br != null) {
                br.close();
            }
            return sb.toString();
        }
        return sb.toString();
    }

    public static boolean isTvGuo() {
        if (TVGUO2.equals(getMobileModel())) {
            return true;
        }
        return false;
    }

    public static boolean copyToFile(File sourceFile, File destFile) {
        IOException e;
        Throwable th;
        boolean z = false;
        if (!(sourceFile == null || destFile == null || !sourceFile.exists())) {
            InputStream inputStream = null;
            FileOutputStream out = null;
            try {
                FileOutputStream out2;
                if (destFile.exists()) {
                    destFile.delete();
                }
                InputStream inputStream2 = new FileInputStream(sourceFile);
                try {
                    out2 = new FileOutputStream(destFile);
                } catch (IOException e2) {
                    e = e2;
                    inputStream = inputStream2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.flush();
                                out.getFD().sync();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            try {
                                out.flush();
                                out.getFD().sync();
                            } catch (IOException e3222) {
                                e3222.printStackTrace();
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e32222) {
                                e32222.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e322222) {
                                e322222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = inputStream2;
                    if (out != null) {
                        out.flush();
                        out.getFD().sync();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
                try {
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int bytesRead = inputStream2.read(buffer);
                        if (bytesRead < 0) {
                            break;
                        }
                        out2.write(buffer, 0, bytesRead);
                    }
                    z = true;
                    if (out2 != null) {
                        try {
                            out2.flush();
                            out2.getFD().sync();
                        } catch (IOException e3222222) {
                            e3222222.printStackTrace();
                        }
                    }
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (IOException e32222222) {
                            e32222222.printStackTrace();
                        }
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e322222222) {
                            e322222222.printStackTrace();
                        }
                    }
                } catch (IOException e4) {
                    e322222222 = e4;
                    out = out2;
                    inputStream = inputStream2;
                    e322222222.printStackTrace();
                    if (out != null) {
                        out.flush();
                        out.getFD().sync();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return z;
                } catch (Throwable th4) {
                    th = th4;
                    out = out2;
                    inputStream = inputStream2;
                    if (out != null) {
                        out.flush();
                        out.getFD().sync();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e322222222 = e5;
                e322222222.printStackTrace();
                if (out != null) {
                    out.flush();
                    out.getFD().sync();
                }
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return z;
            }
        }
        return z;
    }

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : byteArray) {
                hexString.append(HEX_DIGITS[(b >> 4) & 15]);
                hexString.append(HEX_DIGITS[b & 15]);
            }
            return hexString.toString();
        } catch (Exception e) {
            return str;
        }
    }
}
