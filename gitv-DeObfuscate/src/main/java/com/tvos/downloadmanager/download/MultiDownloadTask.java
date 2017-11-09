package com.tvos.downloadmanager.download;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.tvos.apps.utils.MD5Utils;
import com.tvos.apps.utils.SysUtils;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.DownloadProgressRecord;
import com.tvos.downloadmanager.data.DownloadThreadInfo;
import com.tvos.downloadmanager.data.FileBrokenPoint;
import com.tvos.downloadmanager.download.DownloadThread.IDownloadThreadListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cybergarage.http.HTTP;

public class MultiDownloadTask extends DownloadTask {
    private static final int CMD_QUIT = 5;
    private static final int GETSERVERINFO_FAIL_NOTRETRY = -2;
    private static final int GETSERVERINFO_FAIL_RETRY = -1;
    private static final int GETSERVERINFO_SUCCESS = 0;
    private static final int RETRY_NUM = 10;
    private static final int STATUS_CHANGE_ERROR = 2;
    private static final int STATUS_CHANGE_PROGRESS = 3;
    private static final int STATUS_CHANGE_STARTED = 0;
    private static final int STATUS_CHANGE_STOPPED = 1;
    private static final int STATUS_STARTTHREAD = 4;
    private static final String TAG = "MultiDownloadTask";
    private int MAX_THREADSIZE = 12;
    private int currentProgress;
    private File destFile;
    private long downloadBeginTime;
    private long downloadSizeWithoutCalculate;
    private long downloadTime;
    private int error_code = 0;
    private String error_reason;
    private float fileSizeOnePercent;
    private boolean isRemove = false;
    private boolean isStarted = false;
    private DownloadProgressRecord mDownloadProgressRecord;
    private DownloadThreadPool mDownloadThreadPool;
    private Object mRemoveLock = new Object();
    private IDownloadTaskListener mlistener;
    private Handler threadHandler;
    private IDownloadThreadListener threadListener = new C20751();
    private HashMap<Integer, DownloadThreadInfo> threads;

    class C20751 implements IDownloadThreadListener {
        C20751() {
        }

        public void onStopped(int threadId, long downloadSize, long filePoint, boolean reqStop) {
            Message msg = new Message();
            msg.what = 1;
            StoppedInfo info = new StoppedInfo();
            info.threadId = threadId;
            info.downloadSize = downloadSize;
            info.filePoint = filePoint;
            info.reqStop = reqStop;
            msg.obj = info;
            if (MultiDownloadTask.this.threadHandler != null) {
                MultiDownloadTask.this.threadHandler.sendMessage(msg);
            }
        }

        public void onStarted(int threadId) {
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = threadId;
            msg.arg2 = 0;
            if (MultiDownloadTask.this.threadHandler != null) {
                MultiDownloadTask.this.threadHandler.sendMessage(msg);
            }
        }

        public void onError(int threadId, int errorcode, String reason, boolean reqstop) {
            Message msg = new Message();
            msg.what = 2;
            msg.arg1 = threadId;
            msg.arg2 = reqstop ? 1 : 0;
            ErrorInfo errorMap = new ErrorInfo();
            errorMap.errorCode = errorcode;
            errorMap.errorString = reason;
            msg.obj = errorMap;
            if (MultiDownloadTask.this.threadHandler != null) {
                MultiDownloadTask.this.threadHandler.sendMessage(msg);
            }
        }

        public void onProgress(int threadId, long downloadSizeIncrease, long filePoint) {
            Message msg = new Message();
            msg.what = 3;
            ProgressInfo progress = new ProgressInfo();
            progress.threadId = threadId;
            progress.downloadSizeIncrease = downloadSizeIncrease;
            progress.filePoint = filePoint;
            msg.obj = progress;
            if (MultiDownloadTask.this.threadHandler != null) {
                MultiDownloadTask.this.threadHandler.sendMessage(msg);
            }
        }
    }

    class C20762 extends Handler {
        C20762() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MultiDownloadTask.this.onStarted(msg.arg1);
                    break;
                case 1:
                    StoppedInfo info = msg.obj;
                    MultiDownloadTask.this.onStopped(info.threadId, info.downloadSize, info.filePoint, info.reqStop);
                    break;
                case 2:
                    boolean reqStop = false;
                    if (msg.arg2 == 1) {
                        reqStop = true;
                    }
                    ErrorInfo errorMap = msg.obj;
                    String reason = errorMap.errorString;
                    MultiDownloadTask.this.onError(msg.arg1, errorMap.errorCode, reason, reqStop);
                    break;
                case 3:
                    ProgressInfo progress = msg.obj;
                    MultiDownloadTask.this.onProgress(progress.threadId, progress.downloadSizeIncrease, progress.filePoint);
                    break;
                case 4:
                    MultiDownloadTask.this.startThread();
                    break;
                case 5:
                    Log.d(MultiDownloadTask.TAG, " handleThreadListener quit!");
                    MultiDownloadTask.this.close();
                    MultiDownloadTask.this.threadHandler = null;
                    MultiDownloadTask.this.mlistener = null;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    class ErrorInfo {
        public int errorCode;
        public String errorString;

        ErrorInfo() {
        }
    }

    class ProgressInfo {
        public long downloadSizeIncrease;
        public long downloadTime;
        public long filePoint;
        public int threadId;

        ProgressInfo() {
        }
    }

    class StoppedInfo {
        public long downloadSize;
        public long downloadTime;
        public long filePoint;
        public boolean reqStop;
        public int threadId;

        StoppedInfo() {
        }
    }

    private int getServerInfo() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0101 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r16 = this;
        r14 = 0;
        r10 = -1;
        r0 = r16;
        r9 = r0.mDownloadParam;
        if (r9 == 0) goto L_0x017a;
    L_0x0009:
        r2 = 0;
        r8 = new java.net.URL;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadParam;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r9.getUri();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = com.tvos.apps.utils.UrlUtil.encodeUrl(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r8.<init>(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "getServerInfo: ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = r8.getPath();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "getServerInfo: ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = r8.getHost();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "getServerInfo: ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = r8.getProtocol();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r8.openConnection();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r0 = r9;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r2 = r0;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r2.setConnectTimeout(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r2.setReadTimeout(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "GET";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r2.setRequestMethod(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r2.getResponseCode();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r9 != r11) goto L_0x0169;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x0089:
        r9 = r2.getContentLength();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r6 = (long) r9;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "conn.getContentLength() : ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r6);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r5 = r2.getContentType();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1));	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r9 > 0) goto L_0x0103;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x00ac:
        r9 = "Content-Length";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r3 = r2.getHeaderField(r9);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r3 == 0) goto L_0x00cf;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x00b5:
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "contentLength : ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r3);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r6 = java.lang.Long.parseLong(r3);	 Catch:{ Exception -> 0x00f3 }
    L_0x00cf:
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "length : ";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r6);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1));	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r9 > 0) goto L_0x0103;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x00e9:
        r2.disconnect();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r2 == 0) goto L_0x00f1;
    L_0x00ee:
        r2.disconnect();
    L_0x00f1:
        r9 = -2;
    L_0x00f2:
        return r9;
    L_0x00f3:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        goto L_0x00cf;
    L_0x00f8:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r2 == 0) goto L_0x0101;
    L_0x00fe:
        r2.disconnect();
    L_0x0101:
        r9 = r10;
        goto L_0x00f2;
    L_0x0103:
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadParam;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9.setFileSize(r6);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadProgressRecord;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9.setFilesize(r6);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadParam;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r9.getMimetype();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r9 == 0) goto L_0x012c;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x011b:
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadParam;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r9.getMimetype();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = "";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r9.equals(r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r9 == 0) goto L_0x0133;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x012c:
        r0 = r16;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = r0.mDownloadParam;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9.setMimetype(r5);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
    L_0x0133:
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "getFileSize:";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r6);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r9 = "MultiDownloadTask";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r12 = "getMimeType:";	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11.<init>(r12);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.append(r5);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        android.util.Log.d(r9, r11);	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        r2.disconnect();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r2 == 0) goto L_0x0167;
    L_0x0164:
        r2.disconnect();
    L_0x0167:
        r9 = 0;
        goto L_0x00f2;
    L_0x0169:
        r2.disconnect();	 Catch:{ Exception -> 0x00f8, all -> 0x0173 }
        if (r2 == 0) goto L_0x0171;
    L_0x016e:
        r2.disconnect();
    L_0x0171:
        r9 = r10;
        goto L_0x00f2;
    L_0x0173:
        r9 = move-exception;
        if (r2 == 0) goto L_0x0179;
    L_0x0176:
        r2.disconnect();
    L_0x0179:
        throw r9;
    L_0x017a:
        r9 = r10;
        goto L_0x00f2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.MultiDownloadTask.getServerInfo():int");
    }

    private void initThreadHandle() {
        this.threadHandler = new C20762();
    }

    private void updateDownloadSize() {
        Log.d(TAG, "updateDownloadSize");
        this.downloadTime += SystemClock.uptimeMillis() - this.downloadBeginTime;
        long size = 0;
        List<FileBrokenPoint> multiInfos = this.mDownloadProgressRecord.getMultiInfos();
        List<FileBrokenPoint> downloadMultiInfos = this.mDownloadParam.getMultiInfos();
        if (multiInfos != null && multiInfos.size() > 0) {
            for (int i = 0; i < multiInfos.size(); i++) {
                size += ((FileBrokenPoint) multiInfos.get(i)).getDownloadSize();
                FileBrokenPoint pointInfo = (FileBrokenPoint) downloadMultiInfos.get(i);
                pointInfo.setDownloadSize(((FileBrokenPoint) multiInfos.get(i)).getDownloadSize());
                pointInfo.setFilePosition(((FileBrokenPoint) multiInfos.get(i)).getFilePosition());
            }
        } else if (this.threads.size() > 0) {
            size = this.threads.values().toArray()[0].thread.getDownloadSize();
        }
        this.mDownloadParam.setDownloadSize(size);
        this.mDownloadParam.setDownloadTime(this.downloadTime);
        this.mDownloadProgressRecord.setDownloadSize(size);
        this.mDownloadProgressRecord.setDownloadTime(this.downloadTime);
    }

    private void onStarted(int threadId) {
        if (!this.isStarted && this.threads.containsKey(Integer.valueOf(threadId))) {
            this.downloadBeginTime = SystemClock.uptimeMillis();
            this.isStarted = true;
            List<FileBrokenPoint> multiInfos = this.mDownloadParam.getMultiInfos();
            if (!(multiInfos == null || multiInfos.isEmpty())) {
                ((FileBrokenPoint) multiInfos.get(threadId)).setStatus(4);
            }
            if (this.mlistener != null) {
                this.mlistener.onStarted(this.mDownloadParam.getId());
            }
            this.mSpeedUpdater.start(this.mDownloadParam.getDownloadSize());
        }
    }

    private void onStopped(int threadId, long downloadSize, long filePoint, boolean reqStop) {
        if (this.threads.containsKey(Integer.valueOf(threadId)) && !reqStop) {
            DownloadThreadInfo info = (DownloadThreadInfo) this.threads.get(Integer.valueOf(threadId));
            info.isStopped = true;
            List<FileBrokenPoint> multiInfos1 = this.mDownloadParam.getMultiInfos();
            if (multiInfos1 != null && multiInfos1.size() > 0) {
                ((FileBrokenPoint) multiInfos1.get(threadId)).setStatus(6);
                FileBrokenPoint pointInfo = (FileBrokenPoint) multiInfos1.get(info.pointId);
                pointInfo.setDownloadSize(pointInfo.getDownloadSize() + downloadSize);
                pointInfo.setFilePosition(filePoint);
            }
            long size = 0;
            boolean hasError = false;
            List<FileBrokenPoint> multiInfos = this.mDownloadParam.getMultiInfos();
            List<FileBrokenPoint> multiInfos2 = this.mDownloadProgressRecord.getMultiInfos();
            if (multiInfos == null || multiInfos.size() <= 0) {
                size = downloadSize;
            } else {
                int i = 0;
                while (i < multiInfos.size()) {
                    FileBrokenPoint point = (FileBrokenPoint) multiInfos.get(i);
                    if (point.isError() || point.getStatus() == 6) {
                        if (point.isError()) {
                            hasError = true;
                            point.setDownloadSize(((FileBrokenPoint) multiInfos2.get(i)).getDownloadSize());
                            point.setFilePosition(((FileBrokenPoint) multiInfos2.get(i)).getFilePosition());
                        }
                        size += ((FileBrokenPoint) multiInfos.get(i)).getDownloadSize();
                        i++;
                    } else if (this.threadHandler != null) {
                        this.threadHandler.sendMessage(this.threadHandler.obtainMessage(4));
                        return;
                    } else {
                        return;
                    }
                }
            }
            this.downloadTime += SystemClock.uptimeMillis() - this.downloadBeginTime;
            this.mDownloadParam.setDownloadSize(size);
            this.mDownloadParam.setDownloadTime(this.downloadTime);
            if (size == this.mDownloadParam.getFileSize()) {
                if (TextUtils.isEmpty(this.mDownloadParam.getMd5())) {
                    Log.d(TAG, "onStopped, md5 is empty");
                    if (this.mlistener != null) {
                        this.mlistener.onComplete(this.mDownloadParam.getId());
                    }
                    this.mSpeedUpdater.stop();
                } else {
                    boolean checkMd5Suc = MD5Utils.verifyFileByMd5(this.mDownloadParam.getDestination(), this.mDownloadParam.getMd5());
                    Log.d(TAG, "onStopped, checkMd5Suc = " + checkMd5Suc + " , id = " + this.mDownloadParam.getId());
                    if (checkMd5Suc) {
                        if (this.mlistener != null) {
                            this.mlistener.onComplete(this.mDownloadParam.getId());
                        }
                        this.mSpeedUpdater.stop();
                    } else {
                        if (this.mlistener != null) {
                            this.mlistener.onError(this.mDownloadParam.getId(), 8, Download.ERROR_MD5VERIFYFAIL);
                        }
                        this.mSpeedUpdater.stop();
                    }
                }
            } else if (hasError) {
                if (this.mlistener != null) {
                    this.mlistener.onError(this.mDownloadParam.getId(), this.error_code, this.error_reason);
                }
                this.mSpeedUpdater.stop();
            } else {
                if (this.mlistener != null) {
                    this.mlistener.onStopped(this.mDownloadParam.getId());
                }
                this.mSpeedUpdater.stop();
            }
            if (this.threadHandler != null) {
                Log.d(TAG, "send quit message");
                this.threadHandler.sendMessage(this.threadHandler.obtainMessage(5));
            }
        }
    }

    private void onError(int threadId, int errorcode, String reason, boolean reqstop) {
        if (this.threads.containsKey(Integer.valueOf(threadId)) && !reqstop) {
            List<FileBrokenPoint> downloadMultiInfos = this.mDownloadParam.getMultiInfos();
            if (!(downloadMultiInfos == null || downloadMultiInfos.isEmpty())) {
                ((FileBrokenPoint) downloadMultiInfos.get(threadId)).setStatus(0);
                ((FileBrokenPoint) downloadMultiInfos.get(threadId)).setError(true);
            }
            ((DownloadThreadInfo) this.threads.get(Integer.valueOf(threadId))).isError = true;
            this.error_reason = reason;
            this.error_code = errorcode;
            long size = 0;
            List<FileBrokenPoint> multiInfos = this.mDownloadProgressRecord.getMultiInfos();
            if (multiInfos != null && multiInfos.size() > 0) {
                int i = 0;
                while (i < multiInfos.size()) {
                    FileBrokenPoint pointInfo = (FileBrokenPoint) downloadMultiInfos.get(i);
                    if (pointInfo.isError() || pointInfo.getStatus() == 6) {
                        size += ((FileBrokenPoint) multiInfos.get(i)).getDownloadSize();
                        pointInfo.setDownloadSize(((FileBrokenPoint) multiInfos.get(i)).getDownloadSize());
                        pointInfo.setFilePosition(((FileBrokenPoint) multiInfos.get(i)).getFilePosition());
                        i++;
                    } else if (this.threadHandler != null) {
                        this.threadHandler.sendMessage(this.threadHandler.obtainMessage(4));
                        return;
                    } else {
                        return;
                    }
                }
                this.mDownloadParam.setDownloadSize(size);
                this.mDownloadProgressRecord.setDownloadSize(size);
            }
            this.downloadTime += SystemClock.uptimeMillis() - this.downloadBeginTime;
            this.mDownloadParam.setDownloadTime(this.downloadTime);
            this.mDownloadProgressRecord.setDownloadTime(this.downloadTime);
            if (reqstop) {
                if (this.mlistener != null) {
                    this.mlistener.onStopped(this.mDownloadParam.getId());
                }
                this.mSpeedUpdater.stop();
            } else {
                if (this.mlistener != null) {
                    this.mlistener.onError(this.mDownloadParam.getId(), errorcode, reason);
                }
                this.mSpeedUpdater.stop();
            }
            if (this.threadHandler != null) {
                this.threadHandler.sendMessage(this.threadHandler.obtainMessage(5));
            }
        }
    }

    private void onProgress(int threadId, long downloadSizeIncrease, long filePoint) {
        if (this.threads.containsKey(Integer.valueOf(threadId))) {
            DownloadThreadInfo info = (DownloadThreadInfo) this.threads.get(Integer.valueOf(threadId));
            List<FileBrokenPoint> multiInfos = this.mDownloadProgressRecord.getMultiInfos();
            long downloadsize;
            if (multiInfos == null || multiInfos.size() <= 0) {
                downloadsize = this.mDownloadProgressRecord.getDownloadSize() + downloadSizeIncrease;
                this.mDownloadProgressRecord.setDownloadSize(downloadsize);
                this.mDownloadParam.setDownloadSize(downloadsize);
            } else {
                FileBrokenPoint pointInfo = (FileBrokenPoint) multiInfos.get(info.pointId);
                pointInfo.setDownloadSize(pointInfo.getDownloadSize() + downloadSizeIncrease);
                pointInfo.setFilePosition(filePoint);
                downloadsize = 0;
                for (int j = 0; j < multiInfos.size(); j++) {
                    downloadsize += ((FileBrokenPoint) multiInfos.get(j)).getDownloadSize();
                }
                this.mDownloadProgressRecord.setDownloadSize(downloadsize);
                this.mDownloadParam.setDownloadSize(downloadsize);
            }
            if (((float) (this.downloadSizeWithoutCalculate + downloadSizeIncrease)) > this.fileSizeOnePercent) {
                long downloadProgressTime = SystemClock.uptimeMillis();
                this.downloadTime += downloadProgressTime - this.downloadBeginTime;
                this.downloadBeginTime = downloadProgressTime;
                this.mDownloadProgressRecord.setDownloadTime(this.downloadTime);
                int increaseProgress = (int) Math.floor((double) (((float) (this.downloadSizeWithoutCalculate + downloadSizeIncrease)) / this.fileSizeOnePercent));
                this.currentProgress += increaseProgress;
                this.downloadSizeWithoutCalculate = (long) (((float) (this.downloadSizeWithoutCalculate + downloadSizeIncrease)) - (((float) increaseProgress) * this.fileSizeOnePercent));
                int lastProgress = 0;
                if (getDownloadStageMap() != null) {
                    lastProgress = ((Integer) getDownloadStageMap().get(Integer.valueOf(this.mDownloadParam.getId()))).intValue();
                }
                updateDownloadProgressRecord(this.mDownloadProgressRecord.getId(), this.currentProgress, this.mDownloadProgressRecord);
                if (this.mlistener != null) {
                    this.mlistener.onProgress(this.mDownloadProgressRecord.getId(), this.currentProgress);
                }
                checkAvailableStorageEnough(this.mDownloadParam.getId(), lastProgress, this.currentProgress);
                return;
            }
            this.downloadSizeWithoutCalculate += downloadSizeIncrease;
        }
    }

    private void checkAvailableStorageEnough(int id, int lastProgress, int currentProgress) {
        Log.d(TAG, "checkAvailableStorageEnough id " + id + "lastProgress " + lastProgress + "curruentProgress " + currentProgress + " downloadSize " + this.mDownloadParam.getDownloadSize());
        if (this.mDownloadParam.getDownloadSize() != this.mDownloadParam.getFileSize() && getDownloadStageMap() != null && getDownloadStageMap().containsKey(Integer.valueOf(id)) && currentProgress - lastProgress >= 5 && !hasEnoughFreeSpace()) {
            updateDownloadSize();
            Log.d(TAG, "checkAvailableStorageEnough false id " + id);
            synchronized (this.mRemoveLock) {
                if (this.mlistener != null) {
                    this.mlistener.onError(id, 4, Download.ERROR_NOENOUGHSPACE);
                }
                for (Integer threadId : this.threads.keySet()) {
                    ((DownloadThreadInfo) this.threads.get(threadId)).thread.stopDownload();
                }
                this.mSpeedUpdater.stop();
                this.threadHandler.sendMessage(this.threadHandler.obtainMessage(5));
            }
        }
    }

    public MultiDownloadTask(DownloadParam param, DownloadThreadPool pool) {
        super(param);
        this.mDownloadParam = param;
        this.mDownloadParam.setPreAssignedThread(false);
        this.mDownloadThreadPool = pool;
        this.mDownloadProgressRecord = new DownloadProgressRecord(param);
        this.downloadTime = param.getDownloadTime();
        this.threads = new HashMap();
        this.isRemove = false;
    }

    private boolean hasEnoughFreeSpace() {
        if (this.mDownloadParam == null) {
            return true;
        }
        long needSpace = this.mDownloadParam.getFileSize() - this.mDownloadParam.getDownloadSize();
        long freeSpace = SysUtils.getFreeSpace(this.mDownloadParam.getDestination());
        Log.d(TAG, "needSpace : " + needSpace + "         freeSpace :" + freeSpace);
        if (freeSpace > needSpace) {
            return true;
        }
        return false;
    }

    public DownloadParam getCurrentDownloadParam() {
        if (this.mDownloadParam == null) {
            Log.d(TAG, "getCurrentDownloadParam return null ");
        } else {
            Log.d(TAG, "getCurrentDownloadParam id " + this.mDownloadParam.getId());
        }
        return this.mDownloadParam;
    }

    public long getDownloadSize() {
        if (this.mDownloadParam == null) {
            Log.d(TAG, "getDownloadSize downloadParam is null, return 0");
            return 0;
        }
        long size = this.mDownloadParam.getDownloadSize();
        Log.d(TAG, "getDownloadSize, size is " + size);
        return size;
    }

    private void checkResumeBroken(long start, long end) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.mDownloadParam.getUri()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(HTTP.GET);
            conn.setRequestProperty(HTTP.RANGE, "bytes=" + start + "-" + end);
            if (conn.getResponseCode() == 206) {
                this.mDownloadParam.setResumeBroken(true);
                this.mDownloadProgressRecord.setResumeBroken(true);
            } else {
                this.mDownloadParam.setResumeBroken(false);
                this.mDownloadProgressRecord.setResumeBroken(false);
            }
            conn.disconnect();
        } catch (IOException e) {
            this.mDownloadParam.setResumeBroken(false);
            this.mDownloadProgressRecord.setResumeBroken(false);
            e.printStackTrace();
        }
    }

    private boolean configDownload() {
        int retryTime = 0;
        boolean success = false;
        int result = -1;
        while (retryTime < 10) {
            result = getServerInfo();
            if (result == 0) {
                success = true;
                break;
            } else if (result == -2) {
                success = false;
                break;
            } else if (result == -1) {
                success = false;
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retryTime++;
            }
        }
        if (success) {
            checkResumeBroken(100, this.mDownloadParam.getFileSize() - 100);
            if (this.mDownloadParam.isResumeBroken()) {
                segmentDownloadFile(this.mDownloadParam.getFileSize());
            } else {
                Log.d(TAG, new StringBuilder(String.valueOf(this.mDownloadParam.getUri())).append(" just single thread download").toString());
                this.mDownloadParam.setMultiInfos(null);
                this.mDownloadProgressRecord.setMultiInfos(null);
            }
        } else if (this.mlistener != null) {
            if (result == -2) {
                this.mlistener.onError(this.mDownloadParam.getId(), 3, Download.ERROR_FILE_UNNORMAL);
            } else {
                this.mlistener.onError(this.mDownloadParam.getId(), 1, Download.ERROR_NETWORK);
            }
            this.mSpeedUpdater.stop();
        }
        return success;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startThread() {
        /*
        r6 = this;
        r1 = r6.mRemoveLock;
        monitor-enter(r1);
        r0 = r6.isRemove;	 Catch:{ all -> 0x0017 }
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r6.destFile;	 Catch:{ all -> 0x0017 }
        if (r0 == 0) goto L_0x0015;
    L_0x000d:
        r0 = r6.destFile;	 Catch:{ all -> 0x0017 }
        r0 = r0.exists();	 Catch:{ all -> 0x0017 }
        if (r0 != 0) goto L_0x001a;
    L_0x0015:
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        goto L_0x0008;
    L_0x0017:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        throw r0;
    L_0x001a:
        r0 = "MultiDownloadTask";
        r2 = "startThread";
        android.util.Log.d(r0, r2);	 Catch:{ all -> 0x0017 }
        r0 = r6.mDownloadThreadPool;	 Catch:{ all -> 0x0017 }
        r2 = r6.mDownloadParam;	 Catch:{ all -> 0x0017 }
        r3 = r6.destFile;	 Catch:{ all -> 0x0017 }
        r4 = r6.threadListener;	 Catch:{ all -> 0x0017 }
        r5 = r6.threads;	 Catch:{ all -> 0x0017 }
        r0.startDownload(r2, r3, r4, r5);	 Catch:{ all -> 0x0017 }
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.MultiDownloadTask.startThread():void");
    }

    protected void startDownloadImp() {
        Log.d(TAG, "start");
        initThreadHandle();
        synchronized (this.mRemoveLock) {
            if (this.isRemove) {
            } else if (this.mDownloadParam != null) {
                if (this.mDownloadParam.getDownloadSize() == 0) {
                    Log.d(TAG, new StringBuilder(String.valueOf(this.mDownloadParam.getUri())).append(" download firstly!").toString());
                    if (!configDownload()) {
                        return;
                    }
                }
                Log.d(TAG, "hasEnoughFreeSpace : " + hasEnoughFreeSpace());
                if (hasEnoughFreeSpace()) {
                    this.destFile = new File(this.mDownloadParam.getDestination());
                    Log.d(TAG, "destination " + this.mDownloadParam.getDestination());
                    if (!this.destFile.exists()) {
                        try {
                            File parentDir = this.destFile.getParentFile();
                            if (!parentDir.exists()) {
                                parentDir.mkdirs();
                            }
                            RandomAccessFile accessFile = new RandomAccessFile(this.destFile, "rwd");
                            accessFile.setLength(this.mDownloadParam.getFileSize());
                            accessFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (this.mlistener != null) {
                                this.mlistener.onError(this.mDownloadParam.getId(), 5, Download.ERROR_DEVICEBUSY);
                            }
                            this.mSpeedUpdater.stop();
                            return;
                        }
                    }
                    this.isStarted = false;
                    this.fileSizeOnePercent = ((float) this.mDownloadParam.getFileSize()) / 100.0f;
                    this.currentProgress = 0;
                    if (this.mDownloadParam.isResumeBroken() && this.mDownloadParam.getDownloadSize() > 0) {
                        this.currentProgress = (int) ((100 * this.mDownloadParam.getDownloadSize()) / this.mDownloadParam.getFileSize());
                    }
                    Log.d(TAG, "start, currentProgress: " + this.currentProgress);
                    startThread();
                    return;
                }
                if (this.mlistener != null) {
                    this.mlistener.onError(this.mDownloadParam.getId(), 4, Download.ERROR_NOENOUGHSPACE);
                }
                this.mSpeedUpdater.stop();
            } else {
                Log.d(TAG, "mDownloadParam is null");
                if (this.mlistener != null) {
                    this.mlistener.onError(this.mDownloadParam.getId(), 0, Download.ERROR_UNKNOWN);
                }
                this.mSpeedUpdater.stop();
            }
        }
    }

    protected void stopDownloadImp() {
        synchronized (this.mRemoveLock) {
            for (Integer id : this.threads.keySet()) {
                ((DownloadThreadInfo) this.threads.get(id)).thread.stopDownload();
            }
            updateDownloadSize();
            if (this.mlistener != null) {
                this.mlistener.onStopped(this.mDownloadParam.getId());
            }
            this.mSpeedUpdater.stop();
            this.threadHandler.sendMessage(this.threadHandler.obtainMessage(5));
        }
    }

    protected void removeDownloadImp() {
        synchronized (this.mRemoveLock) {
            this.isRemove = true;
            for (Integer id : this.threads.keySet()) {
                ((DownloadThreadInfo) this.threads.get(id)).thread.stopDownload();
            }
            if (this.threadHandler != null) {
                this.threadHandler.sendEmptyMessage(5);
            }
        }
    }

    public void setDownloadTaskListener(IDownloadTaskListener listener) {
        super.setDownloadTaskListener(listener);
        this.mlistener = listener;
    }

    private int segmentDownloadFile(long fileSize) {
        Log.d(TAG, "segmentDownloadFile " + this.mDownloadParam.getUri());
        int count = ((int) Math.round(Math.log(((double) fileSize) / 1048576.0d))) * 2;
        if (count < 2) {
            count = 2;
        } else if (count > this.MAX_THREADSIZE) {
            count = this.MAX_THREADSIZE;
        }
        Log.d(TAG, new StringBuilder(String.valueOf(this.mDownloadParam.getUri())).append("can multi thread download, threadSize ").append(count).toString());
        long block = this.mDownloadParam.getFileSize() / ((long) count);
        List<FileBrokenPoint> multiInfos = new ArrayList();
        List<FileBrokenPoint> multiInfosProgress = new ArrayList();
        for (int i = 0; i < count; i++) {
            FileBrokenPoint pointInfo = new FileBrokenPoint();
            FileBrokenPoint pointInfoProgress = new FileBrokenPoint();
            if (i == count - 1) {
                pointInfo.setReqSize(this.mDownloadParam.getFileSize() - (((long) (count - 1)) * block));
                pointInfoProgress.setReqSize(this.mDownloadProgressRecord.getFilesize() - (((long) (count - 1)) * block));
            } else {
                pointInfo.setReqSize(block);
                pointInfoProgress.setReqSize(block);
            }
            pointInfo.setDownloadSize(0);
            pointInfoProgress.setDownloadSize(0);
            pointInfo.setFilePosition(((long) i) * block);
            pointInfoProgress.setFilePosition(((long) i) * block);
            pointInfo.setError(false);
            pointInfo.setStatus(-1);
            multiInfos.add(pointInfo);
            multiInfosProgress.add(pointInfoProgress);
        }
        this.mDownloadParam.setMultiInfos(multiInfos);
        this.mDownloadProgressRecord.setMultiInfos(multiInfosProgress);
        return count;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startRestDownloadImp() {
        /*
        r2 = this;
        r0 = "MultiDownloadTask";
        r1 = "startRestDownloadImp";
        android.util.Log.d(r0, r1);
        r1 = r2.mRemoveLock;
        monitor-enter(r1);
        r0 = r2.isRemove;	 Catch:{ all -> 0x002f }
        if (r0 == 0) goto L_0x0012;
    L_0x0010:
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r2.mDownloadParam;	 Catch:{ all -> 0x002f }
        if (r0 == 0) goto L_0x002d;
    L_0x0016:
        r0 = r2.mDownloadParam;	 Catch:{ all -> 0x002f }
        r0 = r0.getMultiInfos();	 Catch:{ all -> 0x002f }
        if (r0 == 0) goto L_0x002d;
    L_0x001e:
        r0 = r2.mDownloadParam;	 Catch:{ all -> 0x002f }
        r0 = r0.getMultiInfos();	 Catch:{ all -> 0x002f }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x002f }
        if (r0 != 0) goto L_0x002d;
    L_0x002a:
        r2.startThread();	 Catch:{ all -> 0x002f }
    L_0x002d:
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        goto L_0x0011;
    L_0x002f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.downloadmanager.download.MultiDownloadTask.startRestDownloadImp():void");
    }
}
