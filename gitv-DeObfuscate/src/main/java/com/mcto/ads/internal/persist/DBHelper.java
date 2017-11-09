package com.mcto.ads.internal.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gcupid.db";
    private static String DB_PATH = null;
    private static final int DB_VERSION = 1;
    private static final boolean DEBUG_STRICT_READONLY = false;
    private SQLiteDatabase mDatabase;
    private boolean mIsInitializing;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + DB_NAME;
        Log.d("a71_ads_client", "DB_PATH:" + DB_PATH);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("a71_ads_client", "onCreate():");
        createNativeVideoTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("a71_ads_client", "onUpgrade():");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void createNativeVideoTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS native(id INTEGER PRIMARY KEY, identifier TEXT, playType INTEGER, playCount INTEGER, sendRecord INTEGER, lastUpdateTime INTEGER)";
        db.execSQL(sql);
        Log.d("a71_ads_client", "createNativeVideoTable(): sql: " + sql);
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true);
        }
        return databaseLocked;
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        Log.d("a71_ads_client", "getDatabaseLocked():");
        if (this.mDatabase != null) {
            if (!this.mDatabase.isOpen()) {
                this.mDatabase = null;
                Log.w("a71_ads_client", " The user closed the database by calling mDatabase.close()");
            } else if (!(writable && this.mDatabase.isReadOnly())) {
                Log.d("a71_ads_client", " The database is already open for business");
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        SQLiteDatabase db = this.mDatabase;
        try {
            this.mIsInitializing = true;
            if (db == null) {
                db = SQLiteDatabase.openDatabase(DB_PATH, null, 268435472);
                onCreate(db);
            } else if (writable && db.isReadOnly()) {
                Log.w("a71_ads_client", "Get gcupid.db in read-only mode");
            }
        } catch (SQLiteException ex) {
            if (writable) {
                throw ex;
            } else {
                Log.e("a71_ads_client", "Couldn't open gcupid.db for writing (will try read-only):", ex);
                db = SQLiteDatabase.openDatabase(DB_PATH, null, 268435473);
            }
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (!(db == null || db == this.mDatabase)) {
                db.close();
            }
        }
        onOpen(db);
        if (db.isReadOnly()) {
            Log.w("a71_ads_client", "Opened gcupid.db in read-only mode");
        }
        this.mDatabase = db;
        this.mIsInitializing = false;
        if (db == null || db == this.mDatabase) {
            return db;
        }
        db.close();
        return db;
    }
}
