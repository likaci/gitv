package com.gala.sysinput;

public final class SysInput {
    private static final String TAG = "SysInput";
    static boolean sLoadSuccess;

    public static native boolean isEnable();

    public static native void reset();

    public static native void setKeyEvent(int i);

    static {
        sLoadSuccess = false;
        try {
            System.loadLibrary(TAG);
            sLoadSuccess = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            sLoadSuccess = false;
        }
    }
}
