package com.tvos.downloadmanager.download;

import android.util.Log;
import com.tvos.apps.utils.CommonUtil;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.DownloadProgressRecord;
import com.tvos.downloadmanager.data.IDownloadData;
import java.util.ArrayList;
import java.util.Iterator;

public class Download implements IDownload {
    public static final int DOWNLOAD_STATUS_COMPLETE = 6;
    public static final int DOWNLOAD_STATUS_INIT = -1;
    public static final int DOWNLOAD_STATUS_PAUSED = 2;
    public static final int DOWNLOAD_STATUS_PAUSING = 3;
    public static final int DOWNLOAD_STATUS_STARTING = 5;
    public static final int DOWNLOAD_STATUS_STARTTED = 4;
    public static final int DOWNLOAD_STATUS_STOPPED = 0;
    public static final int DOWNLOAD_STATUS_WAIT = 1;
    public static final int ERRORCODE_DEVICEBUSY = 5;
    public static final int ERRORCODE_FILE_UNNORMAL = 3;
    public static final int ERRORCODE_MD5VERIFYFAIL = 8;
    public static final int ERRORCODE_NETWORKERROR = 1;
    public static final int ERRORCODE_NOENOUGHSPACE = 4;
    public static final int ERRORCODE_NORESUMEBROKEN = 2;
    public static final int ERRORCODE_P2PNOTSTARTED = 6;
    public static final int ERRORCODE_P2PPROGRESSNOTUPDATE = 7;
    public static final int ERRORCODE_UNKNOW = 0;
    public static final String ERROR_DEVICEBUSY = "device or resource busy";
    public static final String ERROR_FILE_UNNORMAL = "download file is unnormal";
    public static final String ERROR_MD5VERIFYFAIL = "md5 verification failed";
    public static final String ERROR_NETWORK = "connect error";
    public static final String ERROR_NOENOUGHSPACE = "no enough free space";
    public static final String ERROR_P2PNOTSTARTED = "p2p download can't be started";
    public static final String ERROR_P2PPROGRESSNOTUPDATE = "p2p download progress can't be updated";
    public static final String ERROR_RESUMEBROKEN = "server dosen't support resume broken";
    public static final String ERROR_UNKNOWN = "unknown error";
    private static final String TAG = "Download";
    private static final String TAG2 = "DownloadComplete";
    public static final int TASK_MAX = 5;
    private IDownloadData downloaddata;
    private boolean isQuit = false;
    private IDownloadStatusListener listener;
    private IDownloadTaskSizeListener mDownloadTaskSizeListener;
    private DownloadThreadPool mDownloadThreadPool;
    private int mTaskSize = 0;
    private IDownloadTaskListener mylistener = new IDownloadTaskListener() {
        public void onStopped(int id) {
            Log.d(Download.TAG, " onStopped , id : " + id);
            IDownloadTask task = Download.this.updateDownloadData(id, 2);
            if (task != null) {
                int size;
                synchronized (Download.this.tasksLock) {
                    Download.this.tasks.remove(task);
                    size = Download.this.tasks.size();
                    Download.this.mTaskSize = size;
                    Download.this.startDownloadRemainFilesInTasks();
                }
                synchronized (Download.this.releaseLock) {
                    if (size == 0) {
                        Download.this.releaseLock.notify();
                    }
                }
                if (Download.this.mDownloadTaskSizeListener != null) {
                    Download.this.mDownloadTaskSizeListener.onDownloadSizeReduced(size);
                }
                if (Download.this.listener != null) {
                    Download.this.listener.onPaused(id);
                }
            }
        }

        public void onStarted(int id) {
            Log.d(Download.TAG, " onStarted , id : " + id);
            if (Download.this.updateDownloadData(id, 4) != null && Download.this.listener != null) {
                Download.this.listener.onStart(id);
            }
        }

        public void onError(int id, int errorcode, String reason) {
            IDownloadTask task = Download.this.updateDownloadData(id, 2);
            if (task != null) {
                int size;
                synchronized (Download.this.tasksLock) {
                    Download.this.tasks.remove(task);
                    size = Download.this.tasks.size();
                    Download.this.mTaskSize = size;
                    Download.this.startDownloadRemainFilesInTasks();
                }
                synchronized (Download.this.releaseLock) {
                    if (size == 0) {
                        Download.this.releaseLock.notify();
                    }
                }
                if (Download.this.mDownloadTaskSizeListener != null) {
                    Download.this.mDownloadTaskSizeListener.onDownloadSizeReduced(size);
                }
                if (Download.this.listener != null) {
                    Download.this.listener.onError(id, errorcode, reason);
                }
            }
        }

        public void onComplete(int id) {
            Log.d(Download.TAG, " onComplete , id : " + id);
            IDownloadTask task = Download.this.updateDownloadData(id, 6);
            if (task != null) {
                int size;
                synchronized (Download.this.tasksLock) {
                    Download.this.tasks.remove(task);
                    size = Download.this.tasks.size();
                    Download.this.mTaskSize = size;
                    Download.this.startDownloadRemainFilesInTasks();
                }
                synchronized (Download.this.releaseLock) {
                    if (size == 0) {
                        Download.this.releaseLock.notify();
                    }
                }
                if (Download.this.mDownloadTaskSizeListener != null) {
                    Download.this.mDownloadTaskSizeListener.onDownloadSizeReduced(size);
                }
                if (Download.this.listener != null) {
                    Download.this.listener.onComplete(id);
                }
            }
            if (!Download.this.isQuit) {
                Download.this.removeDownloadData(id);
            }
        }

        public void onProgress(int id, int progress) {
            Log.d(Download.TAG, " Download onProgress!!!!" + id + " : " + progress);
            if (Download.this.listener != null) {
                Download.this.listener.onProgress(id, progress);
            }
        }

        public void onSaveProgress(DownloadProgressRecord parm) {
            if (Download.this.downloaddata.isValid(parm.getId())) {
                Download.this.downloaddata.updateDownloadProgress(parm);
            }
        }

        public void onSpeedUpdated(int downloadId, long speedBytePerSec) {
            Log.d(Download.TAG, " Download onSpeedUpdated, id is " + downloadId + " , speed is " + speedBytePerSec);
            if (Download.this.listener != null) {
                Download.this.listener.onSpeedUpdated(downloadId, speedBytePerSec);
            }
        }
    };
    public Object releaseLock = new Object();
    private ArrayList<IDownloadTask> tasks;
    private Object tasksLock = new Object();

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean quit() {
        /*
        r4 = this;
        r1 = 1;
        r2 = r4.tasksLock;
        monitor-enter(r2);
        r3 = 1;
        r4.isQuit = r3;	 Catch:{ all -> 0x002e }
        r3 = r4.tasks;	 Catch:{ all -> 0x002e }
        if (r3 == 0) goto L_0x0013;
    L_0x000b:
        r3 = r4.tasks;	 Catch:{ all -> 0x002e }
        r3 = r3.size();	 Catch:{ all -> 0x002e }
        if (r3 != 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
    L_0x0014:
        return r1;
    L_0x0015:
        r1 = r4.tasks;	 Catch:{ all -> 0x002e }
        r1 = r1.iterator();	 Catch:{ all -> 0x002e }
    L_0x001b:
        r3 = r1.hasNext();	 Catch:{ all -> 0x002e }
        if (r3 != 0) goto L_0x0024;
    L_0x0021:
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
        r1 = 0;
        goto L_0x0014;
    L_0x0024:
        r0 = r1.next();	 Catch:{ all -> 0x002e }
        r0 = (com.tvos.downloadmanager.download.IDownloadTask) r0;	 Catch:{ all -> 0x002e }
        r0.stopDownload();	 Catch:{ all -> 0x002e }
        goto L_0x001b;
    L_0x002e:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.Download.quit():boolean");
    }

    public boolean start(DownloadParam parm) {
        if (parm == null || this.isQuit) {
            return false;
        }
        Log.d(TAG, " Download start ! " + parm.getId());
        IDownloadTask task1 = getTaskById(parm.getId());
        Log.d(TAG, " Download start! downloadTime : " + parm.getDownloadTime());
        int parmId = parm.getId();
        if (task1 != null) {
            Log.d(TAG, parm.getId() + " : task has exist ");
            if (this.listener != null) {
                this.listener.onStart(parmId);
            }
            return true;
        } else if (isFull()) {
            parm.setStatus(1);
            updateDownloadData(parm.getId(), 1);
            if (this.listener != null) {
                this.listener.onWait(parmId);
            }
            return false;
        } else {
            Log.d(TAG, " !isFull() ");
            IDownloadTask task = DownloadTaskFactory.createDownloadTask(parm, this.mDownloadThreadPool);
            task.setDownloadTaskListener(this.mylistener);
            synchronized (this.tasksLock) {
                this.tasks.add(task);
                this.mTaskSize = this.tasks.size();
            }
            if (this.mDownloadTaskSizeListener != null) {
                this.mDownloadTaskSizeListener.onDownloadSizeIncreased(this.mTaskSize);
            }
            parm.setStatus(5);
            updateDownloadData(parm.getId(), 5);
            if (this.listener != null) {
                this.listener.onStarting(parmId);
            }
            task.startDownload();
            return true;
        }
    }

    public boolean stop(int id) {
        Log.d(TAG, "stop : " + id);
        IDownloadTask task = getTaskById(id);
        if (task == null) {
            return false;
        }
        task.getCurrentDownloadParam().setStatus(3);
        updateDownloadData(id, 3);
        int stopId = id;
        if (this.listener != null) {
            this.listener.onPauseing(stopId);
        }
        task.stopDownload();
        return true;
    }

    public boolean remove(int id) {
        Log.d(TAG, "stop : " + id);
        IDownloadTask task = getTaskById(id);
        if (task == null) {
            return false;
        }
        task.removeDownload();
        synchronized (this.tasksLock) {
            this.tasks.remove(task);
            int size = this.tasks.size();
            this.mTaskSize = size;
        }
        if (this.mDownloadTaskSizeListener != null) {
            this.mDownloadTaskSizeListener.onDownloadSizeReduced(size);
        }
        synchronized (this.releaseLock) {
            if (size == 0) {
                this.releaseLock.notify();
            }
        }
        return true;
    }

    public long getDownloadSize(int id) {
        IDownloadTask task = getTaskById(id);
        if (task != null) {
            return task.getDownloadSize();
        }
        return 0;
    }

    public long getSpeed(int id) {
        IDownloadTask task = getTaskById(id);
        if (task != null) {
            return task.getSpeed();
        }
        return 0;
    }

    public void setListener(IDownloadStatusListener listener) {
        this.listener = listener;
    }

    public Download(IDownloadData data, DownloadThreadPool pool) {
        this.downloaddata = data;
        this.mDownloadThreadPool = pool;
        synchronized (this.tasksLock) {
            this.tasks = new ArrayList();
        }
        this.listener = null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.tvos.downloadmanager.download.IDownloadTask getTaskById(int r6) {
        /*
        r5 = this;
        r4 = r5.tasksLock;
        monitor-enter(r4);
        r3 = r5.tasks;	 Catch:{ all -> 0x0027 }
        if (r3 == 0) goto L_0x0010;
    L_0x0007:
        r0 = 0;
    L_0x0008:
        r3 = r5.tasks;	 Catch:{ all -> 0x0027 }
        r3 = r3.size();	 Catch:{ all -> 0x0027 }
        if (r0 < r3) goto L_0x0013;
    L_0x0010:
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        r2 = 0;
    L_0x0012:
        return r2;
    L_0x0013:
        r3 = r5.tasks;	 Catch:{ all -> 0x0027 }
        r2 = r3.get(r0);	 Catch:{ all -> 0x0027 }
        r2 = (com.tvos.downloadmanager.download.IDownloadTask) r2;	 Catch:{ all -> 0x0027 }
        r1 = r2.getCurrentDownloadParam();	 Catch:{ all -> 0x0027 }
        r3 = r1.getId();	 Catch:{ all -> 0x0027 }
        if (r3 != r6) goto L_0x002a;
    L_0x0025:
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        goto L_0x0012;
    L_0x0027:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        throw r3;
    L_0x002a:
        r0 = r0 + 1;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.Download.getTaskById(int):com.tvos.downloadmanager.download.IDownloadTask");
    }

    public boolean isFull() {
        synchronized (this.tasksLock) {
            Log.d(TAG, "task size " + this.tasks.size());
            if (this.tasks.size() == 5) {
                return true;
            }
            return false;
        }
    }

    private IDownloadTask updateDownloadData(int id, int status) {
        IDownloadTask task;
        Log.d(TAG, "updateDownloadData id " + id + " status " + status);
        synchronized (this.tasksLock) {
            task = getTaskById(id);
            if (task != null) {
                DownloadParam parm = task.getCurrentDownloadParam();
                if (status == 6) {
                    Log.d(TAG2, "downloadid " + id + " complete");
                    Log.d(TAG2, "downloadtitle : " + parm.getTitle());
                    Log.d(TAG2, "downloadsize : " + parm.getFileSize());
                    Log.d(TAG2, "downloadurl : " + parm.getUri());
                    if (parm.getMd5() == null || parm.getMd5().isEmpty()) {
                        Log.d(TAG2, "download way : multhread");
                    } else {
                        Log.d(TAG2, "download way : p2p");
                    }
                    Log.d(TAG2, "downloadtime : " + parm.getDownloadTime());
                }
                if (!(this.downloaddata == null || parm.getStatus() == 6)) {
                    parm.setStatus(status);
                    this.downloaddata.updateDownloadParm(parm);
                }
            } else {
                Log.d(TAG, "updateDownloadData task is null");
            }
        }
        return task;
    }

    private void removeDownloadData(int id) {
        if (this.downloaddata != null) {
            this.downloaddata.remove(id);
        }
    }

    public void setTaskSizeListener(IDownloadTaskSizeListener listener) {
        this.mDownloadTaskSizeListener = listener;
    }

    public void startDownloadRemainFilesInTasks() {
        synchronized (this.tasksLock) {
            if (!(this.isQuit || this.tasks == null)) {
                for (int i = 0; i < this.tasks.size(); i++) {
                    ((IDownloadTask) this.tasks.get(i)).startRestDownload();
                }
            }
        }
    }

    public int getStartedTaskSize() {
        int size = 0;
        synchronized (this.tasksLock) {
            if (!CommonUtil.isEmptyList(this.tasks)) {
                Iterator it = this.tasks.iterator();
                while (it.hasNext()) {
                    if (((IDownloadTask) it.next()).getCurrentDownloadParam().preAssignedThread()) {
                        size++;
                    }
                }
            }
        }
        return size;
    }
}
