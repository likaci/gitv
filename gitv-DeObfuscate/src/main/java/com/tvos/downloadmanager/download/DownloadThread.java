package com.tvos.downloadmanager.download;

import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.HTTP;

public class DownloadThread extends Thread {
    private static final int EXIT_COMPLETE = 1;
    private static final int EXIT_ERROR = -1;
    private static final int EXIT_READERROR = 3;
    private static final int EXIT_REQUIRE = 0;
    private static final int EXIT_TIMEOUT = 2;
    private static final int RETRYNUM = 10;
    private static final String TAG = "DownloadThread";
    private long downloadsize;
    private long end;
    private File file;
    private long filepoint;
    private boolean isStarted = false;
    private IDownloadThreadFinishListener mFinishListener;
    private boolean needResume;
    private long no_data_timestamp = -1;
    private boolean reqStop;
    private int retryNum = 0;
    private int retryTimeOut = 0;
    private int speedLimitDegree = 0;
    private long start;
    private int threadid;
    private IDownloadThreadListener threadlistener;
    private URL url;

    public interface IDownloadThreadListener {
        void onError(int i, int i2, String str, boolean z);

        void onProgress(int i, long j, long j2);

        void onStarted(int i);

        void onStopped(int i, long j, long j2, boolean z);
    }

    public DownloadThread(int threadid, URL url, File file, long start, long end, IDownloadThreadListener listener, boolean needResume, int speedLimitDegree) {
        this.threadid = threadid;
        this.url = url;
        this.file = file;
        this.start = start;
        this.end = end;
        this.threadlistener = listener;
        this.needResume = needResume;
        this.downloadsize = 0;
        this.filepoint = 0;
        this.reqStop = false;
        this.retryNum = 0;
        this.speedLimitDegree = speedLimitDegree;
    }

    public int getThreadId() {
        return this.threadid;
    }

    public void stopDownload() {
        Log.d(TAG, "stopDownload " + this.threadid);
        this.reqStop = true;
    }

    public void setFinishListener(IDownloadThreadFinishListener listener) {
        this.mFinishListener = listener;
    }

    public long getDownloadSize() {
        return this.downloadsize;
    }

    private HttpURLConnection connect(long startpoint) {
        try {
            HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(60000);
            conn.setRequestMethod(HTTP.GET);
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", this.url.toString());
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty(HTTP.CONNECTION, HTTP.KEEP_ALIVE);
            if (!this.needResume) {
                return conn;
            }
            conn.setRequestProperty(HTTP.RANGE, "bytes=" + startpoint + "-" + this.end);
            return conn;
        } catch (Exception e) {
            return null;
        }
    }

    private int download(HttpURLConnection conn, long needsize, long startpiont) {
        try {
            int sleepTime = (this.speedLimitDegree + 1) * 200;
            int readSleepTime = (this.speedLimitDegree + 1) * 10;
            InputStream inStream = conn.getInputStream();
            RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
            accessFile.seek(startpiont);
            byte[] buffer = new byte[8192];
            int writeSize = 0;
            int rtn = 0;
            while (!this.reqStop) {
                int len;
                if (VERSION.SDK_INT >= 21) {
                    len = inStream.read(buffer);
                    if (this.reqStop) {
                        this.filepoint = accessFile.getFilePointer();
                        accessFile.close();
                        inStream.close();
                        conn.disconnect();
                        this.threadlistener = null;
                        return 0;
                    } else if (len != -1) {
                        try {
                            accessFile.write(buffer, 0, len);
                            if (readSleepTime != 0) {
                                sleep((long) readSleepTime);
                            }
                        } catch (IOException e) {
                            Log.d(TAG, " id : " + this.threadid + "  error : " + Download.ERROR_NOENOUGHSPACE);
                            this.threadlistener.onError(this.threadid, 4, Download.ERROR_NOENOUGHSPACE, this.reqStop);
                            e.printStackTrace();
                            rtn = -1;
                        }
                        try {
                            this.downloadsize += (long) len;
                            writeSize += len;
                            this.filepoint = accessFile.getFilePointer();
                            this.threadlistener.onProgress(this.threadid, (long) len, this.filepoint);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Log.d(TAG, " id : " + this.threadid + "  error : " + 1);
                            e2.printStackTrace();
                            rtn = 3;
                        }
                    } else if (((long) writeSize) >= needsize) {
                        Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" threadId:").append(this.threadid).append(" download complete!").toString());
                        rtn = 1;
                    } else {
                        Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" threadId:").append(this.threadid).append(" read len = -1 but writeSize < needSize").toString());
                        rtn = 2;
                    }
                } else if (inStream.available() > 0) {
                    this.no_data_timestamp = -1;
                    len = inStream.read(buffer);
                    if (len != -1) {
                        try {
                            accessFile.write(buffer, 0, len);
                            this.downloadsize += (long) len;
                            writeSize += len;
                            this.filepoint = accessFile.getFilePointer();
                            this.threadlistener.onProgress(this.threadid, (long) len, this.filepoint);
                        } catch (IOException e3) {
                            Log.d(TAG, " id : " + this.threadid + "  error : " + Download.ERROR_NOENOUGHSPACE);
                            this.threadlistener.onError(this.threadid, 4, Download.ERROR_NOENOUGHSPACE, this.reqStop);
                            e3.printStackTrace();
                            rtn = -1;
                        }
                    } else {
                        continue;
                    }
                } else if (((long) writeSize) >= needsize) {
                    Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" threadId:").append(this.threadid).append(" download complete!").toString());
                    rtn = 1;
                    break;
                } else {
                    sleep((long) sleepTime);
                    if (this.no_data_timestamp == -1) {
                        this.no_data_timestamp = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - this.no_data_timestamp > 60000) {
                        rtn = 2;
                        break;
                    }
                }
            }
            this.filepoint = accessFile.getFilePointer();
            accessFile.close();
            inStream.close();
            conn.disconnect();
            return rtn;
        } catch (Exception e22) {
            e22.printStackTrace();
            Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" thread: ").append(this.threadid).append(Download.ERROR_UNKNOWN).toString());
            if (this.threadlistener != null) {
                this.threadlistener.onError(this.threadid, 0, Download.ERROR_UNKNOWN, this.reqStop);
            }
            return -1;
        }
    }

    public void run() {
        try {
            long reqsize = (this.end - this.start) + 1;
            this.downloadsize = 0;
            Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" threadId:").append(this.threadid).append("require download size: ").append(reqsize).toString());
            int reqCode = 200;
            if (this.needResume) {
                reqCode = 206;
            }
            long startpoint = this.start;
            long needsize = reqsize;
            this.retryTimeOut = 0;
            this.isStarted = false;
            while (this.retryTimeOut < 10) {
                int code = -1;
                HttpURLConnection conn = null;
                startpoint = this.start + this.downloadsize;
                needsize = reqsize - this.downloadsize;
                this.retryNum = 0;
                while (this.retryNum < 10) {
                    if (!this.reqStop) {
                        conn = connect(startpoint);
                        if (conn != null) {
                            try {
                                code = conn.getResponseCode();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (code == reqCode) {
                                break;
                            }
                            conn.disconnect();
                        }
                        this.retryNum++;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    } else if (this.threadlistener != null) {
                        this.threadlistener.onStopped(this.threadid, this.downloadsize, this.filepoint, this.reqStop);
                        return;
                    } else {
                        return;
                    }
                }
                if (code != reqCode) {
                    Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" thread: ").append(this.threadid).append(Download.ERROR_NETWORK).toString());
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (this.threadlistener != null) {
                        this.threadlistener.onError(this.threadid, 1, Download.ERROR_NETWORK, this.reqStop);
                        return;
                    }
                    return;
                }
                if (!(this.isStarted || this.threadlistener == null)) {
                    this.isStarted = true;
                    this.threadlistener.onStarted(this.threadid);
                }
                int rtn = download(conn, needsize, startpoint);
                if (rtn == -1) {
                    return;
                }
                if (rtn == 1 || rtn == 0) {
                    if (this.threadlistener != null) {
                        this.threadlistener.onStopped(this.threadid, this.downloadsize, this.filepoint, this.reqStop);
                    }
                    if (rtn == 0) {
                        if (this.file != null) {
                            Log.d(TAG, "file " + this.file.getName() + " download thread " + this.threadid + "finish");
                        }
                        if (this.reqStop && this.mFinishListener != null) {
                            this.mFinishListener.onFinish();
                            return;
                        }
                        return;
                    }
                    return;
                } else if (rtn == 2 || rtn == 3) {
                    if (!this.needResume || this.retryTimeOut >= 10) {
                        Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" thread: ").append(this.threadid).append(" has timeout!").toString());
                        if (this.threadlistener != null) {
                            this.threadlistener.onError(this.threadid, 1, Download.ERROR_NETWORK, this.reqStop);
                            return;
                        }
                        return;
                    }
                    this.retryTimeOut++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e22) {
                        e22.printStackTrace();
                    }
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            if (this.file != null) {
                Log.d(TAG, new StringBuilder(String.valueOf(this.file.getName())).append(" thread: ").append(this.threadid).append(Download.ERROR_UNKNOWN).toString());
            }
            if (this.threadlistener != null) {
                this.threadlistener.onError(this.threadid, 0, Download.ERROR_UNKNOWN, this.reqStop);
            }
        }
    }
}
