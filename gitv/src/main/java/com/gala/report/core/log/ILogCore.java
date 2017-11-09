package com.gala.report.core.log;

import android.content.Context;

public interface ILogCore {
    void A(String str, String str2);

    void A(String str, String str2, Throwable th);

    void D(String str, String str2);

    void D(String str, String str2, Throwable th);

    void E(String str, String str2);

    void E(String str, String str2, Throwable th);

    void I(String str, String str2);

    void I(String str, String str2, Throwable th);

    void V(String str, String str2);

    void V(String str, String str2, Throwable th);

    void W(String str, String str2);

    void W(String str, String str2, Throwable th);

    void addlog(String str, String str2, String str3, Throwable th);

    String getLog(long j);

    String getLogFromLogcatBuffer(long j);

    boolean init(Context context, boolean z, ILogListener iLogListener, boolean z2);

    void onCreate();

    void onDestroy();

    void onStartCommand();

    boolean release();

    void setJSCongfig(String str);

    void snapShot();

    boolean startRecord();

    boolean stopRecord();
}
