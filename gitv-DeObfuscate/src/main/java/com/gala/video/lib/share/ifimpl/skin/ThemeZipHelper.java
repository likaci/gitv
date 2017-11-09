package com.gala.video.lib.share.ifimpl.skin;

import android.util.Log;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper.BackgroundType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ThemeZipHelper implements IThemeZipHelper {
    private static final String PREFERENCE_NAME = "zip_path";
    private static final String PREV_VERSION = "prev_version";
    private static final String TAG = "ThemeZipHelper";
    private static final ThemeZipHelper mInstance = new ThemeZipHelper();
    private static final String sOutPathString_A = "theme_a";
    private static final String sOutPathString_B = "theme_b";
    private static final String sPath = "path";
    private static final String sShouldCopy = "copy";
    private static final String sState = "state";
    private Object mLock = new Object();

    class C17171 implements Runnable {
        C17171() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r8 = this;
            r2 = com.gala.video.lib.share.ifimpl.skin.ThemeZipHelper.this;
            r3 = r2.mLock;
            monitor-enter(r3);
            r2 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ all -> 0x00b5 }
            r2 = r2.getApplicationContext();	 Catch:{ all -> 0x00b5 }
            r4 = "zip_path";
            r0 = com.gala.video.lib.share.system.preference.AppPreference.get(r2, r4);	 Catch:{ all -> 0x00b5 }
            r2 = "path";
            r4 = "";
            r1 = r0.get(r2, r4);	 Catch:{ all -> 0x00b5 }
            r2 = com.gala.video.lib.framework.core.utils.StringUtils.isEmpty(r1);	 Catch:{ all -> 0x00b5 }
            if (r2 == 0) goto L_0x0028;
        L_0x0026:
            monitor-exit(r3);	 Catch:{ all -> 0x00b5 }
        L_0x0027:
            return;
        L_0x0028:
            r2 = "ThemeZipHelper";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b5 }
            r4.<init>();	 Catch:{ all -> 0x00b5 }
            r5 = "state = ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00b5 }
            r5 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ all -> 0x00b5 }
            r5 = r5.getApplicationContext();	 Catch:{ all -> 0x00b5 }
            r6 = "zip_path";
            r5 = com.gala.video.lib.share.system.preference.AppPreference.get(r5, r6);	 Catch:{ all -> 0x00b5 }
            r6 = "state";
            r7 = 1;
            r5 = r5.getBoolean(r6, r7);	 Catch:{ all -> 0x00b5 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00b5 }
            r4 = r4.toString();	 Catch:{ all -> 0x00b5 }
            com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r2, r4);	 Catch:{ all -> 0x00b5 }
            r2 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ all -> 0x00b5 }
            r2 = r2.getApplicationContext();	 Catch:{ all -> 0x00b5 }
            r4 = "zip_path";
            r2 = com.gala.video.lib.share.system.preference.AppPreference.get(r2, r4);	 Catch:{ all -> 0x00b5 }
            r4 = "state";
            r5 = 1;
            r2 = r2.getBoolean(r4, r5);	 Catch:{ all -> 0x00b5 }
            if (r2 != 0) goto L_0x00b2;
        L_0x0072:
            r2 = com.gala.video.lib.share.ifimpl.skin.ThemeZipHelper.this;	 Catch:{ all -> 0x00b5 }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b5 }
            r4.<init>();	 Catch:{ all -> 0x00b5 }
            r5 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ all -> 0x00b5 }
            r5 = r5.getApplicationContext();	 Catch:{ all -> 0x00b5 }
            r5 = r5.getFilesDir();	 Catch:{ all -> 0x00b5 }
            r5 = r5.getAbsolutePath();	 Catch:{ all -> 0x00b5 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00b5 }
            r5 = "/";
            r4 = r4.append(r5);	 Catch:{ all -> 0x00b5 }
            r5 = com.gala.video.lib.share.ifimpl.skin.ThemeZipHelper.this;	 Catch:{ all -> 0x00b5 }
            r5 = r5.unZipPath();	 Catch:{ all -> 0x00b5 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x00b5 }
            r4 = r4.toString();	 Catch:{ all -> 0x00b5 }
            r2.deleteFolder(r4);	 Catch:{ all -> 0x00b5 }
            r2 = com.gala.video.lib.share.ifimpl.skin.ThemeZipHelper.this;	 Catch:{ all -> 0x00b5 }
            r4 = com.gala.video.lib.share.ifmanager.GetInterfaceTools.getIThemeProvider();	 Catch:{ all -> 0x00b5 }
            r4 = r4.getDayThemeSourcePath();	 Catch:{ all -> 0x00b5 }
            r2.unZipFile(r4);	 Catch:{ all -> 0x00b5 }
        L_0x00b2:
            monitor-exit(r3);	 Catch:{ all -> 0x00b5 }
            goto L_0x0027;
        L_0x00b5:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x00b5 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.skin.ThemeZipHelper.1.run():void");
        }
    }

    public ThemeZipHelper() {
        AppPreference ap = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME);
        if (ap.getBoolean("copy", false)) {
            LogUtils.m1568d(TAG, "copy day/night");
            copyFile(SystemConfigPreference.getDayModeBackground(AppRuntimeEnv.get().getApplicationContext()));
            copyFile(SystemConfigPreference.getNightModeBackground(AppRuntimeEnv.get().getApplicationContext()));
            ap.save("copy", false);
        }
    }

    public static ThemeZipHelper getInstance() {
        return mInstance;
    }

    public boolean isVersionChanged() {
        String prevVersion = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(PREV_VERSION, "");
        String currVersion = Project.getInstance().getBuild().getVersionString();
        LogUtils.m1568d(TAG, "prevVersion=" + prevVersion + ",currVersion=" + currVersion);
        return !prevVersion.equals(currVersion);
    }

    public void unZipFile(String zipFilePath) {
        if (StringUtils.isEmpty((CharSequence) zipFilePath)) {
            Log.d(TAG, "zip file path is empty");
            return;
        }
        File file = new File(zipFilePath);
        if (file == null || !file.exists()) {
            LogUtils.m1571e(TAG, "skin zip do not exist!file=" + zipFilePath);
            GetInterfaceTools.getIThemeProvider().resetDayTheme();
            return;
        }
        synchronized (this.mLock) {
            unZipFile(file, unZipPath());
        }
    }

    private int unZipFile(File zipFile, String desPath) {
        Log.d(TAG, "desPath = " + desPath);
        AppPreference ap = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME);
        ap.save("state", false);
        String path = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + "/";
        deleteFolder(path + desPath);
        try {
            ZipFile file = new ZipFile(zipFile);
            Enumeration zList = file.entries();
            byte[] buf = new byte[1024];
            File baseDir = new File(path + desPath);
            Log.d(TAG, "baseDir = " + baseDir);
            if (!baseDir.exists()) {
                baseDir.mkdir();
            }
            while (zList.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    Log.d(TAG, "folder name = " + ze.getName());
                    String dirString = new String((path + desPath + "/" + ze.getName()).getBytes("8859_1"), "GB2312");
                    Log.d(TAG, "dirString = " + dirString);
                    File f = new File(dirString);
                    if (!f.exists()) {
                        f.mkdir();
                    }
                } else {
                    Log.d(TAG, "file name = " + ze.getName());
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(path + desPath, ze.getName())));
                    InputStream is = new BufferedInputStream(file.getInputStream(ze));
                    while (true) {
                        int readLen = is.read(buf, 0, 1024);
                        if (readLen == -1) {
                            break;
                        }
                        os.write(buf, 0, readLen);
                    }
                    is.close();
                    os.close();
                }
            }
            file.close();
            ap.save("copy", true);
            if (ap.getBoolean("copy", false)) {
                LogUtils.m1568d(TAG, "copy day/night");
                copyFile(SystemConfigPreference.getDayModeBackground(AppRuntimeEnv.get().getApplicationContext()));
                copyFile(SystemConfigPreference.getNightModeBackground(AppRuntimeEnv.get().getApplicationContext()));
                ap.save("copy", false);
            }
            Log.d(TAG, "file copy success");
            ap.save(sPath, path + desPath);
            ap.save("state", true);
            ap.save(PREV_VERSION, Project.getInstance().getBuild().getVersionString());
            Log.d(TAG, "unzip file success");
        } catch (Exception e) {
            Log.e(TAG, "unzip file exception = ", e);
        }
        return 0;
    }

    private void copyFile(String fileName) {
        Log.d(TAG, "copy file name = " + fileName);
        if (!StringUtils.isEmpty((CharSequence) fileName)) {
            String desPath = unZipPath();
            Log.d(TAG, "copy file to path = " + desPath);
            AppPreference ap = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME);
            String path = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + "/";
            if (desPath.equals(sOutPathString_A)) {
                if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_DAY)) {
                    Log.d(TAG, "copy from b to a day");
                    if (new File(path + sOutPathString_B + "/day/" + fileName).exists()) {
                        copy(path + sOutPathString_B + "/day/" + fileName, path + sOutPathString_A + "/day/" + fileName);
                    }
                    if (new File(path + sOutPathString_B + "/day_thumbnail/" + fileName).exists()) {
                        copy(path + sOutPathString_B + "/day_thumbnail/" + fileName, path + sOutPathString_A + "/day_thumbnail/" + fileName);
                    }
                } else if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_NIGHT)) {
                    Log.d(TAG, "copy from b to a night");
                    if (new File(path + sOutPathString_B + "/night/" + fileName).exists()) {
                        copy(path + sOutPathString_B + "/night/" + fileName, path + sOutPathString_A + "/night/" + fileName);
                    }
                    if (new File(path + sOutPathString_B + "/night_thumbnail/" + fileName).exists()) {
                        copy(path + sOutPathString_B + "/night_thumbnail/" + fileName, path + sOutPathString_A + "/night_thumbnail/" + fileName);
                    }
                }
            } else if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_NIGHT)) {
                Log.d(TAG, "copy from a to b night");
                if (new File(path + sOutPathString_A + "/night/" + fileName).exists()) {
                    copy(path + sOutPathString_A + "/night/" + fileName, path + sOutPathString_B + "/night/" + fileName);
                }
                if (new File(path + sOutPathString_A + "/night_thumbnail/" + fileName).exists()) {
                    copy(path + sOutPathString_A + "/night_thumbnail/" + fileName, path + sOutPathString_B + "/night_thumbnail/" + fileName);
                }
            } else if (fileName.startsWith(SystemConfigPreference.SETTING_BACKGROUND_DAY)) {
                Log.d(TAG, "copy from a to b day");
                if (new File(path + sOutPathString_A + "/day/" + fileName).exists()) {
                    copy(path + sOutPathString_A + "/day/" + fileName, path + sOutPathString_B + "/day/" + fileName);
                }
                if (new File(path + sOutPathString_A + "/day_thumbnail/" + fileName).exists()) {
                    copy(path + sOutPathString_A + "/day_thumbnail/" + fileName, path + sOutPathString_B + "/day_thumbnail/" + fileName);
                }
            }
        }
    }

    private File getRealFileName(String baseDir, String absFileName) {
        Log.d(TAG, "baseDir = " + baseDir);
        Log.d(TAG, "absFileName = " + absFileName);
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir + "/";
        if (dirs.length <= 1) {
            return new File(baseDir, absFileName);
        }
        for (int i = 0; i < dirs.length - 1; i++) {
            lastDir = lastDir + dirs[i] + "/";
            File dir = new File(lastDir);
            if (!dir.exists()) {
                dir.mkdirs();
                Log.d(TAG, "create dir = " + lastDir + "/" + dirs[i]);
            }
        }
        File ret = new File(lastDir, dirs[dirs.length - 1]);
        Log.d(TAG, "ret = " + ret);
        return ret;
    }

    public void init() {
        new Thread8K(new C17171(), TAG).start();
    }

    private boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    private boolean deleteDirectory(String filePath) {
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if (files == null) {
            return true;
        }
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isFile()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (flag) {
            return dirFile.delete();
        }
        return false;
    }

    private boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return deleteFile(filePath);
        }
        return deleteDirectory(filePath);
    }

    private void copy(String oldPath, String newPath) {
        FileOutputStream fs;
        Exception e;
        Throwable th;
        Log.d(TAG, "oldPath/newPath = " + oldPath + "/" + newPath);
        InputStream inStream = null;
        FileOutputStream fs2 = null;
        int byteSum = 0;
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream2 = new FileInputStream(oldPath);
                try {
                    fs = new FileOutputStream(newPath);
                } catch (Exception e2) {
                    e = e2;
                    inStream = inStream2;
                    try {
                        Log.e(TAG, "copy file exception = ", e);
                        if (fs2 != null) {
                            try {
                                fs2.flush();
                                fs2.close();
                            } catch (IOException e3) {
                                LogUtils.m1572e(TAG, "copy", e3);
                                return;
                            }
                        }
                        if (inStream == null) {
                            inStream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fs2 != null) {
                            try {
                                fs2.flush();
                                fs2.close();
                            } catch (IOException e32) {
                                LogUtils.m1572e(TAG, "copy", e32);
                                throw th;
                            }
                        }
                        if (inStream != null) {
                            inStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inStream = inStream2;
                    if (fs2 != null) {
                        fs2.flush();
                        fs2.close();
                    }
                    if (inStream != null) {
                        inStream.close();
                    }
                    throw th;
                }
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int byteRead = inStream2.read(buffer);
                        if (byteRead == -1) {
                            break;
                        }
                        byteSum += byteRead;
                        fs.write(buffer, 0, byteRead);
                    }
                    fs2 = fs;
                    inStream = inStream2;
                } catch (Exception e4) {
                    e = e4;
                    fs2 = fs;
                    inStream = inStream2;
                    Log.e(TAG, "copy file exception = ", e);
                    if (fs2 != null) {
                        fs2.flush();
                        fs2.close();
                    }
                    if (inStream == null) {
                        inStream.close();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fs2 = fs;
                    inStream = inStream2;
                    if (fs2 != null) {
                        fs2.flush();
                        fs2.close();
                    }
                    if (inStream != null) {
                        inStream.close();
                    }
                    throw th;
                }
            }
            if (fs2 != null) {
                try {
                    fs2.flush();
                    fs2.close();
                } catch (IOException e322) {
                    LogUtils.m1572e(TAG, "copy", e322);
                    return;
                }
            }
            if (inStream != null) {
                inStream.close();
            }
        } catch (Exception e5) {
            e = e5;
            Log.e(TAG, "copy file exception = ", e);
            if (fs2 != null) {
                fs2.flush();
                fs2.close();
            }
            if (inStream == null) {
                inStream.close();
            }
        }
    }

    private String unZipPath() {
        String pa = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath, "");
        String absolutePath = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + "/";
        if ((absolutePath + sOutPathString_A).equals(pa)) {
            return sOutPathString_B;
        }
        if ((absolutePath + sOutPathString_B).equals(pa)) {
            return sOutPathString_A;
        }
        return sOutPathString_A;
    }

    public boolean hasThemeZip() {
        if (StringUtils.isEmpty(AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath, ""))) {
            return false;
        }
        return true;
    }

    public String getBackground(BackgroundType type) {
        String path = "";
        switch (type) {
            case DAY_THUMB:
                path = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath) + "/day_thumbnail/";
                break;
            case DAY_BACKGROUND:
                path = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath) + "/day/";
                break;
            case NIGHT_THUMB:
                path = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath) + "/night_thumbnail/";
                break;
            case NIGHT_BACKGROUND:
                path = AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath) + "/night/";
                break;
        }
        Log.d(TAG, "background path = " + path);
        return path;
    }

    public String getSkinZip() {
        return AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).get(sPath) + "/day.zip";
    }

    public void reset() {
        LogUtils.m1568d(TAG, LoginConstant.CLICK_RESEAT_CHANGE_PASSWORD);
        AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), PREFERENCE_NAME).clear();
        String filesPath = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath();
        final String themeA = filesPath + File.separator + sOutPathString_A;
        final String themeB = filesPath + File.separator + sOutPathString_B;
        new Thread8K(new Runnable() {
            public void run() {
                ThemeZipHelper.this.deleteDirectory(themeA);
                ThemeZipHelper.this.deleteDirectory(themeB);
            }
        }, TAG).start();
    }
}
