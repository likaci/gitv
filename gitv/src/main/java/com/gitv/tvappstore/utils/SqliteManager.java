package com.gitv.tvappstore.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class SqliteManager {
    private SoftSQLiteOpenHelper mOpenHelper;

    class SoftSQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String APPSTORE_DB = "appstore.db";

        public SoftSQLiteOpenHelper(Context context) {
            super(context, APPSTORE_DB, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table appstore(_id integer primary key, packagename varchar(50));");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public SqliteManager(Context context) {
        this.mOpenHelper = new SoftSQLiteOpenHelper(context);
    }

    public void insert(String packageName) {
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("insert into appstore(packagename) values(?);", new Object[]{packageName});
            db.close();
        }
    }

    public void delete(String packageName) {
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("delete from appstore where packagename = ?;", new Object[]{packageName});
            db.close();
        }
    }

    public int queryCount() {
        int count = 0;
        SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from appstore;", null);
            count = cursor.getCount();
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
            db.close();
        }
        return count;
    }

    public List<String> queryAll() {
        List<String> softList = null;
        SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select packagename from appstore;", null);
            if (cursor == null || cursor.getCount() <= 0) {
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                db.close();
            } else {
                softList = new ArrayList();
                while (cursor.moveToNext()) {
                    softList.add(cursor.getString(0));
                }
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
                db.close();
            }
        }
        return softList;
    }
}
