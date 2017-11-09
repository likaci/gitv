package com.gala.video.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import com.gala.appmanager.GalaAppManager;
import com.gala.appmanager.utils.b;

public class a extends SQLiteOpenHelper {
    private static a a = null;

    public a(Context context, boolean z) {
        super(context, "gala_tv.db", null, GalaAppManager.isLauncher() ? 3 : 2);
        if (z && VERSION.SDK_INT < 11) {
        }
    }

    public static a a(Context context, boolean z) {
        if (a == null) {
            a = new a(context, z);
        }
        return a;
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        b.a("DBHelper", "oldVersion=" + oldVersion + ",newVersion=" + newVersion);
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
