package com.gala.video.app.epg.appdownload.downloader;

import android.content.Context;
import com.gala.video.lib.framework.core.network.download.GalaDownloadCreator;
import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloadParameter;
import com.gala.video.lib.framework.core.network.download.core.IGalaDownloader;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.mcto.ads.internal.net.SendFlag;
import java.io.File;

public class AppDownloader {
    private static final String TAG = "AppDownloadManager/AppDownloader";
    private AppDownloaderListener mAppDownloaderListener;
    private Context mContext;
    private IGalaDownloadListener mDownloadListener = new C05311();
    private IGalaDownloader mGalaDownloader;
    private int mLastProgress = 0;
    private PromotionAppInfo mPromotionAppInfo;

    public interface AppDownloaderListener {
        void onCancel();

        void onError(int i);

        void onExisted(String str);

        void onProgress(int i);

        void onStart();

        void onSuccess(File file, String str);
    }

    class C05311 implements IGalaDownloadListener {
        C05311() {
        }

        public void onStart() {
            LogRecordUtils.logd(AppDownloader.TAG, "onStart");
            AppDownloader.this.onDownloadStart();
        }

        public void onSuccess(Object file, String path) {
            LogRecordUtils.logd(AppDownloader.TAG, "onSuccess: ptah -> " + path);
            AppDownloader.this.onDownloadSuccess(toFile(file), path);
        }

        public void onProgress(long download, long fileSize) {
            int progress = (int) ((100 * download) / fileSize);
            if (AppDownloader.this.mLastProgress != progress) {
                LogRecordUtils.logd(AppDownloader.TAG, "onProgress: progress -> " + progress);
            }
            AppDownloader.this.onDownloadProgress(progress);
            AppDownloader.this.mLastProgress = progress;
        }

        public void onError(GalaDownloadException e) {
            LogRecordUtils.logd(AppDownloader.TAG, "onError: errorCode -> " + e.getErrorCode());
            AppDownloader.this.mLastProgress = 0;
            AppDownloader.this.deleteDownloadFile();
            AppDownloader.this.onDownloadError(e.getErrorCode());
        }

        public void onCancel() {
            LogRecordUtils.logd(AppDownloader.TAG, "onCancel");
            AppDownloader.this.deleteDownloadFile();
            AppDownloader.this.onDownloadCancel();
        }

        public void onExisted(String path) {
            LogRecordUtils.logd(AppDownloader.TAG, "onExisted: path -> " + path);
            AppDownloader.this.onDownloadExisted(path);
        }

        private File toFile(Object obj) {
            try {
                return (File) obj;
            } catch (Exception e) {
                LogRecordUtils.loge(AppDownloader.TAG, "toFile: fail.");
                return null;
            }
        }
    }

    public enum DownloadSpeed {
        HIGHEST,
        HIGHER,
        NORMAL,
        LOWER,
        LOWEST
    }

    public AppDownloader(Context context) {
        this.mContext = context;
        this.mGalaDownloader = GalaDownloadCreator.createFileDownloader();
    }

    public boolean startDownload(PromotionAppInfo promotionAppInfo, DownloadSpeed downloadSpeed) {
        if (promotionAppInfo == null) {
            LogRecordUtils.loge(TAG, "startDownload: data is null.");
            return false;
        }
        this.mPromotionAppInfo = promotionAppInfo;
        if (isDownloading()) {
            LogRecordUtils.loge(TAG, "startDownload: is downloading.");
            return false;
        } else if (!validationData(promotionAppInfo)) {
            return false;
        } else {
            setLimitSpeed(downloadSpeed);
            String downloadUrl = promotionAppInfo.getAppDownloadUrl();
            String fileName = getFileName(promotionAppInfo);
            String md5 = promotionAppInfo.getMd5();
            IGalaDownloadParameter param = this.mGalaDownloader.getDownloadParameter();
            param.setDownloadUrl(downloadUrl);
            param.setSavePath(null);
            param.setFileName(fileName);
            param.setReconnectTotal(2);
            param.setReadTimeOut(IOpenApiCommandHolder.OAA_CONNECT_INTERVAL);
            param.setMD5Code(md5);
            this.mGalaDownloader.callAsync(this.mDownloadListener);
            LogRecordUtils.logd(TAG, "startDownload: start download.");
            return true;
        }
    }

    public void stopDownload() {
        if (this.mGalaDownloader != null && isDownloading()) {
            this.mGalaDownloader.cancel();
            this.mGalaDownloader = null;
            this.mAppDownloaderListener = null;
        }
    }

    public boolean isDownloading() {
        if (this.mGalaDownloader != null) {
            return this.mGalaDownloader.isDownloading();
        }
        return false;
    }

    public String getFileName(PromotionAppInfo promotionAppInfo) {
        String fileName = "";
        if (promotionAppInfo == null || !validationData(promotionAppInfo)) {
            return fileName;
        }
        return promotionAppInfo.getAppPckName() + "_" + promotionAppInfo.getAppVerName() + ".apk";
    }

    public void setDownloadListener(AppDownloaderListener appDownloaderListener) {
        this.mAppDownloaderListener = appDownloaderListener;
    }

    public void setLimitSpeed(DownloadSpeed mode) {
        if (this.mGalaDownloader != null) {
            IGalaDownloadParameter param = this.mGalaDownloader.getDownloadParameter();
            switch (mode) {
                case HIGHEST:
                    param.setLimitSpeed(20971520);
                    return;
                case HIGHER:
                    param.setLimitSpeed(SendFlag.FLAG_KEY_PINGBACK_SP);
                    return;
                case NORMAL:
                    param.setLimitSpeed(307200);
                    return;
                case LOWER:
                    param.setLimitSpeed(102400);
                    return;
                case LOWEST:
                    param.setLimitSpeed(20480);
                    return;
                default:
                    return;
            }
        }
    }

    public String getFilePath() {
        return this.mGalaDownloader.getDownloadParameter().getSavePath();
    }

    public long getFileLength() {
        File file = new File(this.mGalaDownloader.getDownloadParameter().getSavePath());
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    public int getLastProgress() {
        return this.mLastProgress;
    }

    private boolean validationData(PromotionAppInfo promotionAppInfo) {
        if (StringUtils.isEmpty(promotionAppInfo.getAppPckName())) {
            LogRecordUtils.loge(TAG, "startDownload: packageName is null.");
            return false;
        } else if (StringUtils.isEmpty(promotionAppInfo.getAppDownloadUrl())) {
            LogRecordUtils.loge(TAG, "startDownload: downloadUrl is null.");
            return false;
        } else if (StringUtils.isEmpty(promotionAppInfo.getMd5())) {
            LogRecordUtils.loge(TAG, "startDownload: md5 is null.");
            return false;
        } else if (!StringUtils.isEmpty(promotionAppInfo.getAppVerName())) {
            return true;
        } else {
            LogRecordUtils.loge(TAG, "startDownload: version is null.");
            return false;
        }
    }

    private void deleteDownloadFile() {
        if (this.mGalaDownloader != null) {
            IGalaDownloadParameter param = this.mGalaDownloader.getDownloadParameter();
            if (!StringUtils.isEmpty(param.getSavePath())) {
                File file = new File(param.getSavePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    private void onDownloadStart() {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onStart();
        }
    }

    private void onDownloadError(int errCode) {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onError(errCode);
        }
    }

    private void onDownloadProgress(int progress) {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onProgress(progress);
        }
    }

    private void onDownloadSuccess(File file, String path) {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onSuccess(file, path);
        }
    }

    private void onDownloadCancel() {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onCancel();
        }
    }

    private void onDownloadExisted(String path) {
        if (this.mAppDownloaderListener != null) {
            this.mAppDownloaderListener.onExisted(path);
        }
    }
}
