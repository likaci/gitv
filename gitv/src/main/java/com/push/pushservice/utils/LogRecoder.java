package com.push.pushservice.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import com.tvos.apps.utils.DateUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogRecoder {
    private static String END_TAG = ".txt";
    private static File LOG_DIR = null;
    private static final String LOG_FILE_NAME = "PushServiceLog";
    private static String START_TAG = "pushsdk_";
    private static final String TAG = "LogRecoder";
    private static LogRecoder instance = null;
    private static SimpleDateFormat sdfItem = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINESE);
    private static SimpleDateFormat sdfName = new SimpleDateFormat(DateUtil.PATTERN_STANDARD14W, Locale.CHINESE);

    private LogRecoder() {
    }

    @SuppressLint({"NewApi"})
    public static LogRecoder getInstance(Context context) {
        Log.i(TAG, "初始化");
        try {
            if (instance == null) {
                Log.i(TAG, "初始化 instance is null");
                if (context == null) {
                    return null;
                }
                Log.i(TAG, "初始化 context is not null");
                if (!initLogFile(context)) {
                    return null;
                }
                instance = new LogRecoder();
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean initLogFile(Context context) {
        if (context == null) {
            return false;
        }
        File privatePath = context.getExternalFilesDir(null);
        if (privatePath == null) {
            return false;
        }
        LOG_DIR = new File(privatePath.getAbsolutePath() + File.separator + LOG_FILE_NAME);
        if (LOG_DIR.exists() || LOG_DIR.mkdirs()) {
            return true;
        }
        Log.e(TAG, "Fail to create LOG_DIR");
        LOG_DIR = null;
        return false;
    }

    @SuppressLint({"NewApi"})
    private static String checkFreeSize(String root) {
        File mSDFile = new File(root);
        if (mSDFile != null && mSDFile.exists()) {
            try {
                StatFs e = new StatFs(mSDFile.getPath());
                long free_size = (((VERSION.SDK_INT < 18 ? (long) e.getBlockSize() : e.getBlockSizeLong()) * (VERSION.SDK_INT < 18 ? (long) e.getAvailableBlocks() : e.getAvailableBlocksLong())) / 1024) / 1024;
                Log.e("pushService", "根目录剩余空间为：" + free_size);
                if (free_size < 100) {
                    return "根目录存储空间不足100M，请清空冗余数据";
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }

    private static FileWriter getWriter() throws Exception {
        File file = getCurrentLogFile();
        if (file == null) {
            return null;
        }
        return new FileWriter(file, true);
    }

    private static File getCurrentLogFile() throws Exception {
        if (LOG_DIR == null) {
            return null;
        }
        if (!(LOG_DIR == null || LOG_DIR.exists())) {
            LOG_DIR.mkdirs();
        }
        File[] listFiles = LOG_DIR.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return createNewLogFile();
        }
        File log = checkValidLogFile(listFiles);
        if (log == null) {
            return createNewLogFile();
        }
        return log;
    }

    private static File checkValidLogFile(File[] listFiles) throws IOException {
        File log = null;
        int i = 0;
        while (i < listFiles.length) {
            if (listFiles[i] != null && listFiles[i].exists() && listFiles[i].isFile()) {
                long lastModified = listFiles[i].lastModified();
                long deltDays = PushUtils.getDeltDays(lastModified);
                if ((deltDays > 2 || deltDays < 0) && listFiles.length > 3) {
                    listFiles[i].delete();
                } else if (deltDays == 0) {
                    if (log == null) {
                        log = listFiles[i];
                    } else if (lastModified > log.lastModified()) {
                        log = listFiles[i];
                    }
                }
            }
            i++;
        }
        return log;
    }

    private static File createNewLogFile() throws IOException {
        return new File(LOG_DIR, START_TAG + sdfName.format(new Date()) + END_TAG);
    }

    public static void save(String log) {
        try {
            if (instance == null || LOG_DIR == null || !TextUtils.equals(Environment.getExternalStorageState(), "mounted")) {
                Log.e(TAG, "请先初始化 log = " + log);
                return;
            }
            FileWriter fileWrite = null;
            try {
                fileWrite = getWriter();
                fileWrite.append("==").append(sdfItem.format(new Date())).append("==\n");
                if (log != null) {
                    fileWrite.append(log).append("\n");
                }
                fileWrite.append("\n");
                fileWrite.flush();
                if (fileWrite != null) {
                    try {
                        fileWrite.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (fileWrite != null) {
                    try {
                        fileWrite.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (fileWrite != null) {
                    try {
                        fileWrite.close();
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
            }
        } catch (Exception e2222) {
            e2222.printStackTrace();
        }
    }
}
