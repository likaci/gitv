package com.tvos.downloadmanager.download;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.tvos.apps.utils.MD5Utils;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.DownloadProgressRecord;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.cybergarage.http.HTTP;

public abstract class DownloadTask extends Thread implements IDownloadTask {
    private static final int ACTIONTTYPE_REMOVE = 2;
    private static final int ACTIONTYPE_CLOSE = 3;
    private static final int ACTIONTYPE_STOP = 1;
    private static final int CMD_QUIT = 4;
    private static final int CMD_REMOVE = 2;
    private static final int CMD_STARTREST = 3;
    private static final int CMD_STOP = 1;
    private static final String TAG = "DownloadTask";
    private Object mDownloadActionLock = new Object();
    protected DownloadParam mDownloadParam;
    private Map<Integer, Integer> mDownloadStageMap = null;
    private IDownloadTaskListener mDownloadTaskListener;
    private int mRecentActionType = 0;
    protected SpeedUpdater mSpeedUpdater;
    private Handler mThreadHandler;

    class C20731 extends Handler {
        C20731() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DownloadTask.this.stopDownloadImp();
                    break;
                case 2:
                    DownloadTask.this.removeDownloadImp();
                    break;
                case 3:
                    DownloadTask.this.startRestDownloadImp();
                    break;
                case 4:
                    Log.d(DownloadTask.TAG, "quit");
                    Looper.myLooper().quit();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    protected class SpeedUpdater {
        private static final long INERVAL_TIME_INSEC = 1;
        private static final long INTERVAL_TIME_INMILLS = 1000;
        private static final int MSG_UPDATE = 1;
        private final String TAG_SPEEDUPDATER;
        private long lastDownloadedSize = 0;
        private Handler mHandler;
        private HandlerThread mHandlerThread;
        private Object mLocker = new Object();
        private long speed = 0;
        private boolean stopped = true;

        public SpeedUpdater(String title) {
            this.TAG_SPEEDUPDATER = "DownloadTask_" + SpeedUpdater.class.getSimpleName() + "_" + title;
            this.mHandlerThread = new HandlerThread(new StringBuilder(String.valueOf(title)).append("_speedupdater_handler_thread").toString(), 10);
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            synchronized (SpeedUpdater.this.mLocker) {
                                Log.d(SpeedUpdater.this.TAG_SPEEDUPDATER, "handleMessage, MSG_UPDATE, stop status is " + SpeedUpdater.this.stopped);
                                if (!SpeedUpdater.this.stopped) {
                                    SpeedUpdater.this.update();
                                    SpeedUpdater.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                                }
                            }
                            return;
                        default:
                            return;
                    }
                }
            };
        }

        private void update() {
            if (DownloadTask.this.mDownloadParam != null) {
                long downloadedSize = DownloadTask.this.mDownloadParam.getDownloadSize();
                this.speed = (downloadedSize - this.lastDownloadedSize) / INERVAL_TIME_INSEC;
                Log.d(this.TAG_SPEEDUPDATER, "update, lastDownloadedSize = " + this.lastDownloadedSize + " , downloadedSize = " + downloadedSize + " , speed = " + this.speed);
                if (this.speed < 0) {
                    this.speed = 0;
                }
                this.lastDownloadedSize = downloadedSize;
                if (DownloadTask.this.mDownloadTaskListener != null) {
                    DownloadTask.this.mDownloadTaskListener.onSpeedUpdated(DownloadTask.this.mDownloadParam.getId(), this.speed);
                } else {
                    Log.d(this.TAG_SPEEDUPDATER, "update, but param is null.");
                }
            }
        }

        public void start(long downloadedSize) {
            synchronized (this.mLocker) {
                Log.d(this.TAG_SPEEDUPDATER, "start, stop status is " + this.stopped);
                if (this.stopped) {
                    this.lastDownloadedSize = downloadedSize;
                    this.mHandler.sendEmptyMessage(1);
                    this.stopped = false;
                }
            }
        }

        public void stop() {
            synchronized (this.mLocker) {
                Log.d(this.TAG_SPEEDUPDATER, "stop, stop status is " + this.stopped);
                if (!this.stopped) {
                    this.mHandler.removeMessages(1);
                    this.speed = 0;
                    this.stopped = true;
                }
            }
        }

        public long getSpeed() {
            return this.speed;
        }

        public void close() {
            Log.d(this.TAG_SPEEDUPDATER, HTTP.CLOSE);
            stop();
            this.mHandlerThread.quit();
        }
    }

    public abstract long getDownloadSize();

    protected abstract void removeDownloadImp();

    protected abstract void startDownloadImp();

    protected abstract void startRestDownloadImp();

    protected abstract void stopDownloadImp();

    public DownloadTask(DownloadParam downloadParam) {
        this.mDownloadParam = downloadParam;
        this.mSpeedUpdater = new SpeedUpdater(this.mDownloadParam.getTitle());
    }

    public synchronized void startDownload() {
        this.mRecentActionType = 0;
        start();
    }

    public synchronized void startRestDownload() {
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendMessage(this.mThreadHandler.obtainMessage(3));
        }
    }

    public synchronized void stopDownload() {
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendMessage(this.mThreadHandler.obtainMessage(1));
        } else {
            synchronized (this.mDownloadActionLock) {
                this.mRecentActionType = 1;
            }
        }
    }

    public synchronized void removeDownload() {
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendMessage(this.mThreadHandler.obtainMessage(2));
        } else {
            synchronized (this.mDownloadActionLock) {
                this.mRecentActionType = 2;
            }
        }
    }

    public synchronized void close() {
        if (this.mThreadHandler != null) {
            this.mThreadHandler.sendEmptyMessage(4);
        } else {
            synchronized (this.mDownloadActionLock) {
                this.mRecentActionType = 3;
            }
        }
        this.mSpeedUpdater.close();
    }

    public long getSpeed() {
        return this.mSpeedUpdater.getSpeed();
    }

    public DownloadParam getCurrentDownloadParam() {
        return null;
    }

    public void setDownloadTaskListener(IDownloadTaskListener listener) {
        this.mDownloadTaskListener = listener;
    }

    protected void updateDownloadProgressRecord(int downloadId, int progress, DownloadProgressRecord parm) {
        if (this.mDownloadStageMap == null) {
            this.mDownloadStageMap = new HashMap();
        }
        if (!this.mDownloadStageMap.containsKey(Integer.valueOf(downloadId))) {
            this.mDownloadStageMap.put(Integer.valueOf(downloadId), Integer.valueOf(progress));
        }
        if (progress >= ((Integer) this.mDownloadStageMap.get(Integer.valueOf(downloadId))).intValue() + 5) {
            this.mDownloadStageMap.put(Integer.valueOf(downloadId), Integer.valueOf(progress));
            if (this.mDownloadTaskListener != null) {
                this.mDownloadTaskListener.onSaveProgress(parm);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r6 = this;
        r0 = "DownloadTask";
        r1 = "DownloadTask start";
        android.util.Log.d(r0, r1);
        android.os.Looper.prepare();
        r0 = new com.tvos.downloadmanager.download.DownloadTask$1;
        r0.<init>();
        r6.mThreadHandler = r0;
        r1 = r6.mDownloadActionLock;
        monitor-enter(r1);
        r0 = r6.mRecentActionType;	 Catch:{ all -> 0x006d }
        if (r0 != 0) goto L_0x004e;
    L_0x001a:
        r0 = r6.mDownloadParam;	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0046;
    L_0x001e:
        r0 = r6.mDownloadParam;	 Catch:{ all -> 0x006d }
        r2 = r0.getDownloadSize();	 Catch:{ all -> 0x006d }
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 > 0) goto L_0x0046;
    L_0x002a:
        r0 = r6.hasFileExistAndValid();	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0046;
    L_0x0030:
        r0 = r6.mDownloadTaskListener;	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x003f;
    L_0x0034:
        r0 = r6.mDownloadTaskListener;	 Catch:{ all -> 0x006d }
        r2 = r6.mDownloadParam;	 Catch:{ all -> 0x006d }
        r2 = r2.getId();	 Catch:{ all -> 0x006d }
        r0.onComplete(r2);	 Catch:{ all -> 0x006d }
    L_0x003f:
        r0 = r6.mSpeedUpdater;	 Catch:{ all -> 0x006d }
        r0.stop();	 Catch:{ all -> 0x006d }
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
    L_0x0045:
        return;
    L_0x0046:
        r6.startDownloadImp();	 Catch:{ all -> 0x006d }
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
        android.os.Looper.loop();
        goto L_0x0045;
    L_0x004e:
        r0 = r6.mRecentActionType;	 Catch:{ all -> 0x006d }
        r2 = 1;
        if (r0 != r2) goto L_0x006b;
    L_0x0053:
        r0 = r6.mDownloadTaskListener;	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0066;
    L_0x0057:
        r0 = r6.mDownloadParam;	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0066;
    L_0x005b:
        r0 = r6.mDownloadTaskListener;	 Catch:{ all -> 0x006d }
        r2 = r6.mDownloadParam;	 Catch:{ all -> 0x006d }
        r2 = r2.getId();	 Catch:{ all -> 0x006d }
        r0.onStopped(r2);	 Catch:{ all -> 0x006d }
    L_0x0066:
        r0 = r6.mSpeedUpdater;	 Catch:{ all -> 0x006d }
        r0.stop();	 Catch:{ all -> 0x006d }
    L_0x006b:
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
        goto L_0x0045;
    L_0x006d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x006d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.DownloadTask.run():void");
    }

    private boolean hasFileExistAndValid() {
        Log.d(TAG, "hasFileExistAndValid, mDownloadParam = " + this.mDownloadParam);
        if (this.mDownloadParam != null) {
            File file = new File(this.mDownloadParam.getDestination());
            if (file.exists()) {
                if (TextUtils.isEmpty(this.mDownloadParam.getMd5())) {
                    Log.d(TAG, "hasFileExistAndValid, md5 is empty");
                    file.delete();
                } else {
                    boolean checkMd5Suc = MD5Utils.verifyFileByMd5(this.mDownloadParam.getDestination(), this.mDownloadParam.getMd5());
                    Log.d(TAG, "hasFileExistAndValid, checkMd5Suc = " + checkMd5Suc + " , id = " + this.mDownloadParam.getId());
                    if (checkMd5Suc) {
                        this.mDownloadParam.setFileSize(file.length());
                        this.mDownloadParam.setDownloadSize(file.length());
                        return true;
                    }
                    file.delete();
                }
            }
        }
        return false;
    }

    public Map<Integer, Integer> getDownloadStageMap() {
        return this.mDownloadStageMap;
    }
}
