package com.gala.report.core.log;

import android.content.Context;

public interface ILogCore {
    void m402A(String str, String str2);

    void m403A(String str, String str2, Throwable th);

    void m404D(String str, String str2);

    void m405D(String str, String str2, Throwable th);

    void m406E(String str, String str2);

    void m407E(String str, String str2, Throwable th);

    void m408I(String str, String str2);

    void m409I(String str, String str2, Throwable th);

    void m410V(String str, String str2);

    void m411V(String str, String str2, Throwable th);

    void m412W(String str, String str2);

    void m413W(String str, String str2, Throwable th);

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
