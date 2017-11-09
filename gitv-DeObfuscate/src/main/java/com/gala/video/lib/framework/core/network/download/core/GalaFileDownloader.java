package com.gala.video.lib.framework.core.network.download.core;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.MD5Util;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.mcto.ads.internal.net.SendFlag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GalaFileDownloader extends GalaBaseDownloader {
    private boolean mIsCancelled;
    private boolean mIsMD5CheckSuccessful;
    private boolean mIsWriteDiskSuccessful;
    private Call mRequestCall;
    private boolean mShouldLimitSpeed;

    interface ICheckFileExistedCallback {
        void hasExisted(boolean z);
    }

    public /* bridge */ /* synthetic */ void callAsync(IGalaDownloadListener iGalaDownloadListener) {
        super.callAsync(iGalaDownloadListener);
    }

    public /* bridge */ /* synthetic */ void callSync(IGalaDownloadListener iGalaDownloadListener) {
        super.callSync(iGalaDownloadListener);
    }

    public /* bridge */ /* synthetic */ IGalaDownloadParameter getDownloadParameter() {
        return super.getDownloadParameter();
    }

    public /* bridge */ /* synthetic */ boolean isDownloading() {
        return super.isDownloading();
    }

    public GalaFileDownloader() {
        this.mIsCancelled = false;
        this.mIsWriteDiskSuccessful = true;
        this.mIsMD5CheckSuccessful = true;
        this.mRequestCall = null;
        this.mShouldLimitSpeed = false;
        this.mShouldLimitSpeed = shouldLimitSpeed();
    }

    private boolean shouldLimitSpeed() {
        String model = Build.MODEL;
        LogUtils.m1568d("Downloader", "model：" + model);
        if ("i71".equalsIgnoreCase(model) || "i71c".equalsIgnoreCase(model)) {
            return true;
        }
        return false;
    }

    protected void callAsync() {
        this.mIsDownloading = true;
        final String savePath = buildSavePath();
        this.mDownloadParameter.setSavePath(savePath);
        if (savePath != null) {
            checkFileExists(new ICheckFileExistedCallback() {
                public void hasExisted(boolean existed) {
                    if (existed) {
                        GalaFileDownloader.this.mIsDownloading = false;
                        GalaFileDownloader.this.mDownloadListener.onExisted(savePath);
                        return;
                    }
                    GalaFileDownloader.this.mDownloadListener.onStart();
                    OkHttpClient httpClient = GalaFileDownloader.this.getOkHttpClientBuilder().connectTimeout(GalaFileDownloader.this.mDownloadParameter.getConnectTimeOut(), TimeUnit.MILLISECONDS).readTimeout(GalaFileDownloader.this.mDownloadParameter.getReadTimeOut(), TimeUnit.MILLISECONDS).addInterceptor(new RetryIntercepter(GalaFileDownloader.this.mDownloadParameter.getReconnectTotal())).build();
                    Builder requestBuilder = new Builder();
                    requestBuilder.url(GalaFileDownloader.this.mDownloadParameter.getDownloadUrl());
                    Map<String, String> headers = GalaFileDownloader.this.mDownloadParameter.getHeaderList();
                    if (headers != null && headers.size() > 0) {
                        for (String key : headers.keySet()) {
                            requestBuilder.header(key, (String) headers.get(key));
                        }
                    }
                    httpClient.newCall(requestBuilder.build()).enqueue(GalaFileDownloader.this.createCallback(savePath));
                }
            });
        }
    }

    protected void callSync() {
        this.mIsDownloading = false;
        this.mDownloadListener.onError(new GalaDownloadException(1, "FileDownloader doesn't provide synchronous method!"));
    }

    private String buildSavePath() {
        String fileName = this.mDownloadParameter.getFileName();
        if (fileName.contains("/")) {
            return fileName;
        }
        Log.d("Downloader", "start to check direction.");
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!FileUtil.sdcardCanWrite() || IGalaDownloadParameter.FILE_SIZE_LIMIT >= DeviceUtils.getSDCardSpareQuantity() - this.mDownloadParameter.getDiskSizeLimit()) {
            String diskPath = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath();
            if (IGalaDownloadParameter.FILE_SIZE_LIMIT < DeviceUtils.getDataSpareQuantity() - this.mDownloadParameter.getDiskSizeLimit()) {
                Log.d("Downloader", "check disk successfully.");
                return diskPath + "/" + fileName;
            }
            this.mIsDownloading = false;
            this.mDownloadListener.onError(new GalaDownloadException(4, "There is not enough space for download!"));
            return null;
        }
        Log.d("Downloader", "check sdcard successfully.");
        return sdcardPath + "/" + fileName;
    }

    public void cancel() {
        this.mIsDownloading = false;
        if (this.mRequestCall != null) {
            Log.e("Downloader", "onCancel");
            this.mRequestCall.cancel();
            this.mIsCancelled = true;
            if (this.mDownloadListener != null) {
                this.mDownloadListener.onCancel();
            }
        }
    }

    private Callback createCallback(final String savePath) {
        return new Callback() {
            public void onFailure(Call call, IOException e) {
                GalaFileDownloader.this.mIsDownloading = false;
                GalaFileDownloader.this.onDownloadFailure(e);
            }

            public void onResponse(Call call, final Response response) throws IOException {
                GalaFileDownloader.this.mRequestCall = call;
                if (response.isSuccessful()) {
                    GalaBaseDownloader.mFixedThreadPool.execute(new Runnable() {
                        public void run() {
                            if (GalaFileDownloader.this.writeResponseBodyToDisk(response.body(), savePath)) {
                                GalaFileDownloader.this.mIsDownloading = false;
                                GalaFileDownloader.this.mDownloadListener.onSuccess(null, GalaFileDownloader.this.mDownloadParameter.getSavePath());
                                return;
                            }
                            GalaFileDownloader.this.mIsDownloading = false;
                            if (!GalaFileDownloader.this.mIsMD5CheckSuccessful) {
                                GalaFileDownloader.this.mDownloadListener.onError(new GalaDownloadException(6, "Check MD5 failed!"));
                            } else if (!GalaFileDownloader.this.mIsWriteDiskSuccessful) {
                                GalaFileDownloader.this.mDownloadListener.onError(new GalaDownloadException(5, "Write file " + GalaFileDownloader.this.mDownloadParameter.getSavePath() + " to disk failed"));
                            } else if (!GalaFileDownloader.this.mIsCancelled) {
                                GalaFileDownloader.this.onDownloadFailure(null);
                            }
                        }
                    });
                }
            }
        };
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String savePath) {
        if (this.mDownloadParameter.getRangeStartPoint() != 0) {
            return writeRandomAccessFile(body, savePath);
        }
        return writeFile(body, savePath);
    }

    private boolean writeRandomAccessFile(ResponseBody body, String savePath) {
        IOException e;
        Throwable th;
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(savePath, "rwd");
            try {
                channelOut = randomAccessFile2.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(MapMode.READ_WRITE, this.mDownloadParameter.getRangeStartPoint(), body.contentLength());
                byte[] buffer = new byte[this.mDownloadParameter.getLimitSpeed()];
                long fileSizeDownloaded = 0;
                long fileSize = body.contentLength();
                while (true) {
                    int len = in.read(buffer);
                    if (len != -1 && !this.mIsCancelled) {
                        mappedBuffer.put(buffer, 0, len);
                        fileSizeDownloaded += (long) len;
                        Log.d("Downloader", "write RandomAccessFile: " + fileSizeDownloaded + " of " + fileSize);
                        this.mDownloadListener.onProgress(fileSizeDownloaded, fileSize);
                    }
                }
                if (StringUtils.isEmpty(this.mDownloadParameter.getMD5Code()) || checkMD5Code()) {
                    try {
                        in.close();
                        if (channelOut != null) {
                            channelOut.close();
                        }
                        if (randomAccessFile2 != null) {
                            randomAccessFile2.close();
                        }
                        randomAccessFile = randomAccessFile2;
                        return true;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        this.mIsWriteDiskSuccessful = false;
                        randomAccessFile = randomAccessFile2;
                        return false;
                    }
                }
                this.mIsMD5CheckSuccessful = false;
                try {
                    in.close();
                    if (channelOut != null) {
                        channelOut.close();
                    }
                    if (randomAccessFile2 != null) {
                        randomAccessFile2.close();
                    }
                    randomAccessFile = randomAccessFile2;
                    return false;
                } catch (IOException e22) {
                    e22.printStackTrace();
                    this.mIsWriteDiskSuccessful = false;
                    randomAccessFile = randomAccessFile2;
                    return false;
                }
            } catch (IOException e3) {
                e22 = e3;
                randomAccessFile = randomAccessFile2;
            } catch (Throwable th2) {
                th = th2;
                randomAccessFile = randomAccessFile2;
            }
        } catch (IOException e4) {
            e22 = e4;
            try {
                e22.printStackTrace();
                this.mIsWriteDiskSuccessful = false;
                try {
                    in.close();
                    if (channelOut != null) {
                        channelOut.close();
                    }
                    if (randomAccessFile == null) {
                        return false;
                    }
                    randomAccessFile.close();
                    return false;
                } catch (IOException e222) {
                    e222.printStackTrace();
                    this.mIsWriteDiskSuccessful = false;
                    return false;
                }
            } catch (Throwable th3) {
                th = th3;
                try {
                    in.close();
                    if (channelOut != null) {
                        channelOut.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    throw th;
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                    this.mIsWriteDiskSuccessful = false;
                    return false;
                }
            }
        }
    }

    private boolean writeFile(ResponseBody body, String savePath) {
        boolean isWriteSuccess;
        IOException e;
        Throwable th;
        Log.d("Downloader", "save path " + savePath);
        File file = new File(savePath);
        String tmpFilePath = null;
        if (file.exists()) {
            int index = savePath.lastIndexOf("/");
            if (index != -1) {
                String path = savePath.substring(0, index + 1);
                String name = savePath.substring(index + 1);
                int index_ex = name.lastIndexOf(".");
                tmpFilePath = path + name.substring(0, index_ex) + "_tmp" + name.substring(index_ex);
                file.renameTo(new File(tmpFilePath));
                Log.d("Downloader", "rename file to " + tmpFilePath);
            }
        }
        File file2 = new File(savePath);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] bytes = new byte[SendFlag.FLAG_KEY_PINGBACK_1Q];
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            long readBlockSize = 0;
            inputStream = body.byteStream();
            OutputStream fileOutputStream = new FileOutputStream(file2);
            try {
                CpuLoadBalance balance = new CpuLoadBalance();
                long total = 0;
                while (!this.mIsCancelled) {
                    int read = inputStream.read(bytes);
                    if (read == -1) {
                        break;
                    }
                    if (this.mShouldLimitSpeed && readBlockSize >= 131072) {
                        long sleepTime = (long) balance.getSleepTime();
                        total += sleepTime;
                        Log.d("Downloader", "block size " + readBlockSize + ", sleep time = " + sleepTime + ",total = " + total);
                        readBlockSize = 0;
                        if (sleepTime > 0) {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    fileOutputStream.write(bytes, 0, read);
                    fileSizeDownloaded += (long) read;
                    readBlockSize += (long) read;
                    Log.d("Downloader", "downloaded-" + fileSizeDownloaded + "-" + fileSize);
                    this.mDownloadListener.onProgress(fileSizeDownloaded, fileSize);
                }
                fileOutputStream.flush();
                if (fileSizeDownloaded < fileSize) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e3) {
                            this.mIsWriteDiskSuccessful = false;
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    body.close();
                    outputStream = fileOutputStream;
                    return false;
                }
                isWriteSuccess = true;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        this.mIsWriteDiskSuccessful = false;
                        isWriteSuccess = false;
                        outputStream = fileOutputStream;
                    }
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                body.close();
                outputStream = fileOutputStream;
                if (tmpFilePath != null) {
                    file = new File(tmpFilePath);
                    if (file.exists()) {
                        if (isWriteSuccess) {
                            file = new File(savePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            file.renameTo(new File(savePath));
                        } else {
                            file.delete();
                        }
                    }
                }
                if (!isWriteSuccess) {
                    return false;
                }
                if (!StringUtils.isEmpty(this.mDownloadParameter.getMD5Code()) || checkMD5Code()) {
                    this.mIsMD5CheckSuccessful = true;
                    return true;
                }
                this.mIsMD5CheckSuccessful = false;
                return false;
            } catch (IOException e5) {
                e = e5;
                outputStream = fileOutputStream;
            } catch (Throwable th2) {
                th = th2;
                outputStream = fileOutputStream;
            }
        } catch (IOException e6) {
            e = e6;
            try {
                e.printStackTrace();
                isWriteSuccess = false;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e7) {
                        this.mIsWriteDiskSuccessful = false;
                        isWriteSuccess = false;
                    }
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                body.close();
                if (tmpFilePath != null) {
                    file = new File(tmpFilePath);
                    if (file.exists()) {
                        if (isWriteSuccess) {
                            file = new File(savePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            file.renameTo(new File(savePath));
                        } else {
                            file.delete();
                        }
                    }
                }
                if (!isWriteSuccess) {
                    return false;
                }
                if (StringUtils.isEmpty(this.mDownloadParameter.getMD5Code())) {
                }
                this.mIsMD5CheckSuccessful = true;
                return true;
            } catch (Throwable th3) {
                th = th3;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e8) {
                        this.mIsWriteDiskSuccessful = false;
                        throw th;
                    }
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                body.close();
                throw th;
            }
        }
    }

    private boolean checkMD5Code() {
        CharSequence md5Value = MD5Util.md5sum(this.mDownloadParameter.getSavePath());
        Log.d("Downloader", "md5-" + md5Value);
        if (StringUtils.isEmpty(md5Value) || !md5Value.equalsIgnoreCase(this.mDownloadParameter.getMD5Code())) {
            return false;
        }
        return true;
    }

    private void checkFileExists(final ICheckFileExistedCallback callback) {
        Log.d("Downloader", "check md5-" + this.mDownloadParameter.getSavePath() + ", md5-" + this.mDownloadParameter.getMD5Code());
        mFixedThreadPool.execute(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                boolean existed = false;
                File file = new File(GalaFileDownloader.this.mDownloadParameter.getSavePath());
                if (file.exists()) {
                    Log.d("Downloader", "file exist");
                    if (StringUtils.isEmpty(GalaFileDownloader.this.mDownloadParameter.getMD5Code())) {
                        existed = true;
                    } else if (GalaFileDownloader.this.checkMD5Code()) {
                        existed = true;
                    } else {
                        file.delete();
                        existed = false;
                    }
                }
                Log.d("Downloader", "check file existed cost time-" + (System.currentTimeMillis() - start));
                callback.hasExisted(existed);
            }
        });
    }
}
