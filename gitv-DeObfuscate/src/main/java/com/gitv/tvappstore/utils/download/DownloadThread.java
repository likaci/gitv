package com.gitv.tvappstore.utils.download;

import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.apache.http.conn.ConnectTimeoutException;
import org.cybergarage.http.HTTP;

public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private long downloadSize = 0;
    private DownloadListener mDownloadListener = null;
    private long mFileSize = 0;
    private boolean mIsPause = false;
    private boolean mIsStop = false;
    private String mName;
    private int mProgress = 0;
    private int mResponseCode = 0;
    private File mSaveFile;
    private File mSaveFileDir;
    private URL mUrl = null;
    private String mUrlstr = null;
    private long savelength = 0;

    public DownloadThread(String name, String urlstr, DownloadListener listener, String saveFileDir) {
        this.mUrlstr = urlstr;
        this.mDownloadListener = listener;
        this.mName = name;
        this.mSaveFileDir = new File(saveFileDir);
        if (!this.mSaveFileDir.exists()) {
            this.mSaveFileDir.mkdirs();
        }
    }

    public void run() {
        String filename;
        int isFinish = 1;
        try {
            this.mSaveFile = new File(this.mSaveFileDir, getFileName());
            this.mDownloadListener.onStart();
            this.mUrl = new URL(this.mUrlstr);
            HttpURLConnection http = (HttpURLConnection) this.mUrl.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setRequestMethod(HTTP.GET);
            http.setRequestProperty("Accept-Language", "zh-CN");
            http.setRequestProperty("Referer", this.mUrl.toString());
            http.setRequestProperty("Charset", "UTF-8");
            http.setRequestProperty(HTTP.CONNECTION, HTTP.KEEP_ALIVE);
            this.savelength = this.mSaveFile.length();
            if (!this.mSaveFile.exists() || this.savelength <= 0) {
                this.mSaveFile.createNewFile();
            } else {
                http.setRequestProperty(HTTP.RANGE, "bytes=" + this.savelength + "-");
            }
            this.mResponseCode = getConnection(http);
            InputStream inStream;
            byte[] buffer;
            FileOutputStream threadfile;
            int offset;
            if (this.mResponseCode == 200 || this.mResponseCode == 206) {
                this.mFileSize = (long) http.getContentLength();
                if (this.mFileSize <= 0) {
                    isFinish = 4;
                    Log.e(TAG, "DownloadThread ---ThreadRun() >>>> get filesize error mFileSize =" + this.mFileSize);
                }
                inStream = http.getInputStream();
                buffer = new byte[4096];
                threadfile = new FileOutputStream(this.mSaveFile, true);
                this.downloadSize += this.savelength;
                while (true) {
                    offset = inStream.read(buffer, 0, 4096);
                    if (offset != -1) {
                        break;
                    } else if (!!this.mIsStop || this.mIsPause) {
                        isFinish = 3;
                        this.mDownloadListener.onStop();
                    } else {
                        threadfile.write(buffer, 0, offset);
                        this.downloadSize += (long) offset;
                        int progress = (int) ((this.downloadSize * 100) / (this.savelength + this.mFileSize));
                        if (progress > this.mProgress) {
                            this.mDownloadListener.onProgress(progress);
                            this.mProgress = progress;
                        }
                    }
                }
                isFinish = 3;
                this.mDownloadListener.onStop();
                if (this.mIsStop) {
                    this.mDownloadListener.onProgress(0);
                    this.mSaveFile.delete();
                }
                threadfile.close();
                inStream.close();
                if (this.downloadSize == 0 && this.downloadSize == this.mFileSize + this.savelength) {
                    filename = this.mSaveFile.getName();
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                        this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                    } else {
                        Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                        this.mSaveFile.delete();
                        this.mDownloadListener.onComplete(2, "", this.mName);
                    }
                } else if (this.mDownloadListener != null || this.mSaveFile == null) {
                    Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                    this.mDownloadListener.onComplete(isFinish, "", this.mName);
                } else {
                    this.mDownloadListener.onComplete(isFinish, this.mSaveFile.getAbsolutePath(), this.mName);
                }
                DownloadManager.getInstance().stop(this.mUrlstr);
                return;
            } else if (this.mResponseCode == 404) {
                isFinish = 4;
                Log.e(TAG, "DownloadThread ---ThreadRun() >>>> http.getResponseCode() isFinish is 404");
                this.mSaveFile.delete();
            } else {
                Log.e(TAG, "DownloadThread ---ThreadRun() >>>> http.getResponseCode() :" + http.getResponseCode());
                if (this.mFileSize <= 0) {
                    isFinish = 4;
                }
                inStream = http.getInputStream();
                buffer = new byte[4096];
                threadfile = new FileOutputStream(this.mSaveFile, true);
                this.downloadSize += this.savelength;
                while (true) {
                    offset = inStream.read(buffer, 0, 4096);
                    if (offset != -1) {
                        if (!this.mIsStop) {
                            break;
                        }
                        break;
                    }
                    break;
                }
                isFinish = 3;
                this.mDownloadListener.onStop();
                if (this.mIsStop) {
                    this.mDownloadListener.onProgress(0);
                    this.mSaveFile.delete();
                }
                threadfile.close();
                inStream.close();
                if (this.downloadSize == 0) {
                }
                if (this.mDownloadListener != null) {
                }
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(isFinish, "", this.mName);
                DownloadManager.getInstance().stop(this.mUrlstr);
                return;
            }
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(isFinish, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(isFinish, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (ConnectTimeoutException e) {
            Log.e(TAG, "DownloadThread---ThreadRun()-> ConnectTimeoutException ---mName:" + this.mName + "mUrlstr:" + this.mUrlstr + e.getMessage());
            isFinish = 7;
            this.mSaveFile.delete();
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(7, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (SocketTimeoutException e2) {
            Log.e(TAG, "DownloadThread---ThreadRun()-> SocketTimeoutException ---mName:" + this.mName + "mUrlstr:" + this.mUrlstr + e2.getMessage());
            isFinish = 7;
            this.mSaveFile.delete();
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(7, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (ProtocolException e3) {
            Log.e(TAG, "DownloadThread---ThreadRun()-> ProtocolException ---mName:" + this.mName + "mUrlstr:" + this.mUrlstr + e3.getMessage());
            isFinish = 6;
            this.mSaveFile.delete();
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(6, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(6, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (SocketException e4) {
            Log.e(TAG, "DownloadThread---ThreadRun()-> SocketException ---mName:" + this.mName + "mUrlstr:" + this.mUrlstr + e4.getMessage());
            isFinish = 7;
            this.mSaveFile.delete();
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(7, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(7, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (Exception e5) {
            Log.e(TAG, "DownloadThread---ThreadRun()-> catch Exception ---mName:" + this.mName + "mUrlstr:" + this.mUrlstr + e5.getMessage());
            isFinish = 5;
            this.mSaveFile.delete();
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(5, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(5, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        } catch (Throwable th) {
            if (this.downloadSize != 0 && this.downloadSize == this.mFileSize + this.savelength) {
                filename = this.mSaveFile.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                if (this.mSaveFile.renameTo(new File(this.mSaveFileDir, filename))) {
                    this.mDownloadListener.onComplete(0, this.mSaveFileDir + "/" + filename, this.mName);
                } else {
                    Log.e(TAG, "DownloadThread---ThreadRun() >>>> renameTo fialed---");
                    this.mSaveFile.delete();
                    this.mDownloadListener.onComplete(2, "", this.mName);
                }
            } else if (this.mDownloadListener == null || this.mSaveFile == null) {
                Log.e(TAG, "---ThreadRun() >>>> mDownloadListener is null");
                this.mDownloadListener.onComplete(isFinish, "", this.mName);
            } else {
                this.mDownloadListener.onComplete(isFinish, this.mSaveFile.getAbsolutePath(), this.mName);
            }
            DownloadManager.getInstance().stop(this.mUrlstr);
        }
    }

    private String getFileName() {
        String filename = new StringBuilder(String.valueOf(this.mUrlstr.substring(this.mUrlstr.lastIndexOf(47) + 1))).append(".tmp").toString();
        if (!TextUtils.isEmpty(filename)) {
            return filename;
        }
        throw new RuntimeException("DownloadThread--getFileName() >>>> Unkown file name---");
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

    private long getFileSize(String urlStr) {
        HttpURLConnection http = null;
        try {
            URL url = new URL(urlStr);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);
            http.setRequestMethod(HTTP.GET);
            http.setRequestProperty("Accept-Language", "zh-CN");
            http.setRequestProperty("Referer", url.toString());
            http.setRequestProperty("Charset", "UTF-8");
            http.setRequestProperty(HTTP.CONNECTION, HTTP.KEEP_ALIVE);
            this.mResponseCode = getConnection(http);
            if (this.mResponseCode == 200) {
                long fileSize = (long) http.getContentLength();
                if (fileSize <= 0) {
                    Log.e(TAG, "DownloadThread ---getFileSize() ->get filesize error mFileSize =" + this.mFileSize);
                    if (http != null) {
                        http.disconnect();
                    }
                    return 0;
                } else if (http == null) {
                    return fileSize;
                } else {
                    http.disconnect();
                    return fileSize;
                }
            } else if (this.mResponseCode == 404) {
                Log.e(TAG, "DownloadThread ---getFileSize() >>>> http.getResponseCode()-isFinish is 404");
                if (http != null) {
                    http.disconnect();
                }
                return 0;
            } else {
                Log.e(TAG, "DownloadThread---getFileSize() >>>> http.getResponseCode() :" + http.getResponseCode());
                if (http != null) {
                    http.disconnect();
                }
                return 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "DownloadThread---getFileSize() >>>> Exception--------mUrlstr:" + this.mUrlstr + e.getMessage());
            e.printStackTrace();
            if (http != null) {
                http.disconnect();
            }
        } catch (Throwable th) {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    private int getConnection(HttpURLConnection http) {
        long j;
        int i = 404;
        int num = 0;
        while (num < 3) {
            try {
                int stateCode = http.getResponseCode();
                String str = TAG;
                Log.e(str, "DownloadThread---getConnection() >>>> stateCode=" + stateCode);
                if (stateCode == str || stateCode == 206) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "DownloadThread---getConnection() >>>> InterruptedException:" + e.getMessage());
                    }
                    num++;
                    return stateCode;
                } else if (stateCode != i || num < 3) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e2) {
                        Log.e(TAG, "DownloadThread---getConnection() >>>> InterruptedException:" + e2.getMessage());
                    }
                    num++;
                } else {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e22) {
                        Log.e(TAG, "DownloadThread---getConnection() >>>> InterruptedException:" + e22.getMessage());
                    }
                    num++;
                    return i;
                }
            } catch (Exception e3) {
                j = TAG;
                Log.e(j, "DownloadThread---getConnection() >>>>  exception" + e3.getMessage());
                try {
                    Thread.sleep(j);
                } catch (InterruptedException e222) {
                    Log.e(TAG, "DownloadThread---getConnection() >>>> InterruptedException:" + e222.getMessage());
                }
                num++;
            } finally {
                j = 800;
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e2222) {
                    Log.e(TAG, "DownloadThread---getConnection() >>>> InterruptedException:" + e2222.getMessage());
                }
                num++;
            }
        }
        return 7;
    }
}
