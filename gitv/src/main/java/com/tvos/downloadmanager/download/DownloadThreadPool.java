package com.tvos.downloadmanager.download;

import android.util.Log;
import com.tvos.apps.utils.UrlUtil;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.DownloadThreadInfo;
import com.tvos.downloadmanager.data.FileBrokenPoint;
import com.tvos.downloadmanager.download.DownloadThread.IDownloadThreadListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadThreadPool {
    private static final String TAG = "DownloadThreadPool";
    public static final int THREADSIZE_MAX = 20;
    public static final int THREADSIZE_MIN = 5;
    private IDownload mIDownload;
    private IDownloadThreadFinishListener mListener;
    private ThreadPoolExecutor mThreadExecutorService;
    private int mThreadSize = 20;

    public DownloadThreadPool() {
        init(0);
    }

    public DownloadThreadPool(int speedLimitDegree) {
        Log.d(TAG, "construction");
        init(speedLimitDegree);
    }

    public void setmIDownload(IDownload mIDownload) {
        this.mIDownload = mIDownload;
    }

    private void init(int speedLimitDegree) {
        int i = 5;
        this.mThreadSize = 20 - (speedLimitDegree * 5);
        if (this.mThreadSize >= 5) {
            i = this.mThreadSize;
        }
        this.mThreadSize = i;
        this.mThreadExecutorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.mThreadSize);
    }

    public void setDownloadThreadFinishListener(IDownloadThreadFinishListener listener) {
        this.mListener = listener;
    }

    public synchronized void startDownload(DownloadParam downloadParam, File destFile, IDownloadThreadListener threadListener, HashMap<Integer, DownloadThreadInfo> threads) {
        Log.d(TAG, "startDownload, downloadParam = " + downloadParam);
        downloadParam.setPreAssignedThread(true);
        List<FileBrokenPoint> multiInfos = downloadParam.getMultiInfos();
        URL url;
        try {
            DownloadThreadInfo info;
            url = new URL(UrlUtil.encodeUrl(downloadParam.getUri()));
            if (multiInfos != null) {
                if (multiInfos.size() != 0) {
                    int freeThreadSize = (this.mThreadSize - this.mThreadExecutorService.getActiveCount()) - ((5 - this.mIDownload.getStartedTaskSize()) * 2);
                    Log.d(TAG, "free thread size --- " + freeThreadSize + " active count --- " + this.mThreadExecutorService.getActiveCount() + " task size ---" + this.mIDownload.getStartedTaskSize());
                    if (freeThreadSize <= 0) {
                        Log.d(TAG, "startDownload, exception 1");
                        downloadParam.setPreAssignedThread(false);
                    } else {
                        FileBrokenPoint point;
                        int canUsedThreadSize = freeThreadSize;
                        Log.d(TAG, "multiInfos size " + multiInfos.size());
                        if (multiInfos.size() > 1 && downloadParam.getSpeedLimitDegree() != 0) {
                            int threadUsedInTask = 0;
                            for (FileBrokenPoint point2 : multiInfos) {
                                if (point2.getStatus() == 4 || point2.getStatus() == 5) {
                                    threadUsedInTask++;
                                }
                            }
                            Log.d(TAG, "threadUsedIntask " + threadUsedInTask);
                            int maxUsedThreadSize = multiInfos.size() * ((5 - downloadParam.getSpeedLimitDegree()) / 6);
                            if (threadUsedInTask >= maxUsedThreadSize) {
                                Log.d(TAG, "startDownload, exception 2");
                                downloadParam.setPreAssignedThread(false);
                            } else {
                                if (freeThreadSize >= maxUsedThreadSize - threadUsedInTask) {
                                    canUsedThreadSize = maxUsedThreadSize - threadUsedInTask;
                                } else {
                                    canUsedThreadSize = freeThreadSize;
                                }
                                Log.d(TAG, "canUsedThreadSize" + canUsedThreadSize);
                            }
                        }
                        if (canUsedThreadSize <= 0) {
                            Log.d(TAG, "startDownload, exception 3");
                            downloadParam.setPreAssignedThread(false);
                        } else {
                            int usedThreadSize = 0;
                            for (int i = 0; i < multiInfos.size(); i++) {
                                point2 = (FileBrokenPoint) multiInfos.get(i);
                                if (!(point2 == null || point2.getStatus() == 6 || point2.getStatus() == 4 || point2.getStatus() == 5 || point2.isError())) {
                                    int thread_id = i;
                                    long start = point2.getFilePosition();
                                    long end = ((point2.getReqSize() + start) - point2.getDownloadSize()) - 1;
                                    if ((end - start) + 1 > 0) {
                                        DownloadThread thread = new DownloadThread(thread_id, url, destFile, start, end, threadListener, true, downloadParam.getSpeedLimitDegree());
                                        thread.setFinishListener(this.mListener);
                                        point2.setStatus(5);
                                        info = new DownloadThreadInfo();
                                        info.pointId = i;
                                        info.thread = thread;
                                        info.isStarted = false;
                                        info.isStopped = false;
                                        info.isError = false;
                                        threads.put(Integer.valueOf(thread.getThreadId()), info);
                                        this.mThreadExecutorService.execute(thread);
                                        usedThreadSize++;
                                        Log.d(TAG, "usedThreadSize = " + usedThreadSize + " , canUsedThreadSize = " + canUsedThreadSize);
                                        if (usedThreadSize >= canUsedThreadSize) {
                                            break;
                                        }
                                    } else {
                                        point2.setStatus(6);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            DownloadThread singlethread = new DownloadThread(0, url, destFile, 0, downloadParam.getFileSize() - 1, threadListener, false, downloadParam.getSpeedLimitDegree());
            singlethread.setFinishListener(this.mListener);
            info = new DownloadThreadInfo();
            info.pointId = 0;
            info.thread = singlethread;
            info.isStarted = false;
            info.isStopped = false;
            info.isError = false;
            threads.put(Integer.valueOf(singlethread.getThreadId()), info);
            Log.d(TAG, "start singleThread");
            this.mThreadExecutorService.execute(singlethread);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "startDownload, encode url exception");
            downloadParam.setPreAssignedThread(false);
            url = null;
        }
    }
}
