package com.gala.video.lib.framework.core.utils.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
    private static final String CHARSET = "utf-8";
    public static final int IO_BUFFER_SIZE = 8192;
    public static final String TAG = "FileUtil";
    private static Comparator<File> mComparator = new C16181();

    static class C16181 implements Comparator<File> {
        C16181() {
        }

        public int compare(File lhs, File rhs) {
            if (lhs == null) {
                return 1;
            }
            if (rhs == null) {
                return -1;
            }
            if (lhs.lastModified() > rhs.lastModified()) {
                return 1;
            }
            if (lhs.lastModified() < rhs.lastModified()) {
                return -1;
            }
            return 0;
        }
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static boolean exites(String path) {
        return new File(path).exists();
    }

    public static void checkDir(String file) {
        File f = new File(file);
        if (!f.exists()) {
            if (f.isDirectory()) {
                f.mkdirs();
            }
            File parent = f.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
    }

    public static String getCacheDir(Context ctx) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory() + "/";
        }
        return ctx.getCacheDir().getPath() + "/";
    }

    public static String readFile(String file) {
        Exception e;
        Throwable th;
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            try {
                String str = "";
                while (true) {
                    str = in.readLine();
                    if (str == null) {
                        break;
                    }
                    sb.append(str + "\n");
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e2) {
                        bufferedReader = in;
                    }
                }
                bufferedReader = in;
            } catch (Exception e3) {
                e = e3;
                bufferedReader = in;
                try {
                    LogUtils.m1568d("FileUtil", e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e4) {
                        }
                    }
                    return sb.toString();
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e5) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = in;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            LogUtils.m1568d("FileUtil", e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return sb.toString();
        }
        return sb.toString();
    }

    public static boolean writeFile(String fileName, byte[] bytes) {
        int length;
        if (bytes != null) {
            length = bytes.length;
        } else {
            length = 0;
        }
        if (length == 0) {
            return false;
        }
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(fileName);
            fout.write(bytes);
            fout.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean writeFile(String file, String str) {
        Exception e;
        Throwable th;
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            try {
                out2.append(str);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (Exception e2) {
                        out = out2;
                        return false;
                    }
                }
                out = out2;
                return true;
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    LogUtils.m1568d("FileUtil", e.getMessage());
                    if (out != null) {
                        return false;
                    }
                    try {
                        out.close();
                        return false;
                    } catch (Exception e4) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e5) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            LogUtils.m1568d("FileUtil", e.getMessage());
            if (out != null) {
                return false;
            }
            out.close();
            return false;
        }
    }

    public static boolean createPNGFile(String path, Bitmap bmp) {
        IOException e;
        File file;
        Throwable th;
        OutputStream fOut = null;
        try {
            OutputStream fOut2;
            File f = new File(path);
            try {
                f.createNewFile();
                fOut2 = new BufferedOutputStream(new FileOutputStream(f), 8192);
            } catch (IOException e2) {
                e = e2;
                file = f;
                try {
                    LogUtils.m1568d("FileUtil", e.getMessage());
                    if (fOut != null) {
                        try {
                            fOut.close();
                        } catch (IOException e3) {
                            LogUtils.m1568d("FileUtil", e3.getMessage());
                        }
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (fOut != null) {
                        try {
                            fOut.close();
                        } catch (IOException e32) {
                            LogUtils.m1568d("FileUtil", e32.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                file = f;
                if (fOut != null) {
                    fOut.close();
                }
                throw th;
            }
            try {
                bmp.compress(CompressFormat.PNG, 100, fOut2);
                fOut2.flush();
                if (fOut2 != null) {
                    try {
                        fOut2.close();
                        fOut = fOut2;
                        file = f;
                    } catch (IOException e322) {
                        LogUtils.m1568d("FileUtil", e322.getMessage());
                        fOut = fOut2;
                        file = f;
                    }
                } else {
                    file = f;
                }
            } catch (IOException e4) {
                e322 = e4;
                fOut = fOut2;
                file = f;
                LogUtils.m1568d("FileUtil", e322.getMessage());
                if (fOut != null) {
                    fOut.close();
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                fOut = fOut2;
                file = f;
                if (fOut != null) {
                    fOut.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e322 = e5;
            LogUtils.m1568d("FileUtil", e322.getMessage());
            if (fOut != null) {
                fOut.close();
            }
            return false;
        }
        return false;
    }

    public static boolean createJPGFile(String path, Bitmap bmp) {
        IOException e;
        Throwable th;
        boolean flag = false;
        OutputStream fOut = null;
        try {
            File f = new File(path);
            File file;
            try {
                flag = f.createNewFile();
                OutputStream fOut2 = new BufferedOutputStream(new FileOutputStream(f), 8192);
                try {
                    bmp.compress(CompressFormat.JPEG, 100, fOut2);
                    fOut2.flush();
                    if (fOut2 != null) {
                        try {
                            fOut2.close();
                            fOut = fOut2;
                            file = f;
                        } catch (IOException e2) {
                            LogUtils.m1568d("FileUtil", e2.getMessage());
                            fOut = fOut2;
                            file = f;
                        }
                    } else {
                        file = f;
                    }
                } catch (IOException e3) {
                    e2 = e3;
                    fOut = fOut2;
                    file = f;
                    try {
                        LogUtils.m1568d("FileUtil", e2.getMessage());
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (IOException e22) {
                                LogUtils.m1568d("FileUtil", e22.getMessage());
                            }
                        }
                        return flag;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (IOException e222) {
                                LogUtils.m1568d("FileUtil", e222.getMessage());
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fOut = fOut2;
                    file = f;
                    if (fOut != null) {
                        fOut.close();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e222 = e4;
                file = f;
                LogUtils.m1568d("FileUtil", e222.getMessage());
                if (fOut != null) {
                    fOut.close();
                }
                return flag;
            } catch (Throwable th4) {
                th = th4;
                file = f;
                if (fOut != null) {
                    fOut.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e222 = e5;
            LogUtils.m1568d("FileUtil", e222.getMessage());
            if (fOut != null) {
                fOut.close();
            }
            return flag;
        }
        return flag;
    }

    public static boolean remove(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Exception e) {
            LogUtils.m1568d("FileUtil", e.getMessage());
            return false;
        }
    }

    public static boolean remove(File f) {
        if (f == null) {
            return false;
        }
        try {
            if (!f.exists()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Exception e) {
            LogUtils.m1568d("FileUtil", e.getMessage());
            return false;
        }
    }

    public static boolean clearFolder(String folderPath) {
        try {
            File fFolder = new File(folderPath);
            if (!fFolder.exists() || !fFolder.isDirectory() || !fFolder.canWrite()) {
                return false;
            }
            File[] fs = fFolder.listFiles();
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].exists()) {
                    fs[i].delete();
                }
            }
            return true;
        } catch (Exception e) {
            LogUtils.m1568d("FileUtil", e.getMessage());
            return false;
        }
    }

    public static void sortWithlastModified(List<File> fList) {
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(fList, mComparator);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean sdcardCanWrite() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return false;
        }
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard == null) {
            return false;
        }
        File tryFile = new File(sdcard.getAbsolutePath() + "/try.txt");
        LogUtils.m1568d("FileUtil", "tryFile=" + tryFile);
        if (tryFile.exists()) {
            tryFile.delete();
        }
        try {
            tryFile.createNewFile();
            return true;
        } catch (IOException e) {
            LogUtils.m1572e("FileUtil", "sdcardCanWrite", e);
            return false;
        }
    }
}
