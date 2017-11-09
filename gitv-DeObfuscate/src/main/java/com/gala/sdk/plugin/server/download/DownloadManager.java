package com.gala.sdk.plugin.server.download;

import android.util.Log;
import android.webkit.URLUtil;
import com.gala.sdk.plugin.server.utils.CpuLoadBalance;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import com.gala.sdk.plugin.server.utils.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {
    private static final String TAG = "DownloadManager";
    private final ExecutorService mExecutorService;
    private final Map<String, DownloadRunnable> mRunnableMap = new HashMap();

    public interface DownloadListener {
        public static final int CODE_SUCCESS = 0;
        public static final int ERROR_FILE_ERROR = 4;
        public static final int ERROR_NETWORK = 3;
        public static final int ERROR_RENAME_ERROR = 5;
        public static final int ERROR_SAVE_PATH_INVALID = 2;
        public static final int ERROR_URL_INVALID = 1;

        void onCanceled();

        void onError(ErrorDetail errorDetail);

        void onFinish();

        void onProgress(int i, int i2);

        void onStart();
    }

    private static class DownloadRunnable implements Runnable, DownloadListener {
        private static final int BUFFER_SIZE = 1024;
        private static final String PLUGIN_FILE_APPEND = "_temp";
        private boolean mCanceled = false;
        private final DownloadInfo mDownloadInfo;
        private DownloadListener mListener;

        public DownloadRunnable(DownloadInfo info, DownloadListener listener) {
            this.mDownloadInfo = new DownloadInfo(info);
            this.mListener = listener;
        }

        public void cancel() {
            this.mCanceled = true;
        }

        public void run() {
            MalformedURLException e;
            IOException e2;
            Throwable th;
            if (!checkUrl() || PluginDebugUtils.needThrowable(DEBUG_PROPERTY.URL_INVAILD)) {
                onError(new ErrorDetail(1, new Exception("url=" + this.mDownloadInfo.getUrl() + " is invalid!!")));
            } else if (!checkSavePath() || PluginDebugUtils.needThrowable(DEBUG_PROPERTY.CREATE_SAVEPATH_FAILED)) {
                onError(new ErrorDetail(2, new Exception("createSavePath failed!! (savePath=" + this.mDownloadInfo.getSavePath() + ")")));
            } else if (!checkCanceled()) {
                onStart();
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                String urlStr = this.mDownloadInfo.getUrl();
                String savePath = this.mDownloadInfo.getSavePath();
                String savePathTemp = FileUtils.appendFileName(savePath, PLUGIN_FILE_APPEND);
                try {
                    URL url = new URL(urlStr);
                    if (checkCanceled()) {
                        FileUtils.closeStream(null);
                        FileUtils.closeStream(null);
                        return;
                    }
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (checkCanceled()) {
                        FileUtils.closeStream(null);
                        FileUtils.closeStream(null);
                        return;
                    }
                    inputStream = connection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(savePathTemp);
                    try {
                        int totalLength = connection.getContentLength();
                        int currentLength = 0;
                        byte[] buffer = new byte[1024];
                        int sleepSize = 0;
                        int sleepSumtime = 0;
                        FileUtils.sDelaySumTime = 0;
                        while (true) {
                            int size = inputStream.read(buffer);
                            if (size == -1 || checkCanceled()) {
                                break;
                            }
                            sleepSize++;
                            if (sleepSize == 512) {
                                int sleeptime = CpuLoadBalance.getInstance().getSleepTime();
                                try {
                                    Thread.sleep((long) sleeptime);
                                    sleepSumtime += sleeptime;
                                } catch (InterruptedException e3) {
                                    e3.printStackTrace();
                                }
                                sleepSize = 0;
                            }
                            outputStream.write(buffer, 0, size);
                            currentLength += size;
                            onProgress(currentLength, totalLength);
                        }
                        FileUtils.sDelaySumTime += (long) sleepSumtime;
                        Log.d(DownloadManager.TAG, "DownloadRunnable sleepSumtime " + sleepSumtime + "ms");
                        outputStream.flush();
                        if (checkCanceled()) {
                            FileUtils.closeStream(inputStream);
                            FileUtils.closeStream(outputStream);
                            return;
                        }
                        boolean success = true;
                        String md5 = null;
                        if (this.mDownloadInfo.needCheckMd5()) {
                            md5 = FileUtils.md5(savePathTemp);
                            String targetMd5 = this.mDownloadInfo.getMd5();
                            if (Util.isEmpty(md5) || Util.isEmpty(targetMd5)) {
                                success = false;
                            } else {
                                success = Util.equals(md5.toLowerCase(Locale.getDefault()), targetMd5.toLowerCase(Locale.getDefault()));
                            }
                            Log.w(DownloadManager.TAG, "needCheckMd5, check(source=" + md5 + ", target=" + this.mDownloadInfo.getMd5() + ") return " + success);
                        }
                        if (!success || PluginDebugUtils.needThrowable(DEBUG_PROPERTY.MD5_NOT_EQUAL)) {
                            onError(new ErrorDetail(4, new Exception("md5 not equal!! (target=" + this.mDownloadInfo.getMd5() + ", local=" + md5 + ")")));
                            FileUtils.closeStream(inputStream);
                            FileUtils.closeStream(outputStream);
                            return;
                        }
                        success = FileUtils.renameFile(savePathTemp, savePath);
                        if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.DOWNLOAD_IO_EXCAPTION)) {
                            onError(new ErrorDetail(3, new IOException("download io exception!(for debug!)")));
                            FileUtils.closeStream(inputStream);
                            FileUtils.closeStream(outputStream);
                            return;
                        }
                        if (success) {
                            if (!PluginDebugUtils.needThrowable(DEBUG_PROPERTY.RENAME_FAILED)) {
                                if (!checkCanceled()) {
                                    onFinish();
                                }
                                FileUtils.closeStream(inputStream);
                                FileUtils.closeStream(outputStream);
                                fileOutputStream = outputStream;
                                return;
                            }
                        }
                        onError(new ErrorDetail(5, new Exception("rename failed!!")));
                        FileUtils.closeStream(inputStream);
                        FileUtils.closeStream(outputStream);
                    } catch (MalformedURLException e4) {
                        e = e4;
                        fileOutputStream = outputStream;
                    } catch (IOException e5) {
                        e2 = e5;
                        fileOutputStream = outputStream;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = outputStream;
                    }
                } catch (MalformedURLException e6) {
                    e = e6;
                    try {
                        Log.w(DownloadManager.TAG, "downloadRunnable excetion!", e);
                        onError(new ErrorDetail(1, e));
                        e.printStackTrace();
                        FileUtils.closeStream(inputStream);
                        FileUtils.closeStream(fileOutputStream);
                    } catch (Throwable th3) {
                        th = th3;
                        FileUtils.closeStream(inputStream);
                        FileUtils.closeStream(fileOutputStream);
                        throw th;
                    }
                } catch (IOException e7) {
                    e2 = e7;
                    Log.w(DownloadManager.TAG, "downloadRunnable excetion!", e2);
                    onError(new ErrorDetail(3, e2));
                    e2.printStackTrace();
                    FileUtils.closeStream(inputStream);
                    FileUtils.closeStream(fileOutputStream);
                }
            }
        }

        public void onStart() {
            Log.d(DownloadManager.TAG, "downloadRunnable onStart()");
            if (this.mListener != null) {
                this.mListener.onStart();
            }
        }

        public void onProgress(int currentLength, int totalLength) {
            if (this.mListener != null) {
                this.mListener.onProgress(currentLength, totalLength);
            }
        }

        public void onFinish() {
            Log.d(DownloadManager.TAG, "downloadRunnable onFinish()");
            if (this.mListener != null) {
                this.mListener.onFinish();
            }
        }

        public void onCanceled() {
            Log.d(DownloadManager.TAG, "downloadRunnable onCanceled()");
            if (this.mListener != null) {
                this.mListener.onCanceled();
            }
        }

        public void onError(ErrorDetail error) {
            Log.d(DownloadManager.TAG, "downloadRunnable onError(" + error + ")");
            if (this.mListener != null) {
                this.mListener.onError(error);
            }
        }

        private boolean checkUrl() {
            Log.d(DownloadManager.TAG, "checkUrl(), url=" + this.mDownloadInfo.getUrl());
            boolean isValid = URLUtil.isValidUrl(this.mDownloadInfo.getUrl());
            Log.d(DownloadManager.TAG, "checkUrl(), return=" + isValid);
            return isValid;
        }

        private boolean checkSavePath() {
            Log.d(DownloadManager.TAG, "checkSavePath(), path=" + this.mDownloadInfo.getSavePath());
            boolean success = false;
            if (!Util.isEmpty(this.mDownloadInfo.getSavePath())) {
                Log.d(DownloadManager.TAG, "checkSavePath(), path is not empty!");
                File file = new File(this.mDownloadInfo.getSavePath());
                File parent = file.getParentFile();
                if (!(parent == null || parent.exists())) {
                    Log.d(DownloadManager.TAG, "checkSavePath(), path parent is not exists!");
                    parent.mkdirs();
                }
                if (file.exists()) {
                    Log.d(DownloadManager.TAG, "checkSavePath(), path already exists!");
                    file.delete();
                }
                try {
                    success = file.createNewFile();
                    Log.d(DownloadManager.TAG, "checkSavePath(), path createNewFile " + success);
                } catch (IOException e) {
                    Log.d(DownloadManager.TAG, "checkSavePath(), path createNewFile exception!");
                    e.printStackTrace();
                    success = false;
                }
            }
            Log.d(DownloadManager.TAG, "checkSavePath(), return=" + success);
            return success;
        }

        private boolean checkCanceled() {
            if (this.mCanceled) {
                onCanceled();
            }
            return this.mCanceled;
        }
    }

    public static class ErrorDetail {
        public int code;
        public Exception exception;

        public ErrorDetail(int code, Exception exception) {
            this.code = code;
            this.exception = exception;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("ErrorDetail(").append("code=").append(this.code).append(", exception=").append(this.exception).append(")");
            return builder.toString();
        }
    }

    private static class MyDownloadListener implements DownloadListener {
        private ErrorDetail mErrorDetail;

        private MyDownloadListener() {
            this.mErrorDetail = null;
        }

        public ErrorDetail getErrorDetail() {
            return this.mErrorDetail;
        }

        public void onStart() {
            this.mErrorDetail = null;
        }

        public void onProgress(int currentLength, int totalLength) {
            this.mErrorDetail = null;
        }

        public void onFinish() {
            this.mErrorDetail = null;
        }

        public void onCanceled() {
            this.mErrorDetail = null;
        }

        public void onError(ErrorDetail error) {
            this.mErrorDetail = error;
        }
    }

    public DownloadManager(int threadCount) {
        this.mExecutorService = Executors.newFixedThreadPool(threadCount);
    }

    public boolean downloadPluginApk(DownloadInfo info) throws Exception {
        if (info == null) {
            return false;
        }
        MyDownloadListener listener = new MyDownloadListener();
        DownloadRunnable runnable = new DownloadRunnable(info, listener);
        synchronized (this.mRunnableMap) {
            this.mRunnableMap.put(info.getUrl(), runnable);
        }
        runnable.run();
        if (listener.getErrorDetail() == null) {
            return true;
        }
        throw listener.getErrorDetail().exception;
    }

    public boolean startDownloadAsync(DownloadInfo info, DownloadListener listener) {
        if (info == null) {
            return false;
        }
        DownloadRunnable runnable = new DownloadRunnable(info, listener);
        synchronized (this.mRunnableMap) {
            this.mRunnableMap.put(info.getUrl(), runnable);
        }
        this.mExecutorService.submit(runnable);
        return true;
    }

    public boolean stopDownload(DownloadInfo info) {
        if (info == null) {
            return false;
        }
        synchronized (this.mRunnableMap) {
            DownloadRunnable runnable = (DownloadRunnable) this.mRunnableMap.get(info.getUrl());
            if (runnable != null) {
                runnable.cancel();
                this.mRunnableMap.remove(info.getUrl());
            }
        }
        return true;
    }
}
