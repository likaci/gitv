package com.tvos.apps.utils;

import android.util.Log;
import com.mcto.ads.internal.net.SendFlag;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.core.ZipFile;

public class ZipUtil {
    private static final String TAG = "ZipUtil";

    public static boolean UnZipFolder(InputStream in, String outPathString) {
        FileOutputStream out;
        Exception e;
        Throwable th;
        if (in == null) {
            return false;
        }
        boolean isUnZipSuccess = true;
        ZipInputStream zipInputStream = null;
        String szName = "";
        try {
            ZipInputStream inZip = new ZipInputStream(in);
            while (true) {
                try {
                    ZipEntry zipEntry = inZip.getNextEntry();
                    if (zipEntry == null) {
                        try {
                            inZip.close();
                            zipInputStream = inZip;
                            return isUnZipSuccess;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            zipInputStream = inZip;
                            return false;
                        }
                    }
                    szName = zipEntry.getName();
                    if (zipEntry.isDirectory()) {
                        new File(new StringBuilder(String.valueOf(outPathString)).append(File.separator).append(szName.substring(0, szName.length() - 1)).toString()).mkdirs();
                    } else {
                        out = null;
                        try {
                            File file = new File(new StringBuilder(String.valueOf(outPathString)).append(File.separator).append(szName).toString());
                            file.createNewFile();
                            FileOutputStream out2 = new FileOutputStream(file);
                            try {
                                byte[] buffer = new byte[1024];
                                while (true) {
                                    int len = inZip.read(buffer);
                                    if (len == -1) {
                                        break;
                                    }
                                    out2.write(buffer, 0, len);
                                    out2.flush();
                                }
                                if (out2 != null) {
                                    out2.close();
                                } else {
                                    continue;
                                }
                            } catch (Exception e3) {
                                e = e3;
                                out = out2;
                            } catch (Throwable th2) {
                                th = th2;
                                out = out2;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            isUnZipSuccess = false;
                            try {
                                e.printStackTrace();
                                if (out != null) {
                                    out.close();
                                } else {
                                    continue;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        }
                    }
                } catch (Exception e5) {
                    e = e5;
                    zipInputStream = inZip;
                } catch (Throwable th4) {
                    th = th4;
                    zipInputStream = inZip;
                }
            }
        } catch (Exception e6) {
            e = e6;
            try {
                e.printStackTrace();
                try {
                    zipInputStream.close();
                    return false;
                } catch (IOException e22) {
                    e22.printStackTrace();
                    return false;
                }
            } catch (Throwable th5) {
                th = th5;
                try {
                    zipInputStream.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
                throw th;
            }
        }
        if (out != null) {
            out.close();
        }
        throw th;
    }

    public static boolean UnzipUsingZip4j(String filePath, String outPath, boolean needChmod) {
        Exception e;
        try {
            Log.d(TAG, "unzip startTime---" + System.currentTimeMillis());
            ZipFile zipFile = new ZipFile(filePath);
            ZipFile zipFile2;
            try {
                zipFile.extractAll(outPath);
                Log.d(TAG, "unzip completeTime---" + System.currentTimeMillis());
                if (needChmod) {
                    try {
                        if (!RootCmdUtils.runCommand("chmod 666 " + outPath + "\n")) {
                            Log.d(TAG, "no permission");
                            zipFile2 = zipFile;
                            return false;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        zipFile2 = zipFile;
                        return false;
                    }
                }
                zipFile2 = zipFile;
                return true;
            } catch (Exception e2) {
                e = e2;
                zipFile2 = zipFile;
                e.printStackTrace();
                return false;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return false;
        }
    }

    public static boolean UnzipUsingZipFile(String filePath, String outPath, boolean needChmod) {
        java.util.zip.ZipFile zipFile;
        BufferedInputStream is;
        BufferedOutputStream dest;
        Exception e2;
        Throwable th;
        Exception e;
        Log.d(TAG, "UnzipUsingZipFile startTime---" + System.currentTimeMillis());
        boolean isUnzipSuccess = true;
        java.util.zip.ZipFile zipfile = null;
        try {
            zipFile = new java.util.zip.ZipFile(filePath);
            try {
                Enumeration<? extends ZipEntry> e3 = zipFile.entries();
                BufferedInputStream is2 = null;
                BufferedOutputStream dest2 = null;
                while (e3.hasMoreElements()) {
                    try {
                        ZipEntry entry = (ZipEntry) e3.nextElement();
                        File file = new File(new StringBuilder(String.valueOf(outPath)).append("/").append(entry.getName()).toString());
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        if (!entry.isDirectory()) {
                            if (file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            try {
                                is = new BufferedInputStream(zipFile.getInputStream(entry));
                                try {
                                    byte[] data = new byte[2048];
                                    dest = new BufferedOutputStream(new FileOutputStream(file), SendFlag.FLAG_KEY_PINGBACK_SP);
                                    while (true) {
                                        int count = is.read(data, 0, 2048);
                                        if (count == -1) {
                                            break;
                                        }
                                        try {
                                            dest.write(data, 0, count);
                                        } catch (Exception e4) {
                                            e2 = e4;
                                        }
                                    }
                                    if (dest != null) {
                                        dest.flush();
                                        dest.close();
                                    }
                                    if (is != null) {
                                        is.close();
                                    }
                                    if (!isUnzipSuccess) {
                                        if (zipFile != null) {
                                            zipFile.close();
                                        }
                                        if (zipFile != null) {
                                            try {
                                                zipFile.close();
                                            } catch (IOException e5) {
                                                e5.printStackTrace();
                                                zipfile = zipFile;
                                                return false;
                                            }
                                        }
                                        zipfile = zipFile;
                                        return false;
                                    }
                                } catch (Exception e6) {
                                    e2 = e6;
                                    dest = dest2;
                                    try {
                                        e2.printStackTrace();
                                        isUnzipSuccess = false;
                                        if (dest != null) {
                                            dest.flush();
                                            dest.close();
                                        }
                                        if (is != null) {
                                            is.close();
                                        }
                                        if (null == null) {
                                            if (zipFile != null) {
                                                zipFile.close();
                                            }
                                            if (zipFile != null) {
                                                zipFile.close();
                                            }
                                            zipfile = zipFile;
                                            return false;
                                        }
                                        if (needChmod) {
                                            try {
                                                if (!RootCmdUtils.runCommand("chmod 666 " + file.getAbsolutePath() + "\n")) {
                                                    Log.d(TAG, "no permission");
                                                }
                                                if (!isUnzipSuccess) {
                                                    if (zipFile != null) {
                                                        zipFile.close();
                                                    }
                                                    if (zipFile != null) {
                                                        zipFile.close();
                                                    }
                                                    zipfile = zipFile;
                                                    return false;
                                                }
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                                isUnzipSuccess = false;
                                                if (null == null) {
                                                    if (zipFile != null) {
                                                        zipFile.close();
                                                    }
                                                }
                                            } catch (Throwable th2) {
                                                if (!isUnzipSuccess) {
                                                    if (zipFile != null) {
                                                        zipFile.close();
                                                    }
                                                }
                                            }
                                        }
                                        is2 = is;
                                        dest2 = dest;
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    dest = dest2;
                                }
                            } catch (Exception e7) {
                                e2 = e7;
                                is = is2;
                                dest = dest2;
                                e2.printStackTrace();
                                isUnzipSuccess = false;
                                if (dest != null) {
                                    dest.flush();
                                    dest.close();
                                }
                                if (is != null) {
                                    is.close();
                                }
                                if (null == null) {
                                    if (zipFile != null) {
                                        zipFile.close();
                                    }
                                    if (zipFile != null) {
                                        zipFile.close();
                                    }
                                    zipfile = zipFile;
                                    return false;
                                }
                                if (needChmod) {
                                    if (RootCmdUtils.runCommand("chmod 666 " + file.getAbsolutePath() + "\n")) {
                                        Log.d(TAG, "no permission");
                                    }
                                    if (isUnzipSuccess) {
                                        if (zipFile != null) {
                                            zipFile.close();
                                        }
                                        if (zipFile != null) {
                                            zipFile.close();
                                        }
                                        zipfile = zipFile;
                                        return false;
                                    }
                                }
                                is2 = is;
                                dest2 = dest;
                            } catch (Throwable th5) {
                                th = th5;
                                is = is2;
                                dest = dest2;
                            }
                            if (needChmod) {
                                if (RootCmdUtils.runCommand("chmod 666 " + file.getAbsolutePath() + "\n")) {
                                    Log.d(TAG, "no permission");
                                }
                                if (isUnzipSuccess) {
                                    if (zipFile != null) {
                                        zipFile.close();
                                    }
                                    if (zipFile != null) {
                                        zipFile.close();
                                    }
                                    zipfile = zipFile;
                                    return false;
                                }
                            }
                            is2 = is;
                            dest2 = dest;
                        } else if (!file.exists()) {
                            file.mkdirs();
                        }
                    } catch (Exception e8) {
                        e = e8;
                        is = is2;
                        dest = dest2;
                        zipfile = zipFile;
                    } catch (Throwable th6) {
                        th = th6;
                        is = is2;
                        zipfile = zipFile;
                    }
                }
                if (zipFile != null) {
                    try {
                        zipFile.close();
                        is = is2;
                        zipfile = zipFile;
                    } catch (IOException e52) {
                        e52.printStackTrace();
                        is = is2;
                        zipfile = zipFile;
                        return false;
                    }
                }
                zipfile = zipFile;
            } catch (Exception e9) {
                e = e9;
                zipfile = zipFile;
            } catch (Throwable th7) {
                th = th7;
                zipfile = zipFile;
            }
        } catch (Exception e10) {
            e = e10;
            try {
                e.printStackTrace();
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e522) {
                        e522.printStackTrace();
                        return false;
                    }
                }
                Log.d(TAG, "UnzipUsingZipFile endTime---" + System.currentTimeMillis());
                return true;
            } catch (Throwable th8) {
                th = th8;
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e5222) {
                        e5222.printStackTrace();
                        return false;
                    }
                }
                throw th;
            }
        }
        Log.d(TAG, "UnzipUsingZipFile endTime---" + System.currentTimeMillis());
        return true;
        if (dest != null) {
            dest.flush();
            dest.close();
        }
        if (is != null) {
            is.close();
        }
        if (isUnzipSuccess) {
            throw th;
        } else {
            if (zipFile != null) {
                zipFile.close();
            }
            if (zipFile != null) {
                zipFile.close();
            }
            zipfile = zipFile;
            return false;
        }
    }
}
