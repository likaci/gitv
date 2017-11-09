package com.gala.video.app.epg.ui.search.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qsearchhistory.db";
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS searchhistory (id integer primary key autoincrement, keyword text, qpid text, type text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE searchhistory RENAME TO _search_old_20150727");
        db.execSQL("CREATE TABLE searchhistory (id integer primary key autoincrement, keyword text, qpid text, type text)");
        db.execSQL("INSERT INTO searchhistory (id, keyword) SELECT id, keyword FROM _search_old_20150727");
        db.execSQL("DROP TABLE _search_old_20150727");
    }
}
