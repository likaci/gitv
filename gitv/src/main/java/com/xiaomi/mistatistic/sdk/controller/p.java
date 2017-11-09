package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class p extends SQLiteOpenHelper {
    public static final Object a = new Object();

    public p(Context context) {
        super(context, "mistat.db", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        synchronized (a) {
            sQLiteDatabase.execSQL(String.format("create table %s(_id integer primary key autoincrement, category text, ts integer, key text, value text, type text, extra text)", new Object[]{"mistat_event"}));
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
