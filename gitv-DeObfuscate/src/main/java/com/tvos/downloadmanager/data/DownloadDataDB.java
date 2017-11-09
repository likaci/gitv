package com.tvos.downloadmanager.data;

import android.content.Context;
import android.util.Log;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.tvos.downloadmanager.download.IDownloadStatusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadDataDB implements IDownloadData {
    private static final String TAG = "DownloadDataDB";
    private IDownloadStatusListener downloadStatusListener;
    private Context mContext;
    private DownloadDBUtil mDownloadDBUtil = null;

    public DownloadDataDB(Context context, String dbName) {
        this.mContext = context;
        init(dbName);
    }

    private void init(String dbName) {
        Log.d(TAG, InterfaceKey.SHARE_IT);
        this.mDownloadDBUtil = new DownloadDBUtil();
        this.mDownloadDBUtil.init(this.mContext, dbName);
        reset();
    }

    private void reset() {
        List<DownloadRecord> allDownloadRecords = this.mDownloadDBUtil.findAllDownloadRecord();
        if (allDownloadRecords != null && !allDownloadRecords.isEmpty()) {
            for (DownloadRecord record : allDownloadRecords) {
                switch (record.getStatus()) {
                    case -1:
                        record.setStatus(0);
                        break;
                    case 1:
                    case 5:
                        if (record.getDownloadSize() <= 0) {
                            record.setStatus(0);
                            break;
                        } else {
                            record.setStatus(2);
                            break;
                        }
                    case 3:
                    case 4:
                        record.setStatus(2);
                        break;
                }
                if (!record.isP2PDownload() || record.getMd5() == null || record.getMd5().isEmpty() || record.isP2pDownloadError()) {
                    record.setP2PDownload(false);
                } else {
                    record.setP2PDownload(true);
                }
                this.mDownloadDBUtil.update(record);
            }
        }
    }

    public boolean isValid(int id) {
        return this.mDownloadDBUtil.isVaild(id);
    }

    public int getDownloadStatus(int id) {
        Log.d(TAG, "getDownloadStatus");
        DownloadRecord record = this.mDownloadDBUtil.findDownloadRecordByID(id);
        if (record != null) {
            return record.getStatus();
        }
        return -1;
    }

    public boolean removeDownloadFile(int id) {
        Log.d(TAG, "removeDownloadFile id=" + id);
        try {
            File downloadFile = new File(getDownloadParm(id).getDestination());
            if (!downloadFile.exists()) {
                return true;
            }
            downloadFile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DownloadParam getDownloadParm(int id) {
        DownloadRecord record = this.mDownloadDBUtil.findDownloadRecordByID(id);
        if (record != null) {
            return record.toDownloadParam();
        }
        return null;
    }

    public List<DownloadParam> getNextWaits(int size) {
        Log.d(TAG, "getNextWait");
        List<DownloadRecord> recordList = this.mDownloadDBUtil.findDownloadRecordByStatus(1);
        if (recordList == null || recordList.isEmpty()) {
            return null;
        }
        List<DownloadParam> paramList = new ArrayList();
        int waitSize;
        if (size > recordList.size()) {
            waitSize = recordList.size();
        } else {
            waitSize = size;
        }
        for (DownloadRecord record : recordList.subList(0, waitSize)) {
            paramList.add(record.toDownloadParam());
        }
        return paramList;
    }

    public boolean isExist(RequestInfo info) {
        Log.d(TAG, "isExist uri=" + info.getUri());
        if (this.mDownloadDBUtil.getDownloadRecordByUri(info.getUri()) != null) {
            return true;
        }
        return false;
    }

    public int insert(RequestInfo info) {
        if (info == null) {
            return -1;
        }
        Log.d(TAG, "insert uri=" + info.getUri());
        this.mDownloadDBUtil.insert(DownloadRecord.fromRequestInfo(info));
        DownloadRecord record = getDownloadRecord(info.getUri());
        if (this.downloadStatusListener != null) {
            this.downloadStatusListener.onAdd(record.toDownloadInfo());
        }
        return record.getId();
    }

    private DownloadRecord getDownloadRecord(String uri) {
        Log.d(TAG, "getDownloadRecord uri=" + uri);
        return this.mDownloadDBUtil.getDownloadRecordByUri(uri);
    }

    public boolean remove(int id) {
        Log.d(TAG, "remove id=" + id);
        this.mDownloadDBUtil.removeDownloadRecordByID(id);
        if (this.downloadStatusListener != null) {
            this.downloadStatusListener.onRemove(id);
        }
        return true;
    }

    public List<DownloadInfo> getDownloadInfoList() {
        List<DownloadInfo> downloadInfos = new ArrayList();
        List<DownloadRecord> downloadRecords = this.mDownloadDBUtil.findAllDownloadRecord();
        if (downloadRecords == null || downloadRecords.isEmpty()) {
            Log.d(TAG, "getDownloadInfoList size=0");
        } else {
            Log.d(TAG, "getDownloadInfoList size=" + downloadRecords.size());
            for (DownloadRecord record : downloadRecords) {
                downloadInfos.add(record.toDownloadInfo());
            }
        }
        return downloadInfos;
    }

    public void setListener(IDownloadStatusListener listener) {
        Log.d(TAG, "setListener");
        this.downloadStatusListener = listener;
    }

    public void release() {
        Log.d(TAG, "release");
        this.mContext = null;
        if (this.mDownloadDBUtil != null) {
            this.mDownloadDBUtil.release();
        }
    }

    public void updateDownloadParm(DownloadParam param) {
        if (param != null) {
            Log.d(TAG, "updateDownloadParm id=" + param.getId());
            DownloadRecord record = getDownloadRecord(param.getUri());
            if (record != null) {
                record.fromDownloadParam(param);
                this.mDownloadDBUtil.update(record);
            }
        }
    }

    public void updateDownloadProgress(DownloadProgressRecord param) {
        if (param != null) {
            DownloadRecord record = this.mDownloadDBUtil.findDownloadRecordByID(param.getId());
            if (record != null) {
                record.setId(param.getId());
                record.setDownloadSize(param.getDownloadSize());
                record.setFileSize(param.getFilesize());
                record.setMultiInfos(param.getMultiInfos());
                record.setResumeBroken(param.isResumeBroken());
                record.setDownloadTime(param.getDownloadTime());
                this.mDownloadDBUtil.update(record);
            }
        }
    }

    public void resetDownloadRecord(int id) {
        Log.d(TAG, "resetDownloadRecord");
        this.mDownloadDBUtil.resetDownloadRecord(id);
    }

    public DownloadInfo getDownloadInfo(int id) {
        DownloadRecord record = this.mDownloadDBUtil.findDownloadRecordByID(id);
        if (record != null) {
            return record.toDownloadInfo();
        }
        return null;
    }

    public DownloadInfo getDownloadInfoByUrl(String url) {
        DownloadRecord record = this.mDownloadDBUtil.getDownloadRecordByUri(url);
        if (record != null) {
            return record.toDownloadInfo();
        }
        return null;
    }

    public DownloadParam getDownloadParamByUrl(String url) {
        DownloadRecord record = this.mDownloadDBUtil.getDownloadRecordByUri(url);
        if (record != null) {
            return record.toDownloadParam();
        }
        return null;
    }
}
