package com.gala.video.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import com.gala.appmanager.GalaAppManager;
import com.gala.appmanager.utils.C0109b;

public class C1586a extends SQLiteOpenHelper {
    private static C1586a f2013a = null;

    public C1586a(Context context, boolean z) {
        super(context, "gala_tv.db", null, GalaAppManager.isLauncher() ? 3 : 2);
        if (z && VERSION.SDK_INT < 11) {
        }
    }

    public static C1586a m1567a(Context context, boolean z) {
        if (f2013a == null) {
            f2013a = new C1586a(context, z);
        }
        return f2013a;
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        C0109b.m236a("DBHelper", "oldVersion=" + oldVersion + ",newVersion=" + newVersion);
        if (oldVersion == 1) {
            db.beginTransaction();
            try {
                db.execSQL("delete from order_list where sequence < 16");
                db.setTransactionSuccessful();
                oldVersion = 2;
            } finally {
                db.endTransaction();
            }
        }
        if (oldVersion == 2) {
            db.beginTransaction();
            try {
                db.execSQL("delete from order_list");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
