package com.gala.video.app.stub;

public class ProcessInfo {
    private static final String TAG = "ProcessInfo";

    public static String getCurProcessName() {
        return ProcessHelper.getCurrentProcessName();
    }
}
