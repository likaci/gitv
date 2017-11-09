package com.tvos.downloadmanager.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadDBHelper extends SQLiteOpenHelper {
    private static final String CREATE_DOWNLOADRECORD_TABLE = "CREATE TABLE IF NOT EXISTS downloadrecord(id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,title VARCHAR ,uri VARCHAR ,destination VARCHAR ,description VARCHAR ,md5 VARCHAR ,mimetype VARCHAR ,status INTEGER ,downloadSize INTEGER ,fileSize INTEGER ,speedLimitDegree INTEGER ,downloadTime INTEGER ,isResumeBroken INTEGER ,isP2pDownloadError INTEGER ,isP2PDownload INTEGER);";
    private static final String CREATE_FILEBROKENPOINT_TABLE = "CREATE TABLE IF NOT EXISTS filebrokenpoint(fbpid INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,id INTEGER ,downloadSize INTEGER,filePosition INTEGER,reqSize INTEGER);";
    private static final int DATABASE_VERSION = 9;
    private static final String TAG = "DownloadDBHelper";
    private static boolean mTmpDirSet = false;
    private Context mContext;

    public DownloadDBHelper(Context context, String dbName) {
        super(context, dbName, null, 9);
        Log.d(TAG, "construction , dbName = " + dbName);
        this.mContext = context;
        if (VERSION.SDK_INT >= 18) {
            setWriteAheadLoggingEnabled(true);
        } else {
            getWritableDatabase().enableWriteAheadLogging();
        }
    }

    public void onCreate(SQLiteDatabase db) throws SQLException {
        Log.d(TAG, "onCreate, db is " + db.getPath());
        db.execSQL(CREATE_DOWNLOADRECORD_TABLE);
        db.execSQL(CREATE_FILEBROKENPOINT_TABLE);
    }

    public synchronized void close() {
        super.close();
        this.mContext = null;
    }

    public SQLiteDatabase getReadableDatabase() {
        if (!(mTmpDirSet || this.mContext == null)) {
            try {
                String tmpFileDir = "/data/data/" + this.mContext.getPackageName() + "/databases/main";
                new File(tmpFileDir).mkdirs();
                super.getReadableDatabase().execSQL("PRAGMA temp_store_directory='" + tmpFileDir + "'");
                mTmpDirSet = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.getReadableDatabase();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        Log.d(TAG, "onUpgrade, db is " + db.getPath() + " , oldVersion = " + oldVersion + " , newVersion = " + newVersion);
        copyDbData(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onDowngrade, db is " + db.getPath() + " , oldVersion = " + oldVersion + " , newVersion = " + newVersion);
        copyDbData(db);
    }

    private void copyDbData(SQLiteDatabase db) {
        copyTableData(db, CREATE_DOWNLOADRECORD_TABLE, DownloadRecordColumns.TABLE_NAME, DownloadRecordColumns.COLUMNS);
        copyTableData(db, CREATE_FILEBROKENPOINT_TABLE, FileBrokenPointColumns.TABLE_NAME, FileBrokenPointColumns.COLUMNS);
    }

    private synchronized void copyTableData(SQLiteDatabase db, String createTableSql, String tableName, String[] columns) {
        Log.d(TAG, "copyDbData");
        String tempTableName = "temp_" + tableName;
        db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tempTableName);
        Log.d(TAG, "rename");
        db.execSQL(createTableSql);
        Log.d(TAG, "onCreate");
        String[] columnNames = db.query(tempTableName, null, null, null, null, null, null).getColumnNames();
        List<String> matchedColumns = new ArrayList();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < columnNames.length; i++) {
            int j = 0;
            while (j < columns.length) {
                if (columnNames[i].equals(columns[j])) {
                    if (!matchedColumns.contains(columnNames[i])) {
                        matchedColumns.add(columnNames[i]);
                    }
                } else {
                    j++;
                }
            }
        }
        for (int k = 0; k < matchedColumns.size(); k++) {
            if (k != matchedColumns.size() - 1) {
                builder.append(new StringBuilder(String.valueOf((String) matchedColumns.get(k))).append(", ").toString());
            } else {
                builder.append(new StringBuilder(String.valueOf((String) matchedColumns.get(k))).append(" ").toString());
            }
        }
        db.execSQL("INSERT INTO " + tableName + "(" + builder.toString() + ") SELECT " + builder.toString() + " FROM " + tempTableName);
        Log.d(TAG, "copy data");
        db.execSQL("DROP TABLE IF EXISTS " + tempTableName + ";");
        Log.d(TAG, "delete tmp");
    }
}
