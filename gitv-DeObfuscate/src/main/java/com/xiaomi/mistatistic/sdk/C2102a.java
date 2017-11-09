package com.xiaomi.mistatistic.sdk;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import com.push.pushservice.constants.DataConst;
import com.xiaomi.mistatistic.sdk.controller.C2111a;
import com.xiaomi.mistatistic.sdk.controller.C2116g;
import com.xiaomi.mistatistic.sdk.controller.C2124o;
import com.xiaomi.mistatistic.sdk.controller.C2126q;
import com.xiaomi.mistatistic.sdk.controller.C2128s;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.cybergarage.soap.SOAP;

public class C2102a implements UncaughtExceptionHandler {
    private static boolean f2161a = false;
    private static int f2162b = 1;
    private final UncaughtExceptionHandler f2163c;

    public C2102a() {
        this.f2163c = null;
    }

    public C2102a(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.f2163c = uncaughtExceptionHandler;
    }

    public static void m1760a(int i) {
        f2162b = i;
    }

    public static void m1761a(Throwable th) {
        if (!f2161a) {
            return;
        }
        if (th == null) {
            throw new IllegalArgumentException("the throwable is null.");
        } else if (th.getStackTrace() != null && th.getStackTrace().length != 0) {
            Writer stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            String obj = stringWriter.toString();
            List arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, C2111a.m1781b()));
            arrayList.add(new BasicNameValuePair("app_key", C2111a.m1782c()));
            arrayList.add(new BasicNameValuePair("device_uuid", C2116g.m1807a(C2111a.m1779a())));
            arrayList.add(new BasicNameValuePair("device_os", "Android " + VERSION.SDK_INT));
            arrayList.add(new BasicNameValuePair("device_model", Build.MODEL));
            arrayList.add(new BasicNameValuePair("app_version", C2111a.m1784e()));
            arrayList.add(new BasicNameValuePair("app_channel", C2111a.m1783d()));
            arrayList.add(new BasicNameValuePair("app_start_time", String.valueOf(System.currentTimeMillis())));
            arrayList.add(new BasicNameValuePair("app_crash_time", String.valueOf(System.currentTimeMillis())));
            arrayList.add(new BasicNameValuePair("crash_exception_type", th.getClass().getName() + SOAP.DELIM + th.getMessage()));
            arrayList.add(new BasicNameValuePair("crash_exception_desc", th instanceof OutOfMemoryError ? "OutOfMemoryError" : obj));
            arrayList.add(new BasicNameValuePair("crash_callstack", obj));
            try {
                new C2124o().m1840a("upload the exception " + C2126q.m1842a(C2111a.m1779a(), BuildSetting.isTest() ? "http://10.99.168.145:8097/micrash" : "https://data.mistat.xiaomi.com/micrash", arrayList));
            } catch (Throwable e) {
                new C2124o().m1841a("Error to upload the exception", e);
            }
        }
    }

    public static void m1762a(boolean z) {
        UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(defaultUncaughtExceptionHandler instanceof C2102a)) {
            if (z) {
                defaultUncaughtExceptionHandler = null;
            }
            Thread.setDefaultUncaughtExceptionHandler(new C2102a(defaultUncaughtExceptionHandler));
            f2161a = true;
        }
    }

    public static ArrayList m1763b() {
        ArrayList arrayList;
        Object obj;
        Throwable e;
        ArrayList arrayList2 = new ArrayList();
        ObjectInputStream objectInputStream = null;
        ObjectInputStream objectInputStream2;
        try {
            ArrayList arrayList3;
            File filesDir = C2111a.m1779a().getFilesDir();
            if (filesDir != null) {
                File file = new File(filesDir, ".exception");
                if (file.isFile()) {
                    objectInputStream2 = new ObjectInputStream(new FileInputStream(file));
                    try {
                        arrayList3 = (ArrayList) objectInputStream2.readObject();
                        objectInputStream = objectInputStream2;
                        if (objectInputStream == null) {
                            try {
                                objectInputStream.close();
                                arrayList = arrayList3;
                                obj = null;
                            } catch (IOException e2) {
                                arrayList = arrayList3;
                                obj = null;
                            }
                        } else {
                            arrayList = arrayList3;
                            obj = null;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        try {
                            new C2124o().m1841a("", e);
                            obj = 1;
                            if (objectInputStream2 == null) {
                                arrayList = arrayList2;
                            } else {
                                try {
                                    objectInputStream2.close();
                                    arrayList = arrayList2;
                                } catch (IOException e4) {
                                    arrayList = arrayList2;
                                }
                            }
                            if (obj != null) {
                                C2102a.m1765c();
                            }
                            return arrayList;
                        } catch (Throwable th) {
                            e = th;
                            objectInputStream = objectInputStream2;
                            if (objectInputStream != null) {
                                try {
                                    objectInputStream.close();
                                } catch (IOException e5) {
                                }
                            }
                            throw e;
                        }
                    }
                    if (obj != null) {
                        C2102a.m1765c();
                    }
                    return arrayList;
                }
            }
            arrayList3 = arrayList2;
            if (objectInputStream == null) {
                arrayList = arrayList3;
                obj = null;
            } else {
                objectInputStream.close();
                arrayList = arrayList3;
                obj = null;
            }
        } catch (Exception e6) {
            e = e6;
            objectInputStream2 = null;
            new C2124o().m1841a("", e);
            obj = 1;
            if (objectInputStream2 == null) {
                objectInputStream2.close();
                arrayList = arrayList2;
            } else {
                arrayList = arrayList2;
            }
            if (obj != null) {
                C2102a.m1765c();
            }
            return arrayList;
        } catch (Throwable th2) {
            e = th2;
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            throw e;
        }
        if (obj != null) {
            C2102a.m1765c();
        }
        return arrayList;
    }

    public static void m1764b(Throwable th) {
        Throwable e;
        ArrayList b = C2102a.m1763b();
        b.add(th);
        if (b.size() > 5) {
            b.remove(0);
        }
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(C2111a.m1779a().openFileOutput(".exception", 0));
            try {
                objectOutputStream.writeObject(b);
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    new C2124o().m1841a("", e);
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                } catch (Throwable th2) {
                    e = th2;
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw e;
                }
            }
        } catch (IOException e6) {
            e = e6;
            objectOutputStream = null;
            new C2124o().m1841a("", e);
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
        } catch (Throwable th3) {
            e = th3;
            objectOutputStream = null;
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            throw e;
        }
    }

    public static void m1765c() {
        new File(C2111a.m1779a().getFilesDir(), ".exception").delete();
    }

    public static int m1766d() {
        return f2162b;
    }

    public boolean m1767a() {
        if (System.currentTimeMillis() - C2128s.m1851a(C2111a.m1779a(), "crash_time", 0) > 300000) {
            C2128s.m1854b(C2111a.m1779a(), "crash_count", 1);
            C2128s.m1855b(C2111a.m1779a(), "crash_time", System.currentTimeMillis());
        } else {
            int a = C2128s.m1850a(C2111a.m1779a(), "crash_count", 0);
            if (a == 0) {
                C2128s.m1855b(C2111a.m1779a(), "crash_time", System.currentTimeMillis());
            }
            a++;
            C2128s.m1854b(C2111a.m1779a(), "crash_count", a);
            if (a > 10) {
                return true;
            }
        }
        return false;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (VERSION.SDK_INT >= 9) {
            StrictMode.setThreadPolicy(new Builder().build());
        }
        if (!MiStatInterface.shouldExceptionUploadImmediately()) {
            C2102a.m1764b(th);
        } else if (m1767a()) {
            new C2124o().m1840a("crazy crash...");
        } else {
            C2102a.m1761a(th);
        }
        if (this.f2163c != null) {
            this.f2163c.uncaughtException(thread, th);
        }
    }
}
