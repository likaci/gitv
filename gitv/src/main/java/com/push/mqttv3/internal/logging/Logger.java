package com.push.mqttv3.internal.logging;

public interface Logger {
    public static final int CONFIG = 4;
    public static final int FINE = 5;
    public static final int FINER = 6;
    public static final int FINEST = 7;
    public static final int INFO = 3;
    public static final int SEVERE = 1;
    public static final int WARNING = 2;

    void config(String str, String str2, String str3);

    void config(String str, String str2, String str3, Object[] objArr);

    void config(String str, String str2, String str3, Object[] objArr, Throwable th);

    void ffdc(String str, String str2, String str3, Object[] objArr, Throwable th, boolean z);

    void ffdc(String str, String str2, Thread thread, Throwable th, boolean z);

    void ffdc(String str, String str2, Throwable th, boolean z);

    void fine(String str, String str2, String str3);

    void fine(String str, String str2, String str3, Object[] objArr);

    void finer(String str, String str2, String str3);

    void finer(String str, String str2, String str3, Object[] objArr);

    void finest(String str, String str2, String str3);

    void finest(String str, String str2, String str3, Object[] objArr);

    String formatMessage(String str, Object[] objArr);

    void info(String str, String str2, String str3);

    void info(String str, String str2, String str3, Object[] objArr);

    void info(String str, String str2, String str3, Object[] objArr, Throwable th);

    boolean isLoggable(int i);

    void log(int i, String str, String str2, String str3, Object[] objArr, Throwable th);

    void severe(String str, String str2, String str3);

    void severe(String str, String str2, String str3, Object[] objArr);

    void severe(String str, String str2, String str3, Object[] objArr, Throwable th);

    void trace(int i, String str, String str2, String str3, Object[] objArr);

    void warning(String str, String str2, String str3);

    void warning(String str, String str2, String str3, Object[] objArr);

    void warning(String str, String str2, String str3, Object[] objArr, Throwable th);
}
