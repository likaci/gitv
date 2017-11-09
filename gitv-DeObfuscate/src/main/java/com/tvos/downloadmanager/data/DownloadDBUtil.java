package com.tvos.downloadmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DownloadDBUtil {
    private static final String TAG = "DownloadDBUtil";
    private Context mContext = null;
    private DownloadDBHelper mDownloadDBHelper = null;
    private DownloadDBHelper mReadDownloadDBHelper = null;

    public void init(Context context, String dbName) {
        this.mContext = context;
        this.mDownloadDBHelper = new DownloadDBHelper(this.mContext, dbName);
        this.mReadDownloadDBHelper = new DownloadDBHelper(context, dbName);
    }

    public void release() {
        if (this.mDownloadDBHelper != null) {
            this.mDownloadDBHelper.close();
            this.mDownloadDBHelper = null;
        }
        if (this.mReadDownloadDBHelper != null) {
            this.mReadDownloadDBHelper.close();
            this.mReadDownloadDBHelper = null;
        }
        this.mContext = null;
    }

    public synchronized boolean insert(DownloadRecord record) {
        boolean result;
        Log.d(TAG, "insert id : " + record.getId());
        Log.d(TAG, "insert status : " + record.getStatus());
        result = true;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mDownloadDBHelper.getWritableDatabase();
            mDatabase.beginTransaction();
            if (mDatabase.insert(DownloadRecordColumns.TABLE_NAME, null, getContentValuesByDownloadRecord(record)) == -1) {
                result = false;
            }
            for (int i = 0; i < record.getMultiInfos().size(); i++) {
                ContentValues valueFilebrokenpoint = new ContentValues();
                valueFilebrokenpoint.put("id", Integer.valueOf(record.getId()));
                valueFilebrokenpoint.put("downloadSize", Long.valueOf(((FileBrokenPoint) record.getMultiInfos().get(i)).getDownloadSize()));
                valueFilebrokenpoint.put(FileBrokenPointColumns.COLUMN_FILEPOSITION, Long.valueOf(((FileBrokenPoint) record.getMultiInfos().get(i)).getFilePosition()));
                valueFilebrokenpoint.put(FileBrokenPointColumns.COLUMN_REQSIZE, Long.valueOf(((FileBrokenPoint) record.getMultiInfos().get(i)).getReqSize()));
                if (mDatabase.insert(FileBrokenPointColumns.TABLE_NAME, null, valueFilebrokenpoint) == -1) {
                    result = false;
                }
            }
            mDatabase.setTransactionSuccessful();
            if (mDatabase != null) {
                try {
                    mDatabase.endTransaction();
                    mDatabase.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mDatabase != null) {
                try {
                    mDatabase.endTransaction();
                    mDatabase.close();
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                try {
                    mDatabase.endTransaction();
                    mDatabase.close();
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
            }
        }
        return result;
    }

    public synchronized boolean update(DownloadRecord record) {
        boolean result;
        Log.d(TAG, "update id : " + record.getId());
        Log.d(TAG, "update status : " + record.getStatus());
        boolean result2 = true;
        SQLiteDatabase mDatabase = this.mDownloadDBHelper.getWritableDatabase();
        mDatabase.beginTransaction();
        ContentValues valueDownloadrecord = getContentValuesByDownloadRecord(record);
        ContentValues contentValues;
        if (record.getMultiInfos() == null || record.getMultiInfos().size() == 0) {
            try {
                contentValues = valueDownloadrecord;
                mDatabase.update(DownloadRecordColumns.TABLE_NAME, contentValues, "id = ?", new String[]{Integer.toString(record.getId())});
                mDatabase.delete(FileBrokenPointColumns.TABLE_NAME, "id = ?", new String[]{Integer.toString(record.getId())});
                mDatabase.setTransactionSuccessful();
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result2 = false;
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                }
            }
            result = result2;
        } else {
            Cursor cursor = null;
            try {
                contentValues = valueDownloadrecord;
                mDatabase.update(DownloadRecordColumns.TABLE_NAME, contentValues, "id = ?", new String[]{Integer.toString(record.getId())});
                cursor = mDatabase.query(FileBrokenPointColumns.TABLE_NAME, new String[]{FileBrokenPointColumns.COLUMN_FBPID}, "id = ?", new String[]{Integer.toString(record.getId())}, null, null, null);
                if (cursor.getCount() > 0) {
                    int minumb = 0;
                    while (cursor.moveToNext() && minumb < record.getMultiInfos().size()) {
                        contentValues = getContentValuesByBrokenPoint((FileBrokenPoint) record.getMultiInfos().get(minumb), record.getId());
                        mDatabase.update(FileBrokenPointColumns.TABLE_NAME, contentValues, "fbpid = ?", new String[]{Integer.toString(cursor.getInt(0))});
                        minumb++;
                    }
                } else {
                    for (int i = 0; i < record.getMultiInfos().size(); i++) {
                        if (mDatabase.insert(FileBrokenPointColumns.TABLE_NAME, null, getContentValuesByBrokenPoint((FileBrokenPoint) record.getMultiInfos().get(i), record.getId())) == -1) {
                            result2 = false;
                        }
                    }
                }
                mDatabase.setTransactionSuccessful();
                if (cursor != null) {
                    cursor.close();
                }
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e2222) {
                        e2222.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                result2 = false;
                if (cursor != null) {
                    cursor.close();
                }
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e22222) {
                        e22222.printStackTrace();
                    }
                }
            } catch (Throwable th2) {
                if (cursor != null) {
                    cursor.close();
                }
                if (mDatabase != null) {
                    try {
                        mDatabase.endTransaction();
                        mDatabase.close();
                    } catch (Exception e222222) {
                        e222222.printStackTrace();
                    }
                }
            }
            result = result2;
        }
        return result;
    }

    private ContentValues getContentValuesByBrokenPoint(FileBrokenPoint point, int id) {
        ContentValues valueFilebrokenpoint = new ContentValues();
        valueFilebrokenpoint.put("id", Integer.valueOf(id));
        valueFilebrokenpoint.put("downloadSize", Long.valueOf(point.getDownloadSize()));
        valueFilebrokenpoint.put(FileBrokenPointColumns.COLUMN_FILEPOSITION, Long.valueOf(point.getFilePosition()));
        valueFilebrokenpoint.put(FileBrokenPointColumns.COLUMN_REQSIZE, Long.valueOf(point.getReqSize()));
        return valueFilebrokenpoint;
    }

    private ContentValues getContentValuesByDownloadRecord(DownloadRecord record) {
        ContentValues valueDownloadrecord = new ContentValues();
        valueDownloadrecord.put("title", record.getTitle());
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_URI, record.getUri());
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_DESTINATION, record.getDestination());
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_DESCRIPTION, record.getDescription());
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_MD5, record.getMd5());
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_MIMETYPE, record.getMimetype());
        valueDownloadrecord.put("status", Integer.valueOf(record.getStatus()));
        valueDownloadrecord.put("downloadSize", Long.valueOf(record.getDownloadSize()));
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_FILESIZE, Long.valueOf(record.getFileSize()));
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_DOWNLOADTIME, Long.valueOf(record.getDownloadTime()));
        if (record.isResumeBroken()) {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISRESUMEBROKEN, Integer.valueOf(1));
        } else {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISRESUMEBROKEN, Integer.valueOf(0));
        }
        valueDownloadrecord.put(DownloadRecordColumns.COLUMN_SPEEDLIMITDEGREE, Integer.valueOf(0));
        if (record.isP2pDownloadError()) {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISP2PDOWNLOADERROR, Integer.valueOf(1));
        } else {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISP2PDOWNLOADERROR, Integer.valueOf(0));
        }
        if (record.isP2PDownload()) {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISP2PDOWNLOAD, Integer.valueOf(1));
        } else {
            valueDownloadrecord.put(DownloadRecordColumns.COLUMN_ISP2PDOWNLOAD, Integer.valueOf(0));
        }
        return valueDownloadrecord;
    }

    public synchronized void removeDownloadRecordByID(int id) {
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mDownloadDBHelper.getWritableDatabase();
            mDatabase.beginTransaction();
            mDatabase.delete(DownloadRecordColumns.TABLE_NAME, "id = ?", new String[]{Integer.toString(id)});
            mDatabase.delete(FileBrokenPointColumns.TABLE_NAME, "id = ?", new String[]{Integer.toString(id)});
            mDatabase.setTransactionSuccessful();
            if (mDatabase != null) {
                mDatabase.endTransaction();
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mDatabase != null) {
                mDatabase.endTransaction();
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                mDatabase.endTransaction();
                mDatabase.close();
            }
        }
    }

    public DownloadRecord findDownloadRecordByID(int id) {
        SQLiteDatabase mReadDatabase = null;
        List<DownloadRecord> records = new ArrayList();
        Cursor cursorDownload = null;
        Cursor cursorFileBrokenPoint = null;
        mReadDatabase = this.mReadDownloadDBHelper.getReadableDatabase();
        cursorDownload = mReadDatabase.query(DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS, "id = ?", new String[]{Integer.toString(id)}, null, null, null);
        cursorFileBrokenPoint = mReadDatabase.query(FileBrokenPointColumns.TABLE_NAME, FileBrokenPointColumns.COLUMNS, "id = ?", new String[]{Integer.toString(id)}, null, null, null);
        if (!(cursorDownload == null || cursorDownload.getCount() == 0)) {
            while (cursorDownload.moveToNext()) {
                try {
                    DownloadRecord record = getDownloadRecordByCursor(cursorDownload);
                    if (!(cursorFileBrokenPoint == null || cursorFileBrokenPoint.getCount() == 0)) {
                        List<FileBrokenPoint> listfbk = new ArrayList();
                        while (cursorFileBrokenPoint.moveToNext()) {
                            listfbk.add(getFileBrokenPointDBByCursor(cursorFileBrokenPoint));
                        }
                        record.setMultiInfos(listfbk);
                    }
                    records.add(record);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (cursorDownload != null) {
                        cursorDownload.close();
                    }
                    if (cursorFileBrokenPoint != null) {
                        cursorFileBrokenPoint.close();
                    }
                    if (mReadDatabase != null) {
                        mReadDatabase.close();
                    }
                } catch (Throwable th) {
                    if (cursorDownload != null) {
                        cursorDownload.close();
                    }
                    if (cursorFileBrokenPoint != null) {
                        cursorFileBrokenPoint.close();
                    }
                    if (mReadDatabase != null) {
                        mReadDatabase.close();
                    }
                }
            }
        }
        if (cursorDownload != null) {
            cursorDownload.close();
        }
        if (cursorFileBrokenPoint != null) {
            cursorFileBrokenPoint.close();
        }
        if (mReadDatabase != null) {
            mReadDatabase.close();
        }
        if (records == null || records.size() == 0) {
            return null;
        }
        return (DownloadRecord) records.get(0);
    }

    public boolean isVaild(int id) {
        SQLiteDatabase mReadDatabase = null;
        Cursor cursorDownload = null;
        boolean result = false;
        try {
            mReadDatabase = this.mReadDownloadDBHelper.getReadableDatabase();
            cursorDownload = mReadDatabase.query(DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS, "id = ?", new String[]{Integer.toString(id)}, null, null, null);
            if (!(cursorDownload == null || cursorDownload.getCount() == 0)) {
                result = true;
            }
            if (cursorDownload != null) {
                cursorDownload.close();
            }
            if (mReadDatabase != null) {
                mReadDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursorDownload != null) {
                cursorDownload.close();
            }
            if (mReadDatabase != null) {
                mReadDatabase.close();
            }
        } catch (Throwable th) {
            if (cursorDownload != null) {
                cursorDownload.close();
            }
            if (mReadDatabase != null) {
                mReadDatabase.close();
            }
        }
        return result;
    }

    public DownloadRecord getDownloadRecordByUri(String uri) {
        SQLiteDatabase mReadDatabase = null;
        List<DownloadRecord> records = new ArrayList();
        Cursor cursorDownload = null;
        Cursor cursorFileBrokenPoint = null;
        mReadDatabase = this.mReadDownloadDBHelper.getReadableDatabase();
        cursorDownload = mReadDatabase.query(DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS, "uri = ?", new String[]{uri}, null, null, null);
        if (!(cursorDownload == null || cursorDownload.getCount() == 0)) {
            while (cursorDownload.moveToNext()) {
                try {
                    DownloadRecord record = getDownloadRecordByCursor(cursorDownload);
                    cursorFileBrokenPoint = mReadDatabase.query(FileBrokenPointColumns.TABLE_NAME, FileBrokenPointColumns.COLUMNS, "id = ?", new String[]{Integer.toString(record.getId())}, null, null, null);
                    if (!(cursorFileBrokenPoint == null || cursorFileBrokenPoint.getCount() == 0)) {
                        List<FileBrokenPoint> listfbk = new ArrayList();
                        while (cursorFileBrokenPoint.moveToNext()) {
                            listfbk.add(getFileBrokenPointDBByCursor(cursorFileBrokenPoint));
                        }
                        record.setMultiInfos(listfbk);
                    }
                    records.add(record);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (cursorDownload != null) {
                        cursorDownload.close();
                    }
                    if (cursorFileBrokenPoint != null) {
                        cursorFileBrokenPoint.close();
                    }
                    if (mReadDatabase != null) {
                        mReadDatabase.close();
                    }
                } catch (Throwable th) {
                    if (cursorDownload != null) {
                        cursorDownload.close();
                    }
                    if (cursorFileBrokenPoint != null) {
                        cursorFileBrokenPoint.close();
                    }
                    if (mReadDatabase != null) {
                        mReadDatabase.close();
                    }
                }
            }
        }
        if (cursorDownload != null) {
            cursorDownload.close();
        }
        if (cursorFileBrokenPoint != null) {
            cursorFileBrokenPoint.close();
        }
        if (mReadDatabase != null) {
            mReadDatabase.close();
        }
        if (records == null || records.size() == 0) {
            return null;
        }
        return (DownloadRecord) records.get(0);
    }

    public List<DownloadRecord> findDownloadRecordByStatus(int status) {
        SQLiteDatabase mReadDatabase = null;
        List<DownloadRecord> records = new ArrayList();
        Cursor cursorDownload = null;
        Cursor cursorFileBrokenPoint = null;
        mReadDatabase = this.mReadDownloadDBHelper.getReadableDatabase();
        cursorDownload = mReadDatabase.query(DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS, "status = ?", new String[]{Integer.toString(status)}, null, null, null);
        if (!(cursorDownload == null || cursorDownload.getCount() == 0)) {
            while (cursorDownload.moveToNext()) {
                DownloadRecord record = getDownloadRecordByCursor(cursorDownload);
                cursorFileBrokenPoint = mReadDatabase.query(FileBrokenPointColumns.TABLE_NAME, FileBrokenPointColumns.COLUMNS, "id = ?", new String[]{Integer.toString(record.getId())}, null, null, null);
                if (!(cursorFileBrokenPoint == null || cursorFileBrokenPoint.getCount() == 0)) {
                    List<FileBrokenPoint> listfbk = new ArrayList();
                    while (cursorFileBrokenPoint.moveToNext()) {
                        try {
                            listfbk.add(getFileBrokenPointDBByCursor(cursorFileBrokenPoint));
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (cursorDownload != null) {
                                cursorDownload.close();
                            }
                            if (cursorFileBrokenPoint != null) {
                                cursorFileBrokenPoint.close();
                            }
                            if (mReadDatabase != null) {
                                mReadDatabase.close();
                            }
                        } catch (Throwable th) {
                            if (cursorDownload != null) {
                                cursorDownload.close();
                            }
                            if (cursorFileBrokenPoint != null) {
                                cursorFileBrokenPoint.close();
                            }
                            if (mReadDatabase != null) {
                                mReadDatabase.close();
                            }
                        }
                    }
                    record.setMultiInfos(listfbk);
                }
                records.add(record);
            }
        }
        if (cursorDownload != null) {
            cursorDownload.close();
        }
        if (cursorFileBrokenPoint != null) {
            cursorFileBrokenPoint.close();
        }
        if (mReadDatabase != null) {
            mReadDatabase.close();
        }
        return records;
    }

    public List<DownloadRecord> findAllDownloadRecord() {
        SQLiteDatabase mReadDatabase = null;
        List<DownloadRecord> records = new ArrayList();
        Cursor cursorDownload = null;
        Cursor cursorFileBrokenPoint = null;
        mReadDatabase = this.mReadDownloadDBHelper.getReadableDatabase();
        cursorDownload = mReadDatabase.query(DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS, null, null, null, null, "status asc");
        if (!(cursorDownload == null || cursorDownload.getCount() == 0)) {
            while (cursorDownload.moveToNext()) {
                DownloadRecord record = getDownloadRecordByCursor(cursorDownload);
                cursorFileBrokenPoint = mReadDatabase.query(FileBrokenPointColumns.TABLE_NAME, FileBrokenPointColumns.COLUMNS, "id = ?", new String[]{Integer.toString(record.getId())}, null, null, null);
                if (!(cursorFileBrokenPoint == null || cursorFileBrokenPoint.getCount() == 0)) {
                    List<FileBrokenPoint> listfbk = new ArrayList();
                    while (cursorFileBrokenPoint.moveToNext()) {
                        try {
                            listfbk.add(getFileBrokenPointDBByCursor(cursorFileBrokenPoint));
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (cursorDownload != null) {
                                cursorDownload.close();
                            }
                            if (cursorFileBrokenPoint != null) {
                                cursorFileBrokenPoint.close();
                            }
                            if (mReadDatabase != null) {
                                mReadDatabase.close();
                            }
                        } catch (Throwable th) {
                            if (cursorDownload != null) {
                                cursorDownload.close();
                            }
                            if (cursorFileBrokenPoint != null) {
                                cursorFileBrokenPoint.close();
                            }
                            if (mReadDatabase != null) {
                                mReadDatabase.close();
                            }
                        }
                    }
                    record.setMultiInfos(listfbk);
                }
                records.add(record);
            }
        }
        if (cursorDownload != null) {
            cursorDownload.close();
        }
        if (cursorFileBrokenPoint != null) {
            cursorFileBrokenPoint.close();
        }
        if (mReadDatabase != null) {
            mReadDatabase.close();
        }
        return records;
    }

    private FileBrokenPoint getFileBrokenPointDBByCursor(Cursor cursorFileBrokenPoint) {
        FileBrokenPoint fbprecord = new FileBrokenPoint();
        fbprecord.setFbpid(cursorFileBrokenPoint.getInt(cursorFileBrokenPoint.getColumnIndex(FileBrokenPointColumns.COLUMN_FBPID)));
        fbprecord.setDownloadSize(cursorFileBrokenPoint.getLong(cursorFileBrokenPoint.getColumnIndex("downloadSize")));
        fbprecord.setFilePosition(cursorFileBrokenPoint.getLong(cursorFileBrokenPoint.getColumnIndex(FileBrokenPointColumns.COLUMN_FILEPOSITION)));
        fbprecord.setReqSize(cursorFileBrokenPoint.getLong(cursorFileBrokenPoint.getColumnIndex(FileBrokenPointColumns.COLUMN_REQSIZE)));
        return fbprecord;
    }

    private DownloadRecord getDownloadRecordByCursor(Cursor cursorDownload) {
        DownloadRecord record = new DownloadRecord();
        record.setId(cursorDownload.getInt(cursorDownload.getColumnIndex("id")));
        record.setTitle(cursorDownload.getString(cursorDownload.getColumnIndex("title")));
        record.setUri(cursorDownload.getString(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_URI)));
        record.setDestination(cursorDownload.getString(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_DESTINATION)));
        record.setDescription(cursorDownload.getString(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_DESCRIPTION)));
        record.setMd5(cursorDownload.getString(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_MD5)));
        record.setMimetype(cursorDownload.getString(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_MIMETYPE)));
        record.setStatus(cursorDownload.getInt(cursorDownload.getColumnIndex("status")));
        record.setDownloadSize(cursorDownload.getLong(cursorDownload.getColumnIndex("downloadSize")));
        record.setDownloadTime(cursorDownload.getLong(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_DOWNLOADTIME)));
        record.setFileSize(cursorDownload.getLong(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_FILESIZE)));
        if (cursorDownload.getInt(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_ISRESUMEBROKEN)) == 0) {
            record.setResumeBroken(false);
        } else {
            record.setResumeBroken(true);
        }
        record.setSpeedLimitDegree(cursorDownload.getInt(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_SPEEDLIMITDEGREE)));
        if (cursorDownload.getInt(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_ISP2PDOWNLOADERROR)) == 0) {
            record.setP2pDownloadError(false);
        } else {
            record.setP2pDownloadError(true);
        }
        if (cursorDownload.getInt(cursorDownload.getColumnIndex(DownloadRecordColumns.COLUMN_ISP2PDOWNLOAD)) == 0) {
            record.setP2PDownload(false);
        } else {
            record.setP2PDownload(true);
        }
        return record;
    }

    public synchronized void resetDownloadRecord(int id) {
        DownloadRecord record = findDownloadRecordByID(id);
        record.setDownloadSize(0);
        record.setDownloadTime(0);
        record.setStatus(0);
        update(record);
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mDownloadDBHelper.getWritableDatabase();
            mDatabase.delete(FileBrokenPointColumns.TABLE_NAME, "id = ?", new String[]{Integer.toString(id)});
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
    }
}
