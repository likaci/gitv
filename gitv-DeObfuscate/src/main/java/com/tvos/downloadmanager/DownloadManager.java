package com.tvos.downloadmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.tvos.downloadmanager.data.DownloadDataDB;
import com.tvos.downloadmanager.data.DownloadInfo;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.IDownloadData;
import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.Download;
import com.tvos.downloadmanager.download.DownloadThreadPool;
import com.tvos.downloadmanager.download.IDownloadStatusListener;
import com.tvos.downloadmanager.download.IDownloadTaskSizeListener;
import com.tvos.downloadmanager.download.IDownloadThreadFinishListener;
import com.tvos.downloadmanager.process.AutoDownload;
import com.tvos.downloadmanager.process.BaseProcess;
import com.tvos.downloadmanager.process.EnqueueProcess;
import com.tvos.downloadmanager.process.PauseProcess;
import com.tvos.downloadmanager.process.RemoveProcess;
import com.tvos.downloadmanager.process.ResumeProcess;
import com.tvos.downloadmanager.process.StartProcess;
import java.util.List;

public class DownloadManager implements IDownloadManager {
    private static final int CMD_DOWNLOAD_AUTODOWNLOADTASK = 6;
    private static final int CMD_DOWNLOAD_AUTOSTARTTHREAD = 7;
    private static final int CMD_DOWNLOAD_ENQUEUE = 1;
    private static final int CMD_DOWNLOAD_PAUSE = 4;
    private static final int CMD_DOWNLOAD_QUIT = 0;
    private static final int CMD_DOWNLOAD_REMOVE = 2;
    private static final int CMD_DOWNLOAD_RESUME = 5;
    private static final int CMD_DOWNLOAD_START = 3;
    public static final String DEFAULT_DB_NAME = "downloadmanager.db";
    public static final int DEGREE_HIGH_SPEEDLIMIT = 3;
    public static final int DEGREE_LOW_SPEEDLIMIT = 1;
    public static final int DEGREE_MEDIUM_SPEEDLIMIT = 2;
    private static final String TAG = "DownloadManager";
    private AutoDownload mAutoDownload = null;
    private Context mContext = null;
    private String mDBName = "downloadmanager.db";
    private Download mDownload = null;
    private IDownloadData mDownloadData = null;
    private IDownloadThreadFinishListener mDownloadThreadFinishListener = new C20702();
    private DownloadThreadPool mDownloadThreadPool;
    private BaseProcess mEnqueueProcess = null;
    private HandlerRunnable mHandlerRunnable = null;
    private BaseProcess mPauseProcess = null;
    private BaseProcess mRemoveProcess = null;
    private BaseProcess mResumeProcess = null;
    private IDownloadStatusListener mSetStatusListener = null;
    private BaseProcess mStartProcess = null;
    private IDownloadTaskSizeListener mTaskSizeListener = new C20691();
    private Thread mThread = null;
    private Handler mthread_handler = null;

    class C20691 implements IDownloadTaskSizeListener {
        C20691() {
        }

        public void onDownloadSizeReduced(int size) {
            Log.d(DownloadManager.TAG, "onDownloadSizeReduced, task size :" + size);
            if (DownloadManager.this.mthread_handler != null) {
                DownloadManager.this.mthread_handler.sendMessage(DownloadManager.this.mthread_handler.obtainMessage(6, Integer.valueOf(5 - size)));
            }
        }

        public void onDownloadSizeIncreased(int size) {
        }
    }

    class C20702 implements IDownloadThreadFinishListener {
        C20702() {
        }

        public void onFinish() {
            if (DownloadManager.this.mthread_handler != null) {
                DownloadManager.this.mthread_handler.sendMessage(DownloadManager.this.mthread_handler.obtainMessage(7));
            }
        }
    }

    class HandlerRunnable implements Runnable {

        class C20711 extends Handler {
            C20711() {
            }

            public void handleMessage(Message msg) {
                int id;
                int speedLimitDegree;
                switch (msg.what) {
                    case 0:
                        Looper.myLooper().quit();
                        return;
                    case 1:
                        if (msg.obj != null) {
                            RequestInfo info = msg.obj;
                            if (DownloadManager.this.mEnqueueProcess != null) {
                                DownloadManager.this.mEnqueueProcess.process(info);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        if (msg.obj != null) {
                            id = ((Integer) msg.obj).intValue();
                            if (DownloadManager.this.mRemoveProcess != null) {
                                DownloadManager.this.mRemoveProcess.process(id);
                                return;
                            }
                            return;
                        }
                        return;
                    case 3:
                        id = msg.arg1;
                        speedLimitDegree = msg.arg2;
                        if (DownloadManager.this.mStartProcess != null) {
                            DownloadManager.this.mStartProcess.process(id, speedLimitDegree);
                            return;
                        }
                        return;
                    case 4:
                        if (msg.obj != null) {
                            id = ((Integer) msg.obj).intValue();
                            if (DownloadManager.this.mPauseProcess != null) {
                                DownloadManager.this.mPauseProcess.process(id);
                                return;
                            }
                            return;
                        }
                        return;
                    case 5:
                        id = msg.arg1;
                        speedLimitDegree = msg.arg2;
                        if (DownloadManager.this.mResumeProcess != null) {
                            DownloadManager.this.mResumeProcess.process(id, speedLimitDegree);
                            return;
                        }
                        return;
                    case 6:
                        if (msg.obj != null) {
                            int remainSize = ((Integer) msg.obj).intValue();
                            if (DownloadManager.this.mAutoDownload != null) {
                                DownloadManager.this.mAutoDownload.startWaitedTasks(remainSize);
                                return;
                            }
                            return;
                        }
                        return;
                    case 7:
                        if (DownloadManager.this.mAutoDownload != null) {
                            DownloadManager.this.mAutoDownload.startDownloadRemainFilesInTasks();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        HandlerRunnable() {
        }

        public void run() {
            Looper.prepare();
            Log.d(DownloadManager.TAG, "new mthread_handler");
            DownloadManager.this.mthread_handler = new C20711();
            Looper.loop();
            Log.d(DownloadManager.TAG, "mThread complete");
        }
    }

    private void init(Context context, String dbName, int speedLimitDegree) {
        Log.d(TAG, "init, dbName = " + dbName);
        this.mContext = context.getApplicationContext();
        this.mDBName = dbName;
        this.mDownloadThreadPool = new DownloadThreadPool(speedLimitDegree);
        this.mDownloadThreadPool.setDownloadThreadFinishListener(this.mDownloadThreadFinishListener);
        this.mDownloadData = new DownloadDataDB(this.mContext, dbName);
        this.mDownload = new Download(this.mDownloadData, this.mDownloadThreadPool);
        this.mDownloadThreadPool.setmIDownload(this.mDownload);
        this.mDownload.setTaskSizeListener(this.mTaskSizeListener);
        this.mAutoDownload = new AutoDownload();
        this.mAutoDownload.init(this.mDownload, this.mDownloadData);
        this.mEnqueueProcess = new EnqueueProcess();
        this.mEnqueueProcess.setDownload(this.mDownload);
        this.mEnqueueProcess.setDownloadData(this.mDownloadData);
        this.mPauseProcess = new PauseProcess();
        this.mPauseProcess.setDownload(this.mDownload);
        this.mPauseProcess.setDownloadData(this.mDownloadData);
        this.mResumeProcess = new ResumeProcess();
        this.mResumeProcess.setDownload(this.mDownload);
        this.mResumeProcess.setDownloadData(this.mDownloadData);
        this.mRemoveProcess = new RemoveProcess();
        this.mRemoveProcess.setDownload(this.mDownload);
        this.mRemoveProcess.setDownloadData(this.mDownloadData);
        this.mStartProcess = new StartProcess();
        this.mStartProcess.setDownload(this.mDownload);
        this.mStartProcess.setDownloadData(this.mDownloadData);
        this.mHandlerRunnable = new HandlerRunnable();
        this.mThread = new Thread(this.mHandlerRunnable);
        this.mThread.start();
    }

    public DownloadManager(Context context) {
        init(context, "downloadmanager.db", 0);
    }

    public DownloadManager(Context context, String dbName) {
        init(context, dbName, 0);
    }

    public DownloadManager(Context context, String dbName, int speedLimitDegree) {
        init(context, dbName, speedLimitDegree);
    }

    public long getDownloadSize(int id) {
        if (this.mDownloadData.isValid(id) && this.mDownload != null) {
            if (this.mDownloadData.getDownloadStatus(id) == 4) {
                return this.mDownload.getDownloadSize(id);
            }
            DownloadParam param = this.mDownloadData.getDownloadParm(id);
            if (param != null) {
                return param.getDownloadSize();
            }
        }
        return 0;
    }

    public List<DownloadInfo> getDownloadInfoList() {
        Log.d(TAG, "getDownloadInfoList");
        return this.mDownloadData.getDownloadInfoList();
    }

    public int getDownloadStatus(int id) {
        Log.d(TAG, "getDownloadStatus id=" + id);
        return this.mDownloadData.getDownloadStatus(id);
    }

    public boolean enqueue(RequestInfo requestInfo) {
        if (requestInfo == null) {
            return false;
        }
        Log.d(TAG, "enqueue uri=" + requestInfo.getUri());
        try {
            if (this.mthread_handler == null) {
                return true;
            }
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(1, requestInfo));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(int id) {
        Log.d(TAG, "remove id=" + id);
        try {
            if (this.mthread_handler != null) {
                this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(2, Integer.valueOf(id)));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean start(int id) {
        Log.d(TAG, "start id=" + id);
        if (this.mDownloadData == null || this.mthread_handler == null) {
            return false;
        }
        if (this.mDownloadData.isValid(id)) {
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(3, id, 0, Boolean.valueOf(false)));
            return true;
        }
        Log.d(TAG, "start id " + id + "is not valid");
        return false;
    }

    public boolean start(int id, int speedLimitDegree) {
        Log.d(TAG, "start id=" + id + " isSpeedLimited " + speedLimitDegree);
        if (this.mDownloadData == null || this.mthread_handler == null) {
            return false;
        }
        if (this.mDownloadData.isValid(id)) {
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(3, id, speedLimitDegree));
            return true;
        }
        Log.d(TAG, "start id " + id + "is not valid");
        return false;
    }

    public boolean pause(int id) {
        Log.d(TAG, "pause id=" + id);
        if (this.mDownloadData == null || this.mthread_handler == null) {
            return false;
        }
        if (this.mDownloadData.isValid(id)) {
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(4, Integer.valueOf(id)));
            return true;
        }
        Log.d(TAG, "pause id " + id + "is not valid");
        return false;
    }

    public boolean resume(int id) {
        Log.d(TAG, "resume id=" + id);
        if (this.mDownloadData == null || this.mthread_handler == null) {
            return false;
        }
        if (this.mDownloadData.isValid(id)) {
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(5, id, 0, Boolean.valueOf(false)));
            return true;
        }
        Log.d(TAG, "resume id " + id + "is not valid");
        return false;
    }

    public boolean resume(int id, int speedLimitDegree) {
        Log.d(TAG, "resume id=" + id + "isSpeedLimited " + speedLimitDegree);
        if (this.mDownloadData == null || this.mthread_handler == null) {
            return false;
        }
        if (this.mDownloadData.isValid(id)) {
            this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(5, id, speedLimitDegree));
            return true;
        }
        Log.d(TAG, "resume id " + id + "is not valid");
        return false;
    }

    public void startAll() {
        List<DownloadInfo> downloadInfos = this.mDownloadData.getDownloadInfoList();
        if (downloadInfos == null || downloadInfos.isEmpty()) {
            Log.d(TAG, "startall size0");
            return;
        }
        Log.d(TAG, "startall size" + downloadInfos.size());
        for (DownloadInfo info : downloadInfos) {
            if (info.getStatus() == 4 || info.getStatus() == 3 || info.getStatus() == 5 || info.getStatus() == 1) {
                Log.d(TAG, info.getDownloadId() + " has started!");
            } else if (info.getStatus() == 2) {
                resume(info.getDownloadId());
            } else {
                start(info.getDownloadId());
            }
        }
    }

    public void stopAll() {
        List<DownloadInfo> downloadInfos = this.mDownloadData.getDownloadInfoList();
        if (downloadInfos == null || downloadInfos.isEmpty()) {
            Log.d(TAG, "stopall size0");
            return;
        }
        Log.d(TAG, "stopall size" + downloadInfos.size());
        for (DownloadInfo info : downloadInfos) {
            pause(info.getDownloadId());
        }
    }

    public void setListener(IDownloadStatusListener statusListener) {
        Log.d(TAG, "setListener");
        this.mSetStatusListener = statusListener;
        this.mDownloadData.setListener(this.mSetStatusListener);
        this.mDownload.setListener(this.mSetStatusListener);
        this.mEnqueueProcess.setDownloadStatusListener(this.mSetStatusListener);
        this.mPauseProcess.setDownloadStatusListener(this.mSetStatusListener);
        this.mResumeProcess.setDownloadStatusListener(this.mSetStatusListener);
        this.mRemoveProcess.setDownloadStatusListener(this.mSetStatusListener);
        this.mStartProcess.setDownloadStatusListener(this.mSetStatusListener);
    }

    public void release() {
        Log.d(TAG, "release");
        if (this.mStartProcess != null) {
            this.mStartProcess.release();
            this.mStartProcess = null;
        }
        if (this.mPauseProcess != null) {
            this.mPauseProcess.release();
            this.mPauseProcess = null;
        }
        if (this.mResumeProcess != null) {
            this.mResumeProcess.release();
            this.mResumeProcess = null;
        }
        if (this.mRemoveProcess != null) {
            this.mRemoveProcess.release();
            this.mRemoveProcess = null;
        }
        if (this.mEnqueueProcess != null) {
            this.mEnqueueProcess.release();
            this.mEnqueueProcess = null;
        }
        if (this.mAutoDownload != null) {
            this.mAutoDownload.release();
            this.mAutoDownload = null;
        }
        this.mSetStatusListener = null;
        synchronized (this.mDownload.releaseLock) {
            if (!(this.mDownload == null || this.mDownload.quit())) {
                try {
                    this.mDownload.releaseLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "quit");
            if (this.mDownloadData != null) {
                this.mDownloadData.release();
            }
            if (this.mthread_handler != null) {
                this.mthread_handler.sendMessage(this.mthread_handler.obtainMessage(0));
                this.mthread_handler = null;
            }
            this.mHandlerRunnable = null;
            this.mThread = null;
        }
        this.mContext = null;
    }

    public long getFileSize(int id) {
        if (this.mDownloadData != null && this.mDownloadData.isValid(id)) {
            DownloadParam param = this.mDownloadData.getDownloadParm(id);
            if (param != null) {
                return param.getFileSize();
            }
        }
        return 0;
    }

    public DownloadInfo getDownloadInfo(int id) {
        if (this.mDownloadData == null) {
            return null;
        }
        return this.mDownloadData.getDownloadInfo(id);
    }

    public DownloadInfo getDownloadInfoByUrl(String url) {
        if (this.mDownloadData == null) {
            return null;
        }
        return this.mDownloadData.getDownloadInfoByUrl(url);
    }

    public long getSpeed(int id) {
        if (!this.mDownloadData.isValid(id) || this.mDownload == null) {
            return 0;
        }
        return this.mDownload.getSpeed(id);
    }
}
