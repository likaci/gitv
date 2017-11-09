package com.tvos.downloadmanagerjni;

import android.content.Context;
import android.util.Log;
import com.tvos.downloadmanager.DownloadManagerFactory;
import com.tvos.downloadmanager.IDownloadManager;
import com.tvos.downloadmanager.data.DownloadInfo;
import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.IDownloadStatusListener;
import java.util.List;

public class DownloadManagerJNI {
    private static final String TAG = "DownloadManagerJNI_JAVA";
    private static Context context = null;
    public static IDownloadManager manager = null;
    public static IDownloadStatusListener statusListener = new C20771();

    class C20771 implements IDownloadStatusListener {
        C20771() {
        }

        public void onWait(int id) {
            Log.d(DownloadManagerJNI.TAG, "onWaitJNI Java");
            DownloadManagerJNI.onWaitJNI(id);
        }

        public void onStop(int id) {
            Log.d(DownloadManagerJNI.TAG, "onStopJNI Java");
            DownloadManagerJNI.onStopJNI(id);
        }

        public void onStart(int id) {
            Log.d(DownloadManagerJNI.TAG, "onStartJNI Java");
            DownloadManagerJNI.onStartJNI(id);
        }

        public void onPaused(int id) {
            Log.d(DownloadManagerJNI.TAG, "onPausedJNI Java");
            DownloadManagerJNI.onPausedJNI(id);
        }

        public void onError(int id, int errorcode, String reason) {
            Log.d(DownloadManagerJNI.TAG, "onErrorJNI Java");
            DownloadManagerJNI.onErrorJNI(id, reason);
        }

        public void onComplete(int id) {
            Log.d(DownloadManagerJNI.TAG, "onCompleteJNI Java");
            DownloadManagerJNI.onCompleteJNI(id);
        }

        public void onRemove(int id) {
            DownloadManagerJNI.onRemoveJNI(id);
        }

        public void onAdd(DownloadInfo info) {
            DownloadManagerJNI.onAddJNI(info);
        }

        public void onProgress(int id, int progress) {
            DownloadManagerJNI.onProgressJNI(id, progress);
        }

        public void onStarting(int id) {
        }

        public void onPauseing(int id) {
        }

        public void onSpeedUpdated(int id, long speedBytePerSec) {
        }
    }

    private static native void initInstance();

    private static native void onAddJNI(DownloadInfo downloadInfo);

    private static native void onCompleteJNI(int i);

    private static native void onErrorJNI(int i, String str);

    private static native void onPausedJNI(int i);

    private static native void onProgressJNI(int i, int i2);

    private static native void onRemoveJNI(int i);

    private static native void onStartJNI(int i);

    private static native void onStopJNI(int i);

    private static native void onWaitJNI(int i);

    static {
        System.loadLibrary("DownloadManagerJNI");
    }

    public static void setContext(Context ctx) {
        context = ctx;
        initInstance();
        manager = DownloadManagerFactory.createDownloadManager(ctx, "downloadmanager.db");
    }

    public static void setListener() {
        if (manager != null) {
            manager.setListener(statusListener);
        }
    }

    public static long getDownloadSize(int id) {
        if (manager != null) {
            return manager.getDownloadSize(id);
        }
        return 0;
    }

    public static long getFileSize(int id) {
        if (manager != null) {
            return manager.getFileSize(id);
        }
        return 0;
    }

    public static List<DownloadInfo> getDownloadInfoList() {
        if (manager != null) {
            return manager.getDownloadInfoList();
        }
        return null;
    }

    public static int getDownloadStatus(int id) {
        if (manager != null) {
            return manager.getDownloadStatus(id);
        }
        return 0;
    }

    public static boolean enqueue(RequestInfo requestInfo) {
        if (manager != null) {
            return manager.enqueue(requestInfo);
        }
        return false;
    }

    public static boolean remove(int id) {
        if (manager != null) {
            return manager.remove(id);
        }
        return false;
    }

    public static boolean start(int id) {
        if (manager != null) {
            return manager.start(id);
        }
        return false;
    }

    public static boolean pause(int id) {
        if (manager != null) {
            return manager.pause(id);
        }
        return false;
    }

    public static boolean resume(int id) {
        if (manager != null) {
            return manager.resume(id);
        }
        return false;
    }

    public static void startAll() {
        if (manager != null) {
            manager.startAll();
        }
    }

    public static void stopAll() {
        if (manager != null) {
            manager.stopAll();
        }
    }

    public static void release() {
        if (manager != null) {
            DownloadManagerFactory.release("downloadmanager.db");
            manager = null;
        }
    }
}
