package com.gala.video.lib.share.system.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class BaseContentProvider extends ContentProvider {
    private static final String TAG = "SYSTEM/contentprovider/BaseContentProvider";
    private static String tableName = "preference";
    private DBOpenHelper dbOpenHelper;

    public static class DBOpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String KEY = "key";
        private static final String MODULE = "module";
        private static final String VALUE = "value";

        public DBOpenHelper(Context context, String database) {
            super(context, database, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + BaseContentProvider.tableName + " (" + MODULE + " varchar(50)," + "key" + " varchar(50) unique," + "value" + " varchar(80)," + "CONSTRAINT unique_name UNIQUE (MODULE, KEY) ON CONFLICT REPLACE" + ")");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BaseContentProvider.tableName);
            onCreate(db);
        }
    }

    public boolean onCreate() {
        this.dbOpenHelper = new DBOpenHelper(getContext(), tableName);
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.dbOpenHelper.getReadableDatabase().query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Uri insertUri = ContentUris.withAppendedId(uri, this.dbOpenHelper.getWritableDatabase().insert(tableName, null, values));
        getContext().getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return this.dbOpenHelper.getWritableDatabase().delete(tableName, selection, selectionArgs);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return this.dbOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }
}
