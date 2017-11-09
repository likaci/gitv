package com.gala.sdk.plugin.server.utils;

import android.content.Context;
import android.webkit.URLUtil;
import com.gala.sdk.plugin.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    static final int ERROR = -1;
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final int IS_DIRECOTORY = 3;
    static final int IS_FILE = 2;
    private static final String LIB_FOLDER = "plugin_libs";
    public static final String OPEN_PLUGIN_BALANCE = "openbalance";
    public static final String OPEN_PLUGIN_BALANCE_OFF = "1";
    public static final String OPEN_PLUGIN_BALANCE_ON = "0";
    private static final String PLUGIN_FOLDER = "plugin_apk";
    private static final String TAG = "FileUtils";
    public static long sDelaySumTime = 0;
    public static boolean sOpenBalance = true;

    public static boolean exists(String filePath) {
        if (Util.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    public static boolean isFolderEmpty(String filePath) {
        if (Util.isEmpty(filePath)) {
            return true;
        }
        boolean empty = true;
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            String[] paths = file.list();
            empty = paths == null || paths.length == 0;
        }
        return empty;
    }

    public static int getFileCount(String folder, FileFilter filter) {
        if (Util.isEmpty(folder)) {
            return 0;
        }
        int count = 0;
        File file = new File(folder);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles(filter);
            count = files == null ? 0 : files.length;
        }
        if (Log.DEBUG) {
            Log.v(TAG, "getFileCount() folder=" + folder + ", return count=" + count);
        }
        return count;
    }

    public static boolean deleteFile(String filePath) {
        if (Util.isEmpty(filePath)) {
            return false;
        }
        return deleteDir(new File(filePath));
    }

    public static List<String> deleteExportFile(String exportPath) {
        List<String> deleteList = new ArrayList();
        if (!Util.isEmpty(exportPath)) {
            File export = new File(exportPath);
            File parent = export.getParentFile();
            if (parent != null) {
                if (Log.VERBOSE) {
                    Log.v(TAG, "deleteExportFile() export.getName()" + export.getName() + "parent= " + parent.getName());
                }
                String[] children = parent.list();
                if (children != null) {
                    for (int i = 0; i < children.length; i++) {
                        if (Log.VERBOSE) {
                            Log.v(TAG, "deleteExportFile() scanFile=" + children[i]);
                        }
                        if (!Util.equals(export.getName(), children[i])) {
                            if (Log.VERBOSE) {
                                Log.v(TAG, "deleteExportFile() deleteDir=" + children[i]);
                            }
                            deleteList.add(children[i]);
                            deleteDir(new File(parent, children[i]));
                        }
                    }
                } else if (Log.VERBOSE) {
                    Log.e(TAG, "deleteExportFile() children is empty");
                }
            } else if (Log.VERBOSE) {
                Log.e(TAG, "deleteExportFile() getParentFile is empty");
            }
        } else if (Log.VERBOSE) {
            Log.e(TAG, "deleteExportFile() exportPath is empty");
        }
        return deleteList;
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                int i = 0;
                while (i < children.length) {
                    if (deleteDir(new File(dir, children[i]))) {
                        i++;
                    } else if (!Log.VERBOSE) {
                        return false;
                    } else {
                        Log.v(TAG, "deleteDir(" + children[i] + ") return=" + false);
                        return false;
                    }
                }
            }
        }
        boolean success = dir.delete();
        if (Log.VERBOSE) {
            Log.v(TAG, "deleteDir() " + getFileName(dir.getAbsolutePath()) + "  return=" + success);
        }
        return success;
    }

    public static boolean writeFile(InputStream inputStream, String soPath, Context context) throws Throwable {
        Throwable e;
        Throwable th;
        if (!createNewFile(soPath, context)) {
            return false;
        }
        FileOutputStream outputStream = null;
        try {
            FileOutputStream outputStream2 = new FileOutputStream(soPath);
            try {
                byte[] buffer = new byte[1024];
                int sleepSize = 0;
                int sleepSumTime = 0;
                while (true) {
                    int count = inputStream.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    outputStream2.write(buffer, 0, count);
                    sleepSize++;
                    if (sleepSize == 512) {
                        int sleeptime = CpuLoadBalance.getInstance().getSleepTime();
                        Thread.sleep((long) sleeptime);
                        sleepSumTime += sleeptime;
                        sleepSize = 0;
                    }
                }
                sDelaySumTime += (long) sleepSumTime;
                if (Log.DEBUG) {
                    Log.d(TAG, "writeFile sleepSumtime " + sleepSumTime + "ms");
                }
                outputStream2.flush();
                closeStream(outputStream2);
                return true;
            } catch (Throwable th2) {
                th = th2;
                outputStream = outputStream2;
                closeStream(outputStream);
                throw th;
            }
        } catch (Throwable th3) {
            e = th3;
            throw e;
        }
    }

    public static boolean copyFromAssets(Context context, String assestsPath, String targetPath) throws Throwable {
        Throwable e;
        Throwable th;
        if (Log.DEBUG) {
            Log.d(TAG, "copyFromAssets(assetsPath=" + assestsPath + ", targetPath=" + targetPath + ")");
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        createNewFile(targetPath, context);
        try {
            inputStream = context.getAssets().open(assestsPath);
            FileOutputStream outputStream2 = new FileOutputStream(targetPath);
            try {
                byte[] buffer = new byte[1024];
                int sleepSize = 0;
                int sleepSumtime = 0;
                sDelaySumTime = 0;
                while (true) {
                    int size = inputStream.read(buffer);
                    if (size == -1) {
                        break;
                    }
                    outputStream2.write(buffer, 0, size);
                    sleepSize++;
                    if (sleepSize == 512) {
                        int sleeptime = CpuLoadBalance.getInstance().getSleepTime();
                        Thread.sleep((long) sleeptime);
                        sleepSumtime += sleeptime;
                        if (Log.DEBUG) {
                            Log.d(TAG, "copyFromAssets sleep " + sleeptime + "ms");
                        }
                        sleepSize = 0;
                    }
                }
                sDelaySumTime += (long) sleepSumtime;
                if (Log.DEBUG) {
                    Log.d(TAG, "copyFromAssets sleepSumtime " + sleepSumtime + "ms");
                }
                outputStream2.flush();
                closeStream(inputStream);
                closeStream(outputStream2);
                if (Log.DEBUG) {
                    Log.d(TAG, "copyFromAssets(return " + true + ")");
                }
                return true;
            } catch (Throwable th2) {
                th = th2;
                outputStream = outputStream2;
                closeStream(inputStream);
                closeStream(outputStream);
                throw th;
            }
        } catch (Throwable th3) {
            e = th3;
            throw e;
        }
    }

    private static boolean createNewDirectory(File file) {
        Log.d(TAG, "createNewDirectory()");
        boolean success = file.mkdirs();
        if (file.exists()) {
            Log.d(TAG, "createNewDirectory(), path already exists!");
            return true;
        }
        Log.d(TAG, "createNewDirectory() mkdirs fail");
        return success;
    }

    private static boolean createNewFile(String filePath, File file) throws Throwable {
        Log.d(TAG, "createNewFile()");
        File parent = file.getParentFile();
        if (!(parent == null || parent.exists())) {
            Log.d(TAG, "createNewFile(), path parent is not exists!" + file.getParentFile().mkdirs());
        }
        if (file.createNewFile()) {
            Log.d(TAG, "createNewFile() success" + true);
            return true;
        } else if (file.canRead() && file.canWrite()) {
            exist = file.exists();
            deleted = false;
            if (exist) {
                deleted = file.delete();
            }
            success = file.createNewFile();
            Log.d(TAG, "createNewFile() exist? " + exist + " delete-success? " + deleted + " and create=" + success);
            return success;
        } else {
            Log.d(TAG, "createNewFile() file can not write ?/read=" + file.canRead() + "/write=" + file.canWrite() + "/execute=" + file.canExecute());
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            Log.d(TAG, "createNewFile() file can write ?/read=" + file.canRead() + "/write=" + file.canWrite() + "/execute=" + file.canExecute());
            exist = file.exists();
            deleted = false;
            if (exist) {
                deleted = file.delete();
            }
            success = file.createNewFile();
            Log.d(TAG, "createNewFile() exist? " + exist + " delete-success? " + deleted + " and create=" + success);
            return success;
        }
    }

    public static int fileIsDirectory(String filename) {
        if (Util.isEmpty(filename)) {
            return -1;
        }
        int dot = filename.lastIndexOf(47);
        if (dot <= -1 || dot >= filename.length() - 1) {
            return -1;
        }
        if (filename.substring(dot + 1).contains(".")) {
            return 2;
        }
        return 3;
    }

    public static boolean createNewFile(String filePath, Context context) throws Throwable {
        Log.d(TAG, "createNewFile(), path=" + filePath);
        boolean success = false;
        if (Util.isEmpty(filePath)) {
            Log.d(TAG, "empty filePath");
        } else {
            File file = new File(filePath);
            if (fileIsDirectory(filePath) == 3) {
                success = createNewDirectory(file);
                Log.d(TAG, "createNewDirectory()->" + success);
            } else if (fileIsDirectory(filePath) == 2) {
                success = createNewFile(filePath, file);
                Log.d(TAG, "createNewFile()->" + success);
            } else {
                Log.d(TAG, "createNewFile() what's is this Path is->" + filePath);
                Log.d(TAG, "createNewFile() what's is this File is->" + file);
                Log.d(TAG, "createNewFile() what's is this context is->" + context);
            }
        }
        Log.d(TAG, "createNewFile(), final return->" + success);
        return success;
    }

    public static String appendFileName(String filePath, String append) {
        String targetPath = filePath;
        if (Util.isEmpty(filePath) || Util.isEmpty(append)) {
            return targetPath;
        }
        File file = new File(filePath);
        String fileName = file.getName();
        return file.getParentFile().getAbsolutePath() + File.separator + getFileNameNoEx(fileName) + append + "." + getExtensionName(fileName);
    }

    public static String getExtensionName(String filename) {
        if (Util.isEmpty(filename)) {
            return filename;
        }
        int dot = filename.lastIndexOf(46);
        if (dot <= -1 || dot >= filename.length() - 1) {
            return filename;
        }
        return filename.substring(dot + 1);
    }

    public static String getFileNameNoEx(String filename) {
        if (Util.isEmpty(filename)) {
            return filename;
        }
        int dot = filename.lastIndexOf(46);
        if (dot <= -1 || dot >= filename.length()) {
            return filename;
        }
        return filename.substring(0, dot);
    }

    public static String getURLFileName(String url) {
        if (Log.VERBOSE) {
            Log.v(TAG, "getFileName<<(url=" + url + ")");
        }
        String fileName = null;
        if (URLUtil.isValidUrl(url)) {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "getFileName>>() return " + fileName);
        }
        return fileName;
    }

    public static String getFileName(String path) {
        if (Util.isEmpty(path)) {
            return null;
        }
        return path.substring(path.lastIndexOf("/"));
    }

    public static String toFilePath(String rootPath, String... folderName) {
        if (Log.VERBOSE) {
            Log.v(TAG, "toFilePath<<(rootPath=" + rootPath + ", folderName=" + folderName + ")");
        }
        String filePath = rootPath;
        if (!Util.isEmpty(filePath)) {
            for (String name : folderName) {
                if (!Util.isEmpty(name)) {
                    filePath = filePath + File.separator + name;
                }
            }
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "toFilePath>>() return " + filePath);
        }
        return filePath;
    }

    public static boolean renameFile(String sourceFilePath, String targetFilePath) {
        if (!(Util.isEmpty(sourceFilePath) || Util.isEmpty(targetFilePath))) {
            File targetFile = new File(targetFilePath);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            boolean success = new File(sourceFilePath).renameTo(targetFile);
            if (Log.VERBOSE) {
                Log.v(TAG, "renameFile" + success);
            }
        }
        return true;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String md5(String filename) {
        MessageDigest md5;
        Throwable th;
        InputStream inputStream = null;
        byte[] buffer = new byte[1024];
        try {
            InputStream fis = new FileInputStream(filename);
            try {
                md5 = MessageDigest.getInstance("MD5");
                while (true) {
                    int numRead = fis.read(buffer);
                    if (numRead <= 0) {
                        break;
                    }
                    md5.update(buffer, 0, numRead);
                }
                closeStream(fis);
                inputStream = fis;
            } catch (Throwable th2) {
                th = th2;
                inputStream = fis;
                closeStream(inputStream);
                throw th;
            }
        } catch (Throwable th3) {
            System.out.println("md5 error  !!!");
            md5 = null;
            closeStream(inputStream);
            if (md5 == null) {
            }
        }
        if (md5 == null) {
        }
    }

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "closeStream exception!!");
            }
        }
    }
}
