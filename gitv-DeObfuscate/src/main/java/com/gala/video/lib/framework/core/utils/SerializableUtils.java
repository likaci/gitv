package com.gala.video.lib.framework.core.utils;

import android.annotation.SuppressLint;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableUtils {
    private static final String TAG = "SerializableUtils";

    @SuppressLint({"WorldReadableFiles"})
    public static void write(Object rsls, String fileName) throws IOException {
        ObjectOutputStream ostream;
        Exception e;
        Throwable th;
        synchronized (fileName) {
            File file = new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + fileName);
            if (!file.exists()) {
                createFile(file);
            }
            FileOutputStream fstream = null;
            ObjectOutputStream ostream2 = null;
            try {
                FileOutputStream fstream2 = new FileOutputStream(file);
                try {
                    ostream = new ObjectOutputStream(fstream2);
                } catch (Exception e2) {
                    e = e2;
                    fstream = fstream2;
                    try {
                        LogUtils.m1571e(TAG, e + "");
                        if (ostream2 != null) {
                            try {
                                ostream2.flush();
                                ostream2.close();
                            } catch (IOException e3) {
                                LogUtils.m1572e(TAG, "write", e3);
                            }
                        }
                        if (fstream != null) {
                            fstream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (ostream2 != null) {
                            try {
                                ostream2.flush();
                                ostream2.close();
                            } catch (IOException e32) {
                                LogUtils.m1572e(TAG, "write", e32);
                                throw th;
                            }
                        }
                        if (fstream != null) {
                            fstream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fstream = fstream2;
                    if (ostream2 != null) {
                        ostream2.flush();
                        ostream2.close();
                    }
                    if (fstream != null) {
                        fstream.close();
                    }
                    throw th;
                }
                try {
                    ostream.writeObject(rsls);
                    if (ostream != null) {
                        try {
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e322) {
                            LogUtils.m1572e(TAG, "write", e322);
                            ostream2 = ostream;
                            fstream = fstream2;
                        }
                    }
                    if (fstream2 != null) {
                        fstream2.close();
                    }
                    ostream2 = ostream;
                    fstream = fstream2;
                } catch (Exception e4) {
                    e = e4;
                    ostream2 = ostream;
                    fstream = fstream2;
                    LogUtils.m1571e(TAG, e + "");
                    if (ostream2 != null) {
                        ostream2.flush();
                        ostream2.close();
                    }
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    ostream2 = ostream;
                    fstream = fstream2;
                    if (ostream2 != null) {
                        ostream2.flush();
                        ostream2.close();
                    }
                    if (fstream != null) {
                        fstream.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                LogUtils.m1571e(TAG, e + "");
                if (ostream2 != null) {
                    ostream2.flush();
                    ostream2.close();
                }
                if (fstream != null) {
                    fstream.close();
                }
            }
        }
    }

    public static Object read(String fileName) throws Exception {
        Object obj;
        synchronized (fileName) {
            File file = new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + fileName);
            if (!file.exists()) {
                createFile(file);
            }
            FileInputStream fstream = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(fstream);
            obj = s.readObject();
            s.close();
            fstream.close();
        }
        return obj;
    }

    private static void createFile(File file) {
        if (file != null) {
            try {
                File fileParent = new File(file.getParent());
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
