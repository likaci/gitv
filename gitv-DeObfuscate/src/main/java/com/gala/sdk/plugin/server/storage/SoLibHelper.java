package com.gala.sdk.plugin.server.storage;

import android.content.Context;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class SoLibHelper {
    private static final String SO_PREFIX = "lib";
    private static final String SO_SUFFIX = ".so";
    private static final String TAG = "SoLibHelper";

    SoLibHelper() {
    }

    public static boolean copySoLibToHost(Context context, PluginInfo info) throws Throwable {
        Throwable exception;
        Throwable th;
        boolean success = true;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "copySoLibToHost(context=" + context + ", info=" + info + ")");
        }
        if (info == null) {
            return 1;
        }
        String pluginApkPath = info.getPath();
        if (FileUtils.exists(pluginApkPath)) {
            boolean containSo = false;
            int soFileCount = 0;
            ZipInputStream zipInputStream = null;
            try {
                ZipInputStream zin = new ZipInputStream(new FileInputStream(pluginApkPath));
                while (true) {
                    try {
                        ZipEntry entry = zin.getNextEntry();
                        if (entry == null) {
                            break;
                        }
                        if (Log.VERBOSE) {
                            Log.m434v(TAG, "copySoLibToHost scan(entry=" + entry.getName() + ", isDirectory=" + entry.isDirectory() + ")");
                        }
                        if (!entry.isDirectory()) {
                            String name = entry.getName();
                            String soName = name;
                            if (name.contains(File.separator)) {
                                soName = name.substring(name.lastIndexOf(File.separator) + 1);
                            }
                            if (Log.VERBOSE) {
                                Log.m434v(TAG, "copySoLibToHost scan(soName=" + soName + ")");
                            }
                            if (isSoFile(soName)) {
                                containSo = true;
                                soFileCount++;
                                if (FileUtils.writeFile(zin, info.getLibFolder() + File.separator + soName, context)) {
                                    success = true;
                                } else {
                                    success = false;
                                }
                                if (Log.VERBOSE) {
                                    Log.m434v(TAG, "copySoLibToHost copy(" + soName + ") " + success);
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        zipInputStream = zin;
                    }
                }
                if (zin != null) {
                    try {
                        zin.closeEntry();
                    } catch (IOException e) {
                        Log.m437w(TAG, "closeEntry() fail!", e);
                    }
                }
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "copySoLibToHost (containSo=" + containSo + ")");
                }
                info.setContainSoFile(containSo);
                info.setSoFileCount(soFileCount);
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "unzipLibs() return " + success);
                }
                return success;
            } catch (Throwable th3) {
                exception = th3;
                try {
                    Log.m437w(TAG, "unzipLibs() fail!", exception);
                    throw exception;
                } catch (Throwable th4) {
                    th = th4;
                    if (zipInputStream != null) {
                        try {
                            zipInputStream.closeEntry();
                        } catch (IOException e2) {
                            Log.m437w(TAG, "closeEntry() fail!", e2);
                        }
                    }
                    throw th;
                }
            }
        }
        throw new Exception("apk not copy success");
    }

    public static boolean isSoFile(String fileName) {
        return !Util.isEmpty(fileName) && fileName.endsWith(SO_SUFFIX) && fileName.startsWith(SO_PREFIX);
    }
}
