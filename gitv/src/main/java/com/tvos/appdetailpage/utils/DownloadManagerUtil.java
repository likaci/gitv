package com.tvos.appdetailpage.utils;

import android.util.Log;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity;
import com.tvos.downloadmanager.IDownloadManager;
import com.tvos.downloadmanager.data.DownloadInfo;
import com.tvos.downloadmanager.data.RequestInfo;
import com.tvos.downloadmanager.download.IDownloadStatusListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class DownloadManagerUtil {
    public static final int DOWNLOAD_OPERATION_ON_COMPLETE = 2;
    public static final int DOWNLOAD_OPERATION_ON_ERROR = 4;
    public static final int DOWNLOAD_OPERATION_ON_PAUSE = 3;
    public static final int DOWNLOAD_OPERATION_ON_PROCESS = 0;
    public static final int DOWNLOAD_OPERATION_ON_START = 1;
    public static final int DOWNLOAD_OPERATION_ON_STOP = 5;
    public static final int DOWNLOAD_OPERATION_ON_WAIT = 6;
    public static final int DOWNLOAD_STATUS_COMPLETE = 6;
    public static final int DOWNLOAD_STATUS_FILE_EXIST = 5;
    public static final int DOWNLOAD_STATUS_FILE_FAIL = 7;
    public static final int DOWNLOAD_STATUS_NOT_DOWNLOAD = 8;
    public static final int DOWNLOAD_STATUS_PAUSED = 2;
    public static final int DOWNLOAD_STATUS_STARTTED = 4;
    public static final int DOWNLOAD_STATUS_STOPPED = 0;
    public static final int DOWNLOAD_STATUS_SUCCESS = 6;
    public static final int DOWNLOAD_STATUS_WAIT = 1;
    public static String TAG = "DownloadManagerUtil";
    private static Map<String, Integer> mDownloadIDMap = new LinkedHashMap();
    private static Map<Integer, DownloadInfo> mDownloadInfoMap = new LinkedHashMap();
    private static ArrayList<Observer> mDownloadObservers = new ArrayList();
    static IDownloadStatusListener mStatusListener = new IDownloadStatusListener() {
        public void onProgress(int id, int process) {
            Log.d(DownloadManagerUtil.TAG, "onProgress: id=" + id + " process =  " + process);
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 0, process, null));
            }
        }

        public void onRemove(int id) {
            Log.d(DownloadManagerUtil.TAG, "onRemove " + id);
            DownloadManagerUtil.mDownloadIDMap.remove(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath());
            DownloadManagerUtil.mDownloadInfoMap.remove(Integer.valueOf(id));
        }

        public void onAdd(DownloadInfo info) {
            Log.d(DownloadManagerUtil.TAG, "onAdd " + info.getDownloadId());
            DownloadManagerUtil.mDownloadInfoMap.put(Integer.valueOf(info.getDownloadId()), info);
            DownloadManagerUtil.mDownloadIDMap.put(info.getFilePath(), Integer.valueOf(info.getDownloadId()));
        }

        public void onComplete(int id) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "complete!");
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(6);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 2, 0, null));
            }
        }

        public void onStart(int id) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "started!");
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(4);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 1, 0, null));
            }
        }

        public void onPaused(int id) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "paused!");
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(2);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 3, 0, null));
            }
        }

        public void onWait(int id) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "onWait!");
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(1);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 6, 0, null));
            }
        }

        public void onStop(int id) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "onStop!");
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(0);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 5, 0, null));
            }
        }

        public void onError(int id, int errcode, String reason) {
            Log.d(DownloadManagerUtil.TAG, "download " + id + "error:" + reason);
            if (DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id)) != null) {
                ((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).setStatus(2);
                DownloadManagerUtil.update(null, new DownloadState(((DownloadInfo) DownloadManagerUtil.mDownloadInfoMap.get(Integer.valueOf(id))).getFilePath(), 4, 0, reason));
            }
        }

        public void onPauseing(int arg0) {
        }

        public void onStarting(int arg0) {
        }

        public void onSpeedUpdated(int arg0, long arg1) {
        }
    };

    public static class DownloadState {
        public String filePath;
        public int operation;
        public int process;
        public String reason;

        public DownloadState(String filePath, int operation, int process, String reason) {
            this.filePath = filePath;
            this.operation = operation;
            this.process = process;
            this.reason = reason;
        }
    }

    public static void initDownloadManager() {
        IDownloadManager downloadManager = AppStoreDetailActivity.getDownloadManager();
        List<DownloadInfo> mDownloadInfos = downloadManager.getDownloadInfoList();
        if (mDownloadInfos != null) {
            for (DownloadInfo info : mDownloadInfos) {
                mDownloadInfoMap.put(Integer.valueOf(info.getDownloadId()), info);
                mDownloadIDMap.put(info.getFilePath(), Integer.valueOf(info.getDownloadId()));
            }
            Log.d(TAG, "initDownloadManager map size =  " + mDownloadInfoMap.size());
        }
        downloadManager.setListener(mStatusListener);
        downloadManager.startAll();
    }

    public static int queryDownloadState(String filePath) {
        if (mDownloadIDMap.containsKey(filePath)) {
            return ((DownloadInfo) mDownloadInfoMap.get(mDownloadIDMap.get(filePath))).getStatus();
        }
        return 8;
    }

    public static int download(String filePath, String appurl, String appname) {
        Log.d(TAG, "download  filePath = " + filePath + "appname:" + appname);
        if (AppCommUtils.isCompleteAppPkgExist(filePath)) {
            Log.d(TAG, "download  isPackageExist appname:" + appname);
            return 5;
        } else if (mDownloadIDMap.containsKey(filePath)) {
            Log.d(TAG, "download  mDownloadIDMap.containsKey appname:" + appname);
            switch (((DownloadInfo) mDownloadInfoMap.get(mDownloadIDMap.get(filePath))).getStatus()) {
                case 0:
                case 2:
                    AppStoreDetailActivity.getDownloadManager().resume(((Integer) mDownloadIDMap.get(filePath)).intValue());
                    break;
            }
            return ((DownloadInfo) mDownloadInfoMap.get(mDownloadIDMap.get(filePath))).getStatus();
        } else {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setDestination(filePath);
            requestInfo.setMd5("");
            requestInfo.setMimetype("");
            requestInfo.setUri(appurl);
            requestInfo.setTitle(appname);
            if (AppStoreDetailActivity.getDownloadManager().enqueue(requestInfo)) {
                return 6;
            }
            return 7;
        }
    }

    public static void registerObserver(Observer observer) {
        if (!mDownloadObservers.contains(observer)) {
            mDownloadObservers.add(observer);
        }
    }

    public static void unregisterObserver(Observer observer) {
        mDownloadObservers.remove(observer);
    }

    private static void update(Observable observable, Object data) {
        for (int i = 0; i < mDownloadObservers.size(); i++) {
            ((Observer) mDownloadObservers.get(i)).update(observable, data);
        }
    }
}
