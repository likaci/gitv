package com.push.nativeprocess;

import android.content.Context;
import android.util.Log;

public abstract class NativeProcess {
    private static String PackageName = "";
    private static String ServiceName = "";
    public static final String TAG = "NativeProcess";
    private static boolean mUseNativeProcess;
    private Context mContext;
    private int mParentPid;

    public static native void create(Context context, Class<? extends NativeProcess> cls, String str);

    public abstract void runOnSubprocess();

    static {
        mUseNativeProcess = true;
        try {
            Log.d(TAG, "load the library enter");
            System.loadLibrary("nativeprocess");
            Log.d(TAG, "load the library finish");
        } catch (UnsatisfiedLinkError e) {
            mUseNativeProcess = false;
            Log.d(TAG, "Fail to load the library for the daemon process e = " + e);
            e.printStackTrace();
        }
    }

    public static boolean useNativeProcess() {
        return mUseNativeProcess;
    }

    public NativeProcess() {
        Log.d(TAG, "mParentPid=" + this.mParentPid + ", mContext=" + (this.mContext == null));
    }

    public final int getParentPid() {
        return this.mParentPid;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public static void setServiceName(String mServiceName) {
        ServiceName = mServiceName;
    }

    public static String getServiceName() {
        return ServiceName;
    }

    public static String getPackageName() {
        return PackageName;
    }

    public static void setPackageName(String packageName) {
        PackageName = packageName;
    }
}
