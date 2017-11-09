package com.gala.video.lib.framework.core.network.downloadapp;

import android.util.Log;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.apache.http.conn.ConnectTimeoutException;
import org.cybergarage.http.HTTP;

public class DownloadThread implements Runnable {
    public static final int DOWNLOAD_404 = 4;
    public static final int DOWNLOAD_CANCAL = 3;
    public static final int DOWNLOAD_ERROR = 5;
    public static final int DOWNLOAD_FAILED = 2;
    public static final int DOWNLOAD_IO = 6;
    public static final int DOWNLOAD_OTHER = 1;
    public static final int DOWNLOAD_SUCCESS = 0;
    public static final int DOWNLOAD_TIMEOUT = 7;
    private static final String TAG = "DownloadThread";
    private int DOWNLOAD_RETRY_TIMES = 3;
    private int HTTP_RESPONSECODE_200 = 200;
    private int HTTP_RESPONSECODE_206 = 206;
    private int HTTP_RESPONSECODE_404 = 404;
    private DownloadListener mDownloadListener = null;
    private long mDownloadSize = 0;
    private String mDownloadUrl = null;
    private long mFileSize = 0;
    private boolean mIsPause = false;
    private boolean mIsStop = false;
    private String mName;
    private int mProgress = 0;
    private int mResponseCode = 0;
    private File mSaveFile;
    private File mSaveFileDir;
    private URL mUrl = null;

    public DownloadThread(String name, String downloadUrl, DownloadListener listener, String saveFileDir) {
        this.mDownloadUrl = downloadUrl;
        this.mDownloadListener = listener;
        this.mName = name;
        this.mSaveFileDir = new File(saveFileDir);
        if (!this.mSaveFileDir.exists()) {
            this.mSaveFileDir.mkdirs();
        }
    }

    public void run() {
        String filename;
        ConnectTimeoutException e;
        SocketTimeoutException e2;
        ProtocolException e3;
        SocketException e4;
        Exception e5;
        Throwable th;
        int isFinish = 1;
        FileOutputStream threadfile = null;
        InputStream inStream = null;
        try {
            this.mSaveFile = new File(this.mSaveFileDir, getFileName());
            this.mDownloadListener.onStart();
            this.mUrl = new URL(this.mDownloadUrl);
            HttpURLConnection http = (HttpURLConnection) this.mUrl.openConnection();
            setHttpParams(http);
            if (this.mSaveFile.exists()) {
                LogUtils.d(TAG, "run, delete temp file result : " + this.mSaveFile.delete());
            }
            this.mSaveFile.createNewFile();
            this.mResponseCode = getConnection(http);
            if (this.mResponseCode == this.HTTP_RESPONSECODE_200 || this.mResponseCode == this.HTTP_RESPONSECODE_206) {
                this.mFileSize = (long) http.getContentLength();
                if (this.mFileSize <= 0) {
                    Log.e(TAG, "DownloadThread ---ThreadRun() -> get filesize error mFileSize =" + this.mFileSize);
                    if (threadfile != null) {
                        try {
                            threadfile.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                        this.mDownloadListener.onComplete(4, this.mSaveFile.getAbsolutePath(), this.mName);
                    } else {
                        filename = this.mSaveFile.getName();
                        filename = filename.substring(0, filename.lastIndexOf("."));
                        if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                            this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                        } else {
                            LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                            this.mSaveFile.delete();
                            this.mDownloadListener.onComplete(2, "", this.mName);
                        }
                    }
                    DownloadManager.getInstance().stop(this.mDownloadUrl);
                    return;
                }
            } else if (this.mResponseCode == this.HTTP_RESPONSECODE_404) {
                Log.e(TAG, "DownloadThread ---ThreadRun() -> http.getResponseCode() isFinish is 404");
                this.mSaveFile.delete();
                if (threadfile != null) {
                    try {
                        threadfile.close();
                    } catch (IOException e8) {
                    }
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e9) {
                    }
                }
                if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                    this.mDownloadListener.onComplete(4, this.mSaveFile.getAbsolutePath(), this.mName);
                } else {
                    filename = this.mSaveFile.getName();
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                        this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                    } else {
                        LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                        this.mSaveFile.delete();
                        this.mDownloadListener.onComplete(2, "", this.mName);
                    }
                }
                DownloadManager.getInstance().stop(this.mDownloadUrl);
                return;
            } else {
                Log.e(TAG, "DownloadThread ---ThreadRun() -> http.getResponseCode() :" + http.getResponseCode());
                if (this.mFileSize <= 0) {
                    if (threadfile != null) {
                        try {
                            threadfile.close();
                        } catch (IOException e10) {
                        }
                    }
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e11) {
                        }
                    }
                    if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                        this.mDownloadListener.onComplete(4, this.mSaveFile.getAbsolutePath(), this.mName);
                    } else {
                        filename = this.mSaveFile.getName();
                        filename = filename.substring(0, filename.lastIndexOf("."));
                        if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                            this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                        } else {
                            LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                            this.mSaveFile.delete();
                            this.mDownloadListener.onComplete(2, "", this.mName);
                        }
                    }
                    DownloadManager.getInstance().stop(this.mDownloadUrl);
                    return;
                }
            }
            inStream = http.getInputStream();
            byte[] buffer = new byte[4096];
            FileOutputStream threadfile2 = new FileOutputStream(this.mSaveFile, true);
            while (true) {
                try {
                    int offset = inStream.read(buffer, 0, 4096);
                    if (offset == -1) {
                        break;
                    } else if (this.mIsStop || this.mIsPause) {
                        isFinish = 3;
                        this.mDownloadListener.onStop();
                    } else {
                        threadfile2.write(buffer, 0, offset);
                        this.mDownloadSize += (long) offset;
                        int progress = (int) ((this.mDownloadSize * 100) / this.mFileSize);
                        if (progress > this.mProgress) {
                            this.mDownloadListener.onProgress(progress);
                            this.mProgress = progress;
                        }
                    }
                } catch (ConnectTimeoutException e12) {
                    e = e12;
                    threadfile = threadfile2;
                } catch (SocketTimeoutException e13) {
                    e2 = e13;
                    threadfile = threadfile2;
                } catch (ProtocolException e14) {
                    e3 = e14;
                    threadfile = threadfile2;
                } catch (SocketException e15) {
                    e4 = e15;
                    threadfile = threadfile2;
                } catch (Exception e16) {
                    e5 = e16;
                    threadfile = threadfile2;
                } catch (Throwable th2) {
                    th = th2;
                    threadfile = threadfile2;
                }
            }
            isFinish = 3;
            this.mDownloadListener.onStop();
            if (this.mIsStop) {
                this.mDownloadListener.onProgress(0);
                this.mSaveFile.delete();
            }
            if (threadfile2 != null) {
                try {
                    threadfile2.close();
                } catch (IOException e17) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e18) {
                }
            }
            if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                this.mDownloadListener.onComplete(isFinish, this.mSaveFile.getAbsolutePath(), this.mName);
            } else {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            }
            DownloadManager.getInstance().stop(this.mDownloadUrl);
            threadfile = threadfile2;
        } catch (ConnectTimeoutException e19) {
            e = e19;
            try {
                LogUtils.e(TAG, "DownloadThread ---ThreadRun()-> ConnectTimeoutException ---mName:" + this.mName + "mUrlstr:" + this.mDownloadUrl + e.getMessage());
                isFinish = 7;
                this.mSaveFile.delete();
                if (threadfile != null) {
                    try {
                        threadfile.close();
                    } catch (IOException e20) {
                    }
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e21) {
                    }
                }
                if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                    this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
                } else {
                    filename = this.mSaveFile.getName();
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                        this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                    } else {
                        LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                        this.mSaveFile.delete();
                        this.mDownloadListener.onComplete(2, "", this.mName);
                    }
                }
                DownloadManager.getInstance().stop(this.mDownloadUrl);
            } catch (Throwable th3) {
                th = th3;
                if (threadfile != null) {
                    try {
                        threadfile.close();
                    } catch (IOException e22) {
                    }
                }
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e23) {
                    }
                }
                if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                    this.mDownloadListener.onComplete(isFinish, this.mSaveFile.getAbsolutePath(), this.mName);
                } else {
                    filename = this.mSaveFile.getName();
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                        this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                    } else {
                        LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                        this.mSaveFile.delete();
                        this.mDownloadListener.onComplete(2, "", this.mName);
                    }
                }
                DownloadManager.getInstance().stop(this.mDownloadUrl);
                throw th;
            }
        } catch (SocketTimeoutException e24) {
            e2 = e24;
            LogUtils.e(TAG, "DownloadThread ---ThreadRun()-> SocketTimeoutException ---mName:" + this.mName + "mUrlstr:" + this.mDownloadUrl + e2.getMessage());
            isFinish = 7;
            this.mSaveFile.delete();
            if (threadfile != null) {
                try {
                    threadfile.close();
                } catch (IOException e25) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e26) {
                }
            }
            if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
            } else {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            }
            DownloadManager.getInstance().stop(this.mDownloadUrl);
        } catch (ProtocolException e27) {
            e3 = e27;
            LogUtils.e(TAG, "DownloadThread ---ThreadRun()-> ProtocolException ---mName:" + this.mName + "mUrlstr:" + this.mDownloadUrl + e3.getMessage());
            isFinish = 6;
            this.mSaveFile.delete();
            if (threadfile != null) {
                try {
                    threadfile.close();
                } catch (IOException e28) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e29) {
                }
            }
            if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                this.mDownloadListener.onComplete(6, this.mSaveFile.getAbsolutePath(), this.mName);
            } else {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            }
            DownloadManager.getInstance().stop(this.mDownloadUrl);
        } catch (SocketException e30) {
            e4 = e30;
            LogUtils.e(TAG, "DownloadThread ---ThreadRun()-> SocketException ---mName:" + this.mName + "mUrlstr:" + this.mDownloadUrl + e4.getMessage());
            isFinish = 7;
            this.mSaveFile.delete();
            if (threadfile != null) {
                try {
                    threadfile.close();
                } catch (IOException e31) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e32) {
                }
            }
            if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
            } else {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            }
            DownloadManager.getInstance().stop(this.mDownloadUrl);
        } catch (Exception e33) {
            e5 = e33;
            LogUtils.e(TAG, "DownloadThread ---ThreadRun()-> catch Exception ---mName:" + this.mName + "mUrlstr:" + this.mDownloadUrl + e5.getMessage());
            isFinish = 5;
            this.mSaveFile.delete();
            if (threadfile != null) {
                try {
                    threadfile.close();
                } catch (IOException e34) {
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e35) {
                }
            }
            if (this.mDownloadSize == 0 || this.mDownloadSize != this.mFileSize) {
                this.mDownloadListener.onComplete(5, this.mSaveFile.getAbsolutePath(), this.mName);
            } else {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    LogUtils.e(TAG, "DownloadThread---ThreadRun() - > renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            }
            DownloadManager.getInstance().stop(this.mDownloadUrl);
        }
    }

    private void setHttpParams(HttpURLConnection http) throws ProtocolException {
        http.setConnectTimeout(10000);
        http.setReadTimeout(10000);
        http.setRequestMethod(HTTP.GET);
        http.setRequestProperty("Accept-Language", "zh-CN");
        http.setRequestProperty("Referer", this.mUrl.toString());
        http.setRequestProperty("Charset", "UTF-8");
        http.setRequestProperty(HTTP.CONNECTION, HTTP.KEEP_ALIVE);
    }

    private String getFileName() {
        CharSequence filename = this.mDownloadUrl.substring(this.mDownloadUrl.lastIndexOf(47) + 1) + ".tmp";
        if (!StringUtils.isEmpty(filename)) {
            return filename;
        }
        throw new RuntimeException("DownloadThread ---getFileName() -> Unkown file name---");
    }

    public void setStop(boolean isStop) {
        this.mIsStop = isStop;
    }

    public void setPause(boolean isPause) {
        this.mIsPause = isPause;
    }

    public void setDownLoadListener(DownloadListener listener) {
        this.mDownloadListener = listener;
    }

    private int getConnection(HttpURLConnection http) {
        int num = 0;
        while (num < this.DOWNLOAD_RETRY_TIMES) {
            try {
                int stateCode = http.getResponseCode();
                LogUtils.e(TAG, "DownloadThread ---getConnection() -> stateCode=" + stateCode);
                if (stateCode == this.HTTP_RESPONSECODE_200 || stateCode == this.HTTP_RESPONSECODE_206) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        LogUtils.e(TAG, "DownloadThread ---getConnection() -> InterruptedException:" + e.getMessage());
                    }
                    num++;
                    return stateCode;
                } else if (stateCode != this.HTTP_RESPONSECODE_404 || num < this.DOWNLOAD_RETRY_TIMES) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e2) {
                        LogUtils.e(TAG, "DownloadThread ---getConnection() -> InterruptedException:" + e2.getMessage());
                    }
                    num++;
                } else {
                    stateCode = this.HTTP_RESPONSECODE_404;
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e22) {
                        LogUtils.e(TAG, "DownloadThread ---getConnection() -> InterruptedException:" + e22.getMessage());
                    }
                    num++;
                    return stateCode;
                }
            } catch (Exception e3) {
                LogUtils.e(TAG, "DownloadThread ---getConnection() -> exception" + e3.getMessage());
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e222) {
                    LogUtils.e(TAG, "DownloadThread ---getConnection() -> InterruptedException:" + e222.getMessage());
                }
                num++;
            } catch (Throwable th) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e2222) {
                    LogUtils.e(TAG, "DownloadThread ---getConnection() -> InterruptedException:" + e2222.getMessage());
                }
                num++;
            }
        }
        return 7;
    }
}
