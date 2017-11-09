package com.tvos.appmanager.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class AppDBHelper extends SQLiteOpenHelper {
    public static final String[] COLUMNS = new String[]{COLUMN_PKGNAME, COLUMN_APPNAME, COLUMN_APPNAME_PY, COLUMN_APPPATH, "appVersion", COLUMN_APPAUTHOR, COLUMN_APPSIZE, COLUMN_APPINSTALLEDTIME, COLUMN_ISSYSTEMAPP, COLUMN_VERSIONCODE, "status", COLUMN_RUNNINGTIME, "startTime", COLUMN_APPICONDATA};
    public static final String COLUMN_APPAUTHOR = "appAuthor";
    public static final String COLUMN_APPICONDATA = "appIconData";
    public static final String COLUMN_APPINSTALLEDTIME = "appInstalledTime";
    public static final String COLUMN_APPNAME = "appName";
    public static final String COLUMN_APPNAME_PY = "appName_py";
    public static final String COLUMN_APPPATH = "appPath";
    public static final String COLUMN_APPSIZE = "appSize";
    public static final String COLUMN_APPVERSION = "appVersion";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_ISSYSTEMAPP = "isSystemApp";
    public static final String COLUMN_PKGNAME = "pkgName";
    public static final String COLUMN_RUNNINGTIME = "runningTime";
    public static final String COLUMN_STARTTIME = "startTime";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_VERSIONCODE = "appVersionCode";
    private static final String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS appinfo(id INTEGER PRIMARY KEY AUTOINCREMENT ,pkgName VARCHAR UNIQUE NOT NULL ,appName VARCHAR NOT NULL ,appName_py VARCHAR ,appPath VARCHAR ,appVersion VARCHAR ,appAuthor VARCHAR ,appSize BIGINT NOT NULL DEFAULT 0 ,appInstalledTime BIGINT NOT NULL DEFAULT 0 ,isSystemApp INTEGER NOT NULL DEFAULT 0 ,appIconData BLOB ,appVersionCode INTEGER NOT NULL DEFAULT 0 ,status INTEGER NOT NULL DEFAULT 0 ,startTime BITINT NOT NULL DEFAULT 0 ,runningTime BITINT NOT NULL DEFAULT 0 );";
    private static final String DATABASE_NAME = "appmanager.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TABLE_NAME = "appinfo";
    private static final String TAG = "AppDBHelper";
    public static final String TEMP_TABLE_NAME = "temp_appinfo";

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    public void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_APP_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        upgradeDB(db, CREATE_APP_TABLE, TABLE_NAME, COLUMNS);
    }

    private synchronized void upgradeDB(SQLiteDatabase db, String createTableSql, String tableName, String[] columns) {
        Log.d(TAG, "onUpgrade");
        String tempTableName = "temp_" + tableName;
        SQLiteDatabase sQLiteDatabase = db;
        Cursor tableCursor = sQLiteDatabase.query("sqlite_master", null, "type = ? and name = ?", new String[]{"table", tableName}, null, null, null);
        if (tableCursor.getCount() > 0) {
            db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tempTableName);
            Log.d(TAG, "rename");
            db.execSQL(createTableSql);
            Log.d(TAG, "onCreate");
            Cursor cursor = db.query(tempTableName, null, null, null, null, null, null);
            String[] columnNames = cursor.getColumnNames();
            try {
                tableCursor.close();
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        } else {
            db.execSQL(createTableSql);
            Log.d(TAG, "onCreate");
        }
    }
}
