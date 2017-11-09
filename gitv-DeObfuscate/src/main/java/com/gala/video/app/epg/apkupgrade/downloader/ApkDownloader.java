package com.gala.video.app.epg.apkupgrade.downloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import com.gala.video.lib.framework.core.network.download.GalaDownloadCreator;
import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloadParameter;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloader;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.MD5Util;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.internal.net.SendFlag;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class ApkDownloader {
    private static final String API_URL_APK_DOWNLOAD_URL = "http://202.108.14.216/%s";
    private static final String APK_DOWNLOAD_SERVER = "http://202.108.14.216/";
    private static String GALA_CLIENT_APK_NAME = "GALAClient";
    private boolean DOWNLOAD_DEBUG = false;
    private final String TAG = "ApkDownloader";
    private ApkDownloadListener mApkDownloadListener;
    private Context mContext = null;
    IGalaDownloadListener mDownloadListener = new C05234();
    private IGalaDownloader mDownloader = GalaDownloadCreator.createFileDownloader();
    private int mLastProgress = 0;
    private AppVersion mVersion = null;

    public interface ApkCheckDownloadListener {
        void onDownloaded(boolean z);
    }

    public interface ApkDownloadListener {
        void onCancelled();

        void onError(int i);

        void onExist();

        void onFinish();

        void onProgress(int i);

        void onStart();
    }

    class C05234 implements IGalaDownloadListener {
        C05234() {
        }

        public void onStart() {
            LogUtils.m1568d("ApkDownloader", "onStart()");
            ApkDownloader.this._onStart();
        }

        public void onSuccess(Object file, String path) {
            LogUtils.m1568d("ApkDownloader", "onFinish()");
            ApkDownloader.this._onFinish();
        }

        public void onProgress(long download, long fileSize) {
            int progress = (int) ((100 * download) / fileSize);
            if (ApkDownloader.this.mLastProgress != progress) {
                LogUtils.m1568d("ApkDownloader", "onProgress()---" + progress);
            }
            ApkDownloader.this._onProgress(progress);
            ApkDownloader.this.mLastProgress = progress;
        }

        public void onError(GalaDownloadException e) {
            LogUtils.m1568d("ApkDownloader", "onError()---errorCode=" + e.getErrorCode());
            ApkDownloader.this.deleteDownloadFile();
            ApkDownloader.this._onError(e.getErrorCode());
        }

        public void onCancel() {
            LogUtils.m1568d("ApkDownloader", "onCancelled()");
            ApkDownloader.this.deleteDownloadFile();
            ApkDownloader.this._onCancelled();
        }

        public void onExisted(String path) {
            ApkDownloader.this._onExisted();
        }
    }

    public enum DownloadSpeed {
        HIGHEST,
        HIGHER,
        NORMAL,
        LOWER,
        LOWEST
    }

    public class FullPathResult {
        public int errorCode = 0;
        public String filePath = "";
    }

    public ApkDownloader(Context context) {
        this.mContext = context;
    }

    private String getApkNameFix() {
        return GALA_CLIENT_APK_NAME + "_" + Project.getInstance().getBuild().getPackageName();
    }

    public String getApkName(AppVersion version) {
        if (version == null) {
            return null;
        }
        if (this.DOWNLOAD_DEBUG) {
            return getApkNameFix() + "_" + String.valueOf(System.currentTimeMillis()) + ".apk";
        }
        return getApkNameFix() + "_" + version.getVersion() + ".apk";
    }

    @SuppressLint({"DefaultLocale"})
    private String checkApkUrl(String url) {
        String result = url;
        if (StringUtils.isEmpty((CharSequence) url)) {
            LogUtils.m1568d("ApkDownloader", "The url for the update APK is empty!");
            return "";
        }
        if (!url.toLowerCase().startsWith(WebConstants.WEB_SITE_BASE_HTTP)) {
            result = String.format(API_URL_APK_DOWNLOAD_URL, new Object[]{url});
        }
        return result;
    }

    private void deleteOldApks(String strDir, String apkName, final String exceptName) {
        if (!StringUtils.isEmpty((CharSequence) strDir) && !StringUtils.isEmpty((CharSequence) apkName)) {
            File fileDir = new File(strDir);
            if (fileDir.isDirectory() && fileDir.canWrite()) {
                File[] files = fileDir.listFiles(new FilenameFilter() {
                    @SuppressLint({"DefaultLocale"})
                    public boolean accept(File dir, String filename) {
                        if (new File(dir, filename).isDirectory() || filename.equals(exceptName)) {
                            return false;
                        }
                        return Pattern.matches("^.*" + Project.getInstance().getBuild().getPackageName().toLowerCase() + ".*" + "\\.apk$", filename.toLowerCase());
                    }
                });
                for (int i = 0; i < files.length; i++) {
                    if (files[i].exists()) {
                        LogUtils.m1568d("ApkDownloader", "deleteOldApks()---" + files[i].getAbsolutePath());
                        if (!files[i].delete()) {
                            LogUtils.m1571e("ApkDownloader", "deleteOldApks()---fail---" + files[i].getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private String getSdCardDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private String getMemoryDir() {
        return this.mContext.getFilesDir().getAbsolutePath();
    }

    private String getSdCardFilePath(String apkName) {
        return getSdCardDir() + "/" + apkName;
    }

    private String getMemoryFilePath(String apkName) {
        return getMemoryDir() + "/" + apkName;
    }

    @SuppressLint({"DefaultLocale"})
    private boolean checkMd5(String filePath, String md5) {
        long befMd5Sum = System.currentTimeMillis();
        String md5Value = MD5Util.md5sum(filePath);
        LogUtils.m1568d("ApkDownloader", "checkMd5()---md5 sum cost :" + (System.currentTimeMillis() - befMd5Sum));
        if (md5Value.toLowerCase().equals(md5.toLowerCase())) {
            return true;
        }
        return false;
    }

    private boolean checkDownloadedProc(String filePath, String md5, boolean failDeleteFile) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (checkMd5(filePath, md5)) {
            return true;
        }
        if (!failDeleteFile) {
            return false;
        }
        file.delete();
        return false;
    }

    private boolean checkApkDownloadParam(AppVersion version) {
        if (version == null) {
            LogUtils.m1571e("ApkDownloader", "checkApkDownloadParam()---version is null!");
            return false;
        } else if (StringUtils.isEmpty(version.getMd5())) {
            LogUtils.m1571e("ApkDownloader", "checkApkDownloadParam()---md5 is null!");
            return false;
        } else if (!StringUtils.isEmpty(getApkName(version))) {
            return true;
        } else {
            LogUtils.m1571e("ApkDownloader", "checkApkDownloadParam()---apk name is null!");
            return false;
        }
    }

    public void onlyCheckApkDownloadSuccess(final AppVersion version, final ApkCheckDownloadListener listener) {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                boolean bRet = false;
                if (ApkDownloader.this.checkApkDownloadParam(version)) {
                    String apkName = ApkDownloader.this.getApkName(version);
                    if (ApkDownloader.this.checkDownloadedProc(ApkDownloader.this.getSdCardFilePath(apkName), version.getMd5(), false)) {
                        bRet = true;
                    } else {
                        if (ApkDownloader.this.checkDownloadedProc(ApkDownloader.this.getMemoryFilePath(apkName), version.getMd5(), false)) {
                            bRet = true;
                        }
                    }
                } else {
                    LogUtils.m1571e("ApkDownloader", "onlyCheckApkDownloadSuccess()---checkApkDownloadParam return false!");
                }
                listener.onDownloaded(bRet);
            }
        });
    }

    public void checkApkDownloadedAndDeleteInvalidFile(final AppVersion version) {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                if (ApkDownloader.this.checkApkDownloadParam(version)) {
                    String apkName = ApkDownloader.this.getApkName(version);
                    if (ApkDownloader.this.DOWNLOAD_DEBUG) {
                        ApkDownloader.this.deleteOldApks(ApkDownloader.this.getSdCardDir(), ApkDownloader.GALA_CLIENT_APK_NAME, "");
                        ApkDownloader.this.deleteOldApks(ApkDownloader.this.getMemoryDir(), ApkDownloader.GALA_CLIENT_APK_NAME, "");
                    } else {
                        ApkDownloader.this.deleteOldApks(ApkDownloader.this.getSdCardDir(), ApkDownloader.GALA_CLIENT_APK_NAME, apkName);
                        ApkDownloader.this.deleteOldApks(ApkDownloader.this.getMemoryDir(), ApkDownloader.GALA_CLIENT_APK_NAME, apkName);
                    }
                } else {
                    LogUtils.m1571e("ApkDownloader", "onlyCheckApkDownloadSuccess()---checkApkDownloadParam return false!");
                }
                LogUtils.m1568d("ApkDownloader", "delete existed files cost time-" + (System.currentTimeMillis() - start));
            }
        });
    }

    public boolean startDownloadApk(AppVersion version, DownloadSpeed mode, ApkDownloadListener listener) {
        LogUtils.m1568d("ApkDownloader", "startDownloadApk()");
        if (listener == null) {
            LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---listener is null");
            return false;
        } else if (isDownloading()) {
            LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---is downloading");
            return false;
        } else if (version == null) {
            LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---version is null!");
            listener.onError(2);
            return false;
        } else if (StringUtils.isEmpty(version.getMd5())) {
            LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---md5 is null!");
            return false;
        } else if (StringUtils.isEmpty(getApkName(version))) {
            LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---apk name is null!");
            listener.onError(2);
            return false;
        } else {
            CharSequence url = checkApkUrl(version.getUrl());
            if (StringUtils.isEmpty(url)) {
                LogUtils.m1571e("ApkDownloader", "startDownloadUpdatePackage()---url is null!");
                listener.onError(3);
                return false;
            }
            this.mApkDownloadListener = listener;
            this.mVersion = version;
            setLimitSpeed(mode);
            IGalaDownloadParameter param = this.mDownloader.getDownloadParameter();
            param.setDownloadUrl(url);
            param.setSavePath(null);
            param.setFileName(getApkName(this.mVersion));
            param.setReconnectTotal(2);
            param.setReadTimeOut(IOpenApiCommandHolder.OAA_CONNECT_INTERVAL);
            param.setMD5Code(this.mVersion.getMd5());
            this.mDownloader.callAsync(this.mDownloadListener);
            LogUtils.m1568d("ApkDownloader", "startDownloadApk()---start download");
            return true;
        }
    }

    public void stopDownloadUpdatePackage() {
        if (this.mDownloader != null) {
            if (this.mDownloader.isDownloading()) {
                this.mDownloader.cancel();
            }
            this.mDownloader = null;
        }
    }

    public boolean isDownloading() {
        if (this.mDownloader != null) {
            return this.mDownloader.isDownloading();
        }
        return false;
    }

    public void setLimitSpeed(DownloadSpeed mode) {
        if (this.mDownloader != null) {
            IGalaDownloadParameter param = this.mDownloader.getDownloadParameter();
            if (mode == DownloadSpeed.HIGHEST) {
                param.setLimitSpeed(20971520);
            }
            if (mode == DownloadSpeed.HIGHER) {
                param.setLimitSpeed(SendFlag.FLAG_KEY_PINGBACK_SP);
            } else if (mode == DownloadSpeed.NORMAL) {
                param.setLimitSpeed(307200);
            } else if (mode == DownloadSpeed.LOWER) {
                param.setLimitSpeed(102400);
            } else if (mode == DownloadSpeed.LOWEST) {
                param.setLimitSpeed(20480);
            }
        }
    }

    private void deleteDownloadFile() {
        if (this.mDownloader != null) {
            IGalaDownloadParameter param = this.mDownloader.getDownloadParameter();
            if (!StringUtils.isEmpty(param.getSavePath())) {
                File file = new File(param.getSavePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    private void _onStart() {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onStart();
        }
    }

    private void _onError(int errCode) {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onError(errCode);
        }
    }

    private void _onProgress(int progress) {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onProgress(progress);
        }
    }

    private void _onFinish() {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onFinish();
        }
    }

    private void _onCancelled() {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onCancelled();
        }
    }

    private void _onExisted() {
        if (this.mApkDownloadListener != null) {
            this.mApkDownloadListener.onExist();
        }
    }

    public String getFilePath() {
        return this.mDownloader.getDownloadParameter().getSavePath();
    }

    public long getFileLength() {
        File file = new File(this.mDownloader.getDownloadParameter().getSavePath());
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }
}
